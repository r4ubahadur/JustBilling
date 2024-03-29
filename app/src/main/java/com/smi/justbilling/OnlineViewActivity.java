package com.smi.justbilling;

import android.annotation.SuppressLint;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smi.justbilling.adapter.BillSingleItemAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import in.smi.ru.uttamlibrary.Uttam;

public class OnlineViewActivity extends AppCompatActivity implements BillSingleItemAdapter.OnItemClickListener {


    private List<Uttam> mUploads;
    private BillSingleItemAdapter mAdapter;
    private DatabaseReference mDatabase;

    private String date, billNo;

    private Bitmap bitmap;

    private ScrollView scrollView2;
    private Button print_btn, next_bill, back_btn;

    private String ran, ran2, ran3, fileName, random,random2,random3, filePath = "/sdcard/silicon/Data/pdf/", silicon = "/silicon/Data/pdf";

    private TextView bill_number, billTotalAmount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_view);


        date = Objects.requireNonNull(super.getIntent().getExtras()).getString("date");
        billNo = Objects.requireNonNull(super.getIntent().getExtras()).getString("billNo");

        bill_number = findViewById(R.id.bill_number);
        billTotalAmount = findViewById(R.id.billTotalAmount);


        back_btn = findViewById(R.id.back_btn);

        scrollView2 = findViewById(R.id.scrollView2);



        mDatabase = FirebaseDatabase.getInstance().getReference();
        RecyclerView onlineBill = findViewById(R.id.onlineBill);

        mUploads = new ArrayList<>();
        mAdapter = new BillSingleItemAdapter(OnlineViewActivity.this, mUploads);

        onlineBill.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(OnlineViewActivity.this);
        onlineBill.setLayoutManager(new LinearLayoutManager(this));


        mDatabase.child("finalbill")
                .child("smi")
                .child("date")
                .child(date)
                .child(billNo)
                .child("ItemList")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            mUploads.clear();

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Uttam upload = postSnapshot.getValue(Uttam.class);
                                Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                                mUploads.add(upload);
                            }

                            mAdapter.notifyDataSetChanged();
                            bill_number.setText("Bill No:- " + billNo);


                            double sum = 0;
                            String total = "0.00";
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                Map<String, Object> map = (Map<String, Object>) ds.getValue();
                                Object price = map.get("price2");
                                double value = Double.parseDouble(String.valueOf(price));
                                sum = +sum + value;
                                total = String.format("%.2f", sum);
                            }
                            billTotalAmount.setText("₹ " + String.valueOf(total));


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        TextView bill_date = findViewById(R.id.bill_date);

        Date today = Calendar.getInstance().getTime();//getting date
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String date = formatter.format(today);
        bill_date.setText("Date:- " + date);


        Button nextBilling = findViewById(R.id.nextBilling);
        nextBilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent rp = new Intent(OnlineViewActivity.this, FirstActivity.class);
                startActivity(rp);
                finish();

            }
        });


        // Print Button

        print_btn = findViewById(R.id.print_btn);

        print_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(OnlineViewActivity.this, "success", Toast.LENGTH_SHORT).show();

                bitmap = loadBitmapFromView(scrollView2, scrollView2.getWidth(), scrollView2.getHeight());
                createPdf();


            }
        });


        next_bill = findViewById(R.id.next_bill);

        next_bill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String bill_no = billNo.replaceAll("SMI-18-19-", "");

                int bill = Integer.parseInt(bill_no);
                int bill_plus = bill + 1;

                String b = String.valueOf(bill_plus);
                billNo = "SMI-18-19-"+b;

                ru();


            }
        });

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition( R.anim.l2r_enter,  R.anim.r2exit);
            }
        });


    }

    private void ru() {


        mDatabase.child("finalbill")
                .child("smi")
                .child("date")
                .child(date)
                .child(billNo)
                .child("ItemList")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {

                            mUploads.clear();

                            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                Uttam upload = postSnapshot.getValue(Uttam.class);
                                Objects.requireNonNull(upload).setKey(postSnapshot.getKey());
                                mUploads.add(upload);
                            }

                            mAdapter.notifyDataSetChanged();
                            bill_number.setText("Bill No:- " + billNo);


                            int sum = 0;
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                                Map<String, Object> map = (Map<String, Object>) ds.getValue();
                                Object price = map.get("price2");
                                int value = Integer.parseInt(String.valueOf(price));
                                sum = +sum + value;
                            }
                            billTotalAmount.setText("₹ " + String.valueOf(sum)+".00");


                        }else {

                            Intent goToDate = new Intent(OnlineViewActivity.this, ViewBillActivity.class);
                            startActivity(goToDate);
                            finish();


                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

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



            }else {
                Toast.makeText(OnlineViewActivity.this,"Errorrrrrrrrr" , Toast.LENGTH_SHORT).show();
            }
        }


        public static Bitmap loadBitmapFromView(View v, int width, int height) {
            Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.draw(c);

            return b;
        }









    @Override
    public void onItemClick(int position) {

    }
}
