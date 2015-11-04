package com.example.divya.helpers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.divya.model.Member;

public class SQLiteFavorite extends SQLiteOpenHelper {

    private static final String TAG = SQLiteFavorite.class.getSimpleName();


    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "tipstat2";


    public SQLiteFavorite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE ="CREATE TABLE favorite (id TEXT,dob TEXT,status TEXT,ethnicity TEXT,weight TEXT,height TEXT,is_veg TEXT,drink TEXT,imgurl TEXT,created_at TEXT)";

        db.execSQL(CREATE_LOGIN_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS favorite");

        onCreate(db);
    }

    public void addfavorite(Member m)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("id", m.id);
        values.put("dob", m.dob);
        values.put("status", m.status);
        values.put("ethnicity", m.ethnicity);
        values.put("weight", m.weight);
        values.put("height", m.height);
        values.put("is_veg", m.is_veg);
        values.put("drink", m.drink);
        values.put("imgurl", m.imgurl);

        values.put("created_at", getDateTime());

        long sql_id = db.insert("favorite", null, values);
        db.close();

    }

    public ArrayList<Member> getDetails() {
        ArrayList<Member> favorite = new ArrayList<Member>();
        String selectQuery = "SELECT  * FROM favorite";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        int count=1;
        if (cursor.moveToFirst()) {
            do {
                favorite.add(new Member(cursor.getString(cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("dob")),
                        cursor.getString(cursor.getColumnIndex("status")), cursor.getString(cursor.getColumnIndex("ethnicity")),
                        cursor.getString(cursor.getColumnIndex("weight")),cursor.getString(cursor.getColumnIndex("height")),
                        cursor.getString(cursor.getColumnIndex("is_veg")), cursor.getString(cursor.getColumnIndex("drink")),
                        cursor.getString(cursor.getColumnIndex("imgurl"))));
                count++;
            } while (cursor.moveToNext());
        }

        else
        {
        }

        cursor.close();
        db.close();

        return favorite;
    }

    public int getRowCount() {
        String countQuery = "SELECT  * FROM favorite";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();

        return rowCount;
    }

    public void deleteMembers() {
        SQLiteDatabase db = this.getWritableDatabase();
        if(db!=null)
        {
            db.delete("favorite", null, null);
            db.close();
        }
    }

    public void deleteItem(String item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            db.delete("favorite", "no = ?", new String[] { item });
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            db.close();
        }
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}