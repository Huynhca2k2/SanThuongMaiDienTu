package com.chh.shoponline.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.chh.shoponline.Adapter.PopularListAdapter2;
import com.chh.shoponline.Adapter.WishListAdapter;
import com.chh.shoponline.Domain.Category;
import com.chh.shoponline.Domain.PopularDomain;
import com.chh.shoponline.Domain.User;
import com.chh.shoponline.Helper.FirebaseManager;
import com.chh.shoponline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class WishlistActivity extends AppCompatActivity {
    private ImageView backBtn;
    private RecyclerView.Adapter adapterPupolar2;
    private RecyclerView recyclerViewPupolar2;
    private FirebaseManager firebase = new FirebaseManager();
    private ArrayList<PopularDomain> items = new ArrayList<>();
    private User myUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

        init();
        getBundle();
        getWishListFromFirebase();
        initRecyclerview2();
    }

    private void getBundle(){

    }

    private void initRecyclerview2() {
        recyclerViewPupolar2 = findViewById(R.id.view9);
        recyclerViewPupolar2.setLayoutManager(new GridLayoutManager(this, 2));
        adapterPupolar2 = new WishListAdapter(items);
        recyclerViewPupolar2.setAdapter(adapterPupolar2);
    }

    private void init(){
        backBtn = findViewById(R.id.backBtnWishlist);

        backBtn.setOnClickListener(view -> startActivity(new Intent(WishlistActivity.this, MainActivity.class)));
    }

    private void getWishListFromFirebase(){
        firebase.addObserver(new Observer<ArrayList<PopularDomain>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

            @Override
            public void onNext(ArrayList<PopularDomain> productList) {
                items = productList;

                //cap nhat giao dien
                initRecyclerview2();
                adapterPupolar2.notifyDataSetChanged();
            }

            @Override
            public void onError(Throwable e) {
                // Xử lý lỗi
            }

            @Override
            public void onComplete() {

            }
        });

        firebase.fetchWishListFromFirebase().subscribe();
    }
}