package com.example.appballoon.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.view.View;

public class SQLiteHelperWeather extends SQLiteOpenHelper {

    public SQLiteHelperWeather(Context context,
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

    public void insertData (String date, String place, String coordinates, String description, String temperature, String wind, String pressure){
        SQLiteDatabase database = getWritableDatabase();
        String sql= "INSERT INTO WEATHER1 VALUES(NULL, ?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, date);
        statement.bindString(2, place);
        statement.bindString(3, coordinates);
        statement.bindString(4, description);
        statement.bindString(5, temperature);
        statement.bindString(6, wind);
        statement.bindString(7, pressure);
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