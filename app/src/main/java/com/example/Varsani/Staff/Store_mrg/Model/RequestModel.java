package com.example.Varsani.Staff.Store_mrg.Model;

public class RequestModel {

    private String requestID;
    private String name;
    private String phoneNo;
    private String items;
    private String requestDate;
    private String requestStatus;
    private String amount;
    private String color;

    private String bidID;
    private String quantity;
    private String unitPrice;

    public RequestModel(String requestID, String name, String phoneNo,
                        String items, String requestDate, String requestStatus, String amount, String color,
                        String bidID, String quantity, String unitPrice) {
        this.requestID = requestID;
        this.name = name;
        this.phoneNo = phoneNo;
        this.items = items;
        this.amount = amount;
        this.requestDate = requestDate;
        this.requestStatus = requestStatus;
        this.color = color;
        this.bidID = bidID;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

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
    public String getAmount() {
        return amount;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public String getRequestStatus() {
        return requestStatus;
    }
    public String getColor() {return color; }

    public String getBidID() {return bidID; }
    public String getQuantity() {return quantity; }
    public String getUnitPrice() {return unitPrice; }
}
