package com.example.fooddelevery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Commande {
    private String listeAchat;
    private Double facture ;
    private String date;
    private String addresseClient;

    public Commande() {
    }

    public Commande(String listeAchat, Double facture, String addresseClient , String date) {
        this.listeAchat = listeAchat;
        this.facture = facture;
        this.addresseClient = addresseClient;
        this.date=date;
    }


    public String getListeAchat() {
        return listeAchat;
    }

    public void setListeAchat(String listeAchat) {
        this.listeAchat = listeAchat;
    }

    public Double getFacture() {
        return facture;
    }

    public void setFacture(Double facture) {
        this.facture = facture;
    }

    public String getAddresseClient() {
        return addresseClient;
    }

    public void setAddresseClient(String addresseClient) {
        this.addresseClient = addresseClient;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
