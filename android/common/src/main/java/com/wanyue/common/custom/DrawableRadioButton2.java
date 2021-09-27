package com.wanyue.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.util.AttributeSet;

import com.wanyue.common.R;


/**
 * Created by  on 2017/7/19.
 * 可以调节drawable大小的RadioButton
 */

public class DrawableRadioButton2 extends AppCompatRadioButton {

    private int mTopWidth;
    private int mTopHeight;
    private int mLeftWidth;
    private int mLeftHeight;
    private int mRightWidth;
    private int mRightHeight;
    private int mBottomWidth;
    private int mBottomHeight;
    private Drawable mTopDrawable;
    private Drawable mLeftDrawable;
    private Drawable mRightDrawable;
    private Drawable mBottomDrawable;

    public DrawableRadioButton2(Context context) {
        this(context, null);
    }

    public DrawableRadioButton2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableRadioButton2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.DrawableTextView);
        mTopWidth = (int) ta.getDimension(R.styleable.DrawableTextView_dt_top_width, 0);
        mTopHeight = (int) ta.getDimension(R.styleable.DrawableTextView_dt_top_height, 0);
        mLeftWidth = (int) ta.getDimension(R.styleable.DrawableTextView_dt_left_width, 0);
        mLeftHeight = (int) ta.getDimension(R.styleable.DrawableTextView_dt_left_height, 0);
        mRightWidth = (int) ta.getDimension(R.styleable.DrawableTextView_dt_right_width, 0);
        mRightHeight = (int) ta.getDimension(R.styleable.DrawableTextView_dt_right_height, 0);
        mBottomWidth = (int) ta.getDimension(R.styleable.DrawableTextView_dt_bottom_width, 0);
        mBottomHeight = (int) ta.getDimension(R.styleable.DrawableTextView_dt_bottom_height, 0);
        mTopDrawable = ta.getDrawable(R.styleable.DrawableTextView_dt_top_drawable);
        mLeftDrawable = ta.getDrawable(R.styleable.DrawableTextView_dt_left_drawable);
        mRightDrawable = ta.getDrawable(R.styleable.DrawableTextView_dt_right_drawable);
        mBottomDrawable = ta.getDrawable(R.styleable.DrawableTextView_dt_bottom_drawable);
        ta.recycle();

    }

    public void setTopDrawable(Drawable topDrawable) {
        mTopDrawable = topDrawable;
    }

    public void setBottomDrawable(Drawable bottomDrawable) {
        mBottomDrawable = bottomDrawable;
    }

    public void setLeftDrawable(Drawable leftDrawable) {
        mLeftDrawable = leftDrawable;
    }

    public void setRightDrawable(Drawable rightDrawable) {
        mRightDrawable = rightDrawable;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setCompoundDrawablesWithIntrinsicBounds(mLeftDrawable, mTopDrawable, mRightDrawable, mBottomDrawable);
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (left != null) {
            left.setBounds(0, 0, mLeftWidth, mLeftHeight);
        }
        if (top != null) {
            top.setBounds(0, 0, mTopWidth, mTopHeight);
        }
        if (right != null) {
            right.setBounds(0, 0, mRightWidth, mRightHeight);
        }
        if (bottom != null) {
            bottom.setBounds(0, 0, mBottomWidth, mBottomHeight);
        }
        setCompoundDrawables(left, top, right, bottom);
    }

    @Override
    public void setChecked(boolean checked) {
        // super.setChecked(checked);
    }

    public void doToggle() {
        super.setChecked(!isChecked());
    }
}
