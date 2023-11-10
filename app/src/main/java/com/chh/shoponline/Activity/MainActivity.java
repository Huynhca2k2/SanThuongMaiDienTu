package com.chh.shoponline.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.chh.shoponline.Adapter.CategoryListAdapter;
import com.chh.shoponline.Adapter.PopularListAdapter;
import com.chh.shoponline.Adapter.PopularListAdapter2;
import com.chh.shoponline.Domain.Category;
import com.chh.shoponline.Domain.Photo;
import com.chh.shoponline.Domain.PopularDomain;
import com.chh.shoponline.Domain.User;
import com.chh.shoponline.Helper.FirebaseManager;
import com.chh.shoponline.R;
import com.chh.shoponline.Adapter.PhotoViewPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import me.relex.circleindicator.CircleIndicator3;

public class MainActivity extends AppCompatActivity {
    private static final long AUTO_SCROLL_DELAY = 3000;
    private RecyclerView.Adapter adapterPupolar, adapterPupolar2, adapterPupolarCate;
    private RecyclerView recyclerViewPupolar, recyclerViewPupolar2, recyclerViewPupolarCate;
    private ViewPager2 mviewPager2;
    private CircleIndicator3 mcircleIndicator3;
    private List<Photo> mListPhoto;
    private ArrayList<Category> cateLisrt = getListCate();
    private LinearLayout homeBtn, cartBtn, profileBtn, wishlistBtn, messageBtn;
    private ImageView redImage;
    private TextView numberCart;
    private ConstraintLayout layoutLoading;
    private ScrollView scrollMain;
    private CoordinatorLayout coordinatorLayout;
    private FirebaseManager firebase = new FirebaseManager();
    private ArrayList<PopularDomain> items = new ArrayList<>();
    private static User myUser = new User();

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_main);

        init();

        scrollMain.setVisibility(View.GONE);
        coordinatorLayout.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.VISIBLE);

        bottom_navigation();
        getUser();
        initRecyclerviewPhoto();
        initRecyclerviewCate();
        getListProductFromFirebase();

    }
    private void startAutoScroll() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                int currentItem = mviewPager2.getCurrentItem();
                if (currentItem < mviewPager2.getAdapter().getItemCount() - 1) {
                    mviewPager2.setCurrentItem(currentItem + 1);
                } else {
                    mviewPager2.setCurrentItem(0);
                }
                handler.postDelayed(this, AUTO_SCROLL_DELAY);
            }
        };
        handler.postDelayed(runnable, AUTO_SCROLL_DELAY);
    }
    private void init(){
        mviewPager2 = findViewById(R.id.viewPager);
        mcircleIndicator3 = findViewById(R.id.circle_indicator);
        homeBtn = findViewById(R.id.homeBtn);
        cartBtn = findViewById(R.id.cartBtn);
        profileBtn = findViewById(R.id.profileBtn);
        redImage = findViewById(R.id.redImage);
        numberCart = findViewById(R.id.numberCart);
        layoutLoading = findViewById(R.id.layoutLoading);
        scrollMain = findViewById(R.id.scrollMain);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        wishlistBtn = findViewById(R.id.wishlistBtn);
        messageBtn = findViewById(R.id.messageBtn);
    }

    private List<Photo> getListPhoto() {
        List<Photo> list = new ArrayList<>();
        list.add(new Photo(R.drawable.carousel1));
        list.add(new Photo(R.drawable.carousel2));
        list.add(new Photo(R.drawable.carousel3));
        list.add(new Photo(R.drawable.carousel4));
        list.add(new Photo(R.drawable.carousel5));
        list.add(new Photo(R.drawable.carousel6));
        list.add(new Photo(R.drawable.carousel7));
        list.add(new Photo(R.drawable.carousel8));
        return list;
    }

    private ArrayList<Category> getListCate(){
        ArrayList<Category> list = new ArrayList<>();
        list.add(new Category(1, "Shopee Thời Trang", R.drawable.dm1));
        list.add(new Category(2, "ShopeeFood - Giảm 99.000Đ", R.drawable.dm2));
        list.add(new Category(3, "Khung Giờ Săn Sale", R.drawable.dm3));
        list.add(new Category(4, "Shopee Làm Đẹp", R.drawable.dm4));
        list.add(new Category(5, "Shopee Live Giảm 50%", R.drawable.dm5));
        list.add(new Category(6, "Miễn Hết Phí Ship Cho Mọi Đơn", R.drawable.dm6));
        list.add(new Category(7, "Voucher Giảm Đến 500.000Đ", R.drawable.dm7));
        list.add(new Category(8, "Khách Hàng Thân Thiết", R.drawable.dm8));
        list.add(new Category(9, "Hàng Hiệu Outlet Giảm 50%", R.drawable.dm9));
        list.add(new Category(10, "ShopeePay Gần Bạn", R.drawable.dm10));

        return list;
    }

    private void bottom_navigation() {
        homeBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, MainActivity.class)));
        cartBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
        profileBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ProfileActivity.class)));
        wishlistBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, WishlistActivity.class)));
        messageBtn.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, MessagesActivity.class)));
    }

    private void initRecyclerviewPhoto(){
        mListPhoto = getListPhoto();
        PhotoViewPagerAdapter adapter = new PhotoViewPagerAdapter(mListPhoto);
        mviewPager2.setAdapter(adapter);
        mcircleIndicator3.setViewPager(mviewPager2);
        startAutoScroll();
    }

    private void initRecyclerview() {
        recyclerViewPupolar = findViewById(R.id.view1);
        recyclerViewPupolar.setLayoutManager(new LinearLayoutManager(
                this, LinearLayoutManager.HORIZONTAL, false));
        adapterPupolar = new PopularListAdapter(items);
        recyclerViewPupolar.setAdapter(adapterPupolar);

        if (myUser == null || myUser.getNumCart() == 0) {
            numberCart.setVisibility(View.GONE);
            redImage.setVisibility(View.GONE);
        }else{
            numberCart.setText(""+ myUser.getNumCart());
            numberCart.setVisibility(View.VISIBLE);
            redImage.setVisibility(View.VISIBLE);
        }
    }

    private void initRecyclerview2() {
        recyclerViewPupolar2 = findViewById(R.id.view9);
        recyclerViewPupolar2.setLayoutManager(new GridLayoutManager(this, 2));
        adapterPupolar2 = new PopularListAdapter2(items);
        recyclerViewPupolar2.setAdapter(adapterPupolar2);
    }

    private void initRecyclerviewCate() {
        recyclerViewPupolarCate = findViewById(R.id.viewCate);
        recyclerViewPupolarCate.setLayoutManager(new GridLayoutManager(this, 2 , GridLayoutManager.HORIZONTAL, false));
        adapterPupolarCate = new CategoryListAdapter(cateLisrt);
        recyclerViewPupolarCate.setAdapter(adapterPupolarCate);
    }

    private void getUser(){
        firebase.addUserObserver(new Observer<User>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull User user) {
                myUser = user;
                initRecyclerview();
                initRecyclerview2();
                adapterPupolar.notifyDataSetChanged();
                scrollMain.setVisibility(View.VISIBLE);
                coordinatorLayout.setVisibility(View.VISIBLE);
                layoutLoading.setVisibility(View.GONE);
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        firebase.fetchCurrentUserFromFirebase().subscribe();
    }

    private void getListProductFromFirebase(){
        firebase.addObserver(new Observer<ArrayList<PopularDomain>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

            @Override
            public void onNext(ArrayList<PopularDomain> productList) {
                // lay list product
                items = productList;

                //cap nhat giao dien
                initRecyclerview();
                initRecyclerview2();
                adapterPupolar.notifyDataSetChanged();
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

    public static User getMyUser(){
        return myUser;
    }
}
