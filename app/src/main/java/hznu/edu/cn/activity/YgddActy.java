package hznu.edu.cn.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import hznu.edu.cn.entity.CpInfo;

public class YgddActy extends AppCompatActivity {

    List<CpInfo> list;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ygdd_acty);

        final ListView view = (ListView) findViewById(R.id.lv_cpinfo);
        BmobQuery<CpInfo> query = new BmobQuery<>();
        query.findObjects(this, new FindListener<CpInfo>() {
            @Override
            public void onSuccess(List<CpInfo> list) {
                YgddActy.this.list = list;
                List<String> strings = new ArrayList<String>();
                for (int i = 0; i < list.size(); i++) {
                    strings.add(list.get(i).getName() + " 开往" + list.get(i).getEnd());
                }
                adapter = new ArrayAdapter<String>(YgddActy.this, android.R.layout.simple_list_item_1, strings);
                view.setAdapter(adapter);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(YgddActy.this, "已购订单列表查询失败，请检查网络！", Toast.LENGTH_SHORT).show();
            }
        });
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(YgddActy.this);
                builder.setTitle("车票详情");
                builder.setMessage(YgddActy.this.list.get(position).cpInfo());
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        YgddActy.this.list.get(position).delete(YgddActy.this);
                        YgddActy.this.list.remove(position);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("确定", null)
                        .create().show();
            }
        });

    }

}
