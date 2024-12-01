package com.example.fooddelevery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Basket extends AppCompatActivity {

    private List<Repas> panier = new ArrayList<>();

    RecyclerView recyclerView1;
    private MyAdapterBasket myadapter;
    FirebaseFirestore monPanier;
    ProgressDialog progressDialog;
    ImageView Home , Panier , Profile;
    TextView text ;
    String id;
    Button checkOut;
    double Total;
    Map<String, Object> Commande = new HashMap<>();
    FirebaseFirestore db ;
    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data ...");
        progressDialog.show();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        checkOut= findViewById(R.id.CheckOut);
        text= findViewById(R.id.Total);

        monPanier = FirebaseFirestore.getInstance();
        LinearLayoutManager llm= new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView1 = (RecyclerView) findViewById(R.id.View3);
        
        recyclerView1.setLayoutManager(llm);
        myadapter = new MyAdapterBasket(panier);
        recyclerView1.setAdapter(myadapter);
        EventChangeListener();
        Home = findViewById(R.id.Home2);
        Profile = findViewById(R.id.Profile2);
        Home.setOnClickListener(view -> {
           Intent i = new Intent(Basket.this,Intro.class);
           startActivity(i);
        });
        Profile.setOnClickListener(view -> {
            Intent i = new Intent(Basket.this,Account.class);
            startActivity(i);
        });


    }
    private void EventChangeListener(){

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        monPanier.collection("Panier").document(id).collection("Food").orderBy("nom", Query.Direction.ASCENDING)
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
                                panier.add(dc.getDocument().toObject(Repas.class));

                                Repas r = panier.get(panier.size()-1);
                                Total =Total+r.getPrix();


                            }

                            myadapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();

                        }
                        text.setText(String.valueOf(Total)+" DT");


                    }
                });



    }


    public void checkOut(View v) {

        db = FirebaseFirestore.getInstance();
     List<String> plats = new ArrayList<>();
        SimpleDateFormat s = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        String ch ="";
        for (int i =0;i<panier.size();i++)
        {
            ch = ch +" "+panier.get(i).getNom();

        }
        System.out.println("mon panier"+ch);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            String finalCh = ch;
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {
                                Geocoder geocoder = new Geocoder(Basket.this, Locale.getDefault());
                                try {
                                    List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    System.out.println(addresses.get(0).getLocality() + " " + addresses.get(0).getCountryName() + panier);
                                    String MonAdresse = addresses.get(0).getAddressLine(0);


                                    Commande.put("addresseClient", MonAdresse);
                                    Commande.put("listeAchat", finalCh);
                                    Commande.put("facture", Total);

                                        Commande.put("date",s.format(date));

                                    db.collection("Commande").document(id).collection("UserBasket").add(Commande);
                                    Toast.makeText(Basket.this, "Order added successfully", Toast.LENGTH_SHORT).show();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

        }

        db.collection("Panier").document(id).collection("Food").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                          @Override
                                          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                              WriteBatch b = FirebaseFirestore.getInstance().batch();
                                              List<DocumentSnapshot> s = queryDocumentSnapshots.getDocuments();
                                              for (DocumentSnapshot snapshot : s) {
                                                  b.delete(snapshot.getReference());
                                              }
                                              b.commit().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                  @Override
                                                  public void onSuccess(Void unused) {
                                                      Toast.makeText(Basket.this, "Successfull deleted", Toast.LENGTH_SHORT).show();
                                                      Intent i = new Intent(Basket.this ,Intro.class);
                                                      startActivity(i);
                                                  }
                                              });

                                          }
                                      }
                );

    }





}