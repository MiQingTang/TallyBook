package hznu.edu.cn.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.myapplication.R;

import org.xutils.view.annotation.Event;
import org.xutils.x;

import cn.bmob.v3.BmobUser;

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
        if (BmobUser.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }
    }

    @Event(value = {
            R.id.tv_ygdd,
            R.id.tv_dp,
            R.id.tv_userinfo
    }, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_ygdd:
                startActivity(new Intent(this, YgddActy.class));
                break;
            case R.id.tv_dp:
                startActivity(new Intent(this, BuyActy.class));
                break;
            case R.id.tv_userinfo:
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
            default:
                break;
        }
    }


}
