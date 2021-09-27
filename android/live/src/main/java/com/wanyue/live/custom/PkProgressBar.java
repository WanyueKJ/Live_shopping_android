package com.wanyue.live.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.wanyue.live.R;

/**
 * Created by  on 2018/11/17.
 */

public class PkProgressBar extends View {

    private Paint mPaint1;
    private Paint mPaint2;
    private Paint mPaintStroke1;
    private Paint mPaintStroke2;
    private int mMinWidth;
    private float mRate;
    private int mLeftColor;
    private int mRightColor;
    private int mLeftStrokeColor;
    private int mRightStrokeColor;
    private Rect mRect1;
    private Rect mRect2;
    private int mWidth;
    private float mScale;

    public PkProgressBar(Context context) {
        this(context, null);
    }

    public PkProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PkProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScale = context.getResources().getDisplayMetrics().density;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PkProgressBar);
        mMinWidth = (int) ta.getDimension(R.styleable.PkProgressBar_ppb_minWidth, 0);
        mRate = ta.getFloat(R.styleable.PkProgressBar_ppb_rate, 0.5f);
        mLeftColor = ta.getColor(R.styleable.PkProgressBar_ppb_left_color, 0);
        mRightColor = ta.getColor(R.styleable.PkProgressBar_ppb_right_color, 0);
        mLeftStrokeColor = ta.getColor(R.styleable.PkProgressBar_ppb_left_color_stroke, 0);
        mRightStrokeColor = ta.getColor(R.styleable.PkProgressBar_ppb_right_color_stroke, 0);
        ta.recycle();
        init();
    }

    private int dp2px(int dpVal) {
        return (int) (dpVal * mScale + 0.5f);
    }

    private void init() {
        mPaint1 = new Paint();
        mPaint1.setAntiAlias(true);
        mPaint1.setDither(true);
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint1.setColor(mLeftColor);

        mPaintStroke1 = new Paint();
        mPaintStroke1.setAntiAlias(true);
        mPaintStroke1.setDither(true);
        mPaintStroke1.setStyle(Paint.Style.STROKE);
        mPaintStroke1.setStrokeWidth(dp2px(1));
        mPaintStroke1.setColor(mLeftStrokeColor);

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setDither(true);
        mPaint2.setStyle(Paint.Style.FILL);
        mPaint2.setColor(mRightColor);


        mPaintStroke2 = new Paint();
        mPaintStroke2.setAntiAlias(true);
        mPaintStroke2.setDither(true);
        mPaintStroke2.setStyle(Paint.Style.STROKE);
        mPaintStroke2.setStrokeWidth(dp2px(1));
        mPaintStroke2.setColor(mRightStrokeColor);

        mRect1 = new Rect();
        mRect2 = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mWidth = w;
        mRect1.left = 0;
        mRect1.top = 0;
        mRect1.bottom = h;
        mRect2.top = 0;
        mRect2.right = w;
        mRect2.bottom = h;
        changeProgress();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(mRect1, mPaint1);
        canvas.drawRect(mRect1, mPaintStroke1);
        canvas.drawRect(mRect2, mPaint2);
        canvas.drawRect(mRect2, mPaintStroke2);
    }

    public void setProgress(float rate) {
        if (mRate == rate) {
            return;
        }
        mRate = rate;
        changeProgress();
        invalidate();
    }

    private void changeProgress() {
        int bound = (int) (mWidth * mRate);
        if (bound < mMinWidth) {
            bound = mMinWidth;
        }
        if (bound > mWidth - mMinWidth) {
            bound = mWidth - mMinWidth;
        }
        mRect1.right = bound;
        mRect2.left = bound;
    }


}
