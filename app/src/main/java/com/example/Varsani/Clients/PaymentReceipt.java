package com.example.Varsani.Clients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Adapters.AdapterBookingItems;
import com.example.Varsani.Clients.Models.CartModal;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
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

public class PaymentReceipt extends AppCompatActivity {
    private TextView orderID, clientName, txv_fee, txv_paymentCode,receipt_number,receipt_date;
    private ImageView btn_printfile;
    private  String fullName;
    private SessionHandler session;
    private UserModel user;
    private String orderId;
    private final List<CartModal> list = new ArrayList<>();
    private AdapterBookingItems adapterBookingItems;
    private RecyclerView recyclerView;
    private String cartTotalStr = "0";
    private TextView txv_cart_subtotal;

    private final NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
    private static final String TAG = "Product Items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_receipt);

        getSupportActionBar().setSubtitle("Payment Receipt");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize TextViews
        orderID = findViewById(R.id.receipt_orderID);
        clientName = findViewById(R.id.receipt_clientName);
        txv_fee = findViewById(R.id.txv_fee);
        txv_paymentCode = findViewById(R.id.txv_paymentCode);
        receipt_number = findViewById(R.id.receipt_number);
        receipt_date = findViewById(R.id.receipt_date);
        btn_printfile = findViewById(R.id.btn_printfile);
        txv_cart_subtotal = findViewById(R.id.txv_cart_subtotal);

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        adapterBookingItems = new AdapterBookingItems(getApplicationContext(), list);
        recyclerView.setAdapter(adapterBookingItems);

        fullName = user.getFirstname() + " " + user.getLastname();

        Intent intent=getIntent();

        orderId=intent.getStringExtra("orderID");

        // Retrieve and display values from the Intent
        orderID.setText("Booking No: " + getIntent().getStringExtra("orderID"));
        txv_fee.setText("Amount: Ksh " + getIntent().getStringExtra("totalCost"));
        receipt_number.setText("Receipt No: B25-N" +getIntent().getStringExtra("orderID"));
        receipt_date.setText("Date: " + getIntent().getStringExtra("bookingDate"));
        clientName.setText("Client Name: " + fullName);
        txv_paymentCode.setText("Payment Code: KQR4YNMLOZ");

        btn_printfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
            }
        });
        getProducts();
    }
    private void print(){
        btn_printfile.setVisibility(View.GONE);

        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),View. MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        PrintHelper photoPrinter = new PrintHelper(this); // Assume that 'this' is your activity
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("print", bitmap);

        btn_printfile.setVisibility(View.VISIBLE);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 250);
        toast.show();
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