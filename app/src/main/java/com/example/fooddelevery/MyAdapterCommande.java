package com.example.fooddelevery;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MyAdapterCommande extends RecyclerView.Adapter<MyAdapterCommande.MyViewHolder> {

    List<Commande> CommandeList;
    String idUser;

    public MyAdapterCommande(List<Commande> Commandeliste) {
        this.CommandeList = Commandeliste;


    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new MyAdapterCommande.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Commande e = CommandeList.get(position);

     holder.nom.setText(e.getListeAchat());
        holder.prix.setText(String.valueOf(e.getFacture())+" Dt");
        holder.date.setText(e.getDate().toString());




    }

    @Override
    public int getItemCount() {
        return CommandeList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nom;
        TextView prix;
        TextView date;


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String idUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            nom = itemView.findViewById(R.id.noms);
            prix = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.adress);



        }
    }
}
