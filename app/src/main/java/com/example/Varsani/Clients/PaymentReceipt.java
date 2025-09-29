package com.example.Varsani.Clients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.print.PrintHelper;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.utils.SessionHandler;

public class PaymentReceipt extends AppCompatActivity {
    private TextView orderID, clientName, txv_serviceType, txv_paymentDate, txv_fee, txv_paymentCode,receipt_number,receipt_date;
    private ImageView btn_printfile;
    private  String fullName;
    private SessionHandler session;
    private UserModel user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_receipt);

        // Initialize TextViews
        orderID = findViewById(R.id.receipt_orderID);
        clientName = findViewById(R.id.receipt_clientName);
        txv_serviceType = findViewById(R.id.txv_serviceType);
        txv_paymentDate = findViewById(R.id.txv_paymentDate);
        txv_fee = findViewById(R.id.txv_fee);
        txv_paymentCode = findViewById(R.id.txv_paymentCode);
        receipt_number = findViewById(R.id.receipt_number);
        receipt_date = findViewById(R.id.receipt_date);
        btn_printfile = findViewById(R.id.btn_printfile);

        session=new SessionHandler(getApplicationContext());
        user=session.getUserDetails();

        fullName = user.getFirstname() + " " + user.getLastname();

        // Retrieve and display values from the Intent
        orderID.setText("Booking No: " + getIntent().getStringExtra("orderID"));
        txv_serviceType.setText("Service: " + getIntent().getStringExtra("serviceName"));
        txv_paymentDate.setText("Payment Date: " + getIntent().getStringExtra("bookingDate"));
        txv_fee.setText("Amount: Ksh " + getIntent().getStringExtra("serviceFee"));
        receipt_number.setText("Receipt No: B25-N" +getIntent().getStringExtra("orderID"));
        receipt_date.setText("Date: " + getIntent().getStringExtra("bookingDate"));
        clientName.setText("Client Name: " + fullName);
        txv_paymentCode.setText("Payment Code: KQR4YNMLOZ");

        btn_printfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
            }
        });
    }
    private void print(){
        btn_printfile.setVisibility(View.GONE);

        View view = getWindow().getDecorView().findViewById(android.R.id.content);
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),View. MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        PrintHelper photoPrinter = new PrintHelper(this); // Assume that 'this' is your activity
        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
        photoPrinter.printBitmap("print", bitmap);

        btn_printfile.setVisibility(View.VISIBLE);
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 250);
        toast.show();
    }
}