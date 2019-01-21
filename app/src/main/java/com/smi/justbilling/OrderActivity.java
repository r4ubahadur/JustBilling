package com.smi.justbilling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        Intent mnb = new Intent(OrderActivity.this, SalesActivity.class);
        startActivity(mnb);

        finish();


    }
}
