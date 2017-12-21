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

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import hznu.edu.cn.entity.CpInfo;
import hznu.edu.cn.entity.User;

public class YgddActy extends AppCompatActivity {

    List<CpInfo> list;
    private ArrayAdapter adapter;
    private ArrayList<String> strings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ygdd_acty);

        final ListView view = (ListView) findViewById(R.id.lv_cpinfo);
        BmobQuery<CpInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("userid", User.getCurrentUser().getUsername());
        query.findObjects(new FindListener<CpInfo>() {
            @Override
            public void done(List<CpInfo> list, BmobException e) {

                if (e != null) {
                    Toast.makeText(YgddActy.this, "已购订单列表查询失败，请检查网络！", Toast.LENGTH_SHORT).show();
                } else {
                    YgddActy.this.list = list;
                    strings = new ArrayList<String>();
                    for (int i = 0; i < list.size(); i++) {
                        strings.add(list.get(i).getName() + " 开往" + list.get(i).getEnd());
                    }
                    adapter = new ArrayAdapter<String>(YgddActy.this, android.R.layout.simple_list_item_1, strings);
                    view.setAdapter(adapter);
                }


            }
        });
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view1, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(YgddActy.this);
                builder.setTitle("车票详情");
                builder.setMessage(YgddActy.this.list.get(position).cpInfo());
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CpInfo info = YgddActy.this.list.get(position);
                        CpInfo info1 = new CpInfo();
                        info1.setObjectId(info.getObjectId());
                        info1.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    Toast.makeText(YgddActy.this, "success!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(YgddActy.this, e.getErrorCode() + "", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                YgddActy.this.strings.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).

                        setNegativeButton("确定", null)
                        .

                                create().

                        show();
            }
        });

    }

}
