package com.example.asus.myapplication;

public class product{

    private String product_name;
    private String product_price;
    private int product_id;
    private int product_userid;
    private String product_detail;

    public  product(String product_name, String product_price,int product_id, int product_userid,String product_detail) {
        this.product_name = product_name;
        this.product_price = product_price;
        this.product_id = product_id;
        this.product_userid = product_userid;
        this.product_detail = product_detail;
    }

    public String getProduct_name() {
        return this.product_name;
    }

    public String getProduct_price() {
        return this.product_price;
    }


    public int getProduct_id() {
        return this.product_id;
    }

    public int getProduct_userid() {
        return this.product_userid;
    }

    public String getProduct_detail() {
        return this.product_detail;
    }

}