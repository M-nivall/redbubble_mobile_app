package com.example.Varsani.Staff.ServMrg;

import static com.example.Varsani.utils.Urls.URL_NEW_SERV_PAYMENTS;
import static com.example.Varsani.utils.Urls.URL_SERVICE_COMPLETED;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Adapters.AdapterNewServPayments;
import com.example.Varsani.Staff.Finance.Models.PaymentModel;
import com.example.Varsani.Staff.ServMrg.Adapters.AdapterCompletion;
import com.example.Varsani.Staff.ServMrg.Adapters.AdapterServiceCompleted;
import com.example.Varsani.Staff.ServMrg.Models.CompletedModel;
import com.example.Varsani.Staff.ServMrg.Models.CompletionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceCompleted extends AppCompatActivity {

    private List<CompletionModel>list;
    private AdapterCompletion adapterCompletion;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_completed);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Completed Services");
        recyclerView=findViewById(R.id.recyclerView);
        progressBar=findViewById(R.id.progressBar);

        list=new ArrayList<>();
        recyclerView.setLayoutManager( new LinearLayoutManager( getApplicationContext() ) );
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(mLayoutManager);


        newOrders();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void newOrders(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_SERVICE_COMPLETED,
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
                                    String paymentID=jsn.getString("paymentID");
                                    String clientName=jsn.getString("clientName");
                                    String payment_code=jsn.getString("paymentCode");
                                    String payment_mode=jsn.getString("paymentMethod");
                                    String payment_date=jsn.getString("orderDate");
                                    String service_fee=jsn.getString("totalAmount");
                                    String paymentStatus=jsn.getString("paymentStatus");
                                    String phoneNo=jsn.getString("phoneNo");
                                    String email=jsn.getString("email");
                                    String rating=jsn.getString("rating");

                                    CompletionModel completionModel=new CompletionModel(orderID,paymentID,clientName,
                                            payment_code,payment_mode,payment_date,service_fee,paymentStatus,phoneNo,email,rating);
                                    list.add(completionModel);
                                }
                                adapterCompletion=new AdapterCompletion(getApplicationContext(),list);
                                recyclerView.setAdapter(adapterCompletion);
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
