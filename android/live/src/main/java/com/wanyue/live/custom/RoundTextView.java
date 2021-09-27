package com.wanyue.live.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.wanyue.live.R;


public class RoundTextView extends AppCompatTextView {

    public RoundTextView(Context context) {
        this(context, null);
    }

    public RoundTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
        TypedArray attributes = context.getTheme().obtainStyledAttributes(attrs, R.styleable.RoundView, defStyleAttr, 0);
        if (attributes != null) {

            int rtvBorderWidth = attributes.getDimensionPixelSize(R.styleable.RoundView_rtvBorderWidth, 0);
            int rtvBorderColor = attributes.getColor(R.styleable.RoundView_rtvBorderColor, Color.BLACK);
            float rtvRadius = attributes.getDimension(R.styleable.RoundView_rtvRadius, 10);
            int rtvBgColor = attributes.getColor(R.styleable.RoundView_rtvBgColor, Color.WHITE);
            attributes.recycle();

            GradientDrawable gd = new GradientDrawable();//创建drawable
            gd.setColor(rtvBgColor);
            gd.setCornerRadius(rtvRadius);
            if (rtvBorderWidth > 0) {
                gd.setStroke(rtvBorderWidth, rtvBorderColor);
            }

            this.setBackground(gd);
        }
    }

    private void init(Context context, AttributeSet attrs) {
    }

    public void setBackgroungColor(@ColorInt int color) {
        GradientDrawable myGrad = (GradientDrawable) getBackground();
        myGrad.setColor(color);
    }
}
