package muffsandchocss.com.mandc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import muffsandchocss.com.Cart;
import muffsandchocss.com.FoodList;
import muffsandchocss.com.Interface.ItemClickListener;
import muffsandchocss.com.OrderStatus;
import muffsandchocss.com.SignInActivity;
import muffsandchocss.com.ViewHolder.MenuViewHolder;
import muffsandchocss.com.common.Common;
import muffsandchocss.com.fragments.BaseFragment;
import muffsandchocss.com.fragments.OrderSummaryFragment;
import muffsandchocss.com.fragments.PlaceOrderFragment;
import muffsandchocss.com.fragments.UserProfileFragment;
import muffsandchocss.com.model.Category;
import muffsandchocss.com.model.Food;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;



    private TextView textViewUserEmailId;

    private Intent intentLoginActivity;

    //Fragment declaration
    Fragment fragment = null;
    FragmentTransaction fragmentTransaction;

    //Get userdetails
    String sUserName;
    String sUserEmailId;
    String sUserAddress;
    String sUserMobileNo;

    //NEW

    FirebaseDatabase database;
    DatabaseReference category;

    private TextView textViewUserName;
    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;

    FirebaseRecyclerAdapter<Category,MenuViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        textViewUserName =(TextView) headerView.findViewById(R.id.txtViewUserName);
        textViewUserName.setText(Common.currentUser.getName());

        //Toast.makeText(HomeActivity.this,getString(R.string.welcome_label)+" " + getEmail,Toast.LENGTH_LONG).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        //Init Firebase

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        //Load menu

        recycler_menu = (RecyclerView) findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        loadMenu();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cartIntent = new Intent(HomeActivity.this, Cart.class);
                startActivity(cartIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void loadMenu() {

        adapter= new FirebaseRecyclerAdapter<Category, MenuViewHolder>(Category.class,R.layout.menu_item,MenuViewHolder.class,category) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                viewHolder.txtMenuName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.imageView);

                final Category clickItem = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(HomeActivity.this,"" +clickItem.getName(),Toast.LENGTH_SHORT).show();

                        //Get category and send it to new Activity

                        Intent foodList = new Intent(HomeActivity.this, FoodList.class);
                        foodList.putExtra("CategoryId",adapter.getRef(position).getKey());
                        startActivity(foodList);

                    }
                });
            }
        };

        recycler_menu.setAdapter(adapter);
    }

    private void populateUserDetails(){

        //implementent Edit functionality
        //Toast.makeText(getActivity(),"Update user details",Toast.LENGTH_SHORT).show();
        //Getting firebase user details
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.userdetails_firebase_database_reference));
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).hasChildren()){
                    //Toast.makeText(getActivity(),"Details exist",Toast.LENGTH_SHORT).show();

                    sUserAddress = (String) dataSnapshot.child(firebaseUser.getUid()).child("address").getValue();
                    //Toast.makeText(getActivity(),"Address " + sAddress,Toast.LENGTH_SHORT).show();

                    sUserMobileNo = (String) dataSnapshot.child(firebaseUser.getUid()).child("mobile").getValue();
                    //Toast.makeText(getActivity(),"Contact no" +mobile ,Toast.LENGTH_SHORT).show();

                    sUserName = firebaseUser.getDisplayName();
                    sUserEmailId =firebaseUser.getEmail();
                }else {

                    Toast.makeText(getApplicationContext(),"No Details Available !! Please update the details",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),"Db Error " + databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }
    private void displaySelectedScreen(int id){



        switch (id){
            case R.id.nav_home:
                fragment = new BaseFragment();
                break;

            case R.id.nav_place_order:
                fragment = new PlaceOrderFragment();
                break;
            case R.id.nav_orders:
                Intent orderIntent = new Intent(HomeActivity.this, OrderStatus.class);
                startActivity(orderIntent);
                break;
            case R.id.nav_profile:
                break;
            case R.id.nav_cart:
                Intent cartIntent = new Intent(HomeActivity.this, Cart.class);
                startActivity(cartIntent);
                break;
            case  R.id.nav_logout:
                Intent signInIntent = new Intent(HomeActivity.this, SignInActivity.class);
                signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(signInIntent);
                break;

        }

//        if (fragment !=null){
//            fragmentTransaction = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction.replace(R.id.content_frame,fragment);
//            fragmentTransaction.commit();
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        displaySelectedScreen(item.getItemId());
        return true;
    }
}
