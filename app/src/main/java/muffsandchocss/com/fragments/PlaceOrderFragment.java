package muffsandchocss.com.fragments;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import muffsandchocss.com.mandc.R;

public class PlaceOrderFragment extends Fragment {


    String [] SPINNER_LIST_DISH = {"Choclates","Muffins"};
    String [] SPINNER_CHOCLATE_TYPE = {"Dark","Milk","White"};


    //
    final String orderType = "CHOCLATE";
    final int price = 12;
    int iOrderValue =0;

    String sUserSelectedDish;
    //Dry fruit selection
    EditText editTextSelectDryFruits;
    String sDryFruits;

    //Choclate type
    MaterialBetterSpinner spinnerChoclatePicker;
    String sUserSlectedChoclateType = "No choclates selected";
    String userSelectedChoclateType;

    //Quantity
    Button buttonQuantity,buttonAddQuantity,buttonRemoveQuantity;
    int userSelectedQuantity = 0;
    int quantity;


    //Delivery date picker
    Button buttonDeliveryDate;
    Calendar myCalendar;
    Calendar c;
    String userSelectedDeliveryDate = "No delivery date";
    int iUserSelectedYear,iUserSelectedMonth,iUserSelectedDayOfMonth;
    String deliveryDate;


    //

    //Special preparation Comments

    EditText editTextSpecialPreComments;
    String specialPreComments;

    //
    EditText textViewPrice;
    EditText textViewOrderValue;
    //Place order Button
    Button buttonPlaceOrder;

    String [] listDryFruits;
    boolean [] checkedDryFruits;
    ArrayList<Integer> userSelectedDryFruits = new ArrayList<>();

    //FireBase
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    String orderId;
    OrderDetails orderDetails;

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


        //Show choclate type dropdown
        showChoclateDropDown(fragmentView);

        //Show Dry fruits selection
        showDryFruitSelection(fragmentView);

        textViewOrderValue = (EditText) fragmentView.findViewById(R.id.txtViewOrderValue);

        //quantity
        addRemoveQuantity(fragmentView);

        //Delivery Date
        showDeliveryDatePicker(fragmentView);

        //Get Special preparation comments
        //getSpecialPreComments(fragmentView);

        editTextSpecialPreComments = (EditText) fragmentView.findViewById(R.id.specialInstructions);

        //display the price
        textViewPrice = (EditText) fragmentView.findViewById(R.id.txtViewPrice);
        textViewPrice.setText("Price per piece Rs." + Integer.toString(price));

