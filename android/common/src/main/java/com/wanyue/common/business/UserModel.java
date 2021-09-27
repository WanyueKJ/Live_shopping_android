package com.wanyue.common.business;

import androidx.lifecycle.ViewModel;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.server.observer.DefaultObserver;

public class UserModel extends ViewModel {

    public static void refreshBalance(){
        CommonAPI.getBalance().subscribe(new DefaultObserver<JSONObject>() {
            @Override
            public void onNext(JSONObject jsonObject) {
              double price=  jsonObject.getDoubleValue("now_money");
              UserBean userBean= CommonAppConfig.getUserBean();
              if(userBean!=null){
                 userBean.setBalance(price);
                 CommonAppConfig.setUserBean(userBean);
              }
            }
        });
    }
}
