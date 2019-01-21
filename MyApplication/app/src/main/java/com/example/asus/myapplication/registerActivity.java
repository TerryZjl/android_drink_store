package com.example.asus.myapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class registerActivity extends AppCompatActivity implements View.OnClickListener {
    private RadioButton radio_btn3; //用户类型按钮
    private RadioButton radio_btn4; //用户类型按钮
    private Button btn_register;//注册按钮
    //用户名，密码，再次输入的密码的控件
    private EditText et_user_name,et_psw,et_psw_again;
    //用户名，密码，再次输入的密码的控件的获取值
    private String userName,psw,pswAgain;
    private int userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }
    private void init()
    {
        btn_register=findViewById(R.id.btn_register);
        et_user_name = findViewById(R.id.et_user_name);
        et_psw = findViewById(R.id.et_psw);
        et_psw_again = findViewById(R.id.et_psw_again);
        //注册按钮
        btn_register.setOnClickListener(this);
        radio_btn3 = findViewById(R.id.radioButton3);
        radio_btn3.setOnClickListener(this);
        radio_btn4 = findViewById(R.id.radioButton4);
        radio_btn4.setOnClickListener(this);
    }
    /**
     * 获取控件中的字符串
     */
    private void getEditString(){
        userName=et_user_name.getText().toString();
        psw = et_psw.getText().toString();
        pswAgain = et_psw_again.getText().toString();
    }
    /**
     * 保存账号和密码到SharedPreferences中SharedPreferences
     */
    private void saveRegisterInfo(String userName,String psw){
        if(TextUtils.isEmpty(userName)){
            Toast.makeText(registerActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(psw)){
            Toast.makeText(registerActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(pswAgain)){
            Toast.makeText(registerActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
            return;
        }else if(!psw.equals(pswAgain)){
            Toast.makeText(registerActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
            return;
        }else {
            //调用DBOpenHelper
            DBOpenHelper helper = new DBOpenHelper(this,"mydata.db",null,1);
            SQLiteDatabase db = helper.getWritableDatabase();
            //根据画面上输入的账号去数据库中进行查询
            Cursor c = db.query("userInfo",null,"userName=?",new String[]{userName},null,null,null);
            //如果有查询到数据，则说明账号已存在
            if(c!=null && c.getCount() >= 1){
                Toast.makeText(this, "该用户已存在", Toast.LENGTH_SHORT).show();
                c.close();
            }
            //如果没有查询到数据，则往数据库中insert一笔数据
            else{
                //insert data
                ContentValues values= new ContentValues();
                values.put("userType",userType);
                values.put("userName",userName);
                values.put("pwd",psw);
                long rowid = db.insert("userInfo",null,values);
                Toast.makeText(this, "注册成功", Toast.LENGTH_SHORT).show();//提示信息
                this.finish();
            }
            db.close();
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.radioButton3:
                userType = 1;
                Log.i("user_type","1");
                break;
            case R.id.radioButton4:
                userType = 2;
                Log.i("user_type","2");
                break;
            case R.id.btn_register:
                getEditString();
                saveRegisterInfo(userName,psw);
                break;
            default:
                break;
        }
    }
}
