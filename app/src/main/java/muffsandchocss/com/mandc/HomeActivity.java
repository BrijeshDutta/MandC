package muffsandchocss.com.mandc;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import muffsandchocss.com.fragments.BaseFragment;
import muffsandchocss.com.fragments.OrderSummaryFragment;
import muffsandchocss.com.fragments.PlaceOrderFragment;
import muffsandchocss.com.fragments.UserProfileFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;


    private TextView textViewUserName;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //
        intentLoginActivity = new Intent(this,LoginActivity.class);
        firebaseAuth = FirebaseAuth.getInstance();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        //navUsername = (TextView) headerView.findViewById(R.id.navUsername);
        textViewUserName =(TextView) headerView.findViewById(R.id.txtViewUserEmail);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        String getEmail = firebaseUser.getEmail();
        textViewUserName.setText(getEmail);

        Toast.makeText(HomeActivity.this,getString(R.string.welcome_label)+" " + getEmail,Toast.LENGTH_LONG).show();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Populate user details required in user profile
        populateUserDetails();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "https://www.facebook.com/Muffs-And-Chocss-1145625375542101/", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        fragment = new BaseFragment();
        if (fragment !=null){
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame,fragment);
            fragmentTransaction.commit();

        }
        navigationView.setNavigationItemSelectedListener(this);
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
            case R.id.nav_order_summary:
                fragment = new OrderSummaryFragment();
                break;
            case R.id.nav_profile:
                fragment = new UserProfileFragment();
                Bundle bundleArguments = new Bundle();
                bundleArguments.putString("userName",sUserName);
                bundleArguments.putString("userEmailId",sUserEmailId);
                bundleArguments.putString("userAddress",sUserAddress);
                bundleArguments.putString("userMobileNo",sUserMobileNo);
                fragment.setArguments(bundleArguments);
                break;
            case  R.id.nav_logout:
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null){
                    firebaseAuth.signOut();
                    finish();
                    startActivity(intentLoginActivity);
                }
                break;

        }

        if (fragment !=null){
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame,fragment);
            fragmentTransaction.commit();

        }

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
