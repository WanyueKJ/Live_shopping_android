package com.wanyue.common.http;

import android.text.TextUtils;
import android.util.ArrayMap;
import com.wanyue.common.CommonAppConfig;

public class UrlMap {
  private static ArrayMap<String,String>mMap;
  public static final String API=CommonAppConfig.HOST+"/api/";

    public static String getUrl(String serviceName) {
        if(mMap==null){
           mMap =new ArrayMap<>();
        }
        String url=mMap.get(serviceName);
        if(TextUtils.isEmpty(url)){
            url= API+serviceName;
            mMap.put(serviceName,url);
        }
        return url;
    }
}
