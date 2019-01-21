package com.smi.justbilling;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.test.TestActivity;

import java.util.Objects;

public class ProductsActivity extends AppCompatActivity {



    String mainCategory, subCategory;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);






        mainCategory = Objects.requireNonNull(super.getIntent().getExtras()).getString("catName");
        subCategory = Objects.requireNonNull(super.getIntent().getExtras()).getString("subCat");




        LinearLayout addItem = findViewById(R.id.addItem);
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ProductsActivity.this, ItemUploadActivity.class);
                intent.putExtra("catName", mainCategory);
                intent.putExtra("subCat", subCategory);
                startActivity(intent);
                overridePendingTransition(R.anim.r2l_enter, R.anim.l2exit);
            }
        });















    }

}
