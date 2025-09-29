package com.example.Varsani.Staff.Technician.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.Staff.ServMrg.Models.CompletedDesignModel;
import com.example.Varsani.Staff.Technician.ServicesItems;
import com.example.Varsani.utils.SessionHandler;

import java.util.List;

public class AdapterAssigned extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<CompletedDesignModel> items;

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

    public AdapterAssigned(Context context, List<CompletedDesignModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_bookingID,txv_client, txv_service,txv_expected_date;


        public OriginalViewHolder(View v) {
            super(v);

            txv_client =v.findViewById(R.id.txv_client);
            txv_bookingID =v.findViewById(R.id.txv_bookingID);
            txv_expected_date = v.findViewById(R.id.txv_expected_date);
            txv_service = v.findViewById(R.id.txv_service);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_assigned, parent, false);
        vh = new AdapterAssigned.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AdapterAssigned.OriginalViewHolder) {
            final AdapterAssigned.OriginalViewHolder view = (AdapterAssigned.OriginalViewHolder) holder;

            final CompletedDesignModel o= items.get(position);

            view.txv_bookingID.setText("Booking ID "+o.getOrderID());
            view.txv_expected_date.setText("Expected Date: " + o.getExpectedDate());
            view.txv_service.setText("Service: "+o.getServName());
            view.txv_client.setText("Client: "+o.getClientName());

            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(ctx, ServicesItems.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    // Passing all the details from OrderToShipModel
                    in.putExtra("orderID", o.getOrderID());
                    in.putExtra("clientID", o.getClientID());
                    in.putExtra("businessName", o.getBusinessName());
                    in.putExtra("servName", o.getServName());
                    in.putExtra("dimension", o.getDimension());
                    in.putExtra("serviceDesc", o.getServiceDesc());
                    in.putExtra("installationType", o.getInstallationType());
                    in.putExtra("inputText", o.getInputText());
                    in.putExtra("sketchImg", o.getSketchImg());
                    in.putExtra("logoImg", o.getLogoImg());
                    in.putExtra("expectedDate", o.getExpectedDate());
                    in.putExtra("clientName", o.getClientName());
                    in.putExtra("orderDate", o.getOrderDate());
                    in.putExtra("address", o.getAddress());
                    in.putExtra("orderStatus", o.getOrderStatus());
                    in.putExtra("county", o.getCounty());
                    in.putExtra("town", o.getTown());
                    in.putExtra("pdf_design", o.getPdf_design());
                    in.putExtra("phone_no", o.getPhone_no());

                    ctx.startActivity(in);
                }
            });

        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
