package com.wanyue.main.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;
public class SpreadOrderSectionBean implements MultiItemEntity {
    public static final int TYPE=1;

    private String time;
    private List<SpreadOrderBean> child;
    private int count;

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public List<SpreadOrderBean> getChild() {
        return child;
    }
    public void setChild(List<SpreadOrderBean> child) {
        this.child = child;
    }
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getItemType() {
        return TYPE;
    }
}
