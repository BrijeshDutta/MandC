package muffsandchocss.com;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;
import muffsandchocss.com.Database.Database;
import muffsandchocss.com.ViewHolder.CartAdapter;
import muffsandchocss.com.mandc.R;
import muffsandchocss.com.model.Order;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager ;

    TextView txtTotalPrice;
    FButton btnPlaceOrder;

    //Firebase
    FirebaseDatabase database;
    DatabaseReference request;

    List<Order> cart = new ArrayList<>();
    CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //Init Firebase

        database = FirebaseDatabase.getInstance();
        request = database.getReference("Requests");

        //Init
        recyclerView = (RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView) findViewById(R.id.total);
        btnPlaceOrder = (FButton) findViewById(R.id.btnPlaceOrder);

        loadListFood();
        
    }

    private void loadListFood() {
        cart = new Database(this).getCarts();
        cartAdapter = new CartAdapter(cart,this);
        recyclerView.setAdapter(cartAdapter);

        //Calculate total price
        int total = 0;

        for (Order order:cart){
            total+= (Integer.parseInt(order.getPrice()))*(Integer.parseInt(order.getQuantity()));
            Locale locale = new Locale("en","US");
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
            txtTotalPrice.setText(fmt.format(total));
        }
    }
}
