package com.wanyue.common.bean;

import com.google.gson.annotations.SerializedName;

public class ConditionLevel  implements ExportNamer{
    @SerializedName("levelid")
    private String id;
    private String name;

    public ConditionLevel(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public ConditionLevel(){

    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String exportName() {
        return name;
    }

    @Override
    public String exportId() {
        return id;
    }
}
