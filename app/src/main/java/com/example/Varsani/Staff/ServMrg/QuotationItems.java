package com.example.Varsani.Staff.ServMrg;

import static com.example.Varsani.utils.Urls.ROOT_URL_UPLOADS;
import static com.example.Varsani.utils.Urls.URL_ASSIGN_DESIGNER;
import static com.example.Varsani.utils.Urls.URL_GET_DESIGNER;
import static com.example.Varsani.utils.Urls.URL_SUBMIT_SERVICE_FEE;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.Varsani.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuotationItems extends AppCompatActivity {
    private TextView tv_orderID,tv_clientName,tv_county,tv_servName,tv_businessName,tv_serviceDesc,
            tv_dimension,tv_installationType,tv_expectedDate,tv_bookingStatus,tv_inputType,tv_town;
    private EditText edt_designer;
    private Button btn_viewSketch,btn_viewLogo,btn_submit;
    private String sketchImg,logoImg;
    private String sketchPdfUrl = ""; // Set these values after fetching from backend
    private String logoUrl = "";


    private String orderID,clientID;
    String orderStatus;
    private ArrayList<String> drivers;
    private ArrayList<String> driverFullNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_items);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_orderID = findViewById(R.id.tv_orderID);
        tv_clientName = findViewById(R.id.tv_clientName);
        tv_county = findViewById(R.id.tv_county);
        tv_town = findViewById(R.id.tv_town);
        tv_servName = findViewById(R.id.tv_servName);
        tv_businessName = findViewById(R.id.tv_businessName);
        tv_serviceDesc = findViewById(R.id.tv_serviceDesc);
        tv_dimension = findViewById(R.id.tv_dimension);
        tv_installationType = findViewById(R.id.tv_installationType);
        tv_expectedDate = findViewById(R.id.tv_expectedDate);
        tv_bookingStatus = findViewById(R.id.tv_bookingStatus);
        tv_inputType = findViewById(R.id.tv_inputType);
        edt_designer = findViewById(R.id.edt_designer);
        btn_viewSketch = findViewById(R.id.btn_viewSketch);
        btn_viewLogo = findViewById(R.id.btn_viewLogo);
        btn_submit = findViewById(R.id.btn_submit);

        drivers = new ArrayList<>();
        driverFullNames = new ArrayList<>();

        Intent intent=getIntent();

        orderID=intent.getStringExtra("orderID");
        clientID=intent.getStringExtra("clientID");
        String businessName=intent.getStringExtra("businessName");
        String servName=intent.getStringExtra("servName");
        String dimension=intent.getStringExtra("dimension");
        String serviceDesc=intent.getStringExtra("serviceDesc");
        String installationType=intent.getStringExtra("installationType");
        String inputText=intent.getStringExtra("inputText");
        sketchImg=intent.getStringExtra("sketchImg");
        logoImg=intent.getStringExtra("logoImg");
        String expectedDate=intent.getStringExtra("expectedDate");
        String clientName=intent.getStringExtra("clientName");
        String orderDate=intent.getStringExtra("orderDate");
        String address=intent.getStringExtra("address");
        String orderStatus=intent.getStringExtra("orderStatus");
        String county=intent.getStringExtra("county");
        String town=intent.getStringExtra("town");


        tv_orderID.setText("Booking ID: " + orderID);
        tv_clientName.setText("Client: " + clientName);
        tv_county.setText("County: " + county );
        tv_town.setText("Town: " + town );
        tv_servName.setText("Service: " + servName);
        tv_businessName.setText("Business Name: " + businessName);
        tv_serviceDesc.setText("Type: " + serviceDesc);
        tv_dimension.setText("Dimension: " + dimension);
        tv_installationType.setText("Installation Type: " + installationType);
        tv_expectedDate.setText("Expected Date: " + expectedDate);
        tv_bookingStatus.setText("Status: " + orderStatus);
        tv_inputType.setText(inputText);
        tv_dimension.setText("Dimension: " + dimension);


        btn_viewSketch.setOnClickListener(v -> openFile(sketchPdfUrl));
        btn_viewLogo.setOnClickListener(v -> openFile(logoUrl));

        edt_designer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDesigners(v);
            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlertAssign(v);
            }
        });
        fetchFileUrls();
        getDesigners();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void fetchFileUrls() {
        // Construct the full URLs
        sketchPdfUrl = ROOT_URL_UPLOADS + "/" + sketchImg;
        logoUrl = ROOT_URL_UPLOADS + "/" + logoImg;
    }
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
    public void assign() {
        final String username = edt_designer.getText().toString().trim();

        if (TextUtils.isEmpty(username)) {
            Toast toast = Toast.makeText(getApplicationContext(), "Please select Designer", Toast.LENGTH_SHORT);
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