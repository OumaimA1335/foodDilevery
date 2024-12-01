package com.example.fooddelevery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Account extends AppCompatActivity {

    Button b1 ,b2 ,b3;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        b1 = findViewById(R.id.button2);
        b2 =findViewById(R.id.button3);
        b3 = findViewById(R.id.button4);
        mAuth = FirebaseAuth.getInstance();
        b3.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(Account.this, Login.class));
        });
        b2.setOnClickListener(view -> {

            startActivity(new Intent(Account.this, CommandeList.class));
        });
        b1.setOnClickListener(view -> {

            startActivity(new Intent(Account.this, UpdateProfile.class));
        });
    }

}