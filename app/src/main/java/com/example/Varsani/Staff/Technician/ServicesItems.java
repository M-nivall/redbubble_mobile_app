package com.example.Varsani.Staff.Technician;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.Staff.Adapters.AdapterQuotItems;
import com.example.Varsani.Staff.Models.ClientItemsModal;
import com.example.Varsani.R;
import com.example.Varsani.utils.SessionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.Varsani.utils.Urls.ROOT_URL_BRAND_DESIGNS;
import static com.example.Varsani.utils.Urls.ROOT_URL_UPLOADS;
import static com.example.Varsani.utils.Urls.URL_CONFIRM_COMPLETION;
import static com.example.Varsani.utils.Urls.URL_MARK_COMPLETED;
import static com.example.Varsani.utils.Urls.URL_MARK_SERVICE_COMPLETE;
import static com.example.Varsani.utils.Urls.URL_QUOTATION_ITEMS;
import static com.example.Varsani.utils.Urls.URL_SEND_QUOTATION;

public class ServicesItems extends AppCompatActivity {
    private TextView txv_name,txv_tell,txv_county,txv_address,txv_bookingID,txv_service,txv_installation_type,
            txv_expected_date;
    private EditText edt_materials,edt_remarks;
    private ImageView ivArchitectureDesign;
    private CheckBox chk_material1,chk_material2,chk_material3,chk_material4,chk_material5,chk_material6;
    private Button btn_submit,btn_completion;
    private LinearLayout layout_materials,layout_remarks;
    private ProgressBar progressBar1,progressBar;
    private String orderID;
    private String materials;
    private SessionHandler session;
    private UserModel user;
    private String designUrl = "";
    private String pdf_design;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_items2);

        getSupportActionBar().setSubtitle("Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar1=findViewById(R.id.progressBar1);
        progressBar=findViewById(R.id.progressBar);
        txv_name=findViewById(R.id.txv_name);
        txv_tell=findViewById(R.id.txv_tell);
        txv_county=findViewById(R.id.txv_county);
        txv_name=findViewById(R.id.txv_name);
        txv_address=findViewById(R.id.txv_address);
        txv_bookingID=findViewById(R.id.txv_bookingID);
        txv_service=findViewById(R.id.txv_service);
        edt_materials=findViewById(R.id.edt_materials);
        edt_remarks=findViewById(R.id.edt_remarks);
        txv_installation_type=findViewById(R.id.txv_installation_type);
        txv_expected_date=findViewById(R.id.txv_expected_date);
        ivArchitectureDesign=findViewById(R.id.ivArchitectureDesign);
        chk_material1=findViewById(R.id.chk_material1);
        chk_material2=findViewById(R.id.chk_material2);
        chk_material3=findViewById(R.id.chk_material3);
        chk_material4=findViewById(R.id.chk_material4);
        chk_material5=findViewById(R.id.chk_material5);
        chk_material6=findViewById(R.id.chk_material6);
        layout_materials=findViewById(R.id.layout_materials);
        layout_remarks=findViewById(R.id.layout_remarks);
        btn_submit=findViewById(R.id.btn_submit_materials);
        btn_completion=findViewById(R.id.btn_completion);

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        Intent intent=getIntent();

        orderID=intent.getStringExtra("orderID");
        String clientID=intent.getStringExtra("clientID");
        String servName=intent.getStringExtra("servName");
        String installationType=intent.getStringExtra("installationType");
        String expectedDate=intent.getStringExtra("expectedDate");
        String clientName=intent.getStringExtra("clientName");
        String address=intent.getStringExtra("address");
        String orderStatus=intent.getStringExtra("orderStatus");
        String county=intent.getStringExtra("county");
        String town=intent.getStringExtra("town");
        pdf_design=intent.getStringExtra("pdf_design");
        String phone_no=intent.getStringExtra("phone_no");


        txv_bookingID.setText("Booking ID: " +orderID );
        txv_name.setText("Name " +clientName );
        txv_tell.setText("Phone No: " +phone_no );
        txv_county.setText("County: " +county );
        txv_address.setText("Address: " +address );
        txv_service.setText("Service: " +servName );
        txv_installation_type.setText("Installation Type: " +installationType );
        txv_expected_date.setText("Expected Date: " +expectedDate );

        layout_remarks.setVisibility(View.GONE);

        ivArchitectureDesign.setOnClickListener(v -> openFile(designUrl));

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAlert(v);
            }
        });

        if (orderStatus.equals("8")) {
            layout_materials.setVisibility(View.GONE);
            layout_remarks.setVisibility(View.VISIBLE);
        }
        btn_completion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getConfirm(v);
            }
        });

        StringBuilder materials=new StringBuilder();
        chk_material1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_material1.isChecked()) {
                    if (materials.length() > 0) {
                        materials.append(", ");
                    }
                    materials.append("Scaffolding");
                    edt_materials.setText(materials);
                }
            }
        });

        chk_material2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_material2.isChecked()) {
                    if (materials.length() > 0) {
                        materials.append(", ");
                    }
                    materials.append("Power Drill");
                    edt_materials.setText(materials);
                }
            }
        });
        chk_material3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_material3.isChecked()) {
                    if (materials.length() > 0) {
                        materials.append(", ");
                    }
                    materials.append("Angle Grinder");
                    edt_materials.setText(materials);
                }
            }
        });
        chk_material4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_material4.isChecked()) {
                    if (materials.length() > 0) {
                        materials.append(", ");
                    }
                    materials.append("Silicon");
                    edt_materials.setText(materials);
                }
            }
        });
        chk_material5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_material5.isChecked()) {
                    if (materials.length() > 0) {
                        materials.append(", ");
                    }
                    materials.append("Measuring Tape");
                    edt_materials.setText(materials);
                }
            }
        });
        chk_material6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chk_material6.isChecked()) {
                    if (materials.length() > 0) {
                        materials.append(", ");
                    }
                    materials.append("Rivet Gun");
                    edt_materials.setText(materials);
                }
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
        designUrl = ROOT_URL_BRAND_DESIGNS + "/" + pdf_design;
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
    public void markOrder(){
        progressBar1.setVisibility(View.VISIBLE);
        btn_submit.setVisibility(View.GONE);

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_SEND_QUOTATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE",response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");
                            if (status.equals("1")) {

                                Toast toast = Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP, 0, 250);
                                toast.show();
                                finish();
                            }
                            else{

                                Toast toast= Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                progressBar1.setVisibility(View.GONE);
                                btn_submit.setVisibility(View.VISIBLE);
                            }


                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast= Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();

                            progressBar1.setVisibility(View.GONE);
                            btn_submit.setVisibility(View.VISIBLE);
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
                btn_submit.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String,String>getParams()throws AuthFailureError{
                Map<String,String> params=new HashMap<>();
                params.put("orderID",orderID);
                params.put("materials",materials);
                params.put("empID",user.getClientID());
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    public void markComplete(){
        progressBar.setVisibility(View.VISIBLE);
        btn_completion.setVisibility(View.GONE);
        final String remarks = edt_remarks.getText().toString().trim();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_MARK_COMPLETED,
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
                                progressBar.setVisibility(View.GONE);
                                btn_completion.setVisibility(View.VISIBLE);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast= Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();

                            progressBar.setVisibility(View.GONE);
                            btn_completion.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast= Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
                progressBar.setVisibility(View.GONE);
                btn_completion.setVisibility(View.VISIBLE);
            }
        }){
            @Override
            protected Map<String,String>getParams()throws AuthFailureError{
                Map<String,String> params=new HashMap<>();
                params.put("orderID",orderID);
                params.put("remarks",remarks);
                Log.e("PARAMS",""+params);
                return params;
            }
        };
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    public void getAlert(View v){
        materials= edt_materials.getText().toString();

        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Send Quotation Now!!!");
        builder.setNegativeButton("Cancel",null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                markOrder();


                return;
            }
        });
        builder.show();

    }
    public void getConfirm(View v){
        android.app.AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Confirm Completion");
        builder.setNegativeButton("Close",null);
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                markComplete();

                return;
            }
        });
        builder.show();

    }
}
