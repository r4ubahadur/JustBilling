package com.smi.justbilling;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import id.zelory.compressor.Compressor;
import in.smi.ru.uttamlibrary.Uttam;

public class ItemUploadActivity extends AppCompatActivity {


    private String product_type, item_type, image="image";


    List<String> brandNames = new ArrayList<>();
    List<String> colors = new ArrayList<>();
    List<String> counts = new ArrayList<>();
    List<String> descs = new ArrayList<>();
    List<String> expiryDate = new ArrayList<>();
    List<String> names = new ArrayList<>();
    List<String> sizes = new ArrayList<>();

    Context context = this;

    private ProgressDialog mProgressDialog;
    private Uri mImageUri;
    private static final int GALLERY_PICK = 1;
    private ImageView image_view;
    private Button mButtonUpload;

    private StorageTask mUploadTask;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private EditText itemBrand, itemColor, itemCount, itemDesc, itemExpiry, itemName, itemPrice, itemSize;

    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;

    private Toolbar mToolbar;

    private ProgressBar productProgressBar, productMainProgressBar;


    @Override
    protected void onStart() {
        super.onStart();
        LinearLayout singleItemDetails= findViewById(R.id.singleItemDetails);
        singleItemDetails.setVisibility(View.GONE);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_item_upload);



        product_type = Objects.requireNonNull(super.getIntent().getExtras()).getString("catName");
        item_type = Objects.requireNonNull(super.getIntent().getExtras()).getString("subCat");



