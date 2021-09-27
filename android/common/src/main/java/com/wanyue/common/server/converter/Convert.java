package com.wanyue.common.server.converter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.readystatesoftware.chuck.internal.support.JsonConvertor;

import java.io.Reader;
import java.lang.reflect.Type;

public class Convert {

     static Gson create() {
        return Convert.GsonHolder.gson;
    }

    private static class GsonHolder {
        private static Gson gson = new Gson();
    }

    public static <T> T fromJson(String json, Class<T> type) throws JsonIOException, JsonSyntaxException {

        return create().fromJson(json, type);
    }

    public static <T> T fromJson(String json, Type type) {
        return create().fromJson(json, type);
    }

    public static <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return create().fromJson(reader, typeOfT);
    }
    /*public static <T> T fromJsonArray(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
    T list=create().fromJson(new TypeToken<List<T>>(){}.getType());
    }*/

    public static <T> T fromJson(Reader json, Class<T> classOfT) throws JsonSyntaxException, JsonIOException {
        return create().fromJson(json, classOfT);
    }

    public static <T> T fromJson(Reader json, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return create().fromJson(json, typeOfT);
    }

    public static String toJson(Object src) {
        return create().toJson(src);
    }

    public static String toJson(Object src, Type typeOfSrc) {
        return create().toJson(src, typeOfSrc);
    }

    public static String formatJson(String json) {
        try {
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(json);
            return JsonConvertor.getInstance().toJson(je);
        } catch (Exception e) {
            return json;
        }
    }

    public static String formatJson(Object src) {
        try {
            JsonParser jp = new JsonParser();
            JsonElement je = jp.parse(toJson(src));
            return JsonConvertor.getInstance().toJson(je);
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}