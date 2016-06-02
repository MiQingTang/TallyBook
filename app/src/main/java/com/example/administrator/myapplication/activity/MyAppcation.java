package com.example.administrator.myapplication.activity;

import android.app.Application;

import org.xutils.BuildConfig;
import org.xutils.x;

import cn.bmob.v3.Bmob;

/**
 * Created by miser on 2016/5/4.
 */
public class MyAppcation extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化  SDK
        Bmob.initialize(this, "5131fc70552ed08b8e3ff978a3420761");
        x.Ext.init(this);
        // 是否输出debug日志, 开启debug会影响性能.
        x.Ext.setDebug(BuildConfig.DEBUG);
    }
}
