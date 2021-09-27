package com.wanyue.common.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2018/9/25.
 */

public class LiveClassBean implements ExportNamer{
    public static final int FOLLOW=-1;
    public static final int FEATURED=0;

    protected int id;
    protected String name;
    protected String thumb;
    protected int orderNo;
    private boolean isAll;
    private String des;
    private boolean checked;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @JSONField(name = "orderno")
    public int getOrderNo() {
        return orderNo;
    }

    @JSONField(name = "orderno")
    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }

    public boolean isAll() {
        return isAll;
    }

    public void setAll(boolean all) {
        isAll = all;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String exportName() {
        return name;
    }

    @Override
    public String exportId() {
        return null;
    }
}
