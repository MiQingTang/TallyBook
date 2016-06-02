package com.example.administrator.myapplication.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.activity.MyBookActivity;
import com.example.administrator.myapplication.constans.TimeUtils;
import com.example.administrator.myapplication.entity.MyBook;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.sql.Time;
import java.util.List;

/**
 * Created by Administrator on 2016/5/6.
 */
public class BookAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<MyBook> myBooks;

    public BookAdapter(Activity myBookActivity, List<MyBook> list) {
        inflater = LayoutInflater.from(myBookActivity);
        myBooks = list;
    }

    @Override
    public int getCount() {
        if (myBooks == null) {
            return 0;
        }
        return myBooks.size();
    }

    @Override
    public Object getItem(int position) {
        return getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHoulder houlder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_book, parent, false);
            houlder = new ViewHoulder(convertView);
            convertView.setTag(houlder);
        } else {
            houlder = (ViewHoulder) convertView.getTag();
        }
        houlder.tv_howmoney.setText("金额：" + myBooks.get(position).getMoney());
        houlder.tv_remark.setText("备注：" + myBooks.get(position).getRemark());
        houlder.tv_type.setText("类型：" + myBooks.get(position).getType());
        String time = TimeUtils.getYearMonthDay(Long.valueOf(myBooks.get(position).getTime()));
        houlder.tv_time.setText("时间：" + time);
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
        @ViewInject(R.id.tv_time)
        TextView tv_time;

        public ViewHoulder(View view) {
            x.view().inject(this, view);
        }
    }
}
