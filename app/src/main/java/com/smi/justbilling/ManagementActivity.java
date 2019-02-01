package com.smi.justbilling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ManagementActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {



    private boolean doubleBackToExitPressedOnce = false;

    private Button fab, v_bill, category_a, waiter_a;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_management);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent first = new Intent(ManagementActivity.this, FirstActivity.class);
                startActivity(first);
                overridePendingTransition(R.anim.r2l_enter, R.anim.l2exit);
            }
        });


        v_bill = findViewById(R.id.v_bill);
        v_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent first2 = new Intent(ManagementActivity.this, ViewBillActivity.class);
                startActivity(first2);
                overridePendingTransition( R.anim.b2exit, R.anim.t2b_enter);
            }
        });

        category_a = findViewById(R.id.category_a);
        category_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cat = new Intent(ManagementActivity.this, CategoryActivity.class);
                cat.putExtra("animation", "true");
                startActivity(cat);
                overridePendingTransition( R.anim.b2exit, R.anim.t2b_enter);
            }
        });


        waiter_a = findViewById(R.id.waiter_a);
        waiter_a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cat = new Intent(ManagementActivity.this, WaiterActivity.class);
                startActivity(cat);
                overridePendingTransition( R.anim.r2l_enter, R.anim.l2exit);

            }
        });







        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


      //  reload();

    }

    private void reload() {

        uttam();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                reload1();
            }
        }, 100);


    }

    private void reload1() {



        new Handler().postDelayed(new Runnable() {
            public void run() {
                reload();
            }
        }, 100);
    }

    private void uttam() {
        final DatabaseReference mData = FirebaseDatabase.getInstance().getReference();
        mData.child("ruttam").child("rum").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });

        mData.child("ruttam").child("rum").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    String timestamp = Objects.requireNonNull(dataSnapshot.getValue()).toString();   // use ms NOT s
                    SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss" );   // Ex. "HH:mm:ss \n dd/MM/yyyy"
                    String timeString = time.format(new Date(Long.parseLong(timestamp)));

                    SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy" );
                    String dateString = date.format(new Date(Long.parseLong(timestamp)));


                    TextView timeClock = findViewById(R.id.timeClock);
                    TextView timeClock2 = findViewById(R.id.timeClock2);

                    timeClock.setText(timeString);
                    timeClock2.setText(dateString);

                  //  Toast.makeText(ManagementActivity.this, dateString, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {


            if (doubleBackToExitPressedOnce) {
                finishAffinity();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 1200);





        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.managment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.add_item) {
            // Handle the camera action

            Intent add = new Intent(ManagementActivity.this, CategoryActivity.class);
            add.putExtra("animation2", "true");
            startActivity(add);
            overridePendingTransition(R.anim.r2l_enter, R.anim.l2exit);

        } else if (id == R.id.viewBill) {

            Intent bill = new Intent(ManagementActivity.this, ViewBillActivity.class);
            bill.putExtra("animation3", "true");
            startActivity(bill);
            overridePendingTransition(R.anim.r2l_enter, R.anim.l2exit);


        }else if (id == R.id.nav_category) {

            Intent waiter = new Intent(ManagementActivity.this, CategoryActivity.class);
            waiter.putExtra("animation4", "true");
            startActivity(waiter);
            overridePendingTransition(R.anim.r2l_enter, R.anim.l2exit);

        } else if (id == R.id.nav_waiter) {

            Intent waiter = new Intent(ManagementActivity.this, WaiterActivity.class);
            waiter.putExtra("animation5", "true");
            startActivity(waiter);
            overridePendingTransition(R.anim.r2l_enter, R.anim.l2exit);

        } else if (id == R.id.nav_report) {

            Intent waiter = new Intent(ManagementActivity.this, ReportActivity.class);
            waiter.putExtra("animation6", "true");
            startActivity(waiter);
            overridePendingTransition(R.anim.r2l_enter, R.anim.l2exit);

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
