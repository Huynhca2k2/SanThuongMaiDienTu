package com.chh.shoponline.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.chh.shoponline.Adapter.MessagesAdapter;
import com.chh.shoponline.Domain.MessagesList;
import com.chh.shoponline.Domain.User;
import com.chh.shoponline.Helper.FirebaseManager;
import com.chh.shoponline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesActivity extends AppCompatActivity {
    private CircleImageView userProfilePic;
    private List<MessagesList> messages = new ArrayList<>();
    private RecyclerView messagesRecyclerView;
    private MessagesAdapter messagesAdapter;
    private long getMessageKey = 0;
    private String getIdUser, getName, getProfilePic;
    private String lastMsg;
    private User myUser = new User();
    private Long getIdChat, getTimeChat;
    private int unSeenMgs = 0;
    private String myIdUserLocal = MainActivity.getMyUser().getId();
    private FirebaseManager firebase = new FirebaseManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        init();
        getBundle();
        initRecyclerview();
        getListMessageFromFirebase();
    }
    private void init(){
        userProfilePic = findViewById(R.id.userProfilePic);
        messagesRecyclerView = findViewById(R.id.messageRecyclerView);
    }

    private void initRecyclerview() {
        messagesRecyclerView.setHasFixedSize(true);
        messagesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set adapter recyclerView
        messagesAdapter = new MessagesAdapter(messages, MessagesActivity.this);
        messagesRecyclerView.setAdapter(messagesAdapter);
    }

    private void getBundle(){
        Glide.with(this).load(MainActivity.getMyUser().getPicUrl()).error(R.drawable.img_default).into(userProfilePic);
    }

    private void getListMessageFromFirebase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("users/" + MainActivity.getMyUser().getId() + "/messages/");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                    messages.clear();

                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        getIdUser = dataSnapshot.getKey();
                        getName = dataSnapshot.child("name").getValue(String.class);
                        getProfilePic = dataSnapshot.child("picUrl").getValue(String.class);
                        getIdChat = dataSnapshot.child("id_chat").getValue(Long.class);
                        lastMsg = dataSnapshot.child("last_msg").getValue(String.class);
                        unSeenMgs = dataSnapshot.child("un_seen_msg").getValue(Integer.class);
                        getTimeChat = dataSnapshot.child("time_chat").getValue(Long.class);

                        messages.add(new MessagesList(getName, getIdUser, lastMsg, getProfilePic, unSeenMgs, getIdChat, getTimeChat));
                    }
                    initRecyclerview();

                }
                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                }
            });
    }

}