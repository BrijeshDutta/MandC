package muffsandchocss.com.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import muffsandchocss.com.mandc.R;


public class MessagePostOrderPlacedFragment extends Fragment {

    EditText textViewOrderPlacedMessage;
    TextView textViewOrderType;
    TextView textViewOrderId;
    TextView textViewQuantity;
    TextView textViewOrderValue;

    Button buttonAddOrder;
    Button buttonDeleteOrder;

    Fragment fragment;
    FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragmentView = inflater.inflate(R.layout.fragment_message_post_order_placed, container, false);

        String dishType = getArguments().getString("dishType");
        String sOrderId = getArguments().getString("orderId");
        String sOrderQuantity = getArguments().getString("orderQuantity");
        String sOrderValue = getArguments().getString("orderValue");



        textViewOrderPlacedMessage = (EditText) fragmentView.findViewById(R.id.textViewOrderPlacedMessage);
        textViewOrderId = (TextView) fragmentView.findViewById(R.id.textViewOrderId);
        textViewOrderType = (TextView) fragmentView.findViewById(R.id.textViewOrderType);
        textViewQuantity = (TextView) fragmentView.findViewById(R.id.textViewOrderQuantity);
        textViewOrderValue = (TextView) fragmentView.findViewById(R.id.textViewOrderValue);

        buttonAddOrder = (Button) fragmentView.findViewById(R.id.btnAddOrder);
        buttonAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new PlaceOrderFragment();
                if (fragment != null){
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame,fragment);
                    fragmentTransaction.commit();
                }
            }
        });
        buttonDeleteOrder = (Button) fragmentView.findViewById(R.id.btnRemoveOrder);
        buttonDeleteOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Delete functionality Not implemented yet",Toast.LENGTH_SHORT).show();
            }
        });
//
        textViewOrderId.setText("Order Id : " + sOrderId);
        textViewOrderType.setText("Order Type : " + dishType);
        textViewQuantity.setText("Order quantity : " +sOrderQuantity);
        Toast.makeText(getActivity(),sOrderValue,Toast.LENGTH_SHORT).show();
        textViewOrderValue.setText("Order Value : " + sOrderValue);

        //textViewOrderPlacedMessage.setText("OrderPlaced Successfully" + dishType);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Order Summary");

    }


}
