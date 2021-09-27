package com.yunbao.im.bean;

public class ChatInfoBean {
    private int action;
    private int callType;
    private String content;

    public ChatInfoBean() {
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
