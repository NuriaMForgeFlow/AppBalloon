package com.example.appballoon;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class AddComment extends AppCompatActivity {
    EditText edt_addc;
    Button btn_addi, btn_saveall, btn_send, btn_menu;
    public static String d_comment="";
    private static final int PICK_IMAGE=100;
    public static Uri imageUri;
    public static Boolean image_gallery=false;
    public static Image signature = null, photo = null, photo_list = null;
    public static File file;
    Toolbar toolbar;
    ProgressBar progressBar;
    LinearLayout layout;
    Boolean saved=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);
        edt_addc=(EditText)findViewById(R.id.edt_addc);
        btn_addi=(Button)findViewById(R.id.btn_addi);
        btn_saveall=findViewById(R.id.btn_saveall);
        btn_send=findViewById(R.id.btn_send);
        btn_menu=findViewById(R.id.btn_menu);
        progressBar=findViewById(R.id.progressbar);
        layout=findViewById(R.id.layout);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Add a comment");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CalculateMOM.class));
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
        }


        btn_addi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_gallery=true;
                openGallery();
            }

        });



        btn_saveall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_saveall.setVisibility(View.GONE);
                layout.setVisibility(View.GONE);
                d_comment=edt_addc.getText().toString();
                //Name of the file
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.GERMANY);
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+1"));
                final String todayAsString = sdf.format(new Date());
                saveText(todayAsString);
                Toast.makeText(getApplicationContext(),"Report saved",Toast.LENGTH_SHORT).show();
                btn_send.setVisibility(View.VISIBLE);
            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Uri path = Uri.fromFile(file);
                sendMail(path);
            }
        });

        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!saved) {
                    WeatherActivity.mSQLiteHelperWeather.clearDatabase("WEATHER1");
                    Documentation.mSQLiteHelperDoc.clearDatabase("DOCUMENTATIONSIGN");
                    MainPassenger.mSQLiteHelper.clearDatabase("RECORD");
                }
                Signature.signature=false;
                MainPassenger.list_image=false;
                startActivity(new Intent(AddComment.this, Menu.class));


            }
        });


    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();

        }
    }



    private void saveText(String filename){

        file=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),filename+ ".pdf");//keep data in downloads
        //write to file

        try{
            Document document=new Document();
            FileOutputStream fos = new FileOutputStream(file);
            PdfWriter.getInstance(document, fos);

            //Weather
            PdfPTable table_weather = new PdfPTable(7);

            table_weather.addCell("Date and time");
            table_weather.addCell("Place");
            table_weather.addCell("Coordinates");
            table_weather.addCell("Description");
            table_weather.addCell("Temperature");
            table_weather.addCell("Wind");
            table_weather.addCell("Pressure");

            Cursor cursor = WeatherActivity.mSQLiteHelperWeather.getData("SELECT * FROM WEATHER1");
            cursor.moveToFirst();
            int count = cursor.getCount();

                for (int j = 0; j < count; j++) {

                    table_weather.addCell(cursor.getString(cursor.getColumnIndex("date")));
                    table_weather.addCell(cursor.getString(cursor.getColumnIndex("place")));
                    table_weather.addCell(cursor.getString(cursor.getColumnIndex("coordinates")));
                    table_weather.addCell(cursor.getString(cursor.getColumnIndex("description")));
                    table_weather.addCell(cursor.getString(cursor.getColumnIndex("temperature")));
                    table_weather.addCell(cursor.getString(cursor.getColumnIndex("wind")));
                    table_weather.addCell(cursor.getString(cursor.getColumnIndex("pressure")));
                    cursor.moveToNext();
                }
                WeatherActivity.mSQLiteHelperWeather.clearDatabase("WEATHER1");

            table_weather.setSpacingAfter(20);
            table_weather.setSpacingBefore(20);


            //Documentation

            PdfPTable table_documentation = new PdfPTable(6);

            table_documentation.addCell("License");
            table_documentation.addCell("CdA");
            table_documentation.addCell("CdM");
            table_documentation.addCell("Insurance expiration date");
            table_documentation.addCell("RCdA expiration date");
            table_documentation.addCell("Date of today");

            Cursor cursord = Documentation.mSQLiteHelperDoc.getData("SELECT * FROM DOCUMENTATIONSIGN");
            cursord.moveToLast();
            table_documentation.addCell(cursord.getString(cursord.getColumnIndex("license")));
            table_documentation.addCell(cursord.getString(cursord.getColumnIndex("cda")));
             table_documentation.addCell(cursord.getString(cursord.getColumnIndex("cdm")));
             table_documentation.addCell(cursord.getString(cursord.getColumnIndex("insurance")));
             table_documentation.addCell(cursord.getString(cursord.getColumnIndex("rcda")));
             table_documentation.addCell(cursord.getString(cursord.getColumnIndex("today")));

             byte[] imageByte = (byte[]) cursord.getBlob(cursord.getColumnIndex("image"));

                if (Signature.signature) {
                    try {
                        signature = Image.getInstance(imageByte);
                    } catch (BadElementException e1) {
                        e1.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        assert signature != null;
                        signature.scalePercent(15f);
                        //image.scaleAbsoluteWidth(200f);
                        //image.scaleAbsoluteHeight(200f);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                Documentation.mSQLiteHelperDoc.clearDatabase("DOCUMENTATIONSIGN");




            table_documentation.setSpacingAfter(20);
            table_documentation.setSpacingBefore(20);


            //Passengers

            PdfPTable table_passengers = new PdfPTable(4);
                table_passengers.addCell("Name");
                table_passengers.addCell("Surname");
                table_passengers.addCell("Dni");
                table_passengers.addCell("Acceptation of terms");

            if(MainPassenger.list_image) {
                Bitmap bitmapphoto_list = MediaStore.Images.Media.getBitmap(this.getContentResolver(), MainPassenger.imageUri_passenger);
                ByteArrayOutputStream bos_list = new ByteArrayOutputStream();
                bitmapphoto_list.compress(Bitmap.CompressFormat.PNG, 100, bos_list);
                byte[] bytephoto_list = bos_list.toByteArray();
                //Image photo = null;

                try {
                    photo_list = Image.getInstance(bytephoto_list);
                } catch (BadElementException e1) {
                    e1.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    assert photo_list != null;
                    photo_list.scalePercent(10f);
                    //image.scaleAbsoluteWidth(200f);
                    //image.scaleAbsoluteHeight(200f);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            else{
                Cursor cursorp = MainPassenger.mSQLiteHelper.getData("SELECT * FROM RECORD");

                cursorp.moveToFirst();
                int countp = cursorp.getCount();

                for (int j = 0; j < countp; j++) {

                    table_passengers.addCell(cursorp.getString(cursorp.getColumnIndex("name")));
                    table_passengers.addCell(cursorp.getString(cursorp.getColumnIndex("surname")));
                    table_passengers.addCell(cursorp.getString(cursorp.getColumnIndex("dni")));
                    table_passengers.addCell(cursorp.getString(cursorp.getColumnIndex("terms")));
                    cursorp.moveToNext();
                }
                MainPassenger.mSQLiteHelper.clearDatabase("RECORD");
            }





            //MTOM
            Paragraph paragraph_conditions=new Paragraph("Ambient temperature: "+InsertData.d_temperature+" ºC  "+"Altitude: "+InsertData.d_altitude+" m  "+"Maximum altitude: "+InsertData.d_maximum_altitude+" m");
            Paragraph paragraph_payload=new Paragraph("Available payload: "+InsertData.d_payload_available+" kg  "+"Used payload: "+CalculateMOM.d_payload_used+" kg  ");
            Paragraph paragraph_passengers=new Paragraph("Basket capacity: "+InsertData.d_passengers+"  Passengers (pilot included): "+CalculateMOM.d_passengers_onboard);
            Paragraph paragraph_detailedpayload=new Paragraph("Number of men: "+CalculateMOM.d_men+"  Number of women: "+CalculateMOM.d_women+"  Number of children: "+CalculateMOM.d_children+"  kg of fuel: "+CalculateMOM.d_fuel);



            //COMMENT
            Paragraph paragraph_comment=new Paragraph(d_comment);
            if(image_gallery) {
                Bitmap bitmapphoto = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmapphoto.compress(Bitmap.CompressFormat.PNG, 100, bos);
                byte[] bytephoto = bos.toByteArray();
                //Image photo = null;

                try {
                    photo = Image.getInstance(bytephoto);
                } catch (BadElementException e1) {
                    e1.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    assert photo != null;
                    photo.scalePercent(10f);
                    //image.scaleAbsoluteWidth(200f);
                    //image.scaleAbsoluteHeight(200f);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            //User information
            Paragraph paragraph_user=new Paragraph(Login.email);

            //Document creation
            document.open();
            document.setMargins(20,20,20,20);
            document.add(table_weather);
            document.add(table_documentation);
            if(Signature.signature){
                document.add(signature);}
            document.add(table_passengers);
            if(MainPassenger.list_image){
                document.add(photo_list);}
            document.add(paragraph_conditions);
            document.add(paragraph_payload);
            document.add(paragraph_passengers);
            document.add(paragraph_detailedpayload);
            if(image_gallery){
                document.add(photo);}
            document.add(paragraph_comment);
            document.add(paragraph_user);
            document.close();
            saved=true;



        }
        catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1000:
                if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(this,"Permission granted",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this,"Permission not granted",Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    private void sendMail(Uri path){
        Intent send=new Intent(Intent.ACTION_SEND);
        send.putExtra(Intent.EXTRA_EMAIL,new String[]{Login.email});
        send.putExtra(Intent.EXTRA_SUBJECT,"Balloon Report");
        send.putExtra(Intent.EXTRA_TEXT,"Report:");
        send.putExtra(Intent.EXTRA_STREAM, path);
        send.setType("message/rfc822");
        startActivity(Intent.createChooser(send,"Send email..."));

    }



}
