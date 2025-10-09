package com.example.Varsani.Clients;

import static com.example.Varsani.utils.Urls.URL_APPROVE_SERV_PAYMENTS;

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
import com.example.Varsani.Clients.Adapters.AdapterBookingItems;
import com.example.Varsani.Clients.Adapters.AdapterServiceCart;
import com.example.Varsani.Clients.Models.CartModal;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Finance.PaymentDetails;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class BookingItems extends AppCompatActivity {


    private RecyclerView recyclerView;
    private final List<CartModal> list = new ArrayList<>();
    private AdapterBookingItems adapterBookingItems;
    private SessionHandler session;
    private UserModel user;
    private ProgressBar progressBar;
    //private TextView tvClientName,tvClientNo,tvEmail;
    private TextView tvPaymentID,tvPaymentDate,
            tvPaymentCode,tvAmount,tvStatus;
    private Button btn_approve,btn_reject;
    private String orderId,paymentID;
    private String cartTotalStr = "0";
    private TextView txv_cart_subtotal;

    private final NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
    private static final String TAG = "Product Items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_booking_items);

        //getSupportActionBar().setSubtitle("Payment Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressBar=findViewById(R.id.progressBar);
        tvPaymentID=findViewById(R.id.tvPaymentID);
        tvPaymentDate=findViewById(R.id.tvPaymentDate);
        //tvPaymentMode=findViewById(R.id.tvPaymentMode);
        //tvClientName=findViewById(R.id.tvClientName);
        tvPaymentCode=findViewById(R.id.tvPaymentCode);
        tvStatus=findViewById(R.id.tvStatus);
        tvAmount=findViewById(R.id.tvAmount);
        //tvEmail=findViewById(R.id.tvEmail);
        //tvBookingDate=findViewById(R.id.tvBookingDate);
        //tvClientNo=findViewById(R.id.tvClientNo);

        txv_cart_subtotal = findViewById(R.id.txv_cart_subtotal);



        btn_approve=findViewById(R.id.btn_submit);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        adapterBookingItems = new AdapterBookingItems(getApplicationContext(), list);
        recyclerView.setAdapter(adapterBookingItems);

        session = new SessionHandler(getApplicationContext());
        user    = session.getUserDetails();

        Intent intent=getIntent();

        orderId=intent.getStringExtra("orderID");
        String expectedDate=intent.getStringExtra("expectedDate");
        //String clientName=intent.getStringExtra("clientName");
        //String phoneNo=intent.getStringExtra("phoneNo");
        //String email=intent.getStringExtra("email");
        String paymentCode=intent.getStringExtra("paymentCode");
        String paymentMode=intent.getStringExtra("paymentMode");
        String paymentDate=intent.getStringExtra("orderDate");
        String totalCost=intent.getStringExtra("totalCost");
        String orderStatus=intent.getStringExtra("orderStatus");

        tvPaymentID.setText("Order ID: " + orderId);
        tvPaymentDate.setText("Payment Date: " + paymentDate);
        //tvPaymentMode.setText("Payment Mode: " + paymentMode);
        //tvClientName.setText("Name: " + clientName);
        tvPaymentCode.setText("Payment Code: " + paymentCode);
        tvStatus.setText("Status: " + orderStatus);
        tvAmount.setText("Total Amount: " + totalCost + " ksh");
        //tvEmail.setText("Email: " + email);
        //tvBookingDate.setText("Booking Date: " + paymentDate );
        //tvClientNo.setText("Phone No: " + phoneNo );
        //tvBookingID.setText("Booking ID: " + orderID );

        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertApprove();
            }
        });
//         btn_reject.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        getProducts();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void approveOrder(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_APPROVE_SERV_PAYMENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");
                            if (status.equals("1")){

                                Toast toast= Toast.makeText(BookingItems.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                finish();
                            }else{

                                Toast toast= Toast.makeText(BookingItems.this, msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast= Toast.makeText(BookingItems.this, e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast= Toast.makeText(BookingItems.this, error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
            }
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("orderID",orderId);
                params.put("paymentID",paymentID);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void alertApprove(){
        android.app.AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("Approve Payment");
        alertDialog.setCancelable(false);
        alertDialog.setButton2("Close", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

                return;
            }
        });
        alertDialog.setButton("Approve ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                approveOrder();
                return;
            }
        });
        alertDialog.show();
    }
    private void getProducts() {
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

                            adapterBookingItems.notifyDataSetChanged();

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
                            adapterBookingItems.notifyDataSetChanged();
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
                p.put("orderID", orderId);
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
}