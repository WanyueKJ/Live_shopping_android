package com.wanyue.live.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.wanyue.live.R;

/**
 * Created by  on 2018/10/7.
 */

public class MyRelativeLayout1 extends RelativeLayout {

    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private int mRadius;
    private int mBgColor;
    private int mInnerWidth;
    private int mInnerHeight;
    private int mInnerRadius;
    private int mInnerX;
    private int mInnerY;
    private int mLineMargin;
    private int mLineMarginTop;
    private int mLineHeight;

    public MyRelativeLayout1(Context context) {
        this(context, null);
    }

    public MyRelativeLayout1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRelativeLayout1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyRelativeLayout1);
        mRadius = (int) ta.getDimension(R.styleable.MyRelativeLayout1_mrl_radius, 0);
        mBgColor = ta.getColor(R.styleable.MyRelativeLayout1_mrl_bg_color, Color.BLACK);
        mInnerWidth = (int) ta.getDimension(R.styleable.MyRelativeLayout1_mrl_inner_w, 0);
        mInnerHeight = (int) ta.getDimension(R.styleable.MyRelativeLayout1_mrl_inner_h, 0);
        mInnerRadius = (int) ta.getDimension(R.styleable.MyRelativeLayout1_mrl_inner_r, 0);
        mInnerX = (int) ta.getDimension(R.styleable.MyRelativeLayout1_mrl_inner_x, 0);
        mInnerY = (int) ta.getDimension(R.styleable.MyRelativeLayout1_mrl_inner_y, 0);
        mLineMargin = (int) ta.getDimension(R.styleable.MyRelativeLayout1_mrl_line_m, 0);
        mLineMarginTop = (int) ta.getDimension(R.styleable.MyRelativeLayout1_mrl_line_mt, 0);
        mLineHeight = (int) ta.getDimension(R.styleable.MyRelativeLayout1_mrl_line_h, 0);
        ta.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mBgColor);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Path path1 = new Path();
        path1.addRoundRect(new RectF(0, 0, mWidth, mHeight), mRadius, mRadius, Path.Direction.CW);
        Path path2 = new Path();
        path2.addRoundRect(new RectF(mInnerX, mInnerY, mInnerX + mInnerWidth, mInnerY + mInnerHeight), mInnerRadius, mInnerRadius, Path.Direction.CW);
        Path path3 = new Path();
        int lineY = mInnerY + mInnerHeight + mLineMarginTop;
        path3.addRect(mLineMargin, lineY, mWidth - mLineMargin, lineY + mLineHeight, Path.Direction.CW);
        path1.op(path2, Path.Op.DIFFERENCE);
        path1.op(path3, Path.Op.DIFFERENCE);
        canvas.drawPath(path1, mPaint);
    }

}
