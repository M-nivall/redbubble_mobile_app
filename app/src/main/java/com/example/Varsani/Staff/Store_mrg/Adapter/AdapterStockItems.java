package com.example.Varsani.Staff.Store_mrg.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Clients.Adapters.AdapterBookingItems;
import com.example.Varsani.Clients.ItemDetails;
import com.example.Varsani.Clients.Models.CartModal;
import com.example.Varsani.R;
import com.example.Varsani.Staff.Store_mrg.Model.ItemsModal;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterStockItems extends RecyclerView.Adapter<AdapterStockItems.RowVH>{
    private final List<ItemsModal> items;
    private final Context ctx;
    private final NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());

    //public static final String EXTRA_ITEM_ID = "ITEM_ID";

    public AdapterStockItems(Context context, List<ItemsModal> items) {
        this.items = items;
        this.ctx = context;
    }

    static class RowVH extends RecyclerView.ViewHolder {
        TextView col_item, col_item_color, col_desc, col_item_qty;
        RowVH(View v) {
            super(v);
            col_item      = v.findViewById(R.id.col_item);
            col_item_color    = v.findViewById(R.id.col_item_color);
            col_desc         = v.findViewById(R.id.col_desc);
            col_item_qty   = v.findViewById(R.id.col_item_qty);
        }
    }

    @Override
    public AdapterStockItems.RowVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_stock_item_table, parent, false);
        return new AdapterStockItems.RowVH(v);
    }

    @Override
    public void onBindViewHolder(AdapterStockItems.RowVH h, int position) {
        ItemsModal m = items.get(position);

        // Raw strings from your model
        String category = m.getCategory();
        String name   = m.getProductName();
        String qty    = m.getQuantity();
        String price  = m.getPrice();
        String color  = m.getColor();
        String orderID  = m.getOrderID();

        h.col_item.setText(category);
        h.col_item_color.setText(color);
        h.col_desc.setText(name);
        h.col_item_qty.setText(qty);

    }

    private String formatNumber(String raw) {
        try {
            long v = Long.parseLong(raw);
            return nf.format(v);
        } catch (Exception ignore) {
            return raw; // If not numeric, just show raw
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}
