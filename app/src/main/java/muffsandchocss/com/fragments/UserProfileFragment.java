package muffsandchocss.com.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import muffsandchocss.com.mandc.R;

public class UserProfileFragment extends Fragment {

    TextView textViewAddress,textViewMobileNo,textViewUserName,textViewUserEmailId;
    Button buttonUpdateUserDetails;
    String sAddress,sMobileNo;

    //Firebase database
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    boolean isError;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                builder.setTitle("Update details");
                builder.setView(R.layout.update_user_details_dailog);
                //In case it gives you an error for setView(View) try
                final View updateUserDetails = inflater.inflate(R.layout.update_user_details_dailog, null);

                final EditText editTextAddress = (EditText) updateUserDetails.findViewById(R.id.txtUpdateAddress);
                final EditText editTextMobileNo = (EditText) updateUserDetails.findViewById(R.id.txtMobileNo);
                
                builder.setView(updateUserDetails);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        sAddress = editTextAddress.getText().toString().trim();
                        sMobileNo = editTextMobileNo.getText().toString().trim();

                        //Toast.makeText(getActivity(),sAddress+"Mobile "+sMobileNo,Toast.LENGTH_LONG).show();
                        updateUserDetailsinDB(sAddress,sMobileNo);

                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });

        // Inflate the layout for this fragment

        return fragmentLayout;
    }

    private void updateUserDetailsinDB(final String sAddress, final String sMobileNo) {


        isError= false;
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.userdetails_firebase_database_reference));
        databaseReference.child(firebaseUser.getUid()).child("address").setValue(sAddress).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    //Toast.makeText(getActivity(),"Address Updated",Toast.LENGTH_LONG).show();
                    textViewAddress.setText("Address : " +sAddress);
                }else {
                    isError = true;
                }
            }
        });
        databaseReference.child(firebaseUser.getUid()).child("mobile").setValue(sMobileNo).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    //Toast.makeText(getActivity(),"Mobile updated",Toast.LENGTH_LONG).show();
                    textViewMobileNo.setText("Mobile No : "+sMobileNo);

                }else {
                    isError = true;
                }
            }
        });
        if (isError){
            Toast.makeText(getActivity(),"Failed to update user details",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(getActivity(),"Successfuly updated user details",Toast.LENGTH_LONG).show();
        }
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
