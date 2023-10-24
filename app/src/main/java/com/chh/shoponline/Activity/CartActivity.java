package com.chh.shoponline.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chh.shoponline.Adapter.CartListAdapter;
import com.chh.shoponline.Domain.PopularDomain;
import com.chh.shoponline.Helper.FirebaseManager;
import com.chh.shoponline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private TextView totalFeeTxt, taxTxt, deliveryTxt, totalTxt, emptyTxt;
    private double tax;
    private ScrollView scrollView;
    private ImageView backBtn;
    private static int numCart = 0;
    private ArrayList<PopularDomain> items = new ArrayList<>();
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
    }

    private void intView() {
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        taxTxt = findViewById(R.id.taxTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        totalTxt = findViewById(R.id.totalTxt);
        recyclerView = findViewById(R.id.view3);
        scrollView = findViewById(R.id.scrollView3);
        backBtn = findViewById(R.id.backBtn);
        emptyTxt = findViewById(R.id.emptyTxt);
    }

    private void getListCartFromFirebase(){
        firebase.addObserver(new Observer<ArrayList<PopularDomain>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

            @Override
            public void onNext(ArrayList<PopularDomain> productList) {
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