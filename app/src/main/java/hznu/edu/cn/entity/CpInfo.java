package hznu.edu.cn.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2017/12/18.
 */

public class CpInfo extends BmobObject {
    /**
     * 起点
     */
    String start;
    /**
     * 终点
     */
    String end;
    /**
     * 姓名
     */
    String name;
    /**
     * 时间
     */
    String time;
    /**
     * 类型
     */
    String type;
    /**
     * 座位号
     */
    String loc;
    /**
     * 车次
     */
    String num;
    /**
     * 价格
     */
    String money;

    public CpInfo(String start, String end,  String time, String num) {
        this.start = start;
        this.end = end;
        this.time = time;
        this.num = num;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return

                "车次='" + num + '\n' +
                        "起点='" + start + '\n' +
                        "终点='" + end + '\n' +
                        //  "乘车人='" + name + '\n' +
                        "时间='" + time + '\n' +
                        "类型='" + type + '\n';
        //   "席位='" + loc + '\n' +
        // "价格='" + money + '\n';
    }

    public String cpInfo() {
        return

                "车次='" + num + '\n' +
                        "起点='" + start + '\n' +
                        "终点='" + end + '\n' +
                        "乘车人='" + name + '\n' +
                        "时间='" + time + '\n' +
                        "类型='" + type + '\n' +
                        "席位='" + loc + '\n' +
                        "价格='" + money + '\n';
    }
}
