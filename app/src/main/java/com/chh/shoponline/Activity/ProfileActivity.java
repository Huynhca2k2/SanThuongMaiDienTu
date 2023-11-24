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

public class ProfileActivity extends AppCompatActivity {
    private ImageView imageAvatar;
    private TextView nameTxt, gmailTxt;
    private LinearLayout btnUri, btnShopManager;
    private String myUid, photoUrl;
    private AppCompatButton btnSignOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), false);
        setContentView(R.layout.activity_profile);

        initUi();
        showUserInformation();
        initListener();
    }

    private void initListener() {
        btnSignOut.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
        //btnUri.setOnClickListener(view -> Toast.makeText(ProfileActivity.this,"UID : " + myUid, Toast.LENGTH_SHORT).show());

    }

    private void initUi() {
        imageAvatar = findViewById(R.id.imgAvatar);
        //nameTxt = findViewById(R.id.nameProTxt);
        gmailTxt = findViewById(R.id.gmailProTxt);
        //btnUri = findViewById(R.id.btnUid);
        btnSignOut = findViewById(R.id.btnSignout);
        btnShopManager = findViewById(R.id.btnShopManager);
    }

    private void showUserInformation(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null)
            return;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users/" + user.getUid());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User myUser = snapshot.getValue(User.class);
                photoUrl = myUser.getPicUrl();
                String email = user.getEmail();
                //String uid = user.getUid();

                gmailTxt.setText(email);
                Glide.with(getApplicationContext()).load(photoUrl).error(R.drawable.img_default).into(imageAvatar);
//                myUid = uid;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "connect error !!!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}