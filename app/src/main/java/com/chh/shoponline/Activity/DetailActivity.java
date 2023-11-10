package com.chh.shoponline.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chh.shoponline.Adapter.PopularListAdapter2;
import com.chh.shoponline.Adapter.ReviewListAdapter;
import com.chh.shoponline.Domain.PopularDomain;
import com.chh.shoponline.Domain.Review;
import com.chh.shoponline.Domain.User;
import com.chh.shoponline.Helper.FirebaseManager;
import com.chh.shoponline.R;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

public class DetailActivity extends AppCompatActivity {
    public static final String TAG = MainActivity.class.getName();
    private static final int MY_REQUEST_CODE = 10;
    private RecyclerView.Adapter adapterReview, adapterProduct;
    private RecyclerView recyclerViewReview, recyclerViewProduct , view11;
    private LinearLayout layoutReview;
    private Button addToCartBtn, btnSendReview;
    private TextView titleTxt, feeTxt, descriptionTxt, reviewTxt, scoreTxt, seeAllReview;
    private EditText reviewEdt, scoreEdt;
    private ImageView picItem, backBtn, btnLike, imageUpload, cancelButton, chatBtn;
    private PopularDomain object;
    private int numberOrder = 1;
    private User myUser = MainActivity.getMyUser();
    private FirebaseManager firebase = new FirebaseManager();
    private ArrayList<PopularDomain> productList = new ArrayList<>();
    private ArrayList<Review> reviewList = new ArrayList<>();
    private Review review = new Review();
    private String currentTimestamp;
    private Uri selectedImage;
    private ActivityResultLauncher<Intent> mActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.e(TAG, "onActivityResult");
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if(data == null){
                            return;
                        }
                        selectedImage = data.getData();
                        //uploadImageToFirebase(selectedImage);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                            imageUpload.setImageBitmap(bitmap);
                        }catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_detail);
        FirebaseApp.initializeApp(this);

        initView();
        getBundle();
        initRecyclerviewReview();
        initRecyclerview2();
        getListProductFromFirebase();
        getUser();
        getListReviewFromFirebase();

    }

    private void getBundle(){
        object = (PopularDomain) getIntent().getSerializableExtra("object");

        Glide.with(this).load(object.getPicUrl()).error(R.drawable.pic1).into(picItem);
        titleTxt.setText(object.getTitle());
        feeTxt.setText("$" + object.getPrice());
        descriptionTxt.setText(object.getDescription());
        reviewTxt.setText(object.getReview() + "");
        scoreTxt.setText(object.getScore() + "");

        addToCartBtn.setOnClickListener(view -> {
            //neu chua dang nhap khi mua thi chuyen sang login
            nextActivity();
            object.setQuantity(numberOrder);

            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users/" + auth.getUid() + "/carts");
            DatabaseReference myRef2 = database.getReference("users/" + auth.getUid() + "/numCart");

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int quantityCart = 0;
                    if (productList != null){
                        productList.clear();
                    }
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        quantityCart += 1;
                    }
                    System.out.println("product size " + productList.size());
                    myRef2.setValue(quantityCart);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            String pathObject = String.valueOf(object.getId());
            myRef.child(pathObject).setValue((object));

            //sau khi click them san pham vao gio hang thi sang trang thanh toan
            startActivity(new Intent(DetailActivity.this, CartActivity.class));finish();
        });

        btnLike.setOnClickListener(view -> {

            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users/" + auth.getUid() + "/wishlist");

            String pathObject = String.valueOf(object.getId());
            myRef.child(pathObject).setValue((object));

            Toast.makeText(this, "Add to wishlist success!!!", Toast.LENGTH_SHORT).show();
        });

        backBtn.setOnClickListener(view -> startActivity(new Intent(DetailActivity.this, MainActivity.class)));

        layoutReview.setOnClickListener(view -> showBottomDialog());

        chatBtn.setOnClickListener(view -> {
            createChatBox();

        });
    }

    private void createChatBox(){
        String shopId = object.getIdUser();
        Long timeChat = 0L;
        shopId = " X1T9mPKs7ISh4AW1Xw5BJvjk3792";
        int unSeen = 0;
        currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //tao chat box cho minh
        DatabaseReference myRef = database.getReference("users/" + MainActivity.getMyUser().getId() + "/messages/" + shopId);
        System.out.println("users/" + MainActivity.getMyUser().getId() + "/messages/" + shopId);
        myRef.child("id_chat").setValue(Long.parseLong(currentTimestamp));
        myRef.child("last_msg").setValue("");
        myRef.child("name").setValue("shop demo");
        myRef.child("picUrl").setValue("hinh shop");
        myRef.child("time_chat").setValue(timeChat);
        myRef.child("un_seen_msg").setValue(unSeen);

        //tao chat bot cho shop
        DatabaseReference shopRef = database.getReference("users/" + shopId.trim() + "/messages/ " + MainActivity.getMyUser().getId());
        System.out.println("users/" + shopId.trim() + "/messages/ " + MainActivity.getMyUser().getId());
        shopRef.child("id_chat").setValue(Long.parseLong(currentTimestamp));
        shopRef.child("last_msg").setValue("");
        shopRef.child("name").setValue("sinh tien");
        shopRef.child("picUrl").setValue("hinh 1");
        shopRef.child("time_chat").setValue(timeChat);
        shopRef.child("un_seen_msg").setValue(unSeen);

        Intent intent = new Intent(DetailActivity.this, MessagesActivity.class);

        startActivity(intent);
    }

    private void initRecyclerviewReview() {
        ArrayList<Review> review2 = new ArrayList<>();
        if(!reviewList.isEmpty()){
            view11.setVisibility(View.VISIBLE);
            scoreTxt.setText("" + object.getScore());
            reviewTxt.setText(""+ reviewList.size());
            if(reviewList.size() == 1){
                review2.add(reviewList.get(0));
            }else {
                review2.add(reviewList.get(reviewList.size() - 1));
                review2.add(reviewList.get(reviewList.size() - 2));
            }

        }else {
            scoreTxt.setText("0");
            reviewTxt.setText("0");
            view11.setVisibility(View.GONE);
        }
        recyclerViewReview = findViewById(R.id.view11);
        recyclerViewReview.setLayoutManager(new GridLayoutManager(this, 1));
        adapterReview = new ReviewListAdapter(review2);
        recyclerViewReview.setAdapter(adapterReview);
    }

    private void initRecyclerview2() {
        recyclerViewProduct = findViewById(R.id.view10);
        recyclerViewProduct.setLayoutManager(new GridLayoutManager(this, 2));
        adapterProduct = new PopularListAdapter2(productList);
        recyclerViewProduct.setAdapter(adapterProduct);
    }

    private void initView() {
        addToCartBtn = findViewById(R.id.addToCartBtn);
        feeTxt = findViewById(R.id.priceTxt);
        titleTxt = findViewById(R.id.titleTxt);
        descriptionTxt = findViewById(R.id.descriptionTxt);
        picItem = findViewById(R.id.picItem);
        reviewTxt = findViewById(R.id.reviewDetailTxt);
        scoreTxt = findViewById(R.id.scoreDetailTxt);
        backBtn = findViewById(R.id.backBtn);
        btnLike = findViewById(R.id.btnLike);
        layoutReview = findViewById(R.id.layoutReview);
        seeAllReview = findViewById(R.id.seeAllReview);
        view11 = findViewById(R.id.view11);
        chatBtn = findViewById(R.id.chatBtn);
    }

    private void nextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getListProductFromFirebase(){
        firebase.addObserver(new Observer<ArrayList<PopularDomain>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {}

            @Override
            public void onNext(ArrayList<PopularDomain> items) {
                // lay list product
                productList = items;

                //cap nhat giao dien
                initRecyclerview2();
                adapterProduct.notifyDataSetChanged();
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

    private void getListReviewFromFirebase(){
        firebase.addReviewObserver(new Observer<ArrayList<Review>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull ArrayList<Review> reviews) {
                reviewList = reviews;

                seeAllReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DetailActivity.this, ReviewsActivity.class);
                        intent.putExtra("reviewList",(Serializable) reviewList);
                        intent.putExtra("productItem", object);
                        startActivity(intent);
                    }
                });
                initRecyclerviewReview();
                adapterReview.notifyDataSetChanged();
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

        firebase.fetchReviewListFromFirebase(object.getId()).subscribe();
    }

    private void getUser(){
        firebase.addUserObserver(new Observer<User>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.rxjava3.annotations.NonNull User user) {
                myUser.setName(user.getName());
                myUser.setId(user.getId());
                myUser.setPicUrl(user.getPicUrl());

            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        firebase.fetchCurrentUserFromFirebase().subscribe();
    }

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        cancelButton = dialog.findViewById(R.id.cancelButton);
        reviewEdt = dialog.findViewById(R.id.reviewEdt);
        scoreEdt = dialog.findViewById(R.id.scoreEdt);
        imageUpload = dialog.findViewById(R.id.imageUpload);
        btnSendReview = dialog.findViewById(R.id.btnSendReview);

        cancelButton.setOnClickListener(view -> dialog.dismiss());
        onClickDialog();

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void onClickDialog(){

        imageUpload.setOnClickListener(view -> onClickRequestPermission());

        btnSendReview.setOnClickListener(view -> {

            uploadImageToFirebase(selectedImage);
        });
    }
    private void onClickRequestPermission(){
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            openGallery();
            return;
        }

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            openGallery();
        }else{
            String [] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permission, MY_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == MY_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openGallery();
            }
        }
    }

    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        mActivityResultLauncher.launch(Intent.createChooser(intent, "Select Picture"));
    }

    private void uploadImageToFirebase(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        String imageName = "images/" + UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageRef.child(imageName);
        String currentTimestamp = String.valueOf(System.currentTimeMillis()).substring(0, 10);

        long timestampInSeconds = Long.parseLong(currentTimestamp);
        Timestamp timestamp = new Timestamp(timestampInSeconds * 1000);
        Date date = new Date(timestamp.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    System.out.println("down load suscess");
                    // Upload thành công
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        review.setPicUrl(uri.toString());
                        review.setId(Integer.parseInt(currentTimestamp));
                        review.setContent(reviewEdt.getText().toString().trim());
                        review.setScore(Integer.parseInt(String.valueOf(scoreEdt.getText())));
                        review.setTime(simpleDateFormat.format(date) + " " + simpleTimeFormat.format(date));
                        review.setUser(myUser);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("products/" + object.getId() + "/reviews");

                        String pathObject = String.valueOf(review.getId());
                        myRef.child(pathObject).setValue(review, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(DetailActivity.this, "Comment suscess!!!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    });
                })
                .addOnFailureListener(e -> {
                    // Upload thất bại
                    e.printStackTrace();
                });
    }

}