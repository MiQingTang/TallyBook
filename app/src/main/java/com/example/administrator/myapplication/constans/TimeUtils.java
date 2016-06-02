package com.example.administrator.myapplication.constans;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/5/9.
 */
public class TimeUtils {
    /**
     * 获得年月（2016年5月）
     *
     * @param time long类型时间
     */
    public static String getYearMonth(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        return format.format(time);
    }

    public static String getYearMonthDay(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(time);
    }
}
