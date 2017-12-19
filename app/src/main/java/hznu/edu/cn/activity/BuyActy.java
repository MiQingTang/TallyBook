package hznu.edu.cn.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import hznu.edu.cn.constans.Cfg;
import hznu.edu.cn.entity.Batch;
import hznu.edu.cn.entity.CpInfo;
import hznu.edu.cn.entity.LCInfo;
import hznu.edu.cn.entity.User;


public class BuyActy extends AppCompatActivity {

    private EditText et_start;
    private EditText et_end;
    private TagFlowLayout id_flowlayout;

    private List<Batch> batch;
    private TagFlowLayout tag_cp;
    List<String> mVals = new ArrayList<String>();
    private TagAdapter tagAdapter = new TagAdapter<String>(mVals) {
        @Override
        public View getView(FlowLayout parent, int position, String s) {
            TextView tv = new TextView(BuyActy.this);
            tv.setText(s);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tv.setBackground(getDrawable(R.drawable.normal_bg));
            }
            return tv;
        }

        @Override
        public void onSelected(int position, View view) {
            super.onSelected(position, view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setBackground(getDrawable(R.drawable.checked_bg));
            }
        }

        @Override
        public void unSelected(int position, View view) {
            super.unSelected(position, view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setBackground(getDrawable(R.drawable.normal_bg));
            }
        }
    };
    private RadioGroup rg_type;
    List<CpInfo> cp_mVals = new ArrayList<CpInfo>();
    private TagAdapter cp_tagAdapter = new TagAdapter<CpInfo>(cp_mVals) {


        @Override
        public View getView(FlowLayout parent, int position, CpInfo cpInfo) {
            TextView tv = new TextView(BuyActy.this);
            tv.setText(cpInfo.toString());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tv.setBackground(getDrawable(R.drawable.normal_bg));
            }
            return tv;
        }

        @Override
        public void onSelected(int position, View view) {
            super.onSelected(position, view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setBackground(getDrawable(R.drawable.checked_bg));
            }
        }

        @Override
        public void unSelected(int position, View view) {
            super.unSelected(position, view);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setBackground(getDrawable(R.drawable.normal_bg));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_acty);
        findview();
        mVals.addAll(User.getCurrentUser(BuyActy.this, User.class).getCcr());
        tagAdapter.notifyDataChanged();
        cp_tagAdapter.notifyDataChanged();

    }

    private void query(String start, String end, String date) {
        RequestParams url = new RequestParams(Cfg.LCInfo);
        url.addQueryStringParameter("start", start);
        url.addQueryStringParameter("end", end);
        url.addQueryStringParameter("date", date);
        Callback.CommonCallback<String> back = new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LCInfo info = new Gson().fromJson(result, LCInfo.class);
                if (info.getError_code() == 0) {

                } else {
                    Toast.makeText(BuyActy.this, "sorry,查询失败，请核对您的起始点是否输入正确，或者手机是否联网！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        };
        x.http().get(url, back);
    }

    private void findview() {
        et_start = (EditText) findViewById(R.id.et_start);
        tag_cp = (TagFlowLayout) findViewById(R.id.textview);
        et_end = (EditText) findViewById(R.id.et_end);
        rg_type = (RadioGroup) findViewById(R.id.rg_type);
        id_flowlayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);
        id_flowlayout.setAdapter(tagAdapter);

        tag_cp.setAdapter(cp_tagAdapter);

    }

    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.button3://查
                RadioButton rb = (RadioButton) findViewById(rg_type.getCheckedRadioButtonId());
                String start = et_start.getText().toString();
                String end = et_end.getText().toString();
                int num = (int) (Math.random() * 1000);
                String type = rb.getText().toString();

                if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end) || TextUtils.isEmpty(type)) {
                    Toast.makeText(this, "输入错误！", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < (int) (Math.random() * 2) + 1; i++) {
                        CpInfo info = new CpInfo(
                                start,
                                end,
                                start + "5:31分出发\n" + end + "19:62到达",
                                "G" + num
                        );
                        info.setType(type);
                        cp_mVals.add(info);
                        cp_tagAdapter.notifyDataChanged();
                    }
                    findViewById(R.id.button4).setEnabled(true);
                }
                break;
            case R.id.button4://订
                String num1 = cp_mVals.get(tag_cp.getSelectedList().iterator().next()).getNum();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("确认订购[" + num1 + "]这一车次的车票吗？")
                        .setTitle("提示")
                        .setNegativeButton("再看看", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                buy();
                            }
                        })
                        .create().show();
                break;
            case R.id.button2://日期
                final Button button = (Button) view;
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day) {
                        String date = year + "-" + (++month) + "-" + day;
                        button.setText(date);

                    }
                };
                int Year;
                int Month;
                int Day;
                Calendar cal = Calendar.getInstance();
                Year = cal.get(Calendar.YEAR);
                Log.i("wxy", "year" + Year);
                Month = cal.get(Calendar.MONTH);
                Day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(this,
                        0, listener, Year, Month, Day);//后边三个参数为显示dialog时默认的日期，月份从0开始，0-11对应1-12个月
                dialog.show();
                break;
            case R.id.textView2://添加乘車人
                AlertDialog.Builder buidler2 = new AlertDialog.Builder(this);
                final EditText edittext = new EditText(this);
                buidler2
                        .setTitle("添加乘车人")
                        .setView(edittext)
                        .setNegativeButton("取消", null)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mVals.add(edittext.getText().toString());
                                tagAdapter.notifyDataChanged();
                                User bmobUser = User.getCurrentUser(BuyActy.this, User.class);
                                bmobUser.getCcr().add(edittext.getText().toString());
                                bmobUser.update(BuyActy.this, new UpdateListener() {
                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(BuyActy.this, "添加成功", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(int code, String msg) {
                                        Toast.makeText(BuyActy.this, "添加失败" + msg, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })
                        .create().show();
                break;
            default:
                break;
        }

    }

    private void buy() {
        for (int i : tag_cp.getSelectedList()) {
            CpInfo info = cp_mVals.get(i);
            for (int j :
                    id_flowlayout.getSelectedList()) {
                info.setMoney("¥" + Math.random() * 300 + 100);
                info.setName(mVals.get(j));
                info.setLoc((Math.random() * 20 + 1) + "车" + (Math.random() * 90 + 1) + "A");
                info.save(BuyActy.this, new SaveListener() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onFailure(int i, String s) {

                    }
                });
            }
        }

        Toast.makeText(BuyActy.this, "正在支付！", Toast.LENGTH_SHORT).show();
        Toast.makeText(BuyActy.this, "您已经订购成功！", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 2000);
    }
}
