package com.example.Varsani.Staff.Store_mrg;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Store_mrg.Adapter.AdapterGetTools;
import com.example.Varsani.Staff.Store_mrg.Model.GetToolModel;
import com.example.Varsani.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewTools extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private List<GetToolModel> list;
    private AdapterGetTools adapterGetTools;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_tools);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle("Stock");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);

        list = new ArrayList<>();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapterGetTools = new AdapterGetTools(ViewTools.this, list); // updated constructor
        recyclerView.setAdapter(adapterGetTools);

        getStock(); // fetch stock only
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_bar, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    private void search(SearchView searchView) {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapterGetTools.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void getStock() {
        progressBar.setVisibility(android.view.View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_GET_TOOLS,
                response -> {
                    try {
                        Log.e("RESPONSE", response);
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");

                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("details");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsn = jsonArray.getJSONObject(i);
                                String stockID = jsn.getString("stockID");
                                String category = jsn.getString("category");
                                String quantity = jsn.getString("quantity");
                                String color = jsn.getString("color");
                                String description = jsn.getString("description");

                                list.add(new GetToolModel(stockID, category, quantity, color, description));
                            }

                            adapterGetTools.notifyDataSetChanged();
                        } else {
                            showToast(msg);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast(e.toString());
                    } finally {
                        progressBar.setVisibility(android.view.View.GONE);
                    }
                },
                error -> {
                    error.printStackTrace();
                    progressBar.setVisibility(android.view.View.GONE);
                    showToast(error.toString());
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 250);
        toast.show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }
}
