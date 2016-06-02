package com.example.administrator.myapplication.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/5/6.
 */
public class MyBook extends BmobObject {
    //类型
    String type;
    //钱数
    String money;
    //备注
    String remark;
    //用户
    String username;
    //时间
    String time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        return "MyBook{" +
                "type='" + type + '\'' +
                ", money='" + money + '\'' +
                ", remark='" + remark + '\'' +
                ", username='" + username + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
