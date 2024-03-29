package com.smi.justbilling;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class WaiterActivity extends AppCompatActivity {

    Context context = this;
    List<String> waiterr = new ArrayList<>();

    private DatabaseReference mDatabase;

    private ProgressDialog mLoginProgress;

    private Boolean animation = true;

    private ListView waiter;

    private String anim, waiterL, key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);





        try {
            anim = Objects.requireNonNull(super.getIntent().getExtras()).getString("animation5");
            if (Objects.equals(anim, "true")){
                animation = true;
            }
        }catch (Exception e){
            animation = false;
        }

        mLoginProgress = new ProgressDialog(this);

        mLoginProgress.setTitle("Waiter");
        mLoginProgress.setMessage("Please wait ...");
        mLoginProgress.setCanceledOnTouchOutside(false);
        mLoginProgress.show();





        mDatabase = FirebaseDatabase.getInstance().getReference();

         waiter = findViewById(R.id.waiterList);


        mDatabase.child("waiter")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        waiterr.clear();
                        for (DataSnapshot waiterSnapshot: dataSnapshot.getChildren()) {
                            String waiter = waiterSnapshot.child("name").getValue(String.class);
                            waiterr.add(waiter);
                        }
                        ArrayAdapter<String> areasAdapter = new ArrayAdapter<>(WaiterActivity.this, android.R.layout.simple_spinner_item, waiterr);
                        waiter.setAdapter(areasAdapter);

                        mLoginProgress.dismiss();


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        waiter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                waiterL = (String) waiter.getItemAtPosition(i);




                mDatabase.child("waiter").orderByChild("name").equalTo(waiterL)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot d : dataSnapshot.getChildren()){

                                    final String key = d.getKey();

                                   // Toast.makeText(WaiterActivity.this, key, Toast.LENGTH_SHORT).show();


                                    AlertDialog.Builder customBuilder = new AlertDialog.Builder(WaiterActivity.this);
                                    LayoutInflater inflater = WaiterActivity.this.getLayoutInflater();
                                    View dView = inflater.inflate(R.layout.waiter_add, null);
                                    customBuilder.setView(dView);
                                    final AlertDialog cBuilder = customBuilder.create();
                                    cBuilder.setCanceledOnTouchOutside(false);



                                    final EditText enter_waiter_name = dView.findViewById(R.id.enter_waiter_name);
                                    TextView enter_waiter_btn = dView.findViewById(R.id.enter_waiter_btn);

                                    enter_waiter_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

                                            if (!enter_waiter_name.getText().toString().equals("")){

                                                String w = enter_waiter_name.getText().toString();

                                                mDatabase.child("waiter").child(key).child("name").setValue(w)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                                if (task.isSuccessful()){
                                                                    cBuilder.dismiss();

                                                                }else {
                                                                    cBuilder.dismiss();
                                                                }

                                                            }
                                                        });

                                            }


                                        }
                                    });





                                    cBuilder.show();




                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });













            }
        });




        FloatingActionButton waiter_back = findViewById(R.id.waiter_back);

        waiter_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(R.anim.l2r_enter, R.anim.r2exit );
            }
        });











        FloatingActionButton add_waiter = findViewById(R.id.add_waiter);
        add_waiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
// ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = WaiterActivity.this.getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.waiter, null);
                dialog.setView(dialogView);

                final EditText editText = dialogView.findViewById(R.id.addWaiter);
                Button waiter_btn = dialogView.findViewById(R.id.waiter_btn);

                final AlertDialog alertDialog = dialog.create();

                waiter_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String waiterName = editText.getText().toString();
                        String push = mDatabase.child("waiter").push().toString();

                        mDatabase.child("waiter")
                                .push()
                                .child("name")
                                .setValue(waiterName);

                        alertDialog.dismiss();


                    }
                });

                alertDialog.show();



            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent first = new Intent(WaiterActivity.this, ManagementActivity.class);
        startActivity(first);

        if (animation){

            overridePendingTransition(R.anim.l2r_enter, R.anim.r2exit);
            finish();
        }else {
            super.onBackPressed();
            finish();

        }

    }
}
