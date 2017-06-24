package com.example.android.inventoryapplication;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.inventoryapplication.data.ProductContract;
import com.example.android.inventoryapplication.data.ProductDbClass;

import static com.example.android.inventoryapplication.R.id.email;

/**
 * Created by gurjot on 6/20/17.
 */

public class DetailLayout extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static Uri mCurrentUri;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static int flag = 0;
    public static int prev=0;
    //Declaration of instances of the items of editoractivity layout
    EditText pname;
    ImageView piv;
    EditText pprice;
    TextView pquantity;
    EditText pemail;
    Button pplusButton;
    Button pminusButton;
    Button placeOrder;
    ProductDbClass mHelper = null;
    public ImageView imageView;


    Uri actualUri;
    private static final int PICK_IMAGE_REQUEST = 0;
    private static int RESULT_LOAD_IMG = 1;

    @Override

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editoractivity);
        flag = 0;
        //receiving intent data
        final Intent intent = getIntent();
        mCurrentUri = intent.getData();
        if (mCurrentUri == null)//when a new product is to be added
        {
            setTitle(R.string.AddnewProduct);
            //order button is not needed when new item is being added
            hideOrderbutton();
            //menu options like delete need not be displayed
            invalidateOptionsMenu();
        } else {
            //display or edit details of existing items
            setTitle(R.string.EditProduct);
            //image once set should not be altered
            hideSelectImageButton();
            //firing up the loader
            getSupportLoaderManager().initLoader(0, null, this);
        }

        //setting up the instances of editoractivity layout
        imageView = (ImageView) findViewById(R.id.productPic);
        pname = (EditText) findViewById(R.id.nameEntered);
        piv = (ImageView) findViewById(R.id.productPic);
        pprice = (EditText) findViewById(R.id.priceEntered);
        pquantity = (TextView) findViewById(R.id.quantityEntered);
        pemail = (EditText) findViewById(email);
        pplusButton = (Button) findViewById(R.id.plusQuantity);
        pminusButton = (Button) findViewById(R.id.minusQuantity);
        placeOrder = (Button) findViewById(R.id.place_order);

        //onclicklistener for increment button
        pplusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = pquantity.getText().toString();
                int a = Integer.parseInt(q);
                a++;
                String b = Integer.toString(a);
                pquantity.setText(b);
            }
        });
        //onclicklistener for decrement button
        pminusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String q = pquantity.getText().toString();
                int a = Integer.parseInt(q);
                if (a > 0) {
                    a--;
                    String b = Integer.toString(a);
                    pquantity.setText(b);
                } else {
                    pquantity.setText("0");
                }
            }
        });
        //onClickListener for placing order for more items of a particular type i.e PLACE_ORDER button
        placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = pname.getText().toString().trim();
                String price = pprice.getText().toString().trim();
                String quantity = pquantity.getText().toString().trim();
                String email = pemail.getText().toString().trim();
                String res = "Name :" + name + "\n" +
                        "Price :" + price + "\n" +
                        "Quantity :" + quantity + "\n";
                //Intent to the email app
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/html");
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                intent.putExtra(Intent.EXTRA_SUBJECT, "Order Products");
                intent.putExtra(Intent.EXTRA_TEXT, res);
                startActivity(Intent.createChooser(intent, "Send Email"));
            }
        });
        //OnclickListener for imageSelector button
        Button bt = (Button) findViewById(R.id.imageSelector);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToOpenImageSelector();
            }
        });
    }

    public void tryToOpenImageSelector() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return;
        }
        openImageSelector();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                actualUri = resultData.getData();
                imageView.setImageURI(actualUri);
                imageView.invalidate();
            }
        }
    }

    private void openImageSelector() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Projection variable to fetch selected column from database table
        String[] projection = {
                ProductContract.products._ID,
                ProductContract.products.COLUMN_NAME,
                ProductContract.products.COLUMN_PRICE,
                ProductContract.products.COLUMN_EMAIL,
                ProductContract.products.COLUMN_QUANTITY,
                ProductContract.products.COLUMN_IMAGE};
        //returning cursor by creating a new instance of cursorloader class
        return new CursorLoader(this, mCurrentUri, projection, null, null, null);
    }

    //method to hide Place Order button
    public void hideOrderbutton() {
        Button bt = (Button) findViewById(R.id.place_order);
        bt.setVisibility(View.INVISIBLE);
    }

    //method to hide image selector button
    public void hideSelectImageButton() {
        Button bt = (Button) findViewById(R.id.imageSelector);
        bt.setVisibility(View.INVISIBLE);
    }

    //method to be invoked when back button is pressed
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (flag == 1) {
            saveProduct();
            flag = 0;
        }
        Intent i = new Intent(this, ProductActivity.class);
        startActivity(i);
    }

    //method to save new product or update an existing product
    private void saveProduct() {
        //fetching values inputted by the user
        String name = pname.getText().toString().trim();
        String priceString = pprice.getText().toString().trim();
        String quantityString = pquantity.getText().toString().trim();
        String email = pemail.getText().toString().trim();

        //condition for checking if any field is left blank
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(email) && TextUtils.isEmpty(priceString)) {
            Toast.makeText(this, "All fields left blank no product inserted", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Try to insert again !!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Name field empty", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Try to insert again !!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Email field empty", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Try to insert again !!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(priceString)) {
            Toast.makeText(this, "Price field empty", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Try to insert again !!", Toast.LENGTH_SHORT).show();
        } else if (actualUri == null && mCurrentUri == null) {
            Toast.makeText(this, "Image not Selected", Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Try to insert again !!", Toast.LENGTH_SHORT).show();
        } else if (true) {
            int mprice = 0, mquantity = 0;
            if (mCurrentUri == null && TextUtils.isEmpty(name) && TextUtils.isEmpty(priceString) && TextUtils.isEmpty(quantityString) && TextUtils.isEmpty(email)) {
                return;
            }
            //populating contentvalues key value pairs key-database column value-inputted by the user
            ContentValues values = new ContentValues();
            values.put(ProductContract.products.COLUMN_NAME, name);
            values.put(ProductContract.products.COLUMN_QUANTITY, Integer.parseInt(quantityString));
            values.put(ProductContract.products.COLUMN_PRICE, Integer.parseInt(priceString));
            values.put(ProductContract.products.COLUMN_EMAIL, email);

            if (mCurrentUri == null && actualUri != null) {
                values.put(ProductContract.products.COLUMN_IMAGE, actualUri.toString());
            }
            //new product
            if (mCurrentUri == null) {
                Uri newUri = getContentResolver().insert(ProductContract.products.CONTENT_URI, values);
                if (newUri == null) {
                    Toast.makeText(this, R.string.ErrorInsert, Toast.LENGTH_SHORT).show();
                } else/*updating existing product */ {
                    Toast.makeText(this, R.string.ProductNew, Toast.LENGTH_SHORT).show();
                }
            } else {
                mHelper = new ProductDbClass(this);
                SQLiteDatabase db = mHelper.getWritableDatabase();
                int rows = getContentResolver().update(mCurrentUri, values, null, null);//rows is the number of updated rows
                Toast.makeText(this, R.string.UpdateSuccess, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showDeleteConfirmation(Uri k_uri) {
        final Uri uri_g = k_uri;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.DeleteConfirm);
        builder.setPositiveButton(R.string.DeleteItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getContentResolver().delete(uri_g, null, null);
                Intent intent = new Intent(DetailLayout.this, ProductActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(R.string.CancelDelete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int s_id = item.getItemId();
        switch (s_id) {
            case R.id.deleteItem://menu item to delete current item
                if (mCurrentUri != null) {
                    showDeleteConfirmation(mCurrentUri);
                }
                return true;
            case R.id.saveProduct://menu item to save the product
                saveProduct();
                Intent n = new Intent(this, ProductActivity.class);
                startActivity(n);
                return true;
            case android.R.id.home:
                if (flag == 1) {
                    saveProduct();
                    flag = 0;
                }
                Intent nq = new Intent(this, ProductActivity.class);
                startActivity(nq);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentUri == null)//when a new product is to be added
        {
            MenuItem item = menu.findItem(R.id.deleteItem);
            item.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflating the editor menu
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }
        //extracting data from cursor
        if (data.moveToFirst()) {
            int nameIndex = data.getColumnIndex(ProductContract.products.COLUMN_NAME);
            int priceIndex = data.getColumnIndex(ProductContract.products.COLUMN_PRICE);
            int quantityIndex = data.getColumnIndex(ProductContract.products.COLUMN_QUANTITY);
            int emailIndex = data.getColumnIndex(ProductContract.products.COLUMN_EMAIL);
            String name = data.getString(nameIndex);
            int price = data.getInt(priceIndex);
            int quantity = data.getInt(quantityIndex);

            flag = 0;
            //this check is being avoided for dummy data because incase of dummy data the value wont be same
            //eg we had set dummy data quatity as 15 and n_quantity as 0 initially
            //then this if statement will be true and quantity will be set to 0 initially which is not correct
            if (quantity != ProductActivity.n_quantity && prev!=ProductActivity.n_quantity) {
                quantity = ProductActivity.n_quantity;
                prev=ProductActivity.n_quantity;
                flag = 1;
            }

            String email = data.getString(emailIndex);

            //populating the fields with data of the particular item
            imageView.setImageURI(Uri.parse(data.getString(data.getColumnIndex(ProductContract.products.COLUMN_IMAGE))));
            pname.setText(name);
            String a = Integer.toString(price);
            String b = Integer.toString(quantity);
            pprice.setText(a);
            pquantity.setText(b);
            pemail.setText(email);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        //reseting fields when loader is reset
        pname.setText("");
        pprice.setText("");
        pquantity.setText("");
        pemail.setText("");
        piv.setImageResource(0);
    }
}
