package muffsandchocss.com.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import muffsandchocss.com.mandc.R;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class OrderSummaryFragment extends Fragment {

    ListView listViewOrderDetails;
    List<OrderDetails> orderDetailsList;

    DatabaseReference databaseReference;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Order Summary");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentView = inflater.inflate(R.layout.fragment_order_summary, container, false);
        listViewOrderDetails = (ListView) fragmentView.findViewById(R.id.listViewOrderDetails);
        orderDetailsList = new ArrayList<>();
        //Getting firebase user details
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("UserUniqueId"+firebaseUser.getUid());

        listViewOrderDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String iTemText = (String) listViewOrderDetails.getItemAtPosition(position);

                Toast.makeText(getActivity(),"Clicked : ",Toast.LENGTH_SHORT).show();
//              Code to display a new activity
//                Intent intent = new Intent(getActivity(), SummaryActivity.class);
//                String message = "abc";
//                intent.putExtra(EXTRA_MESSAGE, message);
//                startActivity(intent);
            }
        });


        // Inflate the layout for this fragment
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseReference.addValueEventListener(new ValueEventListener() {
            OrderDetails orderDetails;

            public void onDataChange(DataSnapshot dataSnapshot) {
                orderDetailsList.clear();
                for (DataSnapshot  orderDetailSnapshot : dataSnapshot.getChildren()){
                    orderDetails = orderDetailSnapshot.getValue(OrderDetails.class);
                    orderDetailsList.add(orderDetails);
                }
                OrderDetailsList adapter = new OrderDetailsList(getActivity(),orderDetailsList);
                //adapter.insert(orderDetails,0);
                listViewOrderDetails.setAdapter(adapter);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
