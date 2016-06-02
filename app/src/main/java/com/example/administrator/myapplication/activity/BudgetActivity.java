package com.example.administrator.myapplication.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.adapter.BudAdapter;
import com.example.administrator.myapplication.constans.Constans;
import com.example.administrator.myapplication.constans.TimeUtils;
import com.example.administrator.myapplication.entity.Budget;
import com.example.administrator.myapplication.entity.MyBook;
import com.example.administrator.myapplication.utils.L;

import org.json.JSONArray;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class BudgetActivity extends AbActivity {
    @ViewInject(R.id.et_money)
    EditText et_money;
    @ViewInject(R.id.et_remark)
    EditText et_remark;
    @ViewInject(R.id.sp_type)
    Spinner sp_type;
    private SpinnerAdapter adapter;
    @ViewInject(R.id.lv_mybooklist)
    ListView lv_mybooklist;
    private ArrayList<String> data;
    private ArrayAdapter<String> budAdapter;
    private List<Budget> buigets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget);
        x.view().inject(this);
        data = new ArrayList<>();
        for (int i = 0; i < Constans.user_typs.size(); i++) {
            if (Constans.user_typs.get(i).getNAME().contains("[支]")) {
                data.add(Constans.user_typs.get(i).getNAME().replace("[支]", ""));
            }
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);
        buigets = new ArrayList<>();
        budAdapter = new ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, buigets);
        lv_mybooklist.setAdapter(budAdapter);
        lv_mybooklist.setOnCreateContextMenuListener(this);
        //先显示出来我的之前预算信息
        showOldBudget();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("操作");
        menu.add(0, 0, 0, "删除");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        //删除
        buigets.get(info.position).delete(this);
        buigets.remove(info.position);
        budAdapter.notifyDataSetChanged();
        return super.onContextItemSelected(item);
    }

    /**
     * 显示已经添加的预算
     */
    private void showOldBudget() {
        BmobQuery<Budget> query = new BmobQuery();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(this).getUsername());
        query.findObjects(this, new FindListener<Budget>() {
            @Override
            public void onSuccess(List<Budget> list) {
                if (list == null || list.size() == 0) {
                    Toast.makeText(BudgetActivity.this, "尚未添加预算", Toast.LENGTH_SHORT).show();
                    return;
                }
                String time = TimeUtils.getYearMonth(Long.valueOf(list.get(0).getTime()));
                String currentTime = TimeUtils.getYearMonth(System.currentTimeMillis());
                if (currentTime.equals(time)) {//是这个月的
                    //更新已消费的数据
                    updateHasMoney(list);
                    //显示出来
                    showToView(list);
                } else {        //上个月的，清除
                    //不显示，并且将这些数据删除
                    removeAll(list);
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(BudgetActivity.this, "网络连接错误，错误码：" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateHasMoney(List<Budget> list) {
        // 算出来每个预算已经花了多少钱，并且更新数据表
        for (int i = 0; i < list.size(); i++) {
            //算出每个分类已经花了多少钱
            final Budget budget = list.get(i);
            BmobQuery<MyBook> query = new BmobQuery<>();
            query.addWhereEqualTo("username", budget.getUsername());
            query.addWhereEqualTo("type", budget.getType());
            query.findObjects(this, new FindListener<MyBook>() {
                @Override
                public void onSuccess(List<MyBook> list) {
                    int sum = 0;
                    for (int i = 0; i < list.size(); i++) {
                        sum += Integer.valueOf(list.get(i).getMoney());
                    }
                    budget.setHasMoney(String.valueOf(sum));
                    budget.setValue("hasMoney", String.valueOf(sum));
                    budget.update(BudgetActivity.this, budget.getObjectId(), null);
                    budAdapter.notifyDataSetChanged();
                }

                @Override
                public void onError(int i, String s) {

                }
            });
//            query.sum(new String[]{"money"});
//            query.findStatistics(this, MyBook.class, new FindStatisticsListener() {
//                @Override
//                public void onSuccess(Object o) {
//                    JSONArray ary = (JSONArray) o;
//                    L.d("updateHasMoney" + ary.toString());
//                }
//
//                @Override
//                public void onFailure(int i, String s) {
//
//                }
//            });
        }
    }

    /**
     * 将这些数据显示在控件上
     *
     * @param list
     */
    private void showToView(List<Budget> list) {
        buigets.clear();
        buigets.addAll(list);
        budAdapter.notifyDataSetChanged();
    }

    /**
     * 从服务器将这些东西删除
     *
     * @param list
     */
    private void removeAll(List<Budget> list) {
        List<BmobObject> objects = new ArrayList<>();
        objects.addAll(list);
        new BmobObject().deleteBatch(this, objects, null);
    }

    /**
     * 添加按钮被点击
     */
    @Event(value = R.id.btn_addbook, type = View.OnClickListener.class)
    private void btn_onclick(View view) {
        //弹出输入框
        CustomDialog();
    }

    /**
     * 点击出现弹框
     */
    public void CustomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //设置相关参数
        builder.setTitle("添加预算");
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_book, null);
        final Spinner spinner = (Spinner) view.findViewById(R.id.sp_type);
        EditText et_remark = (EditText) view.findViewById(R.id.et_remark);
        et_remark.setVisibility(View.GONE);
        spinner.setAdapter(adapter);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditText et_money = (EditText) view.findViewById(R.id.et_money);
                String type = data.get(spinner.getSelectedItemPosition()) + "[支]";
                String money = et_money.getText().toString();
                if (isNullOrEmpty(money)) {
                    Toast.makeText(BudgetActivity.this, "金额不可以为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Budget book = new Budget();
                book.setType(type);
                book.setBudMoney(money);
                book.setHasMoney("0");
                book.setUsername(BmobUser.getCurrentUser(BudgetActivity.this).getUsername());
                book.setTime(String.valueOf(System.currentTimeMillis()));
                saveToNet(book);
            }
        });

        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    /**
     * 将一条数据保存上去
     *
     * @param book
     */
    private void saveToNet(final Budget book) {
        final BmobQuery<Budget> query = new BmobQuery<>();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(this).getUsername());
        query.addWhereEqualTo("type", book.getType());
        query.findObjects(this, new FindListener<Budget>() {
            @Override
            public void onSuccess(List<Budget> list) {
                if (list.size() == 0) {
                    book.save(BudgetActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(BudgetActivity.this, "添加成功！", Toast.LENGTH_SHORT).show();
                            showOldBudget();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            Toast.makeText(BudgetActivity.this, "添加失败！错误码" + s, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(BudgetActivity.this, "添加失败！这个分类已经添加过预算了", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(BudgetActivity.this, "添加失败！错误码" + s, Toast.LENGTH_SHORT).show();
            }
        });

    }
}
