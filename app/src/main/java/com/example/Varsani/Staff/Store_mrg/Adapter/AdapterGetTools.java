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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
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
import com.example.Varsani.R;
import com.example.Varsani.Staff.Store_mrg.Model.GetToolModel;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterGetTools extends RecyclerView.Adapter<AdapterGetTools.OriginalViewHolder> implements Filterable {

    private List<GetToolModel> items;
    private final List<GetToolModel> searchList;
    private final Context ctx;

    public static final String TAG = "AdapterGetTools";

    public AdapterGetTools(Context context, List<GetToolModel> items) {
        this.items = items;
        this.searchList = new ArrayList<>(items);
        this.ctx = context;
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

        holder.cardContainer.setOnClickListener(v ->
                showRequestStockDialog(tool.getCategory(), tool.getColor()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void showRequestStockDialog(String productName, String productColor) {
        if (!(ctx instanceof Activity) || ((Activity) ctx).isFinishing()) {
            Log.e(TAG, "Context is not valid for showing dialog");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        View dialogView = LayoutInflater.from(ctx).inflate(R.layout.dialog_request_stock, null);
        builder.setView(dialogView);

        TextView txtMaterial = dialogView.findViewById(R.id.txt_material_name);
        EditText edtQuantity = dialogView.findViewById(R.id.edt_quantity);
        Button btnSubmit = dialogView.findViewById(R.id.btn_submit_request);

        txtMaterial.setText("Requesting: " + productName + " - " + productColor);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnSubmit.setOnClickListener(v -> {
            String quantity = edtQuantity.getText().toString().trim();
            if (quantity.isEmpty()) {
                edtQuantity.setError("Enter quantity");
                return;
            }

            // Supplier removed, passing "any" as default
            sendRequest("any", productName, productColor, quantity);
            dialog.dismiss();
        });
    }

    private void sendRequest(String supplier, String productName, String productColor, String quantity) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REQUEST_STOCK,
                response -> {
                    try {
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
                //params.put("supplier", supplier); // always "any"
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

    // Implement search/filter functionality
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();
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
