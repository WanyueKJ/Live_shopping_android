package com.wanyue.live.custom;

import android.content.Context;
import android.content.res.TypedArray;
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

import pl.droidsonroids.gif.GifDrawable;

/**
 * Created by  on 2018/10/14.
 */

public class GiftMarkView extends View {

    private Context mContext;
    private int mWidth;
    private int mIconRes1;
    private int mIconRes2;
    private Paint mPaint;
    private float mScale;
    private int mDp24;
    private int mDp12;
    private int mDp10;


    public GiftMarkView(Context context) {
        this(context, null);
    }

    public GiftMarkView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GiftMarkView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mScale = context.getResources().getDisplayMetrics().density;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GiftMarkView);
        mIconRes1 = ta.getResourceId(R.styleable.GiftMarkView_gm_icon_res_1, 0);
        mIconRes2 = ta.getResourceId(R.styleable.GiftMarkView_gm_icon_res_2, 0);
        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mDp24 = dp2px(24);
        mDp12 = dp2px(12);
        mDp10 = dp2px(10);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mIconRes1 != 0) {
            Bitmap bitmap = ((BitmapDrawable) ContextCompat.getDrawable(mContext, mIconRes1)).getBitmap();
            Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect dst = new Rect(0, 0, mDp24, mDp10);
            canvas.drawBitmap(bitmap, src, dst, mPaint);
        }
        if (mIconRes2 != 0) {
            Bitmap bitmap = ((BitmapDrawable) ContextCompat.getDrawable(mContext, mIconRes2)).getBitmap();
            Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect dst = new Rect(mWidth - mDp12, 0, mWidth, mDp10);
            canvas.drawBitmap(bitmap, src, dst, mPaint);

        }
    }

    public void setIconRes(int res1, int res2) {
        mIconRes1 = res1;
        mIconRes2 = res2;
        invalidate();
    }

    private int dp2px(int dpVal) {
        return (int) (dpVal * mScale + 0.5f);
    }
}
