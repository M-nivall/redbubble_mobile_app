package com.example.Varsani.Staff.ServMrg;

import static com.example.Varsani.utils.Urls.ROOT_URL_UPLOADS;
import static com.example.Varsani.utils.Urls.URL_APPROVE_DESIGN;
import static com.example.Varsani.utils.Urls.URL_DESIGN_ITEMS;
import static com.example.Varsani.utils.Urls.URL_REJECT_DESIGN;
import static com.example.Varsani.utils.Urls.URL_UPLOAD_DESIGN;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.Clients.Models.UserModel;
import com.example.Varsani.R;
import com.example.Varsani.VolleyMultipartRequest;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ItemInformation extends AppCompatActivity {

    private TextView tvProduct, tvPrintArea, tvSize, tvQuantity, tvInputText, tvSelectedFile;
    private Button btn_viewDesign, btn_upload, btn_upload_design;
    private ProgressBar progressBar1;
    private LinearLayout layout_upload_design;

    private String files_url = Urls.ROOT_URL_BRAND_DESIGNS;
    private String url, image_url, orderID, itemId, designFile, designStatus, logoFile;
    private String logoUrl = "";
    private SessionHandler session;
    private UserModel user;
    private RequestQueue rQueue;
    private Uri uri;
    private String displayName = null;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_information);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Initialize views
        tvProduct = findViewById(R.id.tvProduct);
        tvPrintArea = findViewById(R.id.tvPrintArea);
        tvSize = findViewById(R.id.tvSize);
        tvQuantity = findViewById(R.id.tvQuantity);
        tvInputText = findViewById(R.id.tvInputText);
        tvSelectedFile = findViewById(R.id.tvSelectedFile);
        layout_upload_design = findViewById(R.id.layout_upload_design);
        progressBar1 = findViewById(R.id.progressBar1);
        btn_viewDesign = findViewById(R.id.btn_viewDesign);
        btn_upload = findViewById(R.id.btn_upload);
        btn_upload_design = findViewById(R.id.btn_upload_design);

        session = new SessionHandler(getApplicationContext());
        user = session.getUserDetails();

        layout_upload_design.setVisibility(View.GONE);

        // Get intent data
        Intent intent = getIntent();
        itemId = intent.getStringExtra("itemId");
        orderID = intent.getStringExtra("orderID");
        String itemName = intent.getStringExtra("itemName");
        image_url = intent.getStringExtra("image_url");

        tvProduct.setText("Product: " + itemName);

        getItems();

        // Open design URL
        btn_viewDesign.setOnClickListener(v -> openFile(logoUrl));

        // File selection
        btn_upload_design.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_PICK);
            intent1.setType("image/*");
            intent1.putExtra(Intent.EXTRA_MIME_TYPES, new String[]{"image/*"});
            startActivityForResult(Intent.createChooser(intent1, "Select Image"), PICK_IMAGE_REQUEST);
        });

        // Upload selected file
        btn_upload.setOnClickListener(v -> {
            if (uri != null && displayName != null) {
                uploadFile(displayName, uri);
            } else {
                Toast.makeText(this, "Please select a file first.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFile(String url) {
        if (url == null || url.isEmpty()) {
            Toast.makeText(this, "File not available", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            if (uri != null) {
                displayName = null;
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } finally {
                    if (cursor != null) cursor.close();
                }

                if (TextUtils.isEmpty(displayName)) {
                    displayName = uri.getLastPathSegment();
                }

                if (!TextUtils.isEmpty(displayName)) {
                    tvSelectedFile.setText("Selected File: " + displayName);
                    tvSelectedFile.setVisibility(View.VISIBLE);
                    Log.d("Selected File", displayName);
                }
            }
        }
    }

    public void getItems() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DESIGN_ITEMS,
                response -> {
                    try {
                        Log.e("RESPONSE ", response);
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("responseData");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsn = jsonArray.getJSONObject(i);
                                String quantity = jsn.getString("quantity");
                                String printArea = jsn.getString("printArea");
                                String dimension = jsn.getString("dimension");
                                String notes = jsn.getString("notes");
                                designStatus = jsn.getString("designStatus");
                                designFile = jsn.getString("designFile");
                                logoFile = jsn.getString("fileName");

                                logoUrl = ROOT_URL_UPLOADS + "/" + logoFile;

                                tvPrintArea.setText("Print Area: " + printArea);
                                tvSize.setText("Size: " + dimension);
                                tvQuantity.setText("Quantity: " + quantity);
                                tvInputText.setText(notes);

                                if (user.getUser_type().equalsIgnoreCase("Designer") &&
                                        designFile.equalsIgnoreCase("NULL") &&
                                        designStatus.equalsIgnoreCase("Pending approval")) {
                                    layout_upload_design.setVisibility(View.VISIBLE);
                                }

                                url = files_url + designFile;
                            }

                        } else {
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                }, error -> {
            error.printStackTrace();
            Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("itemId", itemId);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void uploadFile(final String fileName, Uri fileUri) {
        InputStream iStream;
        progressBar1.setVisibility(View.VISIBLE);
        btn_upload.setVisibility(View.GONE);

        try {
            iStream = getContentResolver().openInputStream(fileUri);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL_UPLOAD_DESIGN,
                    response -> {
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar1.setVisibility(View.GONE);
                            btn_upload.setVisibility(View.VISIBLE);
                        }
                    },
                    error -> {
                        Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar1.setVisibility(View.GONE);
                        btn_upload.setVisibility(View.VISIBLE);
                    }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("orderID", orderID);
                    params.put("itemId", itemId);
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    params.put("filename", new DataPart(fileName, inputData));
                    return params;
                }
            };

            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            rQueue = Volley.newRequestQueue(this);
            rQueue.add(volleyMultipartRequest);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
