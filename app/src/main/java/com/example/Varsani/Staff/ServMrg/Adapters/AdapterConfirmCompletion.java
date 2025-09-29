package com.example.Varsani.Staff.ServMrg.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Varsani.R;
import com.example.Varsani.Staff.ServMrg.ApproveCompletion;
import com.example.Varsani.Staff.ServMrg.Models.CompletedModel;
import com.example.Varsani.Staff.ServMrg.ViewCompletedItems;

import java.util.List;

public class AdapterConfirmCompletion extends RecyclerView.Adapter<AdapterConfirmCompletion.ViewHolder>{
    private Context context;
    private List<CompletedModel> orderList;

    public AdapterConfirmCompletion(Context context, List<CompletedModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public AdapterConfirmCompletion.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_completed_item, parent, false);
        return new AdapterConfirmCompletion.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterConfirmCompletion.ViewHolder holder, int position) {
        CompletedModel order = orderList.get(position);

        holder.textOrderID.setText("Booking ID: " + order.getOrderID());
        holder.textService.setText("Service: " + order.getServName());
        holder.textClientName.setText("Client Name: " + order.getClientName());
        holder.textDate.setText("Date: " + order.getOrderDate());
        holder.textStatus.setText("Status: " + order.getOrderStatus());

        // Set OnClickListener to pass data when the item is clicked
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to navigate to CompletedItems activity
                Intent intent = new Intent(context, ApproveCompletion.class);

                // Pass the order details
                intent.putExtra("orderID", order.getOrderID());
                intent.putExtra("servName", order.getServName());
                intent.putExtra("clientName", order.getClientName());
                intent.putExtra("orderDate", order.getOrderDate());
                intent.putExtra("expectedDate", order.getExpectedDate());
                intent.putExtra("address", order.getAddress());
                intent.putExtra("techName", order.getTechName());
                intent.putExtra("orderRemark", order.getOrderRemark());
                intent.putExtra("orderStatus", order.getOrderStatus());
                intent.putExtra("county", order.getCounty());
                intent.putExtra("town", order.getTown());

                // Start the CompletedItems activity
                context.startActivity(intent);
            }

        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textOrderID, textService, textClientName, textDate,textStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textOrderID = itemView.findViewById(R.id.textOrderID);
            textService = itemView.findViewById(R.id.textService);
            textClientName = itemView.findViewById(R.id.textClientName);
            textDate = itemView.findViewById(R.id.textDate);
            textStatus = itemView.findViewById(R.id.textStatus);
        }
    }
}
