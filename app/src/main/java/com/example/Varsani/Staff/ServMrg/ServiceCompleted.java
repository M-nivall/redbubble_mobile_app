package com.example.Varsani.Staff.ServMrg;

import static com.example.Varsani.utils.Urls.URL_SERVICE_COMPLETED;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;
import com.example.Varsani.Staff.ServMrg.Adapters.AdapterServiceCompleted;
import com.example.Varsani.Staff.ServMrg.Models.CompletedModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ServiceCompleted extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterServiceCompleted adapterServiceCompleted;
    private List<CompletedModel> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_completed);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        adapterServiceCompleted = new AdapterServiceCompleted(this, orderList);
        recyclerView.setAdapter(adapterServiceCompleted);

        // Call the method to fetch data
        fetchOrders();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void fetchOrders() {
        String url = URL_SERVICE_COMPLETED;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            // Check for status in the response
                            if (response.getString("status").equals("1")) {
                                JSONArray orders = response.getJSONArray("details");
                                for (int i = 0; i < orders.length(); i++) {
                                    JSONObject order = orders.getJSONObject(i);

                                    String orderID = order.getString("orderID");
                                    String servName = order.getString("servName");
                                    String clientName = order.getString("clientName");
                                    String orderDate = order.getString("orderDate");
                                    String expectedDate = order.getString("expectedDate");
                                    String address = order.getString("address");
                                    String techName = order.getString("techName");
                                    String orderRemark = order.getString("orderRemark");
                                    String orderStatus = order.getString("orderStatus");
                                    String county = order.getString("county");
                                    String town = order.getString("town");

                                    orderList.add(new CompletedModel(orderID, clientName, servName, orderDate, expectedDate, address, techName, orderRemark, orderStatus, county, town));
                                }
                                adapterServiceCompleted.notifyDataSetChanged();
                            } else {
                                Toast.makeText(ServiceCompleted.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ServiceCompleted.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(ServiceCompleted.this, "Error fetching data!", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }
}
