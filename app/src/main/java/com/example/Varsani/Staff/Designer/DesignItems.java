package com.example.Varsani.Staff.Designer;

import static com.example.Varsani.utils.Urls.ROOT_URL_UPLOADS;
import static com.example.Varsani.utils.Urls.URL_UPLOAD_DESIGN;

import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.R;
import com.example.Varsani.VolleyMultipartRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class DesignItems extends AppCompatActivity {
    private TextView tv_orderID,tv_clientName,tv_county,tv_servName,tv_businessName,tv_serviceDesc,
            tv_dimension,tv_installationType,tv_expectedDate,tv_bookingStatus,tv_inputType,tv_town;
    private TextView tvSelectedFile;
    private ProgressBar progressBar;
    private Button btn_viewSketch,btn_viewLogo,btn_upload,btn_select_file;
    private String sketchImg,logoImg;
    private String sketchPdfUrl = ""; // Set these values after fetching from backend
    private String logoUrl = "";


    private String orderID,clientID;
    String orderStatus;
    private RequestQueue rQueue;
    Uri uri;
    String displayName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_designtems);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_orderID = findViewById(R.id.tv_orderID);
        tv_clientName = findViewById(R.id.tv_clientName);
        tv_county = findViewById(R.id.tv_county);
        tv_town = findViewById(R.id.tv_town);
        tv_servName = findViewById(R.id.tv_servName);
        tv_businessName = findViewById(R.id.tv_businessName);
        tv_serviceDesc = findViewById(R.id.tv_serviceDesc);
        tv_dimension = findViewById(R.id.tv_dimension);
        tvSelectedFile = findViewById(R.id.tvSelectedFile);
        tv_installationType = findViewById(R.id.tv_installationType);
        tv_expectedDate = findViewById(R.id.tv_expectedDate);
        tv_bookingStatus = findViewById(R.id.tv_bookingStatus);
        tv_inputType = findViewById(R.id.tv_inputType);
        btn_viewSketch = findViewById(R.id.btn_viewSketch);
        btn_viewLogo = findViewById(R.id.btn_viewLogo);
        btn_upload = findViewById(R.id.btn_upload);
        btn_select_file = findViewById(R.id.btn_select_file);
        progressBar = findViewById(R.id.progressBar);



        Intent intent=getIntent();

        orderID=intent.getStringExtra("orderID");
        clientID=intent.getStringExtra("clientID");
        String businessName=intent.getStringExtra("businessName");
        String servName=intent.getStringExtra("servName");
        String dimension=intent.getStringExtra("dimension");
        String serviceDesc=intent.getStringExtra("serviceDesc");
        String installationType=intent.getStringExtra("installationType");
        String inputText=intent.getStringExtra("inputText");
        sketchImg=intent.getStringExtra("sketchImg");
        logoImg=intent.getStringExtra("logoImg");
        String expectedDate=intent.getStringExtra("expectedDate");
        String clientName=intent.getStringExtra("clientName");
        String orderDate=intent.getStringExtra("orderDate");
        String address=intent.getStringExtra("address");
        String orderStatus=intent.getStringExtra("orderStatus");
        String county=intent.getStringExtra("county");
        String town=intent.getStringExtra("town");


        tv_orderID.setText("Booking ID: " + orderID);
        tv_clientName.setText("Client: " + clientName);
        tv_county.setText("County: " + county );
        tv_town.setText("Town: " + town );
        tv_servName.setText("Service: " + servName);
        tv_businessName.setText("Business Name: " + businessName);
        tv_serviceDesc.setText("Type: " + serviceDesc);
        tv_dimension.setText("Dimension: " + dimension);
        tv_installationType.setText("Installation Type: " + installationType);
        tv_expectedDate.setText("Expected Date: " + expectedDate);
        tv_bookingStatus.setText("Status: " + orderStatus);
        tv_inputType.setText(inputText);
        tv_dimension.setText("Dimension: " + dimension);


        btn_viewSketch.setOnClickListener(v -> openFile(sketchPdfUrl));
        btn_viewLogo.setOnClickListener(v -> openFile(logoUrl));

        btn_select_file.setOnClickListener(v -> {
            Intent i = new Intent();
            i.setAction(Intent.ACTION_GET_CONTENT);
            i.setType("application/pdf");
            startActivityForResult(i, 1);
        });

        btn_upload.setOnClickListener(v -> uploadPDF(displayName, uri));

        fetchFileUrls();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    private void fetchFileUrls() {
        // Construct the full URLs
        sketchPdfUrl = ROOT_URL_UPLOADS + "/" + sketchImg;
        logoUrl = ROOT_URL_UPLOADS + "/" + logoImg;
    }
    private void openFile(String url) {
        if (url == null || url.isEmpty()) {
            // Handle case where URL is not available
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("name  ", displayName);


                        if (!TextUtils.isEmpty(displayName)) {
                            // img_pdf.setVisibility(View.VISIBLE);
                        }
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("name  ", displayName);
            }

            if (!TextUtils.isEmpty(displayName)) {
                Log.d("Selected File", displayName);
                tvSelectedFile.setText("Selected File: " + displayName);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }
    private void uploadPDF(final String pdfname, Uri pdffile) {

        InputStream iStream = null;

        progressBar.setVisibility(View.VISIBLE);
        btn_select_file.setVisibility(View.GONE);
        btn_upload.setVisibility(View.GONE);


        try {

            iStream = getContentResolver().openInputStream(pdffile);
            final byte[] inputData = getBytes(iStream);

            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL_UPLOAD_DESIGN,
                    response -> {
                        Log.d("ressssssoo", new String(response.data));
                        rQueue.getCache().clear();
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            jsonObject.toString().replace("\\\\", "");
                            finish();
//                                Intent in=new Intent(getApplicationContext(),ReportsPendingApproval.class);
//                                startActivity(in);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            progressBar.setVisibility(View.GONE);
                            btn_select_file.setVisibility(View.VISIBLE);
                            btn_upload.setVisibility(View.VISIBLE);

                            Log.e("E ", "" + e);
                        }
                    },
                    error -> {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

                        progressBar.setVisibility(View.GONE);
                        btn_select_file.setVisibility(View.VISIBLE);
                        btn_upload.setVisibility(View.VISIBLE);
                        Log.e("ERROR ", "" + error);
                    }) {

                /*
                 * If you want to add more parameters with the pdf
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("orderID", orderID);
                    params.put("clientID", clientID);
                    Log.e("PARAMS ", "" + params);
                    return params;
                }

                /*
                 *pass files using below method
                 * */
                @Override
                protected Map<String, VolleyMultipartRequest.DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();

                    params.put("filename", new DataPart(pdfname, inputData));
                    Log.e("FILE NAME ", "" + params);
                    return params;
                }
            };


            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            rQueue = Volley.newRequestQueue(DesignItems.this);
            rQueue.add(volleyMultipartRequest);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

}