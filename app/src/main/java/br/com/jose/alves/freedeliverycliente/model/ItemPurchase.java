package br.com.jose.alves.freedeliverycliente.model;

import com.google.firebase.database.DatabaseReference;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

import br.com.jose.alves.freedeliverycliente.util.ConfiguracaoFirebase;

public class ItemPurchase {

    @SerializedName("idItem")
    private String idItem;

    @SerializedName("amount")
    private int amount;

    @SerializedName("value")
    private double value;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    private String idUsuario;

    private String status = "pendente";



    public ItemPurchase() {
    }

    public ItemPurchase(String idItem, int amount, double value,
                        String name, String description, String idUsuario, String status) {
        this.idItem = idItem;
        this.amount = amount;
        this.value = value;
        this.name = name;
        this.description = description;
        this.idUsuario = idUsuario;
        this.status = status;
    }

    public void atualizarStatus() {
        HashMap<String, Object> status = new HashMap<>();
        status.put("status", getStatus());
        status.put("idItem", getIdItem());
        status.put("amount", getAmount());
        status.put("value", getValue());
        status.put("name", getName());
        status.put("description", getDescription());

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference pedidoRef = firebaseRef
                .child("lista")
                .child(getIdItem());
        pedidoRef.updateChildren(status);


    }

    public String getIdItem() {
        return idItem;
    }

    public void setIdItem(String idItem) {
        this.idItem = idItem;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
