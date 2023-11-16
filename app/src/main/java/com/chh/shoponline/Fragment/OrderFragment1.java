package com.chh.shoponline.Fragment;

        import android.app.AlertDialog;
        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.fragment.app.Fragment;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ProgressBar;

        import com.chh.shoponline.Activity.MainActivity;
        import com.chh.shoponline.Adapter.OrderListAdapter;
        import com.chh.shoponline.Domain.Order;
        import com.chh.shoponline.Domain.Product;
        import com.chh.shoponline.R;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.ArrayList;
public class OrderFragment1 extends Fragment {
    private ArrayList<Order> orders = new ArrayList<>();
    private RecyclerView orderRecyclerView;
    private OrderListAdapter orderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order1, container, false);

        initRecyclerView(view);
        getOrders(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void initRecyclerView(View view){
        orderRecyclerView = view.findViewById(R.id.recycOrderWait);
        orderRecyclerView.setHasFixedSize(true);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderAdapter = new OrderListAdapter(orders);
        orderRecyclerView.setAdapter(orderAdapter);
    }

    private void getOrders(View view){

        // Khởi tạo AlertDialog.Builder
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Loading...");

        // Khởi tạo ProgressBar
        ProgressBar progressBar = new ProgressBar(getActivity());
        alertDialogBuilder.setView(progressBar);

        // Tạo AlertDialog từ AlertDialog.Builder
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("orders");
        DatabaseReference myRef1 = database.getReference("users/" + MainActivity.getMyUser().getId() + "/order_list");

        myRef1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                orders.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){

                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@androidx.annotation.NonNull DataSnapshot snapshot) {

                            for(DataSnapshot dataSnapshot1 : snapshot.getChildren()){

                                if(dataSnapshot.getKey().equals(dataSnapshot1.getKey()) && !dataSnapshot1.getValue(Order.class).isStatus()){
                                    Order order = dataSnapshot1.getValue(Order.class);
                                    orders.add(order);
                                    break;
                                }

                            }

                            initRecyclerView(view);
                        }
                        @Override
                        public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                        }
                    });
                }
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                alertDialog.dismiss();
            }
        });

    }
}