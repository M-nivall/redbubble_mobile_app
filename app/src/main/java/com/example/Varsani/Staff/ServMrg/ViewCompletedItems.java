package com.example.Varsani.Staff.ServMrg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.Varsani.R;

public class ViewCompletedItems extends AppCompatActivity {

    private TextView textClientName, textAddress, textCounty, textTown,textCustomerRemarks;
    private TextView textOrderID, textDate, textTechName, textService, textStatus ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_completed_items);

        // Initialize TextViews
        textClientName = findViewById(R.id.textClientName);
        textAddress = findViewById(R.id.textAddress);
        textCounty = findViewById(R.id.textCounty);
        textTown = findViewById(R.id.textTown);
        textOrderID = findViewById(R.id.textOrderID);
        textDate = findViewById(R.id.textDate);
        textTechName = findViewById(R.id.textTechName);
        textService = findViewById(R.id.textService);
        textStatus = findViewById(R.id.textStatus);
        textCustomerRemarks = findViewById(R.id.textCustomerRemarks);

        // Get data from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            // Retrieve each extra from the intent
            String orderID = intent.getStringExtra("orderID");
            String servName = intent.getStringExtra("servName");
            String clientName = intent.getStringExtra("clientName");
            String orderDate = intent.getStringExtra("orderDate");
            String expectedDate = intent.getStringExtra("expectedDate");
            String address = intent.getStringExtra("address");
            String techName = intent.getStringExtra("techName");
            String orderRemark = intent.getStringExtra("orderRemark");
            String orderStatus = intent.getStringExtra("orderStatus");
            String county = intent.getStringExtra("county");
            String town = intent.getStringExtra("town");
            // Set the data to the TextViews
            textClientName.setText("Client Name: " + clientName);
            textAddress.setText("Address: " + address);
            textCounty.setText("County: " + county);
            textTown.setText("Town: " + town);
            textOrderID.setText("Booking ID: " + orderID);
            textDate.setText("Date: " + expectedDate);
            textTechName.setText("Technician Name: " + techName);
            textService.setText("Service: " + servName);
            textStatus.setText("Status: " + orderStatus);
            textCustomerRemarks.setText("Remarks: " + orderRemark);
        }

    }
}