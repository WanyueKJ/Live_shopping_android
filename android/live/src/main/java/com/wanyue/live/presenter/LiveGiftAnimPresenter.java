package com.wanyue.live.presenter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.MediaController;
import android.widget.TextView;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.opensource.svgaplayer.utils.SVGARect;
import com.wanyue.common.Constants;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.CommonHttpConsts;
import com.wanyue.common.http.CommonHttpUtil;
import com.wanyue.common.interfaces.CommonCallback;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.GifCacheUtil;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.MD5Util;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.bean.GlobalGiftBean;
import com.wanyue.live.bean.LiveGiftPrizePoolWinBean;
import com.wanyue.live.bean.LiveLuckGiftWinBean;
import com.wanyue.live.bean.LiveReceiveGiftBean;
import com.wanyue.live.views.LiveGiftLuckTopViewHolder;
import com.wanyue.live.views.LiveGiftViewHolder;
import com.wanyue.live.views.LiveTitleAnimViewHolder;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * Created by  on 2018/10/13.
 * 产品让改礼物效果
 */

public class LiveGiftAnimPresenter {

    private Context mContext;
    private ViewGroup mParent2;
    private SVGAImageView mSVGAImageView;
    private GifImageView mGifImageView;
    private GifDrawable mGifDrawable;
    private View mGifGiftTipGroup;
    private TextView mGifGiftTip;
    private ObjectAnimator mGifGiftTipShowAnimator;
    private ObjectAnimator mGifGiftTipHideAnimator;

    private View mGlobalGiftGroup;
    private TextView[] mGlobalGiftTips;
    private ObjectAnimator mGlobalGiftShowAnimator;//全站礼物
    private ValueAnimator mGlobalGiftMoveAnimator;//全站礼物
    private ObjectAnimator mGlobalGiftHideAnimator;//全站礼物
    private int mMoveGlobalSpace;

    private LiveGiftViewHolder[] mLiveGiftViewHolders;
    private ConcurrentLinkedQueue<LiveReceiveGiftBean> mQueue;
    private ConcurrentLinkedQueue<LiveReceiveGiftBean> mGifQueue;
    private ConcurrentLinkedQueue<GlobalGiftBean> mGlobalGiftQueue;
    private Map<String, LiveReceiveGiftBean> mMap;
    private Handler mHandler;
    private MediaController mMediaController;//koral--/android-gif-drawable 这个库用来播放gif动画的
    private static final int WHAT_GIF = -1;
    private static final int WHAT_ANIM = -2;
    private static final int WHAT_GLOBAL = -3;
    private static final int WHAT_GLOBAL_2 = -4;
    private boolean mShowGif;
    private boolean mShowGlobal;
    private CommonCallback<File> mDownloadGifCallback;
    private int mDp10;
    private int mDp50;
    private int mDp500;
    private LiveReceiveGiftBean mTempGifGiftBean;
    private String mSendString;
    private SVGAParser mSVGAParser;
    private SVGAParser.ParseCompletion mParseCompletionCallback;
    private long mSvgaPlayTime;
    private Map<String, SoftReference<SVGAVideoEntity>> mSVGAMap;
    private ViewGroup mTopLuckContainer;
    private LiveGiftLuckTopViewHolder mLiveGiftLuckTopViewHolder;
    private ViewGroup mLiveGiftPrizePoolContainer;

    private ViewGroup mTitleContainer;
    private LiveTitleAnimViewHolder mTitleAnimViewHolder;


    public LiveGiftAnimPresenter(Context context, View v, GifImageView gifImageView, SVGAImageView svgaImageView, ViewGroup liveGiftPrizePoolContainer) {
        mContext = context;
        mParent2 = (ViewGroup) v.findViewById(R.id.gift_group_1);
        mGifImageView = gifImageView;
        mSVGAImageView = svgaImageView;

        mLiveGiftPrizePoolContainer = liveGiftPrizePoolContainer;
        mTitleContainer = v.findViewById(R.id.title_container);
        mSVGAImageView.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
            }
            @Override
            public void onFinished() {
                long diffTime = 4000 - (System.currentTimeMillis() - mSvgaPlayTime);
                if (diffTime < 0) {
                    diffTime = 0;
                }
                if (mHandler != null) {
                    mHandler.sendEmptyMessageDelayed(WHAT_GIF, diffTime);
                }
            }

