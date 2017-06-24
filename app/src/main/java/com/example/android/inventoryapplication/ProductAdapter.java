package com.example.android.inventoryapplication;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.inventoryapplication.data.ProductContract;


/**
 * Created by gurjot on 6/20/17.
 */

public class ProductAdapter extends CursorAdapter {

    public ProductAdapter(Context context, Cursor cursor) {
        super(context, cursor, false);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        //populating the created list view

        //instances of items of the list view layout
        TextView t1 = (TextView) view.findViewById(R.id.productName);
        TextView t2 = (TextView) view.findViewById(R.id.quantityShown);
        TextView t3 = (TextView) view.findViewById(R.id.price);
        ImageView imageView = (ImageView) view.findViewById(R.id.productImage);

        //fetching data from the cursor
        int name = cursor.getColumnIndex(ProductContract.products.COLUMN_NAME);
        int quantiy = cursor.getColumnIndex(ProductContract.products.COLUMN_QUANTITY);
        int price = cursor.getColumnIndex(ProductContract.products.COLUMN_PRICE);
        String nameText = cursor.getString(name);
        int quantityText = cursor.getInt(quantiy);
        int priceText = cursor.getInt(price);
        String m = Integer.toString(quantityText);
        String n = Integer.toString(priceText);

        //populating list view layout with item values
        t1.setText(nameText);
        t2.setText(m);
        t3.setText(n);
        imageView.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(ProductContract.products.COLUMN_IMAGE))));
    }
}
