package com.example.fooddelevery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateProfile extends AppCompatActivity {

    EditText pass , email ,crruPass;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        email = findViewById(R.id.UpEmail);
        pass = findViewById(R.id.UpPassword);
        crruPass = findViewById(R.id.OldPassword);
        update = findViewById(R.id.update);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            update.setOnClickListener(view -> {
                if(email.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
                else if(pass.getText().toString().isEmpty()){
                    Toast.makeText(this, "Please enter your crruent password", Toast.LENGTH_SHORT).show();
                }
                else if(pass.getText().toString().isEmpty())
                {
                    Toast.makeText(this, "Please enter  your  new password", Toast.LENGTH_SHORT).show();
                }
                else {
                    AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(),crruPass.getText().toString());
                    user.reauthenticate(authCredential).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            user.updateEmail(email.getText().toString());
                            user.updatePassword(pass.getText().toString());

                        }
                    });
                }
                Toast.makeText(this, "Password and email updated", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(UpdateProfile.this,Login.class );
                startActivity(i);
            });


        }

}