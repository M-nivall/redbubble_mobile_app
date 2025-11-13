package com.example.Varsani.Clients;

import static com.example.Varsani.Clients.ServiceItems.ORDER_ID;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.Varsani.MainActivity;
import com.example.Varsani.R;
import com.example.Varsani.utils.SessionHandler;
import com.example.Varsani.utils.Urls;
import com.example.Varsani.Clients.Models.UserModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MakePayment extends AppCompatActivity {

    public static final String EXTRA_TOTAL = "EXTRA_TOTAL"; // ensure ServiceItems uses this key

    private static final String TAG = "MakePayment";
    private static final int DEFAULT_SHIPPING_FEE = 400;

    private SessionHandler session;
    private UserModel user;

    private RelativeLayout layout_card, layout_bottom;
    private TextView txv_items_total, txv_shipping_fee, txv_amount_to_pay;
    private EditText edt_county, edt_town, edt_address,edt_expected_date, edt_mpesaCode;
    private ProgressBar progressBar, progressBarTown, progressCheckout;
    private Button btn_order;
    private RadioGroup pay_group;
    private RadioButton rb_mpesa, rb_bank;

    private RadioGroup rg_shipping;
    private RadioButton rb_shipping_yes, rb_shipping_no;

    private final ArrayList<String> arrayCounties = new ArrayList<>();
    private final ArrayList<String> arrayTowns = new ArrayList<>();

    private String countyName;
    private String countyID;
    private String orderID;

    private String totalAmount = "0";
    private String grandTotal = "0";

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private DatePickerDialog datePicker;
    private String date,role;

    @RequiresApi(api = Build.VERSION_CODES.N)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Make Payment");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        totalAmount = getIntent().getStringExtra(EXTRA_TOTAL);
        orderID = getIntent().getStringExtra(ORDER_ID);
        //if (totalAmount == null) totalAmount = "0";

        // ----- bind views -----
        layout_card       = findViewById(R.id.layout_card);
        layout_bottom     = findViewById(R.id.layout_bottom);
        txv_items_total   = findViewById(R.id.txv_items_total);
        txv_shipping_fee  = findViewById(R.id.txv_shipping_fee);
        txv_amount_to_pay = findViewById(R.id.txv_amount_to_pay);

        edt_county        = findViewById(R.id.edt_county);
        edt_town          = findViewById(R.id.edt_town);
        edt_address       = findViewById(R.id.edt_Address);
        edt_expected_date = findViewById(R.id.edt_expected_date);
        edt_mpesaCode     = findViewById(R.id.edt_mpesaCode);

        progressBar       = findViewById(R.id.progressBar);
        progressBarTown   = findViewById(R.id.progressBarTown);
        progressCheckout  = findViewById(R.id.progressCheckout);
        btn_order         = findViewById(R.id.btn_order);
        pay_group         = findViewById(R.id.pay_group);
        rb_mpesa          = findViewById(R.id.rb_mpesa);
        rb_bank           = findViewById(R.id.rb_bank);

        rg_shipping       = findViewById(R.id.rg_shipping);
        rb_shipping_yes   = findViewById(R.id.rb_shipping_yes);
        rb_shipping_no    = findViewById(R.id.rb_shipping_no);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        date = dateFormat.format(calendar.getTime());
        edt_expected_date.setText(date);

        final Calendar calendar2 = Calendar.getInstance();
        final int day = calendar2.get(Calendar.DAY_OF_MONTH);
        final int year = calendar2.get(Calendar.YEAR);
        final int month = calendar2.get(Calendar.MONTH);

        datePicker = new DatePickerDialog(MakePayment.this);

        session = new SessionHandler(getApplicationContext());
        user    = session.getUserDetails();

        long itemsTotal = parseLongSafe(totalAmount);
        long shippingFee = DEFAULT_SHIPPING_FEE;

        // Initially include shipping
        long currentTotal = itemsTotal + shippingFee;
        txv_items_total.setText("Items Total: KES " + itemsTotal);
        txv_shipping_fee.setText("Shipping: KES " + shippingFee);
        txv_amount_to_pay.setText("Amount to Pay: KES " + currentTotal);

        // RadioGroup listener
        rg_shipping.setOnCheckedChangeListener((group, checkedId) -> {
            long total = itemsTotal;
            if (checkedId == R.id.rb_shipping_yes) {
                total += shippingFee;
                txv_shipping_fee.setText("Shipping: KES " + shippingFee);
            } else if (checkedId == R.id.rb_shipping_no) {
                txv_shipping_fee.setText("Shipping: KES 0");
            }
            txv_amount_to_pay.setText("Amount to Pay: KES " + total);
            grandTotal = String.valueOf(total); // Update grandTotal to send to backend
        });


        // ----- static UI state -----
        layout_bottom.setVisibility(View.VISIBLE);
        progressBarTown.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        progressCheckout.setVisibility(View.GONE);

        // pickers
        edt_county.setFocusable(false);
        edt_county.setOnClickListener(v -> {
            progressBarTown.setVisibility(View.VISIBLE);
            edt_town.setVisibility(View.GONE);
            edt_town.setText("");
            getAlertCounties(v);
        });

        edt_town.setFocusable(false);
        edt_town.setOnClickListener(v -> {
            String checkCounty = edt_county.getText().toString().trim();
            if (TextUtils.isEmpty(checkCounty)) {
                toast("Select county first");
            } else {
                getAlertTowns(v);
            }
        });

        edt_expected_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker = new DatePickerDialog(MakePayment.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                        // adding the selected date in the edittext
                        edt_expected_date.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);

                // set maximum date to be selected as today
                datePicker.getDatePicker().setMinDate(calendar.getTimeInMillis());

                // show the dialog
                datePicker.show();
            }
        });

        // Mpesa ref uppercase
        edt_mpesaCode.setFilters(new InputFilter[]{ new InputFilter.AllCaps() });

        // default method
        rb_mpesa.setChecked(true);

        btn_order.setOnClickListener(this::confirmSubmit);

        // prefill + lists
        getDlvyDetails();
        getCounties();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }

    /* ------------ Dialog helpers ------------ */

    private void getAlertCounties(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("County");
        final String[] array = arrayCounties.toArray(new String[0]);
        builder.setNegativeButton("Close", null);
        builder.setSingleChoiceItems(array, -1, (dialog, i) -> {
            edt_county.setText(array[i]);
            dialog.dismiss();
            countyName = array[i];
            getTowns();
        });
        builder.show();
    }

    private void getAlertTowns(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
        builder.setTitle("Town");
        final String[] array = arrayTowns.toArray(new String[0]);
        builder.setNegativeButton("Close", null);
        builder.setSingleChoiceItems(array, -1, (dialog, i) -> {
            edt_town.setText(array[i]);
            dialog.dismiss();
        });
        builder.show();
    }

    /* ------------ Submit flow ------------ */

    private void confirmSubmit(final View v) {
        new AlertDialog.Builder(v.getContext())
                .setTitle("Complete Payment")
                .setNegativeButton("Close", null)
                .setPositiveButton("Yes", (dialog, which) -> {
                    dialog.dismiss();
                    submitPayment();
                }).create().show();
    }

    private void submitPayment() {
        progressBar.setVisibility(View.VISIBLE);
        btn_order.setVisibility(View.GONE);

        final String county    = edt_county.getText().toString().trim();
        final String townName  = edt_town.getText().toString().trim();
        final String address   = edt_address.getText().toString().trim();
        final String reference = edt_mpesaCode.getText().toString().trim();
        final String method    = rb_mpesa.isChecked() ? "mpesa" : "bank";


        final String expected_date=edt_expected_date.getText().toString().trim();

        // delivery validation
        if (TextUtils.isEmpty(county))   { toast("Please select county"); done(); return; }
        if (TextUtils.isEmpty(townName)) { toast("Please select town");   done(); return; }
        if (TextUtils.isEmpty(address))  { toast("Please enter address"); done(); return; }

        // reference validation
        if (TextUtils.isEmpty(reference)) { toast("Please enter payment reference"); done(); return; }
        if ("mpesa".equals(method)) {
            if (reference.length() != 10) {
                toast("Mpesa reference should be 10 characters"); done(); return;
            }
            if (!reference.matches("^(?=.*[A-Z])(?=.*[0-9])[A-Z0-9]+$")) {
                toast("Mpesa reference must contain letters and digits"); done(); return;
            }
        }

        if(TextUtils.isEmpty(expected_date)){
            toast("Please enter the expected date"); done(); return;
        }

        // build values to send
        String orderCost    = String.valueOf(parseLongSafe(totalAmount));
        //String shippingCost = String.valueOf(DEFAULT_SHIPPING_FEE);
        String shippingCost = rb_shipping_yes.isChecked() ? String.valueOf(DEFAULT_SHIPPING_FEE) : "0";
        String totalCost    = grandTotal; // items + shipping

        StringRequest req = new StringRequest(
                Request.Method.POST,
                Urls.URL_SUBMIT_ORDER,
                response -> {
                    try {
                        JSONObject jo = new JSONObject(response);
                        String status = jo.optString("status", "0");
                        String msg    = jo.optString("message", "Done");

                        if ("1".equals(status)) {
                            progressBar.setVisibility(View.GONE);
                            layout_card.setVisibility(View.GONE);
                            new AlertDialog.Builder(MakePayment.this)
                                    .setTitle("Success")
                                    .setMessage(msg)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", (d, w) -> {
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        startActivity(i);
                                        d.dismiss();
                                    }).show();
                        } else {
                            toast(msg);
                            done();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        toast("Parse error: " + e.getMessage());
                        done();
                    }
                },
                error -> {
                    error.printStackTrace();
                    toast("Network error: " + error);
                    done();
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("clientID",      user.getClientID());
                p.put("orderID",       orderID);
                p.put("countyName",    countyName);
                p.put("townName",      townName);
                p.put("address",       address);
                p.put("orderCost",     orderCost);
                p.put("shippingCost",  shippingCost);
                p.put("mpesaCode",     reference);
                p.put("totalCost",     totalCost);
                p.put("paymentMethod", method);
                p.put("expectedDate", expected_date);
                Log.d(TAG, "params: " + p);
                return p;
            }
        };

        RequestQueue q = Volley.newRequestQueue(getApplicationContext());
        q.add(req);
    }

    private void done() {
        progressBar.setVisibility(View.GONE);
        btn_order.setVisibility(View.VISIBLE);
    }

    /* ------------ Data: counties / towns / prefill ------------ */

    private void getCounties() {
        StringRequest req = new StringRequest(
                Request.Method.POST,
                Urls.URL_GET_COUNTIES,
                response -> {
                    try {
                        JSONObject jo = new JSONObject(response);
                        if ("1".equals(jo.optString("status", "0"))) {
                            JSONArray arr = jo.optJSONArray("counties");
                            arrayCounties.clear();
                            if (arr != null) {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject c = arr.getJSONObject(i);
                                    arrayCounties.add(c.optString("countyName", ""));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        toast(e.toString());
                    }
                },
                error -> {
                    error.printStackTrace();
                    toast(error.toString());
                }
        );
        Volley.newRequestQueue(getApplicationContext()).add(req);
    }

    private void getTowns() {
        StringRequest req = new StringRequest(
                Request.Method.POST,
                Urls.URL_GET_TOWNS,
                response -> {
                    try {
                        JSONObject jo = new JSONObject(response);
                        String status = jo.optString("status", "0");
                        if ("1".equals(status)) {
                            JSONArray arr = jo.optJSONArray("towns");
                            arrayTowns.clear();
                            progressBarTown.setVisibility(View.GONE);
                            edt_town.setVisibility(View.VISIBLE);

                            if (arr != null) {
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject t = arr.getJSONObject(i);
                                    arrayTowns.add(t.optString("townName", ""));
                                    // capture countyID if provided in response
                                    String cid = t.optString("countyID", null);
                                    if (!TextUtils.isEmpty(cid)) countyID = cid;
                                }
                            }
                        } else {
                            toast(jo.optString("message", "Failed to load towns"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        toast(e.toString());
                    }
                },
                error -> {
                    error.printStackTrace();
                    toast(error.toString());
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("countyName", countyName == null ? "" : countyName);
                return p;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(req);
    }

    private void getDlvyDetails() {
        StringRequest req = new StringRequest(
                Request.Method.POST,
                Urls.URL_DELIVERY_DETAILS,
                response -> {
                    try {
                        // 1) Inspect raw
                        Log.d("API_RESPONSE", response);

                        // 2) Trim & quick sanity check
                        String body = response == null ? "" : response.trim();
                        if (!body.startsWith("{") && !body.startsWith("[")) {
                            // Server sent HTML or an error page
                            toast("Invalid server response");
                            Log.e("API_RESPONSE", "Unexpected payload: " + body);
                            return;
                        }

                        JSONObject jo = new JSONObject(body);
                        if ("1".equals(jo.optString("status", "0"))) {
                            JSONArray arr = jo.optJSONArray("details");
                            if (arr != null && arr.length() > 0) {
                                JSONObject d = arr.getJSONObject(0);
                                edt_county.setText(d.optString("county", ""));
                                edt_town.setText(d.optString("town", ""));
                                edt_address.setText(d.optString("ship_address", ""));
                                String cid = d.optString("county_ID", null);
                                if (!TextUtils.isEmpty(cid)) countyID = cid;
                            }
                        } else {
                            toast(jo.optString("message", "No delivery details"));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        toast("Parse error: " + e.getMessage());
                    }
                },
                error -> {
                    error.printStackTrace();
                    toast("Network error: " + error.toString());
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> p = new HashMap<>();
                p.put("clientID", user.getClientID());
                return p;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(req);
    }


    private long parseLongSafe(String s) {
        try { return Long.parseLong(s.replaceAll("[^0-9]", "")); }
        catch (Exception e) { return 0L; }
    }

    private void toast(String m) { Toast.makeText(this, m, Toast.LENGTH_SHORT).show(); }
}
