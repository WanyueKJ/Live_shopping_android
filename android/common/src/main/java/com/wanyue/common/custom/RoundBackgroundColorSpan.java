package com.wanyue.common.custom;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

import com.wanyue.common.utils.DpUtil;

public class RoundBackgroundColorSpan extends ReplacementSpan {
    private int bgColor;
    private int textColor;
    public RoundBackgroundColorSpan(int bgColor, int textColor) {
        super();
        this.bgColor = bgColor;
        this.textColor = textColor;
    }
    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        //设置宽度为文字宽度加6dp
        return ((int)paint.measureText(text, start, end)+ DpUtil.dp2px(6));
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        int originalColor = paint.getColor();
        paint.setColor(this.bgColor);
        //画圆角矩形背景
        canvas.drawRoundRect(new RectF(x,
                        top,
                        x + ((int) paint.measureText(text, start, end)+ DpUtil.dp2px(6)),
                        bottom),
                DpUtil.dp2px(3),
                DpUtil.dp2px(3),
                paint);
        paint.setColor(this.textColor);
        //画文字
        canvas.drawText(text, start, end, x+ DpUtil.dp2px(3), y- DpUtil.dp2px(1), paint);
        //将paint复原
        paint.setColor(originalColor);
    }
}
