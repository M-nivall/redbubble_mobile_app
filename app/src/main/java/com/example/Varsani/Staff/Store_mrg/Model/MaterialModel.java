package com.example.Varsani.Staff.Store_mrg.Model;

public class MaterialModel {
    private String requestID;
    private String name;
    private String phoneNo;
    private String items;
    private String requestDate;
    private String requestStatus;
    private String orderID;
    private String datePosted;
    private String tenderStatus;

    public MaterialModel(String requestID, String name, String phoneNo,
                         String items, String requestDate, String requestStatus,
                         String orderID) {
        this.requestID = requestID;
        this.name = name;
        this.phoneNo = phoneNo;
        this.items = items;
        this.requestDate = requestDate;
        this.requestStatus = requestStatus;
        this.orderID = orderID;
        this.datePosted = datePosted;
    }

    // Getters
    public String getRequestID() {
        return requestID;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getItems() {
        return items;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public String getOrderID() {
        return orderID;
    }

}
