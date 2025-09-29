package com.example.Varsani.Clients.Models;


public class ServicesModal {

    private String productID;
    private String productName;
    private String stock;
    private String price;
    private String imgUrl;
    private String desc;
    private String category;


    public ServicesModal() {
    }

    public ServicesModal(String productID, String productName, String stock, String price, String imgUrl,String desc, String category) {
        this.productID = productID;
        this.productName = productName;
        this.stock = stock;
        this.price = price;
        this.imgUrl = imgUrl;
        this.desc = desc;
        this.category=category;
    }

    public String getProductID() {
        return productID;
    }

    public String getProductName() {
        return productName;
    }

    public String getStock() {
        return stock;
    }

    public String getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDesc() {
        return desc;
    }
    public String getCategory() {
        return category;
    }
}
