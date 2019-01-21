package com.smi.justbilling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.adapter.SalesProductAdapter;
import com.smi.justbilling.adapter.BillAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.smi.ru.uttamlibrary.Uttam;

public class SalesActivity extends AppCompatActivity implements SalesProductAdapter.OnItemClickListener, BillAdapter.OnItemClickListener {


    private List<Uttam> nUpload;
    private BillAdapter nAdapter;

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);




        RecyclerView billView = findViewById(R.id.billView);

        billView.setHasFixedSize(true);
        billView.setLayoutManager(new LinearLayoutManager(this));





        nUpload = new ArrayList<>();
        nAdapter = new BillAdapter(SalesActivity.this, nUpload);

        billView.setAdapter(nAdapter);

        nAdapter.setOnItemClickListener(SalesActivity.this);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("bill").child("date").child("billNo");


       //


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int sum = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    Map<String, Object> map = (Map<String, Object>) ds.getValue();

                    Object price = map.get("price");

                    int value = Integer.parseInt(String.valueOf(price));
                    sum =+ sum + value;
                }


                TextView total2 = findViewById(R.id.billItemToatlPrice);
                total2.setText("₹ "+String.valueOf(sum));

                //  Toast.makeText(InvoiceActivity.this, String.valueOf(sum), Toast.LENGTH_SHORT).show();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                LinearLayout showBill = findViewById(R.id.showBill);

                if (dataSnapshot.exists()){

                    showBill.setVisibility(View.VISIBLE);

                }else {

                    showBill.setVisibility(View.GONE);

                }


                nUpload.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Uttam upload = postSnapshot.getValue(Uttam.class);
                    Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                    nUpload.add(upload);
                }

                nAdapter.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    @Override
    public void onItemClick(int position) {

    }


}
