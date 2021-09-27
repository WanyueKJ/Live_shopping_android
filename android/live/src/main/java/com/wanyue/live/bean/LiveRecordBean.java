package com.wanyue.live.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2018/9/30.
 */

public class LiveRecordBean {
    private String id;
    private String uid;
    private int nums;
    private String startTime;
    private String endTime;
    private String title;
    private String city;
    private String dateStartTime;
    private String dateEndTime;

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

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    @JSONField(name = "starttime")
    public String getStartTime() {
        return startTime;
    }

    @JSONField(name = "starttime")
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    @JSONField(name = "endtime")
    public String getEndTime() {
        return endTime;
    }

    @JSONField(name = "endtime")
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @JSONField(name = "datestarttime")
    public String getDateStartTime() {
        return dateStartTime;
    }

    @JSONField(name = "datestarttime")
    public void setDateStartTime(String dateStartTime) {
        this.dateStartTime = dateStartTime;
    }

    @JSONField(name = "dateendtime")
    public String getDateEndTime() {
        return dateEndTime;
    }

    @JSONField(name = "dateendtime")
    public void setDateEndTime(String dateEndTime) {
        this.dateEndTime = dateEndTime;
    }
}
