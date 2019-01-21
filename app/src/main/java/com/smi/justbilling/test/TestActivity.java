


package com.smi.justbilling.test;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smi.justbilling.R;

public class TestActivity extends AppCompatActivity {

    private EditText e1, e2;
    private Button b1, b2;

    String password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        SharedPreferences settings = getSharedPreferences("PREFS", 0);
        password = settings.getString("password", "");

        e1 = findViewById(R.id.e1);
        e2 = findViewById(R.id.e2);
        b1 = findViewById(R.id.b1);
        b2 = findViewById(R.id.b2);




        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (e1.getText().toString().equals("") || e2.getText().toString().equals("")){


                }else {

                    if (e1.getText().toString().equals(e2.getText().toString())){

                        SharedPreferences settings = getSharedPreferences("PREFS", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("password", e1.getText().toString());
                        editor.apply();


                    }


                }



            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (e1.getText().toString().equals(password)){

                    Toast.makeText(TestActivity.this, "Success", Toast.LENGTH_SHORT).show();
                }else {

                    Toast.makeText(TestActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }


            }
        });








    }
}
