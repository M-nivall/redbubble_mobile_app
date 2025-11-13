package com.example.Varsani.Suppliers;

import static com.example.Varsani.utils.Urls.URL_ACCEPT;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.utils.SessionHandler;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Accept extends AppCompatActivity {

    private TextView txv_requestID, txv_items, txt_qty, txv_requestDate, txv_requestStatus;
    private Button btn_submit_price;
    private ProgressBar progressBar;
    private ImageView btn_printfile;
    private EditText edt_price;
    private SessionHandler session;
    private UserModel user;

    private String requestID, color, product, totalAmount;
    private int unitprice, quantity, enteredPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept);

        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txv_requestID = findViewById(R.id.txv_requestID);
        txv_items = findViewById(R.id.txv_items);
        txt_qty = findViewById(R.id.request_qty);
        txv_requestDate = findViewById(R.id.txv_requestDate);
        txv_requestStatus = findViewById(R.id.txv_requestStatus);
        btn_submit_price = findViewById(R.id.btn_submit_price);
        progressBar = findViewById(R.id.progressBar);
        btn_printfile = findViewById(R.id.btn_printfile);
        edt_price = findViewById(R.id.edt_item_price);

        progressBar.setVisibility(View.GONE);

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        Intent in = getIntent();
        requestID = in.getStringExtra("requestID");
        color = in.getStringExtra("color");
        quantity = Integer.parseInt(in.getStringExtra("quantity"));
        product = in.getStringExtra("item");

        txv_requestID.setText("Request ID: " + requestID);
        txv_items.setText("Items: " + product + " - " + color);
        txt_qty.setText("Quantity: " + quantity);
        txv_requestDate.setText("Date: " + in.getStringExtra("requestDate"));
        txv_requestStatus.setText("Status: " + in.getStringExtra("requestStatus"));


        switch (product) {
            case "T-shirts": unitprice = 400; break;
            case "Hoodies": unitprice = 800; break;
            case "Caps": unitprice = 200; break;
            default: unitprice = 0; break;
        }

        btn_submit_price.setOnClickListener(v -> handleSubmit());
        btn_printfile.setOnClickListener(v -> printInvoice());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) finish();
        return super.onOptionsItemSelected(item);
    }

    private void handleSubmit() {
        String enteredPriceStr = edt_price.getText().toString().trim();

        if (enteredPriceStr.isEmpty()) {
            edt_price.setError("Enter price");
            return;
        }

        try {
            enteredPrice = Integer.parseInt(enteredPriceStr);
        } catch (NumberFormatException e) {
            edt_price.setError("Invalid price");
            return;
        }

        if (enteredPrice > unitprice) {
            Toast toast = Toast.makeText(this, "Entered price is more than selling price!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.TOP, 0, 250);
            toast.show();
            return;
        }

        // Calculate total
        totalAmount = String.valueOf(enteredPrice * quantity);

        approve();
    }

    private void approve() {
        progressBar.setVisibility(View.VISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ACCEPT,
                response -> {
                    progressBar.setVisibility(View.GONE);
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        String msg = jsonObject.getString("message");

                        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 250);
                        toast.show();

                        if (status.equals("1")) finish();

                    } catch (Exception e) {
                        e.printStackTrace();
                        showToast(e.toString());
                    }
                },
                error -> {
                    progressBar.setVisibility(View.GONE);
                    error.printStackTrace();
                    showToast(error.toString());
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID",user.getClientID());
                params.put("requestID", requestID);
                params.put("unitPrice", String.valueOf(enteredPrice)); // Single item price
                params.put("totalAmount", totalAmount); // quantity * enteredPrice
                Log.e("PARAMS", "" + params);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showToast(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 250);
        toast.show();
    }

    private void printInvoice() {
        btn_printfile.setVisibility(View.GONE);

        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        PrintHelper photoPrinter = new PrintHelper(this);
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("Invoice_Print", bitmap);

        btn_printfile.setVisibility(View.VISIBLE);
    }
}
