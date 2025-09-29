package com.example.Varsani.Staff.Technician;

import static com.example.Varsani.utils.Urls.URL_PROCEED_SERVICES;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;
import com.example.Varsani.Staff.ServMrg.Models.CompletedDesignModel;
import com.example.Varsani.Staff.Technician.Adapters.AdapterAssigned;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProceedService extends AppCompatActivity {
    private List<CompletedDesignModel> list;
    private AdapterAssigned adapterAssigned;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proceed_service);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle("Service  Requests");
        recyclerView=findViewById(R.id.recyclerView);
        progressBar=findViewById(R.id.progressBar);

        list=new ArrayList<>();
        recyclerView.setLayoutManager( new LinearLayoutManager( getApplicationContext() ) );
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(mLayoutManager);


        getAssigned();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void getAssigned(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_PROCEED_SERVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Log.e("RESPONSE", response);
                            JSONObject jsonObject=new JSONObject(response);
                            String status=jsonObject.getString("status");
                            String msg=jsonObject.getString("message");
                            if(status.equals("1")){

                                JSONArray jsonArray=jsonObject.getJSONArray("details");
                                for(int i=0; i <jsonArray.length();i++){
                                    JSONObject jsn=jsonArray.getJSONObject(i);
                                    String orderID=jsn.getString("orderID");
                                    String clientID=jsn.getString("clientID");
                                    String business_name=jsn.getString("business_name");
                                    String serv_name=jsn.getString("serv_name");
                                    String dimension=jsn.getString("dimension");
                                    String service_desc=jsn.getString("service_desc");
                                    String installation_type=jsn.getString("installation_type");
                                    String input_text=jsn.getString("input_text");
                                    String sketch_img=jsn.getString("sketch_img");
                                    String logo_img=jsn.getString("logo_img");
                                    String expected_date=jsn.getString("expected_date");
                                    String clientName=jsn.getString("clientName");
                                    String orderDate=jsn.getString("orderDate");
                                    String address=jsn.getString("address");
                                    String orderStatus=jsn.getString("orderStatus");
                                    String county=jsn.getString("county");
                                    String town=jsn.getString("town");
                                    String pdf_design=jsn.getString("pdf_design");
                                    String phone_no=jsn.getString("phone_no");

                                    CompletedDesignModel completedDesignModel=new CompletedDesignModel(orderID,clientID,business_name,serv_name,dimension,service_desc,
                                            installation_type,input_text,sketch_img,logo_img,expected_date,clientName,orderDate,address,orderStatus,
                                            county,town,pdf_design,phone_no);
                                    list.add(completedDesignModel);
                                }
                                adapterAssigned=new AdapterAssigned(getApplicationContext(),list);
                                recyclerView.setAdapter(adapterAssigned);
                                progressBar.setVisibility(View.GONE);


                            }else{
                                Toast toast=Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP,0,250);
                                toast.show();
                                progressBar.setVisibility(View.GONE);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                            Toast toast=Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP,0,250);
                            toast.show();
                            Log.e("ERROR E ", e.toString());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast toast=Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP,0,250);
                toast.show();
                Log.e("ERROR E ", error.toString());
            }
        });
        RequestQueue requestQueue= Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}