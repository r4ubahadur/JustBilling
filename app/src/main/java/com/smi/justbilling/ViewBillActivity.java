package com.smi.justbilling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewBillActivity extends AppCompatActivity {

    List<String> list = new ArrayList<>();
    private ListView viewBill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill);


        viewBill = findViewById(R.id.viewBill);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("finalbill")
                .child("smi")
                .child("date")
              //  .child("04-01-2019")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren()) {
                            String name = ds.getKey();
                            list.add(name);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(ViewBillActivity.this, android.R.layout.simple_spinner_item, list);
                        viewBill.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



        viewBill.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String date = (String) viewBill.getItemAtPosition(position);

                Intent goToDate = new Intent(ViewBillActivity.this, DayBillActivity.class);
                goToDate.putExtra("date" , date);
                startActivity(goToDate);


            }
        });


        SearchView searchView = findViewById(R.id.searchView);



    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        Intent first = new Intent(ViewBillActivity.this, ManagementActivity.class);
        startActivity(first);
        overridePendingTransition( R.anim.b2t_enter,  R.anim.t2exit);
        finish();


    }
}
