package com.example.fooddelevery;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CommandeList extends AppCompatActivity {

    ProgressDialog progressDialog;
    ImageView Home , Panier , Profile;
    private MyAdapterCommande myadapter;
    FirebaseFirestore db ;
    String id;
    FirebaseFirestore monCommande;
    private List<Commande> commandes = new ArrayList<>();
    RecyclerView recyclerView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching Data ...");
        progressDialog.show();
        monCommande = FirebaseFirestore.getInstance();
        LinearLayoutManager llm= new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        recyclerView4 = (RecyclerView) findViewById(R.id.View4);
        recyclerView4.setLayoutManager(llm);
        myadapter = new MyAdapterCommande(commandes);
        recyclerView4.setAdapter(myadapter);
        EventChangeListener();

    }

    private void EventChangeListener(){

        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        monCommande.collection("Commande").document(id).collection("UserBasket").orderBy("facture", Query.Direction.ASCENDING)
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
                                commandes.add(dc.getDocument().toObject(Commande.class));
                            }

                            myadapter.notifyDataSetChanged();
                            if(progressDialog.isShowing())
                                progressDialog.dismiss();

                        }


                    }
                });



    }

}