package com.wanyue.shop.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class SpecsBean {
    @SerializedName("attr_name")
    @JSONField(name = "attr_name")
    private String name;
    @SerializedName("attr_values")
    @JSONField(name = "attr_values")
    private List<String> value;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<String> getValue() {
        return value;
    }
    public void setValue(List<String> value) {
        this.value = value;
    }


}
