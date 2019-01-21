package com.example.asus.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ProductOrder extends Activity implements OnClickListener {
    private LinearLayout llgoods, llFriends, llContacts;
    private Button button4;
    private EditText search_bar_text;
    private List<product> productList = new ArrayList<product>();
    private int the_user_id, the_user_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_order);

        initView();
        initProduct1();
        initList();
    }

    private void initView() {
        llgoods = (LinearLayout) findViewById(R.id.llgoods);
        llFriends = (LinearLayout) findViewById(R.id.llFriends);
        llContacts = (LinearLayout) findViewById(R.id.llContacts);
        button4= (Button) findViewById(R.id.button4);
        search_bar_text = (EditText)findViewById(R.id.search_bar_text);
        button4.setOnClickListener(this);
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

    private void initList(){
        productAdapeter adapter = new productAdapeter(this, R.layout.list_item, productList);
        ListView listView = (ListView) findViewById(R.id.list_view1);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                product product = productList.get(position);
                Toast.makeText(ProductOrder.this, product.getProduct_name(), Toast.LENGTH_SHORT).show();
            }
        });
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
                this.finish();
                Intent intent3 = new Intent(this, MyInfomation.class);
                startActivity(intent3);
                break;
            case R.id.button4:
                initProduct2();
                initList();
                break;
            default:
                break;
        }
    }
    private void initProduct2()
    {
        productList.clear();
        String search_bar=search_bar_text.getText().toString();
        if (search_bar=="" || search_bar==null)
        {
            initProduct1();
            return;
        }
        search_bar_text.setText(search_bar);
        //调用DBOpenHelper （mydata.db是创建的数据库的名称）
        DBOpenHelper helper = new DBOpenHelper(this, "mydata.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c;
        if (the_user_type == 1) {
            product title = new product("名称","价格", 0, 0, "商家信息");
            productList.add(title);
            c = db.rawQuery("select * from orderData where userId = ?",new String[]{""+the_user_id});
            //如果有查询到数据
            if (c != null && c.getCount() >= 1) {
                while (c.moveToNext()) {
                    int goods_id = c.getInt(c.getColumnIndex("goodsId"));
                    Cursor sub_c = db.rawQuery("select * from goodsData where id=?  and title like ? ", new String[]{""+goods_id, "%"+search_bar+"%"});
                    if (sub_c != null && sub_c.getCount() >= 1) {
                        while (sub_c.moveToNext()) {
                            Cursor user_c= db.rawQuery("select * from salesData where goodsId=?", new String[]{""+goods_id});
                            user_c.moveToNext();
                            int user_id = user_c.getInt(user_c.getColumnIndex("userId"));
                            Cursor two_sub_c = db.rawQuery("select * from userInfo where id=?", new String[]{""+user_id});
                            two_sub_c.moveToNext();
                            product drinks = new product(
                                    sub_c.getString(sub_c.getColumnIndex("title")),
                                    sub_c.getString(sub_c.getColumnIndex("price")),
                                    sub_c.getInt(sub_c.getColumnIndex("id")),
                                    sub_c.getInt(sub_c.getColumnIndex("userId")),
                                    two_sub_c.getString(two_sub_c.getColumnIndex("userName")));
                            productList.add(drinks);
                        }
                    }
                }
            }
        } else {
            product title = new product("名称","价格", 0, 0, "买家信息");
            productList.add(title);
            c = db.rawQuery("select * from salesData where userId = ?",new String[]{""+the_user_id});
            //如果有查询到数据
            if (c != null && c.getCount() >= 1) {
                while (c.moveToNext()) {
                    int goods_id = c.getInt(c.getColumnIndex("goodsId"));
                    Cursor sub_c = db.rawQuery("select * from goodsData  where id=? and title like ? ", new String[]{""+goods_id,"%"+search_bar+"%"});
                    if (sub_c != null && sub_c.getCount() >= 1) {
                        while (sub_c.moveToNext()) {
                            Cursor user_c= db.rawQuery("select * from orderData where goodsId=?", new String[]{""+goods_id});
                            user_c.moveToNext();
                            int user_id = user_c.getInt(user_c.getColumnIndex("userId"));
                            Cursor two_sub_c = db.rawQuery("select * from userInfo where id=?", new String[]{""+user_id});
                            two_sub_c.moveToNext();
                            product drinks = new product(
                                    sub_c.getString(sub_c.getColumnIndex("title")),
                                    sub_c.getString(sub_c.getColumnIndex("price")),
                                    sub_c.getInt(sub_c.getColumnIndex("id")),
                                    sub_c.getInt(sub_c.getColumnIndex("userId")),
                                    two_sub_c.getString(two_sub_c.getColumnIndex("userName")));
                            productList.add(drinks);
                        }
                    }
                }
            }
        }
    }
    //这里获取数据 改的时候改下这里
    private void initProduct1() {
        productList.clear();
        //调用DBOpenHelper （mydata.db是创建的数据库的名称）
        DBOpenHelper helper = new DBOpenHelper(this, "mydata.db", null, 1);
        SQLiteDatabase db = helper.getWritableDatabase();
        Cursor c;
        if (the_user_type == 1) {
            product title = new product("名称","价格", 0, 0, "商家信息");
            productList.add(title);
            c = db.rawQuery("select * from orderData where userId = ?",new String[]{""+the_user_id});
            //如果有查询到数据
            if (c != null && c.getCount() >= 1) {
                //可以把查询出来的值打印出来在后台显示/查看
                String[] cols = c.getColumnNames();
                while (c.moveToNext()) {
                    int goods_id = c.getInt(c.getColumnIndex("goodsId"));
                    Cursor sub_c = db.rawQuery("select * from goodsData where id=?", new String[]{""+goods_id});
                    if (sub_c != null && sub_c.getCount() >= 1) {
                        while (sub_c.moveToNext()) {
                            Cursor user_c= db.rawQuery("select * from salesData where goodsId=?", new String[]{""+goods_id});
                            user_c.moveToNext();
                            int user_id = user_c.getInt(user_c.getColumnIndex("userId"));
                            Cursor two_sub_c = db.rawQuery("select * from userInfo where id=?", new String[]{""+user_id});
                            if (two_sub_c == null){
                                continue;
                            }
                            two_sub_c.moveToNext();

                            product drinks = new product(
                                    sub_c.getString(sub_c.getColumnIndex("title")),
                                    sub_c.getString(sub_c.getColumnIndex("price")),
                                    sub_c.getInt(sub_c.getColumnIndex("id")),
                                    sub_c.getInt(sub_c.getColumnIndex("userId")),
                                    two_sub_c.getString(two_sub_c.getColumnIndex("userName")));
                            productList.add(drinks);
                        }
                    }
                }
            }
        }else {
            product title = new product("名称","价格", 0, 0, "买家信息");
            productList.add(title);
            c = db.rawQuery("select * from salesData where userId = ?",new String[]{""+the_user_id});
            //如果有查询到数据
            if (c != null && c.getCount() >= 1) {
                //可以把查询出来的值打印出来在后台显示/查看
                String[] cols = c.getColumnNames();
                while (c.moveToNext()) {
                    int goods_id = c.getInt(c.getColumnIndex("goodsId"));
                    Cursor sub_c = db.rawQuery("select * from goodsData where id=?", new String[]{""+goods_id});
                    if (sub_c != null && sub_c.getCount() >= 1) {
                        while (sub_c.moveToNext()) {
                            Cursor user_c= db.rawQuery("select * from orderData where goodsId=?", new String[]{""+goods_id});
                            user_c.moveToNext();
                            int user_id = user_c.getInt(user_c.getColumnIndex("userId"));
                            Cursor two_sub_c = db.rawQuery("select * from userInfo where id=?", new String[]{""+user_id});
                            two_sub_c.moveToNext();
                            product drinks = new product(
                                    sub_c.getString(sub_c.getColumnIndex("title")),
                                    sub_c.getString(sub_c.getColumnIndex("price")),
                                    sub_c.getInt(sub_c.getColumnIndex("id")),
                                    sub_c.getInt(sub_c.getColumnIndex("userId")),
                                    two_sub_c.getString(two_sub_c.getColumnIndex("userName")));
                            productList.add(drinks);
                        }
                    }
                }
            }
        }
    }
}