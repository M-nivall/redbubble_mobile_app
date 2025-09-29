package com.example.Varsani.Staff.Store_mrg.Adapter;

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
import com.example.Varsani.Staff.Finance.OrderDetails;
import com.example.Varsani.Staff.Models.ClientOrderModel;
import com.example.Varsani.Staff.Store_mrg.ApproveMaterials;
import com.example.Varsani.Staff.Store_mrg.ApproveSupply;
import com.example.Varsani.Staff.Store_mrg.Model.MaterialModel;
import com.example.Varsani.Staff.Store_mrg.Model.RequestModel;
import com.example.Varsani.utils.SessionHandler;

import java.util.List;

public class AdapterMaterials extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MaterialModel> items;

    private Context ctx;
    ProgressDialog progressDialog;
//    private OnItemClickListener mOnItemClickListener;
//    private OnMoreButtonClickListener onMoreButtonClickListener;

    //


    public static final String TAG = " adapter";

//    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
//        this.mOnItemClickListener = mItemClickListener;
//    }
//
//    public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
//        this.onMoreButtonClickListener = onMoreButtonClickListener;
//    }

    public AdapterMaterials(Context context, List<MaterialModel> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {

        public TextView txv_ID,txv_items,txv_date, tx_Status;


        public OriginalViewHolder(View v) {
            super(v);

            txv_items =v.findViewById(R.id.txv_items);
            txv_date =v.findViewById(R.id.txv_date);
            txv_ID = v.findViewById(R.id.txv_ID);
            tx_Status = v.findViewById(R.id.tx_Status);

        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_materials, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final MaterialModel o= items.get(position);

            view.txv_ID.setText("ID: "+o.getRequestID());
            view.txv_items.setText("Items: "+o.getItems());
            view.tx_Status.setText("Status: "+o.getRequestStatus());
            view.txv_date.setText("Date: "+o.getRequestDate());

            if (o.getRequestStatus().equals("Pending approval"))
            {

                view.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent in=new Intent(ctx, ApproveMaterials.class);
                        in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        in.putExtra("requestID", o.getRequestID());
                        in.putExtra("items",o.getItems());
                        in.putExtra("technician",o.getName());
                        in.putExtra("status",o.getRequestStatus());
                        in.putExtra("requestDate",o.getRequestDate());
                        in.putExtra("phoneNo",o.getPhoneNo());
                        in.putExtra("orderID",o.getOrderID());
                        ctx.startActivity(in);

                    }
                });

            }




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