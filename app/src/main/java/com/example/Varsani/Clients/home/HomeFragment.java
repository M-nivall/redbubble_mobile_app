package com.example.Varsani.Clients.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Adapters.AdapterProducts;
import com.example.Varsani.Clients.Adapters.AdapterServices;
import com.example.Varsani.Clients.Models.ServicesModal;
import com.example.Varsani.Clients.Models.ProductModal;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.Clients.Profile;
import com.example.Varsani.Clients.ServiceItems;
import com.example.Varsani.R;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.utils.Urls;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private SessionHandler session;
    private UserModel user;
    private List<ProductModal> list;
    private List<ServicesModal> list2;
    private AdapterProducts adapterProducts;
    private AdapterServices adapterServices;
    private ImageView img_profile;
    private ImageView cart;
    private RecyclerView recyclerView2;
    private ProgressBar progressBar;

    private Spinner spinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        progressBar = root.findViewById(R.id.progressBar);
        recyclerView2 = root.findViewById(R.id.recyclerView2);
        cart = root.findViewById(R.id.imgcart);
        img_profile = root.findViewById(R.id.img_profile);
        spinner = root.findViewById(R.id.spinner_categories);

        cart.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), ServiceItems.class);
            startActivity(intent);
        });

        img_profile.setOnClickListener(v -> {
            Intent ght = new Intent(getContext(), Profile.class);
            startActivity(ght);
        });

        recyclerView2.setLayoutManager(new GridLayoutManager(getContext(), 2));

        session = new SessionHandler(getContext());
        user = session.getUserDetails();
        list = new ArrayList<>();
        list2 = new ArrayList<>();
        getProdcuts();
        getservices();

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.service_categories,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String category = parent.getItemAtPosition(position).toString();
                filterCategory(category);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        return root;
    }

    public void getProdcuts() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_GET_PRODUCTS,
                response -> {
                    try {
                        Log.e("Response", "" + response);
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("products");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsn = jsonArray.getJSONObject(i);
                                String productID = jsn.getString("productID");
                                String productName = jsn.getString("productName");
                                String price = jsn.getString("price");
                                String stock = jsn.getString("stock");
                                String image = jsn.getString("image");
                                String desc = jsn.getString("desc");

                                ProductModal productModal = new ProductModal(productID, productName, stock, price, image, desc);
                                list.add(productModal);
                            }
                            progressBar.setVisibility(View.GONE);
                            adapterProducts = new AdapterProducts(getContext(), list);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            error.printStackTrace();
            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public void getservices() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_GET_SERVICES,
                response -> {
                    try {
                        Log.e("Response", "" + response);
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");

                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("products");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsn = jsonArray.getJSONObject(i);
                                String productID = jsn.getString("productID");
                                String productName = jsn.getString("productName");
                                String price = jsn.getString("price");
                                String stock = jsn.getString("stock");
                                String image = jsn.getString("image");
                                String desc = jsn.getString("desc");
                                String category = jsn.getString("category");

                                ServicesModal servicesModal = new ServicesModal(productID, productName, stock, price, image, desc, category);
                                list2.add(servicesModal);
                            }
                            progressBar.setVisibility(View.GONE);
                            adapterServices = new AdapterServices(getContext(), list2);
                            recyclerView2.setAdapter(adapterServices);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {
            error.printStackTrace();
            Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void filterCategory(String category) {
        if (adapterServices == null) return;

        if (category.equals("All")) {
            adapterServices.updateList(list2); // Show everything
        } else {
            List<ServicesModal> filtered = new ArrayList<>();
            for (ServicesModal item : list2) {
                if (item.getCategory().toLowerCase().contains(category.toLowerCase())) {
                    filtered.add(item);
                }
            }
            adapterServices.updateList(filtered);
        }
    }
}
