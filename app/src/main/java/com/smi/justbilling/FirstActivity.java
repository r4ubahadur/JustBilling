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
import android.widget.EditText;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


        FloatingActionButton table_menu = findViewById(R.id.table_menu);
        table_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FirstActivity.this);
                LayoutInflater inflater = FirstActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.table_menu, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog = dialogBuilder.create();











                TextView take_away = dialogView.findViewById(R.id.take_away);
                take_away.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        alertDialog.dismiss();

                        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

                        LayoutInflater inflater = FirstActivity.this.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.waiter_list, null);
                        dialog.setView(dialogView);

                        final ListView waiter_list = dialogView.findViewById(R.id.waiter_list);
                        final AlertDialog aDialog = dialog.create();


                        mDatabase.child("waiter")
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

                                aDialog.dismiss();

                                Intent first = new Intent(FirstActivity.this, KotActivity.class);

                                first.putExtra("WaiterName", waiterName);
                                first.putExtra("TableName", "TakeAway");
                                startActivity(first);
                                overridePendingTransition(R.anim.b2exit, R.anim.t2b_enter );


                            }
                        });
                        aDialog.show();


                    }
                });






















                TextView moving_table = dialogView.findViewById(R.id.moving_table);
                moving_table.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        final AlertDialog.Builder moveBuilder = new AlertDialog.Builder(FirstActivity.this);
                        LayoutInflater inflater = FirstActivity.this.getLayoutInflater();
                        final View dialogView = inflater.inflate(R.layout.table_menu_move, null);
                        moveBuilder.setView(dialogView);
                        final AlertDialog mBuilder = moveBuilder.create();
                        mBuilder.setCancelable(false);



                        TextView ok_move = dialogView.findViewById(R.id.ok_move);
                        TextView cancel_moving = dialogView.findViewById(R.id.cancel_moving);


                        cancel_moving.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                mBuilder.dismiss();

                            }
                        });

                        ok_move.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                EditText from_no = dialogView.findViewById(R.id.from_no);
                                EditText to_no = dialogView.findViewById(R.id.to_no);

                                final String from = "Table "+from_no.getText().toString();
                                final String to = "Table "+to_no.getText().toString();

                                mDatabase.child("uttam").child(from)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                mDatabase.child("uttam").child(to).setValue(dataSnapshot.getValue())
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()){

                                                                    mDatabase.child("uttam").child(from).setValue(null);
                                                                    mDatabase.child("uttam").child("waiter").child(from).setValue(null);

                                                                    mBuilder.dismiss();


                                                                }

                                                            }
                                                        });

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });



                            }
                        });

                        mBuilder.show();

                        alertDialog.dismiss();



                    }
                });




                TextView add_table = dialogView.findViewById(R.id.add_table);
                add_table.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        final int itemCount = mRecyclerView.getAdapter().getItemCount();

                        String push = mDatabase.push().getKey();
                        mDatabase.child("TotalTable").child(push).child("name").setValue(push);
                        mRecyclerView.smoothScrollToPosition(itemCount);

                    }
                });




                TextView set_table = dialogView.findViewById(R.id.set_table);
                set_table.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        mDatabase.child("uttam")
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists()){
                                            AlertDialog.Builder dBuilder = new AlertDialog.Builder(FirstActivity.this);
                                            dBuilder.setIcon(R.drawable.smi);
                                            dBuilder.setTitle("Select From Below");

                                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(FirstActivity.this, android.R.layout.select_dialog_singlechoice);
                                            arrayAdapter.add("1");
                                            arrayAdapter.add("2");
                                            arrayAdapter.add("3");
                                            arrayAdapter.add("4");
                                            arrayAdapter.add("5");
                                            arrayAdapter.add("6");
                                            arrayAdapter.add("7");
                                            arrayAdapter.add("8");
                                            arrayAdapter.add("9");
                                            arrayAdapter.add("10");
                                            arrayAdapter.add("11");
                                            arrayAdapter.add("12");
                                            arrayAdapter.add("13");
                                            arrayAdapter.add("14");
                                            arrayAdapter.add("15");
                                            arrayAdapter.add("16");
                                            arrayAdapter.add("17");
                                            arrayAdapter.add("18");
                                            arrayAdapter.add("19");
                                            arrayAdapter.add("20");
                                            arrayAdapter.add("Custom");


                                            dBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            });


                                            dBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    final String putNumber = arrayAdapter.getItem(which);

                                                    reload(putNumber);

                                                }
                                            });


                                            dBuilder.show();
                                            alertDialog.dismiss();
                                        }else {
                                            alertDialog.dismiss();

                                            AlertDialog.Builder alert = new AlertDialog.Builder(FirstActivity.this);
                                            alert.setMessage("If any table is now running, you can not set the table now.");
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


                alertDialog.show();

            }
        });



    }         //onCreate

    private void reload(final String putNumber) {


        mDatabase.child("TotalTable")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        long count = dataSnapshot.getChildrenCount();
                        Integer childNumber = (int) (long) count;

                        if (putNumber.equals("Custom")){


                        }else {
                            Integer putNo = Integer.parseInt(putNumber);

                            if (!childNumber.equals(putNo)){

                                one(childNumber, putNo);

                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void one(int childNumber, int putNo) {
        delete(childNumber, putNo);
    }

    private void delete(final int childNumber, final int putNo) {

       mDatabase.child("TotalTable").removeValue()
               .addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull Task<Void> task) {

                       two(childNumber, putNo);

                   }
               });

    }

    private void two(final int childNumber, final int putNo) {

        final int itemCount = mRecyclerView.getAdapter().getItemCount();

        mDatabase.child("TotalTable")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        long count = dataSnapshot.getChildrenCount();
                        Integer childNo = (int) (long) count;


                        if (childNo < putNo ){
                            String key = mDatabase.push().getKey();
                            mDatabase.child("TotalTable").child(key).child("name").setValue(key);
                            mRecyclerView.smoothScrollToPosition(itemCount);
                            three(childNumber, putNo);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



    }

    private void three(int childNumber, int putNo) {

        two(childNumber, putNo);

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
       // final int getItemCount = mRecyclerView.getAdapter().getItemCount();
       // String push = mDatabase.push().getKey();

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



    @Override
    public void onItemLongClick(int position) {

        Uttam clickItem = mUpload.get(position);
        final String push = clickItem.getKey();

        final String table = ((TextView) mRecyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.tableName)).getText().toString();

        //   Toast.makeText(FirstActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();

        AlertDialog.Builder alert = new AlertDialog.Builder(FirstActivity.this);
        alert.setMessage("Are you want to Delete this Table ?");
        alert.setTitle("Tech Guru");

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
                                    alert.setMessage("If any table is now running, you can not delete the table now.");
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