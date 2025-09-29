package com.example.Varsani.Staff.Store_mrg;

import static com.example.Varsani.utils.Urls.URL_APPROVE_MATERIALS;
import static com.example.Varsani.utils.Urls.URL_APPROVE_TENDER;
import static com.example.Varsani.utils.Urls.URL_GET_APPROVE_ORDERS;

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
import android.widget.EditText;
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

public class ApproveMaterials extends AppCompatActivity {

    private TextView txv_requestID,txv_technician,txv_items,
            txv_phone_no, txv_requestStatus ,txv_date,txv_status;

    private Button btn_submit;
    private ProgressBar progressBar;
    private String requestID, supplierID,items;

    private SessionHandler session;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_materials);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txv_items =findViewById(R.id.txv_items);
        txv_requestID = findViewById(R.id.txv_tenderID);
        txv_technician = findViewById(R.id.txv_technician);
        txv_phone_no = findViewById(R.id.txv_phone_no);
        txv_status = findViewById(R.id.txv_status);
        progressBar=findViewById(R.id.progressBar);
        btn_submit=findViewById(R.id.btn_submit);
        txv_date = findViewById(R.id.txv_date);

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        progressBar.setVisibility(View.GONE);

        Intent in=getIntent();
        requestID=in.getStringExtra("requestID");
        items=in.getStringExtra("items");


        txv_requestID.setText("#ID: "+in.getStringExtra("requestID"));
        txv_technician.setText("Technician: "+in.getStringExtra("technician"));
        txv_items.setText("Items: "+in.getStringExtra("items"));
        txv_phone_no.setText("Phone No: "+in.getStringExtra("phoneNo"));
        txv_status.setText("Status :"+in.getStringExtra("status"));
        txv_date.setText("Date :"+in.getStringExtra("requestDate"));

        btn_submit.setOnClickListener(v-> approve());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void approve(){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_APPROVE_MATERIALS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");
                            if (status.equals("1")){

                                Toast toast= Toast.makeText(ApproveMaterials.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                finish();
                            }else{

                                Toast toast= Toast.makeText(ApproveMaterials.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast= Toast.makeText(ApproveMaterials.this, e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast= Toast.makeText(ApproveMaterials.this, error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
            }
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params=new HashMap<>();

                params.put("requestID",requestID);
                params.put("item",items);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void alertApprove(){
        android.app.AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("You are About to Approve this Tender");
        alertDialog.setCancelable(false);
        alertDialog.setButton2("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                return;
            }
        });
        alertDialog.setButton("Confirm ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                approve();
                return;
            }
        });
        alertDialog.show();
    }
}