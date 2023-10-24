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
import com.chh.shoponline.Activity.DetailActivity;
import com.chh.shoponline.Domain.PopularDomain;
import com.chh.shoponline.Domain.Review;
import com.chh.shoponline.R;

import java.util.ArrayList;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.Viewholder> {

    ArrayList<Review> items;
    Context context;

    public ReviewListAdapter(ArrayList<Review> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ReviewListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_review_list, parent, false);
        context = parent.getContext();

        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewListAdapter.Viewholder holder, int position) {
        holder.nameUserTxt.setText(items.get(position).getUser().getName());
        holder.timeTxt.setText(items.get(position).getTime());
        holder.scoreTxt2.setText("" + items.get(position).getScore());
        holder.contentTxt.setText(items.get(position).getContent());

        System.out.println("url pic "+ items.get(position).getUser().getPicUrl());
        Glide.with(holder.itemView.getContext()).load(items.get(position).getUser().getPicUrl()).error(R.drawable.pic1).into(holder.picUser);
        Glide.with(holder.itemView.getContext()).load(items.get(position).getPicUrl()).error(R.drawable.pic2).into(holder.picReview);

//        holder.itemView.setOnClickListener(view -> {
//            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
//            holder.itemView.getContext().startActivity(intent);
//        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView nameUserTxt, scoreTxt2, timeTxt, contentTxt;
        ImageView picUser, picReview;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            nameUserTxt = itemView.findViewById(R.id.nameUserTxt);
            timeTxt = itemView.findViewById(R.id.timeTxt);
            scoreTxt2 = itemView.findViewById(R.id.scoreTxt2);
            contentTxt = itemView.findViewById(R.id.contentTxt);
            picUser = itemView.findViewById(R.id.picUser);
            picReview = itemView.findViewById(R.id.picReview);
        }
    }
}
