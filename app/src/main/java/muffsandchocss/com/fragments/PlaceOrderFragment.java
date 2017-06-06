package muffsandchocss.com.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import muffsandchocss.com.mandc.R;
import muffsandchocss.com.mandc.UpdateUserProfileActivity;

public class PlaceOrderFragment extends Fragment {


    String [] SPINNER_LIST_DISH = {"Choclates","Muffins"};
    String [] SPINNER_CHOCLATE_TYPE = {"Dark","Milky"};


    MaterialBetterSpinner materialBetterSpinnerDishSelecter;
    String sUserSelectedDish;
    //Dry fruit selection
    EditText editTextSelectDryFruits;

    //Choclate type
    MaterialBetterSpinner spinnerChoclatePicker;
    String sUserSlectedChoclateType;

    //Quantity
    EditText editTextQuantity;

    //Delivery date picker
    EditText editTextDeliveryDate;
    Calendar myCalendar;

    //Special preparation Comments

    EditText editTextSpecialPreComments;
    String sSpecialPreComments;
    //Place order Button
    Button buttonPlaceOrder;

    String [] listDryFruits;
    boolean [] checkedDryFruits;
    ArrayList<Integer> userSelectedDryFruits = new ArrayList<>();

    //FireBase
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    //Progress dailog for placiing order
    ProgressDialog progressDialogPlaceOrder;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getString(R.string.place_order_label));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_place_order, container, false);
        progressDialogPlaceOrder = new ProgressDialog(getActivity());

        firebaseAuth = FirebaseAuth.getInstance();

        //Show dish drop down
        showDishDropDown(fragmentView);

        //Show choclate type dropdown
        showChoclateDropDown(fragmentView);

        //Show Dry fruits selection
        showDryFruitSelection(fragmentView);

        //quantity
        editTextQuantity = (EditText)fragmentView.findViewById(R.id.quantity);

        showDeliveryDatePicker(fragmentView);

        //Get Special preparation comments
        //getSpecialPreComments(fragmentView);

        editTextSpecialPreComments = (EditText) fragmentView.findViewById(R.id.specialInstructions);

        placeOrderButton(fragmentView);
        return  fragmentView;

    }

    private void getSpecialPreComments(View fragmentView) {

        //sSpecialPreComments = editTextSpecialPreComments.getText().toString().trim();
    }

    private void placeOrderButton(View fragmentView) {
        buttonPlaceOrder = (Button) fragmentView.findViewById(R.id.btnPlaceOrder);
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dishType = sUserSelectedDish;
                String userSelectedChoclateType = sUserSlectedChoclateType;
                String sDryFruits = editTextSelectDryFruits.getText().toString().trim();
                int quantity = Integer.parseInt(editTextQuantity.getText().toString().trim());
                String deliveryDate = editTextDeliveryDate.getText().toString().trim();
                String specialPreComments = editTextSpecialPreComments.getText().toString().trim();

                //Progress dailog
                progressDialogPlaceOrder.setMessage("Placing Order...");
                progressDialogPlaceOrder.show();

                //Getting firebase user details
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                //Database initialization
                databaseReference = FirebaseDatabase.getInstance().getReference("UserUniqueId"+firebaseUser.getUid());
                String orderId = databaseReference.push().getKey();
                OrderDetails orderDetails = new OrderDetails(orderId,dishType,userSelectedChoclateType,sDryFruits,quantity,deliveryDate,specialPreComments);

                databaseReference.child("OrderDetails"+orderId).setValue(orderDetails).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(),"Order Placed Successfully ",Toast.LENGTH_LONG).show();
                            progressDialogPlaceOrder.dismiss();
                            //Summary Fragment display
                            Fragment fragment = new OrderSummaryFragment();

                            if (fragment !=null){
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.content_frame,fragment);
                                fragmentTransaction.commit();

                            }


                        }else {
                            Toast.makeText(getActivity(),"Failed to place order " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

                //Toast.makeText(getActivity(),"Place Order",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDeliveryDatePicker(View fragmentView) {

        editTextDeliveryDate = (EditText)fragmentView.findViewById(R.id.deliveryDate);
        editTextDeliveryDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePicker();
            }
        });
        editTextDeliveryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    datePicker();
                }
            }
        });
    }

    private void datePicker(){
        // TODO Auto-generated method stub

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar myCalendar = Calendar.getInstance();
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        new DatePickerDialog(getActivity(), android.R.style.Theme_DeviceDefault_Light_Dialog,date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editTextDeliveryDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void showDishDropDown(View fragmentView) {

        //Drop down to select type of dish
        materialBetterSpinnerDishSelecter = (MaterialBetterSpinner)fragmentView.findViewById(R.id.dish);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, SPINNER_LIST_DISH);
        materialBetterSpinnerDishSelecter.setAdapter(adapter);
        materialBetterSpinnerDishSelecter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                sUserSelectedDish = materialBetterSpinnerDishSelecter.getText().toString().trim();
            }
        });
    }

    private void showChoclateDropDown(View fragmentView) {

        //Drop down to select type of dish
        spinnerChoclatePicker = (MaterialBetterSpinner) fragmentView.findViewById(R.id.choclateType);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.spinner_item, SPINNER_CHOCLATE_TYPE);
        spinnerChoclatePicker.setAdapter(adapter);
        spinnerChoclatePicker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                sUserSlectedChoclateType = spinnerChoclatePicker.getText().toString().trim();
            }
        });
    }

    private void showDryFruitSelection(View fragmentView){

        editTextSelectDryFruits = (EditText)fragmentView.findViewById(R.id.selectDryFruits);
        //Creating list item
        listDryFruits = getResources().getStringArray(R.array.dryfruit_list);
        checkedDryFruits = new boolean[listDryFruits.length];

        editTextSelectDryFruits.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    dryFruitMultiSelection();
                }
            }
        });
        editTextSelectDryFruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dryFruitMultiSelection();
            }
        });

    }
    private  void dryFruitMultiSelection(){

        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle(R.string.title_select_dryfruits);
        alertDialog.setMultiChoiceItems(listDryFruits, checkedDryFruits, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (isChecked){

                    //Check o avoid duplication
                    if (!userSelectedDryFruits.contains(which) ){
                        //add the the checked value
                        userSelectedDryFruits.add(which);
                    } else {
                        userSelectedDryFruits.remove(which);
                    }
                }
            }
        });
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(R.string.ok_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String item ="";
                for (int i = 0; i<userSelectedDryFruits.size();i++){
                    item = item + listDryFruits[userSelectedDryFruits.get(i)];
                    if (i != userSelectedDryFruits.size()-1){
                        item = item + ", ";
                    }
                }
                editTextSelectDryFruits.setText(item);
            }
        });
        alertDialog.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setNeutralButton(R.string.clearall_label, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0 ; i < checkedDryFruits.length; i++){
                    checkedDryFruits [i] = false;
                    userSelectedDryFruits.clear();
                    editTextSelectDryFruits.setText("");
                }
            }
        });
        AlertDialog alertDialog1 = alertDialog.create();
        alertDialog1.show();

    }


}
