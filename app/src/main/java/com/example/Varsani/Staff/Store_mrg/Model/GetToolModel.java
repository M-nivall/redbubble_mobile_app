package com.example.Varsani.Staff.Store_mrg.Model;


public class GetToolModel {
    String stockID;
    String category;
    String quantity;
    String color;
    String description;

    public GetToolModel(String stockID, String category, String quantity, String color, String description){
        this.stockID=stockID;
        this.category=category;
        this.quantity=quantity;
        this.color=color;
        this.description=description;

    }

    public String getStockID() {
        return stockID;
    }

    public String getCategory() {
        return category;
    }

    public String getQuantity() {
        return quantity;
    }
    public String getColor() {
        return color;
    }
    public String getDescription() {
        return description;
    }

}
