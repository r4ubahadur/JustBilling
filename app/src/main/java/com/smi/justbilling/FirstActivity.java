package com.smi.justbilling;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.adapter.TableAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import in.smi.ru.uttamlibrary.Uttam;


public class FirstActivity extends AppCompatActivity implements TableAdapter.OnItemClickListener, TableAdapter.OnItemLongClickListener {

    Context context = this;

    List<String> waiterr = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private ProgressDialog mainProgressDialog;
    private List<Uttam> mUpload;
    private TableAdapter mAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private String tableNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        mRecyclerView = findViewById(R.id.main_recycler_view);

        mainProgressDialog = new ProgressDialog(this);
        mainProgressDialog.setTitle("Please wait...");
        mainProgressDialog.setCanceledOnTouchOutside(false);
        mainProgressDialog.show();



        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));

        mUpload = new ArrayList<>();

        mAdapter = new TableAdapter(FirstActivity.this, mUpload);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(FirstActivity.this);
        mAdapter.setOnItemLongClickListener(FirstActivity.this);


        mDatabase = FirebaseDatabase.getInstance().getReference();


        mDatabase.child("TotalTable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                mUpload.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Uttam upload = postSnapshot.getValue(Uttam.class);
                    Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                    mUpload.add(upload);
                }


                mainProgressDialog.dismiss();

                mAdapter.notifyDataSetChanged();




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(FirstActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });







        mDatabase.child("TotalTable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.exists()){

                    String push = mDatabase.push().getKey();

                    mDatabase.child("TotalTable").child(push).child("name").setValue("Add Table");

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        FloatingActionButton back_icon_for_home = findViewById(R.id.back_icon_for_home);
        back_icon_for_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent first = new Intent(FirstActivity.this, ManagementActivity.class);
                startActivity(first);
                overridePendingTransition(R.anim.l2r_enter, R.anim.r2exit);
                finish();

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent first = new Intent(FirstActivity.this, ManagementActivity.class);
        startActivity(first);
        overridePendingTransition(R.anim.l2r_enter, R.anim.r2exit);
        finish();
    }

    @Override
    public void onItemClick(final int position) {

        final int getItemCount = mRecyclerView.getAdapter().getItemCount();
        String push = mDatabase.push().getKey();

        if(position == getItemCount - 1){

            mDatabase.child("TotalTable").child(push).child("name").setValue(push)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(FirstActivity.this, "Table Added Successfully", Toast.LENGTH_SHORT).show();
                                mRecyclerView.smoothScrollToPosition(getItemCount);
                            }
                        }
                    });
        }else{
            final String tableName = ((TextView) mRecyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.tableName)).getText().toString();
            final String waiterName = ((TextView) mRecyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.waiterName)).getText().toString();


            tableNumber =  mRecyclerView.findViewHolderForAdapterPosition(position).toString();




            mDatabase.child("uttam")
                    .child("waiter")
                    .child(tableName)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (!dataSnapshot.exists()){

                                DatabaseReference nDatabase = FirebaseDatabase.getInstance().getReference();


                                AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                                LayoutInflater inflater = FirstActivity.this.getLayoutInflater();
                                View dialogView = inflater.inflate(R.layout.waiter_list, null);
                                dialog.setView(dialogView);


                                final ListView waiter_list = dialogView.findViewById(R.id.waiter_list);

                                final AlertDialog alertDialog = dialog.create();


                                nDatabase.child("waiter")
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                waiterr.clear();
                                                for (DataSnapshot waiterSnapshot: dataSnapshot.getChildren()) {
                                                    String waiter = waiterSnapshot.child("name").getValue(String.class);
                                                    waiterr.add(waiter);
                                                }

                                                ArrayAdapter<String> areasAdapter = new ArrayAdapter<>(FirstActivity.this, android.R.layout.simple_spinner_item, waiterr);

                                                waiter_list.setAdapter(areasAdapter);

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });



                                waiter_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        String waiterName = waiter_list.getItemAtPosition(position).toString();

                                        alertDialog.dismiss();

                                        Intent first = new Intent(FirstActivity.this, KotActivity.class);
                                        first.putExtra("TableName", tableName);
                                        first.putExtra("tableNumber", tableNumber);
                                        first.putExtra("WaiterName", waiterName);
                                        startActivity(first);
                                        overridePendingTransition(R.anim.b2exit, R.anim.t2b_enter );


                                    }
                                });


                                alertDialog.show();






                            }else {


                                Intent first = new Intent(FirstActivity.this, KotActivity.class);
                                first.putExtra("TableName", tableName);
                                first.putExtra("WaiterName", waiterName);
                                startActivity(first);
                                overridePendingTransition(R.anim.b2exit, R.anim.t2b_enter );

                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });







        }
    }



    @Override
    public void onItemLongClick(int position) {

        int getItemCount = mRecyclerView.getAdapter().getItemCount();

        Uttam clickItem = mUpload.get(position);
        final String push = clickItem.getKey();


        if(!(position == getItemCount - 1)){

            final String table = ((TextView) mRecyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.tableName)).getText().toString();

            //   Toast.makeText(FirstActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();





            AlertDialog.Builder alert = new AlertDialog.Builder(FirstActivity.this);
            alert.setMessage("Are you want to Delete this Table ?");
            alert.setTitle("Tech Guru");
            alert.setCancelable(false);

            alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    dialog.dismiss();
                }
            }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, int i) {


                    mDatabase.child("uttam")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    if (!dataSnapshot.exists()){
                                        mDatabase.child("TotalTable").child(push).removeValue();
                                        dialog.dismiss();
                                    }else {
                                        dialog.dismiss();

                                        AlertDialog.Builder alert = new AlertDialog.Builder(FirstActivity.this);
                                        alert.setMessage("If any Table is running now then you can't delete any Table yet.");
                                        alert.setTitle("Tech Guru");
                                        alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();
                                            }
                                        });

                                        alert.show();
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                }
            });
            alert.show();
        }



    }
}
