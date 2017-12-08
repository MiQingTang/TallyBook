package hznu.edu.cn.entity;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/12/8.
 */

public class User extends BmobUser {
    /**
     * 乘车人
     */
    List<String> ccr;

    public List<String> getCcr() {
        return ccr;
    }

    public void setCcr(List<String> ccr) {
        this.ccr = ccr;
    }
}