        placeOrderButton(fragmentView);
        return  fragmentView;

    }

    private void addRemoveQuantity(View fragmentView) {


        buttonQuantity = (Button)fragmentView.findViewById(R.id.btnQuantity);
        buttonAddQuantity = (Button)fragmentView.findViewById(R.id.btnAddQuantity);
        buttonRemoveQuantity = (Button) fragmentView.findViewById(R.id.btnRemoveQuantity);
        buttonAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonQuantity.setError(null);
                userSelectedQuantity =userSelectedQuantity + 1;
                iOrderValue = userSelectedQuantity*price;
                textViewOrderValue.setText("Order Value Rs."+ Integer.toString(iOrderValue));
                buttonQuantity.setText(Integer.toString(userSelectedQuantity));
            }
        });
        buttonRemoveQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonQuantity.setError(null);
                if (userSelectedQuantity <=0){
                    buttonQuantity.setError("Quantity cannot be less than 0");
                    Toast.makeText(getActivity(),"Quantity cannot be less than 0",Toast.LENGTH_SHORT).show();
                } else
                {
                    userSelectedQuantity =userSelectedQuantity - 1;
                    iOrderValue = userSelectedQuantity*price;
                    textViewOrderValue.setText("Order Value Rs."+ Integer.toString(iOrderValue));

                    buttonQuantity.setText(Integer.toString(userSelectedQuantity));
                }

            }
        });

    }

    private void getSpecialPreComments(View fragmentView) {

        //sSpecialPreComments = editTextSpecialPreComments.getText().toString().trim();
    }

    private void placeOrderButton(View fragmentView) {
        buttonPlaceOrder = (Button) fragmentView.findViewById(R.id.btnPlaceOrder);
        buttonPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String dishType = orderType;
                userSelectedChoclateType = sUserSlectedChoclateType;
                sDryFruits = editTextSelectDryFruits.getText().toString().trim();
                quantity = Integer.parseInt(buttonQuantity.getText().toString().trim());
                deliveryDate = userSelectedDeliveryDate;
                specialPreComments = editTextSpecialPreComments.getText().toString().trim();

                //Field validation
                View focusView = null;
                if (userSelectedChoclateType.equalsIgnoreCase("No choclates selected")){
                    spinnerChoclatePicker.setError("Select chocolate");
                    focusView = spinnerChoclatePicker;

                    Toast.makeText(getActivity(),"Please select choclate type " + userSelectedChoclateType,Toast.LENGTH_LONG).show();
                }
                else if (sDryFruits.isEmpty()){
                    Toast.makeText(getActivity(),"Please select dry fruits" + sDryFruits,Toast.LENGTH_LONG).show();
                    editTextSelectDryFruits.setError("Select dry fruits");
                    focusView = editTextSelectDryFruits;

                } else if (quantity <= 0){
                    Toast.makeText(getActivity(),"Quantity should be greated than: " + quantity,Toast.LENGTH_LONG).show();
                    buttonQuantity.setError("Quantity should be greated than: " + quantity);
                    focusView = buttonAddQuantity;

                } else if (deliveryDate.equalsIgnoreCase("No delivery date")){
                    Toast.makeText(getActivity(),"Pick a delivery date",Toast.LENGTH_LONG).show();
                    buttonDeliveryDate.setError("Pick a delivery date");
                    focusView = buttonDeliveryDate;
                }
                else {


                    //Getting firebase user details
                    firebaseUser = firebaseAuth.getCurrentUser();
                    //Database initialization
                    databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.order_details_firebase_database));
                    orderId = databaseReference.push().getKey();
                    orderDetails = new OrderDetails(orderId,dishType,userSelectedChoclateType,sDryFruits,quantity,deliveryDate,specialPreComments,iOrderValue);

                    //Alert Dailog box
                    final AlertDialog.Builder alertDialogConfirmOrder = new AlertDialog.Builder(getActivity());

                    alertDialogConfirmOrder.setTitle("Confirmation");
                    alertDialogConfirmOrder.setMessage("Are you sure you want to place order?. Order Value : " + iOrderValue);

                    alertDialogConfirmOrder.setPositiveButton("Yes",  new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Progress dailog
                            progressDialogPlaceOrder.setMessage("Placing Order...");
                            progressDialogPlaceOrder.show();


                            databaseReference.child("UserUniqueId"+firebaseUser.getUid()).child(orderId).setValue(orderDetails).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Toast.makeText(getActivity(),"Order Placed Successfully ",Toast.LENGTH_LONG).show();
                                        progressDialogPlaceOrder.dismiss();
//                            //Summary Fragment display
                                        //Giving Issues
                                        Fragment fragment = new MessagePostOrderPlacedFragment();
                                        Bundle bundleArguments = new Bundle();
                                        bundleArguments.putString("dishType",dishType);
                                        bundleArguments.putString("orderId",orderId);
                                        bundleArguments.putString("choclateType",userSelectedChoclateType);
                                        bundleArguments.putString("dryFruits",sDryFruits);
                                        bundleArguments.putString("orderQuantity",Integer.toString(quantity));
                                        bundleArguments.putString("deliveryDate",deliveryDate);
                                        bundleArguments.putString("specialInstructions",specialPreComments);
                                        bundleArguments.putString("price",Integer.toString(price));
                                        bundleArguments.putString("orderValue",Integer.toString(iOrderValue));
                                        fragment.setArguments(bundleArguments);

//                                        if (fragment !=null){
//                                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//                                            fragmentTransaction.replace(R.id.content_frame,fragment);
//                                            fragmentTransaction.commit();
//
//                                        }


                                    }else {
                                        Toast.makeText(getActivity(),"Failed to place order " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });


                            //Toast.makeText(getActivity(),"Inside Yes",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();

                        }
                    });
                    alertDialogConfirmOrder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            Toast.makeText(getActivity(),"Oh noo !! u cancelled placing order !!",Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = alertDialogConfirmOrder.create();
                    alert.show();
                }



                    //Toast.makeText(getActivity(),"Place Order",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void showDeliveryDatePicker(View fragmentView) {

        buttonDeliveryDate = (Button) fragmentView.findViewById(R.id.btnDeliveryDate);
        buttonDeliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                buttonDeliveryDate.setError(null);
                // Use the current date as the default date in the picker
                c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                //R.style.DatePicker
                DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        iUserSelectedYear = year;
                        iUserSelectedMonth = month;
                        iUserSelectedDayOfMonth = dayOfMonth;

                        deliveryDate= String.valueOf(dayOfMonth) + "/" + String.valueOf(month) + "/" + String.valueOf(year);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

                        validateDeliveryDate(iUserSelectedYear,iUserSelectedMonth,iUserSelectedDayOfMonth,deliveryDate);


                    }
                },year,month,day);

                datePicker.show();

            }
        });


    }

    private void validateDeliveryDate(int year, int month, int dayOfMonth,String deliveryDate) {
        Calendar choosenDate = Calendar.getInstance();
        choosenDate.set(year,month,dayOfMonth);
        if (choosenDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY  || choosenDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){

            Toast.makeText(getActivity(),"Yo !! we deliver only on weekends.." +("\ud83d\ude0a")+"..." +("\ud83d\ude0a")+"..." +("\ud83d\ude0a"),Toast.LENGTH_LONG).show();

            ((Button) getActivity().findViewById(R.id.btnDeliveryDate)).setText(deliveryDate);
            userSelectedDeliveryDate = buttonDeliveryDate.getText().toString().trim();

        }
        else if (choosenDate.before(c)){


            buttonDeliveryDate.setText("DATE IN PAST");
            buttonDeliveryDate.setError("DATE IN PAST");
            //buttonDeliveryDate.setTextColor(Color.parseColor("#ff0000"));
            Toast.makeText(getActivity(),"Oh no !! time never comes back !!" ,Toast.LENGTH_SHORT).show();
            return;

        }
        else {
            buttonDeliveryDate.setError("NOT A WEEKEND");
            //buttonDeliveryDate.setTextColor(Color.parseColor("#ff0000"));
            Toast.makeText(getActivity(),"Hey !! its not a weekend !!! we hate serving on weekdays they are so boring uhh " +
                    ("\ud83d\ude0c"),Toast.LENGTH_LONG).show();
            return;

        }
    }

    private void showChoclateDropDown(View fragmentView) {

        //Drop down to select type of dish
        spinnerChoclatePicker = (MaterialBetterSpinner) fragmentView.findViewById(R.id.choclateType);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(), R.layout.support_simple_spinner_dropdown_item, SPINNER_CHOCLATE_TYPE);
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
                editTextSelectDryFruits.setError(null);
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
//
//        alertDialog.setNegativeButton(R.string.dismiss_label, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
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
