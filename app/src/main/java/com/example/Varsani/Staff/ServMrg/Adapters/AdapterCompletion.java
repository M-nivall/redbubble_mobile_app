package com.example.Varsani.Staff.ServMrg.Adapters;

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
import com.example.Varsani.Staff.Adapters.AdapterNewServPayments;
import com.example.Varsani.Staff.Finance.Models.PaymentModel;
import com.example.Varsani.Staff.Finance.PaymentDetails;
import com.example.Varsani.Staff.ServMrg.Models.CompletionModel;
import com.example.Varsani.Staff.ServMrg.ViewCompletedItems;
import com.example.Varsani.utils.SessionHandler;

import java.util.List;

public class AdapterCompletion extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CompletionModel> items;

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

    public AdapterCompletion(Context context, List<CompletionModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_bookingId,txv_clientName, txv_paymentDate,txv_status;


        public OriginalViewHolder(View v) {
            super(v);

            txv_clientName =v.findViewById(R.id.txv_clientName);
            txv_bookingId =v.findViewById(R.id.txv_bookingId);
            txv_paymentDate = v.findViewById(R.id.txv_paymentDate);
            txv_status = v.findViewById(R.id.txv_status);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_client_orders, parent, false);
        vh = new AdapterCompletion.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof AdapterCompletion.OriginalViewHolder) {
            final AdapterCompletion.OriginalViewHolder view = (AdapterCompletion.OriginalViewHolder) holder;

            final CompletionModel o= items.get(position);

            view.txv_bookingId.setText("Booking ID: " + o.getOrderID());
            view.txv_clientName.setText("Client Name: " + o.getClientName());
            view.txv_paymentDate.setText("Date: "+o.getPayment_date());
            view.txv_status.setText("Status: "+o.getPaymentStatus());


            view.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent in=new Intent(ctx, ViewCompletedItems.class);
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    in.putExtra("orderID", o.getOrderID());
                    in.putExtra("paymentID",o.getPaymentID());
                    in.putExtra("clientName",o.getClientName());
                    in.putExtra("phoneNo",o.getPhoneNo());
                    in.putExtra("email",o.getEmail());
                    in.putExtra("paymentCode",o.getPayment_code());
                    in.putExtra("paymentMode",o.getPayment_mode());
                    in.putExtra("paymentDate",o.getPayment_date());
                    in.putExtra("serviceFee",o.getService_fee());
                    in.putExtra("paymentStatus",o.getPaymentStatus());
                    in.putExtra("rating",o.getRating());
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
