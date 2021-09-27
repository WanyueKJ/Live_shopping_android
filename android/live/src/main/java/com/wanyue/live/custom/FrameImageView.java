package com.wanyue.live.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import android.util.AttributeSet;
import android.widget.ImageView;

import com.wanyue.live.R;
import com.wanyue.common.utils.BitmapUtil;
import com.wanyue.common.utils.L;

import java.util.List;

/**
 * Created by  on 2018/11/17.
 */

public class FrameImageView extends AppCompatImageView {

    private float mRatio;
    private Bitmap mBitmap;
    private List<Integer> mImageList;
    private int mCurIndex;
    private Paint mPaint;
    private Rect mDst;
    private Rect mSrc;
    private boolean mPlayAnim;

    public FrameImageView(Context context) {
        this(context, null);
    }

    public FrameImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.FrameImageView);
        mRatio = ta.getFloat(R.styleable.FrameImageView_fiv_ratio, 0);
        ta.recycle();
        mPaint = new Paint();
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mDst = new Rect();
        mSrc = new Rect();
        mCurIndex = -1;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec((int) (widthSize * mRatio), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mDst.left = 0;
        mDst.top = 0;
        mDst.right = w;
        mDst.bottom = h;
        mSrc.left = 0;
        mSrc.top = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mPlayAnim) {
            if (mBitmap != null) {
                canvas.drawBitmap(mBitmap, mSrc, mDst, mPaint);
            }
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        mPlayAnim=false;
        super.setImageDrawable(drawable);
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        mPlayAnim=false;
        super.setImageResource(resId);
    }


    public void play(int index) {
        if (mImageList == null || index > mImageList.size() - 1) {
            return;
        }
        if (mCurIndex == index) {
            return;
        }
        mCurIndex = index;
        L.e("FrameImageView----index---->" + index);
        if (mBitmap != null) {
            mBitmap.recycle();
        }
        mBitmap = BitmapUtil.getInstance().decodeBitmap(mImageList.get(index));
        mSrc.right = mBitmap.getWidth();
        mSrc.bottom = mBitmap.getHeight();
        mPlayAnim = true;
        invalidate();
    }

    public void setImageList(List<Integer> imageList) {
        mImageList = imageList;
    }

    public void release() {
        if (mBitmap != null) {
            mBitmap.recycle();
        }
    }

}
