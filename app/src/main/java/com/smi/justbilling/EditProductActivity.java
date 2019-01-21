package com.smi.justbilling;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;

public class EditProductActivity extends AppCompatActivity {

    private Toolbar editProductToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);


        editProductToolbar = findViewById(R.id.editProductToolbar);

        editProductToolbar.setTitle(Html.fromHtml("<font color='#ffffff'>Product</font>"));
        editProductToolbar.inflateMenu(R.menu.edit_product);



        editProductToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.action_pricing) {
                    pricingEdit();
                    //    Toast.makeText(ProductActivity.this, "This Function on hold by ADMIN",Toast.LENGTH_SHORT).show();


                }
                else{
                    // do something
                }
                return false;
            }
        });

    }

    private void pricingEdit() {

        Intent price = new Intent(EditProductActivity.this, PricingActivity.class);
        startActivity(price);


    }
}
