package com.wanyue.common.custom;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;

import org.jetbrains.annotations.NotNull;

/**
 * Created by  on 2017/12/1.
 */

public class VerticalImageSpan extends ImageSpan {
    private Drawable drawable;
    public VerticalImageSpan(Drawable drawable) {
        super(drawable);
        this.drawable=drawable;
    }

    @Override
    public int getSize(@NotNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fontMetricsInt) {
        Drawable drawable = getDrawable();
        if(drawable==null){
            drawable= this.drawable;
        }
        Rect rect = drawable.getBounds();
        if (fontMetricsInt != null) {
            Paint.FontMetricsInt fmPaint = paint.getFontMetricsInt();
            int fontHeight = fmPaint.bottom - fmPaint.top;
            int drHeight = rect.bottom - rect.top;
            int top = drHeight / 2 - fontHeight / 4;
            int bottom = drHeight / 2 + fontHeight / 4;
            fontMetricsInt.ascent = -bottom;
            fontMetricsInt.top = -bottom;
            fontMetricsInt.bottom = top;
            fontMetricsInt.descent = top;
        }
        return rect.right;
    }
    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NotNull Paint paint) {
        Drawable drawable = getDrawable();
        canvas.save();
        int transY = ((bottom - top) - drawable.getBounds().bottom) / 2 + top;
        canvas.translate(x, transY);
        drawable.draw(canvas);
        canvas.restore();
    }
}
