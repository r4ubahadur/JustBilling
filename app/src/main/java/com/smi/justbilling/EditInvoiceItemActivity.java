package com.smi.justbilling;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class EditInvoiceItemActivity extends AppCompatActivity {

    private String tableName, key;

    private DatabaseReference mDatabase;

    private TextInputLayout edit_item_name,  edit_item_tax, edit_item_discount, edit_item_price;

    private Button save_btn ;

    private int count = 1, price5 = 0, price2 = 0;

    private ProgressDialog rProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_invoice_item);



        tableName = Objects.requireNonNull(super.getIntent().getExtras()).getString("tableName");
        key = Objects.requireNonNull(super.getIntent().getExtras()).getString("key");

        mDatabase = FirebaseDatabase.getInstance().getReference();

        edit_item_name = findViewById(R.id.edit_item_name);
        edit_item_price = findViewById(R.id.edit_item_price);
        edit_item_tax = findViewById(R.id.edit_item_tax);
        edit_item_discount = findViewById(R.id.edit_item_discount);


        save_btn = findViewById(R.id.save_btn);

        rProgressDialog = new ProgressDialog(this);





        mDatabase.child("uttam").child(tableName).child("root").child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        if (dataSnapshot.child("count").exists()){

                            if (Objects.requireNonNull(dataSnapshot.child("count").getValue()).toString().equals("")){
                                 count = 1;

                            }else {

                                String c = Objects.requireNonNull(dataSnapshot.child("count").getValue()).toString();
                                 count = Integer.parseInt(c);
                            }

                        }




                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





















        save_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                String tax = Objects.requireNonNull(edit_item_tax.getEditText()).getText().toString();
                String discount = Objects.requireNonNull(edit_item_discount.getEditText()).getText().toString();
                String price = Objects.requireNonNull(edit_item_price.getEditText()).getText().toString();

                int p = Integer.parseInt(price);

                price2 = p*count;




                rProgressDialog.setTitle("Please wait...");
                rProgressDialog.setCanceledOnTouchOutside(false);
                rProgressDialog.show();

                if (discount.equals("")){
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("price", price);
                    map.put("price2", String.valueOf(price2) );
                    map.put("tax", tax);

                    mDatabase.child("uttam").child(tableName).child("root").child(key).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                mDatabase.child("uttam").child(tableName).child("root").child(key)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                if (dataSnapshot.exists()){

                                                    String price = Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString();
                                                    String countP = Objects.requireNonNull(dataSnapshot.child("count").getValue()).toString();
                                                    String tax = Objects.requireNonNull(dataSnapshot.child("tax").getValue()).toString();


                                                    if (countP.equals("")){
                                                        int price2 = Integer.parseInt(price);
                                                        int countP2 = 1;
                                                        int three = price2*countP2;

                                                        double p1 = (double) three;
                                                        double t1 =  Double.parseDouble(tax);
                                                        double p2 =  ((p1*100)/(100+t1));

                                                        @SuppressLint("DefaultLocale")
                                                        String price3 = String.format("%.2f", p2);

                                                        mDatabase.child("uttam").child(tableName).child("root").child(key).child("price3").setValue(price3)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        if (task.isSuccessful()){

                                                                            rProgressDialog.dismiss();
                                                                            finish();

                                                                        }

                                                                    }
                                                                });

                                                    }else {

                                                        int price2 = Integer.parseInt(price);
                                                        int countP2 = Integer.parseInt(countP);
                                                        int three = price2*countP2;

                                                        double p1 = (double) three;
                                                        double t1 =  Double.parseDouble(tax);
                                                        double p2 =  ((p1*100)/(100+t1));

                                                        @SuppressLint("DefaultLocale")
                                                        String price3 = String.format("%.2f", p2);

                                                        mDatabase.child("uttam").child(tableName).child("root").child(key).child("price3").setValue(price3)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        if (task.isSuccessful()){

                                                                            rProgressDialog.dismiss();
                                                                            finish();
                                                                        }

                                                                    }
                                                                });

                                                    }


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
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("tax", tax);
                    map.put("disPrice", discount);

                    mDatabase.child("uttam").child(tableName).child("root").child(key).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){

                                mDatabase.child("uttam").child(tableName).child("root").child(key)
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {


                                                if (dataSnapshot.exists()){

                                                    String price = Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString();
                                                    String countP = Objects.requireNonNull(dataSnapshot.child("count").getValue()).toString();
                                                    String tax = Objects.requireNonNull(dataSnapshot.child("tax").getValue()).toString();


                                                    if (countP.equals("")){
                                                        int price2 = Integer.parseInt(price);
                                                        int countP2 = 1;
                                                        int three = price2*countP2;

                                                        double p1 = (double) three;
                                                        double t1 =  Double.parseDouble(tax);
                                                        double p2 =  ((p1*100)/(100+t1));

                                                        @SuppressLint("DefaultLocale")
                                                        String price3 = String.format("%.2f", p2);

                                                        mDatabase.child("uttam").child(tableName).child("root").child(key).child("price3").setValue(price3)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        if (task.isSuccessful()){

                                                                            rProgressDialog.dismiss();
                                                                            finish();

                                                                        }

                                                                    }
                                                                });

                                                    }else {

                                                        int price2 = Integer.parseInt(price);
                                                        int countP2 = Integer.parseInt(countP);
                                                        int three = price2*countP2;

                                                        double p1 = (double) three;
                                                        double t1 =  Double.parseDouble(tax);
                                                        double p2 =  ((p1*100)/(100+t1));

                                                        @SuppressLint("DefaultLocale")
                                                        String price3 = String.format("%.2f", p2);

                                                        mDatabase.child("uttam").child(tableName).child("root").child(key).child("price3").setValue(price3)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        if (task.isSuccessful()){

                                                                            rProgressDialog.dismiss();
                                                                            finish();
                                                                        }

                                                                    }
                                                                });

                                                    }


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
        });




























        mDatabase.child("uttam").child(tableName).child("root").child(key)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()){

                            String name = Objects.requireNonNull(dataSnapshot.child("name").getValue()).toString().toUpperCase();
                            String price = Objects.requireNonNull(dataSnapshot.child("price").getValue()).toString();


                            Objects.requireNonNull(edit_item_name.getEditText()).setText(name);



                            if (dataSnapshot.child("price").exists()){
                                Objects.requireNonNull(edit_item_price.getEditText()).setText(price);
                            }


                            if (dataSnapshot.child("tax").exists()){
                                String tax = Objects.requireNonNull(dataSnapshot.child("tax").getValue()).toString();
                                Objects.requireNonNull(edit_item_tax.getEditText()).setText(tax);
                            }


                            if (dataSnapshot.child("disPrice").exists()) {

                                String disPrice = Objects.requireNonNull(dataSnapshot.child("disPrice").getValue()).toString();
                                Objects.requireNonNull(edit_item_discount.getEditText()).setText(disPrice);
                            }


                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



    }
}
