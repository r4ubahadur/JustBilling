package com.smi.justbilling;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.adapter.KotAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import in.smi.ru.uttamlibrary.Uttam;

public class KotPrintActivity extends AppCompatActivity implements KotAdapter.OnItemClickListener {

    private String tableName, tableNumber, waiterName, date;
    private TextView table_name, waiter_name, kot_date;

    private DatabaseReference mDatabase;

    private RecyclerView mRecyclerView;

    private List<Uttam> mUploads;
    private KotAdapter mAdapter;

    private LinearLayout kot_print;
    private Button print;

    private Bitmap bitmap;

    private String ran, ran2, ran3, fileName, random,random2,random3, filePath = "/sdcard/silicon/Data/pdf/", silicon = "/silicon/Data/pdf";

    private Context context = this;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kot_print);



        tableName = Objects.requireNonNull(super.getIntent().getExtras()).getString("tableName");

        tableNumber = Objects.requireNonNull(super.getIntent().getExtras()).getString("tableNumber");

        waiterName = Objects.requireNonNull(super.getIntent().getExtras()).getString("waiterName");
        date = Objects.requireNonNull(super.getIntent().getExtras()).getString("date");


        kot_print = findViewById(R.id.kot_print);
        print = findViewById(R.id.print);

        table_name = findViewById(R.id.table_name);
        waiter_name = findViewById(R.id.waiter_name);
        kot_date = findViewById(R.id.kot_date);

        table_name.setText(tableName);
        waiter_name.setText("WAITER :  "+waiterName);
        kot_date.setText(date);


        mDatabase = FirebaseDatabase.getInstance().getReference();



        mRecyclerView = findViewById(R.id.kotRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mUploads = new ArrayList<>();
        mAdapter = new KotAdapter(KotPrintActivity.this, mUploads);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(KotPrintActivity.this);



        mDatabase.child("uttam")
                .child(tableName)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        mUploads.clear();

                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            Uttam upload = postSnapshot.getValue(Uttam.class);
                            Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                            mUploads.add(upload);
                        }

                        mAdapter.notifyDataSetChanged();




                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                bitmap = loadBitmapFromView(kot_print, kot_print.getWidth(), kot_print.getHeight());
                createPdf();


            }
        });




    }

    private void createPdf() {


        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        //  Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        float hight = displaymetrics.heightPixels ;
        float width = displaymetrics.widthPixels/(11/5) ;

        int convertHighet = (int) hight, convertWidth = (int) width;



        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(convertWidth, convertHighet, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);

        Canvas canvas = page.getCanvas();

        Paint paint = new Paint();
        canvas.drawPaint(paint);

        bitmap = Bitmap.createScaledBitmap(bitmap, convertWidth, convertHighet, true);

        paint.setColor(Color.BLUE);
        canvas.drawBitmap(bitmap, 0, 0 , null);
        document.finishPage(page);



        Random r = new Random();
        Random u = new Random();
        Random b = new Random();

        int randomNumber = r.nextInt(9);
        int randomNumber2 = u.nextInt(9);
        int randomNumber3 = b.nextInt(9);

        random = String.valueOf(randomNumber);
        random2 = String.valueOf(randomNumber2);
        random3 = String.valueOf(randomNumber3);





        char[] chars1 = getString(R.string.a2z).toCharArray();
        StringBuilder sb1 = new StringBuilder();
        Random randm1 = new Random();

        for (int i = 0; i < 2; i++) {
            char c1 = chars1[randm1.nextInt(chars1.length)];
            sb1.append(c1);
        }

        ran = sb1.toString();

        char[] chars2 = getString(R.string.a2z).toCharArray();
        StringBuilder sb2 = new StringBuilder();
        Random randm2 = new Random();

        for (int i = 0; i < 2; i++) {
            char c2 = chars2[randm2.nextInt(chars2.length)];
            sb2.append(c2);
        }

        ran2 = sb2.toString();

        char[] chars3 = getString(R.string.a2z).toCharArray();
        StringBuilder sb3 = new StringBuilder();
        Random randm3 = new Random();

        for (int i = 0; i < 2; i++) {
            char c3 = chars1[randm3.nextInt(chars3.length)];
            sb3.append(c3);
        }
        ran3 = sb3.toString();



        fileName = date+"_"+ran+random+ran2+random2+ran3+random3;





        File f2 = new File(Environment.getExternalStorageDirectory().toString()+silicon);
        f2.mkdirs();






        // write the document content
        String targetPdf = filePath + fileName +".pdf";
        File filePath;
        filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show();
        }

        // close the document
        document.close();
        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();

        openGeneratedPDF();

    }

    private void openGeneratedPDF() throws ActivityNotFoundException {
        File file = new File(filePath + fileName +".pdf");
        if (file.exists()) {


            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            Uri uri = Uri.fromFile(file);
            sharingIntent.setDataAndType(uri, "application/pdf");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
            sharingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            startActivity(Intent.createChooser(sharingIntent, "Share image using"));



        }
    }


    public static Bitmap loadBitmapFromView(View v, int width, int height) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);

        return b;
    }














    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onItemClickKot(int position) {

    }
}
