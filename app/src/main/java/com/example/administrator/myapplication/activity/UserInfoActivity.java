package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.utils.L;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 用户信息界面
 */
public class UserInfoActivity extends AbActivity {
    @ViewInject(R.id.newpassword)
    EditText newpass;
    @ViewInject(R.id.againpassword)
    EditText againpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        x.view().inject(this);
    }

    @Event(value = {
            R.id.button_update_password,
            R.id.button_exit},
            type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_exit:
                exitUser();
                break;
            case R.id.button_update_password:
                updatePass();
                break;
        }

    }

    private void exitUser() {
        BmobUser.logOut(this);   //清除缓存用户对象
        finish();
    }

    private void updatePass() {
        String npass = newpass.getText().toString();
        String apass = againpass.getText().toString();
        if (isNullOrEmpty(npass, apass)) {
            Toast.makeText(UserInfoActivity.this, "请不要输入空值！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!npass.equals(apass)) {
            Toast.makeText(UserInfoActivity.this, "两次的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobUser bmobUser = BmobUser.getCurrentUser(this);
        bmobUser.setPassword(npass);
        bmobUser.update(this, new UpdateListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(UserInfoActivity.this, "更新用户信息成功", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(int code, String msg) {
                Toast.makeText(UserInfoActivity.this, "更新用户信息失败" + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
