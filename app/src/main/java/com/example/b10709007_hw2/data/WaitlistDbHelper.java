package com.example.b10709007_hw2.data;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.b10709007_hw2.data.WaitlistContract.*;

public class WaitlistDbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "waitlist.db";// db的名稱

    private static final int DATABASE_VERSION = 1;    //如果要改schema才要更動


    public WaitlistDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {// 建立table


        final String SQL_CREATE_WAITLIST_TABLE = "CREATE TABLE " + WaitlistEntry.TABLE_NAME + " (" +
                WaitlistEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                WaitlistEntry.COLUMN_GUEST_NAME + " TEXT NOT NULL, " +
                WaitlistEntry.COLUMN_PARTY_SIZE + " INTEGER NOT NULL, " +
                WaitlistEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_WAITLIST_TABLE);//執行
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + WaitlistEntry.TABLE_NAME);//
        onCreate(sqLiteDatabase);
    }
}