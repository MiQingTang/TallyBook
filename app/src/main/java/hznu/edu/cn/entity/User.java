package hznu.edu.cn.entity;

import java.util.List;

import cn.bmob.v3.BmobUser;
import hznu.edu.cn.activity.ContactsNumber;

/**
 * Created by Administrator on 2017/12/8.
 */

public class User extends BmobUser {
    /**
     * 乘车人
     */
    List<ContactsNumber> ccr;

    public List<ContactsNumber> getCcr() {
        return ccr;
    }

    public void setCcr(List<ContactsNumber> ccr) {
        this.ccr = ccr;
    }
}
