package com.example.orange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class personal_details extends AppCompatActivity {
    Button s3;
    public EditText Fname;
    private EditText surname;
    private EditText phone;
    private EditText pan;
    private EditText address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);
        Fname=findViewById(R.id.name);
        surname=findViewById(R.id.surname);
        phone=findViewById(R.id.phoneno);
        pan=findViewById(R.id.panno);
        address=findViewById(R.id.address);
        s3=findViewById(R.id.Continue_b);
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    Intent Intent_4 = new Intent(personal_details.this, Bank_details.class);
                    startActivity(Intent_4);
                }            }
        });
    }
    private boolean validate(){
        boolean validate=true;
        String name=Fname.getText().toString().trim();
        String Surname=surname.getText().toString().trim();
        String phn = phone.getText().toString().trim();
        String pn= pan.getText().toString().trim();
        String addr= address.getText().toString().trim();
        if (name.isEmpty()){Fname.setError("Name cannot be empty"); validate=false;}
        if (Surname.isEmpty()){surname.setError("Last Name cannot be Empty"); validate=false;}
        if (phn.length() != 10){phone.setError("Enter a Valid Phone Number"); validate=false;}
        if (pn.isEmpty()){pan.setError("Pan No cannot be Empty"); validate=false;}
        if (addr.isEmpty()){address.setError("Address cannot be Empty"); validate=false;}
        return validate;
    }
}
