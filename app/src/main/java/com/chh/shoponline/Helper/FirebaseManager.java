package com.chh.shoponline.Helper;

import androidx.annotation.NonNull;

import com.chh.shoponline.Domain.ChatList;
import com.chh.shoponline.Domain.Product;
import com.chh.shoponline.Domain.Review;
import com.chh.shoponline.Domain.User;
import com.google.firebase.auth.FirebaseAuth;
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

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;

public class FirebaseManager {

    private String getIdUser, getName, getProfilePic;
    private String idUserChat, message, date, time, lastMsg;
    private Long getIdChat, getTimeChat;
    private int unSeenMgs = 0;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    //lay danh sach product
    private ArrayList<Observer<ArrayList<Product>>> observers = new ArrayList<>();
    public void addObserver(Observer<ArrayList<Product>> observer) {
        observers.add(observer);
    }

    private void notifyObservers(ArrayList<Product> productList) {
        for (Observer<ArrayList<Product>> observer : observers) {
            observer.onNext(productList);
        }
    }

    public Observable<ArrayList<Product>> fetchProductListFromFirebase() {
        return Observable.create(emitter -> {
            DatabaseReference myRef = database.getReference("products");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Product> productList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        productList.add(product);
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
    public Observable<ArrayList<Product>> fetchCartListFromFirebase() {
        return Observable.create(emitter -> {
            DatabaseReference myRef = database.getReference("users/" + auth.getUid() + "/carts");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Product> productList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        productList.add(product);
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
    public Observable<ArrayList<Product>> fetchWishListFromFirebase() {
        return Observable.create(emitter -> {
            DatabaseReference myRef = database.getReference("users/" + auth.getUid() + "/wishlist");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<Product> productList = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        Product product = dataSnapshot.getValue(Product.class);
                        productList.add(product);
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
            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
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


    //lay danh sach chat
    private ArrayList<Observer<ArrayList<ChatList>>> ChatObservers = new ArrayList<>();
    public void addChatObserver(Observer<ArrayList<ChatList>> cObserver) {
        ChatObservers.add(cObserver);
    }

    private void notifyChatObservers(ArrayList<ChatList> chatLists) {
        for (Observer<ArrayList<ChatList>> cObserver : ChatObservers) {
            cObserver.onNext(chatLists);
        }
    }

    public Observable<ArrayList<ChatList>> fetchChatListFromFirebase(Long idChat, String nameChat) {
        return Observable.create(emitter -> {
            DatabaseReference myRef = database.getReference("chats/" + idChat +"/messages/");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    ArrayList<ChatList> chatLists = new ArrayList<>();

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                        String messageTimestamps = dataSnapshot.getKey();

                        long timestampInSeconds = Long.parseLong(messageTimestamps);

                        idUserChat = dataSnapshot.child("id_user").getValue(String.class);
                        message = dataSnapshot.child("msg").getValue(String.class);

                        getIdChat = dataSnapshot.child("id_chat").getValue(Long.class);

                        Timestamp timestamp = new Timestamp(timestampInSeconds * 1000);
                        Date date = new Date(timestamp.getTime());
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm aa", Locale.getDefault());

                        ChatList chatList = new ChatList(idUserChat, nameChat, message, simpleDateFormat.format(date), simpleTimeFormat.format(date));
                        chatLists.add(chatList);
                        }
                    notifyChatObservers(chatLists);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        });
    }

}

