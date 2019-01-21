package com.smi.justbilling;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.custom.AutoCompleteDropDown;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {

    List<String> areas = new ArrayList<>();

    private String add_new = "--Add New--";

    private DatabaseReference mDatabase;
    private AutoCompleteDropDown itemCat2;
    private AutoCompleteDropDown itemUnit;


    String[] arr = { "Paries,France", "PA,United States","Parana,Brazil",
            "Padua,Italy", "Pasadena,CA,United States"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);


        mDatabase = FirebaseDatabase.getInstance().getReference();

         itemCat2 = findViewById(R.id.itemCat2);

        itemUnit = findViewById(R.id.itemUnit);













        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_item, arr);

        itemUnit.setThreshold(2);
        itemUnit.setAdapter(adapter);












         uttam();





        mDatabase.child("category").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                areas.clear();

                areas.add(add_new);

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.child("cat").getValue(String.class);


                    areas.add(areaName);
                }



                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddItemActivity.this, android.R.layout.simple_spinner_dropdown_item, areas);
                itemCat2.setAdapter(adapter);



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    private void uttam() {


        new Handler().postDelayed(new Runnable() {
            public void run() {

                if (itemCat2.getEditableText().toString().equals(add_new)){

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddItemActivity.this);

                    LayoutInflater inflater = AddItemActivity.this.getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.add_cat_layout, null);
                    dialogBuilder.setView(dialogView);

                    final EditText addCat = dialogView.findViewById(R.id.addCat);
                    //addCat.setHint("Add New Category");
                    final AlertDialog alertDialog = dialogBuilder.create();

                    Button catBtn = dialogView.findViewById(R.id.catBtn);


                    catBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            String catName = addCat.getEditableText().toString();

                            if (!catName.isEmpty()){

                              //  String push = mDatabase

                               mDatabase.child("category")
                                       .child(catName)
                                       .child("cat")
                                       .setValue(catName)
                                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {

                                               if (task.isSuccessful()){

                                                   alertDialog.dismiss();

                                               }

                                           }
                                       });

                            }




                        }
                    });





                    alertDialog.show();

                    itemCat2.getEditableText().clear();

                    ruma();

                }else {

                    ruma();
                }


            }
        }, 1);


    }




    private void ruma() {


        new Handler().postDelayed(new Runnable() {
            public void run() {
                // do something...

                uttam();


            }
        }, 1);


    }







}
