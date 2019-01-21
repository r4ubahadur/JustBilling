package com.smi.justbilling;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AppsettingsActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appsettings);


        mDatabaseRef = FirebaseDatabase.getInstance().getReference();


        LinearLayout shareBtn = findViewById(R.id.shareBtn);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mDatabaseRef.child("share").child("appShare").child("shareLink")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                if (dataSnapshot.exists()){

                                    String shareText = Objects.requireNonNull(dataSnapshot.getValue()).toString();

                                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                    shareIntent.setType("text/plain");
                                    shareIntent.putExtra(Intent.EXTRA_TEXT , shareText);
                                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "The title");
                                    startActivity(Intent.createChooser(shareIntent, "Dream 11 Pro"));

                                }else {

                                    Toast.makeText(AppsettingsActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




            }
        });




        LinearLayout updateCheck = findViewById(R.id.updateCheck);


        updateCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mDatabaseRef.child("share").child("appShare").child("checkUpdate")
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String shareText = Objects.requireNonNull(dataSnapshot.getValue()).toString();

                                if (shareText.equals("uttam100")){


                                    Toast.makeText(AppsettingsActivity.this, "This is Latest Version", Toast.LENGTH_SHORT).show();


                                }else {




                                    mDatabaseRef.child("share").child("appShare").child("update")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {

                                                    if (dataSnapshot.exists()){

                                                        String shareText = Objects.requireNonNull(dataSnapshot.getValue()).toString();

                                                        try {

                                                            Uri uri = Uri.parse(shareText);
                                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                            startActivity(intent);

                                                        }catch (Exception e){

                                                            Uri uri = Uri.parse("http://www.google.com");
                                                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                            startActivity(intent);
                                                        }



                                                    }else {

                                                        Toast.makeText(AppsettingsActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
                                                    }

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







            }
        });












    }

    @Override
    public void onBackPressed() {
        Intent mnp = new Intent(AppsettingsActivity.this, MainActivity.class);
        startActivity(mnp);
        overridePendingTransition(R.anim.l2r_enter, R.anim.r2exit);

        super.onBackPressed();
    }
}
