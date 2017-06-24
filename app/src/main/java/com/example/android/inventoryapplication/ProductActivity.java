package com.example.android.inventoryapplication;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapplication.data.ProductContract;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static com.example.android.inventoryapplication.R.id.deleteAll;

public class ProductActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    ProductAdapter mAdapter;
    private Cursor Univ_cursor;
    public long m_id;
    public Uri mcurrentProductUri;
    public static int n_quantity = 0;
    public static int isDummy = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //setting up the listview item using listview and cursoradapter
        ListView listView = (ListView) findViewById(R.id.list);
        View empty = findViewById(R.id.emptyView);
        listView.setEmptyView(empty);
        mAdapter = new ProductAdapter(this, null);
        listView.setAdapter(mAdapter);

        //setting up on click listener intent for each item in the list view
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent in = new Intent(ProductActivity.this, DetailLayout.class);
                Uri currentProduct = ContentUris.withAppendedId(ProductContract.products.CONTENT_URI, id);
                m_id=id;
                in.setData(currentProduct);
                startActivity(in);
            }
        });

        //firing up the loader
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();

    }

    @Override
    protected void onStart() {
        getSupportLoaderManager().initLoader(0, null, this).forceLoad();
        super.onStart();
    }

    public void saleButtonMethod(View v) {
        //once the sale button is clecked the details are altered so the product is no longer a dummy data

        //instance of the layout
        RelativeLayout relativeLayout = (RelativeLayout) v.getParent();
        TextView tt = (TextView) relativeLayout.getChildAt(4);
        String a = tt.getText().toString();
        //condition to monitor negative quantity constraint in the listview item
        int b = Integer.parseInt(a);
        if (b > 0) {
            b = b - 1;
            n_quantity = b;
            String c = Integer.toString(b);
            tt.setText(c);
           Toast.makeText(this,"*Now click on this item to view and save new updations",Toast.LENGTH_LONG).show();

        } else {
            tt.setText("0");
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //setting up the menu layout
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //inserting dummy data
    private void insertDummyData() {
        //flag variable to depict dummy data
        isDummy = 1;
        //Dummy Product 1-Colgate
        ContentValues values = new ContentValues();
        values.put(ProductContract.products.COLUMN_NAME, "Colgate");
        values.put(ProductContract.products.COLUMN_PRICE, 10);
        values.put(ProductContract.products.COLUMN_QUANTITY, 15);
        values.put(ProductContract.products.COLUMN_EMAIL, "colgate@gmail.com");
        values.put(ProductContract.products.COLUMN_IMAGE, "android.resource://com.example.android.inventoryapplication/drawable/colgate");
        //using get content resolver to call insert method of the provider class
        Uri urim = getContentResolver().insert(ProductContract.products.CONTENT_URI, values);

        //Dummy Product 2-Dairy Milk
        ContentValues values1 = new ContentValues();
        values1.put(ProductContract.products.COLUMN_NAME, "Dairy Milk");
        values1.put(ProductContract.products.COLUMN_PRICE, 20);
        values1.put(ProductContract.products.COLUMN_QUANTITY, 30);
        values1.put(ProductContract.products.COLUMN_EMAIL, "dairymilk@gmail.com");
        values1.put(ProductContract.products.COLUMN_IMAGE, "android.resource://com.example.android.inventoryapplication/drawable/dairy_milk");
        //using get content resolver to call insert method of the provider class
        Uri uria = getContentResolver().insert(ProductContract.products.CONTENT_URI, values1);

        //Dummy Product 3-Toothbrushes
        ContentValues values2 = new ContentValues();
        values2.put(ProductContract.products.COLUMN_NAME, "Toothbrush");
        values2.put(ProductContract.products.COLUMN_PRICE, 5);
        values2.put(ProductContract.products.COLUMN_QUANTITY, 24);
        values2.put(ProductContract.products.COLUMN_EMAIL, "oralb@gmail.com");
        values2.put(ProductContract.products.COLUMN_IMAGE, "android.resource://com.example.android.inventoryapplication/drawable/toothbrush");
        //using get content resolver to call insert method of the provider class
        Uri uric = getContentResolver().insert(ProductContract.products.CONTENT_URI, values2);

        //Dummy Product 4-Snickers
        ContentValues values3 = new ContentValues();
        values3.put(ProductContract.products.COLUMN_NAME, "Snickers");
        values3.put(ProductContract.products.COLUMN_PRICE, 23);
        values3.put(ProductContract.products.COLUMN_QUANTITY, 9);
        values3.put(ProductContract.products.COLUMN_EMAIL, "snickers@gmail.com");
        values3.put(ProductContract.products.COLUMN_IMAGE, "android.resource://com.example.android.inventoryapplication/drawable/snickers");
        //using get content resolver to call insert method of the provider class
        Uri urid = getContentResolver().insert(ProductContract.products.CONTENT_URI, values3);

        //Dummy Product 5-Mars
        ContentValues values4 = new ContentValues();
        values4.put(ProductContract.products.COLUMN_NAME, "Mars");
        values4.put(ProductContract.products.COLUMN_PRICE, 12);
        values4.put(ProductContract.products.COLUMN_QUANTITY, 25);
        values4.put(ProductContract.products.COLUMN_EMAIL, "mars@gmail.com");
        values4.put(ProductContract.products.COLUMN_IMAGE, "android.resource://com.example.android.inventoryapplication/drawable/mars");
        //using get content resolver to call insert method of the provider class
        Uri urir = getContentResolver().insert(ProductContract.products.CONTENT_URI, values4);

        //Dummy Product 6-Cheese
        ContentValues values5 = new ContentValues();
        values5.put(ProductContract.products.COLUMN_NAME, "Cheese");
        values5.put(ProductContract.products.COLUMN_PRICE, 16);
        values5.put(ProductContract.products.COLUMN_QUANTITY, 13);
        values5.put(ProductContract.products.COLUMN_EMAIL, "Cheese@gmail.com");
        values5.put(ProductContract.products.COLUMN_IMAGE, "android.resource://com.example.android.inventoryapplication/drawable/cheese");
        //using get content resolver to call insert method of the provider class
        Uri urik = getContentResolver().insert(ProductContract.products.CONTENT_URI, values5);

        //Dummy Product 7-Milk
        ContentValues values6 = new ContentValues();
        values6.put(ProductContract.products.COLUMN_NAME, "Milk");
        values6.put(ProductContract.products.COLUMN_PRICE, 24);
        values6.put(ProductContract.products.COLUMN_QUANTITY, 17);
        values6.put(ProductContract.products.COLUMN_EMAIL, "milk@gmail.com");
        values6.put(ProductContract.products.COLUMN_IMAGE, "android.resource://com.example.android.inventoryapplication/drawable/milk");
        //using get content resolver to call insert method of the provider class
        Uri urij = getContentResolver().insert(ProductContract.products.CONTENT_URI, values6);

        //Dummy Product 8-Chips
        ContentValues values7 = new ContentValues();
        values7.put(ProductContract.products.COLUMN_NAME, "Doritos");
        values7.put(ProductContract.products.COLUMN_PRICE, 32);
        values7.put(ProductContract.products.COLUMN_QUANTITY, 16);
        values7.put(ProductContract.products.COLUMN_EMAIL, "doritos@gmail.com");
        values7.put(ProductContract.products.COLUMN_IMAGE, "android.resource://com.example.android.inventoryapplication/drawable/doritos");
        //using get content resolver to call insert method of the provider class
        Uri uriq = getContentResolver().insert(ProductContract.products.CONTENT_URI, values7);

        //Dummy Product 9-Butter
        ContentValues values8 = new ContentValues();
        values8.put(ProductContract.products.COLUMN_NAME, "Butter");
        values8.put(ProductContract.products.COLUMN_PRICE, 56);
        values8.put(ProductContract.products.COLUMN_QUANTITY, 4);
        values8.put(ProductContract.products.COLUMN_EMAIL, "butter@gmail.com");
        values8.put(ProductContract.products.COLUMN_IMAGE, "android.resource://com.example.android.inventoryapplication/drawable/butter");
        //using get content resolver to call insert method of the provider class
        Uri urie = getContentResolver().insert(ProductContract.products.CONTENT_URI, values8);

    }

    //delete all the products
    private void deleteAllProducts() {
        int rowsDeleted = getContentResolver().delete(ProductContract.products.CONTENT_URI, null, null);
        Log.v(ProductContract.class.getName(), "Rows deleted " + rowsDeleted);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //menu item to add new product
            case R.id.addNewItem:
                Intent intent = new Intent(this, DetailLayout.class);
                startActivity(intent);
                return true;
            //menu item to add dummy data
            case R.id.dummy:
                insertDummyData();
                onStart();
                return true;
            //menu item to delete all items
            case deleteAll:
                deleteAllProducts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //projection string for fetching particular columns
        String[] projection = {ProductContract.products._ID,
                ProductContract.products.COLUMN_NAME,
                ProductContract.products.COLUMN_PRICE,
                ProductContract.products.COLUMN_QUANTITY,
                ProductContract.products.COLUMN_EMAIL,
                ProductContract.products.COLUMN_IMAGE};
        //returning new cursor by creating instance of cursorloader class
        return new CursorLoader(this, ProductContract.products.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Univ_cursor = data;
        //swapping cursor data when cursor is changed on notifyChange method call
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //setting the cursor to null when loader is reset
        mAdapter.swapCursor(null);
    }
}
