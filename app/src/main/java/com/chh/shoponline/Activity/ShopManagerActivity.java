package com.chh.shoponline.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.WindowCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chh.shoponline.Domain.User;
import com.chh.shoponline.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ShopManagerActivity extends AppCompatActivity {
    private ImageView imageAvatar;
    private TextView nameTxt, gmailTxt;
    private LinearLayout salesBtn;
    private String myUid, photoUrl;
    private AppCompatButton btnSignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_shop_manager);

        initUi();
        initListener();
    }

    private void initListener() {
        salesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(ShopManagerActivity.this, SalesActivity.class));
            }
        });
    }

    private void initUi() {
        imageAvatar = findViewById(R.id.imgAvatar);
        //nameTxt = findViewById(R.id.nameProTxt);
        gmailTxt = findViewById(R.id.gmailProTxt);
        //btnUri = findViewById(R.id.btnUid);
        salesBtn = findViewById(R.id.sales);
    }

}