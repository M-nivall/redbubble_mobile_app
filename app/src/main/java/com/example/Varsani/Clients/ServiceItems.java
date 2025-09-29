// ServiceItems.java
package com.example.Varsani.Clients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Adapters.AdapterServiceCart;
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

public class ServiceItems extends AppCompatActivity {

    public static final String EXTRA_TOTAL = "EXTRA_TOTAL";
    public static final String ORDER_ID = "ORDER_ID";

    private final List<CartModal> list = new ArrayList<>();
    private AdapterServiceCart adapterServiceCart;

    private SessionHandler session;
    private UserModel user;

    private LinearLayout layout_bottom;
    private TextView txv_cart_total;
    private TextView txv_cart_subtotal;
    private TextView txv_success;

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private Button btn_pay;

    private String cartTotalStr = "0", orderID;

    private final NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
    private static final String TAG = "ServiceItems";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_items);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Requested Services");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar       = findViewById(R.id.progressBar);
        recyclerView      = findViewById(R.id.recyclerView);
        txv_cart_total    = findViewById(R.id.txv_cart_total);
        txv_cart_subtotal = findViewById(R.id.txv_cart_subtotal);
        txv_success       = findViewById(R.id.txv_success);
        layout_bottom     = findViewById(R.id.layout_bottom);
        btn_pay           = findViewById(R.id.btn_pay);

        layout_bottom.setVisibility(View.GONE);
        txv_success.setVisibility(View.GONE);
        txv_cart_total.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        adapterServiceCart = new AdapterServiceCart(getApplicationContext(), list);
        recyclerView.setAdapter(adapterServiceCart);

        session = new SessionHandler(getApplicationContext());
        user    = session.getUserDetails();

        btn_pay.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MakePayment.class);
            intent.putExtra(EXTRA_TOTAL, cartTotalStr);
            intent.putExtra(ORDER_ID, orderID);
            startActivity(intent);
        });

        getProducts();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }

    private void getProducts() {
        StringRequest req = new StringRequest(
                Request.Method.POST,
                Urls.URL_GET_CART2,
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
                                    orderID      = jsn.optString("orderID", "");
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

                            progressBar.setVisibility(View.GONE);
                            txv_success.setVisibility(View.GONE);
                            layout_bottom.setVisibility(View.VISIBLE);
                            btn_pay.setText("Proceed to Payment");

                            btn_pay.setEnabled(true);

                        } else {
                            progressBar.setVisibility(View.GONE);
                            layout_bottom.setVisibility(View.GONE);
                            txv_success.setVisibility(View.VISIBLE);
                            adapterServiceCart.notifyDataSetChanged();
                            btn_pay.setEnabled(false);
                            Toast.makeText(getApplicationContext(), "Booking list is empty", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Parse error", e);
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), "Network error: " + error, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Volley error", error);
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("clientID", user.getClientID());
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
