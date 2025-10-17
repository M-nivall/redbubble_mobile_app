package com.example.Varsani.Staff.Store_mrg.Adapter;

import static com.example.Varsani.utils.Urls.URL_REQUEST_STOCK;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Store_mrg.Model.GetToolModel;
import com.example.Varsani.utils.SessionHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterGetTools extends RecyclerView.Adapter<AdapterGetTools.OriginalViewHolder> {

    private List<GetToolModel> items;
    private final List<GetToolModel> searchList;
    private final Context ctx;
    private final List<String> supplierList;

    private SessionHandler session;
    private UserModel user;
    private String clientId = "";
    private String orderID = "";

    public static final String TAG = "AdapterGetTools";

    public AdapterGetTools(Context context, List<GetToolModel> items, List<String> supplierList) {
        this.items = items;
        this.searchList = new ArrayList<>(items);
        this.ctx = context;
        this.supplierList = supplierList;
    }

    public static class OriginalViewHolder extends RecyclerView.ViewHolder {
        public TextView txv_title, txv_toolID, txv_quantity;
        public CardView cardContainer;

        public OriginalViewHolder(View v) {
            super(v);
            txv_title = v.findViewById(R.id.txv_title);
            txv_quantity = v.findViewById(R.id.txv_quantity);
            txv_toolID = v.findViewById(R.id.txv_toolID);
            cardContainer = v.findViewById(R.id.card_container);
        }
    }

    @NonNull
    @Override
    public OriginalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.lv_tools, parent, false);
        return new OriginalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OriginalViewHolder holder, int position) {
        final GetToolModel tool = items.get(position);

        holder.txv_title.setText(tool.getColor() + " " + tool.getCategory());
        holder.txv_quantity.setText("Qty: " + tool.getQuantity());
        holder.txv_toolID.setText("#ID: " + tool.getStockID());

        // Change card color based on category
        String category = tool.getCategory().toLowerCase();
        int colorResId;

        if (category.contains("t-shirt") || category.contains("shirt")) {
            colorResId = R.color.tshirt_color;
        } else if (category.contains("hoodie")) {
            colorResId = R.color.hoodie_color;
        } else if (category.contains("cap")) {
            colorResId = R.color.cap_color;
        } else {
            colorResId = R.color.default_card;
        }

        holder.cardContainer.setCardBackgroundColor(ContextCompat.getColor(ctx, colorResId));

        holder.cardContainer.setOnClickListener(v -> showRequestStockDialog(tool.getCategory(),tool.getColor()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void showRequestStockDialog(String productName, String productColor) {
        // ✅ Ensure context is valid (prevents crash)
        if (!(ctx instanceof Activity) || ((Activity) ctx).isFinishing()) {
            Log.e(TAG, "Context is not valid for showing dialog");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View dialogView = LayoutInflater.from(ctx).inflate(R.layout.dialog_request_stock, null);
        builder.setView(dialogView);

        TextView txtMaterial = dialogView.findViewById(R.id.txt_material_name);
        Spinner spnSupplier = dialogView.findViewById(R.id.spn_supplier);
        EditText edtQuantity = dialogView.findViewById(R.id.edt_quantity);
        Button btnSubmit = dialogView.findViewById(R.id.btn_submit_request);

        txtMaterial.setText("Requesting: " + productName + " - " + productColor);

        if (supplierList == null || supplierList.isEmpty()) {
            Toast.makeText(ctx, "No suppliers available. Try again later.", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_dropdown_item, supplierList);
        spnSupplier.setAdapter(adapter);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnSubmit.setOnClickListener(v -> {
            String selectedSupplier = spnSupplier.getSelectedItem().toString();
            String quantity = edtQuantity.getText().toString().trim();

            if (quantity.isEmpty()) {
                edtQuantity.setError("Enter quantity");
                return;
            }

            sendRequest(selectedSupplier, productName, productColor, quantity);
            dialog.dismiss();
        });
    }

    private void sendRequest(String selectedSupplier, String productName, String productColor, String quantity) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REQUEST_STOCK,
                response -> {
                    try {
                        Log.e("RESPONSE", response);
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("message");

                        showToast(msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast(e.toString());
                    }
                },
                error -> {
                    error.printStackTrace();
                    showToast(error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("supplier", selectedSupplier);
                params.put("productName", productName);
                params.put("productColor", productColor);
                params.put("quantity", quantity);
                Log.e("PARAMS", "" + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ctx);
        requestQueue.add(stringRequest);
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(ctx, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 250);
        toast.show();
    }

    // 🔍 Optional search filter
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString().toLowerCase();
                List<GetToolModel> filtered = new ArrayList<>();

                if (query.isEmpty()) {
                    filtered.addAll(searchList);
                } else {
                    for (GetToolModel item : searchList) {
                        if (item.getCategory().toLowerCase().contains(query)) {
                            filtered.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filtered;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                items = (ArrayList<GetToolModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
