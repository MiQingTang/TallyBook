package hznu.edu.cn.activity;

/**
 * Created by Ssumday on 2017/11/26.
 */

public class ContactsNumber {
    private Boolean checked;
    private String name;
    private String phoneNumber;

    public ContactsNumber() {
    }

    public ContactsNumber(String name, String Number) {
        checked=false;
        this.name = name;
        this.phoneNumber = Number;
    }

    public String getName() {
        return name;
    }

    public String getContactsNumber() {
        return phoneNumber;
    }
    public Boolean getChecked() {
        return checked;
    }
    public void setChecked(boolean check ) {
        this.checked=check;
    }
}