        mToolbar = findViewById(R.id.itemUploadToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(item_type);




        mProgressDialog = new ProgressDialog(this);
        image_view = findViewById(R.id.image_view);
        mButtonUpload = findViewById(R.id.uploadButton);




        itemBrand = findViewById(R.id.itemBrand);
        itemColor = findViewById(R.id.itemColor);
        itemCount= findViewById(R.id.itemCount2);
        itemDesc= findViewById(R.id.itemDesc);
        itemExpiry= findViewById(R.id.itemExpiry);
        itemName = findViewById(R.id.itemName);
        itemPrice= findViewById(R.id.itemPrice);
        itemSize= findViewById(R.id.itemSize);



        mStorageRef = FirebaseStorage.getInstance().getReference().child("products").child("head").child(product_type).child("category").child(item_type);
        mDatabaseRef = FirebaseDatabase.getInstance().getReference().child("products").child("head").child(product_type).child("category").child(item_type);


        final LinearLayout uploadDetails = findViewById(R.id.uploadDetails);

        final LinearLayout singleItemDetails= findViewById(R.id.singleItemDetails);



        productProgressBar = findViewById(R.id.productProgressBar);
        productMainProgressBar = findViewById(R.id.productMainProgressBar);
        productMainProgressBar.setVisibility(View.VISIBLE);


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("itemUploadDetails").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    singleItemDetails.setVisibility(View.GONE);
                    uploadDetails.setVisibility(View.VISIBLE);

                    productMainProgressBar.setVisibility(View.INVISIBLE);


                } else {
                    singleItemDetails.setVisibility(View.VISIBLE);
                    uploadDetails.setVisibility(View.GONE);

                    productMainProgressBar.setVisibility(View.INVISIBLE);


                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        final LinearLayout brandName= findViewById(R.id.brandName);
        final LinearLayout catDetails= findViewById(R.id.catDetails);
        final LinearLayout colorDetails= findViewById(R.id.colorDetails);
        final LinearLayout countDetails= findViewById(R.id.countDetails);
        final LinearLayout descDetails= findViewById(R.id.descDetails);
        final LinearLayout exDateDetails= findViewById(R.id.exDateDetails);
        final LinearLayout nameDetails= findViewById(R.id.nameDetails);
        final LinearLayout priceDetails= findViewById(R.id.priceDetails);
        final LinearLayout sizeDetails= findViewById(R.id.sizeDetails);

        final Spinner brandSpinner = findViewById(R.id.brandSpinner);
        final Spinner colorSpinner = findViewById(R.id.colorSpinner);
        final Spinner countSpinner = findViewById(R.id.countSpinner);
        final Spinner descSpinner = findViewById(R.id.descSpinner);
        final Spinner exSpinner = findViewById(R.id.exSpinner);
        final Spinner nameSpinner = findViewById(R.id.nameSpinner);
        final Spinner sizeSpinner = findViewById(R.id.sizeSpinner);

         itemColor = findViewById(R.id.itemColor);


        mDatabase.child("itemSection").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                colors.clear();
                for (DataSnapshot areaSnapshot: dataSnapshot.child("color").getChildren()) {
                    String colorList = areaSnapshot.getValue(String.class);
                    colors.add(colorList);
                }

                ArrayAdapter<String> colorAdapter = new ArrayAdapter<>(ItemUploadActivity.this, android.R.layout.simple_spinner_item, colors);
                colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                colorSpinner.setAdapter(colorAdapter);

                colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String color = parent.getItemAtPosition(position).toString();
                        itemColor.setText(color);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });




                brandNames.clear();
                for (DataSnapshot areaSnapshot: dataSnapshot.child("brandName").getChildren()) {
                    String brandName = areaSnapshot.getValue(String.class);
                    brandNames.add(brandName);
                }

                ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(ItemUploadActivity.this, android.R.layout.simple_spinner_item, brandNames);
                brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                brandSpinner.setAdapter(brandAdapter);









            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        mDatabase.child("itemUploadDetails").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
              //  String color = Objects.requireNonNull(dataSnapshot.child("color").getValue()).toString();










                if (dataSnapshot.child("brandName").getValue()!=null){
                    brandName.setVisibility(View.VISIBLE);
                }else {
                    brandName.setVisibility(View.GONE);
                }

                if (dataSnapshot.child("cat").getValue()!=null){
                    catDetails.setVisibility(View.VISIBLE);
                }else {
                    catDetails.setVisibility(View.GONE);
                }



                if (dataSnapshot.child("color").getValue()!=null){
                    colorDetails.setVisibility(View.VISIBLE);
                }else {
                    colorDetails.setVisibility(View.GONE);
                }


                if (dataSnapshot.child("count").getValue()!=null){
                    countDetails.setVisibility(View.VISIBLE);
                }else {
                    countDetails.setVisibility(View.GONE);
                }


                if (dataSnapshot.child("desc").getValue()!=null){
                    descDetails.setVisibility(View.VISIBLE);
                }else {
                    descDetails.setVisibility(View.GONE);
                }


                if (dataSnapshot.child("expiryDate").getValue()!=null){
                    exDateDetails.setVisibility(View.VISIBLE);
                }else {
                    exDateDetails.setVisibility(View.GONE);
                }



                if (dataSnapshot.child("name").getValue()!=null){
                    nameDetails.setVisibility(View.VISIBLE);
                }else {
                    nameDetails.setVisibility(View.GONE);
                }


                if (dataSnapshot.child("price").getValue()!=null){
                    priceDetails.setVisibility(View.VISIBLE);
                }else {
                    priceDetails.setVisibility(View.GONE);
                }


                if (dataSnapshot.child("size").getValue()!=null){
                    sizeDetails.setVisibility(View.VISIBLE);
                }else {
                    sizeDetails.setVisibility(View.GONE);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        DatabaseReference aData = FirebaseDatabase.getInstance().getReference().child("itemUploadDetails");
        aData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                CheckBox itemIdCheck = findViewById(R.id.itemIdCheck);
                CheckBox itemCatCheck = findViewById(R.id.itemCatCheck);
                CheckBox itemNameCheck = findViewById(R.id.itemNameCheck);
                CheckBox itemPriceCheck = findViewById(R.id.itemPriceCheck);
                CheckBox itemColorCheck = findViewById(R.id.itemColorCheck);
                CheckBox itemSizeCheck = findViewById(R.id.itemSizeCheck);
                CheckBox itemBrandNameCheck = findViewById(R.id.itemBrandNameCheck);
                CheckBox itemDescCheck = findViewById(R.id.itemDescCheck);
                CheckBox itemCountCheck = findViewById(R.id.itemCountCheck);
                CheckBox itemExpiryDate = findViewById(R.id.itemExpiryDate);



                if (dataSnapshot.child("id").getValue()!=null){
                    itemIdCheck.setChecked(true);
                    itemIdCheck.setTypeface(itemIdCheck.getTypeface(), Typeface.BOLD_ITALIC);
                }else{
                    itemIdCheck.setChecked(false);
                    itemIdCheck.setTypeface(null, Typeface.NORMAL);
                }


                if (dataSnapshot.child("name").getValue()!=null){
                    itemNameCheck.setChecked(true);
                    itemNameCheck.setTypeface(itemNameCheck.getTypeface(), Typeface.BOLD_ITALIC);
                }else{
                    itemNameCheck.setChecked(false);
                    itemNameCheck.setTypeface(null, Typeface.NORMAL);
                }

                if (dataSnapshot.child("cat").getValue()!=null){
                    itemCatCheck.setChecked(true);
                    itemCatCheck.setTypeface(itemCatCheck.getTypeface(), Typeface.BOLD_ITALIC);
                }else{
                    itemCatCheck.setChecked(false);
                    itemCatCheck.setTypeface(null, Typeface.NORMAL);
                }


                if (dataSnapshot.child("price").getValue()!=null){
                    itemPriceCheck.setChecked(true);
                    itemPriceCheck.setTypeface(itemPriceCheck.getTypeface(), Typeface.BOLD_ITALIC);
                }else{
                    itemPriceCheck.setChecked(false);
                    itemPriceCheck.setTypeface(null, Typeface.NORMAL);
                }
                if (dataSnapshot.child("color").getValue()!=null){
                    itemColorCheck.setChecked(true);
                    itemColorCheck.setTypeface(itemColorCheck.getTypeface(), Typeface.BOLD_ITALIC);
                }else{
                    itemColorCheck.setChecked(false);
                    itemColorCheck.setTypeface(null, Typeface.NORMAL);
                }
                if (dataSnapshot.child("size").getValue()!=null){
                    itemSizeCheck.setChecked(true);
                    itemSizeCheck.setTypeface(itemSizeCheck.getTypeface(), Typeface.BOLD_ITALIC);
                }else{
                    itemSizeCheck.setChecked(false);
                    itemSizeCheck.setTypeface(null, Typeface.NORMAL);
                }
                if (dataSnapshot.child("brandName").getValue()!=null){
                    itemBrandNameCheck.setChecked(true);
                    itemBrandNameCheck.setTypeface(itemBrandNameCheck.getTypeface(), Typeface.BOLD_ITALIC);
                }else{
                    itemBrandNameCheck.setChecked(false);
                    itemBrandNameCheck.setTypeface(null, Typeface.NORMAL);
                }
                if (dataSnapshot.child("desc").getValue()!=null){
                    itemDescCheck.setChecked(true);
                    itemDescCheck.setTypeface(itemDescCheck.getTypeface(), Typeface.BOLD_ITALIC);
                }else{
                    itemDescCheck.setChecked(false);
                    itemDescCheck.setTypeface(null, Typeface.NORMAL);
                }
                if (dataSnapshot.child("count").getValue()!=null){
                    itemCountCheck.setChecked(true);
                    itemCountCheck.setTypeface(itemCountCheck.getTypeface(), Typeface.BOLD_ITALIC);
                }else{
                    itemCountCheck.setChecked(false);
                    itemCountCheck.setTypeface(null, Typeface.NORMAL);
                }
                if (dataSnapshot.child("expiryDate").getValue()!=null){
                    itemExpiryDate.setChecked(true);
                    itemExpiryDate.setTypeface(itemExpiryDate.getTypeface(), Typeface.BOLD_ITALIC);
                }else{
                    itemExpiryDate.setChecked(false);
                    itemExpiryDate.setTypeface(null, Typeface.NORMAL);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final RelativeLayout updateBtn = findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (updateBtn.getVisibility()==View.VISIBLE){

                    singleItemDetails.setVisibility(View.GONE);
                    uploadDetails.setVisibility(View.VISIBLE);


                }

            }
        });



        ImageView brandAddItem = findViewById(R.id.brandAddItem);
        ImageView colorAddItem = findViewById(R.id.colorAddItem);
        ImageView descAddItem = findViewById(R.id.descAddItem);
        ImageView nameAddItem = findViewById(R.id.nameAddItem);
        ImageView priceAddItem = findViewById(R.id.priceAddItem);
        ImageView sizeAddItem = findViewById(R.id.sizeAddItem);
     //   ImageView sizeAddItem = findViewById(R.id.sizeAddItem);
      //  ImageView colorAddItem = findViewById(R.id.colorAddItem);






        Button addDetails = findViewById(R.id.addDetails);
        Button uploadButton = findViewById(R.id.uploadButton);


        addDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadDetails.setVisibility(View.GONE);
                singleItemDetails.setVisibility(View.VISIBLE);

            }
        });




        ImageView addImage = findViewById(R.id.addImage);



       addImage.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View v) {

               openFileChooser2();
               return true;
           }
       });

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFileChooser();

               // Toast.makeText(ItemUploadActivity.this, "jh", Toast.LENGTH_SHORT).show();

            }
        });




        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(ItemUploadActivity.this, "Uttam in progress", Toast.LENGTH_SHORT).show();
                } else {


                    uploadFile();
                }
            }
        });











        
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);



        itemExpiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });






        // Text Clear Step Start



        ImageView nameClear = findViewById(R.id.nameClear);
        ImageView priceClear = findViewById(R.id.priceClear);

        nameClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemName.setText("");

            }
        });

        priceClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                itemPrice.setText("");

            }
        });




















    }  //onCreate



    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {

                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {

        itemExpiry.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));

    }


    private void openFileChooser2() {

        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, 0);
    }

    private void openFileChooser() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, GALLERY_PICK);


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 0 && resultCode == RESULT_OK){

            mImageUri = data.getData();
            CropImage.activity(mImageUri)
                    .start(this);

        }


        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK && data != null && data.getData() != null) {

            mImageUri = data.getData();


            CropImage.activity(mImageUri)
                    .start(this);

        }



        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mImageUri = result.getUri();
                Picasso.with(this).load(mImageUri).into(image_view);

            }


        }


    }

    private void uploadFile() {
        if (mImageUri != null) {

            // mProgressDialog.setTitle("Please wait");
            mProgressDialog.setMessage("Please wait....");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." +"jpg" /* getFileExtension(mImageUri) */ );


            final StorageReference thumb_filepath = mStorageRef.child("thumbs").child(System.currentTimeMillis()
                    + "." +"jpg" /* getFileExtension(mImageUri) */ );




            // image size change

            File thumb_filePath = new File(mImageUri.getPath());

            Bitmap thumb_bitmap = null;

            try {


                thumb_bitmap = new Compressor(this)
                        .setMaxHeight(140)
                        .setMaxWidth(120)
                        .setQuality(30)
                        .compressToBitmap(thumb_filePath);


            } catch (Exception e) {

                e.printStackTrace();
            }

            ByteArrayOutputStream bios = new ByteArrayOutputStream();
            Objects.requireNonNull(thumb_bitmap).compress(Bitmap.CompressFormat.JPEG, 35, bios);
            final byte[] thumb_byte = bios.toByteArray();

            // image size change






            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {


                            if (task.isSuccessful()){


                                // Toast.makeText(ItemUploadActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
                                final Uttam upload = new Uttam(


                                        Objects.requireNonNull(task.getResult().getDownloadUrl()).toString(), "null",
                                        itemBrand.getText().toString().trim(),
                                        itemColor.getText().toString().trim(),
                                        itemCount.getText().toString().trim(),
                                        itemDesc.getText().toString().trim(),
                                        itemExpiry.getText().toString().trim(),
                                        itemName.getText().toString().trim(),
                                        itemPrice.getText().toString().trim(),
                                        itemSize.getText().toString().trim()
                                );

                                mProgressDialog.dismiss();



                                final String uploadId = mDatabaseRef.push().getKey();
                                mProgressDialog.setTitle("Please wait.....");
                                mProgressDialog.show();

                                mDatabaseRef.child(Objects.requireNonNull(uploadId)).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        if (task.isSuccessful()){
                                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);

                                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                    String thumbImageUrl = task.getResult().getDownloadUrl().toString();

                                                    if (task.isSuccessful()){

                                                        mDatabaseRef.child(uploadId)
                                                                .child("thumbImageUrl")
                                                                .setValue(thumbImageUrl)
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        if (task.isSuccessful()){

                                                                            mDatabaseRef.child(uploadId).child("id").setValue(ServerValue.TIMESTAMP);

                                                                            mProgressDialog.dismiss();

                                                                            AlertDialog.Builder alert = new AlertDialog.Builder(ItemUploadActivity.this);
                                                                            alert.setMessage("Upload successfully...");
                                                                            alert.setCancelable(false);
                                                                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                                                    dialog.dismiss();

                                                                                }
                                                                            });
                                                                            alert.show();

                                                                        }else {
                                                                            mDatabaseRef.child(uploadId).removeValue();
                                                                            mProgressDialog.dismiss();

                                                                        }

                                                                    }
                                                                });
                                                    }


                                                }
                                            });

                                        }else {

                                            mDatabaseRef.child(uploadId).removeValue();

                                        }



                                    }
                                });




                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ItemUploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        } else {



            // mProgressDialog.setTitle("Please wait");
            mProgressDialog.setMessage("Please wait....");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.show();

            // Toast.makeText(ItemUploadActivity.this, "Upload successful", Toast.LENGTH_LONG).show();
            Uttam upload = new Uttam(
                    "image","image",
                    itemBrand.getText().toString().trim(),
                    itemColor.getText().toString().trim(),
                    itemCount.getText().toString().trim(),
                    itemDesc.getText().toString().trim(),
                    itemExpiry.getText().toString().trim(),
                    itemName.getText().toString().trim(),
                    itemPrice.getText().toString().trim(),
                    itemSize.getText().toString().trim()
                                );

            mProgressDialog.dismiss();

            final String uploadId = mDatabaseRef.push().getKey();
            mProgressDialog.setTitle("Please wait.....");
            mProgressDialog.show();

            mDatabaseRef.child(Objects.requireNonNull(uploadId)).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()){
                        mDatabaseRef.child(uploadId).child("id").setValue(ServerValue.TIMESTAMP);
                        mProgressDialog.dismiss();

                        AlertDialog.Builder alert = new AlertDialog.Builder(ItemUploadActivity.this);
                        alert.setMessage("Upload successfully...");
                        alert.setCancelable(false);
                        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();

                                }
                        });
                        alert.show();
                    }
                }
            });





        }
    }








    public void onCheckBoxClicked(View view) {

        final CheckBox itemIdCheck = findViewById(R.id.itemIdCheck);
        final CheckBox itemNameCheck = findViewById(R.id.itemNameCheck);
        final CheckBox itemCatCheck = findViewById(R.id.itemCatCheck);
        final CheckBox itemPriceCheck = findViewById(R.id.itemPriceCheck);
        final CheckBox itemColorCheck = findViewById(R.id.itemColorCheck);
        final CheckBox itemSizeCheck = findViewById(R.id.itemSizeCheck);
        final CheckBox itemBrandNameCheck = findViewById(R.id.itemBrandNameCheck);
        final CheckBox itemDescCheck = findViewById(R.id.itemDescCheck);
        final CheckBox itemCountCheck = findViewById(R.id.itemCountCheck);
        final CheckBox itemExpiryDate = findViewById(R.id.itemExpiryDate);

        //Is the view (Clicked CheckBox) now checked
        boolean checked = ((CheckBox) view).isChecked();

        DatabaseReference mData = FirebaseDatabase.getInstance().getReference().child("itemUploadDetails");


        final RelativeLayout updateBtn = findViewById(R.id.updateBtn);
        switch(view.getId()){ //get the id of clicked CheckBox

            case R.id.itemNameCheck:
                if (checked){


                    productProgressBar.setVisibility(View.VISIBLE);
                    updateBtn.setVisibility(View.INVISIBLE);
                    mData.child("name").setValue("itemName").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                itemNameCheck.setTypeface(itemNameCheck.getTypeface(), Typeface.BOLD_ITALIC);
                                productProgressBar.setVisibility(View.GONE);

                                updateBtn.setVisibility(View.VISIBLE);

                            }else {
                                itemNameCheck.setTypeface(null, Typeface.NORMAL);
                                productProgressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                }
                else{

                    updateBtn.setVisibility(View.INVISIBLE);
                    productProgressBar.setVisibility(View.VISIBLE);
                    itemNameCheck.setTypeface(null, Typeface.NORMAL);
                    mData.child("name").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            updateBtn.setVisibility(View.VISIBLE);
                            productProgressBar.setVisibility(View.GONE);

                        }
                    });
                }
                break;



            case R.id.itemPriceCheck:
                if (checked){


                    productProgressBar.setVisibility(View.VISIBLE);
                    updateBtn.setVisibility(View.INVISIBLE);
                    mData.child("price").setValue("itemPrice").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                itemPriceCheck.setTypeface(itemPriceCheck.getTypeface(), Typeface.BOLD_ITALIC);
                                productProgressBar.setVisibility(View.GONE);
                                updateBtn.setVisibility(View.VISIBLE);

                            }else {
                                itemPriceCheck.setTypeface(null, Typeface.NORMAL);
                                productProgressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                }
                else{
                    updateBtn.setVisibility(View.INVISIBLE);
                    productProgressBar.setVisibility(View.VISIBLE);
                    itemPriceCheck.setTypeface(null, Typeface.NORMAL);
                    mData.child("price").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateBtn.setVisibility(View.VISIBLE);
                            productProgressBar.setVisibility(View.GONE);

                        }
                    });
                }
                break;


            case R.id.itemColorCheck:
                if (checked){


                    productProgressBar.setVisibility(View.VISIBLE);
                    updateBtn.setVisibility(View.INVISIBLE);
                    mData.child("color").setValue("itemColor").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                itemColorCheck.setTypeface(itemColorCheck.getTypeface(), Typeface.BOLD_ITALIC);
                                productProgressBar.setVisibility(View.GONE);
                                updateBtn.setVisibility(View.VISIBLE);

                            }else {
                                itemColorCheck.setTypeface(null, Typeface.NORMAL);
                                productProgressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                }
                else{
                    updateBtn.setVisibility(View.INVISIBLE);
                    productProgressBar.setVisibility(View.VISIBLE);
                    itemColorCheck.setTypeface(null, Typeface.NORMAL);
                    mData.child("color").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateBtn.setVisibility(View.VISIBLE);
                            productProgressBar.setVisibility(View.GONE);

                        }
                    });
                }
                break;


            case R.id.itemSizeCheck:
                if (checked){


                    productProgressBar.setVisibility(View.VISIBLE);
                    updateBtn.setVisibility(View.INVISIBLE);
                    mData.child("size").setValue("itemSize").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                itemSizeCheck.setTypeface(itemSizeCheck.getTypeface(), Typeface.BOLD_ITALIC);
                                productProgressBar.setVisibility(View.GONE);
                                updateBtn.setVisibility(View.VISIBLE);

                            }else {
                                itemSizeCheck.setTypeface(null, Typeface.NORMAL);
                                productProgressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                }
                else{
                    updateBtn.setVisibility(View.INVISIBLE);
                    productProgressBar.setVisibility(View.VISIBLE);
                    itemSizeCheck.setTypeface(null, Typeface.NORMAL);
                    mData.child("size").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateBtn.setVisibility(View.VISIBLE);
                            productProgressBar.setVisibility(View.GONE);

                        }
                    });
                }
                break;


            case R.id.itemBrandNameCheck:
                if (checked){


                    productProgressBar.setVisibility(View.VISIBLE);
                    updateBtn.setVisibility(View.INVISIBLE);
                    mData.child("brandName").setValue("itemBrandName").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                itemBrandNameCheck.setTypeface(itemBrandNameCheck.getTypeface(), Typeface.BOLD_ITALIC);
                                productProgressBar.setVisibility(View.GONE);
                                updateBtn.setVisibility(View.VISIBLE);

                            }else {
                                itemBrandNameCheck.setTypeface(null, Typeface.NORMAL);
                                productProgressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                }
                else{
                    updateBtn.setVisibility(View.INVISIBLE);
                    productProgressBar.setVisibility(View.VISIBLE);
                    itemBrandNameCheck.setTypeface(null, Typeface.NORMAL);
                    mData.child("brandName").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateBtn.setVisibility(View.VISIBLE);
                            productProgressBar.setVisibility(View.GONE);

                        }
                    });
                }
                break;



            case R.id.itemDescCheck:
                if (checked){


                    productProgressBar.setVisibility(View.VISIBLE);
                    updateBtn.setVisibility(View.INVISIBLE);
                    mData.child("desc").setValue("itemDesc").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                itemDescCheck.setTypeface(itemDescCheck.getTypeface(), Typeface.BOLD_ITALIC);
                                productProgressBar.setVisibility(View.GONE);
                                updateBtn.setVisibility(View.VISIBLE);

                            }else {
                                itemDescCheck.setTypeface(null, Typeface.NORMAL);
                                productProgressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                }
                else{
                    updateBtn.setVisibility(View.INVISIBLE);
                    productProgressBar.setVisibility(View.VISIBLE);
                    itemDescCheck.setTypeface(null, Typeface.NORMAL);
                    mData.child("desc").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateBtn.setVisibility(View.VISIBLE);
                            productProgressBar.setVisibility(View.VISIBLE);

                        }
                    });
                }
                break;



            case R.id.itemCountCheck:
                if (checked){


                    productProgressBar.setVisibility(View.VISIBLE);
                    updateBtn.setVisibility(View.INVISIBLE);
                    mData.child("count").setValue("itemCount").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                itemCountCheck.setTypeface(itemCountCheck.getTypeface(), Typeface.BOLD_ITALIC);
                                productProgressBar.setVisibility(View.GONE);
                                updateBtn.setVisibility(View.VISIBLE);

                            }else {
                                itemCountCheck.setTypeface(null, Typeface.NORMAL);
                                productProgressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                }
                else{
                    updateBtn.setVisibility(View.INVISIBLE);
                    itemCountCheck.setTypeface(null, Typeface.NORMAL);
                    productProgressBar.setVisibility(View.VISIBLE);
                    mData.child("count").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateBtn.setVisibility(View.VISIBLE);
                            productProgressBar.setVisibility(View.GONE);

                        }
                    });
                }
                break;


            case R.id.itemIdCheck:
                if (checked){


                    productProgressBar.setVisibility(View.VISIBLE);
                    updateBtn.setVisibility(View.INVISIBLE);
                    mData.child("id").setValue("id").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                itemIdCheck.setTypeface(itemIdCheck.getTypeface(), Typeface.BOLD_ITALIC);
                                productProgressBar.setVisibility(View.GONE);
                                updateBtn.setVisibility(View.VISIBLE);

                            }else {
                                itemIdCheck.setTypeface(null, Typeface.NORMAL);
                                productProgressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                }
                else{
                    updateBtn.setVisibility(View.INVISIBLE);
                    itemIdCheck.setTypeface(null, Typeface.NORMAL);
                    productProgressBar.setVisibility(View.VISIBLE);
                    mData.child("id").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateBtn.setVisibility(View.VISIBLE);
                            productProgressBar.setVisibility(View.GONE);

                        }
                    });
                }
                break;






            case R.id.itemCatCheck:
                if (checked){


                    productProgressBar.setVisibility(View.VISIBLE);
                    updateBtn.setVisibility(View.INVISIBLE);
                    mData.child("cat").setValue("cat").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                itemCatCheck.setTypeface(itemCatCheck.getTypeface(), Typeface.BOLD_ITALIC);
                                productProgressBar.setVisibility(View.GONE);
                                updateBtn.setVisibility(View.VISIBLE);

                            }else {
                                itemCatCheck.setTypeface(null, Typeface.NORMAL);
                                productProgressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                }
                else{
                    updateBtn.setVisibility(View.INVISIBLE);
                    itemCatCheck.setTypeface(null, Typeface.NORMAL);
                    productProgressBar.setVisibility(View.VISIBLE);
                    mData.child("cat").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateBtn.setVisibility(View.VISIBLE);
                            productProgressBar.setVisibility(View.GONE);

                        }
                    });
                }
                break;

























            case R.id.itemExpiryDate:
                if (checked){


                    productProgressBar.setVisibility(View.VISIBLE);
                    updateBtn.setVisibility(View.INVISIBLE);
                    mData.child("expiryDate").setValue("itemExpiryDate").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                itemExpiryDate.setTypeface(itemExpiryDate.getTypeface(), Typeface.BOLD_ITALIC);
                                productProgressBar.setVisibility(View.GONE);
                                updateBtn.setVisibility(View.VISIBLE);

                            }else {
                                itemExpiryDate.setTypeface(null, Typeface.NORMAL);
                                productProgressBar.setVisibility(View.INVISIBLE);
                            }

                        }
                    });
                }
                else{
                    updateBtn.setVisibility(View.INVISIBLE);
                    itemExpiryDate.setTypeface(null, Typeface.NORMAL);
                    productProgressBar.setVisibility(View.VISIBLE);
                    mData.child("expiryDate").removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            updateBtn.setVisibility(View.VISIBLE);
                            productProgressBar.setVisibility(View.GONE);

                        }
                    });
                }
                break;

        }

    }

    public void onUpdate(View view) {

       // ImageView nameAddItem = findViewById(R.id.nameAddItem);

        switch(view.getId()){

            case R.id.brandAddItem:

                Intent nameAddItem = new Intent(ItemUploadActivity.this, MasterEditActivity.class);
                nameAddItem.putExtra("addString", "brandName");
                startActivity(nameAddItem);
                break;
            case R.id.colorAddItem:

                Intent colorIntent = new Intent(ItemUploadActivity.this, MasterEditActivity.class);
                colorIntent.putExtra("addString", "color");
                startActivity(colorIntent);
                break;

            case R.id.countAddItem:

                Intent countAddItem = new Intent(ItemUploadActivity.this, MasterEditActivity.class);
                countAddItem.putExtra("addString", "count");
                startActivity(countAddItem);
                break;
            case R.id.descAddItem:

                Intent descIntent = new Intent(ItemUploadActivity.this, MasterEditActivity.class);
                descIntent.putExtra("addString", "desc");
                startActivity(descIntent);
                break;


            case R.id.exAddItem:

                Intent exAddItem = new Intent(ItemUploadActivity.this, MasterEditActivity.class);
                exAddItem.putExtra("addString", "date");
                startActivity(exAddItem);
                break;
            case R.id.nameAddItem:

                Intent nameIntent = new Intent(ItemUploadActivity.this, MasterEditActivity.class);
                nameIntent.putExtra("addString", "name");
                startActivity(nameIntent);
                break;

            case R.id.sizeAddItem:

                Intent sizeIntent = new Intent(ItemUploadActivity.this, MasterEditActivity.class);
                sizeIntent.putExtra("addString", "size");
                startActivity(sizeIntent);
                break;






        }



    }
}
