package com.wanyue.live.utils;

import android.util.SparseIntArray;

import com.wanyue.live.R;

import java.util.Arrays;
import java.util.List;

/**
 * Created by  on 2018/10/11.
 */

public class LiveIconUtil {
    private static SparseIntArray sLiveLightMap;//飘心动画图片
    private static SparseIntArray sLiveGiftCountMap;//送礼物数字
    private static List<Integer> sLinkMicPkAnim;//连麦pk帧动画

    static {
        sLiveLightMap = new SparseIntArray();
        sLiveLightMap.put(1, R.mipmap.icon_live_light_1);
        sLiveLightMap.put(2, R.mipmap.icon_live_light_2);
        sLiveLightMap.put(3, R.mipmap.icon_live_light_3);
        sLiveLightMap.put(4, R.mipmap.icon_live_light_4);
        sLiveLightMap.put(5, R.mipmap.icon_live_light_5);
        sLiveLightMap.put(6, R.mipmap.icon_live_light_6);

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

        sLinkMicPkAnim = Arrays.asList(
                R.mipmap.pk01,
                R.mipmap.pk02,
                R.mipmap.pk03,
                R.mipmap.pk04,
                R.mipmap.pk05,
                R.mipmap.pk06,
                R.mipmap.pk07,
                R.mipmap.pk08,
                R.mipmap.pk09,
                R.mipmap.pk10,
                R.mipmap.pk11,
                R.mipmap.pk12,
                R.mipmap.pk13,
                R.mipmap.pk14,
                R.mipmap.pk15,
                R.mipmap.pk16,
                R.mipmap.pk17,
                R.mipmap.pk18,
                R.mipmap.pk19
        );
    }


    public static int getLiveLightIcon(int key) {
        if (key > 6 || key < 1) {
            key = 1;
        }
        return sLiveLightMap.get(key);
    }

    public static int getGiftCountIcon(int key) {
        return sLiveGiftCountMap.get(key);
    }

    public static List<Integer> getLinkMicPkAnim() {
        return sLinkMicPkAnim;
    }

}
