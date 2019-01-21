package com.example.asus.myapplication;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainInterface extends Activity implements OnClickListener {
    private LinearLayout llgoods, llFriends, llContacts;
    private Button button;
    private List<product> productList = new ArrayList<product>();
    private int the_user_id, the_user_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_interface);

        initView();
        initList();
    }
    private void initList(){
        initProduct(); // 初始化饮料数据
        productAdapeter adapter = new productAdapeter(this, R.layout.list_item, productList);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                product product = productList.get(position);
                if (the_user_type == 1) {
                    buy_drink(product);
                }
            }
        });
    }
    private void buy_drink(product myproduct)
    {
        DBOpenHelper helper = new DBOpenHelper(this, "mydata.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        int user_id = myproduct.getProduct_userid();
        int id = myproduct.getProduct_id();

        ContentValues values= new ContentValues();
        values.put("userId",the_user_id);
        values.put("goodsId",id);
        long rowid = db.insert("orderData",null,values);

        ContentValues sales_values= new ContentValues();
        sales_values.put("userId",user_id);
        sales_values.put("goodsId",id);
        long sales_rowid = db.insert("salesData",null,sales_values);

        Toast.makeText(MainInterface.this, "购买成功,请在订单页查看", Toast.LENGTH_SHORT).show();
    }
    private void initView() {
        llgoods = (LinearLayout) findViewById(R.id.llgoods);
        llFriends = (LinearLayout) findViewById(R.id.llFriends);
        llContacts = (LinearLayout) findViewById(R.id.llContacts);
        llgoods.setOnClickListener(this);
        llFriends.setOnClickListener(this);
        llContacts.setOnClickListener(this);
        //loginInfo表示文件名  SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        the_user_id = sp.getInt("userId",0);
        the_user_type = sp.getInt("userType",0);
    }

    @Override
    public void onClick(View v) {
        changeTab(v.getId());
    }

    private void changeTab(int id) {
        switch (id) {
            case R.id.llgoods:
                Log.i("this","is llgoods");
                this.finish();
                Intent intent1 = new Intent(this, MainInterface.class);
                startActivity(intent1);
                break;
            case R.id.llFriends:
                Log.i("this","is llFriends");
                this.finish();
                Intent intent2 = new Intent(this, ProductOrder.class);
                startActivity(intent2);
                break;
            case R.id.llContacts:
                Log.i("this","is llContacts");
                MainInterface.this.finish();
                Intent intent3 = new Intent(this, MyInfomation.class);
                startActivity(intent3);
                break;
            default:
                break;
        }
    }

    //这里获取数据 改的时候改下这里
    private void initProduct() {
        product title = new product("名称","价格", 0, 0, "备注");
        productList.add(title);
        //调用DBOpenHelper （qianbao.db是创建的数据库的名称）
        DBOpenHelper helper = new DBOpenHelper(this, "mydata.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        //根据画面上输入的账号/密码去数据库中进行查询（goodsData是表名）
        Cursor c = db.query("goodsData",null, null, null, null, null, null);
        //如果有查询到数据
        if (c != null && c.getCount() >= 1) {
            //可以把查询出来的值打印出来在后台显示/查看
            String[] cols = c.getColumnNames();
            while (c.moveToNext()) {
                product drinks = new product(
                        c.getString(c.getColumnIndex("title")),
                        c.getString(c.getColumnIndex("price")),
                        c.getInt(c.getColumnIndex("id")),
                        c.getInt(c.getColumnIndex("userId")),
                        c.getString(c.getColumnIndex("detail")));
                productList.add(drinks);
            }
            c.close();
            db.close();
        }
    }
}