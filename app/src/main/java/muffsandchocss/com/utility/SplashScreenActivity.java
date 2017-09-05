package muffsandchocss.com.utility;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import muffsandchocss.com.MainActivity;
import muffsandchocss.com.mandc.LoginActivity;
import muffsandchocss.com.mandc.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread myThread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(3000);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }
}
