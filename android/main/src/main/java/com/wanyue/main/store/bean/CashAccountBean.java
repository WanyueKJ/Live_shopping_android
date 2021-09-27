package com.wanyue.main.store.bean;

import com.alibaba.fastjson.annotation.JSONField;

public class CashAccountBean {

    private String id;
    private String uid;
    private int type;
    private String bankName;
    private String userName;
    private String account;
    private boolean checked;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @JSONField(name = "bank")
    public String getBankName() {
        return bankName;
    }

    @JSONField(name = "bank")
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    @JSONField(name = "name")
    public String getUserName() {
        return userName;
    }

    @JSONField(name = "name")
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
