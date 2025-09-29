package com.example.Varsani.Clients;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.utils.Urls;
import com.squareup.picasso.Picasso;

public class SingleService extends AppCompatActivity {

    private static final String TAG = "Order Now";

    private SessionHandler session;
    private UserModel user;

    // Intent extras received
    private String productID;
    private String productName;
    private String stock;     // optional, numeric
    private String desc;      // not shown here, kept for future
    private String imagename;
    private String price;     // unit price string (e.g., "25.00" or "Ksh 25.00")

    // Views
    private ImageView imageView;
    private TextView txvTitle;
    private TextView txvPrice;
    private EditText edtQuantity;
    private Button btnPlus, btnMinus;
    private Button btn_quotation;
    private ProgressBar progressBar;

    // Quantity clamp
    private int maxStock = Integer.MAX_VALUE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_service);

        // === Get extras from previous screen ===
        Intent intent = getIntent();
        productID   = intent.getStringExtra("proID");
        productName = intent.getStringExtra("proName");
        stock       = intent.getStringExtra("stock");
        desc        = intent.getStringExtra("desc");
        imagename   = intent.getStringExtra("image");
        price       = intent.getStringExtra("price");

        if (getSupportActionBar() != null) {
            getSupportActionBar().setSubtitle(productName != null ? productName : "");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Session
        session = new SessionHandler(getApplicationContext());
        user    = session.getUserDetails();

        // === Bind views (IDs must match your activity_single_service.xml) ===
        imageView     = findViewById(R.id.image_c);
        txvTitle      = findViewById(R.id.txv_title);
        txvPrice      = findViewById(R.id.txv_price);
        edtQuantity   = findViewById(R.id.edt_quantity);
        btnPlus       = findViewById(R.id.btn_plus);
        btnMinus      = findViewById(R.id.btn_minus);
        btn_quotation = findViewById(R.id.btn_quotation); // your "Proceed to Quotation" button
        progressBar   = findViewById(R.id.progressBar);

        // Initial UI
        if (progressBar != null) progressBar.setVisibility(View.GONE);
        txvTitle.setText("Product: " + (productName != null ? productName : "")); // parentheses fix

        if (!TextUtils.isEmpty(price)) {
            txvPrice.setText("Price " + price + " / unit");
            txvPrice.setVisibility(View.VISIBLE);
        } else {
            txvPrice.setVisibility(View.GONE);
        }

        // Stock clamp
        if (!TextUtils.isEmpty(stock)) {
            try {
                maxStock = Math.max(1, Integer.parseInt(stock));
            } catch (NumberFormatException ignored) {
                maxStock = Integer.MAX_VALUE;
            }
        }

        // Default quantity = 1, keep within [1, maxStock]
        edtQuantity.setText("1");
        edtQuantity.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                int q = safeQtyFromEdit();
                if (q < 1) setQty(1);
                else if (q > maxStock) setQty(maxStock);
            }
        });

        // Load image (optional preview on this screen)
        String base = Urls.ROOT_URL_IMAGES; // ensure trailing slash if server requires it
        Picasso.get()
                .load((TextUtils.isEmpty(imagename) ? "" : base + imagename))
                .placeholder(R.drawable.ic_menu_gallery)
                .error(R.drawable.ic_menu_gallery)
                .fit()
                .centerInside()
                .into(imageView);

        // +/– listeners
        btnPlus.setOnClickListener(v -> {
            int q = safeQtyFromEdit();
            if (q < maxStock) setQty(q + 1);
            else Toast.makeText(this, "Max stock reached", Toast.LENGTH_SHORT).show();
        });

        btnMinus.setOnClickListener(v -> {
            int q = safeQtyFromEdit();
            if (q > 1) setQty(q - 1);
        });

        // Proceed to Quotation (navigate to CheckOut2 and PASS details)
        btn_quotation.setOnClickListener(v -> {
            if (!session.isLoggedIn()) {
                Toast.makeText(getApplicationContext(), "You must login to proceed", Toast.LENGTH_SHORT).show();
                return;
            }
            int q = safeQtyFromEdit();
            if (q < 1) { Toast.makeText(this, "Quantity must be at least 1", Toast.LENGTH_SHORT).show(); return; }
            if (q > maxStock) { Toast.makeText(this, "Quantity exceeds available stock", Toast.LENGTH_SHORT).show(); return; }

            // Build intent for CheckOut2 and pass required details
            Intent i = new Intent(SingleService.this, CheckOut2.class);
            // Use the constants from CheckOut2 to avoid key mismatches
            i.putExtra(CheckOut2.EXTRA_PRODUCT_ID,   productID != null ? productID : "");
            i.putExtra(CheckOut2.EXTRA_PRODUCT_NAME, productName != null ? productName : "");
            i.putExtra(CheckOut2.EXTRA_PRICE,        price != null ? price : "0");
            i.putExtra(CheckOut2.EXTRA_QTY,          String.valueOf(q));

            startActivity(i);
        });
    }

    private int safeQtyFromEdit() {
        try {
            String t = edtQuantity.getText().toString().trim();
            if (t.isEmpty()) return 1;
            return Integer.parseInt(t);
        } catch (Exception e) {
            return 1;
        }
    }

    private void setQty(int q) {
        String s = String.valueOf(q);
        if (!s.equals(edtQuantity.getText().toString())) {
            edtQuantity.setText(s);
            edtQuantity.setSelection(edtQuantity.getText().length());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
