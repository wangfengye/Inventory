package com.ascend.wangfeng.inventory;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ListView mProductsListView;
    private Button mAddBtn;
    private ProductAdapter adapter;
    private SQLiteDatabase mDatabase;
    private TextView mEmptyView;
    private AlertDialog.Builder dialog;
    ArrayList<Product> products;
    private ImageView imageView;
    private Bitmap mBitmap;
    private Uri mUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mProductsListView = (ListView) findViewById(R.id.list_product);
        mAddBtn = (Button) findViewById(R.id.add_product);
        mEmptyView = (TextView) findViewById(R.id.empty);
        adapter = new ProductAdapter(this, R.layout.item_product);

        products = new ArrayList<>();
        mDatabase = new DbHelper(this).getWritableDatabase();
        mProductsListView.setAdapter(adapter);
        mAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialogShow();
            }
        });
        update();
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    private void addDialogShow() {
            final View layout = getLayoutInflater().inflate(R.layout.dialog_add_product
                    , null);
            final EditText titleView = layout.findViewById(R.id.title);
            final EditText priceView = layout.findViewById(R.id.price);
            imageView = layout.findViewById(R.id.image);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 1);
                }
            });
            dialog = new AlertDialog.Builder(this);
            dialog.setTitle(getString(R.string.add_product));
            dialog.setView(layout);
            dialog.setNegativeButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface anInterface, int i) {
                    String title = titleView.getText().toString();
                    String price = priceView.getText().toString();
                    if (isEmpty(title) || isEmpty(price)) {
                        Toast.makeText(MainActivity.this, R.string.input_error, Toast.LENGTH_SHORT).show();
                    } else {
                        ContentValues values = new ContentValues();
                        values.put(Contract.TITLE, title);
                        values.put(Contract.PRICE, price);
                        if (mUri != null) {
                            values.put(Contract.IMAGE,mUri.getPath());
                            mUri = null;
                        }
                        long count = mDatabase.insert(DbHelper.TABLE_PRODUCT_NAME, null, values);
                        if (count > 0) {
                            Toast.makeText(MainActivity.this, R.string.add_success, Toast.LENGTH_SHORT).show();
                            update();
                        } else {
                            Toast.makeText(MainActivity.this, R.string.add_error, Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
            dialog.create();
        dialog.show();
    }

    /**
     * 更新数据
     */
    private void update() {
        products.clear();
        String[] strings = new String[]{Contract.ID, Contract.TITLE, Contract.IMAGE, Contract.PRICE, Contract.SOLD_COUNT, Contract.INVENTORY_COUNT};
        Cursor cursor = mDatabase.query(DbHelper.TABLE_PRODUCT_NAME, strings, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Contract.ID));
            String title = cursor.getString(cursor.getColumnIndex(Contract.TITLE));
            Double price = cursor.getDouble(cursor.getColumnIndex(Contract.PRICE));
            String image = cursor.getString(cursor.getColumnIndex(Contract.IMAGE));
            Product product = new Product(id, title, image, price);
            product.setSoldCount(cursor.getInt(cursor.getColumnIndex(Contract.SOLD_COUNT)));
            product.setInventoryCount(cursor.getInt(cursor.getColumnIndex(Contract.INVENTORY_COUNT)));
            products.add(product);
        }
        if (products.size() > 0) {
            mEmptyView.setVisibility(View.GONE);
            mProductsListView.setVisibility(View.VISIBLE);
            adapter.clear();
            adapter.addAll(products);
            adapter.notifyDataSetChanged();
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
            mProductsListView.setVisibility(View.GONE);
        }
    }

    private boolean isEmpty(String value) {
        if (value == null || value.length() == 0) {
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) return;

        mUri = data.getData();
        if (imageView != null) {
            imageView.setImageURI(mUri);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
