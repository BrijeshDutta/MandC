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
import android.widget.Button;
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
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    Button buttonViewOrderSummary;

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
        showOrderDetailsInViewWhenButtonClicked(fragmentView);



//
//        listViewOrderDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //String iTemText = (String) listViewOrderDetails.getItemAtPosition(position);
//
//                Toast.makeText(getActivity(),"Clicked : ",Toast.LENGTH_SHORT).show();
////              Code to display a new activity
////                Intent intent = new Intent(getActivity(), SummaryActivity.class);
////                String message = "abc";
////                intent.putExtra(EXTRA_MESSAGE, message);
////                startActivity(intent);
//            }
//        });
//

        return fragmentView;
    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        showOrderDetailsInView();
//    }

    public void showOrderDetailsInViewWhenButtonClicked(View fragmentView){

        buttonViewOrderSummary = (Button) fragmentView.findViewById(R.id.btnViewOrderSummary);

        buttonViewOrderSummary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(),"View Order Summary Clicker",Toast.LENGTH_LONG).show();
                showOrderDetailsInView();
            }
        });

    }

    private void showOrderDetailsInView() {
        orderDetailsList = new ArrayList<>();
        //Getting firebase user details
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.order_details_firebase_database));

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            OrderDetails orderDetails;

            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("UserUniqueId"+firebaseUser.getUid()).hasChildren()){
                    orderDetailsList.clear();

                    for (DataSnapshot  orderDetailSnapshot : dataSnapshot.child("UserUniqueId"+firebaseUser.getUid()).getChildren()){
                        orderDetails = orderDetailSnapshot.getValue(OrderDetails.class);
                        orderDetailsList.add(orderDetails);
                    }
                    OrderDetailsList adapter = new OrderDetailsList(getActivity(),orderDetailsList);
                    //adapter.insert(orderDetails,0);
                    listViewOrderDetails.setAdapter(adapter);

                }
                else{
                    Toast.makeText(getActivity(),"No Orders to View",Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getActivity(),"Error db " +databaseError.getMessage(),Toast.LENGTH_LONG).show();

            }
        });

    }
}
