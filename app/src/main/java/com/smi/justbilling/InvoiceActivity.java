package com.smi.justbilling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.adapter.SalesProductAdapter;
import com.smi.justbilling.adapter.BillAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.smi.ru.uttamlibrary.Uttam;

public class InvoiceActivity extends AppCompatActivity implements SalesProductAdapter.OnItemClickListener,
        SalesProductAdapter.OnItemLongClickListener  {

    private List<Uttam> mUploads;
    private SalesProductAdapter mAdapter;
    private List<Uttam> bUploads;
    private BillAdapter nAdapter;
    private DatabaseReference mDatabaseRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);


        RecyclerView salesRecyclerView = findViewById(R.id.salesRecyclerView);
        salesRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mUploads = new ArrayList<>();
        bUploads = new ArrayList<>();
        mAdapter = new SalesProductAdapter(InvoiceActivity.this, mUploads);
       // bUploads = new SalesProductAdapter(InvoiceActivity.this, mUploads);

        salesRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(InvoiceActivity.this);
        mAdapter.setOnItemLongClickListener(InvoiceActivity.this);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        mDatabaseRef.child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
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








        mDatabaseRef.child("bill").child("date").child("billNo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int sum = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    Map<String, Object> map = (Map<String, Object>) ds.getValue();

                    Object price = map.get("price");

                    int value = Integer.parseInt(String.valueOf(price));
                    sum =+ sum + value;
                }


                TextView total = findViewById(R.id.total);
                total.setText(String.valueOf(sum));

                //  Toast.makeText(InvoiceActivity.this, String.valueOf(sum), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });














    }


    @Override
    public void onItemClick(int position) {

        Uttam clickItem = mUploads.get(position);
        final String rp = clickItem.getKey();



        mDatabaseRef.child("bill").child("date").child("billNo").child(rp).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {

                    mDatabaseRef.child("bill").child("date").child("billNo").child(rp).removeValue();

                    // Do stuff
                } else {
                    // Do stuff

                    mDatabaseRef.child("products").child(rp).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String name = dataSnapshot.child("name").getValue().toString();
                            String price = dataSnapshot.child("price").getValue().toString();

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", name);
                            userMap.put("price", price);

                            mDatabaseRef.child("bill").child("date").child("billNo").child(rp).setValue(userMap);



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });















                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
































    }

    @Override
    public void onItemLongClick(int position) {


        RelativeLayout uttamtest = findViewById(R.id.uttamtest);

        uttamtest.setVisibility(View.VISIBLE);



    }



}
