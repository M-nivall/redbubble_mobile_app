package com.example.Varsani.Clients.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Clients.BookingItems;
import com.example.Varsani.Clients.CompletedServiceItems;
import com.example.Varsani.Clients.Models.MyBookingModel;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.utils.SessionHandler;

import java.util.List;

public class AdapterMyBookings extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<MyBookingModel> items;

    private Context ctx;
    ProgressDialog progressDialog;
//    private OnItemClickListener mOnItemClickListener;
//    private OnMoreButtonClickListener onMoreButtonClickListener;

    //

    private SessionHandler session;
    private UserModel user;
    private String clientId = "";
    private String orderID = "";

    public static final String TAG = "Orders adapter";

//    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
//        this.mOnItemClickListener = mItemClickListener;
//    }
//
//    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
//        this.onMoreButtonClickListener = onMoreButtonClickListener;
//    }

    public AdapterMyBookings(Context context, List<MyBookingModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_orderID, txv_amount,txv_bookingDate,txv_orderStatus;


        public OriginalViewHolder(View v) {
            super(v);

            txv_orderID =v.findViewById(R.id.txv_orderID);
            txv_amount = v.findViewById(R.id.txv_amount);
            txv_orderStatus = v.findViewById(R.id.txv_orderStatus);
            txv_bookingDate = v.findViewById(R.id.txv_bookingDate);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_orders, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final MyBookingModel o = items.get(position);

            view.txv_orderID.setText("Booking ID: "+o.getOrderID());
            view.txv_orderStatus.setText("Status: " + o.getOrderStatus());
            view.txv_amount.setText("Amount ksh : "+o.getTotalCost());
            view.txv_bookingDate.setText("Booking Date: "+o.getOderDate());

            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in=new Intent(ctx, BookingItems.class);   //CompletedServiceItems
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("orderID", o.getOrderID());
                    in.putExtra("totalCost",o.getTotalCost());
                    in.putExtra("paymentCode",o.getPaymentCode());
                    in.putExtra("orderDate",o.getOderDate());
                    in.putExtra("expectedDate",o.getExpectedDate());
                    in.putExtra("orderStatus",o.getOrderStatus());
                    ctx.startActivity(in);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

//    public interface OnItemClickListener {
//        void onItemClick(View view, ProductModal obj, int pos);
//    }
//
//    public interface OnMoreButtonClickListener {
//        void onItemClick(View view, ProductModal obj, MenuItem item);
//    }
}
