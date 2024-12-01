package com.example.fooddelevery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.UserDataWriter;
import com.google.firebase.firestore.core.UserData;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapterFood   extends  RecyclerView.Adapter<MyAdapterFood.MyViewHolder>{

    Map<String, Object> docData = new HashMap<>();
    List<Repas> RepasList;
    FirebaseFirestore db ;
    String id;
    int position=0;
    public MyAdapterFood(List<Repas> Repasliste) {
        this.RepasList=Repasliste;


    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Repas e= RepasList.get(position);

        holder.nom.setText(e.getNom());
        holder.prix.setText(String.valueOf(e.getPrix()));
        Picasso.get().load(e.getImage()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return RepasList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView nom;
        TextView prix;
        ImageView image;
        Button ajouter;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            db = FirebaseFirestore.getInstance();
             id = FirebaseAuth.getInstance().getCurrentUser().getUid();
             System.out.println(id);
            Repas e= RepasList.get(position);
            nom = itemView.findViewById(R.id.nom);
            prix = itemView.findViewById(R.id.prix);
            image = itemView.findViewById(R.id.imageView2);
            ajouter = itemView.findViewById(R.id.ajouter);
            ajouter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    docData.put("nom",nom.getText().toString());
                    docData.put("prix",Float.parseFloat(prix.getText().toString()));
                    docData.put("image", e.getImage());
                    db.collection("Panier").document(id).collection("Food").add(docData);


                }  });
            position++;
        }
    }


}
