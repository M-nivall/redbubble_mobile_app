package com.example.Varsani.Clients.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Clients.Models.CartModal;
import com.example.Varsani.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterServiceCart extends RecyclerView.Adapter<AdapterServiceCart.RowVH> {

    private final List<CartModal> items;
    private final Context ctx;
    private final NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());

    public AdapterServiceCart(Context context, List<CartModal> items) {
        this.items = items;
        this.ctx = context;
    }

    static class RowVH extends RecyclerView.ViewHolder {
        TextView colItemId, colItemName, colQty, colItemPrice, colTotal;
        RowVH(View v) {
            super(v);
            colItemId   = v.findViewById(R.id.col_item_id);
            colItemName = v.findViewById(R.id.col_item_name);
            colQty      = v.findViewById(R.id.col_qty);
            colItemPrice= v.findViewById(R.id.col_item_price);
            colTotal    = v.findViewById(R.id.col_total);
        }
    }

    @Override
    public RowVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cart_table, parent, false);
        return new RowVH(v);
    }

    @Override
    public void onBindViewHolder(RowVH h, int position) {
        CartModal m = items.get(position);

        // Raw strings from your model
        String itemId   = m.getItemID();      // item_id
        String name     = m.getProductName(); // item
        String qty      = m.getQuantity();    // quantity
        String price    = m.getPrice();       // item_price
        String total    = m.getSubToatl();    // total_price per row

        h.colItemId.setText(itemId);
        h.colItemName.setText(name);
        h.colQty.setText(qty);

        // Nicely format numbers if they are numeric
        h.colItemPrice.setText(formatNumber(price));
        h.colTotal.setText(formatNumber(total));
    }

    private String formatNumber(String raw) {
        try {
            // Works for integers; adjust if you store decimals
            long v = Long.parseLong(raw);
            return nf.format(v);
        } catch (Exception ignore) {
            // If not numeric, just show raw
            return raw;
        }
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
}
