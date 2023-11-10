package com.chh.shoponline.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.chh.shoponline.Adapter.ReviewListAdapter;
import com.chh.shoponline.Domain.Product;
import com.chh.shoponline.Domain.Review;
import com.chh.shoponline.R;

import java.util.ArrayList;

public class ReviewsActivity extends AppCompatActivity {
    private ArrayList<Review> reviews = new ArrayList<>();
    private Product productItem ;
    private TextView reviewsTxt, scoreRatingTxt;
    private ImageView backBtnReview;
    private RecyclerView.Adapter adapterReview;
    private RecyclerView recyclerViewReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews2);

        init();
        getBundle();
        initRecyclerviewReview();
    }

    private void init(){
        reviewsTxt = findViewById(R.id.reviewsTxt);
        scoreRatingTxt = findViewById(R.id.scoreRatingTxt);
        backBtnReview = findViewById(R.id.backBtnReview);
    }
    private void getBundle() {
        reviews = (ArrayList<Review>) getIntent().getSerializableExtra("reviewList");
        productItem = (Product) getIntent().getSerializableExtra("productItem");

        reviewsTxt.setText(""+reviews.size());
        scoreRatingTxt.setText("" + productItem.getScore());

        backBtnReview.setOnClickListener(view -> finish());
    }

    private void initRecyclerviewReview() {

        recyclerViewReview = findViewById(R.id.viewReviewMain);
        recyclerViewReview.setLayoutManager(new GridLayoutManager(this, 1));
        adapterReview = new ReviewListAdapter(reviews);
        recyclerViewReview.setAdapter(adapterReview);
    }
}