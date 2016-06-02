package com.example.administrator.myapplication.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
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
import com.example.administrator.myapplication.adapter.BookAdapter;
import com.example.administrator.myapplication.constans.Constans;
import com.example.administrator.myapplication.entity.Budget;
import com.example.administrator.myapplication.entity.MyBook;
import com.example.administrator.myapplication.utils.L;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 记账本界面
 */
public class MyBookActivity extends AbActivity {

    @ViewInject(R.id.et_money)
    EditText et_money;
    @ViewInject(R.id.et_remark)
    EditText et_remark;
    @ViewInject(R.id.sp_type)
    Spinner sp_type;
    private SpinnerAdapter adapter;
    @ViewInject(R.id.lv_mybooklist)
    ListView lv_mybooklist;
    private BookAdapter bookadapter;
    private String[] data;
    private List<MyBook> books;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_book);
        x.view().inject(this);
        findMyBook();
        data = new String[Constans.user_typs.size()];
        for (int i = 0; i < data.length; i++) {
            data[i] = Constans.user_typs.get(i).getNAME();
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);
        books = new ArrayList<MyBook>();
        lv_mybooklist.setOnCreateContextMenuListener(this);
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
        builder.setTitle("分类名称");
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_book, null);
        final Spinner spinner = (Spinner) view.findViewById(R.id.sp_type);
        spinner.setAdapter(adapter);
        builder.setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                EditText et_money = (EditText) view.findViewById(R.id.et_money);
                EditText et_remark = (EditText) view.findViewById(R.id.et_remark);
                String type = data[spinner.getSelectedItemPosition()];
                String money = et_money.getText().toString();
                String remark = et_remark.getText().toString();
                if (isNullOrEmpty(money)) {
                    Toast.makeText(MyBookActivity.this, "金额不可以为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                MyBook book = new MyBook();
                book.setRemark(remark);
                book.setType(type);
                book.setMoney(money);
                book.setUsername(BmobUser.getCurrentUser(MyBookActivity.this).getUsername());
                book.setTime(String.valueOf(System.currentTimeMillis()));
                saveToNet(book);
            }
        });

        builder.setNegativeButton("取消", null);
        builder.create().show();
    }

    private void saveToNet(final MyBook book) {
        //在Budget表上更新数据
        //1.先查出来
        BmobQuery<Budget> query = new BmobQuery<>();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(this).getUsername());
        query.addWhereEqualTo("type", book.getType());
        query.findObjects(this, new FindListener<Budget>() {
            @Override
            public void onSuccess(List<Budget> list) {
                if (list.size() == 0) {
                    return;
                }
                Budget budget = list.get(0);
                String oldMoney = budget.getHasMoney();
                int some = Integer.valueOf(oldMoney) + Integer.valueOf(book.getMoney());
                //超过90%
                if (some > 0.9 * Integer.valueOf(budget.getBudMoney())) {
                    dialog(budget.getType());
                }
                budget.setHasMoney(String.valueOf(some));
                budget.update(MyBookActivity.this);
            }

            @Override
            public void onError(int i, String s) {
                L.d("更新数据成功");
            }
        });
        //在MyBook表上加数据
        book.save(MyBookActivity.this, new SaveListener() {
            @Override
            public void onSuccess() {
                findMyBook();
            }

            @Override
            public void onFailure(int i, String s) {
                Toast.makeText(MyBookActivity.this, "请检查网络！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("操作");
        menu.add(0, 0, 0, "删除");
        menu.add(0, 1, 1, "编辑");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case 0:
                final MyBook book = new MyBook();
                book.setObjectId(books.get(info.position).getObjectId());
                book.delete(this);
                books.remove(info.position);
                bookadapter.notifyDataSetChanged();
                break;
            case 1:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                //设置相关参数
                builder.setTitle("分类名称");
                final View view = LayoutInflater.from(this).inflate(R.layout.dialog_book, null);
                final Spinner spinner = (Spinner) view.findViewById(R.id.sp_type);
                final EditText et_money = (EditText) view.findViewById(R.id.et_money);
                final EditText et_remark = (EditText) view.findViewById(R.id.et_remark);
                et_money.setText(books.get(info.position).getMoney());
                et_remark.setText(books.get(info.position).getRemark());
                spinner.setVisibility(View.GONE);
                //  spinner.setAdapter(adapter);
                builder.setView(view);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //   String type = data[spinner.getSelectedItemPosition()];
                        String money = et_money.getText().toString();
                        String remark = et_remark.getText().toString();
                        if (isNullOrEmpty(money)) {
                            Toast.makeText(MyBookActivity.this, "金额不可以为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //   MyBook book = new MyBook(type, money);
                        books.get(info.position).setRemark(remark);
                        books.get(info.position).setMoney(money);
                        MyBook gameScore = new MyBook();
                        gameScore.setValue("remark", remark);
                        gameScore.setValue("money", money);
                        gameScore.update(MyBookActivity.this, books.get(info.position).getObjectId(), null);
                        //     books.get(info.position).setUsername(BmobUser.getCurrentUser(MyBookActivity.this).getUsername());
                        //   books.get(info.position).setTime(String.valueOf(System.currentTimeMillis()));

                    }
                });

                builder.setNegativeButton("取消", null);
                builder.create().show();
                break;

            default:
                break;
        }

        return super.onContextItemSelected(item);
    }

    protected void dialog(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("[" + type + "]已经超出预算，宝宝不要再多花钱了,少花一块钱就少几分剁手的危险！");
        builder.setTitle("提示");
        builder.setPositiveButton("宝宝知道了", null);
        builder.create().show();
    }

    /**
     * 获取服务器数据
     */
    private void findMyBook() {
        BmobQuery<MyBook> query = new BmobQuery<>();
        query.setLimit(100);
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(this).getUsername());
        query.findObjects(this, new FindListener<MyBook>() {
            @Override
            public void onSuccess(List<MyBook> list) {
                books.clear();
                books.addAll(list);
                bookadapter = new BookAdapter(MyBookActivity.this, books);
                lv_mybooklist.setAdapter(bookadapter);
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(MyBookActivity.this, "获取服务器数据" + s, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
