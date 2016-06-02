package com.example.administrator.myapplication.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.utils.L;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;


import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * 一个登录屏幕,提供通过电子邮件/密码登录。
 */
public class LoginActivity extends AbActivity {

    // UI references.
    @ViewInject(R.id.email)
    AutoCompleteTextView username;
    @ViewInject(R.id.password)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.view().inject(this);
    }

    @Event(value = R.id.email_sign_in_button, type = View.OnClickListener.class)
    private void login(View view) {
        String user = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if (isNullOrEmpty(user, pass)) {
            Toast.makeText(LoginActivity.this, "用户或密码为空", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobUser bu2 = new BmobUser();
        bu2.setUsername(user);
        bu2.setPassword(pass);
        bu2.login(LoginActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                //通过BmobUser user = BmobUser.getCurrentUser(context)获取登录成功后的本地用户信息
                //如果是自定义用户对象MyUser，可通过MyUser user = BmobUser.getCurrentUser(context,MyUser.class)获取自定义用户信息
                //跳转到Mainactivity（主页面）
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                L.d("登录:code=" + code + "msg=" + msg);
                if (code == 101) {
                    msg = "用户名或密码错误！";
                }
                Toast.makeText(LoginActivity.this, "登录失败," + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 注册按钮被点击，这里使用了Xutils注解
     *
     * @param view
     */
    @Event(value = R.id.tv_regst, type = View.OnClickListener.class)
    private void onRegsterClick(View view) {
        //跳转到注册界面
        Intent intent = new Intent(this, RegActivity.class);
        startActivity(intent);
    }


}

