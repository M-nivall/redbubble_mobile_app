package com.example.Varsani.Staff.Store_mrg.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Store_mrg.Model.GetToolModel;
import com.example.Varsani.utils.SessionHandler;

import java.util.ArrayList;
import java.util.List;

public class AdapterGetTools extends RecyclerView.Adapter<AdapterGetTools.OriginalViewHolder> {

    private List<GetToolModel> items;
    private final List<GetToolModel> searchList;
    private final Context ctx;
    ProgressDialog progressDialog;

    private SessionHandler session;
    private UserModel user;
    private String clientId = "";
    private String orderID = "";

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
        holder.txv_quantity.setText("Quantity: " + tool.getQuantity());
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

        holder.cardContainer.setCardBackgroundColor(ctx.getResources().getColor(colorResId));
    }

    @Override
    public int getItemCount() {
        return items.size();
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
