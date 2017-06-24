package com.example.android.inventoryapplication.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by gurjot on 6/20/17.
 */

public class ProductProvider extends ContentProvider {
    //default contructor
    public ProductProvider() {
    }

    //code -100 if all rows are to be accessed
    private static final int PRODUCTS = 100;
    //code -101 if only particular row is to be accessed
    private static final int PRODUCTS_ID = 101;
    //setting up the urimatcher
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //adding types of uri to the uriMatcher
    static {
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PRODUCT_PATH, PRODUCTS);
        uriMatcher.addURI(ProductContract.CONTENT_AUTHORITY, ProductContract.PRODUCT_PATH + "/#", PRODUCTS_ID);
    }

    //instance of ProductDbClass
    ProductDbClass mHelper = null;

    @Override
    public boolean onCreate() {
        //instantiating the mhelper
        mHelper = new ProductDbClass(getContext());
        return false;
    }

    //query/read method
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor = null;
        SQLiteDatabase database = mHelper.getReadableDatabase();
        int m = uriMatcher.match(uri);
        switch (m) {
            case PRODUCTS:
                cursor = database.query(ProductContract.products.TABLE_NAME, projection, null, null, null, null, null);
                break;
            case PRODUCTS_ID:
                selection = ProductContract.products._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(ProductContract.products.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
                break;
            default:
                throw new IllegalArgumentException("Illegal uri" + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }


    //insert method
    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int ma = uriMatcher.match(uri);
        Uri final_uri = null;
        switch (ma) {
            case PRODUCTS:
                //fetching input from database column
                String name = values.getAsString(ProductContract.products.COLUMN_NAME);
                int price = values.getAsInteger(ProductContract.products.COLUMN_PRICE);
                String email = values.getAsString(ProductContract.products.COLUMN_EMAIL);
                //validating to check if any field is left blank
                if (name.isEmpty()) {
                    Toast.makeText(getContext(), "Name field left Blank !!!", Toast.LENGTH_SHORT).show();
                } else if (price == 0) {
                    Toast.makeText(getContext(), "Price field left blank !!!", Toast.LENGTH_SHORT).show();
                } else if (email.isEmpty()) {
                    Toast.makeText(getContext(), "Email field left Blank !!!", Toast.LENGTH_SHORT).show();
                } else {
                    final_uri = insertProduct(uri, values);
                }
                break;
            default:
                throw new IllegalArgumentException("error");
        }
        //notify all listeners if new row is inserted
        getContext().getContentResolver().notifyChange(final_uri, null);
        return final_uri;
    }

    private Uri insertProduct(Uri uri, ContentValues values) {
        //instance of SQLiteDatabase to execute the insert operation
        SQLiteDatabase db = mHelper.getWritableDatabase();
        long id = db.insert(ProductContract.products.TABLE_NAME, null, values);
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    //delete method
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted = 0;
        SQLiteDatabase database = mHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PRODUCTS://delete all rows
                rowsDeleted = database.delete(ProductContract.products.TABLE_NAME, null, null);
                break;
            case PRODUCTS_ID://delete a particular row
                selection = ProductContract.products._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(ProductContract.products.TABLE_NAME, selection, selectionArgs);
                break;
        }
        //if no row has been deleted
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    //update method
    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = uriMatcher.match(uri);
        int mrows = 0;
        switch (match) {
            case PRODUCTS://updating multiple rows
                mrows = updateProduct(uri, values, null, null);
            case PRODUCTS_ID://updating a particular row id
                selection = ProductContract.products._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                mrows = updateProduct(uri, values, selection, selectionArgs);
        }
        return mrows;
    }

    public int updateProduct(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //condition to check if name field is left null
        if (values.containsKey(ProductContract.products.COLUMN_NAME)) {
            String name = values.getAsString(ProductContract.products.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }
        //condition to check if email field is left null
        if (values.containsKey(ProductContract.products.COLUMN_EMAIL)) {
            String email = values.getAsString(ProductContract.products.COLUMN_EMAIL);
            if (email == null) {
                throw new IllegalArgumentException("email is required");
            }
        }
        //condition to check if price field is left null
        if (values.containsKey(ProductContract.products.COLUMN_PRICE)) {
            int price = values.getAsInteger(ProductContract.products.COLUMN_PRICE);
            if (price < 0) {
                throw new IllegalArgumentException("price can not be negative");
            }
        }
        //condition to check if quantity field is negative
        if (values.containsKey(ProductContract.products.COLUMN_QUANTITY)) {
            int quantity = values.getAsInteger(ProductContract.products.COLUMN_QUANTITY);
            if (quantity < 0) {
                throw new IllegalArgumentException("quantity can not be negative");
            }
        }
        //if no values have been fetched
        if (values.size() == 0) {
            return 0;
        }
        //update operation
        SQLiteDatabase db = mHelper.getWritableDatabase();
        int rows = db.update(ProductContract.products.TABLE_NAME, values, selection, selectionArgs);
        if (rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        //number of rows updated
        return rows;
    }
}
