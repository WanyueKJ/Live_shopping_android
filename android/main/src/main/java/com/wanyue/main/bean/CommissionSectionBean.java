package com.wanyue.main.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import java.util.List;

public class CommissionSectionBean implements MultiItemEntity {
    public static final int TYPE=1;
    private String time;
    private List<CommissionBean> list;

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public List<CommissionBean> getList() {
        return list;
    }
    public void setList(List<CommissionBean> list) {
        this.list = list;
    }

    @Override
    public int getItemType() {
        return TYPE;
    }
}
