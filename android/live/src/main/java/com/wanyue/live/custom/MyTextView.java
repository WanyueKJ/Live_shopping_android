package com.wanyue.live.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.wanyue.live.R;
import com.wanyue.live.bean.ImpressBean;

/**
 * Created by  on 2018/10/15.
 */

public class MyTextView extends AppCompatTextView {

    private Paint mPaint;
    private Paint mPaint2;
    private int mRadius;
    private boolean mChecked;
    private int mColor;
    private int mColor2;
    private RectF mRectF;
    private float mScale;
    private int mStrokeWidth;
    private ImpressBean mBean;

    public MyTextView(Context context) {
        this(context, null);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScale = context.getResources().getDisplayMetrics().density;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MyTextView);
        mRadius = (int) ta.getDimension(R.styleable.MyTextView_mt_radius, 0);
        mChecked = ta.getBoolean(R.styleable.MyTextView_mt_checked, false);
        mColor = ta.getColor(R.styleable.MyTextView_mt_color, 0);
        ta.recycle();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mStrokeWidth = dp2px(1);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        if (mColor != 0) {
            mPaint.setColor(mColor);
        }
        mRectF = new RectF();

        mPaint2 = new Paint();
        mPaint2.setAntiAlias(true);
        mPaint2.setDither(true);
        mPaint2.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mRectF.top = mStrokeWidth;
        mRectF.left = mStrokeWidth;
        mRectF.right = w - mStrokeWidth;
        mRectF.bottom = h - mStrokeWidth;
        LinearGradient linearGradient = new LinearGradient(0, 0, w, 0, mColor, mColor2, Shader.TileMode.CLAMP);
        mPaint2.setShader(linearGradient);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mChecked) {
            if (mPaint2 != null) {
                canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint2);
            }
        } else {
            if (mPaint != null) {
                canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
            }
        }
        super.onDraw(canvas);
    }

    private int dp2px(int dpVal) {
        return (int) (mScale * dpVal + 0.5f);
    }


    public void toggleChecked() {
        setChecked(!mChecked);
    }

    public void setChecked(boolean checked) {
        if (mChecked == checked) {
            return;
        }
        mChecked = checked;
        if (checked) {
            setTextColor(0xffffffff);
        } else {
            setTextColor(mColor);
        }
    }

    public ImpressBean getBean() {
        return mBean;
    }

    public void setBean(ImpressBean bean) {
        setBean(bean, false);
    }

    public void setBean(ImpressBean bean, boolean showNum) {
        mBean = bean;
        if (!TextUtils.isEmpty(bean.getColor())) {
            mColor = Color.parseColor(bean.getColor());
        }
        if (!TextUtils.isEmpty(bean.getColor2())) {
            mColor2 = Color.parseColor(bean.getColor2());
        }
        mPaint.setColor(mColor);
        mChecked = bean.isChecked();
        if (showNum) {
            setText(bean.getName() + "(" + bean.getNums() + ")");
        } else {
            setText(bean.getName());
        }
        if (mChecked) {
            setTextColor(0xffffffff);
        } else {
            setTextColor(mColor);
        }
    }


    public boolean isChecked() {
        return mChecked;
    }
}
