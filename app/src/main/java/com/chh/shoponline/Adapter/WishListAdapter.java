package com.chh.shoponline.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.chh.shoponline.Activity.MainActivity;
import com.chh.shoponline.Domain.PopularDomain;
import com.chh.shoponline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.Viewholder> {

    ArrayList<PopularDomain> items;
    Context context;

    public WishListAdapter(ArrayList<PopularDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public WishListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_pop_list_2, parent, false);
        context = parent.getContext();

        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull WishListAdapter.Viewholder holder, int position) {
        holder.titleTxt.setText(items.get(position).getTitle());
        holder.feeTxt.setText("$" + items.get(position).getPrice());
        holder.scoreTxt.setText("" + items.get(position).getScore());
        holder.reviewTxt.setText("" + items.get(position).getReview());

        Glide.with(holder.itemView.getContext()).load(items.get(position).getPicUrl()).error(R.drawable.pic1).into(holder.pic);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String pathObject = String.valueOf(items.get(position).getId());
        DatabaseReference myRef = database.getReference("users/" + auth.getUid() + "/wishlist");

        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), DetailActivity.class);
            intent.putExtra("object", items.get(position));
            holder.itemView.getContext().startActivity(intent);
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Hiển thị AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setMessage("Do you want to remove product?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Xử lý khi nhấn Yes
                                myRef.child(pathObject).removeValue();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Xử lý khi nhấn No

                            }
                        });
                // Tạo và hiển thị AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();

                return true; // Trả về true để ngăn sự kiện click tiếp tục lan truyền
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView titleTxt, feeTxt, scoreTxt, reviewTxt;
        ImageView pic;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            titleTxt = itemView.findViewById(R.id.titleTxt);
            feeTxt = itemView.findViewById(R.id.feeTxt);
            scoreTxt = itemView.findViewById(R.id.scoreTxt);
            reviewTxt = itemView.findViewById(R.id.reviewTxt);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}
