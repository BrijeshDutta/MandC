package muffsandchocss.com.mandc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class UpdateUserProfileActivity extends AppCompatActivity {

    //UI reference
    private View mProgressView;
    private View mLoginFormView;
    private EditText editTextAddress;
    private EditText editTextMobileNo;
    private Button buttonUpdateProfile;

    //FireBase
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);

        //Database initialization

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        // Set up the login form.

        editTextAddress = (EditText) findViewById (R.id.txtUpdateAddress);
        editTextMobileNo = (EditText)findViewById(R.id.txtMobileNo);
        buttonUpdateProfile = (Button) findViewById(R.id.btnUpdateProfile);

        final Intent intentHomeActivity = new Intent(this,HomeActivity.class);



        buttonUpdateProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatedAddress()){

                }
                else if (validatedMobileNo()){

                }
                else {
                    saveUserInformation();
                    startActivity(intentHomeActivity);

                }
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }

    private void saveUserInformation() {

        String address = editTextAddress.getText().toString().trim();
        String mobile = editTextMobileNo.getText().toString().trim();

        UserInformation userInformation = new UserInformation(address,mobile);
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        databaseReference.child(firebaseUser.getUid()).setValue(userInformation).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(UpdateUserProfileActivity.this,"Updated Successfully ",Toast.LENGTH_LONG).show();

                }else {
                    Toast.makeText(UpdateUserProfileActivity.this,"Failed to update " + task.getException().getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private boolean validatedAddress() {


        // Reset errors.
        editTextAddress.setError(null);

        // Store values at the time of the login attempt.
        String userAddress = editTextAddress.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(userAddress)) {
            editTextAddress.setError(getString(R.string.error_field_required));
            focusView = editTextAddress;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        return cancel;
    }

    private boolean validatedMobileNo() {


        // Reset errors.
        editTextMobileNo.setError(null);

        // Store values at the time of the login attempt.
        String userMobileNo = editTextMobileNo.getText().toString().trim();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(userMobileNo)) {
            editTextMobileNo.setError(getString(R.string.error_field_required));
            focusView = editTextMobileNo;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        return cancel;
    }
}

