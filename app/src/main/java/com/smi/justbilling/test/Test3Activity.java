package com.smi.justbilling.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.R;

import java.util.ArrayList;
import java.util.List;

public class Test3Activity extends AppCompatActivity {

    List<String> areas = new ArrayList<>();
    private String selecteditem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test3);


        final Spinner areaSpinner = findViewById(R.id.spinner2);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("a").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("aName").getValue(String.class);

                    areas.add(areaName);
                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<>(Test3Activity.this, android.R.layout.simple_spinner_item, areas);

                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                areaSpinner.setAdapter(areasAdapter);

                areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                        selecteditem =  parent.getItemAtPosition(position).toString();
                        if (selecteditem.equals("Select One")){


                        }else {

                            Toast.makeText(Test3Activity.this, selecteditem, Toast.LENGTH_SHORT).show();

                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });













    }
}
