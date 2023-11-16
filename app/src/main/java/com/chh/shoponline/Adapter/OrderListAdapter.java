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
import com.chh.shoponline.Activity.MainActivity;
import com.chh.shoponline.Domain.Order;
import com.chh.shoponline.Domain.Product;
import com.chh.shoponline.R;

import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.Viewholder> {

    ArrayList<Order> items;
    Context context;
    public OrderListAdapter(ArrayList<Order> items) {
        this.items = items;
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView idOrder, addressShop,
                dateSend, nameShopTxt,
                nameUserOrder, addressUser,
                nameProOrder, creditTxt,
                price1Txt, quantity1Txt,
                statusOrderTxt, totalPriceOrder;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            idOrder = itemView.findViewById(R.id.idOrder);
            dateSend = itemView.findViewById(R.id.dateSend);
            addressShop = itemView.findViewById(R.id.addressShop);
            nameShopTxt = itemView.findViewById(R.id.nameShopTxt);
            nameUserOrder = itemView.findViewById(R.id.nameUserOrder);
            addressUser = itemView.findViewById(R.id.addressUser);
            nameProOrder = itemView.findViewById(R.id.nameProOrder);
            creditTxt = itemView.findViewById(R.id.creditTxt);
            price1Txt = itemView.findViewById(R.id.price1Txt);
            quantity1Txt = itemView.findViewById(R.id.quantity1Txt);
            statusOrderTxt = itemView.findViewById(R.id.statusOrderTxt);
            totalPriceOrder = itemView.findViewById(R.id.totalPriceOrder);

        }
    }

    @NonNull
    @Override
    public OrderListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_order_list, parent, false);
        context = parent.getContext();

        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListAdapter.Viewholder holder, int position) {
        Double total = items.get(position).getPrice() * items.get(position).getQuantity();
        holder.idOrder.setText(items.get(position).getId());
        holder.dateSend.setText(items.get(position).getDate());
        holder.addressShop.setText(items.get(position).getAddress_shop());
        holder.nameShopTxt.setText(items.get(position).getId_shop());
        holder.nameUserOrder.setText(MainActivity.getMyUser().getName() + "");
        holder.addressUser.setText(items.get(position).getAddress_user());
        holder.nameProOrder.setText("Iphone 15 Pro max 525Gb");
        holder.creditTxt.setText(items.get(position).getCredit());
        holder.price1Txt.setText(String.valueOf(items.get(position).getPrice()));
        holder.quantity1Txt.setText(items.get(position).getQuantity() + "");
        holder.statusOrderTxt.setText("Waiting");
        holder.totalPriceOrder.setText("$ "+total);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}
