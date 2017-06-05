package muffsandchocss.com.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import muffsandchocss.com.mandc.R;

public class PlaceOrderFragment extends Fragment {

    String [] SPINNER_LIST_DISH = {"Choclates","Sub way","Burger"};
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Place Order");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_place_order, container, false);

        MaterialBetterSpinner materialBetterSpinner = (MaterialBetterSpinner)fragmentView.findViewById(R.id.dish);
        /// / Inflate the layout for this fragment

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_dropdown_item_1line, SPINNER_LIST_DISH);
        //adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        materialBetterSpinner.setAdapter(adapter);


       return  fragmentView;

    }

}
