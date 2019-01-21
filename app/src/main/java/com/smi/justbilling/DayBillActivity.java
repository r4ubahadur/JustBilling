package com.smi.justbilling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DayBillActivity extends AppCompatActivity {


    List<String> list = new ArrayList<>();
    private ListView viewBill;

    private String date, billNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_bill);


        viewBill = findViewById(R.id.billDayList);


        date = Objects.requireNonNull(super.getIntent().getExtras()).getString("date");

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("finalbill")
                .child("smi")
                .child("date")
                .child(date)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        list.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name = ds.getKey();
                            list.add(name);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(DayBillActivity.this, android.R.layout.simple_spinner_item, list);
                        viewBill.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




        viewBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                billNo = (String) viewBill.getItemAtPosition(position);


                Intent goToDate = new Intent(DayBillActivity.this, OnlineViewActivity.class);
                goToDate.putExtra("billNo" , billNo);
                goToDate.putExtra("date", date);
                startActivity(goToDate);



            }
        });





    }
}
