package com.chh.shoponline.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chh.shoponline.Domain.Product;
import com.chh.shoponline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    ArrayList<Product> listItemSelected;

    public CartListAdapter(ArrayList<Product> listItemSelected) {
        this.listItemSelected = listItemSelected;
    }

    @NonNull
    @Override
    public CartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {
        holder.title.setText(listItemSelected.get(position).getTitle());
        holder.feeEachItem.setText("$" + listItemSelected.get(position).getPrice());
        holder.totalEachItem.setText("$" + Math.round(listItemSelected.get(position).getQuantity()) * listItemSelected.get(position).getPrice());
        holder.num.setText(String.valueOf(listItemSelected.get(position).getQuantity()));
        Glide.with(holder.itemView.getContext()).load(listItemSelected.get(position).getPicUrl()).error(R.drawable.pic1).into(holder.pic);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String pathObject = String.valueOf(listItemSelected.get(position).getId());
        DatabaseReference myRef = database.getReference("users/" + auth.getUid() + "/carts/" + pathObject + "/quantity");
        DatabaseReference myRef2 = database.getReference("users/" + auth.getUid() + "/carts/");
        DatabaseReference myRef3 = database.getReference("users/" + auth.getUid() + "/numCart");

        holder.plusItem.setOnClickListener(view -> {

            int addQuan = listItemSelected.get(position).getQuantity() + 1;
            myRef.setValue(addQuan);
        });

        holder.minusItem.setOnClickListener(view -> {
            if(listItemSelected.get(position).getQuantity() > 1){
                int minusQuan = listItemSelected.get(position).getQuantity() - 1;
                myRef.setValue(minusQuan);
            }else{
                myRef2.child(pathObject).removeValue();
                myRef3.setValue(listItemSelected.size() - 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItemSelected.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title, feeEachItem, plusItem, minusItem;
        ImageView pic;
        TextView totalEachItem, num;
        public ViewHolder(@NonNull View itemView){
            super(itemView);

            title = itemView.findViewById(R.id.titleTxt);
            pic = itemView.findViewById(R.id.pic);
            feeEachItem = itemView.findViewById(R.id.feeEachItem);
            totalEachItem = itemView.findViewById(R.id.totalEachItem);
            plusItem = itemView.findViewById(R.id.pludCartBtn);
            minusItem = itemView.findViewById(R.id.minusCartBtn);
            num = itemView.findViewById(R.id.numberItemTxt);
        }
    }

}
