package com.example.Varsani.Staff.Designer.Adapters;

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
import com.example.Varsani.Staff.Designer.DesignItems;
import com.example.Varsani.Staff.Models.OrderToShipModel;
import com.example.Varsani.utils.SessionHandler;

import java.util.List;

public class AdapterDesign extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<OrderToShipModel> items;

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

    public AdapterDesign(Context context, List<OrderToShipModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_bookingID,txv_clientName, txv_service,txv_Status;


        public OriginalViewHolder(View v) {
            super(v);

            txv_clientName =v.findViewById(R.id.txv_clientName);
            txv_bookingID =v.findViewById(R.id.txv_bookingID);
            txv_Status = v.findViewById(R.id.txv_Status);
            txv_service = v.findViewById(R.id.txv_service);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_orderstoship, parent, false);
        vh = new AdapterDesign.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AdapterDesign.OriginalViewHolder) {
            final AdapterDesign.OriginalViewHolder view = (AdapterDesign.OriginalViewHolder) holder;

            final OrderToShipModel o= items.get(position);

            view.txv_bookingID.setText("Booking ID "+o.getOrderID());
            view.txv_Status.setText("Status: " + o.getOrderStatus());
            view.txv_service.setText("Service: "+o.getServName());
            view.txv_clientName.setText("Client: "+o.getClientName());



            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in = new Intent(ctx, DesignItems.class);
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
