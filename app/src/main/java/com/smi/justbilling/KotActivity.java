package com.smi.justbilling;

import android.annotation.SuppressLint;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.adapter.AllItemAdapter;
import com.smi.justbilling.adapter.DrinkAdapter;
import com.smi.justbilling.adapter.FoodAdapter;
import com.smi.justbilling.adapter.KotProductAdapter;
import com.smi.justbilling.adapter.RiceAdapter;
import com.smi.justbilling.adapter.SubCatAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import in.smi.ru.uttamlibrary.Uttam;

public class KotActivity extends AppCompatActivity implements
       // SalesProductAdapter.OnItemClickListener,

        KotProductAdapter.IMethodCaller,
        AllItemAdapter.OnAllItemClickListener,
        KotProductAdapter.OnItemClickListenerInvoice,
        DrinkAdapter.OnItemClickListenerDrink,
        FoodAdapter.OnItemClickListenerFood,
        RiceAdapter.OnItemClickListenerRice,
        SubCatAdapter.OnItemClickListenerSub {



    protected LinearLayoutManager mLayoutManager;

    private LinearLayout main_cat_section, sub_category_llayout, search_layout;
    private ScrollView all_cat_layout;

    private RecyclerView dRecyclerView, nRecyclerView, fRecyclerView, rRecyclerView, subRecyclerView, allRecyclerView;

    private DatabaseReference mDatabase;

    private List<Uttam> dUploads, fUploads, nUploads, rUploads, subUploads, allUploads;

    List<Integer> list = new ArrayList<>();

    private DrinkAdapter dAdapter;
    private FoodAdapter fAdapter;
    private RiceAdapter rAdapter;
    private SubCatAdapter subAdapter;
    private KotProductAdapter nAdapter;
    private AllItemAdapter allAdapter;


    private EditText item_search;
    private Button search_btn;


    private String tableName, waiterName, tableNumber, date, specialDate, currentBillNumber, currentKotNumber, companyShortName = "SMI", financialYearSortForm;
    private String cateName, subCateName, maxNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kot);


        tableNumber = Objects.requireNonNull(super.getIntent().getExtras()).getString("tableNumber");
        tableName = Objects.requireNonNull(super.getIntent().getExtras()).getString("TableName");
        waiterName = Objects.requireNonNull(super.getIntent().getExtras()).getString("WaiterName");


        mDatabase = FirebaseDatabase.getInstance().getReference();


        search_layout = findViewById(R.id.search_layout);
        all_cat_layout = findViewById(R.id.all_cat_layout);

        item_search = findViewById(R.id.item_search);
        search_btn = findViewById(R.id.search_btn);

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




        /*
        mRecyclerView = findViewById(R.id.salesProduct);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));
        mUploads = new ArrayList<>();
        mAdapter = new SalesProductAdapter(KotActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(KotActivity.this);
        */
        mLayoutManager = new LinearLayoutManager(this);

        nRecyclerView = findViewById(R.id.kotSales);
        nRecyclerView.setLayoutManager(mLayoutManager);
        nUploads = new ArrayList<>();
        nAdapter = new KotProductAdapter(KotActivity.this, nUploads);
        nRecyclerView.setAdapter(nAdapter);
        nAdapter.setOnItemClickListener(KotActivity.this);

       // mDatabase.keepSynced(true);

        mDatabase.child("uttam").child(tableName).orderByChild("time").addValueEventListener(new ValueEventListener() {
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

                        TextView TotalAmount = findViewById(R.id.total_kot_price);
                        TextView tax_total = findViewById(R.id.tax_total);

                        if (dataSnapshot.exists()){

                            double sum2 = 0;
                            double sum3 = 0;
                            double tax = 0;

                            for(DataSnapshot ru : dataSnapshot.getChildren()) {

                                Map<String,Object> map = (Map<String,Object>) ru.getValue();

                                Object priceTwo = Objects.requireNonNull(map).get("price2");
                                Object priceThree = Objects.requireNonNull(map).get("price3");
                                Object priceFour = Objects.requireNonNull(map).get("disPrice");

                                if (priceTwo==null ||priceThree==null ){
                                }else {
                                    double price2 = Double.parseDouble(String.valueOf(priceTwo));
                                    double price3 = Double.parseDouble(String.valueOf(priceThree));

                                    sum2 =+ sum2 + price2;
                                    sum3 =+ sum3 + price3;
                                    tax = sum2 - sum3;
                                }

                            }

                            TotalAmount.setText("₹ "+String.format("%.2f", sum2));
                            tax_total.setText("₹ "+String.format("%.2f", tax));

                        }else {
                            TotalAmount.setText("₹ 0.00");
                            tax_total.setText("₹ 0.00");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



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

        mDatabase.child("finalbill").child("currentKot").child("number")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            currentKotNumber = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                        }else {
                            mDatabase.child("finalbill")
                                    .child("currentKot")
                                    .child("number")
                                    .setValue("0");
                            currentKotNumber = "0";
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


                mDatabase.child("uttam").child(tableName)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()){


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

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });






            }
        });






        TextView kotBtn = findViewById(R.id.kotBtn);
        kotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("uttam").child(tableName)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){

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
                                                                    SimpleDateFormat specialDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                                                    date = dateFormat.format(new Date(Long.parseLong(timestamp)));
                                                                    specialDate = specialDateFormat.format(new Date(Long.parseLong(timestamp)));

                                                                    Intent abc = new Intent(KotActivity.this, KotPrintActivity.class);
                                                                    abc.putExtra("waiterName", waiterName);
                                                                    abc.putExtra("tableName", tableName);
                                                                    abc.putExtra("tableNumber", tableNumber);
                                                                    abc.putExtra("date", date);
                                                                    abc.putExtra("kotNumber", currentKotNumber);
                                                                    abc.putExtra("specialDate", specialDate);
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

                                }else {

                                    Toast.makeText(KotActivity.this, "No Item Selected", Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

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












        allRecyclerView = findViewById(R.id.allRecyclerView);
        allRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        allUploads = new ArrayList<>();
        allAdapter = new AllItemAdapter(KotActivity.this, allUploads);
        allRecyclerView.setAdapter(allAdapter);
        allAdapter.setOnItemClickListener(KotActivity.this);





        item_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                final String item = item_search.getText().toString().toLowerCase();
                search_layout.setVisibility(View.VISIBLE);
                all_cat_layout.setVisibility(View.GONE);

                Query query = mDatabase.child("products").child("allItem").orderByChild("name").startAt(item).endAt(item+"\uf8ff");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (!dataSnapshot.exists()){ allUploads.clear(); }

                        if (!item.equals("")){
                            allUploads.clear();
                            for (DataSnapshot a : dataSnapshot.getChildren()) {
                                Uttam item = a.getValue(Uttam.class);
                                Objects.requireNonNull(item).setKey(a.getKey());
                                allUploads.add(item);
                            }
                            allAdapter.notifyDataSetChanged();
                        }

                        if (item.equals("")){

                            all_cat_layout.setVisibility(View.VISIBLE);
                            search_layout.setVisibility(View.GONE);

                        }


                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });









        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String item = item_search.getText().toString().toLowerCase();
              //  Toast.makeText(KotActivity.this, item , Toast.LENGTH_SHORT).show();

                search_layout.setVisibility(View.VISIBLE);
                all_cat_layout.setVisibility(View.GONE);

                Query query = mDatabase.child("products").child("allItem").orderByChild("name").startAt(item).endAt(item+"\uf8ff");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (!dataSnapshot.exists()){ allUploads.clear(); }

                                if (!item.equals("")){

                                    if (item.equals("all")||item.equals("total")||item.equals("full")||item.equals("all item")){


         mDatabase.child("products").child("allItem").addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {

                 allUploads.clear();
                 for (DataSnapshot a : dataSnapshot.getChildren()) {
                     Uttam item = a.getValue(Uttam.class);
                     Objects.requireNonNull(item).setKey(a.getKey());
                     allUploads.add(item);
                 }
                 allAdapter.notifyDataSetChanged();

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });


                                    }else {

                                        allUploads.clear();
                                        for (DataSnapshot a : dataSnapshot.getChildren()) {
                                            Uttam item = a.getValue(Uttam.class);
                                            Objects.requireNonNull(item).setKey(a.getKey());
                                            allUploads.add(item);
                                        }
                                        allAdapter.notifyDataSetChanged();

                                    }



                                }









                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

            }
        });



        FloatingActionButton back_kot_home = findViewById(R.id.back_kot_home);
        back_kot_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayout main_cat_section = findViewById(R.id.main_cat_section);
                LinearLayout sub_category_llayout = findViewById(R.id.sub_category_llayout);

                if (search_layout.getVisibility()==View.VISIBLE){
                    search_layout.setVisibility(View.GONE);
                    all_cat_layout.setVisibility(View.VISIBLE);
                    item_search.getText().clear();
                    item_search.clearFocus();

                }else {

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

        if (search_layout.getVisibility()==View.VISIBLE){
            search_layout.setVisibility(View.GONE);
            all_cat_layout.setVisibility(View.VISIBLE);
            item_search.getText().clear();
            item_search.clearFocus();

        }else {

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


    }






    @Override
    public void onItemClickInvoice(int position) {

        Uttam clickItem = nUploads.get(position);
        final String ruma = clickItem.getKey();


        AlertDialog.Builder alert = new AlertDialog.Builder(KotActivity.this);
        alert.setMessage("Please Choose Any Button From Below");
        alert.setTitle(R.string.smi);

        alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {

                dialog.dismiss();
            }
        }).setNegativeButton("Edit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int i) {
                Intent intent = new Intent(KotActivity.this, EditInvoiceItemActivity.class);
                intent.putExtra("tableName",tableName);
                intent.putExtra("key",ruma);
                startActivity(intent);

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

        cateName = "Rice";
        subCateName = catName;

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

        final int getItemCount = nRecyclerView.getAdapter().getItemCount();

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
                            String tax = Objects.requireNonNull(dataSnapshot.child(tableName).child(rp).child("tax").getValue()).toString();


                            if (countP.equals("")){

                                int price2 = Integer.parseInt(price);
                                int countP2 = 2;
                                int three = price2*countP2;

                                String totalPrice = String.valueOf(three);

                                double p1 = (double) three;
                                double t1 =  Double.parseDouble(tax);
                                double p2 =  ((p1*100)/(100+t1));


                                @SuppressLint("DefaultLocale")
                                String price3 = String.format("%.2f", p2);

                                HashMap<String, Object> map = new HashMap<>();
                                map.put("price2", totalPrice);
                                //map.put("price3", price3);

                                mDatabase.child("uttam").child(tableName).child(rp).updateChildren(map);



                            }else {

                                int price2 = Integer.parseInt(price);
                                int countP2 = Integer.parseInt(countP);

                                int three = price2*(countP2+1);

                                String totalPrice = String.valueOf(three);

                                double p1 = (double) three;
                                double t1 =  Double.parseDouble(tax);
                                double p2 =  ((p1*100)/(100+t1));

                                @SuppressLint("DefaultLocale")
                                String price3 = String.format("%.2f", p2);

                                HashMap<String, Object> map2 = new HashMap<>();
                                map2.put("price2", totalPrice);
                                //map2.put("price3", price3);

                                mDatabase.child("uttam").child(tableName).child(rp).updateChildren(map2);


                            }


                        }else {
                            mDatabase.child("products").child("head").child(cateName).child("category").child(subCateName)
                                    .child(rp)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {


                                            String price = Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString();
                                            String tax = Objects.requireNonNull(dataSnapshot.child("tax").getValue()).toString();


                                            double p1 =  Double.parseDouble(price);
                                            double t1 =  Double.parseDouble(tax);
                                            double p2 =  ((p1*100)/(100+t1));

                                            @SuppressLint("DefaultLocale")
                                            String price3 = String.format("%.2f", p2);

                                            HashMap<String, Object> map3 = new HashMap<>();

                                            map3.put("time", ServerValue.TIMESTAMP);
                                            map3.put("price2", Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString());
                                            //map3.put("price3", price3);

                                            Uttam upload = dataSnapshot.getValue(Uttam.class);
                                            mDatabase.child("uttam").child(tableName).child(rp).setValue(upload);
                                            mDatabase.child("uttam").child(tableName).child(rp).updateChildren(map3);

                                            nRecyclerView.smoothScrollToPosition(getItemCount);



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


        mDatabase.child("uttam").child(tableName).child(rp)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("tax").exists()){

                            String tax = Objects.requireNonNull(dataSnapshot.child("tax").getValue()).toString();
                            String price = Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString();
                            String count = Objects.requireNonNull(dataSnapshot.child("count").getValue()).toString();

                            if (count.equals("")){

                                double p =  Double.parseDouble(price);

                                double t1 =  Double.parseDouble(tax);

                                double p3 =  ((p*100)/(100+t1));

                                @SuppressLint("DefaultLocale")
                                String price3 = String.format("%.2f", p3);

                                mDatabase.child("uttam").child(tableName).child(rp)
                                        .child("price3").setValue(price3);

                            }else {

                                double p =  Double.parseDouble(price);
                                double c =  Double.parseDouble(count);

                                double t = p*c;
                                double t1 =  Double.parseDouble(tax);

                                double p3 =  ((t*100)/(100+t1));

                                @SuppressLint("DefaultLocale")
                                String price3 = String.format("%.2f", p3);

                                mDatabase.child("uttam").child(tableName).child(rp)
                                        .child("price3").setValue(price3);
                            }

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


    @Override
    public void onAllItemClick(int position) {

        final int getItemCount = nRecyclerView.getAdapter().getItemCount();

        Uttam clickItem = allUploads.get(position);
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
                            String tax = Objects.requireNonNull(dataSnapshot.child(tableName).child(rp).child("tax").getValue()).toString();

                            if (countP.equals("")){

                                int price2 = Integer.parseInt(price);
                                int countP2 = 2;
                                int three = price2*countP2;

                                String totalPrice = String.valueOf(three);

                                double p1 = (double) three;
                                double t1 =  Double.parseDouble(tax);
                                double p2 =  ((p1*100)/(100+t1));

                                @SuppressLint("DefaultLocale")
                                String price3 = String.format("%.2f", p2);

                                HashMap<String, Object> map = new HashMap<>();
                                map.put("price2", totalPrice);
                                //map.put("price3", price3);

                                mDatabase.child("uttam").child(tableName).child(rp).updateChildren(map);

                            }else {

                                int price2 = Integer.parseInt(price);
                                int countP2 = Integer.parseInt(countP);

                                int three = price2*(countP2+1);

                                String totalPrice = String.valueOf(three);

                                double p1 = (double) three;
                                double t1 =  Double.parseDouble(tax);
                                double p2 =  ((p1*100)/(100+t1));

                                @SuppressLint("DefaultLocale")
                                String price3 = String.format("%.2f", p2);

                                HashMap<String, Object> map2 = new HashMap<>();
                                map2.put("price2", totalPrice);
                                //map2.put("price3", price3);

                                mDatabase.child("uttam").child(tableName).child(rp).updateChildren(map2);
                            }

                        }else {
                            mDatabase.child("products").child("allItem")
                                    .child(rp)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            String price = Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString();
                                            String tax = Objects.requireNonNull(dataSnapshot.child("tax").getValue()).toString();

                                            double p1 =  Double.parseDouble(price);
                                            double t1 =  Double.parseDouble(tax);
                                            double p2 =  ((p1*100)/(100+t1));

                                            @SuppressLint("DefaultLocale")
                                            String price3 = String.format("%.2f", p2);

                                            HashMap<String, Object> map3 = new HashMap<>();
                                            map3.put("time", ServerValue.TIMESTAMP);
                                            map3.put("price2", Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString());
                                            //map3.put("price3", price3);

                                            Uttam upload = dataSnapshot.getValue(Uttam.class);
                                            mDatabase.child("uttam").child(tableName).child(rp).setValue(upload);
                                            mDatabase.child("uttam").child(tableName).child(rp).updateChildren(map3);

                                            nRecyclerView.smoothScrollToPosition(getItemCount);

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



        mDatabase.child("uttam").child(tableName).child(rp)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child("tax").exists()){

                            String tax = Objects.requireNonNull(dataSnapshot.child("tax").getValue()).toString();
                            String price = Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString();
                            String count = Objects.requireNonNull(dataSnapshot.child("count").getValue()).toString();

                            if (count.equals("")){

                                double p =  Double.parseDouble(price);

                                double t1 =  Double.parseDouble(tax);

                                double p3 =  ((p*100)/(100+t1));

                                @SuppressLint("DefaultLocale")
                                String price3 = String.format("%.2f", p3);

                                mDatabase.child("uttam").child(tableName).child(rp)
                                        .child("price3").setValue(price3);

                            }else {

                                double p =  Double.parseDouble(price);
                                double c =  Double.parseDouble(count);

                                double t = p*c;
                                double t1 =  Double.parseDouble(tax);

                                double p3 =  ((t*100)/(100+t1));

                                @SuppressLint("DefaultLocale")
                                String price3 = String.format("%.2f", p3);

                                mDatabase.child("uttam").child(tableName).child(rp)
                                        .child("price3").setValue(price3);
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });






    }


    @Override
    public void itemCount(final String text) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(KotActivity.this);
        dialogBuilder.setIcon(R.drawable.smi);
        dialogBuilder.setTitle("Select From Below");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(KotActivity.this, android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("1");
        arrayAdapter.add("2");
        arrayAdapter.add("3");
        arrayAdapter.add("4");
        arrayAdapter.add("5");
        arrayAdapter.add("10");
        arrayAdapter.add("15");
        arrayAdapter.add("20");
        arrayAdapter.add("30");


        dialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialogBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = arrayAdapter.getItem(which);

                if (Objects.equals(strName, "1")){

                    mDatabase.child("uttam").child(tableName).child(text)
                            .child("count").setValue("")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){



                                        mDatabase.child("uttam").child(tableName).child(text)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                                        String price = Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString();
                                                        String tax = Objects.requireNonNull(dataSnapshot.child("tax").getValue()).toString();

                                                        double p = Double.parseDouble(price);
                                                        double t = Double.parseDouble(tax);

                                                        double taxPrice = p*t/100;
                                                        double netAmount = p - taxPrice;

                                                        @SuppressLint("DefaultLocale")
                                                        String price2 = String.format("%.2f", p);
                                                        @SuppressLint("DefaultLocale")
                                                        String price3 = String.format("%.2f", netAmount);

                                                        HashMap<String, Object> map = new HashMap<>();
                                                        map.put("price2", price2);
                                                        map.put("price3", price3);

                                                        mDatabase.child("uttam").child(tableName).child(text)
                                                                .updateChildren(map);

                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });

                                        Toast.makeText(KotActivity.this, "added", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(KotActivity.this, "try again", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });

                }else {
                    mDatabase.child("uttam").child(tableName).child(text)
                            .child("count").setValue(strName)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){

                                     mDatabase.child("uttam").child(tableName).child(text)
                                     .addListenerForSingleValueEvent(new ValueEventListener() {
                                         @Override
                                         public void onDataChange(DataSnapshot dataSnapshot) {

                                             String price = Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString();
                                             String count = Objects.requireNonNull(dataSnapshot.child("count").getValue()).toString();
                                             String tax = Objects.requireNonNull(dataSnapshot.child("tax").getValue()).toString();

                                             double p = Double.parseDouble(price);
                                             double c = Double.parseDouble(count);
                                             double t = Double.parseDouble(tax);

                                             double tPrice = p*c;
                                             double taxPrice = tPrice*t/100;
                                             double netAmount = tPrice - taxPrice;

                                             @SuppressLint("DefaultLocale")
                                             String price2 = String.format("%.2f", tPrice);
                                             @SuppressLint("DefaultLocale")
                                             String price3 = String.format("%.2f", netAmount);

                                             HashMap<String, Object> map = new HashMap<>();
                                             map.put("price2", price2);
                                             map.put("price3", price3);

                                             mDatabase.child("uttam").child(tableName).child(text)
                                                      .updateChildren(map);

                                         }

                                         @Override
                                         public void onCancelled(DatabaseError databaseError) {

                                         }
                                     });

                                    }else {
                                        Toast.makeText(KotActivity.this, "try again", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }






            }
        });
        dialogBuilder.show();


    }
}
