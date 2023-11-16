package com.chh.shoponline.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chh.shoponline.Adapter.CartListAdapter;
import com.chh.shoponline.Domain.Order;
import com.chh.shoponline.Domain.Product;
import com.chh.shoponline.Helper.FirebaseManager;
import com.chh.shoponline.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private Button btnOrder;
    private TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt, emptyTxt;
    private double tax;
    private ScrollView scrollView;
    private ImageView backBtn;
    private static int numCart = 0;
    private ArrayList<Product> items = new ArrayList<>();
    private FirebaseManager firebase = new FirebaseManager();

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        intView();
        setVariavle();
        getListCartFromFirebase();
    }
    private void initList() {

        recyclerView = findViewById(R.id.view3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new CartListAdapter(items);
        recyclerView.setAdapter(adapter);

        if (items.isEmpty()) {
            emptyTxt.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else {
            emptyTxt.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }

    public double getTotalFee(){
        double fee = 0;
        for (int i = 0; i < items.size(); i++){
            fee += items.get(i).getPrice() * items.get(i).getQuantity();
        }
        return fee;
    }

    private void calculateCart() {
        double percentTax = 0.02;
        double delivery = 10;
        double fee = getTotalFee();

        tax = Math.round((fee * percentTax * 100.0)) / 100.0;
        double total = Math.round((fee + tax + delivery) * 100) / 100;
        double itemTotal = Math.round(fee * 100) / 100;

        totalFeeTxt.setText("$" + itemTotal);
        taxTxt.setText("$" + tax);
        deliveryTxt.setText("$" + delivery);
        totalTxt.setText("$" + total);
    }

    private void setVariavle() {
        backBtn.setOnClickListener(view -> startActivity(new Intent(CartActivity.this, MainActivity.class)));

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(items.size() + "sizzzzzzz");
                if(items.isEmpty())
                    return;
                createBill();
            }
        });
    }

    private void createBill(){
        System.out.println(items.size() + "sizzzzzzz");
        String id_user, id_shop, address_user, address_shop, credit;
        String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 9);
        String date;
        int id_product, quantity;
        boolean status;
        Double price;

        id_user = MainActivity.getMyUser().getId();
        address_user = MainActivity.getMyUser().getAddress();
        credit = MainActivity.getMyUser().getCredit();
        status = false;

        long timestampInSeconds = Long.parseLong(currentTimestamp);
        Timestamp timestamp = new Timestamp(timestampInSeconds * 1000);
        Date dateNow = new Date(timestamp.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("orders");
        DatabaseReference userRef = database.getReference("users/" + MainActivity.getMyUser().getId() +"/order_list");

        for (int i = 0; i < items.size(); i++){
            String pathObject = currentTimestamp + i +"";
            id_shop = items.get(i).getUser().getId();
            address_shop = items.get(i).getUser().getAddress();
            id_product = items.get(i).getId();
            quantity = items.get(i).getQuantity();
            price = (items.get(i).getPrice());
            date = simpleDateFormat.format(dateNow) + " " + simpleTimeFormat.format(dateNow);
            DatabaseReference shopRef = database.getReference("users/" + id_shop + "/order_list");

            //id lay luon gia tri la thoi gian tao hoa don
            Order order = new Order(pathObject, id_user, id_shop, date, id_product, status, quantity, address_user, address_shop, credit, price);
            myRef.child(pathObject).setValue(order);
            //tao hoa don cho user
            userRef.child(pathObject).child("id_user").setValue(pathObject);
            //tao hoa don cho shop owner
            shopRef.child(pathObject).child("id_user").setValue(pathObject);

        }

        startActivity(new Intent(CartActivity.this, OrderActivity.class));
        finish();
        // sau khi lap hoa don thi xoa cart di
        removeCarts();

    }

    private void removeCarts(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + MainActivity.getMyUser().getId() + "/carts");
        myRef.removeValue();
    }

//    private void getCarts(){
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("users/" + MainActivity.getMyUser().getId() +"/carts");
//        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {
//
//                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
//
//                }
//            }
//            @Override
//            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
//
//            }
//        });
//    }

    private void intView() {
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        taxTxt = findViewById(R.id.taxTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt = findViewById(R.id.totalTxt);
        recyclerView = findViewById(R.id.view3);
        scrollView = findViewById(R.id.scrollView3);
        backBtn = findViewById(R.id.backBtn);
        emptyTxt = findViewById(R.id.emptyTxt);
        btnOrder = findViewById(R.id.btnOrder);
    }

    private void getListCartFromFirebase(){
        firebase.addObserver(new Observer<ArrayList<Product>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

            @Override
            public void onNext(ArrayList<Product> productList) {
                // lay list product
                items = productList;
                initList();
                calculateCart();
                //cap nhat giao dien
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                // Xử lý lỗi
            }

            @Override
            public void onComplete() {

            }
        });

        firebase.fetchCartListFromFirebase().subscribe();
    }
}