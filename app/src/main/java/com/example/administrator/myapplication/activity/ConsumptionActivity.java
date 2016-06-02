package com.example.administrator.myapplication.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.constans.Constans;
import com.example.administrator.myapplication.entity.MyBook;
import com.example.administrator.myapplication.utils.L;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.FindStatisticsListener;

/**
 * 消费统计界面
 */
public class ConsumptionActivity extends AbActivity {
    private PieChart mChart;
    List<InnerPieDate> pieDates;
    private int sum;
    //饼状图位置标志位
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumption);
        // howMoney();
        howMoney2();
    }

    /**
     * 计算钱的总量
     */
    private void howMoney2() {
        index = 0;
        //算一下的钱数总量
        List<BmobQuery<MyBook>> qys = new ArrayList<>();
        for (int i = 0; i < Constans.user_typs.size(); i++) {
            if (Constans.user_typs.get(i).getNAME().contains("[支]")) {
                BmobQuery<MyBook> qy1 = new BmobQuery();
                qy1.addWhereEqualTo("type", Constans.user_typs.get(i).getNAME());
                qys.add(qy1);
            }
        }
        BmobQuery<MyBook> query = new BmobQuery();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(this).getUsername());
        query.or(qys);
        //    qy1.sum(new String[]{"money"});
        query.findObjects(this, new FindListener<MyBook>() {
            @Override
            public void onSuccess(List<MyBook> list) {
                L.d(list.toString());
                //求和
                sum = 0;
                for (MyBook book : list) {
                    sum += Integer.valueOf(book.getMoney());
                }
                if (sum == 0) {
                    Toast.makeText(ConsumptionActivity.this, "您太厉害了，一分钱都没有花！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //计算每个分类的钱数
                getTypeMoney();
            }

            @Override
            public void onError(int i, String s) {
                L.d(s);
            }
        });

    }

    /**
     * 有BUG，已经弃用
     */
    private void howMoney() {
        index = 0;
        //算一下的钱数总量
        BmobQuery<MyBook> query = new BmobQuery();
        query.addWhereEqualTo("username", BmobUser.getCurrentUser(this).getUsername());
        query.findObjects(this, new FindListener<MyBook>() {
            @Override
            public void onSuccess(List<MyBook> list) {
                sum = 0;
                for (MyBook book : list) {
                    sum += Integer.valueOf(book.getMoney());
                }
                if (sum == 0) {
                    Toast.makeText(ConsumptionActivity.this, "您太厉害了，一分钱都没有花！", Toast.LENGTH_SHORT).show();
                    return;
                }
                //计算每个分类的钱数
                getTypeMoney();
            }


            @Override
            public void onError(int i, String s) {
                Toast.makeText(ConsumptionActivity.this, "请检查您的网络！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 获得每个分类的钱数
     */
    private void getTypeMoney() {
        pieDates = new ArrayList<>();
        for (int i = 0; i < Constans.user_typs.size(); i++) {
            if (Constans.user_typs.get(i).getNAME().contains("[支]")) {
                BmobQuery<MyBook> query = new BmobQuery<>();
                query.addWhereEqualTo("username", BmobUser.getCurrentUser(this).getUsername());
                final String typename = Constans.user_typs.get(i).getNAME();
                query.addWhereEqualTo("type", typename);
                query.findObjects(ConsumptionActivity.this, new FindListener<MyBook>() {
                    @Override
                    public void onSuccess(List<MyBook> list) {
                        int moneys = 0;
                        for (int i = 0; i < list.size(); i++) {
                            final int finalI = i;
                            com.example.administrator.myapplication.entity.MyBook book = (com.example.administrator.myapplication.entity.MyBook) list.get(i);
                            moneys += Integer.valueOf(book.getMoney());
                        }
                        //饼状图中添加一个值，（颜色，百分比，位置，名称）
                        pieDates.add(new InnerPieDate(getRandomColor(), new Entry(moneys * 100.0f / sum * 1.0f, index++), typename.replace("[支]", "")));
                        if (pieDates.size() == Constans.getZCCount()) {
                            //显示饼状图
                            showPieChart(pieDates);
                        }
                    }

                    @Override
                    public void onError(int i, String s) {
                        Toast.makeText(ConsumptionActivity.this, "您的网络异常，计算统计结果失败！", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        }
    }

    /**
     * 开始显示
     *
     * @param pieDates
     */
    private void showPieChart(List<InnerPieDate> pieDates) {
        mChart = (PieChart) findViewById(R.id.spread_pie_chart);
        int count = pieDates.size();
        List<Entry> yvalues = new ArrayList<>();
        List<String> xvalues = new ArrayList<>();
        int[] colors = new int[count];
        for (int i = 0; i < count; i++) {
            xvalues.add(pieDates.get(i).value);
            yvalues.add(pieDates.get(i).entry);
            colors[i] = pieDates.get(i).color;
        }
        PieDataSet dateSet = new PieDataSet(yvalues, "统计表");
        dateSet.setColors(colors);
        PieData pieDate = new PieData(xvalues, dateSet);
        mChart.setDescription("类别报表");
        mChart.setData(pieDate);
        Legend mLegend = mChart.getLegend();  //设置比例图
        //    mLegend.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);  //最右边显示
//      mLegend.setForm(LegendForm.LINE);  //设置比例图的形状，默认是方形
        mLegend.setXEntrySpace(7f);
        mLegend.setYEntrySpace(5f);

        mChart.animateXY(5000, 5000);  //设置动画
    }

    /**
     * 饼快封装类
     */
    class InnerPieDate {
        //饼快颜色
        int color;
        //饼快百分比
        Entry entry;
        //饼快内容
        String value;

        public InnerPieDate(int color, Entry entry, String value) {
            this.color = color;
            this.entry = entry;
            this.value = value;
        }
    }
}
