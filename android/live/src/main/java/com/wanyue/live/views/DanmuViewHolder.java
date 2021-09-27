package com.wanyue.live.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.bean.LevelBean;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.ScreenDimenUtil;
import com.wanyue.common.views.AbsViewHolder;
import com.wanyue.live.R;
import com.wanyue.live.bean.LiveDanMuBean;

/**
 * Created by  on 2017/8/25.
 * 弹幕
 */

public class DanmuViewHolder extends AbsViewHolder {

    private static final float SPEED = 0.2f;//弹幕的速度，这个值越小，弹幕走的越慢
    private static final int MARGIN_TOP = DpUtil.dp2px(150);
    private static final int SPACE = DpUtil.dp2px(50);
    private static final int DP_15 = DpUtil.dp2px(15);
    private ImageView mAvatar;
    private TextView mName;
    private TextView mContent;
    private int mScreenWidth;//屏幕宽度
    private int mWidth;//控件的宽度
    private ValueAnimator mAnimator;
    private ValueAnimator.AnimatorUpdateListener mUpdateListener;
    private Animator.AnimatorListener mAnimatorListener;
    private boolean mCanNext;//是否可以有下一个
    private boolean mIdle;//是否空闲
    private ActionListener mActionListener;
    private int mLineNum;

    public DanmuViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_gift_danmu;
    }

    @Override
    public void init() {
        mAvatar = (ImageView) findViewById(R.id.avatar);
        mName = (TextView) findViewById(R.id.name);
        mContent = (TextView) findViewById(R.id.content);
        mScreenWidth = ScreenDimenUtil.getInstance().getScreenWdith();
        mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                mContentView.setX(v);
                if (!mCanNext && v <= mScreenWidth - mWidth - DP_15) {
                    mCanNext = true;
                    if (mActionListener != null) {
                        mActionListener.onCanNext(mLineNum);
                    }
                }
            }

        };
        mAnimatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeFromParent();
                mIdle = true;
                if (mActionListener != null) {
                    mActionListener.onAnimEnd(DanmuViewHolder.this);
                }
            }
        };
    }

    public void show(LiveDanMuBean bean, int lineNum) {
        mLineNum = lineNum;
        ImgLoader.display(mContext,bean.getAvatar(), mAvatar);
        mName.setText(bean.getUserNiceName());
        LevelBean levelBean =null
  ;
        if (levelBean != null) {
           //mName.setTextColor(Color.parseColor(levelBean.getColor()));
        }
        mContent.setText(bean.getContent());
        mCanNext = false;
        mContentView.measure(0, 0);
        mWidth = mContentView.getMeasuredWidth();
        mContentView.setX(mScreenWidth);
        mContentView.setY(MARGIN_TOP + lineNum * SPACE);
        addToParent();
        mAnimator = ValueAnimator.ofFloat(mScreenWidth, -mWidth);
        mAnimator.addUpdateListener(mUpdateListener);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration((int) ((mScreenWidth + mWidth) / SPEED));
        mAnimator.addListener(mAnimatorListener);
        mAnimator.start();
    }

    public boolean isIdle() {
        return mIdle;
    }

    public void setIdle(boolean idle) {
        mIdle = idle;
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public void release() {
        if (mAnimator != null) {
            mAnimator.cancel();
        }
        removeFromParent();
        mActionListener = null;
    }

    public interface ActionListener {
        void onCanNext(int lineNum);

        void onAnimEnd(DanmuViewHolder vh);
    }
}
