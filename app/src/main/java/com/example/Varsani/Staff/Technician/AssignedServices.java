package com.example.Varsani.Staff.Technician;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Staff.Adapters.AdapterAssignedServices;
import com.example.Varsani.Staff.Adapters.AdapterAssignedVisits;
import com.example.Varsani.Staff.Designer.Adapters.AdapterDesign;
import com.example.Varsani.Staff.Models.AssignedModel;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Adapters.AdapterAsgnOrders;
import com.example.Varsani.Staff.ServMrg.Models.BookingModel;
import com.example.Varsani.Staff.Technician.Adapters.AdapterAssigned;
import com.example.Varsani.utils.SessionHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.Varsani.utils.Urls.URL_ASSIGNED_DESIGNS;
import static com.example.Varsani.utils.Urls.URL_ASSIGNED_SERVICES;
import static com.example.Varsani.utils.Urls.URL_GET_ASSIGNED_ORDERS;
import static com.example.Varsani.utils.Urls.URL_GET_ASSIGNED_SERVICES;
import static com.example.Varsani.utils.Urls.URL_GET_ASSIGNED_SITES;

public class AssignedServices extends AppCompatActivity {

    private List<BookingModel> list;
    private AdapterAssigned adapterAssigned;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private SessionHandler session;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assigned_services);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setSubtitle("Assigned Bookings");
        recyclerView=findViewById(R.id.recyclerView);
        progressBar=findViewById(R.id.progressBar);

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        list=new ArrayList<>();
        recyclerView.setLayoutManager( new LinearLayoutManager( getApplicationContext() ) );
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
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
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URL_ASSIGNED_SERVICES,
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
                                    String county=jsn.getString("county");
                                    String town=jsn.getString("town");
                                    String address=jsn.getString("address");
                                    String expectedDate=jsn.getString("expectedDate");
                                    String orderDate=jsn.getString("orderDate");
                                    String clientName=jsn.getString("clientName");
                                    String phoneNo=jsn.getString("phoneNo");
                                    String orderStatus=jsn.getString("orderStatus");

                                    BookingModel bookingModel=new BookingModel(orderID,clientID,county,town,address,expectedDate,
                                            orderDate,clientName,phoneNo,orderStatus);
                                    list.add(bookingModel);
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
        }){
            @Override
            protected Map<String,String> getParams()throws AuthFailureError {
                Map<String,String>params=new HashMap<>();
                params.put("techID",user.getClientID());
                Log.e("PARAMS","" +params);
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
