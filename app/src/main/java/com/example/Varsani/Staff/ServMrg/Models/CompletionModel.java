package com.example.Varsani.Staff.ServMrg.Models;

public class CompletionModel {
    String orderID;
    String paymentID;
    String clientName;
    String payment_code;
    String payment_mode;
    String payment_date;
    String service_fee;
    String paymentStatus;
    String phoneNo;
    String email;
    String rating;

    public CompletionModel(String orderID, String paymentID, String clientName, String payment_code,
                        String payment_mode,String payment_date
            ,String service_fee,String paymentStatus, String phoneNo, String email, String rating){
        this.orderID=orderID ;
        this.clientName=clientName ;
        this.paymentID=paymentID ;
        this.payment_code=payment_code ;
        this.payment_mode=payment_mode ;
        this.payment_date=payment_date ;
        this.service_fee=service_fee ;
        this.paymentStatus=paymentStatus ;
        this.phoneNo=phoneNo;
        this.email=email;
        this.rating=rating;
    }

    public String getOrderID() {
        return orderID;
    }

    public String getPaymentID() {
        return paymentID;
    }

    public String getClientName() {
        return clientName;
    }

    public String getPayment_code() {
        return payment_code;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public String getService_fee() {
        return service_fee;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }
    public String getPhoneNo() {
        return phoneNo;
    }
    public String getEmail() {
        return email;
    }
    public String getRating() {
        return rating;
    }
}
