package com.example.asus.myapplication;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyInfomation extends Activity implements OnClickListener {
    private LinearLayout llgoods, llFriends, llContacts;
    private Button button4, button6, button5;
    private TextView user_name, user_val;
    private String userName;
    private int userType, the_user_id, the_user_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //loginInfo表示文件名  SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        the_user_id = sp.getInt("userId",0);
        the_user_type = sp.getInt("userType",0);
        if (the_user_type == 2){
            setContentView(R.layout.activity_infomation);
        }else {
            setContentView(R.layout.activity_infomation_common);
        }
        initView();
        setUserInfo();
    }

    private void initView() {
        llgoods = (LinearLayout) findViewById(R.id.llgoods);
        llFriends = (LinearLayout) findViewById(R.id.llFriends);
        llContacts = (LinearLayout) findViewById(R.id.llContacts);
        button5= (Button) findViewById(R.id.button5);
        button5.setOnClickListener(this);
        if (the_user_type == 2) {
            button4 = (Button) findViewById(R.id.button4);
            button4.setOnClickListener(this);
        }
        button6=(Button)findViewById(R.id.button6);
        user_name = (TextView)findViewById(R.id.user_name);
        user_val = (TextView)findViewById(R.id.user_val);
        button6.setOnClickListener(this);
        llgoods.setOnClickListener(this);
        llFriends.setOnClickListener(this);
        llContacts.setOnClickListener(this);
    }

    public void setUserInfo() {
        //mode_private SharedPreferences sp = getSharedPreferences( );
        // "loginInfo", MODE_PRIVATE
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取密码
        userType = sp.getInt("userType",0);
        userName = sp.getString("loginUserName","");
        user_name.setText(userName);
        if (userType == 1)
        {
            user_val.setText("普通用户");
        }else {
            user_val.setText("商家");
        }
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
                this.finish();
                Intent intent3 = new Intent(this, MyInfomation.class);
                startActivity(intent3);
                break;
            case R.id.button4:
                if (the_user_type == 2){
                    this.finish();
                    Intent intent4 = new Intent(this, AddProduct.class);
                    intent4.putExtra("page_title","新增商品");
                    startActivity(intent4);
                }
                break;
            case R.id.button5:
                if (the_user_type == 2) {
                    this.finish();
                    Intent intent5 = new Intent(this, SaleStat.class);
                    intent5.putExtra("page_title", "销售统计");
                    startActivity(intent5);
                } else {
                    this.finish();
                    Intent intent5 = new Intent(this, SaleStat.class);
                    intent5.putExtra("page_title", "购买统计");
                    startActivity(intent5);
                }
                break;
            case R.id.button6:
                SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
                //获取编辑器
                SharedPreferences.Editor editor=sp.edit();
                editor.clear();
                editor.commit();
                this.finish();
                Intent intent6 = new Intent(this, MainActivity.class);
                startActivity(intent6);
                break;
            default:
                break;
        }
    }
}