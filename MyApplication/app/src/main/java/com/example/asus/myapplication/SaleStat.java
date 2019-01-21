package com.example.asus.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SaleStat extends AppCompatActivity implements View.OnClickListener {
    private RadioButton radio_btn1, radio_btn2, radio_btn3;
    private Button btn_ok;
    private TextView tv_back, tv_main_title;
    private List<StatStruct> productList = new ArrayList<StatStruct>();
    private int the_user_id, the_user_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat);
        init();
        initProduct1();
        initList();
    }
    private void initList(){
        StatAdapeter adapter = new StatAdapeter(this, R.layout.stat_list_item, productList);
        ListView listView = (ListView) findViewById(R.id.list_view1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                StatStruct product = productList.get(position);
                Toast.makeText(SaleStat.this, product.getProduct_total(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        radio_btn1 = findViewById(R.id.radioButton1);
        radio_btn1.setOnClickListener(this);
        radio_btn2 = findViewById(R.id.radioButton2);
        radio_btn2.setOnClickListener(this);
        radio_btn3 = findViewById(R.id.radioButton3);
        radio_btn3.setOnClickListener(this);
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        the_user_id = sp.getInt("userId",0);
        the_user_type = sp.getInt("userType",0);


        tv_back = (TextView)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        tv_main_title = (TextView)findViewById(R.id.tv_main_title);
        Intent intent = getIntent();
        String page_title = intent.getStringExtra("page_title");
        tv_main_title.setText(page_title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radioButton1:
                initProduct1(); // 初始化饮料数据
                initList();
                break;
            case R.id.radioButton2:
                initProduct2(); // 初始化饮料数据
                initList();
                break;
            case R.id.radioButton3:
                initProduct3(); // 初始化饮料数据
                initList();
                break;
            case R.id.tv_back:
                this.finish();
                Intent intent = new Intent(this, MyInfomation.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
    //这里获取数据 改的时候改下这里
    private void initProduct1() {
        productList.clear();
        StatStruct title = new StatStruct("日期","收入合计","销量合计");
        productList.add(title);

        //调用DBOpenHelper （mydata.db是创建的数据库的名称）
        DBOpenHelper helper = new DBOpenHelper(this, "mydata.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c;
        Date now = new Date(); //获取当前时间
        int cur = 0;
        if (the_user_type == 1) {
            while (true){
                int time = cur *1;
                cur +=1;
                int prev_time = cur*1;
                Calendar calendar = Calendar.getInstance();//得到calendar
                Calendar prev_calendar = Calendar.getInstance();//得到calendar
                prev_calendar.setTime(now);//当前时间设置给calendar
                calendar.setTime(now);//当前时间设置给calendar
                calendar.add(Calendar.DAY_OF_YEAR, -time);
                prev_calendar.add(Calendar.DAY_OF_YEAR, -prev_time);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd 23:59:59");// "yyyy-MM-dd HH:mm:ss.fff"
                String defaultStartDate = simpleDateFormat.format(calendar.getTime());//格式化
                String prev_defaultStartDate = simpleDateFormat.format(prev_calendar.getTime());//格式化

                c = db.rawQuery("select * from orderData where userId = ? and createTime<=datetime(?) and createTime>datetime(?)",new String[]{""+the_user_id,defaultStartDate, prev_defaultStartDate});
                //如果有查询到数据
                String Date = "第"+cur+"天";
                int product_count = 0;
                int product_total = 0;
                if (c != null && c.getCount() >= 1) {
                    //可以把查询出来的值打印出来在后台显示/查看
                    String[] cols = c.getColumnNames();
                    while (c.moveToNext()) {
                        int goods_id = c.getInt(c.getColumnIndex("goodsId"));
                        Cursor sub_c = db.rawQuery("select * from goodsData where id=?", new String[]{"" + goods_id});
                        if (sub_c != null && sub_c.getCount() >= 1) {
                            while (sub_c.moveToNext()) {
                                product_count += Integer.parseInt(sub_c.getString(sub_c.getColumnIndex("price")));
                                product_total +=1;
                            }
                        }
                    }
                    StatStruct maidong = new StatStruct(Date,""+product_count,""+product_total);
                    productList.add(maidong);
                }
                if (cur > 10){
                    break;
                }
            }
        }else {
            while (true){
                int time = cur *1;
                cur +=1;
                int prev_time = cur*1;
                Calendar calendar = Calendar.getInstance();//得到calendar
                Calendar prev_calendar = Calendar.getInstance();//得到calendar
                prev_calendar.setTime(now);//当前时间设置给calendar
                calendar.setTime(now);//当前时间设置给calendar
                calendar.add(Calendar.DAY_OF_YEAR, -time);
                prev_calendar.add(Calendar.DAY_OF_YEAR, -prev_time);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd 23:59:59");// "yyyy-MM-dd HH:mm:ss.fff"
                String defaultStartDate = simpleDateFormat.format(calendar.getTime());//格式化
                String prev_defaultStartDate = simpleDateFormat.format(prev_calendar.getTime());//格式化

                c = db.rawQuery("select * from salesData where userId = ? and createTime<=datetime(?) and createTime>datetime(?)",new String[]{""+the_user_id,defaultStartDate, prev_defaultStartDate});
                //如果有查询到数据
                String Date = "第"+cur+"天";
                int product_count = 0;
                int product_total = 0;
                if (c != null && c.getCount() >= 1) {
                    //可以把查询出来的值打印出来在后台显示/查看
                    String[] cols = c.getColumnNames();
                    while (c.moveToNext()) {
                        int goods_id = c.getInt(c.getColumnIndex("goodsId"));
                        Cursor sub_c = db.rawQuery("select * from goodsData where id=?", new String[]{"" + goods_id});
                        if (sub_c != null && sub_c.getCount() >= 1) {
                            while (sub_c.moveToNext()) {
                                product_count += Integer.parseInt(sub_c.getString(sub_c.getColumnIndex("price")));
                                product_total +=1;
                            }
                        }
                    }
                    StatStruct maidong = new StatStruct(Date,""+product_count,""+product_total);
                    productList.add(maidong);
                }
                if (cur > 10){
                    break;
                }
            }
        }
    }
    private void initProduct2() {
        productList.clear();
        Date now = new Date(); //获取当前时间
        StatStruct title = new StatStruct("日期","收入合计","销量合计");
        productList.add(title);
        //调用DBOpenHelper （mydata.db是创建的数据库的名称）
        DBOpenHelper helper = new DBOpenHelper(this, "mydata.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c;
        int cur = 0;
        if (the_user_type == 1) {
            while (true){
                int time = cur *7;
                cur +=1;
                int prev_time = cur*7;
                Calendar calendar = Calendar.getInstance();//得到calendar
                Calendar prev_calendar = Calendar.getInstance();//得到calendar
                prev_calendar.setTime(now);//当前时间设置给calendar
                calendar.setTime(now);//当前时间设置给calendar
                calendar.add(Calendar.DAY_OF_YEAR, -time);
                prev_calendar.add(Calendar.DAY_OF_YEAR, -prev_time);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd 23:59:59");// "yyyy-MM-dd HH:mm:ss.fff"
                String defaultStartDate = simpleDateFormat.format(calendar.getTime());//格式化
                String prev_defaultStartDate = simpleDateFormat.format(prev_calendar.getTime());//格式化

                c = db.rawQuery("select * from orderData where userId = ? and createTime<=datetime(?) and createTime>datetime(?)",new String[]{""+the_user_id,defaultStartDate, prev_defaultStartDate});
                //如果有查询到数据
                String Date = "第"+cur+"周";
                int product_count = 0;
                int product_total = 0;
                if (c != null && c.getCount() >= 1) {
                    //可以把查询出来的值打印出来在后台显示/查看
                    String[] cols = c.getColumnNames();
                    while (c.moveToNext()) {
                        int goods_id = c.getInt(c.getColumnIndex("goodsId"));
                        Cursor sub_c = db.rawQuery("select * from goodsData where id=?", new String[]{"" + goods_id});
                        if (sub_c != null && sub_c.getCount() >= 1) {
                            while (sub_c.moveToNext()) {
                                product_count += Integer.parseInt(sub_c.getString(sub_c.getColumnIndex("price")));
                                product_total +=1;
                            }
                        }
                    }
                    StatStruct maidong = new StatStruct(Date,""+product_count,""+product_total);
                    productList.add(maidong);
                }
                if(cur > 10){
                    break;
                }
            }
        }else {
            while (true){
                int time = cur *7;
                cur +=1;
                int prev_time = cur*7;
                Calendar calendar = Calendar.getInstance();//得到calendar
                Calendar prev_calendar = Calendar.getInstance();//得到calendar
                prev_calendar.setTime(now);//当前时间设置给calendar
                calendar.setTime(now);//当前时间设置给calendar
                calendar.add(Calendar.DAY_OF_YEAR, -time);
                prev_calendar.add(Calendar.DAY_OF_YEAR, -prev_time);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd 23:59:59");// "yyyy-MM-dd HH:mm:ss.fff"
                String defaultStartDate = simpleDateFormat.format(calendar.getTime());//格式化
                String prev_defaultStartDate = simpleDateFormat.format(prev_calendar.getTime());//格式化

                c = db.rawQuery("select * from salesData where userId = ? and createTime<=datetime(?) and createTime>datetime(?)",new String[]{""+the_user_id,defaultStartDate, prev_defaultStartDate});
                //如果有查询到数据
                String Date = "第"+cur+"周";
                int product_count = 0;
                int product_total = 0;
                if (c != null && c.getCount() >= 1) {
                    //可以把查询出来的值打印出来在后台显示/查看
                    String[] cols = c.getColumnNames();
                    while (c.moveToNext()) {
                        int goods_id = c.getInt(c.getColumnIndex("goodsId"));
                        Cursor sub_c = db.rawQuery("select * from goodsData where id=?", new String[]{"" + goods_id});
                        if (sub_c != null && sub_c.getCount() >= 1) {
                            while (sub_c.moveToNext()) {
                                product_count += Integer.parseInt(sub_c.getString(sub_c.getColumnIndex("price")));
                                product_total +=1;
                            }
                        }
                    }
                    StatStruct maidong = new StatStruct(Date,""+product_count,""+product_total);
                    productList.add(maidong);
                }
                if(cur>10){
                    break;
                }
            }
        }    }

    private void initProduct3() {
        productList.clear();
        Date now = new Date(); //获取当前时间
        StatStruct title = new StatStruct("日期","收入合计","销量合计");
        productList.add(title);
        //调用DBOpenHelper （mydata.db是创建的数据库的名称）
        DBOpenHelper helper = new DBOpenHelper(this, "mydata.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c;
        int cur = 0;
        if (the_user_type == 1) {
            while (true){
                int time = cur *30;
                cur +=1;
                int prev_time = cur*7;
                Calendar calendar = Calendar.getInstance();//得到calendar
                Calendar prev_calendar = Calendar.getInstance();//得到calendar
                prev_calendar.setTime(now);//当前时间设置给calendar
                calendar.setTime(now);//当前时间设置给calendar
                calendar.add(Calendar.DAY_OF_YEAR, -time);
                prev_calendar.add(Calendar.DAY_OF_YEAR, -prev_time);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd 23:59:59");// "yyyy-MM-dd HH:mm:ss.fff"
                String defaultStartDate = simpleDateFormat.format(calendar.getTime());//格式化
                String prev_defaultStartDate = simpleDateFormat.format(prev_calendar.getTime());//格式化
                c = db.rawQuery("select * from orderData where userId = ? and createTime<=datetime(?) and createTime>datetime(?)",new String[]{""+the_user_id,defaultStartDate, prev_defaultStartDate});
                //如果有查询到数据
                String Date = "第"+cur+"月";
                int product_count = 0;
                int product_total = 0;
                if (c != null && c.getCount() >= 1) {
                    //可以把查询出来的值打印出来在后台显示/查看
                    String[] cols = c.getColumnNames();
                    while (c.moveToNext()) {
                        int goods_id = c.getInt(c.getColumnIndex("goodsId"));
                        Cursor sub_c = db.rawQuery("select * from goodsData where id=?", new String[]{"" + goods_id});
                        if (sub_c != null && sub_c.getCount() >= 1) {
                            while (sub_c.moveToNext()) {
                                product_count += Integer.parseInt(sub_c.getString(sub_c.getColumnIndex("price")));
                                product_total +=1;
                            }
                        }
                    }
                    StatStruct maidong = new StatStruct(Date,""+product_count,""+product_total);
                    productList.add(maidong);
                }
                if (cur > 10){
                    break;
                }
            }
        }else {
            while (true){
                int time = cur *30;
                cur +=1;
                int prev_time = cur*7;
                Calendar calendar = Calendar.getInstance();//得到calendar
                Calendar prev_calendar = Calendar.getInstance();//得到calendar
                prev_calendar.setTime(now);//当前时间设置给calendar
                calendar.setTime(now);//当前时间设置给calendar
                calendar.add(Calendar.DAY_OF_YEAR, -time);
                prev_calendar.add(Calendar.DAY_OF_YEAR, -prev_time);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd 23:59:59");// "yyyy-MM-dd HH:mm:ss.fff"
                String defaultStartDate = simpleDateFormat.format(calendar.getTime());//格式化
                String prev_defaultStartDate = simpleDateFormat.format(prev_calendar.getTime());//格式化
                c = db.rawQuery("select * from salesData where userId = ? and createTime<=datetime(?) and createTime>datetime(?)",new String[]{""+the_user_id,defaultStartDate,prev_defaultStartDate});
                //如果有查询到数据
                String Date = "第"+cur+"月";
                int product_count = 0;
                int product_total = 0;
                if (c != null && c.getCount() >= 1) {
                    //可以把查询出来的值打印出来在后台显示/查看
                    String[] cols = c.getColumnNames();
                    while (c.moveToNext()) {
                        int goods_id = c.getInt(c.getColumnIndex("goodsId"));
                        Cursor sub_c = db.rawQuery("select * from goodsData where id=?", new String[]{"" + goods_id});
                        if (sub_c != null && sub_c.getCount() >= 1) {
                            while (sub_c.moveToNext()) {
                                product_count += Integer.parseInt(sub_c.getString(sub_c.getColumnIndex("price")));
                                product_total +=1;
                            }
                        }
                    }
                    StatStruct maidong = new StatStruct(Date,""+product_count,""+product_total);
                    productList.add(maidong);
                }
                break;
            }
        }
    }

}
