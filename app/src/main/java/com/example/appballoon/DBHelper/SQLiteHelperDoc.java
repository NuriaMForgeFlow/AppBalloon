package com.example.appballoon.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.media.Image;

public class SQLiteHelperDoc extends SQLiteOpenHelper {

   public SQLiteHelperDoc(Context context,
                          String name,
                          SQLiteDatabase.CursorFactory factory,
                          int version){
       super(context, name, factory, version);
   }



    @Override
   public void onCreate(SQLiteDatabase db) {

   }

   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

   }


   public void queryData(String sql){
       SQLiteDatabase database = getWritableDatabase();
       database.execSQL(sql);
   }

   public void insertData (String license, String cda, String cdm, String insurance, String rcda, String today, byte[] signature){
       SQLiteDatabase database = getWritableDatabase();
       String sql= "INSERT INTO DOCUMENTATIONSIGN VALUES(NULL, ?, ?, ?, ?, ?, ?, ?)";

       SQLiteStatement statement=database.compileStatement(sql);
       statement.clearBindings();
       statement.bindString(1, license);
       statement.bindString(2, cda);
       statement.bindString(3, cdm);
       statement.bindString(4, insurance);
       statement.bindString(5, rcda);
       statement.bindString(6, today);
       statement.bindBlob(7, signature);



       statement.executeInsert();
   }

    public Cursor getData(String sql){
        SQLiteDatabase database=getReadableDatabase();
        return database.rawQuery(sql,null);
    }

    public void clearDatabase(String TABLE_NAME) {
        SQLiteDatabase database=getWritableDatabase();
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        database.execSQL(clearDBQuery);
    }






}


