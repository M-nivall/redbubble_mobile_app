package com.example.Varsani.Suppliers.Model;

public class MyRequetsModel {

    private String requestID;
    private String items;
    private String requestDate;
    private String requestStatus;
    private String quantity;


    public MyRequetsModel(String requestID, String items,
                          String requestDate, String requestStatus, String quantity) {
        this.requestID = requestID;
        this.items = items;
        this.quantity= quantity;
        this.requestDate = requestDate;
        this.requestStatus = requestStatus;
    }

    public String getRequestID() {
        return requestID;
    }


    public String getItems() {
        return items;
    }

    public String getQuantity() {

        return quantity;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getRequestStatus() {
        return requestStatus;
    }
}
