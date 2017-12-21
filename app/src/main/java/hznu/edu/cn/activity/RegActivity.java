package hznu.edu.cn.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.administrator.myapplication.R;

import cn.bmob.v3.exception.BmobException;
import hznu.edu.cn.entity.User;
import hznu.edu.cn.utils.L;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.SaveListener;

/**
 * 注册界面
 */
public class RegActivity extends AbActivity {
    @ViewInject(R.id.email)
    EditText userName;
    @ViewInject(R.id.password)
    EditText passWoid;
    @ViewInject(R.id.password_agarn)
    EditText passAgain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        x.view().inject(this);
    }

    /**
     * 点击注册
     */
    @Event(value = R.id.email_regst_in_button, type = View.OnClickListener.class)
    private void regist(View view) {
        String pass = passWoid.getText().toString().trim();
        String again = passAgain.getText().toString().trim();
        String username = userName.getText().toString().trim();
        if (isNullOrEmpty(pass, again, username)) {
            Toast.makeText(RegActivity.this, "请不要输入空的值", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!pass.equals(again)) {
            Toast.makeText(RegActivity.this, "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        //注册用户
        User bu = new User();
        bu.setUsername(username);
        bu.setPassword(pass);
        bu.setCcr(new ArrayList<ContactsNumber>());
        //注意：不能用save方法进行注册
        bu.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {

                if (e == null) {
                    Toast.makeText(RegActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                    //通过BmobUser.getCurrentUser(context)方法获取登录成功后的本地用户信息
                    //返回
                    finish();
                } else {

                    Toast.makeText(RegActivity.this, "注册失败," + e.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }

        });
    }
}
