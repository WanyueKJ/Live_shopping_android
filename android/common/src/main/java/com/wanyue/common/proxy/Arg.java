package com.wanyue.common.proxy;

import java.util.Map;

public class Arg {
    protected   static <T> T getParm(BaseViewProxy baseViewProxy,String key,Class<T>cs){
        Map<String,Object>map=baseViewProxy.getArgMap();
        Object object=map.get(key);
        if(object!=null&&object.getClass()==cs){
            return (T) object;
        }
        return null;
    }
    public static String getParmString(BaseViewProxy baseViewProxy,String key){
        return getParm(baseViewProxy,key,String.class);
    }


}
