package com.chh.shoponline.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.chh.shoponline.Adapter.ChatListAdapter;
import com.chh.shoponline.Adapter.ReviewListAdapter;
import com.chh.shoponline.Domain.Chat;
import com.chh.shoponline.Domain.User;
import com.chh.shoponline.R;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterChat;
    private RecyclerView recyclerViewChat;
    private TextView nameYouTxt, contentNowTxt, timeChat;
    private ImageView picYou;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        init();
        initRecyclerviewChat();
    }

    private void initRecyclerviewChat() {
        recyclerViewChat = findViewById(R.id.viewChat);
        recyclerViewChat.setLayoutManager(new GridLayoutManager(this, 1));
        adapterChat = new ChatListAdapter(getListChat());
        recyclerViewChat.setAdapter(adapterChat);
    }

    private ArrayList<Chat> getListChat(){
        User user = new User("abc", "go vap", "thanh toan", "Cai Hoang Huynh", "user_role", "picuser");
        ArrayList<Chat> chats = new ArrayList<>();
        chats.add(new Chat(1, user, "xin chao ban1!!!", "11/12"));
        chats.add(new Chat(2, user, "xin chao ban2!!!", "12/12"));
        chats.add(new Chat(3, user, "xin chao ban3!!!", "13/12"));
        chats.add(new Chat(4, user, "xin chao ban4!!!", "14/12"));

        return chats;
    }
    private void init(){
        nameYouTxt = findViewById(R.id.nameYouTxt);
        contentNowTxt = findViewById(R.id.contentNowTxt);
        timeChat = findViewById(R.id.timeChat);
        picYou = findViewById(R.id.picYou);
    }
}