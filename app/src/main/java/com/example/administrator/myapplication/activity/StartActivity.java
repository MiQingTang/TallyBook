package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.myapplication.R;

import cn.bmob.v3.BmobUser;

/**
 * 启动界面
 */
public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        setContentView(R.layout.activity_start);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent;
                //判断需不需要登录
                if (BmobUser.getCurrentUser(StartActivity.this) == null) {
                    intent = new Intent(StartActivity.this, LoginActivity.class);
                } else {
                    intent = new Intent(StartActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
            //等待时间
        }, 1500);
    }
}
