package com.example.Varsani.Clients.Models;

public class InvoiceModal {
    private String orderID;
    private String serviceName;
    private String orderDate;
    private String paymentId;
    private String serviceFee;
    private String clientName;
    private String orderStatus;

    // Constructor
    public InvoiceModal(String orderID, String serviceName, String orderDate, String paymentId,
                        String serviceFee, String clientName, String orderStatus) {
        this.orderID = orderID;
        this.serviceName = serviceName;
        this.orderDate = orderDate;
        this.paymentId = paymentId;
        this.serviceFee = serviceFee;
        this.clientName = clientName;
        this.orderStatus = orderStatus;
    }

    // Getters
    public String getOrderID() {
        return orderID;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public String getClientName() {
        return clientName;
    }

    public String getOrderStatus() {
        return orderStatus;
    }
}
