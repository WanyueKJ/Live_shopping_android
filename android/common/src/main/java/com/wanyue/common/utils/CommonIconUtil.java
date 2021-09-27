package com.wanyue.common.utils;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.util.SparseIntArray;

import com.wanyue.common.CommonApplication;
import com.wanyue.common.Constants;
import com.wanyue.common.R;

/**
 * Created by  on 2018/10/11.
 */

public class CommonIconUtil {

    private static SparseIntArray sLiveGiftCountMap;//送礼物数字
    private static SparseIntArray sOnLineMap2;//在线类型图标
    private static Drawable sSexMaleDrawble;
    private static Drawable sSexFemaleDrawble;
    private static Drawable sSexMaleBgDrawble;
    private static Drawable sSexFemaleBgDrawble;

    private static SparseIntArray sCashTypeMap;//提现图片

    static {
        sLiveGiftCountMap = new SparseIntArray();
        sLiveGiftCountMap.put(0, R.mipmap.icon_live_gift_count_0);
        sLiveGiftCountMap.put(1, R.mipmap.icon_live_gift_count_1);
        sLiveGiftCountMap.put(2, R.mipmap.icon_live_gift_count_2);
        sLiveGiftCountMap.put(3, R.mipmap.icon_live_gift_count_3);
        sLiveGiftCountMap.put(4, R.mipmap.icon_live_gift_count_4);
        sLiveGiftCountMap.put(5, R.mipmap.icon_live_gift_count_5);
        sLiveGiftCountMap.put(6, R.mipmap.icon_live_gift_count_6);
        sLiveGiftCountMap.put(7, R.mipmap.icon_live_gift_count_7);
        sLiveGiftCountMap.put(8, R.mipmap.icon_live_gift_count_8);
        sLiveGiftCountMap.put(9, R.mipmap.icon_live_gift_count_9);


        sOnLineMap2 = new SparseIntArray();
        sOnLineMap2.put(Constants.LINE_TYPE_OFF, R.mipmap.o_user_line_off);
        sOnLineMap2.put(Constants.LINE_TYPE_DISTURB, R.mipmap.o_user_line_disturb);
        sOnLineMap2.put(Constants.LINE_TYPE_CHAT, R.mipmap.o_user_line_chat);
        sOnLineMap2.put(Constants.LINE_TYPE_ON, R.mipmap.o_user_line_on);


        sCashTypeMap = new SparseIntArray();
        sCashTypeMap.put(Constants.CASH_ACCOUNT_ALI, R.mipmap.icon_cash_ali);
        sCashTypeMap.put(Constants.CASH_ACCOUNT_WX, R.mipmap.icon_cash_wx);
        sCashTypeMap.put(Constants.CASH_ACCOUNT_BANK, R.mipmap.icon_cash_bank);

    }

    public static int getGiftCountIcon(int key) {
        return sLiveGiftCountMap.get(key);
    }

    public static Drawable getSexDrawable(int sex) {
        if (sex == 1) {
            if (sSexMaleDrawble == null) {
                sSexMaleDrawble = ContextCompat.getDrawable(CommonApplication.sInstance, R.mipmap.icon_sex_male);
            }
            return sSexMaleDrawble;
        } else {
            if (sSexFemaleDrawble == null) {
                sSexFemaleDrawble = ContextCompat.getDrawable(CommonApplication.sInstance, R.mipmap.icon_sex_female);
            }
            return sSexFemaleDrawble;
        }
    }


    public static Drawable getSexBgDrawable(int sex) {
        if (sex == 1) {
            if (sSexMaleBgDrawble == null) {
                sSexMaleBgDrawble = ContextCompat.getDrawable(CommonApplication.sInstance, R.drawable.bg_sex_male);
            }
            return sSexMaleBgDrawble;
        } else {
            if (sSexFemaleBgDrawble == null) {
                sSexFemaleBgDrawble = ContextCompat.getDrawable(CommonApplication.sInstance, R.drawable.bg_sex_female);
            }
            return sSexFemaleBgDrawble;
        }
    }

    public static int getOnLineIcon2(int key) {
        return sOnLineMap2.get(key);
    }

    public static int getCashTypeIcon(int key) {
        return sCashTypeMap.get(key);
    }
}
