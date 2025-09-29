package com.example.Varsani.Staff.Finance;

import static com.example.Varsani.utils.Urls.URL_MY_REQUESTS;
import static com.example.Varsani.utils.Urls.URL_REQUESTS;
import static com.example.Varsani.utils.Urls.URL_SUPPLY_PAYMENTS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Adapters.AdapterSupplyPayments;
import com.example.Varsani.Staff.Models.SupplyPaymentsModel;
import com.example.Varsani.Staff.Store_mrg.Adapter.AdapterRequest;
import com.example.Varsani.Staff.Store_mrg.Model.RequestModel;
import com.example.Varsani.Staff.Store_mrg.RequestSupplier;
import com.example.Varsani.Suppliers.Adapter.AdapterMyRequest;
import com.example.Varsani.Suppliers.Model.MyRequetsModel;
import com.example.Varsani.utils.SessionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupplyPayments extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<SupplyPaymentsModel> list;
    private AdapterSupplyPayments adapter;

    private SessionHandler session;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supply_payments);


        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        list = new ArrayList<>();

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(layoutManager);

        requests();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    public void requests(){
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_SUPPLY_PAYMENTS,
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
                                    String id=jsn.getString("id");
                                    String supplierID =jsn.getString("supplierID");
                                    String supplierName= jsn.getString("supplierName");
                                    String amount=jsn.getString("amount");
                                    String payment_description=jsn.getString("payment_description");
                                    String payment_status=jsn.getString("payment_status");
                                    String payment_date = jsn.getString("payment_date");
                                    String requestID = jsn.getString("requestID");
                                    SupplyPaymentsModel rq=new SupplyPaymentsModel(id,supplierID,supplierName,amount,payment_description,payment_status,payment_date,requestID);
                                    list.add(rq);
                                }
                                adapter=new AdapterSupplyPayments(getApplicationContext(),list);
                                recyclerView.setAdapter(adapter);
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
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("userID",user.getClientID());
                return params;
            }
        };
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