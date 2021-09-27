package com.wanyue.common.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JsonUtil {


    /**
     * 功能描述：把JSON数据转换成指定的java对象
     * @param jsonData JSON数据
     * @param clazz 指定的java对象
     * @return 指定的java对象
     */
    public static <T> T getJsonToBean(String jsonData, Class<T> clazz) {
        if(jsonData==null) {
            return null;
        }

        return JSON.parseObject(jsonData, clazz);
    }

    /**
     * 功能描述：把java对象转换成JSON数据
     * @param object java对象
     * @return JSON数据
     */
    public static String getBeanToJson(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象列表
     * @param jsonData JSON数据
     * @param clazz 指定的java对象
     * @return List<T>
     */
    public static <T> List<T> getJsonToList(String jsonData, Class<T> clazz) {
        if(jsonData==null) {
            return null;
        }
        try {
            return JSON.parseArray(jsonData, clazz);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }





    /**
     * 功能描述：把JSON数据转换成较为复杂的List<Map<String, Object>>
     * @param jsonData JSON数据
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getJsonToListMap(String jsonData) {
        if(jsonData==null) {
            return null;
        }

        return JSON.parseObject(jsonData, new TypeReference<List<Map<String, Object>>>() {
        });
    }

    public  static <T> List<T> getData(JSONArray jsonArray,Class<T>cs){
        if(jsonArray==null){
            DebugUtil.sendException("JSONArray!=null!!!");
            return new ArrayList<>(1);
        }
        int size=jsonArray.size();
        List<T>list=new ArrayList<>(jsonArray.size());
        for(int i=0;i<size;i++){
            T t=jsonArray.getObject(i,cs);
            list.add(t);
        }
        return list;
    }


    public static String getString(String data,String key){
        try {
            org.json.JSONObject jsonObject=new org.json.JSONObject(data);
           return jsonObject.getString(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public static int getInt(String data,String key){
        try {
            org.json.JSONObject jsonObject=new org.json.JSONObject(data);
            return jsonObject.getInt(key);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
    }


    public static final int JSON_TYPE_OBJECT=1;
    public static final int JSON_TYPE_ARRAY=2;
    public static final int JSON_TYPE_ERROR=-1;
    public static final int JSON_TYPE_NULL=-2;


    public static int getJsonType(String json){
        if(TextUtils.isEmpty(json)){
            return JSON_TYPE_NULL;
        }
        char firstChar=json.charAt(0);
        if(firstChar=='{'){
            return JSON_TYPE_OBJECT;
        }else if(firstChar=='['){
            return JSON_TYPE_ARRAY;
        }else{
            return JSON_TYPE_ERROR;
        }
    }
}
