package com.smi.justbilling;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SubCategoryActivity extends AppCompatActivity {

    Context context = this;
    List<String> subCategory = new ArrayList<>();

    private ProgressDialog mainProgressDialog;

    private DatabaseReference mDatabase;
    private String mainCategory;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);


        mainProgressDialog = new ProgressDialog(this);
        mainProgressDialog.setTitle("Please wait...");
        mainProgressDialog.setCanceledOnTouchOutside(false);
        mainProgressDialog.show();



        mainCategory = Objects.requireNonNull(super.getIntent().getExtras()).getString("catName");
        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView sub_category_head = findViewById(R.id.sub_category_head);
        sub_category_head.setText(mainCategory.toUpperCase()+" MENU");

        final ListView sub_category = findViewById(R.id.sub_category);

        mDatabase.child("products")
                .child("subCategory")
                .child(mainCategory)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        subCategory.clear();
                        for (DataSnapshot waiterSnapshot: dataSnapshot.getChildren()) {
                            String waiter = waiterSnapshot.child("name").getValue(String.class);
                            subCategory.add(waiter);
                        }


                        mainProgressDialog.dismiss();


                        ArrayAdapter<String> areasAdapter = new ArrayAdapter<>(SubCategoryActivity.this, android.R.layout.simple_spinner_item, subCategory);
                        sub_category.setAdapter(areasAdapter);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        FloatingActionButton add_sub_category = findViewById(R.id.add_sub_category);

        add_sub_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = SubCategoryActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.abc_sub_category, null);
                dialog.setView(dialogView);

                final EditText editText = dialogView.findViewById(R.id.addSubCategory);
                Button sub_cat_btn = dialogView.findViewById(R.id.sub_cat_btn);

                final AlertDialog alertDialog = dialog.create();

                sub_cat_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String subCatName = editText.getText().toString();

                        if (!subCatName.equals("")){

                            mDatabase.child("products")
                                    .child("subCategory")
                                    .child(mainCategory)
                                    .child(mDatabase.push().getKey())
                                    .child("name")
                                    .setValue(subCatName);

                            alertDialog.dismiss();

                        }

                    }
                });

                alertDialog.show();


            }
        });


        sub_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String sub_cat = ((TextView) view).getText().toString();


                Intent sbc = new Intent(SubCategoryActivity.this, SubItemListActivity.class);
                sbc.putExtra("catName", mainCategory);
                sbc.putExtra("subCat", sub_cat);
                startActivity(sbc);


            }
        });



        sub_category.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final String sub_cat = ((TextView) view).getText().toString();


                AlertDialog.Builder alert = new AlertDialog.Builder(SubCategoryActivity.this);
                alert.setMessage("Are you sure Delete this Category ?");
                alert.setTitle("Tech Guru");
                //alert.setCancelable(false);

                alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        dialog.dismiss();
                    }
                }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int i) {


                        mDatabase.child("products")
                                .child("subCategory")
                                .child(mainCategory)
                                .orderByChild("name").equalTo(sub_cat)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (DataSnapshot post : dataSnapshot.getChildren()){
                                            String push = post.getKey();

                                            mDatabase.child("products")
                                                    .child("subCategory")
                                                    .child(mainCategory)
                                                    .child(push).removeValue();

                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                    }
                });
                alert.show();






                return true;
            }
        });





        FloatingActionButton back_subCat_home = findViewById(R.id.back_subCat_home);
        back_subCat_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent first = new Intent(SubCategoryActivity.this, CategoryActivity.class);
                startActivity(first);
                overridePendingTransition(R.anim.l2r_enter, R.anim.r2exit);
                finish();


            }
        });









    }
}
