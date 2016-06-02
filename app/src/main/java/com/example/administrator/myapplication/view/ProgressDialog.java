package com.example.administrator.myapplication.view;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.example.administrator.myapplication.R;


/**
 * Created by Administrator on 2016/4/21.
 */
public class ProgressDialog extends Dialog {

    public ProgressDialog(Context context) {
        this(context, R.style.progress_dialog);
    }

    public ProgressDialog(Context context, int theme) {
        super(context, theme);
        setContentView(R.layout.dialog);//设置显示界面
        setCancelable(false);//不能取消
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);//设置背景
    }

    public void showDialog() {
        super.show();
    }

    /**
     * @param progress 0 ~ 100
     */
    public void updateProgressDialog(String progress) {
        TextView msg = (TextView) findViewById(R.id.tv_loadingmsg);
        msg.setText(progress);
    }

    public void closs() {
        super.dismiss();
    }
}
