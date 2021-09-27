package com.wanyue.live.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.wanyue.live.R;

/**
 * Created by  on 2017/9/2.
 * 带有环形进度条的TextView
 */

public class ProgressTextView extends AppCompatTextView {

    public static final int MAX_PROGRESS = 10;
    private int mBgColor;
    private int mFgColor;
    private int mStrokeWidth;
    private RectF mRectF;
    private int mRadius;
    private int mX;
    private Paint mPaint1;
    private Paint mPaint2;
    private int mProgress;

    public ProgressTextView(Context context) {
        this(context, null);
    }

    public ProgressTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ProgressTextView);
        mBgColor = ta.getColor(R.styleable.ProgressTextView_ptv_bg_color, 0);
        mFgColor = ta.getColor(R.styleable.ProgressTextView_ptv_fg_color, 0);
        mStrokeWidth = (int) ta.getDimension(R.styleable.ProgressTextView_ptv_stroke_width, 0);
        mProgress = ta.getInteger(R.styleable.ProgressTextView_ptv_progress, MAX_PROGRESS);
        ta.recycle();
        init();
    }

    private void init() {
        mPaint1 = new Paint();
        mPaint1.setAntiAlias(true);
        mPaint1.setDither(true);
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setColor(mBgColor);
        mPaint1.setStrokeWidth(mStrokeWidth);

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setDither(true);
        mPaint2.setStyle(Paint.Style.STROKE);
        mPaint2.setColor(mFgColor);
        mPaint2.setStrokeWidth(mStrokeWidth);

        mRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mX = w / 2;
        int offset = mStrokeWidth / 2;
        mRadius = mX - offset;
        mRectF.left = offset;
        mRectF.top = offset;
        mRectF.right = w - offset;
        mRectF.bottom = w - offset;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mProgress == 0) {
            canvas.drawCircle(mX, mX, mRadius, mPaint1);
        } else if (mProgress == MAX_PROGRESS) {
            canvas.drawCircle(mX, mX, mRadius, mPaint2);
        } else {
            canvas.drawCircle(mX, mX, mRadius, mPaint1);
            canvas.drawArc(mRectF, -90, 360 * mProgress / 10f, false, mPaint2);
        }
        super.onDraw(canvas);
    }


    public void setProgress(int progress) {
        if (progress > MAX_PROGRESS) {
            progress = MAX_PROGRESS;
        }
        if (progress < 0) {
            progress = 0;
        }
        if (mProgress == progress) {
            return;
        }
        mProgress = progress;
        setText(String.valueOf(progress));
    }

}
