package com.wanyue.common.server;

import android.util.ArrayMap;

import com.wanyue.common.CommonAppConfig;

import java.util.Map;

public class MapBuilder {
 private static MapBuilder mapBuilder;

private ArrayMap<String,Object> map;
private MapBuilder(){
    map=new ArrayMap<>();
}

 public MapBuilder put(String key, Object value){
    if(value!=null){
        map.put(key,value);
    }else{
        map.put(key,"");
    }
    return this;
 }

 public MapBuilder addBaseParm(){
     return this;
 }

 public Map<String,Object> build(){
     map.put("source",1);
     addBaseParm();
    return map;
 }


 public void init(){
     if(map!=null){
        map.clear();
     }

     if(map==null){
        map=new ArrayMap<>();
     }
 }

   /**
     * 获取极光推送 RegistrationID
     */


 public static synchronized MapBuilder factory(){
     if(mapBuilder==null){
        mapBuilder=new MapBuilder();
     }
     mapBuilder.init();
    return mapBuilder;
 }

}
