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

public class PopupLayout extends FrameLayout {

    private static final int DIRECTION_TOP = 0;
    private static final int DIRECTION_BOTTOM = 1;

    private int mBgColor;
    private float mRadius;
    private int mStrokeWidth;
    private int mStrokeColor;
    private float mArrowWidth;
    private float mArrowHeight;
    private int mWidth;
    private int mHeight;
    private Paint mPaint1;
    private Paint mPaint2;
    private RectF mLeftTopRectF;
    private RectF mLeftBottomRectF;
    private RectF mRightTopRectF;
    private RectF mRightBottomRectF;
    private float mRadiusD;
    private int mLineWidth;
    private int mDirection;


    public PopupLayout(Context context) {
        super(context, null);
    }

    public PopupLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PopupLayout);
        mBgColor = ta.getColor(R.styleable.PopupLayout_pop_bg_color, 0);
        mRadius = ta.getDimension(R.styleable.PopupLayout_pop_radius, 0);
        mStrokeWidth = (int) ta.getDimension(R.styleable.PopupLayout_pop_stroke_width, 0);
        mStrokeColor = ta.getColor(R.styleable.PopupLayout_pop_stroke_color, 0);
        mArrowWidth = ta.getDimension(R.styleable.PopupLayout_pop_arrow_width, 0);
        mArrowHeight = ta.getDimension(R.styleable.PopupLayout_pop_arrow_height, 0);
        mDirection = ta.getInt(R.styleable.PopupLayout_pop_arrow_direction, DIRECTION_BOTTOM);
        ta.recycle();
        initPaint();
        mLeftTopRectF = new RectF();
        mLeftBottomRectF = new RectF();
        mRightTopRectF = new RectF();
        mRightBottomRectF = new RectF();
        mRadiusD = mRadius * 2;
        mLineWidth = mStrokeWidth / 2;
    }

    private void initPaint() {
        mPaint1 = new Paint();
        mPaint1.setAntiAlias(true);
        mPaint1.setDither(true);
        mPaint1.setColor(mBgColor);
        mPaint1.setStyle(Paint.Style.FILL);

        if (mStrokeWidth > 0) {
            mPaint2 = new Paint();
            mPaint2.setAntiAlias(true);
            mPaint2.setDither(true);
            mPaint2.setStrokeWidth(mStrokeWidth);
            mPaint2.setColor(mStrokeColor);
            mPaint2.setStyle(Paint.Style.STROKE);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        if (mDirection == DIRECTION_BOTTOM) {
            mLeftTopRectF.left = mLineWidth;
            mLeftTopRectF.top = mLineWidth;
            mLeftTopRectF.right = mLeftTopRectF.left + mRadiusD;
            mLeftTopRectF.bottom = mLeftTopRectF.top + mRadiusD;

            mRightTopRectF.top = mLineWidth;
            mRightTopRectF.right = mWidth - mLineWidth;
            mRightTopRectF.left = mRightTopRectF.right - mRadiusD;
            mRightTopRectF.bottom = mRightTopRectF.top + mRadiusD;

            mLeftBottomRectF.left = mLineWidth;
            mLeftBottomRectF.right = mLeftBottomRectF.left + mRadiusD;
            mLeftBottomRectF.bottom = mHeight - mArrowHeight - mLineWidth;
            mLeftBottomRectF.top = mLeftBottomRectF.bottom - mRadiusD;


            mRightBottomRectF.right = mWidth - mLineWidth;
            mRightBottomRectF.left = mRightBottomRectF.right - mRadiusD;
            mRightBottomRectF.bottom = mHeight - mArrowHeight - mLineWidth;
            mRightBottomRectF.top = mRightBottomRectF.bottom - mRadiusD;
        } else if (mDirection == DIRECTION_TOP) {

            mLeftTopRectF.left = mLineWidth;
            mLeftTopRectF.top = mLineWidth + mArrowHeight;
            mLeftTopRectF.right = mLeftTopRectF.left + mRadiusD;
            mLeftTopRectF.bottom = mLeftTopRectF.top + mRadiusD;

            mRightTopRectF.top = mLeftTopRectF.top;
            mRightTopRectF.right = mWidth - mLineWidth;
            mRightTopRectF.left = mRightTopRectF.right - mRadiusD;
            mRightTopRectF.bottom = mRightTopRectF.top + mRadiusD;

            mLeftBottomRectF.left = mLineWidth;
            mLeftBottomRectF.right = mLeftBottomRectF.left + mRadiusD;
            mLeftBottomRectF.bottom = mHeight - mLineWidth;
            mLeftBottomRectF.top = mLeftBottomRectF.bottom - mRadiusD;


            mRightBottomRectF.right = mWidth - mLineWidth;
            mRightBottomRectF.left = mRightBottomRectF.right - mRadiusD;
            mRightBottomRectF.bottom = mHeight - mLineWidth;
            mRightBottomRectF.top = mRightBottomRectF.bottom - mRadiusD;
        }
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mArrowHeight > 0 && mArrowWidth > 0) {
            Path path = new Path();
            if (mDirection == DIRECTION_BOTTOM) {
                path.moveTo(mWidth / 2, mHeight - mLineWidth);
                path.rLineTo(-mArrowWidth / 2, -mArrowHeight);
                path.rLineTo(-mWidth / 2 + mRadius + mArrowWidth / 2 + mLineWidth, 0);
                path.arcTo(mLeftBottomRectF, 90, 90);
                path.rLineTo(0, -mHeight + mArrowHeight + mRadiusD + mLineWidth * 2);
                path.arcTo(mLeftTopRectF, 180, 90);
                path.rLineTo(mWidth - mRadiusD - mLineWidth * 2, 0);
                path.arcTo(mRightTopRectF, -90, 90);
                path.rLineTo(0, mHeight - mArrowHeight - mRadiusD - mLineWidth * 2);
                path.arcTo(mRightBottomRectF, 0, 90);
                path.rLineTo(-mWidth / 2 + mRadius + mArrowWidth / 2 + mLineWidth, 0);
            } else if (mDirection == DIRECTION_TOP) {
                path.moveTo(mWidth / 2, mLineWidth);
                path.rLineTo(-mArrowWidth / 2, mArrowHeight);
                path.rLineTo(-mWidth / 2 + mRadius + mArrowWidth / 2 + mLineWidth, 0);
                path.arcTo(mLeftTopRectF, -90, -90);
                path.rLineTo(0, mHeight - mArrowHeight - mRadiusD - mLineWidth * 2);
                path.arcTo(mLeftBottomRectF, 180, -90);
                path.rLineTo(mWidth - mRadiusD - mLineWidth * 2, 0);
                path.arcTo(mRightBottomRectF, 90, -90);
                path.rLineTo(0, -mHeight + mArrowHeight + mRadiusD + mLineWidth * 2);
                path.arcTo(mRightTopRectF, 0, -90);
                path.rLineTo(-mWidth / 2 + mRadius + mArrowWidth / 2 + mLineWidth, 0);
            }
            path.close();
            canvas.drawPath(path, mPaint1);
            if (mStrokeWidth > 0) {
                canvas.drawPath(path, mPaint2);
            }

        }
        super.dispatchDraw(canvas);
    }

}
