package com.chh.shoponline.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chh.shoponline.Adapter.ChatAdapter;
import com.chh.shoponline.Domain.ChatList;
import com.chh.shoponline.Domain.MessagesList;
import com.chh.shoponline.Helper.FirebaseManager;
import com.chh.shoponline.R;

import com.chh.shoponline.Adapter.MessagesAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class ChatActivity extends AppCompatActivity {

    private String getName, lastMessage, lastTime;
    private Long id_chat, timeChat;
    private int unseenMessage;
    private RecyclerView chattingRecyclerView;
    private List<ChatList> chats = new ArrayList<>();
    private ChatAdapter chatAdapter;
    private TextView nameTv;
    private String getIdUser;
    private ImageView backBtn;
    private EditText messageEdt;
    private CircleImageView profilePic;
    private ImageView sendBtn;
    private  long getLastSeenMessage;
    private FirebaseManager firebase = new FirebaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        getBundle();
        initRecyclerview();
        getListChatFromFirebase();
    }

    private void init(){
        backBtn = findViewById(R.id.backBtn);
        nameTv = findViewById(R.id.name);
        messageEdt = findViewById(R.id.messageEdt);
        profilePic = findViewById(R.id.profilePic2);
        sendBtn = findViewById(R.id.sendBtn);
    }

    private void initRecyclerview() {
        chattingRecyclerView = findViewById(R.id.chattingRecyclerView);
        chattingRecyclerView.setHasFixedSize(true);
        chattingRecyclerView.setLayoutManager(new LinearLayoutManager(ChatActivity.this));
        chatAdapter = new ChatAdapter(chats, ChatActivity.this);
        chattingRecyclerView.setAdapter(chatAdapter);
    }

    private void getBundle(){
        getName = getIntent().getStringExtra("name");
        String getPicUrl = getIntent().getStringExtra("picUrl");
        id_chat = getIntent().getLongExtra("id_chat", 0);
        getIdUser = getIntent().getStringExtra("id_user");
        timeChat = getIntent().getLongExtra("time_chat", 0);
        nameTv.setText(getName);
        Glide.with(this).load(getPicUrl).error(R.drawable.img_default).into(profilePic);

        sendBtn.setOnClickListener(view -> {
            sendChat();
            //clear edit text
            messageEdt.setText("");
        });

        backBtn.setOnClickListener(view -> finish());
    }

    private void getListChatFromFirebase(){
        firebase.addChatObserver(new Observer<ArrayList<ChatList>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

            @Override
            public void onNext(@NonNull ArrayList<ChatList> chatLists) {
                // lay list chat
                chats = chatLists;

                //cap nhat giao dien
                initRecyclerview();
                chatAdapter.notifyDataSetChanged();
                chattingRecyclerView.scrollToPosition(chatLists.size() - 1);
            }

            @Override
            public void onError(Throwable e) {
                // Xử lý lỗi
            }
            @Override
            public void onComplete() {

            }
        });

        firebase.fetchChatListFromFirebase(id_chat, getName).subscribe();
    }

    private void sendChat() {

        String getTxtMessage = messageEdt.getText().toString();
        if (!getTxtMessage.isEmpty()) {
            //get current timestamps
            String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("chats/" + id_chat + "/messages/" + currentTimestamp);

            myRef.child("id_user").setValue(MainActivity.getMyUser().getId());
            myRef.child("msg").setValue(getTxtMessage);

            setTimeLast(id_chat, getIdUser);
            setLastMessages(id_chat, getIdUser);
            setUnSeenMsg(id_chat, getIdUser);
        }else
            Toast.makeText(this, "Enter messages!", Toast.LENGTH_SHORT).show();

    }

    private void setLastMessages(Long id_chat, String idUser){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chats/" + id_chat +"/messages");
        DatabaseReference myRef1 = database.getReference("users/" + idUser.trim() + "/messages/ " + MainActivity.getMyUser().getId());
        DatabaseReference myRef2 = database.getReference("users/" + MainActivity.getMyUser().getId().trim() + "/messages/ " + idUser.trim());
        System.out.println("users/" + MainActivity.getMyUser().getId().trim() + "/messages/ " + idUser.trim() + "day la set last mess");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                String getLastMessages = "";

                for(DataSnapshot chatDataSnapshot : snapshot.getChildren()){
                    getLastMessages = chatDataSnapshot.child("msg").getValue(String.class);
                }
                //set last message cho 2 user
                myRef2.child("last_msg").setValue(getLastMessages);
                myRef1.child("last_msg").setValue(getLastMessages);
            }
            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });
    }

    private void setUnSeenMsg(Long id_chat, String idUser){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef1 = database.getReference("users/" + idUser.trim() + "/messages/ " + MainActivity.getMyUser().getId());
        DatabaseReference myRef2 = database.getReference("chats/" + id_chat +"/messages");

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                Long timeChat1 = snapshot.child("time_chat").getValue(Long.class);
                myRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                        int unSeenMsg = 0;

                        if (!idUser.trim().equals(MainActivity.getMyUser().getId())){
                            for(DataSnapshot chatDataSnapshot : snapshot.getChildren()){
                                long getChatKey = Long.parseLong(chatDataSnapshot.getKey());
                                if(getChatKey > timeChat1){
                                    unSeenMsg++;
                                }
                                System.out.println(unSeenMsg + " un seen msg");
                            }

                            myRef1.child("un_seen_msg").setValue(unSeenMsg);
                        }

                    }
                    @Override
                    public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                    }
                });
            }
            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });

    }

    private void setTimeLast(Long id_chat, String idUser){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chats/" + id_chat +"/messages");
        DatabaseReference myRef2 = database.getReference("users/" + MainActivity.getMyUser().getId().trim() + "/messages/ " + idUser.trim());
        System.out.println("users/" + MainActivity.getMyUser().getId().trim() + "/messages/ " + idUser.trim() + "day la set time last");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                long getMessageKey = 0;

                for(DataSnapshot chatDataSnapshot : snapshot.getChildren()){
                    getMessageKey = Long.parseLong(chatDataSnapshot.getKey());
                }
                myRef2.child("time_chat").setValue(getMessageKey);

            }
            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });
    }



}