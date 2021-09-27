package com.wanyue.common.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.util.ArrayMap;
import com.wanyue.common.CommonApplication;

public class ResourceUtil {
    private static ArrayMap<Integer, Drawable>drawableArrayMap;
    private static ArrayMap<Integer, Integer>colorArrayMap;

    static {
        drawableArrayMap=new ArrayMap<>();
        colorArrayMap=new ArrayMap<>();
    }

    public static ColorStateList getColorList(Context context,int color) {
        Resources resources = context.getResources();
        return resources.getColorStateList(color);
    }

    public static int getColor(int colorId) {
       return getColor(CommonApplication.sInstance,colorId);
    }


    public static int getColor(Context context,int colorId) {
        if(context==null){
            DebugUtil.sendException("Context不能为null");
            return 0;
        }
        if(colorArrayMap==null){
           colorArrayMap=new ArrayMap<>();
        }
        Integer color=colorArrayMap.get(colorId);
        if(color!=null){
           return color;
        }
        Resources resources = context.getResources();
        color=resources.getColor(colorId);
        colorArrayMap.put(colorId,color);
        return color;
    }

    /*Drawable的*/
    public static Drawable getDrawable(int resourceId,boolean shouldUserCache){
        Drawable drawable=null;
        if(resourceId==0){
            return drawable;
        }
        if(!shouldUserCache){
            drawable= ContextCompat.getDrawable(CommonApplication.sInstance, resourceId);
        }
         else{
            drawable=drawableArrayMap.get(resourceId);
            if(drawable==null){
            drawable= ContextCompat.getDrawable(CommonApplication.sInstance, resourceId);
            drawableArrayMap.put(resourceId,drawable);
            }else{
                L.e("用到了缓存");
            }
        }
         return drawable;
    }


    public static void clearDrawable(int...drawaleIdArray){
        if(drawaleIdArray==null||drawaleIdArray.length==0){
            return;
        }
        for(int drawaleId:drawaleIdArray){
            drawableArrayMap.remove(drawaleId);
        }
    }

    public static void clearAllDrawable(){
        if(drawableArrayMap!=null){
           drawableArrayMap.clear();
        }
    }






    public static String getMetaDataString(String key) {
        String res = null;
        try {
            ApplicationInfo appInfo = CommonApplication.sInstance.getPackageManager().getApplicationInfo(CommonApplication.sInstance.getPackageName(), PackageManager.GET_META_DATA);
            res = appInfo.metaData.getString(key);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return res;
    }
}
