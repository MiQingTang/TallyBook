package com.example.administrator.myapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.constans.TimeUtils;
import com.example.administrator.myapplication.entity.MyBook;
import com.example.administrator.myapplication.utils.L;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * 月度报表
 */
public class MonthlyActivity extends AppCompatActivity {
    @ViewInject(R.id.lv_monthy)
    ListView lv_monthy;
    ArrayAdapter<String> adapter;
    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);
        x.view().inject(this);
        data = new ArrayList<>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, data);
        lv_monthy.setAdapter(adapter);
        //查询用户所有的消费记录
        BmobQuery<MyBook> query = new BmobQuery<>();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(this).getUsername());
        query.order("time");
        query.findObjects(this, new FindListener<MyBook>() {
            @Override
            public void onSuccess(List<MyBook> list) {
                //[年月]消费数量
                MyBook lastbook = new MyBook();
                lastbook.setType("");
                lastbook.setMoney("0");
                lastbook.setTime(String.valueOf(0));
                list.add(lastbook);
                //月消费金额sumMonth
                int xfMonth = 0;
                //月收入金额
                int srMonth = 0;
                //上一个月份
                String lastMonth = TimeUtils.getYearMonth(Long.valueOf(list.get(0).getTime()));
                for (int i = 0; i < list.size(); i++) {
                    MyBook book = list.get(i);
                    String time = TimeUtils.getYearMonth(Long.valueOf(book.getTime()));
                    String money = book.getMoney();
                    if (book.getType().contains("[收]")) {
                        srMonth += Integer.valueOf(money);
                    } else {
                        xfMonth += Integer.valueOf(money);
                    }
                    if (!lastMonth.equals(time)) {
                        data.add("[" +
                                lastMonth +
                                "]" +
                                "共支出" +
                                xfMonth);
                        xfMonth = 0;
                        data.add("[" +
                                lastMonth +
                                "]" +
                                "共收入" +
                                srMonth);
                        srMonth = 0;
                        lastMonth = time;
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MonthlyActivity.this, "请连接网络！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
