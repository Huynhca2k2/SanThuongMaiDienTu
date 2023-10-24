package com.chh.shoponline.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chh.shoponline.Activity.ChatContentActivity;
import com.chh.shoponline.Domain.Chat;
import com.chh.shoponline.Domain.Review;
import com.chh.shoponline.R;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.Viewholder> {

    ArrayList<Chat> items;
    Context context;

    public ChatListAdapter(ArrayList<Chat> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ChatListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_chat_list, parent, false);
        context = parent.getContext();

        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.Viewholder holder, int position) {
        holder.nameYouTxt.setText(items.get(position).getUser().getName());
        holder.timeChat.setText(items.get(position).getTimeChat());
        holder.contentNowTxt.setText(items.get(position).getContentChat());

        Glide.with(holder.itemView.getContext()).load(items.get(position).getUser().getPicUrl()).error(R.drawable.pic1).into(holder.picYou);

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), ChatContentActivity.class);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView nameYouTxt, timeChat, contentNowTxt;
        ImageView picYou;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            nameYouTxt = itemView.findViewById(R.id.nameYouTxt);
            timeChat = itemView.findViewById(R.id.timeChat);
            contentNowTxt = itemView.findViewById(R.id.contentNowTxt);
            picYou = itemView.findViewById(R.id.picYou);
        }
    }
}
