package com.example.Varsani.Staff.ServMrg;

import static com.example.Varsani.utils.Urls.URL_ASSIGN_DESIGNER;
import static com.example.Varsani.utils.Urls.URL_CONFIRM_COMPLETE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApproveCompletion extends AppCompatActivity {
    private TextView textClientName, textAddress, textCounty, textTown,textCustomerRemarks;
    private TextView textOrderID, textDate, textTechName, textService, textStatus ;
    private Button btn_approve_completion;
    private  String orderID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_completion);

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
        btn_approve_completion = findViewById(R.id.btn_approve_completion);

        // Get data from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            // Retrieve each extra from the intent
            orderID = intent.getStringExtra("orderID");
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
        btn_approve_completion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlertConfirm(v);
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void confirm() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CONFIRM_COMPLETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("RESPONSE", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (status.equals("1")) {
                                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();
                                finish();
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 250);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 250);
                toast.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("orderID", orderID);
                Log.e("Params", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void getAlertConfirm(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Work Completion");

        builder.setMessage("Are you sure you want to confirm work completion?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //progressBar1.setVisibility(View.VISIBLE);
                confirm();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}