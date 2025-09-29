package com.example.Varsani.Staff.Store_mrg.Model;


public class GetToolModel {
    String toolID;
    String toolName;
    String quantity;

    public GetToolModel(String toolID, String toolName, String quantity){
        this.toolName=toolName;
        this.quantity=quantity;
        this.toolID=toolID;
    }

    public String getToolID() {
        return toolID;
    }

    public String getToolName() {
        return toolName;
    }

    public String getQuantity() {
        return quantity;
    }

}
