package com.example.Varsani.Staff.Models;

public class SupplyPaymentsModel {

    private String id;
    private String supplierID;
    private String supplierName;
    private String amount;
    private String payment_description;
    private String payment_status;
    private String payment_date;
    private  String requestID;


    public String getId() {
        return id;
    }

    public String getSupplierID() {
        return supplierID;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getAmount() {
        return amount;
    }

    public String getPayment_description() {
        return payment_description;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public String getPayment_date() {
        return payment_date;
    }
    public String getRequestID() {
        return requestID;
    }

    public SupplyPaymentsModel(String id, String supplierID, String supplierName, String amount, String payment_description, String payment_status, String payment_date, String requestID) {
        this.id = id;
        this.supplierName =supplierName;
        this.supplierID = supplierID;
        this.amount = amount;
        this.payment_description = payment_description;
        this.payment_status = payment_status;
        this.payment_date = payment_date;
        this.requestID = requestID;
    }
}
