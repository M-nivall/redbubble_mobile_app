package com.example.Varsani.Staff.Finance;

import static com.example.Varsani.utils.Urls.URL_ACCEPT;
import static com.example.Varsani.utils.Urls.URL_GET_APPROVE_ORDERS;
import static com.example.Varsani.utils.Urls.URL_PAY_SUPPLIER;


import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Finance.OrderDetails;
import com.example.Varsani.utils.SessionHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PaySupplier extends AppCompatActivity {
    private TextView txv_id,txv_supplierName,txv_amount,txv_amount2,txv_paymentdesc,txv_paymentstatus,txv_payment_date;

    private Button btn_submit;
    private ProgressBar progressBar;
    private String id,requestID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_supplier);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txv_id =findViewById(R.id.txv_supplyid);
        txv_supplierName =findViewById(R.id.txv_supplierName);
        txv_amount = findViewById(R.id.txv_amount);
        txv_amount2 = findViewById(R.id.txv_amount2);
        txv_paymentdesc = findViewById(R.id.txv_paymentdescription);
        txv_paymentstatus = findViewById(R.id.txv_paymentstatus);
        txv_payment_date = findViewById(R.id.txv_paymentdate);

        progressBar=findViewById(R.id.progressBar);
        btn_submit=findViewById(R.id.paysupplier);


        progressBar.setVisibility(View.GONE);

        Intent in=getIntent();
        id=in.getStringExtra("id");
        requestID = in.getStringExtra("requestID");
        txv_id.setText("Serial:"+in.getStringExtra("id"));
        txv_supplierName.setText("Payment To:"+in.getStringExtra("supplierName"));
        txv_amount.setText("Amount:"+in.getStringExtra("amount"));
        txv_amount2.setText("Kes:"+in.getStringExtra("amount"));
        txv_paymentdesc.setText("Supply Of :"+in.getStringExtra("payment_description"));
        txv_payment_date.setText("Invoiced On:"+in.getStringExtra("payment_date"));
        txv_paymentstatus.setText("Status:"+in.getStringExtra("payment_status"));

        btn_submit.setOnClickListener(v-> alertApprove());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void approve(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_PAY_SUPPLIER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");
                            if (status.equals("1")){

                                Toast toast= Toast.makeText(PaySupplier.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                finish();
                            }else{

                                Toast toast= Toast.makeText(PaySupplier.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast= Toast.makeText(PaySupplier.this, e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast= Toast.makeText(PaySupplier.this, error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
            }
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("id",id);
                params.put("requestID",requestID);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void alertApprove(){
        android.app.AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Pay Supplier");
        alertDialog.setCancelable(false);
        alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                return;
            }
        });
        alertDialog.setButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                approve();
                return;
            }
        });
        alertDialog.show();
    }
}