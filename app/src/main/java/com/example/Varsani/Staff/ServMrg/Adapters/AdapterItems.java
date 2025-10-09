package com.example.Varsani.Staff.ServMrg.Adapters;

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
import com.example.Varsani.Staff.ServMrg.ItemInformation;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdapterItems extends RecyclerView.Adapter<AdapterItems.RowVH>{
    private final List<CartModal> items;
    private final Context ctx;
    private final NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());

    //public static final String EXTRA_ITEM_ID = "ITEM_ID";

    public AdapterItems(Context context, List<CartModal> items) {
        this.items = items;
        this.ctx = context;
    }

    static class RowVH extends RecyclerView.ViewHolder {
        TextView colItemId, colItemName, colQty, colItemPrice, colTotal, colViewDetails;
        RowVH(View v) {
            super(v);
            colItemId      = v.findViewById(R.id.col_item_id);
            colItemName    = v.findViewById(R.id.col_item_name);
            colQty         = v.findViewById(R.id.col_qty);
            colItemPrice   = v.findViewById(R.id.col_item_price);
            colTotal       = v.findViewById(R.id.col_total);
            colViewDetails = v.findViewById(R.id.col_view_details);
        }
    }

    @Override
    public AdapterItems.RowVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cart_item_table, parent, false);
        return new AdapterItems.RowVH(v);
    }

    @Override
    public void onBindViewHolder(AdapterItems.RowVH h, int position) {
        CartModal m = items.get(position);

        // Raw strings from your model
        String itemId = m.getItemID();        // item_id
        String name   = m.getProductName();   // item
        String qty    = m.getQuantity();      // quantity
        String price  = m.getPrice();         // item_price
        String total  = m.getSubToatl();
        String image_url  = m.getImgUrl();
        String orderID  = m.getOrderID();

        h.colItemId.setText(itemId);
        h.colItemName.setText(name);
        h.colQty.setText(qty);

        // Nicely format numbers if they are numeric
        h.colItemPrice.setText(formatNumber(price));
        h.colTotal.setText(formatNumber(total));

        // Style the "View Details" like a link
        h.colViewDetails.setPaintFlags(h.colViewDetails.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        h.colViewDetails.setOnClickListener(v -> {
            if (itemId == null || itemId.trim().isEmpty()) return;

            Intent i = new Intent(v.getContext(), ItemInformation.class);
            i.putExtra("itemId", itemId);
            i.putExtra("itemName", name);
            i.putExtra("image_url", image_url);
            i.putExtra("orderID", orderID);

            // If adapter was built with application context, ensure we start a new task
            Context c = v.getContext();
            if (!(c instanceof Activity)) {
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            c.startActivity(i);
        });
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
