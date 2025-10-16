package com.example.Varsani.Staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.Varsani.R;

public class SelectLogin extends AppCompatActivity {

    private CardView btn_finance,btn_designer,btn_store_mrg, btn_service_mrg,
            btn_tech, card_dispatch, card_driver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_login);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_designer=findViewById(R.id.card_designer);
        btn_finance=findViewById(R.id.card_finance);
        btn_store_mrg=findViewById(R.id.card_inventory);
        btn_service_mrg = findViewById(R.id.card_service);
        btn_tech = findViewById(R.id.card_technician);
        card_dispatch = findViewById(R.id.card_dispatch);
        card_driver = findViewById(R.id.card_driver);


        btn_designer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String staff="Designer";
                Intent intent=new Intent(getApplicationContext(),StaffLogin.class);
                intent.putExtra("Staff",staff);
                startActivity(intent);
            }
        });
        btn_finance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String staff="Finance";
                Intent intent=new Intent(getApplicationContext(),StaffLogin.class);
                intent.putExtra("Staff",staff);
                startActivity(intent);
            }
        });
        btn_store_mrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String staff="Stock manager";
                Intent intent=new Intent(getApplicationContext(),StaffLogin.class);
                intent.putExtra("Staff",staff);
                startActivity(intent);
            }
        });

        btn_service_mrg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String staff="Service manager";
                Intent sv = new Intent(getApplicationContext(),StaffLogin.class);
                sv.putExtra("Staff",staff);
                startActivity(sv);
            }
        });

        btn_tech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String staff="Technician";
                Intent tc = new Intent(getApplicationContext(),StaffLogin.class);
                tc.putExtra("Staff",staff);
                startActivity(tc);
            }
        });

        card_dispatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String staff="Dispatch manager";
                Intent tc = new Intent(getApplicationContext(),StaffLogin.class);
                tc.putExtra("Staff",staff);
                startActivity(tc);
            }
        });

        card_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String staff="Driver";
                Intent tc = new Intent(getApplicationContext(),StaffLogin.class);
                tc.putExtra("Staff",staff);
                startActivity(tc);
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
}
