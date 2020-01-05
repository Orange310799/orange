package com.example.orange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {
    private static final Pattern Password_Pattern =
            Pattern.compile("^" + ".{6,15}"+ "$");
    private EditText inputEmail;
    private EditText inputpass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        inputEmail= findViewById(R.id.Signupemail);
        inputpass= findViewById(R.id.signup_pass);
        mAuth=FirebaseAuth.getInstance();
        findViewById(R.id.Signup_bttn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(validatePass() != false || validateEmail() != false)) {
                    showErrorDialog("Check email pass");
                } else {
                    Intent step2 = new Intent(Signup.this,personal_details.class);
                    startActivity(step2);
                }
            }
        });
    }


    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null )
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    private boolean validateEmail() {
        String email = inputEmail.getText().toString();
        if (email.length() == 0) {
            inputEmail.setError("Field Can't be empty");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            inputEmail.setError("Please Enter a Valid Email Address");
            return false;
        } else {
            inputEmail.setError(null);
            Log.d("chat", "login pass");

            return true;

        }
    }
    private boolean validatePass() {

        String pass = inputpass.getText().toString();
        if (inputpass.length() == 0) {
            inputpass.setError("Field can't be empty");
            return false;
        } else if (!Password_Pattern.matcher(pass).matches()) {
            inputpass.setError("Minimum 6 characters& use one special character");
            return false;

        } else {
            inputpass.setError(null);

            return true;
        }
    }

}