package com.smi.justbilling;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.adapter.BillSingleItemAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.smi.ru.uttamlibrary.Uttam;

public class OnlineViewActivity extends AppCompatActivity implements BillSingleItemAdapter.OnItemClickListener {


    private List<Uttam> mUploads;
    private BillSingleItemAdapter mAdapter;
    private DatabaseReference mDatabase;

    private String date, billNo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_view);




        date = Objects.requireNonNull(super.getIntent().getExtras()).getString("date");
        billNo = Objects.requireNonNull(super.getIntent().getExtras()).getString("billNo");

        TextView bill_number = findViewById(R.id.bill_number);
        bill_number.setText("Bill No:- "+billNo);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        RecyclerView onlineBill = findViewById(R.id.onlineBill);

        mUploads = new ArrayList<>();
        mAdapter = new BillSingleItemAdapter(OnlineViewActivity.this, mUploads);

        onlineBill.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(OnlineViewActivity.this);
        onlineBill.setLayoutManager(new LinearLayoutManager(this));


        mDatabase.child("finalbill")
                .child("smi")
                .child("date")
                .child(date)
                .child(billNo)
                .child("ItemList")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){
                           // Toast.makeText(OnlineViewActivity.this, ">>>>>>>", Toast.LENGTH_SHORT).show();
                        }

                        mUploads.clear();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Uttam upload = postSnapshot.getValue(Uttam.class);
                            Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                            mUploads.add(upload);
                        }

                        mAdapter.notifyDataSetChanged();






                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




        final TextView billTotalAmount = findViewById(R.id.billTotalAmount);


        mDatabase.child("finalbill")
                .child("smi")
                .child("date")
                .child(date)
                .child(billNo)
                .child("ItemList")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int sum = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    Map<String, Object> map = (Map<String, Object>) ds.getValue();

                    Object price = map.get("price2");

                    int value = Integer.parseInt(String.valueOf(price));
                    sum =+ sum + value;
                }

                billTotalAmount.setText("₹ "+String.valueOf(sum));

                //  Toast.makeText(InvoiceActivity.this, String.valueOf(sum), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });







        TextView bill_date = findViewById(R.id.bill_date);

        Date today = Calendar.getInstance().getTime();//getting date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(today);
        bill_date.setText("Date:- "+date);


        Button nextBilling = findViewById(R.id.nextBilling);
        nextBilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent rp = new Intent(OnlineViewActivity.this, FirstActivity.class);
                startActivity(rp);
                finish();

            }
        });










    }

    @Override
    public void onItemClick(int position) {

    }
}
