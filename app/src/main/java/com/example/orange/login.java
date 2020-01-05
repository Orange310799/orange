package com.example.orange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class login extends AppCompatActivity {
    private static final Pattern Password_Pattern =
            Pattern.compile("^" + ".{6,15}" + "(?=.*[@#$%^&+=])" + "$");
    private EditText inputEmail;
    private EditText inputpass;
    private FirebaseAuth mAuth;


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
                if (!(validateEmail() & validatePass())) {
                } else {
                   login_activity();
                }
            }
        });

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
            login_activity();
            return true;

        }
    }
    public static final String pass;

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
            login_activity();
            return true;
        }
    }
    private void login_activity(){
        Log.d("chat", "Login.......");
        mAuth.signInWithEmailAndPassword(Email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Log.d("FlashChat", "Problem signing in: " + task.getException());
                    showErrorDialog("There was a problem signing in");
                }
                else {
                    Intent login = new Intent(login.this, personal_details.class);
                    startActivity(login);
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
}
