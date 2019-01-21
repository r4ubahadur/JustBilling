package com.smi.justbilling;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class PricingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar pricingToolbar;


    private Spinner spinner;
    private static final String[] paths = {"--Select--", "item 1", "item 2", "item 3"};






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing);


        pricingToolbar = findViewById(R.id.pricingToolbar);

        pricingToolbar.setTitle(Html.fromHtml("<font color='#ffffff'>Pricng Catalog</font>"));
        pricingToolbar.inflateMenu(R.menu.pricing);



        pricingToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.action_price_save) {
                 //   productEdit();
                     Toast.makeText(PricingActivity.this, "Successful",Toast.LENGTH_SHORT).show();


                }
                else{
                    // do something
                }
                return false;
            }
        });





        spinner = findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(PricingActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(PricingActivity.this);





    }

    public void onPrice(View view) {
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (position) {
            case 0:
                // Whatever you want to happen when the first item gets selected
                break;
            case 1:
                // Whatever you want to happen when the second item gets selected
                break;
            case 2:
                // Whatever you want to happen when the thrid item gets selected
                break;

        }



    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
