package com.example.android.inventoryapplication.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by gurjot on 6/20/17.
 */

public class ProductDbClass extends SQLiteOpenHelper {
    //Database name
    private static final String DATABASE_NAME = "products.db";
    //database version
    private static final int DATABASE_VERSION = 1;

    //public constructor
    public ProductDbClass(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //oncreate method to create table
    @Override
    public void onCreate(SQLiteDatabase db) {
        //SQL statement to create new table
        String SQL_CREATE = "create table " + ProductContract.products.TABLE_NAME + " ( " +
                ProductContract.products._ID + " INTEGER primary key autoincrement , " +
                ProductContract.products.COLUMN_NAME + " TEXT not null, " +
                ProductContract.products.COLUMN_PRICE + " INTEGER not null, " +
                ProductContract.products.COLUMN_QUANTITY + " INTEGER default 0, " +
                ProductContract.products.COLUMN_EMAIL + " TEXT not null ," +
                ProductContract.products.COLUMN_IMAGE + " TEXT );";
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //SQL statement to drop table
        String SQL_DROP = "drop table " + ProductContract.products.TABLE_NAME;
        db.execSQL(SQL_DROP);
    }
}
