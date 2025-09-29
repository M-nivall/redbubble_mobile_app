package com.example.Varsani.Clients.Models;


public class CartModal {

    private String productID;
    private String productName;
    private String quantity;
    private String price;
    private String imgUrl;
    private String itemID;
    private String subToatl;
    private String stock;
    private String orderID;


    public CartModal(String orderID, String productID, String productName, String quantity, String price,
                     String imgUrl, String itemID ,String subToatl,String stock) {
        this.orderID = orderID;
        this.productID = productID;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.imgUrl = imgUrl;
        this.itemID = itemID;
        this.subToatl = subToatl;
        this.stock=stock;
    }

    public String getProductID() {
        return productID;
    }
    public String getOrderID() {
        return orderID;
    }

    public String getProductName() {
        return productName;
    }


    public String getPrice() {
        return price;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getItemID() {
        return itemID;
    }

    public String getSubToatl() {
        return subToatl;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getStock() {
        return stock;
    }
}
