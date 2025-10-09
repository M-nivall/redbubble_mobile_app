package com.example.Varsani.Staff.ServMrg.Models;

public class BookingModel {
    private String orderID;
    private String clientID;
    private String county;
    private String town;
    private String address;
    private String expectedDate;
    private String orderDate;
    private String clientName;
    private String phoneNo;
    private String orderStatus;

    // Constructor
    public BookingModel(String orderID, String clientID, String county, String town, String address, String expectedDate,
                            String orderDate, String clientName, String phoneNo, String orderStatus) {
        this.orderID = orderID;
        this.clientID = clientID;
        this.county = county;
        this.town = town;
        this.address = address;
        this.expectedDate = expectedDate;
        this.orderDate = orderDate;
        this.clientName = clientName;
        this.phoneNo = phoneNo;
        this.orderStatus = orderStatus;
    }

    // Getter methods
    public String getOrderID() {
        return orderID;
    }
    public String getClientID() {
        return clientID;
    }

    public String getCounty() {
        return county;
    }

    public String getTown() {
        return town;
    }

    public String getAddress() {
        return address;
    }

    public String getExpectedDate() {
        return expectedDate;
    }
    public String getOrderDate() {
        return orderDate;
    }

    public String getClientName() {
        return clientName;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
