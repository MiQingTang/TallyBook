package hznu.edu.cn.activity;

import android.app.DatePickerDialog;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.google.gson.Gson;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.http.request.AssetsRequest;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import hznu.edu.cn.constans.Cfg;
import hznu.edu.cn.entity.Batch;
import hznu.edu.cn.entity.LCInfo;


public class BuyActy extends AppCompatActivity {

    private EditText et_start;
    private EditText et_end;
    private TagFlowLayout id_flowlayout;

    private List<Batch> batch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_acty);
        findview();

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
        et_end = (EditText) findViewById(R.id.et_end);
        id_flowlayout = (TagFlowLayout) findViewById(R.id.id_flowlayout);

        List<String> mVals = new ArrayList<String>();
        mVals.add("123");
        mVals.add("123");
        mVals.add("123");
        mVals.add("123");
        id_flowlayout.setAdapter(new TagAdapter<String>(mVals) {
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
        });
    }

    public void onClick(final View view) {
        switch (view.getId()) {
            case R.id.button3://查
                String start = et_start.getText().toString();
                String end = et_start.getText().toString();
                if (TextUtils.isEmpty(start) || TextUtils.isEmpty(end)) {
                    Toast.makeText(this, "输入错误！", Toast.LENGTH_SHORT).show();
                } else {
                    String resul =
                            "车次：G" + (int) (Math.random() * 1000) + "\n" +
                                    "从" + start + "到" + end + "\n"
                                    + start + "5:31分出发\n"
                                    + end + "19:62到达\n票价(￥):680";
                }
                break;
            case R.id.button4://订
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
            default:
                break;
        }

    }
}
