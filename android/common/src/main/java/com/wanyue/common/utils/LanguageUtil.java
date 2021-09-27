package com.wanyue.common.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.wanyue.common.CommonApplication;
import com.wanyue.common.Constants;

import java.util.Locale;

/**
 * Created by  on 2019/6/5.
 */

public class LanguageUtil {

    private static LanguageUtil instance;
    private String mLanguage;
    private Locale mEnLocate;//英语


    public static LanguageUtil getInstance() {
        if (instance == null) {
            synchronized (LanguageUtil.class) {
                if (instance == null) {
                    instance = new LanguageUtil();
                }
            }
        }
        return instance;
    }


    public Locale getEnLocate() {
        if (mEnLocate == null) {
            mEnLocate = new Locale("en", "US");
        }
        return mEnLocate;
    }

    /**
     * 设置语言
     */
    private static void setConfiguration() {
        Locale targetLocale = getLanguageLocale();
        Resources resources = CommonApplication.sInstance.getResources();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(targetLocale);
        } else {
            configuration.locale = targetLocale;
        }
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);//语言更换生效的代码!
    }


    public static Locale getLanguageLocale() {
        if (isEn()) {
            return LanguageUtil.getInstance().getEnLocate();
        }
        return Locale.SIMPLIFIED_CHINESE;
    }


    public void updateLanguage(String language) {
        setLanguage(language);
        setConfiguration();
        ///EventBus.getDefault().post(new OnChangeLanguageEvent(language));
    }


    public static Context attachBaseContext(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return createConfigurationResources(context);
        } else {
            setConfiguration();
            return context;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context createConfigurationResources(Context context) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        Locale locale = getLanguageLocale();
        configuration.setLocale(locale);
        return context.createConfigurationContext(configuration);
    }

    public String getLanguage() {
        if (TextUtils.isEmpty(mLanguage)) {
            mLanguage = SpUtil.getInstance().getStringValue(SpUtil.LANGUAGE);
            if (TextUtils.isEmpty(mLanguage)) {
                setLanguage(Constants.LANG_ZH);
            }
        }
        return mLanguage;
    }

    public void setLanguage(String language) {
        mLanguage = language;
        SpUtil.getInstance().setStringValue(SpUtil.LANGUAGE, language);
    }


    public static boolean isEn() {
        return Constants.LANG_EN.equals(LanguageUtil.getInstance().getLanguage());
    }
}
