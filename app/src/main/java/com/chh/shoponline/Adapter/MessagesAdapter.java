package com.chh.shoponline.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.chh.shoponline.Activity.ChatActivity;
import com.chh.shoponline.Activity.MainActivity;
import com.chh.shoponline.R;
import com.chh.shoponline.Domain.Messages;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder>{

    private List<Messages> messages;
    private final Context context;

    public MessagesAdapter(List<Messages> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_chat_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {
        Messages list2 = messages.get(position);
        Glide.with(this.context).load(list2.getPicUrl()).error(R.drawable.img_default).into(holder.profilePic);

        holder.name.setText(list2.getName());
        holder.lastMessage.setText(list2.getLast_msg());

        if(list2.getUn_seen_msg() == 0){
            holder.unseenMessages.setVisibility(View.GONE);
            holder.lastMessage.setTextColor(Color.parseColor("#959595"));
        }else {
            holder.unseenMessages.setVisibility(View.VISIBLE);
            holder.unseenMessages.setText(list2.getUn_seen_msg() + "");
            holder.lastMessage.setTextColor(context.getResources().getColor(R.color.theme_color_80));
        }

        holder.rootLayout.setOnClickListener(view -> {
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra("id_user", list2.getIdUser());
            intent.putExtra("name", list2.getName());
            intent.putExtra("picUrl", list2.getPicUrl());
            intent.putExtra("id_chat", list2.getId_chat());
            intent.putExtra("time_chat", list2.getTime_chat());

            //set lai thoi gian xem tin nhan moi nhat
            setTimeLast(list2.getId_chat(), list2.getIdUser());
            //set 0 lai cho so tin nhan chua doc khi click vao message
            setUnSeenMsgMy(list2.getIdUser());

            context.startActivity(intent);

        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView profilePic, profilePic2;
        private TextView name, lastMessage, unseenMessages;
        private LinearLayout rootLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic2 = itemView.findViewById(R.id.profilePic2);
            profilePic = itemView.findViewById(R.id.profilePic);
            name = itemView.findViewById(R.id.name);
            lastMessage = itemView.findViewById(R.id.lastMessage);
            unseenMessages = itemView.findViewById(R.id.unseenMessages);
            rootLayout = itemView.findViewById(R.id.root_layout);
        }
    }

    private void setTimeLast(Long id_chat, String idUser){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("chats/" + id_chat +"/messages");
        DatabaseReference myRef2 = database.getReference("users/" + MainActivity.getMyUser().getId() + "/messages/" + idUser);
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

    private void setUnSeenMsgMy(String idUser){
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef = database.getReference("users/" + MainActivity.getMyUser().getId() + "/messages/" + idUser);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
                myRef.child("un_seen_msg").setValue(0);
            }
            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

            }
        });

    }
}
