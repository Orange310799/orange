package com.example.orange;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class newinvoice extends AppCompatActivity {
    Button s1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newinvoice);
        s1=findViewById(R.id.Submit_b);
        startActivity(new Intent(newinvoice.this,generated.class));
    }
}
