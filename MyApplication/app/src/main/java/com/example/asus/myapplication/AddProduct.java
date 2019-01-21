package com.example.asus.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddProduct extends Activity implements OnClickListener {
    private Button btn_ok;
    private TextView tv_back, tv_main_title;
    private EditText product_name, product_price, product_detail;
    private int user_id;
    private String price, title, detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_product);

        initView();
    }

    private void initView() {
        btn_ok= (Button) findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(this);
        tv_back = (TextView)findViewById(R.id.tv_back);
        tv_back.setOnClickListener(this);
        tv_main_title = (TextView)findViewById(R.id.tv_main_title);
        Intent intent = getIntent();
        String page_title = intent.getStringExtra("page_title");
        tv_main_title.setText(page_title);
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        user_id = sp.getInt("userId",0);
        product_price = (EditText) findViewById(R.id.product_price);
        product_name = (EditText) findViewById(R.id.product_name);
        product_detail = (EditText) findViewById(R.id.product_detail);
    }

    @Override
    public void onClick(View v) {
        changeTab(v.getId());
    }

    private void changeTab(int id) {
        switch (id) {
            case R.id.btn_ok:
                add_product();
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
    public void add_product(){
        //调用DBOpenHelper
        DBOpenHelper helper = new DBOpenHelper(this,"mydata.db",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();
        //insert data
        ContentValues values= new ContentValues();
        price=product_price.getText().toString();
        title=product_name.getText().toString();
        detail=product_detail.getText().toString();
        values.put("userId",user_id);
        values.put("price",price);
        values.put("title",title);
        values.put("detail",detail);
        long rowid = db.insert("goodsData",null,values);
        this.finish();
        Intent intent1 = new Intent(this, AddProduct.class);
        startActivity(intent1);
        Toast.makeText(this, "新增成功", Toast.LENGTH_SHORT).show();
    }
}