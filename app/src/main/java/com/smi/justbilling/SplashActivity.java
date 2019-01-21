package com.smi.justbilling;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        uttam();

    }

    private void uttam() {

        final DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child("test");
        final DatabaseReference nData = FirebaseDatabase.getInstance().getReference().child("test");

        mData.child("time").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                nData.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String time = Objects.requireNonNull(dataSnapshot.child("time").getValue()).toString();

                        Long aLong = Long.parseLong(time);
                        Date myDate = new Date(aLong);

                        TextView timeView = findViewById(R.id.timeView);
                        timeView.setText(myDate.toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        bah();
    }

    private void bah() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                uttam();
            }
        }, 1000);

    }
}



