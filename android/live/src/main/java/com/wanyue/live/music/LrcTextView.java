package com.wanyue.live.music;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.wanyue.live.R;


/**
 * Created by  on 2017/9/15.
 * 显示歌词的控件，有字体颜色从左向右渐变的效果
 */

public class LrcTextView extends AppCompatTextView {

    private float mProgress;
    private int mProgressColor;
    private int mWidth;
    private int mHeight;
    private Paint mPaint;
    private String mCurString;
    private Paint.FontMetricsInt mFontMetricsInt;

    public LrcTextView(Context context) {
        this(context, null);
    }

    public LrcTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LrcTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LrcTextView);
        mProgress = ta.getFloat(R.styleable.LrcTextView_ltv_progress, 0);
        mProgressColor = ta.getColor(R.styleable.LrcTextView_ltv_progressColor, 0);
        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(mProgressColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
        mCurString = text.toString();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTextSize(getTextSize());
        //画渐变部分的文字
        if(mFontMetricsInt==null){
            mFontMetricsInt = getPaint().getFontMetricsInt();
        }

        int baseLine = (mFontMetricsInt.bottom - mFontMetricsInt.top) / 2 - mFontMetricsInt.bottom + getHeight() / 2
                + getPaddingTop() / 2 - getPaddingBottom() / 2;
        canvas.save();
        int end = (int) (mProgress * mWidth);
        canvas.clipRect(0, 0, end, mHeight);
        canvas.drawText(mCurString, 0, baseLine, mPaint);
        canvas.restore();
    }

    /**
     * @param progress 0~1之间的小数
     */
    public void setProgress(float progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > 1) {
            progress = 1;
        }
        mProgress = progress;
        invalidate();
    }

}
