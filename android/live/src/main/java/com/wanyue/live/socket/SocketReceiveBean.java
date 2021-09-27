package com.wanyue.live.socket;

import com.alibaba.fastjson.JSONArray;

/**
 * Created by  on 2017/8/22.
 * 接收socket的实体类
 */

public class SocketReceiveBean {
    private String retcode;
    private String retmsg;
    private JSONArray msg;

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
    }

    public JSONArray getMsg() {
        return msg;
    }

    public void setMsg(JSONArray msg) {
        this.msg = msg;
    }
}
