package com.chh.shoponline.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chh.shoponline.Adapter.PopularListAdapter2;
import com.chh.shoponline.Domain.Category;
import com.chh.shoponline.Domain.PopularDomain;
import com.chh.shoponline.Helper.FirebaseManager;
import com.chh.shoponline.R;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class CategoryActivity extends AppCompatActivity {
    private Category cateItem;
    private TextView cateName;
    private ImageView backBtn;
    private RecyclerView.Adapter adapterPupolar2;
    private RecyclerView recyclerViewPupolar2;
    private FirebaseManager firebase = new FirebaseManager();
    private ArrayList<PopularDomain> productOfCate = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        init();
        getBundle();
        initRecyclerview2();
        getListProductFromFirebase();

    }

    private void getBundle(){
        cateItem = (Category) getIntent().getSerializableExtra("cateItem");

        cateName.setText(cateItem.getNameCate());
    }

    private void initRecyclerview2() {
        recyclerViewPupolar2 = findViewById(R.id.view9);
        recyclerViewPupolar2.setLayoutManager(new GridLayoutManager(this, 2));
        adapterPupolar2 = new PopularListAdapter2(productOfCate);
        recyclerViewPupolar2.setAdapter(adapterPupolar2);
    }

    private void init(){
        cateName = findViewById(R.id.cateName);
        backBtn = findViewById(R.id.backBtnCate);

        backBtn.setOnClickListener(view -> finish());
    }

    private void getListProductFromFirebase(){
        firebase.addObserver(new Observer<ArrayList<PopularDomain>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

            @Override
            public void onNext(ArrayList<PopularDomain> productList) {

                for(int i = 0; i< productList.size(); i++){
                    System.out.println("id cart " + productList.get(i).getCart_id());
                    if(productList.get(i).getCart_id() == cateItem.getId())
                        productOfCate.add(productList.get(i));
                }
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

        firebase.fetchProductListFromFirebase().subscribe();
    }
}