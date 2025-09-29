package com.example.Varsani.Staff.ServMrg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Staff.Models.ClientItemsModal;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Adapters.AdapterClientItems;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.Varsani.utils.Urls.ROOT_URL_UPLOADS;
import static com.example.Varsani.utils.Urls.URL_ASSIGN_TECH;
import static com.example.Varsani.utils.Urls.URL_GET_CLIENT_ITEMS;
import static com.example.Varsani.utils.Urls.URL_GET_DRIVERS;
import static com.example.Varsani.utils.Urls.URL_GET_TECHNICIANS;
import static com.example.Varsani.utils.Urls.URL_QUOTATION_ITEMS;
import static com.example.Varsani.utils.Urls.URL_SHIP_ORDER;
import static com.example.Varsani.utils.Urls.URL_SUBMIT_SERVICE_FEE;

public class QuotItems extends AppCompatActivity {
    private  TextView tv_orderID,tv_clientName,tv_county,tv_servName,tv_businessName,tv_serviceDesc,
            tv_dimension,tv_installationType,tv_expectedDate,tv_bookingStatus,tv_inputType,tv_town;
    private EditText edt_service_fee;
    private Button btn_viewSketch,btn_viewLogo,btn_submit;
    private String sketchImg,logoImg;
    private String sketchPdfUrl = ""; // Set these values after fetching from backend
    private String logoUrl = "";


    private String orderID,clientID;
    String orderStatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quot_items);

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
        edt_service_fee = findViewById(R.id.edt_service_fee);
        btn_viewSketch = findViewById(R.id.btn_viewSketch);
        btn_viewLogo = findViewById(R.id.btn_viewLogo);
        btn_submit = findViewById(R.id.btn_submit);



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

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlertShip(v);
            }
        });
        fetchFileUrls();
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


    public void shipOrder(){

        final String service_fee = edt_service_fee.getText().toString().trim();

        if(TextUtils.isEmpty(service_fee)){
            Toast toast= Toast.makeText(getApplicationContext(), "Please enter service fee to be charged", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP,0,250);
            toast.show();
            return;
        }
        try {
            int fee = Integer.parseInt(service_fee);

            if (fee > 60000) {
                Toast toast = Toast.makeText(getApplicationContext(), "Service fee cannot exceed 60,000", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 250);
                toast.show();
                return;
            }

        } catch (NumberFormatException e) {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid service fee entered", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 250);
            toast.show();
            return;
        }
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_SUBMIT_SERVICE_FEE,
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
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast= Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast= Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
            }
        }){
            @Override
            protected Map<String,String>getParams()throws AuthFailureError{
                Map<String,String> params=new HashMap<>();
                params.put("orderID",orderID);
                params.put("clientID",clientID);
                params.put("service_fee",service_fee);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    public void getAlertShip(View v){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Submit");
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                shipOrder();

                return;
            }
        });
        builder.show();

    }
}
