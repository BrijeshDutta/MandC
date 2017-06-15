package muffsandchocss.com.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import muffsandchocss.com.mandc.R;


public class UserProfileFragment extends Fragment {

    TextView textViewAddress,textViewMobileNo,textViewUserName,textViewUserEmailId;
    Button buttonUpdateUserDetails;

    //Firebase database

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.user_profile_label));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragmentLayout = inflater.inflate(R.layout.fragment_user_profile, container, false);


        String userName = getArguments().getString("userName");
        String userEmailId = getArguments().getString("userEmailId");
        String userAddress = getArguments().getString("userAddress");
        String userMobileNo = getArguments().getString("userMobileNo");

        //Initializing UI components
        textViewAddress = (TextView) fragmentLayout.findViewById(R.id.editTextAddress);
        textViewMobileNo = (TextView) fragmentLayout.findViewById(R.id.editTextMobile);
        textViewUserName = (TextView) fragmentLayout.findViewById(R.id.user_profile_name);
        textViewUserEmailId = (TextView) fragmentLayout.findViewById(R.id.user_profile_email_id);
        buttonUpdateUserDetails = (Button) fragmentLayout.findViewById(R.id.btn_update_user_details);

        textViewUserName.setText(userName);
        textViewUserEmailId.setText(userEmailId);
        textViewAddress.setText("Address : " + userAddress);
        textViewMobileNo.setText("Mobile No : " + userMobileNo);




        buttonUpdateUserDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // Inflate the layout for this fragment

        return fragmentLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();
        //populateUserDetails();
    }
}
