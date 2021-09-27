package com.wanyue.main.bean;

import com.wanyue.common.bean.commit.CommitEntity;
import com.wanyue.common.utils.ValidatePhoneUtil;

public class LoginCommitBean extends CommitEntity {
   private String phoneString;
   private String checkString; //可以是密码也可以使验证码

    public LoginCommitBean(){

    }

    @Override
    public boolean observerCondition() {
        return ValidatePhoneUtil.validateMobileNumber(phoneString)&&
                fieldNotEmpty(checkString);
    }

    public String getPhoneString() {
        return phoneString;
    }
    public void setPhoneString(String phoneString) {
        this.phoneString = phoneString;
        observer();
    }
    public String getCheckString() {
        return checkString;
    }
    public void setCheckString(String checkString) {
        this.checkString = checkString;
        observer();
    }
}
