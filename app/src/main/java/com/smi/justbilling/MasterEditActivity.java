package com.smi.justbilling;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class MasterEditActivity extends AppCompatActivity {

    Context context = this;
    List<String> areas = new ArrayList<>();
    ProgressBar mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master_edit);



        mProgressDialog = findViewById(R.id.progressDialog);

        final ListView masterUpdateListView = findViewById(R.id.masterUpdateListView);
        final EditText addItemNew = findViewById(R.id.addItemNew);





        final String categ = Objects.requireNonNull(getIntent().getExtras()).getString("addString");
       // Toast.makeText(MasterEditActivity.this, colorDetails, Toast.LENGTH_SHORT).show();

        String categ2 = Objects.requireNonNull(categ).toUpperCase();
        String categ3 = categ2+" LIST";

        TextView listDetails = findViewById(R.id.listDetails);
        listDetails.setText(categ3);


        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("itemSection");
        mDatabase.child(Objects.requireNonNull(categ)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {





                areas.clear();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    String areaName = areaSnapshot.getValue(String.class);

                    areas.add(areaName);
                }

                final ArrayAdapter<String> areasAdapter = new ArrayAdapter<>(MasterEditActivity.this, android.R.layout.simple_spinner_item, areas);


                masterUpdateListView.setAdapter(areasAdapter);
                mProgressDialog.setVisibility(View.INVISIBLE);


                Button zToA = findViewById(R.id.zToA);
                zToA.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        areasAdapter.sort(new Comparator<String>() {
                            @Override
                            public int compare(String arg1, String arg0) {
                                return arg0.compareTo(arg1);
                            }
                        });

                    }
                });


                Button aToZ = findViewById(R.id.aToZ);
                aToZ.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        areasAdapter.sort(new Comparator<String>() {
                            @Override
                            public int compare(String arg1, String arg0) {
                                return arg1.compareTo(arg0);
                            }
                        });

                    }
                });





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mProgressDialog.setVisibility(View.INVISIBLE);

            }
        });









        masterUpdateListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = masterUpdateListView.getItemAtPosition(i).toString();
                Toast.makeText(MasterEditActivity.this, "Long Click for Delete Item", Toast.LENGTH_SHORT).show();
                addItemNew.setText(item);
            }
        });

        masterUpdateListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final String item = masterUpdateListView.getItemAtPosition(position).toString();

                AlertDialog.Builder alert = new AlertDialog.Builder(context, R.style.DialogSlideAnim);

                //  final AlertDialog.Builder alert = new AlertDialog.Builder(ItemDetailsActivity.this);
                alert.setMessage("Are you sure Delete "+" "+"\""+item+"\""+" ?");
                alert.setTitle("Delete Item !");
                //alert.setCustomTitle(view.)
                alert.setCancelable(false);

                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, int whichButton) {

                        mDatabase.child(categ).child(item).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    dialog.dismiss();
                                }

                            }
                        });

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                ;

                alert.show();

                return true;
            }
        });





        Button addItem2 = findViewById(R.id.addItem2);
        addItem2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String rp = addItemNew.getText().toString();
                String result = rp.replaceAll("\\s+$", "");
                String ub = result.toUpperCase();

                mDatabase.child(categ).child(ub).setValue(ub);


                }


        });













    }


}
