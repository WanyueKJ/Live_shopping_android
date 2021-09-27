package com.wanyue.common.server;

/**
 * Created by nh on 2017/2/14.
 */

public class RequestFactory {
    //工厂模式解耦网络框架
    public static IRequestManager getRequestManager(){
        return OkGoRequestMannger.getInstance();
    }
}
