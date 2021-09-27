package com.wanyue.common.utils;

import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.wanyue.common.CommonApplication;
import com.wanyue.common.R;

/**
 * Created by  on 2018/10/11.
 */

public class GiftTextRender {

    private static ForegroundColorSpan sColorSpan;

    static {
        sColorSpan = new ForegroundColorSpan(0xFFFFFFFF);
    }



    public static SpannableStringBuilder renderGiftInfo2(String giftName) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String s1 = WordUtil.getString(R.string.reward);
        String content = s1 + " " + giftName;
        int index1 = s1.length();
        builder.append(content);
        builder.setSpan(sColorSpan, index1, content.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return builder;
    }



    public static SpannableStringBuilder renderGiftCount(int count) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String s = String.valueOf(count);
        builder.append(s);
        for (int i = 0, length = s.length(); i < length; i++) {
            String c = String.valueOf(s.charAt(i));
            if (StringUtil.isInt(c)) {
                int icon = CommonIconUtil.getGiftCountIcon(Integer.parseInt(c));
                Drawable drawable = ContextCompat.getDrawable(CommonApplication.sInstance, icon);
                if (drawable != null) {
                    drawable.setBounds(0, 0, DpUtil.dp2px(24), DpUtil.dp2px(32));
                    builder.setSpan(new ImageSpan(drawable), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return builder;
    }

    public static SpannableStringBuilder renderGiftCount2(int count) {
        SpannableStringBuilder builder = new SpannableStringBuilder();
        String s = String.valueOf(count);
        builder.append(s);
        for (int i = 0, length = s.length(); i < length; i++) {
            String c = String.valueOf(s.charAt(i));
            if (StringUtil.isInt(c)) {
                int icon = CommonIconUtil.getGiftCountIcon(Integer.parseInt(c));
                Drawable drawable = ContextCompat.getDrawable(CommonApplication.sInstance, icon);
                if (drawable != null) {
                    drawable.setBounds(0, 0, DpUtil.dp2px(18), DpUtil.dp2px(24));
                    builder.setSpan(new ImageSpan(drawable), i, i + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return builder;
    }


}
