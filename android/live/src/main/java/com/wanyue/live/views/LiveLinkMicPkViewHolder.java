package com.wanyue.live.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.ScreenDimenUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.common.views.AbsViewHolder;
import com.wanyue.live.R;
import com.wanyue.live.custom.FrameImageView;
import com.wanyue.live.custom.PkProgressBar;
import com.wanyue.live.custom.ProgressTextView;
import com.wanyue.live.utils.LiveIconUtil;

/**
 * Created by  on 2018/11/17.
 * 主播连麦pk相关逻辑
 */

public class LiveLinkMicPkViewHolder extends AbsViewHolder {

    private FrameImageView mFrameImageView;
    private PkProgressBar mPkProgressBar;
    private TextView mLeft;
    private TextView mRight;
    private String mLeftString;
    private String mRightString;
    private int mHalfScreenWidth;
    private ValueAnimator mAnimator1;
    private ValueAnimator mAnimator2;
    private TextView mTime;
    private ImageView mResultImageView;
    private ValueAnimator mEndAnimator1;
    private ScaleAnimation mEndAnim2;
    private ValueAnimator mEndAnimator3;
    private int mOffsetX;
    private int mOffsetY;
    private ProgressTextView mPkWaitProgress;


    public LiveLinkMicPkViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_link_mic_pk;
    }

    @Override
    public void init() {
        mHalfScreenWidth = ScreenDimenUtil.getInstance().getScreenWdith() / 2;
        mLeftString = WordUtil.getString(R.string.live_link_mic_pk_1);
        mRightString = WordUtil.getString(R.string.live_link_mic_pk_2);
        mFrameImageView = (FrameImageView) findViewById(R.id.frame_img);
        mFrameImageView.setImageList(LiveIconUtil.getLinkMicPkAnim());
        mPkProgressBar = (PkProgressBar) findViewById(R.id.progressbar);
        mLeft = (TextView) findViewById(R.id.left);
        mRight = (TextView) findViewById(R.id.right);
        mLeft.setText(mLeftString + "  0");
        mRight.setText("0  " + mRightString);
        mLeft.setTranslationX(-mHalfScreenWidth);
        mRight.setTranslationX(mHalfScreenWidth);
        mAnimator1 = ValueAnimator.ofFloat(0, mHalfScreenWidth);
        mAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                mLeft.setTranslationX(v - mHalfScreenWidth);
                mRight.setTranslationX(mHalfScreenWidth - v);
            }
        });
        mAnimator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mPkProgressBar != null && mPkProgressBar.getVisibility() != View.VISIBLE) {
                    mPkProgressBar.setVisibility(View.VISIBLE);
                }
                if (mLeft != null) {
                    mLeft.setBackground(null);
                }
                if (mRight != null) {
                    mRight.setBackground(null);
                }
            }
        });
        mAnimator1.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator1.setDuration(400);
        mAnimator2 = ValueAnimator.ofFloat(0, 18);
        mAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float index = (float) animation.getAnimatedValue();
                if(mFrameImageView!=null){
                    mFrameImageView.play((int) index);
                }
            }
        });
        mAnimator2.setDuration(800);
        mAnimator2.setInterpolator(new LinearInterpolator());
        mTime = (TextView) findViewById(R.id.time);
        mResultImageView = (ImageView) findViewById(R.id.result);
        mOffsetX = DpUtil.dp2px(75) / 2;
        mOffsetY = DpUtil.dp2px(50) / 2;
        mPkWaitProgress = (ProgressTextView) findViewById(R.id.pk_wait_progress);
    }

    public void startAnim() {
        if (mAnimator1 != null) {
            mAnimator1.start();
        }
        if (mAnimator2 != null) {
            mAnimator2.start();
        }
    }

    public void showTime() {
        if (mTime != null && mTime.getVisibility() != View.VISIBLE) {
            mTime.setVisibility(View.VISIBLE);
        }
    }

    public void hideTime() {
        if (mTime != null && mTime.getVisibility() == View.VISIBLE) {
            mTime.setVisibility(View.INVISIBLE);
        }
    }

    public void setTime(String content) {
        if (mTime != null) {
            mTime.setText(content);
        }
    }

    public void onEnterRoomPkStart() {
        if (mFrameImageView != null) {
            mFrameImageView.setImageResource(R.mipmap.pk19);
        }
        if (mPkProgressBar != null && mPkProgressBar.getVisibility() != View.VISIBLE) {
            mPkProgressBar.setVisibility(View.VISIBLE);
        }
        if (mLeft != null) {
            mLeft.setBackground(null);
            mLeft.setTranslationX(0);
        }
        if (mRight != null) {
            mRight.setBackground(null);
            mRight.setTranslationX(0);
        }
    }

    public void onProgressChanged(long leftGift, long rightGift) {
        mLeft.setText(mLeftString + "  " + leftGift);
        mRight.setText(rightGift + "  " + mRightString);
        if (leftGift == rightGift) {
            mPkProgressBar.setProgress(0.5f);
        } else {
            mPkProgressBar.setProgress(leftGift * 1f / (leftGift + rightGift));
        }
    }


    private ValueAnimator getEndValueAnimator(int result) {
        ValueAnimator valueAnimator = null;
        if (result == 0) {
            valueAnimator = ObjectAnimator.ofFloat(mResultImageView, "translationY", DpUtil.dp2px(80));
        } else {
            int width = mContentView.getWidth();
            int height = mContentView.getHeight();
            Path path = new Path();
            path.lineTo(0, 0);
            path.moveTo(width / 2, height / 2);
            path.arcTo(new RectF(width / 2 - height / 2, height / 2, width / 2 + height / 2, height / 2 + height), -90, result > 0 ? -70 : 70);
            final PathMeasure pathMeasure = new PathMeasure(path, false);
            final float[] position = new float[2];
            valueAnimator = ValueAnimator.ofFloat(0, pathMeasure.getLength());
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float v = (float) animation.getAnimatedValue();
                    pathMeasure.getPosTan(v, position, null);
                    mResultImageView.setX(position[0] - mOffsetX);
                    mResultImageView.setY(position[1] - mOffsetY);
                }
            });
        }
        valueAnimator.setDuration(1500);
        return valueAnimator;
    }


    /**
     * pk结束
     *
     * @param result -1自己的主播输 0平  1自己的主播赢
     */
    public void end(final int result) {
        if (mResultImageView == null) {
            return;
        }
        mEndAnimator1 = ValueAnimator.ofFloat(1, 0.2f);
        mEndAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                mFrameImageView.setScaleX(v);
                mFrameImageView.setScaleY(v);
                mFrameImageView.setAlpha(v);
            }
        });
        mEndAnimator1.setDuration(500);
        mEndAnimator1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mResultImageView != null) {
                    if (mFrameImageView.getVisibility() == View.VISIBLE) {
                        mFrameImageView.setVisibility(View.INVISIBLE);
                    }
                    mResultImageView.setImageResource(result == 0 ? R.mipmap.icon_live_pk_result_ping : R.mipmap.icon_live_pk_result_win);
                    mResultImageView.startAnimation(mEndAnim2);
                }
            }
        });
        mEndAnim2 = new ScaleAnimation(0.2f, 1, 0.2f, 1, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mEndAnim2.setDuration(500);
        mEndAnim2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mEndAnimator3 = getEndValueAnimator(result);
                mEndAnimator3.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mEndAnimator1.start();
    }

    public void release() {
        if (mAnimator1 != null) {
            mAnimator1.cancel();
        }
        if (mAnimator2 != null) {
            mAnimator2.cancel();
        }
        if (mEndAnimator1 != null) {
            mEndAnimator1.cancel();
        }
        if (mEndAnim2 != null) {
            mEndAnim2.cancel();
        }
        if (mFrameImageView != null) {
            mFrameImageView.clearAnimation();
            mFrameImageView.release();
        }
        if (mEndAnimator3 != null) {
            mEndAnimator3.cancel();
        }
    }

    public void setPkWaitProgress(int progress) {
        if (mPkWaitProgress != null && mPkWaitProgress.getVisibility() == View.VISIBLE) {
            mPkWaitProgress.setProgress(progress);
        }
    }

    public void setPkWaitProgressVisible(boolean visible) {
        if (mPkWaitProgress != null) {
            if (visible) {
                if (mPkWaitProgress.getVisibility() != View.VISIBLE) {
                    mPkWaitProgress.setVisibility(View.VISIBLE);
                }
            } else {
                if (mPkWaitProgress.getVisibility() == View.VISIBLE) {
                    mPkWaitProgress.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

}
