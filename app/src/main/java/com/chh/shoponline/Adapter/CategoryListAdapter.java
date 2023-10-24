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

import com.chh.shoponline.Activity.CategoryActivity;
import com.chh.shoponline.Domain.Category;
import com.chh.shoponline.R;

import java.util.ArrayList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.Viewholder> {

    ArrayList<Category> items;
    Context context;

    public CategoryListAdapter(ArrayList<Category> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cate_list, parent, false);
        context = parent.getContext();

        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryListAdapter.Viewholder holder, int position) {
        holder.nameCate.setText(items.get(position).getNameCate());
        holder.imgCate.setImageResource(items.get(position).getIdRes());

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), CategoryActivity.class);
            intent.putExtra("cateItem", items.get(position));
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView nameCate;
        ImageView imgCate;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            nameCate = itemView.findViewById(R.id.nameCate);
            imgCate = itemView.findViewById(R.id.imgCate);
        }
    }
}
