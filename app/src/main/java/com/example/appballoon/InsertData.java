package com.example.appballoon;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appballoon.Class.Balloon;
import com.example.appballoon.Interface.IFirebaseLoadDone;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class InsertData extends AppCompatActivity implements IFirebaseLoadDone {

    EditText edt_h,edt_maxh,edt_temp;
    Button btn_savedata;
    Spinner spinner;
    String item;


    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference reference;
    IFirebaseLoadDone iFirebaseLoadDone;
    List <Balloon> balloons;
    public static String d_temperature,d_payload_available,d_altitude,d_maximum_altitude,d_passengers;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);
        edt_temp=(EditText)findViewById(R.id.edt_temp);
        edt_temp.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_SIGNED);
        edt_h=(EditText)findViewById(R.id.edt_h);
        edt_h.setInputType(InputType.TYPE_CLASS_NUMBER);
        edt_maxh=(EditText)findViewById(R.id.edt_maxh);
        edt_maxh.setInputType(InputType.TYPE_CLASS_NUMBER);
        spinner=findViewById(R.id.spinner_load);
        btn_savedata=(Button)findViewById(R.id.btn_savedata);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Calculate MOM");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainPassenger.class));
            }
        });


        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        reference= FirebaseDatabase.getInstance().getReference().child(user.getUid());
        iFirebaseLoadDone= this;
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List <Balloon> balloons = new ArrayList<>();
                for(DataSnapshot balloonSnapShot:dataSnapshot.getChildren())
                {
                    balloons.add(balloonSnapShot.getValue(Balloon.class));

                }
                iFirebaseLoadDone.onFireBaseLoadSuccess(balloons);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                iFirebaseLoadDone.onFireBaseLoadFailed(databaseError.getMessage());

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_savedata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_temp.getText().toString().isEmpty() || edt_h.getText().toString().isEmpty() || edt_maxh.getText().toString().isEmpty() ){
                    AlertDialog.Builder alert_empty= new AlertDialog.Builder(InsertData.this);
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
                    d_temperature = edt_temp.getText().toString();
                    d_altitude = edt_h.getText().toString();
                    d_maximum_altitude = edt_maxh.getText().toString();
                    final int temperature=Integer.parseInt(d_temperature);
                    final int altitude=Integer.parseInt(d_altitude);
                    final int max_altitude=Integer.parseInt(d_maximum_altitude);


                    final Query query = reference.orderByChild("license").equalTo(item);

                    query.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            String volume=dataSnapshot.child("volume").getValue().toString();
                            String passengers=dataSnapshot.child("passengers").getValue().toString();
                            String emptyweight=dataSnapshot.child("emptyweight").getValue().toString();
                            String mom=dataSnapshot.child("mom").getValue().toString();
                            Double nempty_weight=Double.parseDouble(emptyweight);
                            Double nmom=Double.parseDouble(mom);
                            int nvolume=Integer.parseInt(volume);
                            int npassengers=Integer.parseInt(passengers);
                            d_passengers=passengers;

                            final Double R = 287.06;
                            final Double temp_env=373.00001;
                            Double temp_altitude=288.15-0.0065*altitude;
                            Double ISA=temperature+273-temp_altitude;
                            Double temp_maxaltitude=288.15-0.0065*max_altitude;
                            Double pressure=101325*Math.pow((temp_maxaltitude/288.15),5.265);
                            Double temp_amb_maxaltitude=temp_maxaltitude+ISA;
                            Double lift=((pressure*nvolume)/R)*((1/temp_amb_maxaltitude)-(1/temp_env));
                            double lift_min;
                            if(lift<=nmom){
                                lift_min=lift;
                            }
                            else{
                                lift_min=nmom;
                            }
                            Double payload=lift_min-nempty_weight;
                            d_payload_available=String.format("%.2f", payload);
                            Intent intent=new Intent(getApplicationContext(),CalculateMOM.class);
                            intent.putExtra("payload",payload);
                            intent.putExtra("passengers",npassengers);
                            startActivity(intent);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "License not found", Toast.LENGTH_SHORT).show();

                        }
                    });
                }



            }

        });



    }


    @Override
    public void onFireBaseLoadSuccess(List<Balloon> balloonList) {
        balloons=balloonList;
        List <String> name_list= new ArrayList<>();
        for(Balloon balloon:balloonList)
            name_list.add(balloon.getLicense());
        ArrayAdapter<String> adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,name_list);
        spinner.setAdapter(adapter);

    }

    @Override
    public void onFireBaseLoadFailed(String message) {

    }


}