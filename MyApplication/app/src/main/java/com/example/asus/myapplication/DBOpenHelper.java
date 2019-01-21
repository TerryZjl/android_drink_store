package com.example.asus.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * @programName: DBOpenHelper.java
 * @programFunction: database helper class
 * @createDate: 2018/09/29
 * @author: AnneHan
 * @version:
 * xx.   yyyy/mm/dd   ver    author    comments
 * 01.   2018/09/29   1.00   AnneHan   New Create
 */
public class DBOpenHelper extends SQLiteOpenHelper {
    public DBOpenHelper(Context context,String name, CursorFactory factory,
                        int version){
        super(context, name, factory, version);
    }

    @Override
    //首次创建数据库的时候调用，一般可以执行建库，建表的操作
    //Sqlite没有单独的布尔存储类型，它使用INTEGER作为存储类型，0为false，1为true
    public void onCreate(SQLiteDatabase db){
        //user table
        db.execSQL("create table if not exists userInfo(id integer primary key autoincrement," +
                "userType integer not null," +
                "userName text not null," +
                "pwd text not null)");
        db.execSQL("create table if not exists goodsData(id integer primary key autoincrement," +
                "userId integer not null," +
                "price text not null," +
                "detail text not null," +
                "title text not null)");
        db.execSQL("create table if not exists orderData(id integer primary key autoincrement," +
                "userId integer not null," +
                "goodsId integer not null," +
                "createTime DATETIME DEFAULT CURRENT_TIMESTAMP)");
        db.execSQL("create table if not exists salesData(id integer primary key autoincrement," +
                "userId integer not null," +
                "goodsId integer not null," +
                "createTime DATETIME DEFAULT CURRENT_TIMESTAMP)");

        //测试时,批量导入数据进行测试
        db.execSQL("INSERT INTO userInfo (id,userType,userName,pwd) VALUES (2,2,'zhaojialong','456')");
        db.execSQL("INSERT INTO userInfo (id,userType,userName,pwd) VALUES (1,1,'zhouruirui','123')");
        db.execSQL("INSERT INTO goodsData (userId,price,detail,title) VALUES (2,'7','提供能量','红牛')");
        db.execSQL("INSERT INTO goodsData (userId,price,detail,title) VALUES (2,'6','让你随时脉动回来','脉动')");
        db.execSQL("INSERT INTO goodsData (userId,price,detail,title) VALUES (2,'5','冰爽一下','雪碧')");
        db.execSQL("INSERT INTO goodsData (userId,price,detail,title) VALUES (2,'3','饭后消消食','酸奶')");
        db.execSQL("INSERT INTO goodsData (userId,price,detail,title) VALUES (2,'12','青春的距离','鸡尾酒')");
        db.execSQL("INSERT INTO goodsData (userId,price,detail,title) VALUES (2,'500','入口柔，一线喉','茅台')");
        db.execSQL("INSERT INTO goodsData (userId,price,detail,title) VALUES (2,'4','冰爽夏日','冰红茶')");
    }

    @Override//当数据库的版本发生变化时，会自动执行
    public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){

    }
}