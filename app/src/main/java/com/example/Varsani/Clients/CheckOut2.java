package com.example.Varsani.Clients;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.VolleyMultipartRequest;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.utils.Urls;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class CheckOut2 extends AppCompatActivity {

    public static final String EXTRA_PRODUCT_ID   = "proID";
    public static final String EXTRA_PRODUCT_NAME = "proName";
    public static final String EXTRA_PRICE        = "price";
    public static final String EXTRA_QTY          = "qty";

    private static final int PICK_LOGO_IMAGE_REQUEST = 10;

    private SessionHandler session;
    private UserModel user;

    private String productID;
    private String productName;
    private String unitPriceStr;
    private double unitPrice = 0.0;
    private int qty = 1;

    private ProgressBar progressBar;

    private TextView txvName, txvEmail, txvPhone;
    private TextView txvProdName, txvUnitPrice, txvQty, txvTotal;

    private Spinner spPrintArea, spColor, spSize;
    private Button btnLogo;
    private TextView txvLogoName;
    private EditText edtNotes;

    private Button btnAddToCart;

    private Uri logoImageUri = null;

    private final DecimalFormat moneyFmt = new DecimalFormat("#,##0.00");

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out2);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Send Quotation Request");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        session = new SessionHandler(getApplicationContext());
        user    = session.getUserDetails();

        progressBar  = findViewById(R.id.progressBar);

        txvName   = findViewById(R.id.txv_name);
        txvEmail  = findViewById(R.id.txv_email);
        txvPhone  = findViewById(R.id.txv_phoneNo);

        txvProdName  = findViewById(R.id.txv_selected_service);
        txvUnitPrice = findViewById(R.id.txv_unit_price);
        txvQty       = findViewById(R.id.txv_quantity);
        txvTotal     = findViewById(R.id.txv_total_price);

        spPrintArea  = findViewById(R.id.sp_print_area);
        spColor      = findViewById(R.id.sp_color);
        spSize       = findViewById(R.id.sp_size);

        btnLogo      = findViewById(R.id.btn_logo);
        txvLogoName  = findViewById(R.id.txv_logo_name);
        edtNotes     = findViewById(R.id.edt_notes);

        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        progressBar.setVisibility(View.GONE);

        txvName.setText(" " + user.getFirstname() + " " + user.getLastname());
        txvEmail.setText(user.getEmail());
        txvPhone.setText(user.getPhoneNo());

        Intent in = getIntent();
        productID     = in.getStringExtra(EXTRA_PRODUCT_ID);
        productName   = in.getStringExtra(EXTRA_PRODUCT_NAME);
        unitPriceStr  = in.getStringExtra(EXTRA_PRICE);
        try { qty = Integer.parseInt(defaultIfEmpty(in.getStringExtra(EXTRA_QTY), "1")); }
        catch (Exception ignored) { qty = 1; }

        unitPrice = parsePrice(unitPriceStr);

        txvProdName.setText(productName != null ? productName : "");
        txvUnitPrice.setText("Unit: ksh " + moneyFmt.format(unitPrice));
        txvQty.setText(String.valueOf(qty));
        updateTotal();

        spPrintArea.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Front", "Back", "Left Chest", "Right Chest", "Sleeve", "Cap Front", "Cap Side"}));

        spColor.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Black", "White", "Red", "Blue", "Green", "Navy", "Maroon"}));

        spSize.setAdapter(new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_dropdown_item,
                new String[]{"Small", "Medium", "Large"}));

        edtNotes.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {}
        });

        btnLogo.setOnClickListener(v -> selectLogo());

        btnAddToCart.setOnClickListener(v -> {
            if (!session.isLoggedIn()) {
                Toast.makeText(this, "Please login to continue", Toast.LENGTH_SHORT).show();
                return;
            }
            addToCart(); // inline validation happens inside
        });
    }

    public void addToCart() {
        progressBar.setVisibility(View.VISIBLE);
        btnAddToCart.setVisibility(View.GONE);

        final String clientID    = user.getClientID();
        final String clientName  = txvName.getText().toString().trim();
        final String clientEmail = txvEmail.getText().toString().trim();
        final String clientPhone = txvPhone.getText().toString().trim();

        final String _productID   = defaultIfEmpty(productID, "");
        final String _productName = defaultIfEmpty(productName, "");
        final String _unitPrice   = String.valueOf(unitPrice);
        final String _quantity    = String.valueOf(qty);
        final String _total       = txvTotal.getTag() != null ? txvTotal.getTag().toString() : "0.0";

        final String _printArea = spPrintArea.getSelectedItem() != null ? spPrintArea.getSelectedItem().toString() : "";
        final String _color     = spColor.getSelectedItem() != null ? spColor.getSelectedItem().toString() : "";
        final String _size      = spSize.getSelectedItem() != null ? spSize.getSelectedItem().toString() : "";
        final String _notes     = defaultIfEmpty(edtNotes.getText().toString().trim(), "");

        // ===== REQUIRED ONLY: design (image) and notes =====
        if (logoImageUri == null) {
            Toast.makeText(getApplicationContext(), "Please upload your design image.", Toast.LENGTH_SHORT).show();
            btnAddToCart.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }
        if (TextUtils.isEmpty(_notes)) {
            Toast.makeText(getApplicationContext(), "Please enter design notes.", Toast.LENGTH_SHORT).show();
            btnAddToCart.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            return;
        }
        // ===== end required checks =====

        VolleyMultipartRequest req = new VolleyMultipartRequest(
                Request.Method.POST,
                Urls.URL_ADD_CART2,
                response -> {
                    try {
                        String body = new String(response.data);
                        JSONObject jsonObject = new JSONObject(body);

                        String status  = String.valueOf(jsonObject.opt("status")); // "1","2","0"
                        String message = jsonObject.optString("message", "Done");

                        if ("1".equals(status)) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            btnAddToCart.setVisibility(View.VISIBLE);
                            finish();
                        } else if ("2".equals(status)) {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            btnAddToCart.setVisibility(View.VISIBLE);
                            // Optional: int orderId = jsonObject.optInt("order_id", -1);
                            // openCart(orderId);
                        } else {
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            btnAddToCart.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                        btnAddToCart.setVisibility(View.VISIBLE);
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Network error: " + error, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    btnAddToCart.setVisibility(View.VISIBLE);
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                // Client
                params.put("clientID",   clientID);
                params.put("clientName", clientName);
                params.put("clientEmail",clientEmail);
                params.put("clientPhone",clientPhone);
                // Product
                params.put("productID",   _productID);
                params.put("productName", _productName);
                params.put("unitPrice",   _unitPrice);
                params.put("quantity",    _quantity);
                params.put("totalPrice",  _total);
                // Branding
                params.put("printArea",   _printArea);
                params.put("color",       _color);
                params.put("size",        _size);
                params.put("notes",       _notes);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() throws AuthFailureError {
                Map<String, DataPart> files = new HashMap<>();

                byte[] bytes = readBytesFromUri(logoImageUri);
                if (bytes != null && bytes.length > 0) {
                    String filename = getFileName(logoImageUri);
                    files.put("logoImage", new DataPart(filename, bytes));
                }
                return files;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }


    private byte[] readBytesFromUri(Uri uri) {
        try (java.io.InputStream is = getContentResolver().openInputStream(uri);
             java.io.ByteArrayOutputStream buffer = new java.io.ByteArrayOutputStream()) {
            byte[] data = new byte[4096];
            int nRead;
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            return buffer.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void updateTotal() {
        double total = unitPrice * qty;
        txvTotal.setText("Total: ksh " + moneyFmt.format(total));
        txvTotal.setTag(String.valueOf(total));
    }

    private double parsePrice(String s) {
        if (TextUtils.isEmpty(s)) return 0.0;
        try {
            String cleaned = s.replaceAll("[^0-9.,]", "").replace(",", "");
            return Double.parseDouble(cleaned);
        } catch (Exception e) {
            return 0.0;
        }
    }

    private String defaultIfEmpty(String s, String def) { return TextUtils.isEmpty(s) ? def : s; }

    private void selectLogo() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_LOGO_IMAGE_REQUEST);
        } else {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("image/*");
            startActivityForResult(Intent.createChooser(i, "Select Logo / Design"), PICK_LOGO_IMAGE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_LOGO_IMAGE_REQUEST &&
                grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            selectLogo();
        } else {
            toast("Permission denied.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_LOGO_IMAGE_REQUEST && data != null) {
            logoImageUri = data.getData();
            txvLogoName.setText(getFileName(logoImageUri));
            toast("Logo selected");
        }
    }

    private String getFileName(Uri uri) {
        String fileName = null;
        if (uri != null && "content".equals(uri.getScheme())) {
            Cursor c = getContentResolver().query(uri, null, null, null, null);
            try {
                if (c != null && c.moveToFirst()) {
                    int idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (idx >= 0) fileName = c.getString(idx);
                }
            } finally {
                if (c != null) c.close();
            }
        }
        if (fileName == null && uri != null) {
            fileName = uri.getPath();
            int cut = fileName != null ? fileName.lastIndexOf('/') : -1;
            if (cut != -1) fileName = fileName.substring(cut + 1);
        }
        return fileName != null ? fileName : "design.png";
    }

    private void toast(String m) { Toast.makeText(this, m, Toast.LENGTH_SHORT).show(); }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}
