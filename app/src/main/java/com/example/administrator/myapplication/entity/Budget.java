package com.example.administrator.myapplication.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/5/10
 */
public class Budget extends BmobObject {
    //分类
    String type;
    //预算金额
    String budMoney;
    //用户
    String username;
    //时间
    String time;
    //已经消费
    String hasMoney;

    public String getHasMoney() {
        return hasMoney;
    }

    public void setHasMoney(String hasMoney) {
        this.hasMoney = hasMoney;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBudMoney() {
        return budMoney;
    }

    public void setBudMoney(String budMoney) {
        this.budMoney = budMoney;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[" + type.replace("[支]", "") + ']');
        if (hasMoney == null) {
            builder.append(0);
        } else {
            builder.append(hasMoney);
        }
        builder.append("/");
        if (budMoney == null) {
            builder.append(0);
        } else {
            builder.append(budMoney);
        }
        builder.append("(消费/预算)");
        return builder.toString();
    }
}
