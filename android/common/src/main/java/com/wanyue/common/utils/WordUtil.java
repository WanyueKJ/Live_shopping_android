package com.wanyue.common.utils;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.wanyue.common.CommonApplication;
import com.wanyue.common.R;

import java.util.Locale;

/**
 * Created by  on 2017/10/10.
 * 获取string.xml中的字
 */

public class WordUtil {

    private static Resources sResources;

    static {
        sResources = CommonApplication.sInstance.getResources();
    }

    public static String getString(int res,Object...obj) {
        Locale appLocale = LanguageUtil.getLanguageLocale();
        Locale locale = sResources.getConfiguration().locale;
//        if (!appLocale.getLanguage().equals(locale.getLanguage())) {
//            Configuration configuration = sResources.getConfiguration();
//            configuration.setLocale(appLocale);
//            DisplayMetrics dm = sResources.getDisplayMetrics();
//            sResources.updateConfiguration(configuration, dm);//语言更换生效的代码!
//        }
        Configuration configuration = sResources.getConfiguration();
        configuration.setLocale(appLocale);
        DisplayMetrics dm = sResources.getDisplayMetrics();
        sResources.updateConfiguration(configuration, dm);//语言更换生效的代码!
        if(obj==null){
            return sResources.getString(res);
        }else{
            return sResources.getString(res,obj);
        }
    }



    public static String getString(int res){
      return   getString(res,"");
    }
}
