package com.smi.justbilling;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout main_invoice, main_order, main_purchase;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        mDatabaseRef = FirebaseDatabase.getInstance().getReference();

        final DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        final TextView total_suppliers = findViewById(R.id.total_suppliers);
        final TextView total_customers = findViewById(R.id.total_customers);
        final TextView total_products = findViewById(R.id.total_products);


        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(300);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                float fractionAnim = (float) valueAnimator.getAnimatedValue();

                total_suppliers.setTextColor(ColorUtils.blendARGB(Color.parseColor("#000000")
                        , Color.parseColor("#cc7902"), fractionAnim));
                total_customers.setTextColor(ColorUtils.blendARGB(Color.parseColor("#000000")
                        , Color.parseColor("#c2c200"), fractionAnim));
                total_products.setTextColor(ColorUtils.blendARGB(Color.parseColor("#000000")
                        , Color.parseColor("#1da36b"), fractionAnim));
            }
        });
        valueAnimator.start();
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);




        main_invoice = findViewById(R.id.main_invoice);
        main_order = findViewById(R.id.main_order);
        main_purchase = findViewById(R.id.main_purchase);


        main_invoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent invoice = new Intent(MainActivity.this, InvoiceActivity.class);
                startActivity(invoice);
                overridePendingTransition(R.anim.b2t_enter, R.anim.t2exit);


            }
        });

        main_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invoice = new Intent(MainActivity.this, OrderActivity.class);
                startActivity(invoice);
            }
        });

        main_purchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent invoice = new Intent(MainActivity.this, PurchaseActivity.class);
                startActivity(invoice);
            }
        });




        LinearLayout main_product = findViewById(R.id.main_product);
        main_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent product = new Intent(MainActivity.this, ProductsActivity.class);
                startActivity(product);
                drawer.closeDrawer(GravityCompat.START);

            }
        });



        LinearLayout editProduct = findViewById(R.id.editProduct);
        editProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mno = new Intent(MainActivity.this, ProductActivity.class);
                startActivity(mno);


            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

            Intent mnp = new Intent(MainActivity.this, AppsettingsActivity.class);
            startActivity(mnp);
            overridePendingTransition(R.anim.r2l_enter, R.anim.l2exit);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
      //  int id = item.getItemId();



        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }










    @Override
    protected void onStart() {

        mDatabaseRef.child("share").child("appShare").child("checkUpdate")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String shareText = Objects.requireNonNull(dataSnapshot.getValue()).toString();

                        if (!shareText.equals("uttam003")){



                            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                            alert.setMessage("Your App is Older Version. Please Update Now.");
                            //  alert.setTitle("Create New Category");
                            alert.setCancelable(false);

                            alert.setPositiveButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                    dialog.dismiss();

                                }
                            }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(final DialogInterface dialog, int i) {

                                    dialog.dismiss();

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

                                                        Toast.makeText(MainActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
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

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });




        super.onStart();
    }









}
