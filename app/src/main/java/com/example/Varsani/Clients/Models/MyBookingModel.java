package com.example.Varsani.Clients.Models;

public class MyBookingModel {
    private String orderID;
    private String totalCost;
    private String paymentCode;
    private String oderDate;
    private String expectedDate;
    private String orderStatus;



    public MyBookingModel(String orderID, String totalCost, String paymentCode,
                          String oderDate, String expectedDate, String orderStatus){
        this.orderID=orderID;
        this.totalCost=totalCost;
        this.paymentCode=paymentCode;
        this.oderDate=oderDate;
        this.expectedDate=expectedDate;
        this.orderStatus=orderStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public String getOderDate() {
        return oderDate;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

}
