package com.example.Varsani.Staff.ServMrg;

import static com.example.Varsani.utils.Urls.ROOT_URL_UPLOADS;
import static com.example.Varsani.utils.Urls.URL_ASSIGN_DESIGNER;
import static com.example.Varsani.utils.Urls.URL_GET_DESIGNER;
import static com.example.Varsani.utils.Urls.URL_GET_TECH;
import static com.example.Varsani.utils.Urls.URL_SUBMIT_SERVICE_FEE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class QuotationItems extends AppCompatActivity {

    private AdapterItems adapterItems;
    private final List<CartModal> list = new ArrayList<>();
    private TextView tv_orderID,tv_clientName,tv_county,tv_tell,tv_expDate,tv_bookingStatus,tv_town;
    private EditText edt_designer,edt_technician;
    private Button btn_viewLogo,btn_submit;
    private RecyclerView recyclerView;
    private static final String TAG = "Product Items";
    private final NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
    private String cartTotalStr = "0";
    private TextView txv_cart_subtotal;


    private String orderID;
    String orderStatus;
    private ArrayList<String> drivers;
    private ArrayList<String> driverFullNames;

    private ArrayList<String> technicians;
    private ArrayList<String> techFullNames;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_items);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerView);
        tv_orderID = findViewById(R.id.tv_orderID);
        tv_clientName = findViewById(R.id.tv_clientName);
        tv_county = findViewById(R.id.tv_county);
        tv_town = findViewById(R.id.tv_town);
        tv_tell = findViewById(R.id.tv_tell);
        tv_expDate = findViewById(R.id.tv_expDate);
        tv_bookingStatus = findViewById(R.id.tv_bookingStatus);
        edt_designer = findViewById(R.id.edt_designer);
        edt_technician = findViewById(R.id.edt_technician);
        btn_viewLogo = findViewById(R.id.btn_viewLogo);
        btn_submit = findViewById(R.id.btn_submit);

        txv_cart_subtotal = findViewById(R.id.txv_cart_subtotal);

        drivers = new ArrayList<>();
        driverFullNames = new ArrayList<>();

        technicians = new ArrayList<>();
        techFullNames = new ArrayList<>();

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

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        adapterItems = new AdapterItems(getApplicationContext(), list);
        recyclerView.setAdapter(adapterItems);



        edt_designer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDesigners(v);
            }
        });

        edt_technician.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertTech(v);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlertAssign(v);
            }
        });
        getItems();
        getDesigners();
        getTechnicians();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
   // private void fetchFileUrls() {
        // Construct the full URLs
        //sketchPdfUrl = ROOT_URL_UPLOADS + "/" + sketchImg;
    //    logoUrl = ROOT_URL_UPLOADS + "/" + logoImg;
   // }
    private void openFile(String url) {
        if (url == null || url.isEmpty()) {
            // Handle case where URL is not available
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
    public void getDesigners() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_DESIGNER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("RESPONSE", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String username = jsn.getString("username");
                                    String fullName = jsn.getString("fullName"); // Assume this field is in the JSON response
                                    drivers.add(username);
                                    driverFullNames.add(fullName); // Add the full name to the list
                                }
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 250);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 250);
                toast.show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void getTechnicians() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GET_TECH,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("RESPONSE", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");

                            if (status.equals("1")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("details");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsn = jsonArray.getJSONObject(i);
                                    String username = jsn.getString("username");
                                    String fullName = jsn.getString("fullName"); // Assume this field is in the JSON response
                                    technicians.add(username);
                                    techFullNames.add(fullName); // Add the full name to the list
                                }
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 250);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 250);
                toast.show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
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
    public void assign() {
        final String username = edt_designer.getText().toString().trim();
        final String tech = edt_technician.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select Designer", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 250);
            toast.show();
            return;
        }
        if (TextUtils.isEmpty(tech)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select Technician", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 250);
            toast.show();
            return;
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ASSIGN_DESIGNER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.e("RESPONSE", response);
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String msg = jsonObject.getString("message");
                            if (status.equals("1")) {
                                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();
                                finish();
                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast toast = Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 250);
                            toast.show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 250);
                toast.show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("orderID", orderID);
                params.put("username", username);
                params.put("tech", tech);
                Log.e("Params", "" + params);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void alertDesigners(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Designer");

        // Create a string array of full names for the dialog
        String[] fullNamesArray = driverFullNames.toArray(new String[0]);

        builder.setItems(fullNamesArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When an instructor is selected, set the username in the EditText
                edt_designer.setText(drivers.get(which)); // Get the corresponding username
            }
        });

        builder.show();
    }

    public void alertTech(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Technician");

        // Create a string array of full names for the dialog
        String[] fullNamesArray = techFullNames.toArray(new String[0]);

        builder.setItems(fullNamesArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // When an instructor is selected, set the username in the EditText
                edt_technician.setText(technicians.get(which)); // Get the corresponding username
            }
        });

        builder.show();
    }
    public void getAlertAssign(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Approve");

        builder.setMessage("Are you sure you want to approve?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //progressBar1.setVisibility(View.VISIBLE);
                assign();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }

}