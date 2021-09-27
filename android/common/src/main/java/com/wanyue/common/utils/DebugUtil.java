package com.wanyue.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.tencent.bugly.crashreport.CrashReport;

public class DebugUtil {
   private static boolean mIsDeBug=false;
   public static void sendException(String errorMessage){
       if(mIsDeBug){
         //throw new WanyueException(errorMessage);
       }else{
          CrashReport.postCatchedException(new WanyueException(errorMessage));
       }
   }

    public static void logJson(JSONObject info) {
        if(mIsDeBug){
            String json=info.toJSONString();
            L.e("json=="+json);
        }

    }

    public static boolean isDeBug() {
        return mIsDeBug;
    }

    public static class WanyueException extends  IllegalArgumentException{
       public WanyueException(String s) {
           super(s);
       }
    }


    public static void setDeBug(boolean deBug) {
        mIsDeBug = deBug;
    }
}
