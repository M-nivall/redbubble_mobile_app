package com.example.Varsani.Staff.Store_mrg.Model;

public class MaterialModel {
    private String orderID;
    private String techName;
    private String dateAssigned;
    private String clientName;
    private String releaseState;

    public MaterialModel(String orderID, String techName, String dateAssigned,
                         String clientName, String releaseState) {
        this.orderID = orderID;
        this.techName = techName;
        this.dateAssigned = dateAssigned;
        this.clientName = clientName;
        this.releaseState = releaseState;
    }

    // Getters
    public String getOrderID() {
        return orderID;
    }

    public String getTechName() {
        return techName;
    }

    public String getDateAssigned() {
        return dateAssigned;
    }

    public String getClientName() {
        return clientName;
    }

    public String getReleaseState() {
        return releaseState;
    }

}
