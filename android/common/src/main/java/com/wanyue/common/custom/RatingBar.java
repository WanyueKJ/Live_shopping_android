package com.wanyue.common.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.wanyue.common.R;
import com.wanyue.common.utils.L;

/**
 * Created by  on 2019/8/7.
 */

public class RatingBar extends FrameLayout implements SeekBar.OnSeekBarChangeListener {

    private Context mContext;
    private int mStarCount;
    private int mFillCount;
    private int mStarWidth;
    private int mStarHeight;
    private int mStarSpace;
    private Drawable mNormalDrawable;
    private Drawable mFillDrawable;
    private SeekBar mSeekBar;
    private ImageView[] mImageViews;
    private OnRatingChangedListener mOnRatingChangedListener;

    public RatingBar(@NonNull Context context) {
        this(context, null);
    }

    public RatingBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingBar(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
        mStarCount = ta.getInt(R.styleable.RatingBar_rtb_star_count, 0);
        mFillCount = ta.getInt(R.styleable.RatingBar_rtb_fill_count, 0);
        mStarWidth = (int) ta.getDimension(R.styleable.RatingBar_rtb_star_width, 0);
        mStarHeight = (int) ta.getDimension(R.styleable.RatingBar_rtb_star_height, 0);
        mStarSpace = (int) ta.getDimension(R.styleable.RatingBar_rtb_star_space, 0);
        mNormalDrawable = ta.getDrawable(R.styleable.RatingBar_rtb_normal_drawable);
        mFillDrawable = ta.getDrawable(R.styleable.RatingBar_rtb_fill_drawable);
        ta.recycle();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mSeekBar = new SeekBar(mContext);
        mSeekBar.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mSeekBar.setThumb(null);
        mSeekBar.setProgressDrawable(null);
        mSeekBar.setBackground(new ColorDrawable(0x00000000));
        mSeekBar.setMax(mStarCount);
        mSeekBar.setProgress(mFillCount);
        mSeekBar.setOnSeekBarChangeListener(this);
        addView(mSeekBar);
        LinearLayout linearLayout = new LinearLayout(mContext);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        mImageViews = new ImageView[mStarCount];
        for (int i = 0; i < mStarCount; i++) {
            ImageView imageView = new ImageView(mContext);
            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(mStarWidth, mStarHeight);
            if (i > 0) {
                p.leftMargin = mStarSpace;
            }
            imageView.setLayoutParams(p);
            if (i <= mFillCount - 1) {
                imageView.setImageDrawable(mFillDrawable);
            } else {
                imageView.setImageDrawable(mNormalDrawable);
            }
            mImageViews[i] = imageView;
            linearLayout.addView(imageView);
        }
        addView(linearLayout);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        mFillCount = progress;
        for (int i = 0; i < mStarCount; i++) {
            ImageView imageView = mImageViews[i];
            if (i <= progress - 1) {
                imageView.setImageDrawable(mFillDrawable);
            } else {
                imageView.setImageDrawable(mNormalDrawable);
            }
        }
        if (mOnRatingChangedListener != null) {
            mOnRatingChangedListener.onRatingChanged(progress, mStarCount);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public int getFillCount() {
        if (mSeekBar != null) {
            return mSeekBar.getProgress();
        }
        return 0;
    }

    public void setOnRatingChangedListener(OnRatingChangedListener onRatingChangedListener) {
        mOnRatingChangedListener = onRatingChangedListener;
    }

    public interface OnRatingChangedListener {
        void onRatingChanged(int curCount, int maxCount);
    }
}
