package com.wanyue.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.wanyue.common.R;

/**
 * Created by  on 2018/7/11.
 */

public class CoinGiveLayout extends FrameLayout {

    private static final String TAG = "CoinGiveLayout";
    private Paint mPaint;
    private int mBgColor;
    private float mRadius;
    private float mRadiusD;
    private float mArrowWidth;
    private float mArrowHeight;
    private float mArrowOffsetX;
    private int mWidth;
    private int mHeight;
    private RectF mRectF;

    public CoinGiveLayout(Context context) {
        super(context, null);
    }

    public CoinGiveLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CoinGiveLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CoinGiveLayout);
        mBgColor = ta.getColor(R.styleable.CoinGiveLayout_cgl_bg_color, 0);
        mRadius = ta.getDimension(R.styleable.CoinGiveLayout_cgl_radius, 0);
        mArrowWidth = ta.getDimension(R.styleable.CoinGiveLayout_cgl_arrow_width, 0);
        mArrowHeight = ta.getDimension(R.styleable.CoinGiveLayout_cgl_arrow_height, 0);
        mArrowOffsetX = ta.getDimension(R.styleable.CoinGiveLayout_cgl_arrow_offset_x, 0);
        ta.recycle();
        initPaint();
        mRadiusD = mRadius * 2;
        mRectF = new RectF();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRectF.left = 0;
        mRectF.top = 0;
        mRectF.right = w;
        float bottom = h - mArrowHeight;
        mRectF.bottom = bottom > 0 ? bottom : 0;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.drawRoundRect(mRectF, mRadius, mRadiusD, mPaint);
        if (mArrowHeight > 0 && mArrowWidth > 0) {
            Path path = new Path();
            path.moveTo(mWidth - mArrowWidth - mArrowOffsetX, mHeight - mArrowHeight);
            path.rLineTo(0, mArrowHeight);
            path.rLineTo(mArrowWidth, -mArrowHeight);
            path.close();
            canvas.drawPath(path, mPaint);
        }
        super.dispatchDraw(canvas);
    }

}
