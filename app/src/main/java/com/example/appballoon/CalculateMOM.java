package com.example.appballoon;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import java.io.IOException;
import java.io.OutputStreamWriter;

public class CalculateMOM extends AppCompatActivity {
    EditText edt_men,edt_women,edt_fuel,edt_children;
    Button btn_calculate;
    Toolbar toolbar;
    public static String d_payload_used,d_passengers_onboard,d_men="0",d_women="0",d_children="0",d_fuel="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_mom);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Calculate MOM");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),InsertData.class));
            }
        });
        Intent intent = getIntent();
        final int passengers=intent.getIntExtra("passengers",0);
        final Double payload=intent.getDoubleExtra("payload",0.00);
        //FUEL CYLINDERS ARE NOT TAKEN INTO ACCOUNT
        edt_children=(EditText)findViewById(R.id.edt_children);
        edt_children.setInputType(InputType.TYPE_CLASS_NUMBER);
        edt_men=(EditText)findViewById(R.id.edt_men);
        edt_men.setInputType(InputType.TYPE_CLASS_NUMBER);
        edt_women=(EditText)findViewById(R.id.edt_women);
        edt_women.setInputType(InputType.TYPE_CLASS_NUMBER);
        edt_fuel=(EditText)findViewById(R.id.edt_fuel);
        edt_fuel.setInputType(InputType.TYPE_CLASS_NUMBER);
        btn_calculate=(Button) findViewById(R.id.btn_calculate);

        btn_calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int men=0;
                if (edt_men.getText() != null && !edt_men.getText().toString().equals("")){
                    d_men=edt_men.getText().toString();
                    men = Integer.parseInt(d_men);}
                int women=0;
                if (edt_women.getText() != null && !edt_women.getText().toString().equals("")){
                    d_women=edt_women.getText().toString();
                    women = Integer.parseInt(d_women);}
                int children=0;
                if (edt_children.getText() != null && !edt_children.getText().toString().equals("")){
                    d_children=edt_children.getText().toString();
                    children = Integer.parseInt(d_children);}
                double fuel=0.00;
                if (edt_fuel.getText() != null && !edt_fuel.getText().toString().equals("")){
                    d_fuel=edt_fuel.getText().toString();
                    fuel = Double.parseDouble(d_fuel);}

                int sum_passengers=men+women+children;
                Double sum=men*80+women*60+children*35+fuel;
                if(sum_passengers<=passengers){
                    if(sum<=payload){
                        if(sum_passengers==(MainPassenger.number_passengers+1) || MainPassenger.list_image){
                        d_payload_used=Double.toString(sum);
                        d_passengers_onboard=Integer.toString(sum_passengers);

                        Toast.makeText(getApplicationContext(),"Correct",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CalculateMOM.this, AddComment.class));
                    }
                    else    {
                            AlertDialog.Builder alert_listpassengers= new AlertDialog.Builder(CalculateMOM.this);
                            alert_listpassengers.setTitle("Warning");
                            alert_listpassengers.setMessage("The number of passengers does not correspond to the passengers added in the previous phase. "+"Number of passengers: "+MainPassenger.number_passengers);
                            alert_listpassengers.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            alert_listpassengers.show();
                        }
                    }
                    else{
                        AlertDialog.Builder alert_payload= new AlertDialog.Builder(CalculateMOM.this);
                        alert_payload.setTitle("Warning");
                        alert_payload.setMessage("The passengers and the fuel weight exceed the payload available. "+"The payload available is "+InsertData.d_payload_available+" kg");
                        alert_payload.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alert_payload.show();
                    }
                }
                else{
                    AlertDialog.Builder alert_passengers= new AlertDialog.Builder(CalculateMOM.this);
                    alert_passengers.setTitle("Warning");
                    alert_passengers.setMessage("The passengers exceed the capacity of the basket");
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


}
