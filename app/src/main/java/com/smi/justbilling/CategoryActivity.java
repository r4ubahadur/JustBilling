package com.smi.justbilling;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    List<String> list = new ArrayList<>();
    private ListView category_list;

    private ProgressDialog mainProgressDialog;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category); //category_list


        mainProgressDialog = new ProgressDialog(this);
        mainProgressDialog.setTitle("Please wait...");
        mainProgressDialog.setCanceledOnTouchOutside(false);
        mainProgressDialog.show();

        category_list = findViewById(R.id.category_list);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("products").child("category")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        list.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            String upload = postSnapshot.child("name").getValue(String.class);
                            list.add(upload);
                        }

                        mainProgressDialog.dismiss();

                        ArrayAdapter<String> areasAdapter = new ArrayAdapter<>(CategoryActivity.this, android.R.layout.simple_spinner_item, list);
                        category_list.setAdapter(areasAdapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });



        category_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cat = ((TextView) view).getText().toString();
                Intent sub = new Intent(CategoryActivity.this, SubCategoryActivity.class);
                sub.putExtra("catName", cat);
                startActivity(sub);
            }
        });


        FloatingActionButton back_cat_home = findViewById(R.id.back_cat_home);
        back_cat_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent first = new Intent(CategoryActivity.this, ManagementActivity.class);
                startActivity(first);
                overridePendingTransition(R.anim.l2r_enter, R.anim.r2exit);
                finish();


            }
        });








    }


    @Override
    public void onBackPressed() {
        Intent first = new Intent(CategoryActivity.this, ManagementActivity.class);
        startActivity(first);
        overridePendingTransition(R.anim.l2r_enter, R.anim.r2exit);
        finish();
    }
}
