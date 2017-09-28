package com.ascend.wangfeng.inventory;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_PRODUCT = "product";
    private ListView mProductsListView;
    private Button mAddBtn;
    private ProductAdapter adapter;
    private SQLiteDatabase mDatabase;
    private TextView mEmptyView;
    private AlertDialog.Builder dialog;
    ArrayList<Product> products;

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
        mProductsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> view, View view1, int i, long l) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra(KEY_PRODUCT, (Serializable) products.get(i));
                startActivity(intent);
            }
        });
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
        String[] strings = new String[]{Contract.ID, Contract.TITLE, Contract.PRICE, Contract.SOLD_COUNT, Contract.INVENTORY_COUNT};
        Cursor cursor = mDatabase.query(DbHelper.TABLE_PRODUCT_NAME, strings, null, null, null, null, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex(Contract.ID));
            String title = cursor.getString(cursor.getColumnIndex(Contract.TITLE));
            Double price = cursor.getDouble(cursor.getColumnIndex(Contract.PRICE));
            Product product = new Product(id, title, price);
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
}
