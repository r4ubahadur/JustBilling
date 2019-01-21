package com.smi.justbilling.test;

import android.os.Build;
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
import java.util.Locale;
import java.util.Objects;


public class Test2Activity extends AppCompatActivity {


    ArrayList<String> names = new ArrayList<String>();
    ArrayAdapter<String> adapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);


        Spinner sp= findViewById(R.id.spinner1);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_single_choice, names);

        adapter.add("1");
        adapter.add("2");
        adapter.add("3");
        adapter.add("4");
        adapter.add("5");
        adapter.add("6");
        adapter.add("7");
        adapter.add("8");



        sp.setAdapter(adapter);












    }


    }