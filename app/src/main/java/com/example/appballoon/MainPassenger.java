package com.example.appballoon;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.appballoon.DBHelper.SQLiteHelper;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.List;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class MainPassenger extends AppCompatActivity {

    Button mBtnList, mBtnAddM, mBtnPhoto, mBtnSave;
    public static SQLiteHelper mSQLiteHelper;
    private static final int PICK_IMAGE = 100;
    public static Uri imageUri_passenger;
    public static Boolean list_image = false;
    NumberPicker np;
    public static int number_passengers = 0;
    int id;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_passenger);
        mBtnList = findViewById(R.id.btnList);
        mBtnAddM = findViewById(R.id.btnAddM);
        mBtnPhoto = findViewById(R.id.btn_image_pass);
        mBtnSave = findViewById(R.id.btnsave);
        np = findViewById(R.id.np);
        np.setMinValue(0);
        np.setMaxValue(15);

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                number_passengers = newVal;
            }
        });
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Documentation");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Documentation.class));
            }
        });

        mSQLiteHelper = new SQLiteHelper(this, "RECORDDB.sqlite", null, 1);
        mSQLiteHelper.queryData("CREATE TABLE IF NOT EXISTS RECORD(id INTEGER PRIMARY KEY, name VARCHAR, surname VARCHAR, dni VARCHAR, terms VARCHAR)");


        mBtnAddM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPassenger.this, AddPassenger.class));
            }
        });

        mBtnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPassenger.this, PassengersList.class));

            }
        });

        mBtnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_image=true;
                openGallery();
            }

        });


        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = 0;
                Cursor cursor = mSQLiteHelper.getData("SELECT * FROM RECORD");
                while (cursor.moveToNext()) {
                    id++;
                }
                if (id == number_passengers || list_image) {
                    startActivity(new Intent(MainPassenger.this, InsertData.class));
                } else {
                    AlertDialog.Builder alert_passengers = new AlertDialog.Builder(MainPassenger.this);
                    alert_passengers.setTitle("Warning");
                    alert_passengers.setMessage("The number of passengers does not correspond to the passengers added. ");
                    alert_passengers.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert_passengers.show();
                }

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
            imageUri_passenger = data.getData();

        }
    }



}


