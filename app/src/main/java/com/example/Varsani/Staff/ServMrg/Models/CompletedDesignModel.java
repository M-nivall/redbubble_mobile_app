package com.example.Varsani.Staff.ServMrg.Models;

public class CompletedDesignModel {
    private String orderID;
    private String clientID;
    private String businessName;
    private String servName;
    private String dimension;
    private String serviceDesc;
    private String installationType;
    private String inputText;
    private String sketchImg;
    private String logoImg;
    private String expectedDate;
    private String clientName;
    private String orderDate;
    private String address;
    private String orderStatus;
    private String county;
    private String town;
    private String pdf_design;
    private String phone_no;

    // Constructor
    public CompletedDesignModel(String orderID, String clientID, String businessName, String servName, String dimension, String serviceDesc,
                            String installationType, String inputText, String sketchImg, String logoImg, String expectedDate,
                            String clientName, String orderDate, String address, String orderStatus, String county, String town, String pdf_design, String phone_no) {
        this.orderID = orderID;
        this.clientID = clientID;
        this.businessName = businessName;
        this.servName = servName;
        this.dimension = dimension;
        this.serviceDesc = serviceDesc;
        this.installationType = installationType;
        this.inputText = inputText;
        this.sketchImg = sketchImg;
        this.logoImg = logoImg;
        this.expectedDate = expectedDate;
        this.clientName = clientName;
        this.orderDate = orderDate;
        this.address = address;
        this.orderStatus = orderStatus;
        this.county = county;
        this.town = town;
        this.pdf_design = pdf_design;
        this.phone_no = phone_no;
    }

    // Getter methods
    public String getOrderID() {
        return orderID;
    }
    public String getClientID() {
        return clientID;
    }

    public String getBusinessName() {
        return businessName;
    }

    public String getServName() {
        return servName;
    }

    public String getDimension() {
        return dimension;
    }

    public String getServiceDesc() {
        return serviceDesc;
    }

    public String getInstallationType() {
        return installationType;
    }

    public String getInputText() {
        return inputText;
    }

    public String getSketchImg() {
        return sketchImg;
    }

    public String getLogoImg() {
        return logoImg;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public String getClientName() {
        return clientName;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public String getAddress() {
        return address;
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
    public String getPdf_design() {
        return pdf_design;
    }
    public String getPhone_no() {
        return phone_no;
    }
}
