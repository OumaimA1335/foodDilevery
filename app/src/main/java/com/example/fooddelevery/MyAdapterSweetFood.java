package com.example.fooddelevery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyAdapterSweetFood extends  RecyclerView.Adapter<MyAdapterSweetFood.MyViewHolder>{

    private final List<Repas> ListeSweet;
    Map<String, Object> docData = new HashMap<>();
    String id;
    FirebaseFirestore db ;
    int position=0;

    public MyAdapterSweetFood(List<Repas> ListeSweet) {
        this.ListeSweet=ListeSweet;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart, parent, false);
        return new MyAdapterSweetFood.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Repas e= ListeSweet.get(position);

        holder.nom.setText(e.getNom());
        holder.prix.setText(String.valueOf(e.getPrix()));
        Picasso.get().load(e.getImage()).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return ListeSweet.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView nom;
        TextView prix;
        ImageView image;
        Button ajouter;

        public MyViewHolder(@NonNull View itemView ) {
            super(itemView);
            Repas e= ListeSweet.get(position);
            db = FirebaseFirestore.getInstance();
            id = FirebaseAuth.getInstance().getCurrentUser().getUid();
            nom = itemView.findViewById(R.id.nom);
            prix = itemView.findViewById(R.id.prix);
            image = itemView.findViewById(R.id.imageView2);
            ajouter = itemView.findViewById(R.id.ajouter);
            ajouter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    docData.put("nom",nom.getText().toString());
                    docData.put("prix",Float.parseFloat(prix.getText().toString()));
                    docData.put("image",e.getImage());
                    db.collection("Panier").document(id).collection("Food").add(docData);


                }  });
            position++;
        }
    }
}
