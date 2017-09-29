package com.ascend.wangfeng.inventory;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity {

    public static final String EMAIL_TYPE = "message/rfc822";
    private static final String WHERE_CLAUSE = Contract.ID + " = ?";
    private static final int COUNT_CHANGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //获取数据
        final Product product = (Product) getIntent().getSerializableExtra(ProductAdapter.KEY_PRODUCT);

        //实例化控件
        TextView titleView = (TextView) findViewById(R.id.title);
        TextView priceView = (TextView) findViewById(R.id.price);
        final TextView soldView = (TextView) findViewById(R.id.count_sold);
        final TextView inventoryView = (TextView) findViewById(R.id.count_inventory);
        Button soldBtn = (Button) findViewById(R.id.sold);
        Button addInventoryBtn = (Button) findViewById(R.id.add);
        final Button clearBtn = (Button) findViewById(R.id.clear);
        final Button connectBtn = (Button) findViewById(R.id.connect);
        final DbHelper dbHelper = new DbHelper(this);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();

        //初始化
        titleView.setText(product.getTitle());
        priceView.setText(Convert.convertMoney(product.getPrice()));
        soldView.setText(getString(R.string.solded) + product.getSoldCount());
        inventoryView.setText(getString(R.string.inventory) + product.getInventoryCount());
        soldBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dbHelper.sold(product, COUNT_CHANGE)) {
                    Toast.makeText(DetailActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                    soldView.setText(getString(R.string.solded) + product.getSoldCount());
                    inventoryView.setText(getString(R.string.inventory) + product.getInventoryCount());
                } else {
                    Toast.makeText(DetailActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                }
            }
        });
        addInventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                product.setInventoryCount(product.getInventoryCount() + COUNT_CHANGE);
                ContentValues values = new ContentValues();
                values.put(Contract.INVENTORY_COUNT, product.getInventoryCount());
                database.update(DbHelper.TABLE_PRODUCT_NAME, values, WHERE_CLAUSE,
                        new String[]{String.valueOf(product.getId())});
                Toast.makeText(DetailActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                inventoryView.setText(getString(R.string.inventory) + product.getInventoryCount());
            }
        });
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DetailActivity.this);
                builder.setMessage(R.string.clear_message);
                builder.setTitle(R.string.hint);
                builder.setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface anInterface, int i) {
                        int count = database.delete(DbHelper.TABLE_PRODUCT_NAME, WHERE_CLAUSE,
                                new String[]{String.valueOf(product.getId())});
                        Toast.makeText(DetailActivity.this, R.string.success, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.create().show();
            }
        });

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setType(EMAIL_TYPE);
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{getString(R.string.email_url)});
                email.putExtra(Intent.EXTRA_SUBJECT, product.getTitle());
                startActivity(Intent.createChooser(email, getString(R.string.choose_email)));
            }
        });

        ImageView imageView = (ImageView) findViewById(R.id.image);
        if (product.getImage()!=null){
        Uri uri = getUri(product.getImage());
        imageView.setImageURI(uri);}
    }

    private Uri getUri(String path) {
        Uri mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = this.getContentResolver().query(mediaUri,
                null,
                MediaStore.Images.Media.DISPLAY_NAME + "= ?",
                new String[] {path.substring(path.lastIndexOf("/") + 1)},
                null);

        Uri uri = null;
        if(cursor.moveToFirst()) {
            uri = ContentUris.withAppendedId(mediaUri,
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
        }
        cursor.close();
        return uri;}

}
