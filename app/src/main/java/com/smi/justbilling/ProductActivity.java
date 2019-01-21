package com.smi.justbilling;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.adapter.ProductAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.smi.ru.uttamlibrary.Uttam;

public class ProductActivity extends AppCompatActivity implements ProductAdapter.OnItemClickListener {


    private Toolbar productToolbar, productToolbar2;
    private List<Uttam> mUploads;
    private ProductAdapter mAdapter;
    private DatabaseReference mDatabaseRef;
    private Bundle bundle;

    private boolean ascending = true;

    private FloatingActionButton productAdd;

   // private  RecyclerView productRecyclerView;
   // private  ScrollView productView;
    private RelativeLayout layout_one,layout_two;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        layout_one = findViewById(R.id.layout_one);
        layout_two = findViewById(R.id.layout_two);



        productAdd = findViewById(R.id.productAdd);

        productToolbar = findViewById(R.id.productToolbar);

        productToolbar2 = findViewById(R.id.productToolbar2);

        productToolbar.setTitle(Html.fromHtml("<font color='#ffffff'>Product</font>"));
        productToolbar2.setTitle(Html.fromHtml("<font color='#ffffff'>Product</font>"));


        productToolbar.inflateMenu(R.menu.single_item_list);
        productToolbar2.inflateMenu(R.menu.single_item);

        productToolbar2.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.action_edit) {
                    productEdit();
                //    Toast.makeText(ProductActivity.this, "This Function on hold by ADMIN",Toast.LENGTH_SHORT).show();


                }
                else{
                        // do something
                }
                return false;
            }
        });


        productToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.action_sort_by) {

                }
                else{
                    // do something
                }
                return false;
            }
        });




        bundle = new Bundle();



        mDatabaseRef = FirebaseDatabase.getInstance().getReference();
        RecyclerView productRecyclerView = findViewById(R.id.productRecyclerView);

        mUploads = new ArrayList<>();
        mAdapter = new ProductAdapter(ProductActivity.this, mUploads);

        productRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(ProductActivity.this);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));


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


















        productRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {

                    // Scrolling up
                    productAdd.setVisibility(View.VISIBLE);

                } else {
                    // Scrolling down
                    productAdd.setVisibility(View.GONE);

                }
                }


                });
















        productAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent upload = new Intent(ProductActivity.this, AddItemActivity.class);
                startActivity(upload);

            }
        });








    }









    private void productEdit() {

        Intent edit = new Intent(ProductActivity.this, EditProductActivity.class);
        startActivity(edit);

    }

    @Override
    public void onItemClick(int position) {

        Uttam clickItem = mUploads.get(position);
        final String ruma = clickItem.getKey();

      //  bundle.putString("key1", ruma); // Key1


        openSingleItem(ruma); // manually method

        // Toast.makeText(MainActivity.this, "This Function on hold by ADMIN",Toast.LENGTH_SHORT).show();


    }

    private void openSingleItem(String ruma) {

        mDatabaseRef.child("products").child(ruma).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ImageView imagePreview = findViewById(R.id.imagePreview);

                String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString();
                String imageUrl = dataSnapshot.child("thumbImageUrl").getValue().toString();

                Uri uri = Uri.parse(imageUrl);
                Picasso.with(ProductActivity.this).load(uri).placeholder(R.drawable.gift).into(imagePreview);

                TextView itemTitle = findViewById(R.id.itemTitle);
                layout_one.setVisibility(View.GONE);
                layout_two.setVisibility(View.VISIBLE);
                itemTitle.setText(name);

                productAdd.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    @Override
    public void onBackPressed() {


        if (layout_one.getVisibility()==View.GONE){
            layout_one.setVisibility(View.VISIBLE);
            layout_two.setVisibility(View.GONE);
            productAdd.setVisibility(View.VISIBLE);

        }else {
            super.onBackPressed();
        }

    }






}
