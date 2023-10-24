package com.chh.shoponline.Helper;

import androidx.annotation.NonNull;

import com.chh.shoponline.Domain.PopularDomain;
import com.chh.shoponline.Domain.Review;
import com.chh.shoponline.Domain.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

public class FirebaseManager {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    //lay danh sach product
    private ArrayList<Observer<ArrayList<PopularDomain>>> observers = new ArrayList<>();
    public void addObserver(Observer<ArrayList<PopularDomain>> observer) {
        observers.add(observer);
    }

    private void notifyObservers(ArrayList<PopularDomain> productList) {
        for (Observer<ArrayList<PopularDomain>> observer : observers) {
            observer.onNext(productList);
        }
    }

    public Observable<ArrayList<PopularDomain>> fetchProductListFromFirebase() {
        return Observable.create(emitter -> {
            DatabaseReference myRef = database.getReference("products");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<PopularDomain> productList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        PopularDomain popularDomain = dataSnapshot.getValue(PopularDomain.class);
                        productList.add(popularDomain);
                    }
                    notifyObservers(productList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    //lay list product cua cart
    public Observable<ArrayList<PopularDomain>> fetchCartListFromFirebase() {
        return Observable.create(emitter -> {
            DatabaseReference myRef = database.getReference("users/" + auth.getUid() + "/carts");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<PopularDomain> productList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        PopularDomain popularDomain = dataSnapshot.getValue(PopularDomain.class);
                        productList.add(popularDomain);
                    }
                    notifyObservers(productList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    //lay listreview

    private ArrayList<Observer<ArrayList<Review>>> reviewObservers = new ArrayList<Observer<ArrayList<Review>>>();
    public void addReviewObserver(Observer<ArrayList<Review>> robserver) {
        reviewObservers.add(robserver);
    }
    private void notifyReviewObservers(ArrayList<Review> reviewList) {
        for (Observer<ArrayList<Review>> robserver : reviewObservers) {
            robserver.onNext(reviewList);
        }
    }

    public Observable<ArrayList<Review>> fetchReviewListFromFirebase(int idProduct) {
        return Observable.create(emitter -> {
            DatabaseReference myRef = database.getReference("products/" + idProduct + "/reviews");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Review> reviewList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        System.out.println("day la id user" + dataSnapshot.getValue(Review.class).getTime());
                        Review review = dataSnapshot.getValue(Review.class);
                        reviewList.add(review);
                    }
                    notifyReviewObservers(reviewList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    //lay wishlist cua user
    public Observable<ArrayList<PopularDomain>> fetchWishListFromFirebase() {
        return Observable.create(emitter -> {
            DatabaseReference myRef = database.getReference("users/" + auth.getUid() + "/wishlist");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<PopularDomain> productList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        PopularDomain popularDomain = dataSnapshot.getValue(PopularDomain.class);
                        productList.add(popularDomain);
                    }
                    notifyObservers(productList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

    //lay user hien tai
    public Observable<User> fetchCurrentUserFromFirebase() {
        return Observable.create(emitter -> {
            DatabaseReference myRef = database.getReference("users/" + auth.getUid());
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    User myUser = snapshot.getValue(User.class);
                    notifyUserObservers(myUser);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    emitter.onError(error.toException());
                }
            });
        });
    }

    private ArrayList<Observer<User>> userObservers = new ArrayList<>();

    public void addUserObserver(Observer<User> observer) {
        userObservers.add(observer);
    }

    private void notifyUserObservers(User myUser) {
        for (Observer<User> observer : userObservers) {
            observer.onNext(myUser);
        }
    }


}

