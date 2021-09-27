package com.wanyue.common.adapter.radio;

public class CheckEntity implements IRadioChecker{
    private String name;
    private String id;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String getContent() {
        return name;
    }
    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
