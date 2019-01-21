package com.smi.justbilling;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.adapter.SubItemAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.smi.ru.uttamlibrary.Uttam;

public class SubItemListActivity extends AppCompatActivity implements
        SubItemAdapter.OnItemClickListenerSub{

    Context context = this;
    List<String> subCategory = new ArrayList<>();

    private Toolbar mToolbar;
    private String mainCategory, subcategory;
    private ProgressDialog mainProgressDialog;
    private DatabaseReference mDatabase;

    private List<Uttam> subUploads;

    private SubItemAdapter subAdapter;
    private RecyclerView subItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_item_list);


        mainProgressDialog = new ProgressDialog(this);
        mainProgressDialog.setTitle("Please wait...");
        mainProgressDialog.setCanceledOnTouchOutside(false);
        mainProgressDialog.show();


        mainCategory = Objects.requireNonNull(super.getIntent().getExtras()).getString("catName");
        subcategory = Objects.requireNonNull(super.getIntent().getExtras()).getString("subCat");



        mToolbar = findViewById(R.id.subItemListToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(subcategory+" List");



        mDatabase = FirebaseDatabase.getInstance().getReference();


        subItemList = findViewById(R.id.subItemList);
        subItemList.setLayoutManager(new GridLayoutManager(this,3));
        subUploads = new ArrayList<>();
        subAdapter = new SubItemAdapter(SubItemListActivity.this, subUploads);
        subItemList.setAdapter(subAdapter);
        subAdapter.setOnItemClickListener(SubItemListActivity.this);



        mDatabase.child("products").child("head").child(mainCategory).child("category").child(subcategory)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        subUploads.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Uttam upload = postSnapshot.getValue(Uttam.class);
                            Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                            subUploads.add(upload);
                        }

                        mainProgressDialog.dismiss();
                        subAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


        FloatingActionButton add_sub_item = findViewById(R.id.add_sub_item);
        add_sub_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(SubItemListActivity.this, ItemUploadActivity.class);
                intent.putExtra("catName", mainCategory);
                intent.putExtra("subCat", subcategory);
                startActivity(intent);


            }
        });


        FloatingActionButton back_subItem_back = findViewById(R.id.back_subItem_back);
        back_subItem_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             finish();


            }
        });




    }

    @Override
    public void onItemClickSub(int position) {

    }
}
