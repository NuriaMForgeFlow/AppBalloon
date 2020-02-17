package com.example.appballoon;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.appballoon.Adapter.MyAdapterDoc;
import com.example.appballoon.Class.Balloon;
import com.example.appballoon.DBHelper.SQLiteHelperDoc;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.view.View.GONE;



public class Documentation extends AppCompatActivity  {

    private static final String TAG ="Documentation";

    FirebaseDatabase firebaseDatabase;
    FirebaseAuth mAuth;
    FirebaseUser user;
    Toolbar toolbar;
    ProgressBar loading;

    DatabaseReference reference;
    private RecyclerView recyclerView;
    private ArrayList<Balloon> list;
    private MyAdapterDoc adapterDoc;


    public static SQLiteHelperDoc mSQLiteHelperDoc;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentation);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Documentation");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),WeatherActivity.class));
            }
        });

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loading=findViewById(R.id.loading);

        list=new ArrayList<Balloon>();
        firebaseDatabase=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        mSQLiteHelperDoc= new SQLiteHelperDoc(this,"DOCUMENTATIONSIGNDB.sqlite",null,1);
        mSQLiteHelperDoc.queryData("CREATE TABLE IF NOT EXISTS DOCUMENTATIONSIGN(id INTEGER PRIMARY KEY, license VARCHAR, cda VARCHAR, cdm VARCHAR, insurance VARCHAR, rcda VARCHAR, today VARCHAR, image BLOB)");




        reference= FirebaseDatabase.getInstance().getReference().child(user.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    Balloon b=dataSnapshot1.getValue(Balloon.class);
                    list.add(b);
                }
                adapterDoc=new MyAdapterDoc(Documentation.this,list);
                recyclerView.setAdapter(adapterDoc);
                loading.setVisibility(GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("Ch3","Failed to read value.", databaseError.toException());

            }
        });

    }



}
