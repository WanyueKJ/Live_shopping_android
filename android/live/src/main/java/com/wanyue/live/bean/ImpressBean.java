package com.wanyue.live.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by  on 2018/10/15.
 * 印象实体类
 */

public class ImpressBean {

    private int id;
    private String name;
    private int order;
    private String color;
    private String color2;
    private int check;
    private String nums;

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

    @JSONField(name = "orderno")
    public int getOrder() {
        return order;
    }

    @JSONField(name = "orderno")
    public void setOrder(int order) {
        this.order = order;
    }

    @JSONField(name = "colour")
    public String getColor() {
        return color;
    }

    @JSONField(name = "colour")
    public void setColor(String color) {
        this.color = color;
    }

    @JSONField(name = "colour2")
    public String getColor2() {
        return color2;
    }
    @JSONField(name = "colour2")
    public void setColor2(String color2) {
        this.color2 = color2;
    }

    @JSONField(name = "ifcheck")
    public int getCheck() {
        return check;
    }

    @JSONField(name = "ifcheck")
    public void setCheck(int check) {
        this.check = check;
    }

    public boolean isChecked() {
        return this.check == 1;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }
}
