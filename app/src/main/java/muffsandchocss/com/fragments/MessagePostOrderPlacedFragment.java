package muffsandchocss.com.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import muffsandchocss.com.mandc.R;


public class MessagePostOrderPlacedFragment extends Fragment {

    TextView textViewOrderPlacedMessage;
    TextView textViewDishType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View fragmentView = inflater.inflate(R.layout.fragment_message_post_order_placed, container, false);

        String dishType = getArguments().getString("dishType");
        textViewOrderPlacedMessage = (TextView) fragmentView.findViewById(R.id.textViewOrderPlacedMessage);
        textViewDishType = (TextView) fragmentView.findViewById(R.id.textViewDishType);
        textViewDishType.setText("Dish Type : " + dishType);
        //textViewOrderPlacedMessage.setText("OrderPlaced Successfully" + dishType);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Order Summary");

    }


}
