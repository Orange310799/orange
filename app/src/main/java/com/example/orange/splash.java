package com.example.orange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash extends AppCompatActivity {
    Handler handler;
    private FirebaseAuth mAuth;
   // FirebaseAuth.AuthStateListener mAuthListner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        handler =new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in, send to mainmenu
                    Log.d("", "onAuthStateChanged:signed_in:" + user.getUid());
                    startActivity(new Intent(splash.this, Dashboard.class));
                } else {
                    // User is signed out, send to register/login
                    startActivity(new Intent(splash.this, MainActivity.class));
                }
            }
        },2000);
    }
}
