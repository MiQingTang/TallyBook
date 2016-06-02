package com.example.administrator.myapplication.constans;

import com.example.administrator.myapplication.entity.ChannelItem;

import java.util.List;

/**
 * Created by Administrator on 2016/5/6.
 */
public class Constans {
    public static List<ChannelItem> user_typs;

    /**
     * 算出有多少个支出
     */
    public static int getZCCount() {
        if (user_typs == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < user_typs.size(); i++) {
            if (Constans.user_typs.get(i).getNAME().contains("[支]")) {
                count++;
            }
        }
        return count;
    }
}
