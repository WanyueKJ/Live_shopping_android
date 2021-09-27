package com.wanyue.main.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.gson.annotations.SerializedName;
import com.wanyue.common.bean.ExportNamer;

import java.util.List;

public class ClassifyBean implements ExportNamer {
    private String id;
    private String pid;
    @JSONField(name = "cate_name")
    @SerializedName( "cate_name")
    private String name;
    private List<ClassifyBean> children;
    private String pic;
    private int index;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<ClassifyBean> getChildren() {
        return children;
    }

    public void setChildren(List<ClassifyBean> children) {
        this.children = children;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
