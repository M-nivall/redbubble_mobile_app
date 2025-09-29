package com.example.Varsani.Staff.ServMrg.Models;

public class CompletedModel {
    private String orderID;
    private String servName;
    private String clientName;
    private String orderDate;
    private String expectedDate;
    private String address;
    private String techName;
    private String orderRemark;
    private String orderStatus;
    private String county;
    private String town;

    public CompletedModel(String orderID, String clientName, String servName, String orderDate,
                          String expectedDate, String address, String techName,
                          String orderRemark, String orderStatus, String county, String town) {
        this.orderID = orderID;
        this.servName = servName;
        this.clientName = clientName;
        this.orderDate = orderDate;
        this.expectedDate = expectedDate;
        this.address = address;
        this.techName = techName;
        this.orderRemark = orderRemark;
        this.orderStatus = orderStatus;
        this.county = county;
        this.town = town;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getServName() {
        return servName;
    }

    public String getClientName() {
        return clientName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public String getAddress() {
        return address;
    }

    public String getTechName() {
        return techName;
    }

    public String getOrderRemark() {
        return orderRemark;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public String getCounty() {
        return county;
    }

    public String getTown() {
        return town;
    }
}

