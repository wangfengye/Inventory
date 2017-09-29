package com.ascend.wangfeng.inventory;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by fengye on 2017/9/27.
 * email 1040441325@qq.com
 */

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "product.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_PRODUCT_NAME = "product";
    private final String CREATE_TABLE_PRODUCT = "create table " + TABLE_PRODUCT_NAME
            + "(" + Contract.ID + " integer primary key "//on conflict replace"
            + "," + Contract.TITLE + " text NOT NULL UNIQUE"
            + "," + Contract.IMAGE + " text"
            + "," + Contract.PRICE + " real"
            + "," + Contract.SOLD_COUNT + " integer"
            + "," + Contract.INVENTORY_COUNT + " integer"
            + ")";

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

    /**
     * 销售产品
     *
     * @param product 产品
     * @param count   数量
     * @return
     */
    public boolean sold(Product product, int count) {
        if (product.getInventoryCount() <= 0) return false;
        ContentValues values = new ContentValues();
        product.setSoldCount(product.getSoldCount() + count);
        product.setInventoryCount(product.getInventoryCount() - count);
        values.put(Contract.SOLD_COUNT, product.getSoldCount());
        values.put(Contract.INVENTORY_COUNT, product.getInventoryCount());
        int updateCount = getWritableDatabase().update(DbHelper.TABLE_PRODUCT_NAME, values, Contract.ID + " = ?",
                new String[]{String.valueOf(product.getId())});
        if (updateCount > 0) {
            return true;
        }
        return false;
    }


}