            @Override
            public void onRepeat() {

            }

            @Override
            public void onStep(int i, double v) {

            }
        });
        mGifGiftTipGroup = v.findViewById(R.id.gif_gift_tip_group);
        mGifGiftTip = (TextView) v.findViewById(R.id.gif_gift_tip);
        mDp500 = DpUtil.dp2px(500);
        mGifGiftTipShowAnimator = ObjectAnimator.ofFloat(mGifGiftTipGroup, "translationX", mDp500, 0);
        mGifGiftTipShowAnimator.setDuration(1000);
        mGifGiftTipShowAnimator.setInterpolator(new LinearInterpolator());
        mGifGiftTipShowAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mHandler != null) {
                    mHandler.sendEmptyMessageDelayed(WHAT_ANIM, 2000);
                }
            }
        });
        mDp10 = DpUtil.dp2px(10);
        mGifGiftTipHideAnimator = ObjectAnimator.ofFloat(mGifGiftTipGroup, "translationX", 0);
        mGifGiftTipHideAnimator.setDuration(800);
        mGifGiftTipHideAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mGifGiftTipHideAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mGifGiftTipGroup.setAlpha(1 - animation.getAnimatedFraction());
            }
        });
        mSendString = WordUtil.getString(R.string.live_send_gift_3);

        mDp50 = DpUtil.dp2px(50);
        mGlobalGiftGroup = v.findViewById(R.id.global_gift_tip_group);
        mGlobalGiftTips = new TextView[3];
        mGlobalGiftTips[0] = (TextView) v.findViewById(R.id.global_gift_tip_0);
        mGlobalGiftTips[1] = (TextView) v.findViewById(R.id.global_gift_tip_1);
        mGlobalGiftTips[2] = (TextView) v.findViewById(R.id.global_gift_tip_2);
        LinearInterpolator linearInterpolator = new LinearInterpolator();
        mGlobalGiftShowAnimator = ObjectAnimator.ofFloat(mGlobalGiftGroup, "translationX", mDp500, 0);
        mGlobalGiftShowAnimator.setDuration(1000);
        mGlobalGiftShowAnimator.setInterpolator(linearInterpolator);
        mGlobalGiftShowAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mHandler != null) {
                    mHandler.sendEmptyMessageDelayed(WHAT_GLOBAL, 1200);
                }
            }
        });

        mGlobalGiftMoveAnimator = ValueAnimator.ofFloat(0, 2);
        mGlobalGiftMoveAnimator.setInterpolator(linearInterpolator);
        mGlobalGiftMoveAnimator.setDuration(4500);
        mGlobalGiftMoveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float x = ((float) animation.getAnimatedValue()) * mMoveGlobalSpace;
                mGlobalGiftTips[0].setTranslationX(-x);
                mGlobalGiftTips[1].setTranslationX(mMoveGlobalSpace - x);
                mGlobalGiftTips[2].setTranslationX(mMoveGlobalSpace * 2 - x);
            }
        });
        mGlobalGiftMoveAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mHandler != null) {
                    mHandler.sendEmptyMessageDelayed(WHAT_GLOBAL_2, 1200);
                }
            }
        });
        mGlobalGiftHideAnimator = ObjectAnimator.ofFloat(mGlobalGiftGroup, "alpha", 1, 0);
        mGlobalGiftHideAnimator.setDuration(500);
        mGlobalGiftHideAnimator.setRepeatCount(3);
        mGlobalGiftHideAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mGlobalGiftGroup.setTranslationX(mDp500);
                mShowGlobal = false;
                if (mGlobalGiftQueue != null) {
                    GlobalGiftBean bean = mGlobalGiftQueue.poll();
                    if (bean != null) {
                        showGlobalGift(bean);
                    }
                }
            }
        });


        mLiveGiftViewHolders = new LiveGiftViewHolder[2];
        mLiveGiftViewHolders[0] = new LiveGiftViewHolder(context, (ViewGroup) v.findViewById(R.id.gift_group_2));
        mLiveGiftViewHolders[0].addToParent();
        mQueue = new ConcurrentLinkedQueue<>();
        mGifQueue = new ConcurrentLinkedQueue<>();
        mGlobalGiftQueue = new ConcurrentLinkedQueue<>();
        mMap = new HashMap<>();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == WHAT_GIF) {
                    mShowGif = false;
                    if (mGifImageView != null) {
                        mGifImageView.setImageDrawable(null);
                    }
                    if (mGifDrawable != null && !mGifDrawable.isRecycled()) {
                        mGifDrawable.stop();
                        mGifDrawable.recycle();
                    }
                    LiveReceiveGiftBean bean = mGifQueue.poll();
                    if (bean != null) {
                        showGifGift(bean);
                    }
                } else if (msg.what == WHAT_ANIM) {
                    mGifGiftTipHideAnimator.setFloatValues(0, -mDp10 - mGifGiftTipGroup.getWidth());
                    mGifGiftTipHideAnimator.start();
                } else if (msg.what == WHAT_GLOBAL) {
                    mGlobalGiftMoveAnimator.start();
                } else if (msg.what == WHAT_GLOBAL_2) {
                    mGlobalGiftHideAnimator.start();
                } else {
                    LiveGiftViewHolder vh = mLiveGiftViewHolders[msg.what];
                    if (vh != null) {
                        LiveReceiveGiftBean bean = mQueue.poll();
                        if (bean != null) {
                            mMap.remove(bean.getKey());
                            vh.show(bean, false);
                            resetTimeCountDown(msg.what);
                        } else {
                            vh.hide();
                        }
                    }
                }
            }
        };
        mDownloadGifCallback = new CommonCallback<File>() {
            @Override
            public void callback(File file) {
                if (file != null) {
                    playHaoHuaGift(file);
                } else {
                    mShowGif = false;
                }
            }
        };
    }

    public void showGiftAnim(LiveReceiveGiftBean bean) {
        if (bean.getGif() == 1) {
            showGifGift(bean);
        } else {
            showNormalGift(bean);
        }
    }

    /**
     * 显示gif礼物
     */
    private void showGifGift(LiveReceiveGiftBean bean) {
        String url = bean.getGifUrl();
        L.e("gif礼物----->" + bean.getGiftName() + "----->" + url);
        if (TextUtils.isEmpty(url)) {
            return;
        }
        if (mShowGif) {
            if (mGifQueue != null) {
                mGifQueue.offer(bean);
            }
        } else {
            mShowGif = true;
            mTempGifGiftBean = bean;
            if (!url.endsWith(".gif") && !url.endsWith(".svga")) {
                ImgLoader.displayDrawable(mContext, url, new ImgLoader.DrawableCallback() {
                    @Override
                    public void onLoadSuccess(Drawable drawable) {
                        resizeGifImageView(drawable);
                        mGifImageView.setImageDrawable(drawable);
                        showHaoHuaGiftTip();
                        if (mHandler != null) {
                            mHandler.sendEmptyMessageDelayed(WHAT_GIF, 4000);
                        }
                    }

                    @Override
                    public void onLoadFailed() {
                        if (mHandler != null) {
                            mHandler.sendEmptyMessage(WHAT_GIF);
                        }
                    }
                });
            } else {
                GifCacheUtil.getFile(MD5Util.getMD5(url), url, mDownloadGifCallback);
            }
        }
    }


    private void showHaoHuaGiftTip() {
        if (mTempGifGiftBean != null && mTempGifGiftBean.getIsGlobal() == 0) {
            mGifGiftTip.setText(mTempGifGiftBean.getUserNiceName() + "  " + mSendString + mTempGifGiftBean.getGiftName());
            mGifGiftTipGroup.setAlpha(1f);
            mGifGiftTipShowAnimator.start();
        }
    }

    /**
     * 调整mGifImageView的大小
     */
    private void resizeGifImageView(Drawable drawable) {
        float w = drawable.getIntrinsicWidth();
        float h = drawable.getIntrinsicHeight();
        ViewGroup.LayoutParams params = mGifImageView.getLayoutParams();
        params.height = (int) (mGifImageView.getWidth() * h / w);
        mGifImageView.setLayoutParams(params);
    }

    /**
     * 调整mSVGAImageView的大小
     */
    private void resizeSvgaImageView(double w, double h) {
        ViewGroup.LayoutParams params = mSVGAImageView.getLayoutParams();
        params.height = (int) (mSVGAImageView.getWidth() * h / w);
        mSVGAImageView.setLayoutParams(params);
    }

    /**
     * 播放豪华礼物
     */
    private void playHaoHuaGift(File file) {
        if (mTempGifGiftBean.getGitType() == 0) {//豪华礼物类型 0是gif  1是svga
            showHaoHuaGiftTip();
            playGift(file);
        } else {
            SVGAVideoEntity svgaVideoEntity = null;
            if (mSVGAMap != null) {
                SoftReference<SVGAVideoEntity> reference = mSVGAMap.get(mTempGifGiftBean.getGiftId());
                if (reference != null) {
                    svgaVideoEntity = reference.get();
                }
            }
            if (svgaVideoEntity != null) {
                playSVGA(svgaVideoEntity);
            } else {
                decodeSvga(file);
            }
        }
    }

    /**
     * 播放gif
     */
    private void playGift(File file) {
        try {
            mGifDrawable = new GifDrawable(file);
            mGifDrawable.setLoopCount(1);
            resizeGifImageView(mGifDrawable);
            mGifImageView.setImageDrawable(mGifDrawable);
            if (mMediaController == null) {
                mMediaController = new MediaController(mContext);
                mMediaController.setVisibility(View.GONE);
            }
            mMediaController.setMediaPlayer((GifDrawable) mGifImageView.getDrawable());
            mMediaController.setAnchorView(mGifImageView);
            int duration = mGifDrawable.getDuration();
            mMediaController.show(duration);
            if (duration < 4000) {
                duration = 4000;
            }
            if (mHandler != null) {
                mHandler.sendEmptyMessageDelayed(WHAT_GIF, duration);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mShowGif = false;
        }
    }

    /**
     * 播放svga
     */
    private void playSVGA(SVGAVideoEntity svgaVideoEntity) {
        if (mSVGAImageView != null) {
            SVGARect rect = svgaVideoEntity.getVideoSize();
            resizeSvgaImageView(rect.getWidth(), rect.getHeight());
            //SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
            //mSVGAImageView.setImageDrawable(drawable);
            mSVGAImageView.setVideoItem(svgaVideoEntity);
            mSvgaPlayTime = System.currentTimeMillis();
            mSVGAImageView.startAnimation();
            showHaoHuaGiftTip();
        }
    }

    /**
     * 播放svga
     */
    private void decodeSvga(File file) {
        if (mSVGAParser == null) {
            mSVGAParser = new SVGAParser(mContext);
        }
        if (mParseCompletionCallback == null) {
            mParseCompletionCallback = new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                    if (mSVGAMap == null) {
                        mSVGAMap = new HashMap<>();
                    }
                    if (mTempGifGiftBean != null) {
                        mSVGAMap.put(mTempGifGiftBean.getGiftId(), new SoftReference<>(svgaVideoEntity));
                    }
                    playSVGA(svgaVideoEntity);
                }

                @Override
                public void onError() {
                    mShowGif = false;
                }
            };
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            mSVGAParser.decodeFromInputStream(bis, file.getAbsolutePath(), mParseCompletionCallback, true);
        } catch (Exception e) {
            e.printStackTrace();
            mShowGif = false;
        }
    }

    /**
     * 显示普通礼物
     */
    private void showNormalGift(LiveReceiveGiftBean bean) {
        if (mLiveGiftViewHolders[0].isIdle()) {
            if (mLiveGiftViewHolders[1] != null && mLiveGiftViewHolders[1].isSameGift(bean)) {
                mLiveGiftViewHolders[1].show(bean, true);
                resetTimeCountDown(1);
                return;
            }
            mLiveGiftViewHolders[0].show(bean, false);
            resetTimeCountDown(0);
            return;
        }
        if (mLiveGiftViewHolders[0].isSameGift(bean)) {
            mLiveGiftViewHolders[0].show(bean, true);
            resetTimeCountDown(0);
            return;
        }
        if (mLiveGiftViewHolders[1] == null) {
            mLiveGiftViewHolders[1] = new LiveGiftViewHolder(mContext, mParent2);
            mLiveGiftViewHolders[1].addToParent();
        }
        if (mLiveGiftViewHolders[1].isIdle()) {
            mLiveGiftViewHolders[1].show(bean, false);
            resetTimeCountDown(1);
            return;
        }
        if (mLiveGiftViewHolders[1].isSameGift(bean)) {
            mLiveGiftViewHolders[1].show(bean, true);
            resetTimeCountDown(1);
            return;
        }
        String key = bean.getKey();
        if (!mMap.containsKey(key)) {
            mMap.put(key, bean);
            mQueue.offer(bean);

        } else {
            LiveReceiveGiftBean bean1 = mMap.get(key);
            bean1.setLianCount(bean1.getLianCount() + 1);
        }
    }

    private void resetTimeCountDown(int index) {
        if (mHandler != null) {
            mHandler.removeMessages(index);
            mHandler.sendEmptyMessageDelayed(index, 5000);
        }
    }


    public void cancelAllAnim() {
        clearAnim();
        mShowGif = false;
        mShowGlobal = false;
        cancelNormalGiftAnim();
        if (mGifGiftTipGroup != null && mGifGiftTipGroup.getTranslationX() != mDp500) {
            mGifGiftTipGroup.setTranslationX(mDp500);
        }
        if (mGlobalGiftGroup != null && mGlobalGiftGroup.getTranslationX() != mDp500) {
            mGlobalGiftGroup.setTranslationX(mDp500);
        }
    }

    private void cancelNormalGiftAnim() {
        if (mLiveGiftViewHolders[0] != null) {
            mLiveGiftViewHolders[0].cancelAnimAndHide();
        }
        if (mLiveGiftViewHolders[1] != null) {
            mLiveGiftViewHolders[1].cancelAnimAndHide();
        }
    }


    private void clearAnim() {
        CommonHttpUtil.cancel(CommonHttpConsts.DOWNLOAD_GIF);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        if (mGifGiftTipShowAnimator != null) {
            mGifGiftTipShowAnimator.cancel();
        }
        if (mGifGiftTipHideAnimator != null) {
            mGifGiftTipHideAnimator.cancel();
        }
        if (mGlobalGiftShowAnimator != null) {
            mGlobalGiftShowAnimator.cancel();
        }
        if (mGlobalGiftMoveAnimator != null) {
            mGlobalGiftMoveAnimator.cancel();
        }
        if (mGlobalGiftHideAnimator != null) {
            mGlobalGiftHideAnimator.cancel();
        }
        if (mQueue != null) {
            mQueue.clear();
        }
        if (mGifQueue != null) {
            mGifQueue.clear();
        }
        if (mGlobalGiftQueue != null) {
            mGlobalGiftQueue.clear();
        }
        if (mMap != null) {
            mMap.clear();
        }
        if (mMediaController != null) {
            mMediaController.hide();
            mMediaController.setAnchorView(null);
        }
        if (mGifImageView != null) {
            mGifImageView.setImageDrawable(null);
        }
        if (mGifDrawable != null && !mGifDrawable.isRecycled()) {
            mGifDrawable.stop();
            mGifDrawable.recycle();
            mGifDrawable = null;
        }
        if (mSVGAImageView != null) {
            mSVGAImageView.stopAnimation(true);
        }
        if (mSVGAMap != null) {
            mSVGAMap.clear();
        }
        if (mTitleAnimViewHolder != null) {
            mTitleAnimViewHolder.clearAnim();
        }
        if (mLiveGiftLuckTopViewHolder != null) {
            mLiveGiftLuckTopViewHolder.clearAnim();
        }

    }

    public void release() {
        clearAnim();
        if (mGifGiftTipShowAnimator != null) {
            mGifGiftTipShowAnimator.removeAllListeners();
            mGifGiftTipShowAnimator.removeAllUpdateListeners();
        }
        if (mGifGiftTipHideAnimator != null) {
            mGifGiftTipHideAnimator.removeAllListeners();
            mGifGiftTipHideAnimator.removeAllUpdateListeners();
        }
        if (mGlobalGiftShowAnimator != null) {
            mGlobalGiftShowAnimator.removeAllListeners();
            mGlobalGiftShowAnimator.removeAllUpdateListeners();
        }
        if (mGlobalGiftMoveAnimator != null) {
            mGlobalGiftMoveAnimator.removeAllListeners();
            mGlobalGiftMoveAnimator.removeAllUpdateListeners();
        }
        if (mGlobalGiftHideAnimator != null) {
            mGlobalGiftHideAnimator.removeAllListeners();
            mGlobalGiftHideAnimator.removeAllUpdateListeners();
        }
        if (mLiveGiftViewHolders[0] != null) {
            mLiveGiftViewHolders[0].release();
        }
        if (mLiveGiftViewHolders[1] != null) {
            mLiveGiftViewHolders[1].release();
        }
        if (mSVGAImageView != null) {
            mSVGAImageView.setCallback(null);
        }
        if (mTitleAnimViewHolder != null) {
            mTitleAnimViewHolder.release();
        }
        if (mLiveGiftLuckTopViewHolder != null) {
            mLiveGiftLuckTopViewHolder.release();
        }


        mSVGAImageView = null;
        mDownloadGifCallback = null;
        mHandler = null;
        mTitleAnimViewHolder = null;
        mLiveGiftLuckTopViewHolder = null;
    }


    /**
     * 幸运礼物中奖
     */
    public void showLuckGiftWinAnim(LiveLuckGiftWinBean bean) {
        if (mTopLuckContainer == null || bean == null) {
            return;
        }
        if (mLiveGiftLuckTopViewHolder == null) {
            mLiveGiftLuckTopViewHolder = new LiveGiftLuckTopViewHolder(mContext, mTopLuckContainer);
            mLiveGiftLuckTopViewHolder.addToParent();
        }
        mLiveGiftLuckTopViewHolder.show(bean);
    }


    /**
     * 奖池中奖
     */
    public void showPrizePoolWinAnim(LiveGiftPrizePoolWinBean bean) {
        if (mLiveGiftPrizePoolContainer == null) {
            return;
        }
    }

    /**
     * 直播间标题动画
     */
    public void showLiveTitleAnim(String title) {
        if (mTitleContainer == null || mHandler == null) {
            return;
        }
        if (mTitleAnimViewHolder == null) {
            mTitleAnimViewHolder = new LiveTitleAnimViewHolder(mContext, mTitleContainer, mHandler);
            mTitleAnimViewHolder.addToParent();
        }
        mTitleAnimViewHolder.show(title);
    }


    /**
     * 全站礼物
     */
    public void showGlobalGift(GlobalGiftBean bean) {
        if (mShowGlobal) {
            if (mGlobalGiftQueue != null) {
                mGlobalGiftQueue.offer(bean);
            }
            return;
        }
        mShowGlobal = true;
        String s = String.format(WordUtil.getString(R.string.global_gift), bean.getUserName(), bean.getLiveName(), bean.getGiftName());
        mGlobalGiftTips[0].setText(s);
        mGlobalGiftTips[1].setText(s);
        mGlobalGiftTips[2].setText(s);
        mGlobalGiftTips[0].measure(0, 0);
        int width = mGlobalGiftTips[0].getMeasuredWidth();
        L.e("showGlobalGift----width-----> " + width);
        mMoveGlobalSpace = mDp50 + width;
        mGlobalGiftTips[0].setTranslationX(0);
        mGlobalGiftTips[1].setTranslationX(mMoveGlobalSpace);
        mGlobalGiftTips[2].setTranslationX(mMoveGlobalSpace * 2);
        mGlobalGiftGroup.setAlpha(1f);
        mGlobalGiftShowAnimator.start();
    }


}
