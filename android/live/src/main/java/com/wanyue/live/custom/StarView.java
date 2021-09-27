package com.wanyue.live.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.wanyue.live.R;

/**
 * Created by  on 2018/10/17.
 */

public class StarView extends View {

    private Context mContext;
    private int mWidth;
    private float mScale;
    private int mDp14;
    private Paint mPaint;

    public StarView(Context context) {
        this(context, null);
    }

    public StarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mScale = context.getResources().getDisplayMetrics().density;
        mDp14 = dp2px(14);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap bitmap = ((BitmapDrawable) ContextCompat.getDrawable(mContext, R.mipmap.icon_live_star)).getBitmap();
        if (bitmap != null) {
            Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect dst = new Rect(mWidth - mDp14, 0, mWidth, mDp14);
            canvas.drawBitmap(bitmap, src, dst, mPaint);
        }

    }

    private int dp2px(int dpVal) {
        return (int) (dpVal * mScale + 0.5f);
    }
}
