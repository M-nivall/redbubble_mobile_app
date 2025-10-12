package com.example.Varsani.Staff.Technician;

import static com.example.Varsani.utils.Urls.URL_APPROVE_DESIGN;
import static com.example.Varsani.utils.Urls.URL_COMPLETE_WORK;
import static com.example.Varsani.utils.Urls.URL_REJECT_DESIGN;
import static com.example.Varsani.utils.Urls.URL_START_WORK;

import android.annotation.SuppressLint;
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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Models.CartModal;
import com.example.Varsani.R;
import com.example.Varsani.Staff.ServMrg.Adapters.AdapterItems;
import com.example.Varsani.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ViewItems extends AppCompatActivity {
    private AdapterItems adapterItems;
    private final List<CartModal> list = new ArrayList<>();
    private TextView tv_orderID,tv_clientName,tv_county,tv_tell,tv_expDate,tv_bookingStatus,tv_town;
    private RecyclerView recyclerView;
    private static final String TAG = "Product Items";
    private final NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
    private String cartTotalStr = "0";
    private TextView txv_cart_subtotal;
    private Button btnStartWork,btnCompleteWork;
    private ProgressBar progressBar1;


    private String orderID;

    @SuppressLint("SetTextI18n")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_items);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        tv_orderID = findViewById(R.id.tv_orderID);
        tv_clientName = findViewById(R.id.tv_clientName);
        tv_county = findViewById(R.id.tv_county);
        tv_town = findViewById(R.id.tv_town);
        tv_tell = findViewById(R.id.tv_tell);
        tv_expDate = findViewById(R.id.tv_expDate);
        tv_bookingStatus = findViewById(R.id.tv_bookingStatus);
        btnCompleteWork = findViewById(R.id.btnCompleteWork);
        btnStartWork = findViewById(R.id.btnStartWork);
        progressBar1 = findViewById(R.id.progressBar1);

        txv_cart_subtotal = findViewById(R.id.txv_cart_subtotal);


        Intent intent=getIntent();

        orderID=intent.getStringExtra("orderID");
        String clientID=intent.getStringExtra("clientID");
        String expectedDate=intent.getStringExtra("expDate");
        String orderDate=intent.getStringExtra("orderDate");
        String clientName=intent.getStringExtra("clientName");
        String tell=intent.getStringExtra("tell");
        String address=intent.getStringExtra("address");
        String orderStatus=intent.getStringExtra("orderStatus");
        String county=intent.getStringExtra("county");
        String town=intent.getStringExtra("town");


        tv_orderID.setText("Booking ID: " + orderID);
        tv_clientName.setText("Client: " + clientName);
        tv_county.setText("County: " + county );
        tv_town.setText("Town: " + town );
        tv_tell.setText("Phone No: " + tell);
        tv_expDate.setText("Expected Date: " + expectedDate);
        tv_bookingStatus.setText("Status: " + orderStatus);

        assert orderStatus != null;
        if (orderStatus.equalsIgnoreCase("In progress")){
            btnStartWork.setVisibility(View.GONE);
            btnCompleteWork.setVisibility(View.VISIBLE);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        adapterItems = new AdapterItems(getApplicationContext(), list);
        recyclerView.setAdapter(adapterItems);

        btnStartWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlert(v);
            }
        });
        btnCompleteWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertComplete(v);
            }
        });


        getItems();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void getItems() {
        StringRequest req = new StringRequest(
                Request.Method.POST,
                Urls.URL_GET_ITEMS,
                response -> {
                    try {
                        Log.d(TAG, "Response: " + response);
                        JSONObject root = new JSONObject(response);
                        String status = root.optString("status", "0");

                        list.clear();

                        if ("1".equals(status)) {
                            JSONArray arr = root.optJSONArray("items");
                            if (arr != null) {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject jsn = arr.getJSONObject(i);

                                    String itemID      = jsn.optString("itemID", "");
                                    String orderID      = jsn.optString("orderID", "");
                                    String productID   = jsn.optString("productID", "");
                                    String productName = jsn.optString("productName", "");
                                    String price       = jsn.optString("itemPrice", "0");
                                    String quantity    = jsn.optString("quantity", "0");
                                    String subTotal    = jsn.optString("subTotal", "0");

                                    CartModal m = new CartModal(
                                            orderID, productID, productName, quantity, price,
                                            "", itemID, subTotal, ""
                                    );
                                    list.add(m);
                                }
                            }

                            adapterItems.notifyDataSetChanged();

                            cartTotalStr = root.optString("cartTotal", sumClientSide(list));
                            txv_cart_subtotal.setText("Ksh " + formatNumber(cartTotalStr));

                            //progressBar.setVisibility(View.GONE);
                            //txv_success.setVisibility(View.GONE);
                            //layout_bottom.setVisibility(View.VISIBLE);
                            //btn_pay.setText("Proceed to Payment");

                            //btn_pay.setEnabled(true);

                        } else {
                            //progressBar.setVisibility(View.GONE);
                            //layout_bottom.setVisibility(View.GONE);
                            //txv_success.setVisibility(View.VISIBLE);
                            adapterItems.notifyDataSetChanged();
                            //btn_pay.setEnabled(false);
                            //Toast.makeText(getApplicationContext(), "Booking list is empty", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Parse error", e);
                    }
                },
                error -> {
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Network error: " + error, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Volley error", error);
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                //p.put("clientID", user.getClientID());
                p.put("orderID", orderID);
                return p;
            }
        };

        RequestQueue q = Volley.newRequestQueue(getApplicationContext());
        q.add(req);
    }

    private String sumClientSide(List<CartModal> items) {
        long total = 0L;
        for (CartModal m : items) {
            try {
                total += Long.parseLong(m.getSubToatl());
            } catch (Exception ignore) {}
        }
        return String.valueOf(total);
    }

    private String formatNumber(String raw) {
        try {
            long v = Long.parseLong(raw);
            return nf.format(v);
        } catch (Exception e) {
            return raw;
        }
    }
    public void getAlert(View v){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirm Start of the work");
        builder.setNegativeButton("Close",null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                startWork();

                return;
            }
        });
        builder.show();

    }

    public void startWork(){
        progressBar1.setVisibility(View.VISIBLE);
        btnStartWork.setVisibility(View.GONE);
        //final String feedback = etFeedback.getText().toString().trim();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_START_WORK,
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
                                btnStartWork.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast= Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();

                            progressBar1.setVisibility(View.GONE);
                            btnStartWork.setVisibility(View.VISIBLE);
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
                btnCompleteWork.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String,String>getParams()throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("orderID",orderID);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void alertComplete(View v){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirm Completion of the Work ");
        builder.setNegativeButton("Close",null);
        builder.setPositiveButton("Complete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                complete();

                return;
            }
        });
        builder.show();
    }

    public void complete(){
        progressBar1.setVisibility(View.VISIBLE);
        btnCompleteWork.setVisibility(View.GONE);
        //final String feedback = etFeedback.getText().toString().trim();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_COMPLETE_WORK,
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
                                btnCompleteWork.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast= Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();

                            progressBar1.setVisibility(View.GONE);
                            btnCompleteWork.setVisibility(View.VISIBLE);
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
                btnCompleteWork.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String,String>getParams()throws AuthFailureError{
                Map<String,String> params=new HashMap<>();
                params.put("orderID",orderID);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}