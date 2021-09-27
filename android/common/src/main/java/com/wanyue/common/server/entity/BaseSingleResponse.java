package com.wanyue.common.server.entity;

public class BaseSingleResponse<T>  extends BaseSimpleReponse {
    private T data;

    public T getData() {
        return data;
    }
    public void setData(T data) {
        this.data = data;
    }
}
