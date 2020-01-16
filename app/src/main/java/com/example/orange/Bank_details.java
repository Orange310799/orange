package com.example.orange;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.orange.Signup.inputEmail;
import static com.example.orange.Signup.inputpass;

public class Bank_details extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public EditText b_name;
    public EditText br_name;
    public EditText acc_no;
    public EditText c_acc_no;
    public EditText ifsc;
    //private EditText inputemail;
    //private EditText inputpass;
    private FirebaseAuth mAuth;
    Button Signup;
    public Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_details);
        b_name= findViewById(R.id.Bank_name);
        br_name= findViewById(R.id.Branch_name);
        acc_no =findViewById(R.id.Account_no);
        c_acc_no=findViewById(R.id.confirm_acc_no);
        ifsc=findViewById(R.id.Ifsc_code);
        //inputemail=(EditText) findViewById(R.id.Signupemail);
        //inputpass= (EditText) findViewById(R.id.signup_pass);
        //String value = getIntent().getExtras().getString("key");
        mAuth=FirebaseAuth.getInstance();
        Signup = (Button) findViewById(R.id.Signup_bank);
        spinner=findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.accounttype,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 Log.d("fist call", "registeruser");
                 if (!validate()){showErrorDialog("Check Details");}
                 else{registerUser();}

            }
        });
    }
    public void registerUser(){
        String email = inputEmail.getText().toString().trim();
        String pass = inputpass.getText().toString().trim();
        Log.d("3","1");
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Log.d("FlashChat", "Problem signing in: " + task.getException());
                    showErrorDialog("There was a problem signing in");}
                else{
                    Intent step2 = new Intent(Bank_details.this,Dashboard.class);
                    startActivity(step2);
                }
            }
        });

    }
    public boolean validate(){
        boolean valid=true;
        String bname =b_name.getText().toString().trim();
        String brname= br_name.getText().toString().trim();
        String accno= acc_no.getText().toString().trim();
        String caccno=c_acc_no.getText().toString().trim();
        String ifsc1=ifsc.getText().toString().trim();
        Log.d("2","2");
        if (bname.isEmpty()||bname.length()>35){
            b_name.setError("Enter a Valid Bank");
        }Log.d("2","3");
        if (brname.isEmpty()||brname.length()>15){
            br_name.setError("Enter a valid branch");
            valid = false;
        }Log.d("2","4");
        if(accno.isEmpty()||accno.length()>20){
            acc_no.setError("Enter a valid account number");
            valid= false;
        }
         Log.d("2", "5");
        if (Objects.equals(accno,caccno)) {
        } else {
            c_acc_no.setError("Account Number and Confirm Account Number Doesnt match");
            valid= false;
        }
        Log.d("2","6");
        if(ifsc1.isEmpty()||ifsc1.length()>10){
            ifsc.setError("Enter a valid IFSC code");
            valid= false;
        }Log.d("2","7");
        return valid;

    }
    private void showErrorDialog(String message){
        new AlertDialog.Builder(this)
                .setTitle("Oops")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null )
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String text = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(),text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
