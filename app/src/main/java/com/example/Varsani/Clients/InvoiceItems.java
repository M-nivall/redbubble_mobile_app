package com.example.Varsani.Clients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.utils.Urls;
import com.example.Varsani.Clients.Adapters.AdapterOrdersItems;
import com.example.Varsani.Clients.Models.OrderItemModal;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InvoiceItems extends AppCompatActivity {

    private List<OrderItemModal> list;
    private SessionHandler session;
    private UserModel user;
    private  ProgressBar progress_bar;
    private TextView tv_clientName,tv_orderID,tv_service,tv_bookingDate,tv_status,
            tv_serviceFee;
    private LinearLayout layout_payment;
    private EditText et_payment_reference;
    private RadioGroup rgPaymentMethods;
    private RadioButton selectedRadioButton;
    private Button btn_submit_payment,btn_receipt;
    private  String orderID,serviceName,bookingDate,paymentID,serviceFee,clientName,status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_items);

        getSupportActionBar().setTitle("Invoice Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_orderID = findViewById(R.id.tv_orderID);
        tv_clientName = findViewById(R.id.tv_clientName);
        tv_service = findViewById(R.id.tv_service);
        tv_bookingDate = findViewById(R.id.tv_bookingDate);
        tv_status = findViewById(R.id.tv_status);
        tv_serviceFee = findViewById(R.id.tv_serviceFee);
        et_payment_reference = findViewById(R.id.et_payment_reference);
        progress_bar = findViewById(R.id.progress_bar);
        btn_submit_payment = findViewById(R.id.btn_submit_payment);
        layout_payment = findViewById(R.id.layout_payment);
        btn_receipt = findViewById(R.id.btn_receipt);

        et_payment_reference.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        progress_bar.setVisibility(View.GONE);
        btn_receipt.setVisibility(View.GONE);

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        Intent intent = getIntent();
        orderID = intent.getStringExtra("orderID");
        serviceName = intent.getStringExtra("serviceName");
        bookingDate = intent.getStringExtra("bookingDate");
        paymentID = intent.getStringExtra("paymentID");
        serviceFee = intent.getStringExtra("serviceFee");
        clientName = intent.getStringExtra("clientName");
        status = intent.getStringExtra("status");


        tv_orderID.setText("Booking ID: " + orderID);
        tv_clientName.setText("Client Name: " + clientName);
        tv_service.setText("Service: " + serviceName);
        tv_bookingDate.setText("Booking Date: " + bookingDate);
        tv_status.setText("Status: " + status);
        tv_serviceFee.setText("Amount: " + serviceFee);


        session=new SessionHandler( getApplicationContext());
        user=session.getUserDetails();

        list=new ArrayList<>();


        if(status.equals("Pay Invoiced Amount")){
            btn_submit_payment.setVisibility(View.VISIBLE);
        }

        if(status.equals("Invoice Paid")){
            layout_payment.setVisibility(View.GONE);
            btn_receipt.setVisibility(View.VISIBLE);
            tv_serviceFee.setText("Amount Paid: " + serviceFee);
        }

        btn_submit_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertSubmit();
            }
        });
        btn_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),PaymentReceipt.class);
                intent.putExtra("orderID", orderID);
                intent.putExtra("serviceName", serviceName);
                intent.putExtra("bookingDate", bookingDate);
                intent.putExtra("paymentID", paymentID);
                intent.putExtra("serviceFee", serviceFee);
                startActivity(intent);
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
    public void submitPayment(){
        btn_submit_payment.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);
        final String payment_code = et_payment_reference.getText().toString().trim();
        final String paymentMethod = getSelectedPaymentMethod();

        if (TextUtils.isEmpty(paymentMethod)) {
            Toast.makeText(this, "Please select a payment method", Toast.LENGTH_SHORT).show();
            btn_submit_payment.setVisibility(View.VISIBLE);
            progress_bar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(payment_code)) {
            Toast.makeText(this, "Please enter payment code", Toast.LENGTH_SHORT).show();
            btn_submit_payment.setVisibility(View.VISIBLE);
            progress_bar.setVisibility(View.GONE);
            return;
        }
        if(payment_code.length()>10 ||payment_code.length()<10){
            Toast.makeText(this, "Payment code should contain 10 digits", Toast.LENGTH_SHORT).show();
            btn_submit_payment.setVisibility(View.VISIBLE);
            progress_bar.setVisibility(View.GONE);
            return;
        }
        if(!payment_code.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$")){
            Toast.makeText(this, "Mpesa code should have characters and digits", Toast.LENGTH_SHORT).show();
            btn_submit_payment.setVisibility(View.VISIBLE);
            progress_bar.setVisibility(View.GONE);
            return;
        }

        StringRequest stringRequest=new StringRequest(Request.Method.POST, Urls.URL_SUBMIT_INVOICE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");
                            if(status.equals("1")){
                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                finish();
                            }else{
                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            btn_submit_payment.setVisibility(View.GONE);
                            progress_bar.setVisibility(View.GONE);
                            Toast toast= Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast= Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
                btn_submit_payment.setVisibility(View.VISIBLE);
                progress_bar.setVisibility(View.GONE);

            }
        }){
            @Override
            protected Map<String,String>getParams()throws AuthFailureError{
                Map<String,String>params=new HashMap<>();
                params.put("orderID",orderID);
                params.put("clientID",user.getClientID());
                params.put("paymentID",paymentID);
                params.put("paymentCode",payment_code);
                params.put("paymentMethod", paymentMethod);
                return  params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    private String getSelectedPaymentMethod() {
        rgPaymentMethods = findViewById(R.id.rg_payment_methods);
        int selectedId = rgPaymentMethods.getCheckedRadioButtonId();

        if (selectedId == -1) {
            // No option selected
            return null;
        }

        selectedRadioButton = findViewById(selectedId);
        return selectedRadioButton.getText().toString();
    }
    private void alertSubmit() {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Set title and message for the alert
        builder.setTitle("Confirm Payment")
                .setMessage("Are you sure you want to proceed with the payment? Please verify the details and ensure the payment reference code is correct.")
                //.setIcon(R.drawable.ic_payment) // Optional: payment-related icon

                // Set the positive button and its click listener
                .setPositiveButton("Yes, Proceed", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Call the submitPayment method
                        submitPayment();
                    }
                })

                // Set the negative button and its click listener
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });

        // Create and show the AlertDialog
        AlertDialog alert = builder.create();
        alert.show();

        // Customize button styles
        alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));
        alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
    }

}
