package com.example.Varsani.Clients;

import static com.example.Varsani.utils.Urls.URL_APPROVE_DESIGN;
import static com.example.Varsani.utils.Urls.URL_DESIGN_ITEMS;
import static com.example.Varsani.utils.Urls.URL_MARK_SERVICE_COMPLETE;
import static com.example.Varsani.utils.Urls.URL_REJECT_DESIGN;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Adapters.AdapterCompletedItems;
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

public class CompletedServiceItems extends AppCompatActivity {

    private TextView tvOrderID,tvServiceType,tvServiceDesc,tvInstallationType,tvDimension,tvBusinessName,
            tvBookingDate,tvOrderStatus,tvInputText;
    private EditText etFeedback,etRemarks;
    private  Button btnConfirmDesign,btnConfirmCompletion,btn_viewDesign,btnRejectDesign;
    private ProgressBar progressBar1,progressBar;
    private LinearLayout layout_approval,layout_remarks;
    private String files_url = Urls.ROOT_URL_BRAND_DESIGNS;
    private String url;
    private String orderID,name,approvalStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_service_items);

        // Bind views
        tvOrderID = findViewById(R.id.tvOrderID);
        tvServiceType = findViewById(R.id.tvServiceType);
        tvServiceDesc = findViewById(R.id.tvServiceDesc);
        tvInstallationType = findViewById(R.id.tvInstallationType);
        tvDimension = findViewById(R.id.tvDimension);
        tvInputText = findViewById(R.id.tvInputText);
        tvBusinessName = findViewById(R.id.tvBusinessName);
        tvBookingDate = findViewById(R.id.tvBookingDate);
        tvOrderStatus = findViewById(R.id.tvOrderStatus);
        layout_approval = findViewById(R.id.layout_approval);
        layout_remarks = findViewById(R.id.layout_remarks);
        etFeedback = findViewById(R.id.etFeedback);
        etRemarks = findViewById(R.id.etRemarks);
        btnConfirmDesign = findViewById(R.id.btnConfirmDesign);
        btnRejectDesign = findViewById(R.id.btnRejectDesign);
        btnConfirmCompletion = findViewById(R.id.btnConfirmCompletion);
        progressBar1 = findViewById(R.id.progressBar1);
        progressBar = findViewById(R.id.progressBar);
        btn_viewDesign = findViewById(R.id.btn_viewDesign);

        layout_approval.setVisibility(View.GONE);
        layout_remarks.setVisibility(View.GONE);

        // Retrieve data from the intent
        Intent intent = getIntent();
        orderID = intent.getStringExtra("orderID");
        String  businessName = intent.getStringExtra("businessName");
        String service = intent.getStringExtra("service");
        String dimension = intent.getStringExtra("dimension");
        String serviceDesc = intent.getStringExtra("serviceDesc");
        String installationType = intent.getStringExtra("installationType");
        String inputText = intent.getStringExtra("inputText");
        String bookingDate = intent.getStringExtra("bookingDate");
        String status = intent.getStringExtra("status");
        //String remark = intent.getStringExtra("remark");

        tvOrderID.setText("Booking ID: " + orderID);
        tvServiceType.setText("Service: " + service);
        tvServiceDesc.setText("Model: " + serviceDesc);
        tvInstallationType.setText("Installation Type: " + installationType);
        tvDimension.setText("Dimension: " + dimension);
        tvBusinessName.setText("Business Name: " + businessName);
        tvBookingDate.setText("Booking Date: " + bookingDate);
        tvOrderStatus.setText("Status: " + status);
        tvInputText.setText(inputText);

        if (status.equals("Approve Brand Design")){
            layout_approval.setVisibility(View.VISIBLE);
        }

        if (status.equals("Approve Completion")){
            layout_remarks.setVisibility(View.VISIBLE);
        }
       //// if (status.equals("Completed") && remark != null){
       //     layout_remarks.setVisibility(View.VISIBLE);
       // }

        getMyPackages();


        btn_viewDesign.setOnClickListener(v -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        });

        btnConfirmDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlert(v);
            }
        });
        btnRejectDesign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertReject(v);
            }
        });
        btnConfirmCompletion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getConfirm(v);
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

    public void getMyPackages() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DESIGN_ITEMS,
                response -> {
                    try {

                        Log.e("RESPONSE ", response);
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("responseData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsn = jsonArray.getJSONObject(i);
                                String empID = jsn.getString("empID");
                                name = jsn.getString("name");
                                approvalStatus = jsn.getString("approvalStatus");

                                if(approvalStatus.equalsIgnoreCase("Pending Approval")){
                                    layout_approval.setVisibility(View.VISIBLE);

                                }

                                url = files_url + name;
                                Log.e("URL FILE", " " + url);

                            }

                        } else if (status.equals("0")) {
                            String msg = jsonObject.getString("message");

                            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }, error -> {
            error.printStackTrace();
            Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("orderID", orderID);

                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void approve(){
        progressBar1.setVisibility(View.VISIBLE);
        btnConfirmDesign.setVisibility(View.GONE);
        final String feedback = etFeedback.getText().toString().trim();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_APPROVE_DESIGN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");
                            if (status.equals("1")){

                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                finish();
                            }else{

                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                progressBar1.setVisibility(View.GONE);
                                btnConfirmDesign.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast= Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();

                            progressBar1.setVisibility(View.GONE);
                            btnConfirmDesign.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast= Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
                progressBar1.setVisibility(View.GONE);
                btnConfirmDesign.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String,String>getParams()throws AuthFailureError{
                Map<String,String> params=new HashMap<>();
                params.put("orderID",orderID);
                params.put("feedback",feedback);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void reject(){
        progressBar1.setVisibility(View.VISIBLE);
        btnRejectDesign.setVisibility(View.GONE);
        final String feedback = etFeedback.getText().toString().trim();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_REJECT_DESIGN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");
                            if (status.equals("1")){

                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                finish();
                            }else{

                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                progressBar1.setVisibility(View.GONE);
                                btnRejectDesign.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast= Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();

                            progressBar1.setVisibility(View.GONE);
                            btnRejectDesign.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast= Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
                progressBar1.setVisibility(View.GONE);
                btnRejectDesign.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String,String>getParams()throws AuthFailureError{
                Map<String,String> params=new HashMap<>();
                params.put("orderID",orderID);
                params.put("feedback",feedback);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void markComplete(){
        progressBar.setVisibility(View.VISIBLE);
        btnConfirmCompletion.setVisibility(View.GONE);
        final String remarks = etRemarks.getText().toString().trim();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_MARK_SERVICE_COMPLETE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");
                            if (status.equals("1")){

                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                finish();
                            }else{

                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                progressBar.setVisibility(View.GONE);
                                btnConfirmCompletion.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast= Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();

                            progressBar.setVisibility(View.GONE);
                            btnConfirmCompletion.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast= Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
                progressBar.setVisibility(View.GONE);
                btnConfirmCompletion.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String,String>getParams()throws AuthFailureError{
                Map<String,String> params=new HashMap<>();
                params.put("orderID",orderID);
                params.put("remarks",remarks);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void getAlert(View v){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Approve Design");
        builder.setNegativeButton("Close",null);
        builder.setPositiveButton("Approve", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                approve();

                return;
            }
        });
        builder.show();

    }
    public void alertReject(View v){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Do you want to Reject this design ");
        builder.setNegativeButton("Close",null);
        builder.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                reject();

                return;
            }
        });
        builder.show();

    }

    public void getConfirm(View v){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirm Completion");
        builder.setNegativeButton("Close",null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                markComplete();

                return;
            }
        });
        builder.show();

    }
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
