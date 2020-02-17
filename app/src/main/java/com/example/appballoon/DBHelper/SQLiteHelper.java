package com.example.appballoon.DBHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context,
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

    public void insertData (String name, String surname, String phone, String terms){
        SQLiteDatabase database = getWritableDatabase();
        String sql= "INSERT INTO RECORD VALUES(NULL, ?, ?, ?, ?)";

        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, name);
        statement.bindString(2, surname);
        statement.bindString(3, phone);
        statement.bindString(4, terms);



        statement.executeInsert();
    }




    public void deleteData(int id){
        SQLiteDatabase database =getWritableDatabase();

        String sql="DELETE FROM RECORD WHERE id=?";

        SQLiteStatement statement=database.compileStatement(sql);
        statement.clearBindings();
        statement.bindDouble(1, (double)id);

        statement.execute();
        database.close();
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
