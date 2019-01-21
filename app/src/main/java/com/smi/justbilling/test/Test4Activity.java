package com.smi.justbilling.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.smi.justbilling.R;
import com.smi.justbilling.classes.NumberToWordsConverter;

public class Test4Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test4);


        Button ffffff = findViewById(R.id.ffffffff);
        final EditText ddddddd= findViewById(R.id.ddddddd);

        ffffff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!ddddddd.getText().toString().equals("")){

                    int n = Integer.parseInt(ddddddd.getText().toString());
                    String uuu =  NumberToWordsConverter.convertByUttam(n);
                    Toast.makeText(Test4Activity.this, uuu+" Only", Toast.LENGTH_SHORT ).show();

                }else if (ddddddd.getText().toString().equals("0")){

                    Toast.makeText(Test4Activity.this, "Zero", Toast.LENGTH_SHORT ).show();
                }




            }
        });




    }
}
