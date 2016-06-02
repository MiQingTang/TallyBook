package com.example.administrator.myapplication.constans;


import com.example.administrator.myapplication.entity.ChannelItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/21.
 */


public class ChannelManage {
    public static ChannelManage channelManage;
    /**
     * 默认的用户选择频道列表
     */
    public static ArrayList<ChannelItem> defaultUserChannels;
    /**
     * 默认的其他频道列表
     */
    public static ArrayList<ChannelItem> defaultOtherChannels;
//    private ChannelDao channelDao;
    /**
     * 判断数据库中是否存在用户数据
     */
    private boolean userExist = false;

    static {
        defaultUserChannels = new ArrayList<ChannelItem>();
        defaultOtherChannels = new ArrayList<ChannelItem>();
//        defaultUserChannels.add(new ChannelItem(1, "添加", 1, 1));
//        defaultUserChannels.add(new ChannelItem(2, "吃饭", 2, 1));
//        defaultUserChannels.add(new ChannelItem(3, "买装备", 3, 1));
//        defaultUserChannels.add(new ChannelItem(4, "打车", 4, 1));
//        defaultUserChannels.add(new ChannelItem(5, "加油", 5, 1));
//        defaultUserChannels.add(new ChannelItem(6, "美容", 6, 1));
//        defaultUserChannels.add(new ChannelItem(7, "旅游", 7, 1));
//        defaultOtherChannels.add(new ChannelItem(8, "雕塑", 1, 0));
//        defaultOtherChannels.add(new ChannelItem(9, "静物", 2, 0));
//        defaultOtherChannels.add(new ChannelItem(10, "星空", 3, 0));
//        defaultOtherChannels.add(new ChannelItem(11, "航模", 4, 0));
//        defaultOtherChannels.add(new ChannelItem(12, "游戏", 5, 0));
//        defaultOtherChannels.add(new ChannelItem(13, "数码", 6, 0));
//        defaultOtherChannels.add(new ChannelItem(14, "人物", 7, 0));
//        defaultOtherChannels.add(new ChannelItem(15, "盆景", 8, 0));
//        defaultOtherChannels.add(new ChannelItem(16, "山谷", 9, 0));
//        defaultOtherChannels.add(new ChannelItem(17, "二次元", 10, 0));
//        defaultOtherChannels.add(new ChannelItem(18, "图形", 11, 0));
    }


}


