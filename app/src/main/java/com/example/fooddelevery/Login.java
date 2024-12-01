package com.example.fooddelevery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText Email ,Password;
    Button login ;
    TextView ToSignUp;
    FirebaseAuth myAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Email = findViewById(R.id.Email);
        Password=findViewById(R.id.Password);
        ToSignUp = findViewById(R.id.ToSignUp);
        login = findViewById(R.id.login);
        myAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(view -> {
            loginUser();
        });

    }

    private void loginUser(){
        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if (email.isEmpty()){
            Email.setError("Email cannot be empty");
            Email.requestFocus();
        }else if (password.isEmpty()){
            Password.setError("Password cannot be empty");
            Password.requestFocus();
        }else{
            myAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Login.this, "User logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Login.this, Intro.class));
                    }else{
                        Toast.makeText(Login.this, "Log in Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    public void ToSignUP(View v)
    {
        Intent i = new Intent(Login.this ,SignUp.class);
        startActivity(i);
    }
}