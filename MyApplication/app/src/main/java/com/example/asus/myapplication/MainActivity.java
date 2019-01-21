package com.example.asus.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.TokenWatcher;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private RadioButton radio_btn3; //用户类型按钮
    private RadioButton radio_btn4; //用户类型按钮
    private Button btn_login;//登录按钮
    private String userName,psw,spPsw, spName;//获取的用户名，密码，本地密码, 本地用户名
    private int user_id;
    private EditText et_user_name,et_psw;//编辑框
    private TextView tv_register,tv_find_psw;//返回键,显示的注册，找回密码
    private int userType, spUserType; //输入的类型，数据库中存的类型


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        checkLogin();
        init();
    }
    public  void checkLogin(){
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取密码
        String isLoginName = sp.getString("loginUserName","");
        if(!TextUtils.isEmpty(isLoginName)) {
            MainActivity.this.finish();
            //跳转到主界面，登录成功的状态传递到 MainActivity 中
            Intent intent2 = new Intent(this, MainInterface.class);
            startActivity(intent2);
        }
    }
    public void init()
    {
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        et_user_name=findViewById(R.id.et_user_name);
        et_psw=findViewById(R.id.et_psw);
        radio_btn3 = findViewById(R.id.radioButton3);
        radio_btn3.setOnClickListener(this);
        radio_btn4 = findViewById(R.id.radioButton4);
        radio_btn4.setOnClickListener(this);
    }
    public void login_fun()
    {
        //开始登录，获取用户名和密码 getText().toString().trim();
        userName=et_user_name.getText().toString().trim();
        psw=et_psw.getText().toString().trim();
        if(TextUtils.isEmpty(userName)){
            Toast.makeText(MainActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
            return;
        }else if(TextUtils.isEmpty(psw)){
            Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }else {
            //调用DBOpenHelper （qianbao.db是创建的数据库的名称）
            DBOpenHelper helper = new DBOpenHelper(this, "mydata.db", null, 1);
            SQLiteDatabase db = helper.getWritableDatabase();
            //根据画面上输入的账号/密码去数据库中进行查询（userInfo是表名）
            Cursor c = db.query("userInfo", null, "userName=? and pwd=?", new String[]{userName, psw}, null, null, null);
            //如果有查询到数据
            if (c != null && c.getCount() >= 1) {
                //可以把查询出来的值打印出来在后台显示/查看
                String[] cols = c.getColumnNames();
                while (c.moveToNext()) {
                    for (String ColumnName : cols) {
                        Log.i("info", ColumnName + ":" + c.getString(c.getColumnIndex(ColumnName)));
                        if (ColumnName.equals("id")){
                            user_id = c.getInt(c.getColumnIndex("id"));
                        }else if(ColumnName.equals("userType")){
                            spUserType = c.getInt(c.getColumnIndex("userType"));
                        }
                    }
                }
                c.close();
                db.close();
                //一致登录成功
                if (userType != spUserType){
                    Toast.makeText(MainActivity.this, "用户类型不匹配", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                //保存登录状态，在界面保存登录的用户名 定义个方法 saveLoginStatus boolean 状态 , userName 用户名;
                saveLoginStatus(true, userName, user_id);
                //销毁登录界面
                MainActivity.this.finish();
                //跳转到主界面，登录成功的状态传递到 MainActivity 中
                Intent intent1 = new Intent(this, MainInterface.class);
                startActivity(intent1);
                return;
            }
            //如果没有查询到数据
            else {
                Toast.makeText(this, "用户名或密码输入错误！", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void saveLoginStatus(boolean status,String userName, int user_id){
        if (user_id != 0)
        {
            Log.i("user_id", "1");
        }
        //saveLoginStatus(true, userName);
        //loginInfo表示文件名  SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        SharedPreferences sp=getSharedPreferences("loginInfo", MODE_PRIVATE);
        //获取编辑器
        SharedPreferences.Editor editor=sp.edit();
        //存入boolean类型的登录状态
        editor.putBoolean("isLogin", status);
        //存入登录状态时的用户名
        editor.putString("loginUserName", userName);
        editor.putInt("userId", user_id);
        editor.putInt("userType", userType);
        //提交修改
        editor.commit();
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
            case R.id.btn_login:
                login_fun();
                break;
            case R.id.tv_register:
                Intent intent2 = new Intent(this, registerActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
