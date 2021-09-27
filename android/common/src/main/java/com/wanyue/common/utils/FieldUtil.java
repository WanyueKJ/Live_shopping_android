package com.wanyue.common.utils;

import android.text.TextUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class FieldUtil {
    public static<T> Class<T> getType(Object object){
        Type type = object.getClass().getGenericSuperclass();
        if( type instanceof ParameterizedType){
            ParameterizedType pType = (ParameterizedType)type;
            Type claz = pType.getActualTypeArguments()[0];
            if( claz instanceof Class ){
               return (Class<T>) claz;
            }
        }
        return null;
    }



    public static Integer parseInt(String string){
        if(TextUtils.isEmpty(string)){
            return null;
        }
        try {
            return Integer.parseInt(string);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Long parseLong(String string){
        if(TextUtils.isEmpty(string)){
            return null;
        }
        try {
            return Long.parseLong(string);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
