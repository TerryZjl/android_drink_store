package com.example.asus.myapplication;

public class StatStruct{

    private  String Date;
    private String product_count;
    private String product_total;

    public  StatStruct(String Date, String product_count, String product_total) {
        this.product_count = product_count;
        this.product_total = product_total;
        this.Date = Date;
    }

    public String getProduct_count() {
        return this.product_count;
    }
    public String getProduct_Date() {
        return this.Date;
    }
    public String getProduct_total() {
        return this.product_total;
    }
}