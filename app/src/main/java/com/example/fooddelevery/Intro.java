package com.example.fooddelevery;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.remote.WatchChange;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class Intro extends AppCompatActivity {

    private List<Repas> RepasList = new ArrayList<>();
    private List<Repas> ListSweet = new ArrayList<>();
    RecyclerView recyclerView1,recyclerView2;
    private MyAdapterFood myadapter;
    private MyAdapterSweetFood myadapter2;
    FirebaseFirestore ListFood;
    ProgressDialog progressDialog;
    ImageView Home , Panier , Profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                PackageManager.PERMISSION_GRANTED);

        Home = findViewById(R.id.Home);
        Panier = findViewById(R.id.Panier);
        Profile= findViewById(R.id.Profile);
        Home.setOnClickListener(view -> {
            Intent i = new Intent(Intro.this ,Intro.class);
            startActivity(i);
        });

        Panier.setOnClickListener(view -> {
            Intent i = new Intent(Intro.this ,Basket.class);
            startActivity(i);
        });
        Profile.setOnClickListener(view -> {
            Intent i = new Intent(Intro.this,Account.class);
            startActivity(i);
        });


        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data ...");
        progressDialog.show();
        ListFood = FirebaseFirestore.getInstance();
        LinearLayoutManager llm= new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recyclerView1 = (RecyclerView) findViewById(R.id.View1);
        recyclerView1.setLayoutManager(llm);
        myadapter = new MyAdapterFood(RepasList);
        recyclerView1.setAdapter(myadapter);

        EventChangeListener();

        LinearLayoutManager llm2= new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recyclerView2 = (RecyclerView) findViewById(R.id.View2);
        recyclerView2.setLayoutManager(llm2);
        myadapter2 = new MyAdapterSweetFood(ListSweet);
        recyclerView2.setAdapter(myadapter2);

        EventChangeListener2();


    }
    private void EventChangeListener(){
        ListFood.collection("repas").orderBy("nom", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error !=null)
                        {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("FireStore error" , error.getMessage());
                            return;
                        }
                        for(DocumentChange dc :value.getDocumentChanges()){
                            if(dc.getType()== DocumentChange.Type.ADDED)
                            {
                                RepasList.add(dc.getDocument().toObject(Repas.class));

                            }
                            myadapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                    }
                });
    }

    private void EventChangeListener2(){
        ListFood.collection("sweetFood").orderBy("nom", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error !=null)
                        {
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();
                            Log.e("FireStore error" , error.getMessage());
                            return;
                        }
                        for(DocumentChange dc :value.getDocumentChanges()){
                            if(dc.getType()== DocumentChange.Type.ADDED)
                            {
                                ListSweet.add(dc.getDocument().toObject(Repas.class));

                            }
                            myadapter2.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                    }
                });
    }

}