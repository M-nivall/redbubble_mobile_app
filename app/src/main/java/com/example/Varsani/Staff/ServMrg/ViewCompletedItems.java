package com.example.Varsani.Staff.ServMrg;

import static com.example.Varsani.utils.Urls.URL_APPROVE_SERV_PAYMENTS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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

public class ViewCompletedItems extends AppCompatActivity {

    private RecyclerView recyclerView;
    private final List<CartModal> list = new ArrayList<>();
    private AdapterServiceCart adapterServiceCart;
    private SessionHandler session;
    private UserModel user;
    private ProgressBar progressBar;
    private TextView tvClientName,tvClientNo,tvEmail,
            tvPaymentID,tvPaymentDate,
            tvPaymentMode,tvPaymentCode,tvAmount,tvStatus;
    private String orderId,paymentID;
    private String cartTotalStr = "0";
    private TextView txv_cart_subtotal, tvGuestRating;
    private RatingBar ratingBar;

    private final NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
    private static final String TAG = "Product Items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_completed_items);

        getSupportActionBar().setSubtitle("Completed Bookings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar=findViewById(R.id.progressBar);
        tvPaymentID=findViewById(R.id.tvPaymentID);
        tvPaymentDate=findViewById(R.id.tvPaymentDate);
        tvPaymentMode=findViewById(R.id.tvPaymentMode);
        tvClientName=findViewById(R.id.tvClientName);
        tvPaymentCode=findViewById(R.id.tvPaymentCode);
        tvStatus=findViewById(R.id.tvStatus);
        tvAmount=findViewById(R.id.tvAmount);
        tvEmail=findViewById(R.id.tvEmail);
        //tvBookingDate=findViewById(R.id.tvBookingDate);
        tvClientNo=findViewById(R.id.tvClientNo);

        txv_cart_subtotal = findViewById(R.id.txv_cart_subtotal);

        tvGuestRating = findViewById(R.id.tvGuestRating);
        ratingBar = findViewById(R.id.ratingBar);


        recyclerView      = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        adapterServiceCart = new AdapterServiceCart(getApplicationContext(), list);
        recyclerView.setAdapter(adapterServiceCart);

        session = new SessionHandler(getApplicationContext());
        user    = session.getUserDetails();

        Intent intent=getIntent();

        orderId=intent.getStringExtra("orderID");
        paymentID=intent.getStringExtra("paymentID");
        String clientName=intent.getStringExtra("clientName");
        String phoneNo=intent.getStringExtra("phoneNo");
        String email=intent.getStringExtra("email");
        String paymentCode=intent.getStringExtra("paymentCode");
        String paymentMode=intent.getStringExtra("paymentMode");
        String paymentDate=intent.getStringExtra("paymentDate");
        String serviceFee=intent.getStringExtra("serviceFee");
        String paymentStatus=intent.getStringExtra("paymentStatus");
        String ratingValue=intent.getStringExtra("rating");

        tvPaymentID.setText("Payment ID: " + paymentID);
        tvPaymentDate.setText("Payment Date: " + paymentDate);
        tvPaymentMode.setText("Payment Mode: " + paymentMode);
        tvClientName.setText("Name: " + clientName);
        tvPaymentCode.setText("Payment Code: " + paymentCode);
        tvStatus.setText("Status: " + paymentStatus);
        tvAmount.setText("Paid Amount: " + serviceFee + " ksh");
        tvEmail.setText("Email: " + email);
        tvClientNo.setText("Phone No: " + phoneNo );

        float rating = Float.parseFloat(ratingValue);
        ratingBar.setRating(rating);
        tvGuestRating.setText("Rating: " + rating);

        getProducts();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
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

                            adapterServiceCart.notifyDataSetChanged();

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
                            adapterServiceCart.notifyDataSetChanged();
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