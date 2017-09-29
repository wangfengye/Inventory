package com.ascend.wangfeng.inventory;

import java.io.Serializable;

/**
 * Created by fengye on 2017/9/27.
 * email 1040441325@qq.com
 */

public class Product implements Serializable{
    /**
     * 数据库数据id
     */
    private int id;
    private String title;
    private String image;
    private  int inventoryCount;
    private double price;
    private int soldCount;

    public Product(int id, String title, String image, double price) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getInventoryCount() {
        return inventoryCount;
    }

    public void setInventoryCount(int inventoryCount) {
        this.inventoryCount = inventoryCount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(int soldCount) {
        this.soldCount = soldCount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
