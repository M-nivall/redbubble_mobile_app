package com.example.Varsani.Clients;

import static com.example.Varsani.utils.Urls.URL_APPROVE_DESIGN;
import static com.example.Varsani.utils.Urls.URL_DESIGN_ITEMS;
import static com.example.Varsani.utils.Urls.URL_MARK_SERVICE_COMPLETE;
import static com.example.Varsani.utils.Urls.URL_REJECT_DESIGN;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;
import com.example.Varsani.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ItemDetails extends AppCompatActivity {
    private TextView tvProduct,tvPrintArea,tvSize,tvQuantity,tvInputText;
    private EditText etFeedback;
    private Button btnConfirmDesign,btn_viewDesign,btnRejectDesign;
    private ProgressBar progressBar1;
    private LinearLayout layout_approval;
    private String files_url = Urls.ROOT_URL_BRAND_DESIGNS;
    private String url;
    private String itemId,orderID,designFile,designStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_details);

        tvProduct = findViewById(R.id.tvProduct);
        tvPrintArea = findViewById(R.id.tvPrintArea);
        tvSize = findViewById(R.id.tvSize);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvInputText = findViewById(R.id.tvInputText);
        layout_approval = findViewById(R.id.layout_approval);
        etFeedback = findViewById(R.id.etFeedback);
        btnConfirmDesign = findViewById(R.id.btnConfirmDesign);
        btnRejectDesign = findViewById(R.id.btnRejectDesign);
        progressBar1 = findViewById(R.id.progressBar1);
        btn_viewDesign = findViewById(R.id.btn_viewDesign);

        layout_approval.setVisibility(View.GONE);

        // Retrieve data from the intent
        Intent intent = getIntent();
        itemId = intent.getStringExtra("itemId");
        orderID = intent.getStringExtra("orderID");
        String itemName = intent.getStringExtra("itemName");

        tvProduct.setText("Product: " + itemName);


        getItems();


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

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void getItems() {
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
                                String productId = jsn.getString("productId");
                                String quantity = jsn.getString("quantity");
                                String printArea = jsn.getString("printArea");
                                String dimension = jsn.getString("dimension");
                                String notes = jsn.getString("notes");
                                designStatus = jsn.getString("designStatus");
                                designFile = jsn.getString("designFile");

                                tvPrintArea.setText("Print Area: " + printArea);
                                tvSize.setText("Size: " + dimension);
                                tvQuantity.setText("Quantity: " + quantity);
                                tvInputText.setText(notes);

                                if(!designFile.equalsIgnoreCase("NULL") && designStatus.equalsIgnoreCase("Pending approval")){
                                    layout_approval.setVisibility(View.VISIBLE);

                                }

                                url = files_url + designFile;
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
                params.put("itemId", itemId);

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
            protected Map<String,String>getParams()throws AuthFailureError {
                Map<String,String> params=new HashMap<>();
                params.put("itemId",itemId);
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
                params.put("itemId",itemId);
                params.put("feedback",feedback);
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
    @Override
    public void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}