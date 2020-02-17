package com.example.appballoon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;




public class AddPassenger extends AppCompatActivity {

    EditText mEdtName,mEdtSurname,mEdtDni;
    Button mBtnAdd, mBtnList, mBtnPhoto;
    CheckBox btn_accept;
    String str_terms;
    Toolbar toolbar;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpassenger);

        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Add Passengers");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainPassenger.class));
            }
        });
        mEdtName=findViewById(R.id.edtName);
        mEdtSurname=findViewById(R.id.edtSurname);
        mEdtDni=findViewById(R.id.edtDni);
        btn_accept=findViewById(R.id.btn_accept);
        mBtnAdd=findViewById(R.id.btnAdd);

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked= btn_accept.isChecked();
                if(checked)
                {
                    str_terms = "Yes";

                }
                else{

                    str_terms="No";

                }
            }
        });




        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEdtName.getText().toString().isEmpty() || mEdtSurname.getText().toString().isEmpty() || mEdtDni.getText().toString().isEmpty() ){
                    AlertDialog.Builder alert_empty= new AlertDialog.Builder(AddPassenger.this);
                    alert_empty.setTitle("Warning");
                    alert_empty.setMessage("There are empty fields");
                    alert_empty.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert_empty.show();
                }
                else {
                    try {
                        MainPassenger.mSQLiteHelper.insertData(
                                mEdtName.getText().toString().trim(),
                                mEdtSurname.getText().toString().trim(),
                                mEdtDni.getText().toString().trim(),
                                str_terms.trim()
                        );
                        Toast.makeText(AddPassenger.this, "Added successfully", Toast.LENGTH_SHORT).show();
                        mEdtName.getText().clear();
                        mEdtSurname.getText().clear();
                        mEdtDni.getText().clear();
                        btn_accept.setChecked(false);
                        hideKeyboard();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });



    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        }

    }



}

