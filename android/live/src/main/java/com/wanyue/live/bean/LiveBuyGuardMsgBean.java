package com.wanyue.live.bean;

/**
 * Created by  on 2018/11/15.
 * 直播间购买守护的消息
 */

public class LiveBuyGuardMsgBean {

    private String votes;
    private int guardNum;
    private String uid;
    private String userName;
    private int guardType;

    public String getVotes() {
        return votes;
    }

    public void setVotes(String votes) {
        this.votes = votes;
    }

    public int getGuardNum() {
        return guardNum;
    }

    public void setGuardNum(int guardNum) {
        this.guardNum = guardNum;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getGuardType() {
        return guardType;
    }

    public void setGuardType(int guardType) {
        this.guardType = guardType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
