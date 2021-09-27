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

public class MusicProgressTextView extends AppCompatTextView {

    private String mStartText;
    private String mEndText;
    private int mBgColor;
    private int mLineColor;
    private int mProgress;
    private RectF mRectF;
    private int mRadius;
    private Paint mPaint1;
    private Paint mPaint2;
    private Paint mPaint3;
    private boolean mStartProgress;

    public MusicProgressTextView(Context context) {
        this(context, null);
    }

    public MusicProgressTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicProgressTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MusicProgressTextView);
        mStartText = ta.getString(R.styleable.MusicProgressTextView_mptv_start_text);
        mEndText = ta.getString(R.styleable.MusicProgressTextView_mptv_end_text);
        mBgColor = ta.getColor(R.styleable.MusicProgressTextView_mptv_bg_color, 0);
        mLineColor = ta.getColor(R.styleable.MusicProgressTextView_mptv_line_color, 0);
        ta.recycle();
        init();
    }

    private void init() {
        mPaint1 = new Paint();
        mPaint1.setAntiAlias(true);
        mPaint1.setDither(true);
        mPaint1.setStyle(Paint.Style.FILL);
        mPaint1.setColor(mBgColor);

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setDither(true);
        mPaint2.setStyle(Paint.Style.FILL);
        //mPaint2.setStrokeWidth(mStrokeWidth);
        mPaint2.setColor(mLineColor);

        mPaint3 = new Paint();
        mPaint3.setAntiAlias(true);
        mPaint3.setDither(true);
        mPaint3.setStyle(Paint.Style.FILL);
        mPaint3.setColor(mLineColor);
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
        mRadius = w / 2;
        mRectF.left = 0;
        mRectF.top = 0;
        mRectF.right = w;
        mRectF.bottom = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mStartProgress) {
            if (mProgress == 0) {
                canvas.drawCircle(mRadius, mRadius, mRadius, mPaint1);
            } else if (mProgress == 100) {
                canvas.drawCircle(mRadius, mRadius, mRadius, mPaint3);
            } else {
                canvas.drawCircle(mRadius, mRadius, mRadius, mPaint1);
                canvas.drawArc(mRectF, -90, 360 * mProgress / 100f, true, mPaint2);
            }
        }
        super.onDraw(canvas);
    }


    public void setProgress(int progress) {
        if (progress > 100) {
            progress = 100;
        }
        if (progress < 0) {
            progress = 0;
        }
        mStartProgress = true;
        mProgress = progress;
        if (progress == 0) {
            setText(mStartText);
        } else if (progress == 100) {
            setText(mEndText);
        } else {
            setText(progress + "%");
        }
    }

}
