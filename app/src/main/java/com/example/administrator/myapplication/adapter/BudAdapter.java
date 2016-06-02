package com.example.administrator.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.entity.Budget;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Administrator on 2016/5/10.
 */
public class BudAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<Budget> budgets;

    public BudAdapter(List<Budget> budgets, Context context) {
        this.budgets = budgets;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return budgets.size();
    }

    @Override
    public Object getItem(int position) {
        return budgets.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoulder houlder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_budget, parent, false);
            houlder = new ViewHoulder(convertView);
            convertView.setTag(houlder);
        } else {
            houlder = (ViewHoulder) convertView.getTag();
        }
//        houlder.tv_howmoney.setText("金额：" + myBooks.get(position).getMoney());
//        houlder.tv_remark.setText("备注：" + myBooks.get(position).getRemark());
//        houlder.tv_type.setText("类型：" + myBooks.get(position).getType());
        return convertView;
    }

    /**
     * 控件封装类
     */
    class ViewHoulder {
        @ViewInject(R.id.tv_howmoney)
        TextView tv_howmoney;
        @ViewInject(R.id.tv_type)
        TextView tv_type;
        @ViewInject(R.id.tv_remark)
        TextView tv_remark;

        public ViewHoulder(View view) {
            x.view().inject(this, view);
        }
    }
}
