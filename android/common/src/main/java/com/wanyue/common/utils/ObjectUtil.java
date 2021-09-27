package com.wanyue.common.utils;

public class ObjectUtil {
    public static boolean equalsObject(Object obj,Object obj2){
        if(obj==null||obj2==null||!obj.equals(obj2)){
            return false;
        }
        return true;
    }


    public static boolean isAClass(Object obj,Object obj2){
        if(obj!=null&&obj2!=null&&obj.getClass()==obj2.getClass()){
            return true;
        }
        return false;
    }
}
