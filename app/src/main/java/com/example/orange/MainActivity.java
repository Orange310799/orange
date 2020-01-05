package com.example.orange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button l1,s1;
    TextView signupT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        l1= findViewById(R.id.login_b);
        s1= findViewById(R.id.signup_b);
        signupT= findViewById(R.id.signupT);
        String s2 = "Sign up";
        SpannableString spannableString = new SpannableString(s2);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(View Intent) {
                Intent intent_3 = new Intent(MainActivity.this,Signup.class);
                startActivity(intent_3);
            }
        };
        spannableString.setSpan(clickableSpan1, 0,6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
       
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_one = new Intent(MainActivity.this,login.class);
                startActivity(intent_one);
            }
        });
        s1.setOnClickListener(new View.OnClickListener() {
                                  @Override
                                  public void onClick(View view) {
                                      Intent Intent_two =new Intent(MainActivity.this,Signup.class);
                                      startActivity(Intent_two);
                                  }
                              }

        );

    }

    }

