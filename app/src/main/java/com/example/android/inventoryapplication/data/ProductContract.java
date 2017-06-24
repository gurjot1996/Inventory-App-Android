package com.example.android.inventoryapplication.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gurjot on 6/20/17.
 */

public class ProductContract {
    //defining Content authority
    public static final String CONTENT_AUTHORITY = "com.example.android.inventoryapplication";

    //defining the base uri
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //defining the product path
    public static final String PRODUCT_PATH = "products";

    //database table named products
    public static final class products implements BaseColumns {
        //final Content uri
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PRODUCT_PATH);

        //table name
        public static final String TABLE_NAME = "products";

        //column of the products table
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NAME = "NAME";
        public static final String COLUMN_PRICE = "PRICE";
        public static final String COLUMN_QUANTITY = "QUANTITY";
        public static final String COLUMN_EMAIL = "EMAIL";
        public static final String COLUMN_IMAGE = "IMAGE";
    }
}
