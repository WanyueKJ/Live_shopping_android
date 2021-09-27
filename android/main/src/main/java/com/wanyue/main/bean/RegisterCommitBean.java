package com.wanyue.main.bean;

import com.wanyue.common.bean.commit.CommitEntity;
import com.wanyue.common.utils.ValidatePhoneUtil;

public class RegisterCommitBean extends CommitEntity {
    private String phone;
    private String pwd;
    private String code;
    private String spread;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        observer();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
        observer();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        observer();
    }

    public String getSpread() {
        return spread;
    }
    public void setSpread(String spread) {
        this.spread = spread;
        observer();
    }

    @Override
    public boolean observerCondition() {
        return ValidatePhoneUtil.validateMobileNumber(phone)&&fieldNotEmpty(pwd)&&fieldNotEmpty(code);
    }
}
