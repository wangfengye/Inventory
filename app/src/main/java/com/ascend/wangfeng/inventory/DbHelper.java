package com.ascend.wangfeng.inventory;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fengye on 2017/9/27.
 * email 1040441325@qq.com
 */

public class DbHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME="product.db";
    private static final int DATABASE_VERSION=1;
    public static final String TABLE_PRODUCT_NAME="product";
    private final String CREATE_TABLE_PRODUCT="create table "+TABLE_PRODUCT_NAME
            +"("+Contract.ID+" integer primary key "//on conflict replace"
            +","+Contract.TITLE+" text NOT NULL UNIQUE"
            +","+Contract.PRICE+" real"
            +","+Contract.SOLD_COUNT+" integer"
            +","+Contract.INVENTORY_COUNT+" integer"
            +")";
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_PRODUCT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int i, int i1) {

    }
}
