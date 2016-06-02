package com.example.administrator.myapplication.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.constans.Constans;
import com.example.administrator.myapplication.entity.ChannelItem;

import org.xutils.view.annotation.Event;
import org.xutils.x;

import java.util.List;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * 主页面
 */
public class MainActivity extends AbActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //注册Xutils
        x.view().inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获得
        if (BmobUser.getCurrentUser(this) == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        BmobQuery<ChannelItem> query = new BmobQuery<>();
        //查询数据
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(this).getUsername());
        //返回50条数据，如果不加上这条语句，默认返回10条数据。
        query.setLimit(50);
        //执行查询方法
        query.findObjects(this, new FindListener<ChannelItem>() {
            @Override
            public void onSuccess(List<ChannelItem> object) {
                Constans.user_typs = object;
            }

            @Override
            public void onError(int code, String msg) {
                Toast.makeText(MainActivity.this, code + msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Event(value = {
            R.id.tv_accounts,//我的记账
            R.id.tv_Report,//月度报表
            R.id.tv_statistics,//消费统计
            R.id.tv_type,//类别维护
            R.id.tv_yusuan,//消费预算
            R.id.tv_userinfo//个人信息
    }, type = View.OnClickListener.class)
    private void onClick(View view) {
        TextView textView = (TextView) view;
        textView.setTextColor(getRandomColor());
        switch (view.getId()) {
            case R.id.tv_accounts://我的记账
                startActivity(new Intent(this, MyBookActivity.class));
                break;
            case R.id.tv_Report://月度报表
                if (Constans.user_typs == null || Constans.user_typs.size() == 0) {
                    Toast.makeText(MainActivity.this, "请链接网络！", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(this, MonthlyActivity.class));
                break;
            case R.id.tv_statistics://消费统计
                if (Constans.user_typs == null || Constans.user_typs.size() == 0) {
                    Toast.makeText(MainActivity.this, "请链接网络！", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(this, ConsumptionActivity.class));
                break;
            case R.id.tv_type://类别维护
                startActivity(new Intent(this, NavigationActivity.class));
                break;
            case R.id.tv_yusuan://消费预算
                if (Constans.user_typs == null || Constans.user_typs.size() == 0) {
                    Toast.makeText(MainActivity.this, "请链接网络！", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(new Intent(this, BudgetActivity.class));
                break;
            case R.id.tv_userinfo://个人信息
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
        }
    }


}
