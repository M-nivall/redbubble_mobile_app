package com.example.Varsani.Suppliers.Model;

public class ApprovedRequestModel {

    private String requestID;
    private String items;
    private String requestDate;
    private String requestStatus;
    private String quantity;
    private String color;
    private String unitPrice;
    private String totalAmount;


    public ApprovedRequestModel(String requestID, String items,
                          String requestDate, String requestStatus, String quantity,
                                String color, String unitPrice, String totalAmount) {
        this.requestID = requestID;
        this.items = items;
        this.quantity= quantity;
        this.requestDate = requestDate;
        this.requestStatus = requestStatus;
        this.color = color;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
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

    public String getColor() {
        return color;
    }
    public String getUnitPrice() {
        return unitPrice;
    }
    public String getTotalAmount() {
        return totalAmount;
    }
}
