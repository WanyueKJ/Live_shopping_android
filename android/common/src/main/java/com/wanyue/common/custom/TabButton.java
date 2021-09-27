package com.wanyue.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wanyue.common.R;

/**
 * Created by  on 2018/9/21.
 */

public class TabButton extends LinearLayout {

    private Context mContext;
    private float mScale;
    private String mTip;
    private int mIconSize;
    private int mTextSize;
    private int mTextColorChecked;
    private int mTextColorUnChecked;
    private boolean mChecked;
    private ImageView mImg;
    private TextView mText;
    private Drawable mUnCheckedDrawable;
    private Drawable mCheckedDrawable;

    public TabButton(Context context) {
        this(context, null);
    }

    public TabButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mScale = context.getResources().getDisplayMetrics().density;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabButton);
        int iconArrayId = ta.getResourceId(R.styleable.TabButton_tbn_icon_array_id, 0);
        mTip = ta.getString(R.styleable.TabButton_tbn_tip);
        mIconSize = (int) ta.getDimension(R.styleable.TabButton_tbn_icon_size, 0);
        mTextSize = (int) ta.getDimension(R.styleable.TabButton_tbn_text_size, 0);
        mTextColorChecked = ta.getColor(R.styleable.TabButton_tbn_text_color_checked, 0);
        mTextColorUnChecked = ta.getColor(R.styleable.TabButton_tbn_text_color_unchecked, 0);
        mChecked = ta.getBoolean(R.styleable.TabButton_tbn_checked, false);
        ta.recycle();
        if (iconArrayId != 0) {
            TypedArray arr = getResources().obtainTypedArray(iconArrayId);
            mUnCheckedDrawable = ContextCompat.getDrawable(context, arr.getResourceId(0, 0));
            mCheckedDrawable = ContextCompat.getDrawable(context, arr.getResourceId(1, 0));
            arr.recycle();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);
        mImg = new ImageView(mContext);
        LayoutParams params1 = new LayoutParams(mIconSize, mIconSize);
        params1.setMargins(0, dp2px(5), 0, dp2px(4));
        mImg.setLayoutParams(params1);
        mImg.setImageDrawable(mChecked ? mCheckedDrawable : mUnCheckedDrawable);
        mText = new TextView(mContext);
        LayoutParams params2 = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mText.setLayoutParams(params2);
        mText.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSize);
        mText.setText(mTip);
        mText.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mText.setTextColor(mChecked ? mTextColorChecked : mTextColorUnChecked);
        addView(mImg);
        addView(mText);
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
        mImg.setImageDrawable(mChecked ? mCheckedDrawable : mUnCheckedDrawable);
        if (mChecked) {
            if (mText != null) {
                mText.setTextColor(mTextColorChecked);

            }
            if (mImg != null) {
                mImg.setImageDrawable(mCheckedDrawable);
            }
        } else {
            if (mImg != null) {
                mImg.setImageDrawable(mUnCheckedDrawable);
            }
            if (mText != null) {
                mText.setTextColor(mTextColorUnChecked);

            }
        }
    }

    private int dp2px(int dpVal) {
        return (int) (mScale * dpVal + 0.5f);
    }

    public void cancelAnim() {
    }

}
