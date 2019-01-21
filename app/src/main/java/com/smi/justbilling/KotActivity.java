package com.smi.justbilling;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.adapter.DrinkAdapter;
import com.smi.justbilling.adapter.FoodAdapter;
import com.smi.justbilling.adapter.KotProductAdapter;
import com.smi.justbilling.adapter.RiceAdapter;
import com.smi.justbilling.adapter.SubCatAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.smi.ru.uttamlibrary.Uttam;

public class KotActivity extends AppCompatActivity implements
       // SalesProductAdapter.OnItemClickListener,
        KotProductAdapter.OnItemClickListener,
        DrinkAdapter.OnItemClickListenerDrink,
        FoodAdapter.OnItemClickListenerFood,
        RiceAdapter.OnItemClickListenerRice,
        SubCatAdapter.OnItemClickListenerSub {


    private LinearLayout main_cat_section, sub_category_llayout;


    private RecyclerView dRecyclerView, mRecyclerView, nRecyclerView, fRecyclerView, rRecyclerView, subRecyclerView;

    private DatabaseReference mDatabase;

    private List<Uttam> dUploads;
    private List<Uttam> fUploads, mUploads, nUploads, rUploads, subUploads;

    private DrinkAdapter dAdapter;
    private FoodAdapter fAdapter;
    private RiceAdapter rAdapter;

    private SubCatAdapter subAdapter;


    private KotProductAdapter nAdapter;

    private String tableName, waiterName, tableNumber, date, currentBillNumber, companyShortName = "SMI", financialYearSortForm;
    private String cateName, subCateName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kot);


        tableNumber = Objects.requireNonNull(super.getIntent().getExtras()).getString("tableNumber");
        tableName = Objects.requireNonNull(super.getIntent().getExtras()).getString("TableName");
        waiterName = Objects.requireNonNull(super.getIntent().getExtras()).getString("WaiterName");


        mDatabase = FirebaseDatabase.getInstance().getReference();




        main_cat_section = findViewById(R.id.main_cat_section);
        sub_category_llayout = findViewById(R.id.sub_category_llayout);








        dRecyclerView = findViewById(R.id.drink_category);
        dRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        dUploads = new ArrayList<>();
        dAdapter = new DrinkAdapter(KotActivity.this, dUploads);
        dRecyclerView.setAdapter(dAdapter);
        dAdapter.setOnItemClickListener(KotActivity.this);








        fRecyclerView = findViewById(R.id.food_category);
        fRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        fUploads = new ArrayList<>();
        fAdapter = new FoodAdapter(KotActivity.this, fUploads);
        fRecyclerView.setAdapter(fAdapter);
        fAdapter.setOnItemClickListener(KotActivity.this);













        rRecyclerView = findViewById(R.id.rice_category);
        rRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        rUploads = new ArrayList<>();
        rAdapter = new RiceAdapter(KotActivity.this, rUploads);
        rRecyclerView.setAdapter(rAdapter);
        rAdapter.setOnItemClickListener(KotActivity.this);








        subRecyclerView = findViewById(R.id.sub_category_layout);
        subRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
        subUploads = new ArrayList<>();
        subAdapter = new SubCatAdapter(KotActivity.this, subUploads);
        subRecyclerView.setAdapter(subAdapter);
        subAdapter.setOnItemClickListener(KotActivity.this);





























        mDatabase.child("finalbill").child("currentBill")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("number").exists()){
                            currentBillNumber = Objects.requireNonNull(dataSnapshot.child("number").getValue()).toString();
                        }else {
                            mDatabase.child("finalbill")
                                    .child("currentBill")
                                    .child("number")
                                    .setValue("1");
                            currentBillNumber = "1";
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });













        /*
        mRecyclerView = findViewById(R.id.salesProduct);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        mUploads = new ArrayList<>();
        mAdapter = new SalesProductAdapter(KotActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(KotActivity.this);
        */





        nRecyclerView = findViewById(R.id.kotSales);
        nRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        nUploads = new ArrayList<>();
        nAdapter = new KotProductAdapter(KotActivity.this, nUploads);
        nRecyclerView.setAdapter(nAdapter);
        nAdapter.setOnItemClickListener(KotActivity.this);


       // mDatabase.keepSynced(true);


        mDatabase.child("uttam").child(tableName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                nUploads.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Uttam upload = postSnapshot.getValue(Uttam.class);
                    Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                    nUploads.add(upload);
                }
                nAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        FloatingActionButton add_product = findViewById(R.id.add_product);
        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent first = new Intent(KotActivity.this, CategoryActivity.class);
                startActivity(first);
            }
        });

        mDatabase.child("uttam").child(tableName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){



                            int sum = 0;
                            for(DataSnapshot ds : dataSnapshot.getChildren()) {

                                Map<String,Object> map = (Map<String,Object>) ds.getValue();

                                Object price = Objects.requireNonNull(map).get("price2");

                                if (price==null){

                                }else {
                                    int value = Integer.parseInt(String.valueOf(price));
                                    sum =+ sum + value;
                                }
                            }


                            TextView total2 = findViewById(R.id.total_kot_price);
                            total2.setText("₹ "+String.valueOf(sum)+".00");


                        }else {

                            TextView total2 = findViewById(R.id.total_kot_price);
                            total2.setText("₹ 0.00");

                        }



                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



        TextView processKey = findViewById(R.id.processKey);
        processKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabase.child("date").child("servervalue").setValue(ServerValue.TIMESTAMP)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    mDatabase.child("date").child("servervalue").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.exists()){
                                                String timestamp = Objects.requireNonNull(dataSnapshot.getValue()).toString();   // use ms NOT s
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy" );
                                                SimpleDateFormat extraDate = new SimpleDateFormat("dd" );
                                                SimpleDateFormat extraMonth = new SimpleDateFormat("MM" );
                                                SimpleDateFormat extraYear = new SimpleDateFormat("yyyy" );
                                                SimpleDateFormat extraYearShortForm = new SimpleDateFormat("yy" );

                                                date = dateFormat.format(new Date(Long.parseLong(timestamp)));


                                                String exDate = extraDate.format(new Date(Long.parseLong(timestamp)));
                                                String exMonth = extraMonth.format(new Date(Long.parseLong(timestamp)));
                                                String exYear = extraYear.format(new Date(Long.parseLong(timestamp)));
                                                String exYearShortForm = extraYearShortForm.format(new Date(Long.parseLong(timestamp)));


                    // Extra Work for financialYear Result

                                                int month = Integer.parseInt(exMonth);

                                                if (1 <= month && month <= 3){

                                                    int fShortForm = Integer.parseInt(exYearShortForm)-1;
                                                    financialYearSortForm = String.valueOf(fShortForm)+"-"+exYearShortForm;

                                                }else {

                                                    int fShortForm = Integer.parseInt(exYearShortForm)+1;
                                                    financialYearSortForm = exYearShortForm+"-"+String.valueOf(fShortForm);

                                                }

                     // Extra Work for financialYear Result



                                               // Toast.makeText(KotActivity.this, financialYearSortForm, Toast.LENGTH_SHORT).show();


                                                mDatabase.child("uttam").child(tableName)
                                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                                mDatabase.child("finalbill").child("smi").child("date").child(date)
                                                                        .child(companyShortName+"-"+financialYearSortForm+"-"+currentBillNumber)
                                                                        .child("ItemList")
                                                                        .setValue(dataSnapshot.getValue())
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                                mDatabase.child("uttam").child(tableName).removeValue();
                                                                                mDatabase.child("uttam").child("waiter").child(tableName).removeValue();

                                                                                Toast.makeText(KotActivity.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                                                                                finish();
                                                                                viewBill(date);


                   int nextBill = Integer.parseInt(currentBillNumber)+1;

                   mDatabase.child("finalbill").child("currentBill")
                           .child("number").setValue(String.valueOf(nextBill));




                   mDatabase.child("finalbill")
                           .child("smi")
                           .child("waiterReport")
                           .child(date)
                           .child(waiterName)
                           .addListenerForSingleValueEvent(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   if (dataSnapshot.exists()){
                                       String count2 = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                                       int u = Integer.parseInt(count2);
                                       int total = u+1;
                                       String totall = String.valueOf(total);

                                      // Toast.makeText(KotActivity.this,count2 , Toast.LENGTH_SHORT).show();

        mDatabase.child("finalbill").child("smi").child("waiterReport").child(date).child(waiterName).setValue(totall);
         }else {
            mDatabase.child("finalbill").child("smi").child("waiterReport").child(date).child(waiterName).setValue("1");
                                     //  Toast.makeText(KotActivity.this,"1" , Toast.LENGTH_SHORT).show();
            }
         }
                               @Override
                               public void onCancelled(DatabaseError databaseError) {

                               }
                           });













                                                                            }
                   });

                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });










                                                //  Toast.makeText(ManagementActivity.this, dateString, Toast.LENGTH_SHORT).show();
                                            }else {
                                                Toast.makeText(KotActivity.this, "Please Check Your Internet", Toast.LENGTH_SHORT).show();

                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        });

            }
        });






        TextView kotBtn = findViewById(R.id.kotBtn);
        kotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mDatabase.child("date").child("servervalue").setValue(ServerValue.TIMESTAMP)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {

                                    mDatabase.child("date").child("servervalue").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            if (dataSnapshot.exists()) {
                                                String timestamp = Objects.requireNonNull(dataSnapshot.getValue()).toString();   // use ms NOT s
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy");
                                                date = dateFormat.format(new Date(Long.parseLong(timestamp)));

                                                Intent abc = new Intent(KotActivity.this, KotPrintActivity.class);
                                                abc.putExtra("waiterName", waiterName);
                                                abc.putExtra("tableName", tableName);
                                                abc.putExtra("tableNumber", tableNumber);
                                                abc.putExtra("date", date);
                                                startActivity(abc);


                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }
                        });


            }
        });


        mDatabase.child("uttam")
                .child(tableName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (!dataSnapshot.exists()){

                            mDatabase.child("uttam").child("waiter").child(tableName).removeValue();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





               // food menu start



        mDatabase.child("products")
                .child("subCategory")
                .child("Drink")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        dUploads.clear();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Uttam upload = postSnapshot.getValue(Uttam.class);
                            Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                            dUploads.add(upload);
                        }

                        dAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });










        mDatabase.child("products")
                .child("subCategory")
                .child("Food")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        fUploads.clear();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Uttam upload = postSnapshot.getValue(Uttam.class);
                            Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                            fUploads.add(upload);
                        }

                        fAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



        mDatabase.child("products")
                .child("subCategory")
                .child("Rice")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {



                        rUploads.clear();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Uttam upload = postSnapshot.getValue(Uttam.class);
                            Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                            rUploads.add(upload);
                        }

                        rAdapter.notifyDataSetChanged();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



    }




    private void viewBill(String date) {

        Intent viewBill = new Intent(KotActivity.this, OnlineViewActivity.class);
        viewBill.putExtra("date", date);
        viewBill.putExtra("billNo", "SMI-18-19-"+currentBillNumber);
        startActivity(viewBill);

    }


    @Override
    protected void onStart() {

        super.onStart();




    }

    @Override
    public void onBackPressed() {

        LinearLayout main_cat_section = findViewById(R.id.main_cat_section);
        LinearLayout sub_category_llayout = findViewById(R.id.sub_category_llayout);

        if (main_cat_section.getVisibility()==View.GONE){
            main_cat_section.setVisibility(View.VISIBLE);
            sub_category_llayout.setVisibility(View.GONE);

        }else {

            Intent first = new Intent(KotActivity.this, FirstActivity.class);
            startActivity(first);
            overridePendingTransition(R.anim.b2t_enter, R.anim.t2exit );

            finish();

        }



    }




























































    @Override
    public void onItemClickKot(int position) {

        Uttam clickItem = nUploads.get(position);
        final String ruma = clickItem.getKey();

      //  Toast.makeText(KotActivity.this, ruma, Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alert = new AlertDialog.Builder(KotActivity.this);

        alert.setMessage("Please Choose Any Button From Below");
        alert.setTitle("Tech Guru");
        alert.setCancelable(false);

        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.dismiss();

            }
        }).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int i) {

                dialog.dismiss();


            }
        }).setNeutralButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {




                mDatabase.child("uttam").child(tableName).child(ruma).removeValue()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){

                                    dialog.dismiss();
                                }
                            }
                        });

            }
        });

        alert.show();








    }



    @Override
    public void onItemClickDrink(int position) {

        LinearLayout main_cat_section = findViewById(R.id.main_cat_section);
        LinearLayout sub_category_llayout = findViewById(R.id.sub_category_llayout);
        String catName = ((TextView) dRecyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.category_name)).getText().toString();
        main_cat_section.setVisibility(View.GONE);
        sub_category_llayout.setVisibility(View.VISIBLE);

        cateName = "Drink"; subCateName = catName;

        mDatabase.child("products")
                .child("head")
                .child("Drink")
                .child("category")
                .child(catName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        subUploads.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Uttam upload = postSnapshot.getValue(Uttam.class);
                            Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                            subUploads.add(upload);
                        }
                        subAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });





    }

    @Override
    public void onItemClickFood(int position) {


        main_cat_section = findViewById(R.id.main_cat_section);
        sub_category_llayout = findViewById(R.id.sub_category_llayout);
        String catName = ((TextView) fRecyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.category_name)).getText().toString();
        main_cat_section.setVisibility(View.GONE);
        sub_category_llayout.setVisibility(View.VISIBLE);

        cateName = "Food"; subCateName = catName;

        mDatabase.child("products")
                .child("head")
                .child("Food")
                .child("category")
                .child(catName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        subUploads.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Uttam upload = postSnapshot.getValue(Uttam.class);
                            Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                            subUploads.add(upload);
                        }
                        subAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });





    }

    @Override
    public void onItemClickRice(int position) {



        LinearLayout main_cat_section = findViewById(R.id.main_cat_section);
        LinearLayout sub_category_llayout = findViewById(R.id.sub_category_llayout);
        String catName = ((TextView) rRecyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.category_name)).getText().toString();
        main_cat_section.setVisibility(View.GONE);
        sub_category_llayout.setVisibility(View.VISIBLE);

        cateName = "Rice"; subCateName = catName;

        mDatabase.child("products")
                .child("head")
                .child("Rice")
                .child("category")
                .child(catName)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        subUploads.clear();
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Uttam upload = postSnapshot.getValue(Uttam.class);
                            Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                            subUploads.add(upload);
                        }
                        subAdapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });





    }

















































    @Override
    public void onItemClickSub(int position) {

        Uttam clickItem = subUploads.get(position);
        final String rp = clickItem.getKey();



        mDatabase.child("uttam")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(tableName).child(rp).exists()){
                            String count = Objects.requireNonNull(dataSnapshot.child(tableName).child(rp).child("count").getValue()).toString();

                            if (count.equals("")){
                                int countPlus = 2;
                                String totalCount = String.valueOf(countPlus);
                                mDatabase.child("uttam").child(tableName).child(rp).child("count").setValue(totalCount);

                            }else {
                                int countt = Integer.parseInt(count);
                                int countPlus = countt+1;
                                String totalCount = String.valueOf(countPlus);
                                mDatabase.child("uttam").child(tableName).child(rp).child("count").setValue(totalCount);
                            }

                            String price = Objects.requireNonNull(dataSnapshot.child(tableName).child(rp).child("price").getValue()).toString();
                            String countP = Objects.requireNonNull(dataSnapshot.child(tableName).child(rp).child("count").getValue()).toString();

                            if (countP.equals("")){

                                int price2 = Integer.parseInt(price);
                                int countP2 = 2;

                                int three = price2*countP2;

                                String totalPrice = String.valueOf(three);

                                mDatabase.child("uttam").child(tableName).child(rp).child("price2").setValue(totalPrice);

                            }else {

                                int price2 = Integer.parseInt(price);
                                int countP2 = Integer.parseInt(countP);

                                int three = price2*(countP2+1);

                                String totalPrice = String.valueOf(three);

                                mDatabase.child("uttam").child(tableName).child(rp).child("price2").setValue(totalPrice);

                            }


                        }else {
                            mDatabase.child("products").child("head").child(cateName).child("category").child(subCateName)
                                    .child(rp)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            Uttam upload = dataSnapshot.getValue(Uttam.class);
                                            mDatabase.child("uttam").child(tableName).child(rp).setValue(upload);
                                            mDatabase.child("uttam").child(tableName).child(rp).child("price2").setValue(dataSnapshot.child("price").getValue());
                                            // mDatabase.child("uttam").setValue(dataSnapshot.getValue());
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


        if (!waiterName.equals("")){

            mDatabase.child("uttam")
                    .child("waiter")
                    .child(tableName)
                    .setValue(waiterName);

        }












    }
















}
