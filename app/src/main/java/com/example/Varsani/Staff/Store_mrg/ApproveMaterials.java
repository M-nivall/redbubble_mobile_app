package com.example.Varsani.Staff.Store_mrg;

import static com.example.Varsani.utils.Urls.URL_APPROVE_MATERIALS;
import static com.example.Varsani.utils.Urls.URL_APPROVE_TENDER;
import static com.example.Varsani.utils.Urls.URL_GET_APPROVE_ORDERS;

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
import com.example.Varsani.Clients.Adapters.AdapterBookingItems;
import com.example.Varsani.Clients.Models.CartModal;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Finance.OrderDetails;
import com.example.Varsani.Staff.Store_mrg.Adapter.AdapterStockItems;
import com.example.Varsani.Staff.Store_mrg.Model.ItemsModal;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApproveMaterials extends AppCompatActivity {

    private TextView txv_orderID,txv_tech,txv_client,
                    txv_date,txv_status;
    private RecyclerView recyclerView;

    private Button btn_submit;
    private ProgressBar progressBar;
    private String orderID, tech;

    private SessionHandler session;
    private UserModel user;

    private final List<ItemsModal> list = new ArrayList<>();
    private AdapterStockItems adapterStockItems;
    private static final String TAG = "Product Items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_materials);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txv_client =findViewById(R.id.txv_client);
        txv_orderID = findViewById(R.id.txv_orderID);
        txv_tech = findViewById(R.id.txv_tech);
        txv_status = findViewById(R.id.txv_status);
        progressBar=findViewById(R.id.progressBar);
        btn_submit=findViewById(R.id.btn_submit);
        txv_date = findViewById(R.id.txv_date);

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        adapterStockItems = new AdapterStockItems(getApplicationContext(), list);
        recyclerView.setAdapter(adapterStockItems);

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        progressBar.setVisibility(View.GONE);

        Intent in=getIntent();
        orderID=in.getStringExtra("orderID");
        tech=in.getStringExtra("tech");


        txv_orderID.setText("#ID: "+in.getStringExtra("orderID"));
        txv_tech.setText("Technician: "+in.getStringExtra("tech"));
        txv_client.setText("Items: "+in.getStringExtra("client"));
        txv_status.setText("Status :"+in.getStringExtra("status"));
        txv_date.setText("Date :"+in.getStringExtra("requestDate"));

        btn_submit.setOnClickListener(v -> alertApprove());

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
                Urls.URL_GET_STOCK_ITEMS,
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
                                    String category    = jsn.optString("category", "0");
                                    String color    = jsn.optString("color", "0");

                                    ItemsModal m = new ItemsModal(
                                            orderID, productID, productName, quantity, price,
                                            "", itemID, subTotal, "", category, color
                                    );
                                    list.add(m);
                                }
                            }

                            adapterStockItems.notifyDataSetChanged();

                            //cartTotalStr = root.optString("cartTotal", sumClientSide(list));
                            //txv_cart_subtotal.setText("Ksh " + formatNumber(cartTotalStr));

                            //progressBar.setVisibility(View.GONE);
                            //txv_success.setVisibility(View.GONE);
                            //layout_bottom.setVisibility(View.VISIBLE);
                            //btn_pay.setText("Proceed to Payment");

                            //btn_pay.setEnabled(true);

                        } else {
                            //progressBar.setVisibility(View.GONE);
                            //layout_bottom.setVisibility(View.GONE);
                            //txv_success.setVisibility(View.VISIBLE);
                            adapterStockItems.notifyDataSetChanged();
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

    public void approve(List<ItemsModal> itemsList) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_APPROVE_MATERIALS,
                response -> {
                    try {
                        Log.e("RESPONSE", response);
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");

                        Toast toast = Toast.makeText(ApproveMaterials.this, msg, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 250);
                        toast.show();

                        if (status.equals("1")) finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ApproveMaterials.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(ApproveMaterials.this, error.toString(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                // Convert the itemsList to a JSON string
                JSONArray itemsArray = new JSONArray();
                for (ItemsModal item : itemsList) {
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("orderID", orderID);
                        obj.put("productName", item.getProductName());
                        obj.put("quantity", item.getQuantity());
                        obj.put("price", item.getPrice());
                        obj.put("color", item.getColor());
                        obj.put("category", item.getCategory());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    itemsArray.put(obj);
                }

                params.put("orderID", orderID);
                params.put("items", itemsArray.toString());

                Log.e("PARAMS", params.toString());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    public void alertApprove() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setMessage("You are about to release these Items. Proceed?");
        alertDialog.setCancelable(false);

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialog, which) -> dialog.cancel());

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", (dialog, which) -> {
            approve(list);
        });

        alertDialog.show();
    }

}