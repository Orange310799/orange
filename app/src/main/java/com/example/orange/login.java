package com.example.orange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class login extends AppCompatActivity {
    private static final Pattern Password_Pattern =
            Pattern.compile("^" + ".{6,15}" + "$");
    private EditText inputEmail;
    private EditText inputpass;
    private FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    private TextView Login_t;
    private TextView Forget;
    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        inputEmail = (EditText) findViewById(R.id.login_text);
        inputpass = (EditText) findViewById(R.id.pass_text);
        findViewById(R.id.login_pg_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validatePass()) {
                    showErrorDialog("Check email pass");
                } else {
                    login_activity();
                }
            }
        });
        Login_t = findViewById(R.id.signupT);
        Login_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(login.this, Signup.class));
            }
        });
        Forget = findViewById(R.id.ForgetPassword);
        Forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(login.this, forgetpassword.class));
            }
        });
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    finish();
                    startActivity(new Intent(login.this, Dashboard.class));
                }
            }
        };
    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }


    private boolean validatePass() {
        boolean valid = true;

        String pass = inputpass.getText().toString();
        String email = inputEmail.getText().toString();
        if (inputpass.length() == 0) {
            inputpass.setError("Field can't be empty");
            valid = false;
        }
        if (!Password_Pattern.matcher(pass).matches()) {
            inputpass.setError("Minimum 6 characters& use one special character");
            valid = false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Please Enter a Valid Email Address");
            valid = false;
        }
        if (email.length() == 0) {
            inputEmail.setError("Field Can't be empty");
            valid = false;
        }
        return valid;
    }

    public void login_activity() {
        Log.d("chat", "Login.......");
        String Email = inputEmail.getText().toString();
        String pass = inputpass.getText().toString();
        mAuth.signInWithEmailAndPassword(Email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Log.d("FlashChat", "Problem signing in: " + task.getException());
                    showErrorDialog("Username Or Password Incorrect");
                } else {
                    Intent login = new Intent(login.this, Dashboard.class);
                    finish();
                    startActivity(login);
                }
            }
        });


    }

    /*public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }*/

}
