package com.wanyue.common.server.converter;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;

public class FashJsonConvert {

    public static <T> T fromJson(String json, Class<T> type) throws JsonIOException, JsonSyntaxException {
        return JSON.parseObject(json,type);
    }
    public static <T> T fromJson(String json, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return JSON.parseObject(json,typeOfT);
    }
}
