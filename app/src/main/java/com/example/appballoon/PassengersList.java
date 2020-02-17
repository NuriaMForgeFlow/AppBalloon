package com.example.appballoon;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appballoon.Adapter.RecordListAdapter;
import com.example.appballoon.Class.Passenger;

import java.util.ArrayList;

public class PassengersList extends AppCompatActivity {

    ListView mListView;
    ArrayList<Passenger> mList;
    RecordListAdapter mAdapter=null;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Passengers List");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),MainPassenger.class));
            }
        });

        mListView=findViewById(R.id.listView);
        mList=new ArrayList<>();
        mAdapter=new RecordListAdapter(this,R.layout.row,mList);
        mListView.setAdapter(mAdapter);

        Cursor cursor = MainPassenger.mSQLiteHelper.getData("SELECT * FROM RECORD");
        mList.clear();
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String name=cursor.getString(1);
            String surname=cursor.getString(2);
            String dni=cursor.getString(3);
            String terms=cursor.getString(4);
            mList.add(new Passenger(id,name,surname,dni,terms));
        }
        mAdapter.notifyDataSetChanged();
        if(mList.size()==0){
            Toast.makeText(this, "No data", Toast.LENGTH_SHORT).show();
        }

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                CharSequence[] items={"Delete"};

                AlertDialog.Builder dialog = new AlertDialog.Builder(PassengersList.this);
                dialog.setTitle("Choose an action");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            Cursor c= MainPassenger.mSQLiteHelper.getData("SELECT id FROM RECORD");
                            ArrayList<Integer> arrID= new ArrayList<Integer>();
                            while(c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    private void showDialogDelete(final int idRecord) {
        AlertDialog.Builder dialogDelete=new AlertDialog.Builder(PassengersList.this);
        dialogDelete.setTitle("Warning!");
        dialogDelete.setMessage("Are you sure?");
        dialogDelete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    MainPassenger.mSQLiteHelper.deleteData(idRecord);
                    Toast.makeText(PassengersList.this,"Deleted",Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Log.e("error",e.getMessage());
                }
                updateRecordList();
            }
        });
        dialogDelete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }



    private void updateRecordList() {
        Cursor cursor= MainPassenger.mSQLiteHelper.getData("SELECT * FROM RECORD");
        mList.clear();
        while (cursor.moveToNext()){
            int id=cursor.getInt(0);
            String name=cursor.getString(1);
            String surname=cursor.getString(2);
            String dni=cursor.getString(3);
            String terms=cursor.getString(4);
            mList.add(new Passenger(id,name,surname,dni,terms));
        }
        mAdapter.notifyDataSetChanged();
    }


}

