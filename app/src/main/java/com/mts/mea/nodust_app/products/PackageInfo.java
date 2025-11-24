package com.mts.mea.nodust_app.products;

/**
 * Created by Mahmoud on 2/17/2018.
 */

public class PackageInfo {
    private  int Package_id;
    private int Product_id;
    private int Quantity;
    private double Price;

    public int getPackage_id() {
        return Package_id;
    }

    public double getPrice() {
        return Price;
    }

    public int getProduct_id() {
        return Product_id;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setPackage_id(int package_id) {
        Package_id = package_id;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public void setProduct_id(int product_id) {
        Product_id = product_id;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
