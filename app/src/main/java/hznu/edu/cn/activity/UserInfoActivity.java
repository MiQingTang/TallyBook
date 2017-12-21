package hznu.edu.cn.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import hznu.edu.cn.entity.User;

/**
 * 用户信息界面
 */
public class UserInfoActivity extends AbActivity {
    @ViewInject(R.id.newpassword)
    EditText newpass;
    @ViewInject(R.id.againpassword)
    EditText againpass;
    @ViewInject(R.id.oldpassword)
    EditText oldpassword;
    List<String> mVals = new ArrayList<String>();
    private TagAdapter tagAdapter = new TagAdapter<String>(mVals) {
        @Override
        public View getView(FlowLayout parent, int position, String s) {
            TextView tv = new TextView(UserInfoActivity.this);
            tv.setPadding(10, 10, 10, 10);
            tv.setText(s);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tv.setBackground(getDrawable(R.drawable.normal_bg));
            }
            return tv;
        }

        @Override
        public void onSelected(int position, View view) {
            super.onSelected(position, view);
            mVals.remove(position);
            tagAdapter.notifyDataChanged();
            User bmobUser = User.getCurrentUser(User.class);
            bmobUser.getCcr().remove(position);
            bmobUser.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e != null) {
                        Toast.makeText(UserInfoActivity.this, "请检查网络！" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {

                    }
                }
            });
        }

        @Override
        public void unSelected(int position, View view) {
            super.unSelected(position, view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setBackground(getDrawable(R.drawable.normal_bg));
            }
        }
    };
    private TagFlowLayout id_flowlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        x.view().inject(this);

        mVals.addAll(User.getCurrentUser(User.class).getCcr());
        id_flowlayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        id_flowlayout.setAdapter(tagAdapter);
        tagAdapter.notifyDataChanged();
    }

    @Event(value = {
            R.id.button_update_password,
            R.id.textView2,
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
            case R.id.textView2:
                AlertDialog.Builder buidler2 = new AlertDialog.Builder(this);
                final EditText edittext = new EditText(this);
                buidler2
                        .setTitle("添加乘车人")
                        .setView(edittext)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mVals.add(edittext.getText().toString());
                                tagAdapter.notifyDataChanged();
                                User bmobUser = User.getCurrentUser(User.class);
                                bmobUser.getCcr().add(edittext.getText().toString());
                                bmobUser.update(new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e != null) {
                                            Toast.makeText(UserInfoActivity.this, "添加失败，检查网络！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        })
                        .create().show();
                break;
        }

    }

    private void exitUser() {
        BmobUser.logOut();   //清除缓存用户对象
        finish();
    }

    private void updatePass() {
        String old = oldpassword.getText().toString();
        final String npass = newpass.getText().toString();
        final String apass = againpass.getText().toString();

        if (isNullOrEmpty(npass, apass, old)) {
            Toast.makeText(UserInfoActivity.this, "请不要输入空值！", Toast.LENGTH_SHORT).show();
            return;
        }
        BmobUser bu2 = new BmobUser();
        bu2.setUsername(User.getCurrentUser().getUsername());
        bu2.setPassword(old);
        bu2.login(new SaveListener<Object>() {
            @Override
            public void done(Object o, BmobException e) {
                if (e == null) {
                    if (!npass.equals(apass)) {
                        Toast.makeText(UserInfoActivity.this, "两次的密码不一致", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    bmobUser.setPassword(npass);
                    bmobUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(UserInfoActivity.this, "更新用户信息成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else
                                Toast.makeText(UserInfoActivity.this, "更新用户信息失败", Toast.LENGTH_SHORT).show();

                        }
                    });
                } else {
                    Toast.makeText(UserInfoActivity.this, "sorry!您输入的旧密码错误或网络不畅通！", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
