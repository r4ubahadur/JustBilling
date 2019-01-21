package com.smi.justbilling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class ReportActivity extends AppCompatActivity {

    private LinearLayout waiter_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        waiter_report = findViewById(R.id.waiter_report);

        waiter_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent waiter = new Intent(ReportActivity.this, WaiterReportActivity.class);
                startActivity(waiter);

            }
        });



    }
}
