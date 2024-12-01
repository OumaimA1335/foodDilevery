package com.example.fooddelevery;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.logging.Handler;

public class MyAdapterBasket extends  RecyclerView.Adapter<MyAdapterBasket.MyViewHolder> {
    List<Repas> RepasList;
    String idUser;

    public MyAdapterBasket(List<Repas> Repasliste) {
        this.RepasList = Repasliste;


    }

    @NonNull
    @Override
    public MyAdapterBasket.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new MyAdapterBasket.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapterBasket.MyViewHolder holder, int position) {

        Repas e = RepasList.get(position);

        holder.nom.setText(e.getNom());
        holder.prix.setText(String.valueOf(e.getPrix()));
        Picasso.get().load(e.getImage()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return RepasList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nom;
        TextView prix;
        ImageView image;
        ImageView delete;

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            nom = itemView.findViewById(R.id.nom1);
            prix = itemView.findViewById(R.id.prix1);
            image = itemView.findViewById(R.id.image1);
            delete = itemView.findViewById(R.id.delete);


            delete.setOnClickListener(view -> {
                db.collection("Panier").document(idUser).collection("Food").whereEqualTo("nom",nom.getText().toString()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
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


                                System.out.println("Success");

                            }
                        });


                    }
                });


            });
        }
    }
}
