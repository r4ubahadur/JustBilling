package com.smi.justbilling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.adapter.WaiterAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import in.smi.ru.uttamlibrary.Uttam;

public class WaiterReportActivity extends AppCompatActivity implements WaiterAdapter.OnItemClickListener {

    private DatabaseReference mDatabase;

    private RecyclerView mRecyclerView;

    private List<Uttam> mUploads;
    private WaiterAdapter mAdapter;

    private String date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_report);



        mDatabase = FirebaseDatabase.getInstance().getReference();




        mRecyclerView = findViewById(R.id.waiterRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();
        mAdapter = new WaiterAdapter(WaiterReportActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(WaiterReportActivity.this);




        mDatabase.child("waiter")
                .addValueEventListener(new ValueEventListener() {
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





        mDatabase.child("date").child("servervalue").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    String timestamp = Objects.requireNonNull(dataSnapshot.getValue()).toString();   // use ms NOT s
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                    SimpleDateFormat extraDate = new SimpleDateFormat("dd");
                    SimpleDateFormat extraMonth = new SimpleDateFormat("MM");
                    SimpleDateFormat extraYear = new SimpleDateFormat("yyyy");
                    SimpleDateFormat extraYearShortForm = new SimpleDateFormat("yy");

                    date = dateFormat.format(new Date(Long.parseLong(timestamp)));




                }

            }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });






                }

    @Override
    public void onItemClickKot(int position) {

        final String waiterName = ((TextView) mRecyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.waiter_single_name)).getText().toString();



        mDatabase.child("finalbill")
                .child("smi")
                .child("waiterReport")
                .child(date)
                .child(waiterName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            String number = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                            Toast.makeText(WaiterReportActivity.this, number, Toast.LENGTH_SHORT).show();

                        }else {


                            Toast.makeText(WaiterReportActivity.this, "No Table Complete Yet.", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



    }
}
