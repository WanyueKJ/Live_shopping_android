package com.yunbao.im.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.yunbao.im.R;

/**
 * Created by  on 2018/7/11.
 */

public class BubbleLayout extends FrameLayout {

    private static final String TAG = "BubbleLayout";
    private static final int DIRECTION_LEFT = 0;
    private static final int DIRECTION_RIGHT = 1;
    private Paint mPaint;
    private float mScale;
    private int mBubbleBgColor;
    private float mBubbleRadius;
    private float mRadiusD;
    private float mArrowWidth;
    private float mArrowHeight;
    private float mArrowPositionY;
    private int mArrowDirecrion;
    private int mWidth;
    private int mHeight;
    private RectF mLeftTopRectF;
    private RectF mLeftBottomRectF;
    private RectF mRightTopRectF;
    private RectF mRightBottomRectF;

    public BubbleLayout(Context context) {
        super(context, null);
    }

    public BubbleLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BubbleLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScale = context.getResources().getDisplayMetrics().density;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BubbleLayout);
        mBubbleBgColor = ta.getColor(R.styleable.BubbleLayout_bubbleBgColor, 0x00ffffff);
        mBubbleRadius = ta.getDimension(R.styleable.BubbleLayout_bubbleRadius, 0);
        mArrowWidth = ta.getDimension(R.styleable.BubbleLayout_arrowWidth, 0);
        mArrowHeight = ta.getDimension(R.styleable.BubbleLayout_arrowHeight, 0);
        mArrowPositionY = ta.getDimension(R.styleable.BubbleLayout_arrowPositionY, 0);
        mArrowDirecrion = ta.getInt(R.styleable.BubbleLayout_arrowDirecrion, DIRECTION_LEFT);
        ta.recycle();
        initPaint();
        mRadiusD = mBubbleRadius * 2;
        mLeftTopRectF = new RectF();
        mLeftBottomRectF = new RectF();
        mRightTopRectF = new RectF();
        mRightBottomRectF = new RectF();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mBubbleBgColor);
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        if (mArrowDirecrion == DIRECTION_LEFT) {
            mLeftTopRectF.left = mArrowHeight;
            mLeftTopRectF.top = 0;
            mLeftTopRectF.right = mArrowHeight + mRadiusD;
            mLeftTopRectF.bottom = mRadiusD;

            mLeftBottomRectF.left = mArrowHeight;
            mLeftBottomRectF.top = mHeight - mRadiusD;
            mLeftBottomRectF.right = mArrowHeight + mRadiusD;
            mLeftBottomRectF.bottom = mHeight;

            mRightTopRectF.left = mWidth - mRadiusD;
            mRightTopRectF.top = 0;
            mRightTopRectF.right = mWidth;
            mRightTopRectF.bottom = mRadiusD;

            mRightBottomRectF.left = mWidth - mRadiusD;
            mRightBottomRectF.top = mHeight - mRadiusD;
            mRightBottomRectF.right = mWidth;
            mRightBottomRectF.bottom = mHeight;
        } else {
            mLeftTopRectF.left = 0;
            mLeftTopRectF.top = 0;
            mLeftTopRectF.right = mRadiusD;
            mLeftTopRectF.bottom = mRadiusD;

            mLeftBottomRectF.left = 0;
            mLeftBottomRectF.top = mHeight - mRadiusD;
            mLeftBottomRectF.right = mRadiusD;
            mLeftBottomRectF.bottom = mHeight;

            mRightTopRectF.left = mWidth - mArrowHeight - mRadiusD;
            mRightTopRectF.top = 0;
            mRightTopRectF.right = mWidth - mArrowHeight;
            mRightTopRectF.bottom = mRadiusD;

            mRightBottomRectF.left = mWidth - mArrowHeight - mRadiusD;
            mRightBottomRectF.top = mHeight - mRadiusD;
            mRightBottomRectF.right = mWidth - mArrowHeight;
            mRightBottomRectF.bottom = mHeight;
        }

    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        Path path = new Path();
        if (mArrowDirecrion == DIRECTION_LEFT) {
            path.moveTo(mArrowHeight, mBubbleRadius);
            path.arcTo(mLeftTopRectF, 180, 90);
            path.rLineTo(mWidth - mArrowHeight - mRadiusD, 0);
            path.arcTo(mRightTopRectF, -90, 90);
            path.rLineTo(0, mHeight - mRadiusD);
            path.arcTo(mRightBottomRectF, 0, 90);
            path.rLineTo(-mWidth + mRadiusD + mArrowHeight, 0);
            path.arcTo(mLeftBottomRectF, 90, 90);
            path.rLineTo(0, -mHeight + mArrowPositionY + mArrowWidth / 2 + mBubbleRadius);
            path.rLineTo(-mArrowHeight, -mArrowWidth / 2);
            path.rLineTo(mArrowHeight, -mArrowWidth / 2);
        } else {
            path.moveTo(mWidth - mArrowHeight, mBubbleRadius);
            path.arcTo(mRightTopRectF, 0, -90);
            path.rLineTo(-mWidth + mArrowHeight + mRadiusD, 0);
            path.arcTo(mLeftTopRectF, 270, -90);
            path.rLineTo(0, mHeight - mRadiusD);
            path.arcTo(mLeftBottomRectF, 180, -90);
            path.rLineTo(mWidth - mArrowHeight - mRadiusD, 0);
            path.arcTo(mRightBottomRectF, 90, -90);
            path.rLineTo(0, -mHeight + mArrowPositionY + mArrowWidth / 2 + mBubbleRadius);
            path.rLineTo(mArrowHeight, -mArrowWidth / 2);
            path.rLineTo(-mArrowHeight, -mArrowWidth / 2);
        }
        path.close();
        canvas.drawPath(path, mPaint);
        super.dispatchDraw(canvas);
    }

    private int dp2px(int dpVal) {
        return (int) (mScale * dpVal + 0.5f);
    }

}
