package com.wanyue.common.server.entity;

import java.util.List;

public class BaseArrayResponse<T> extends BaseSimpleReponse{
    private List<T> data;
    public List<T> getData() {
        return data;
    }
    public void setData(List<T> data) {
        this.data = data;
    }
}
