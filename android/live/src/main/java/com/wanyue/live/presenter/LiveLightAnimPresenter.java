package com.wanyue.live.presenter;

import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.RandomUtil;
import com.wanyue.common.utils.ScreenDimenUtil;
import com.wanyue.live.custom.LiveLightView;
import com.wanyue.live.utils.LiveIconUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by  on 2018/10/11.
 * 直播间飘心动画逻辑
 */

public class LiveLightAnimPresenter {

    private Context mContext;
    private ViewGroup mContainer;
    private int mStartX;
    private int mStartY;
    private PathMeasure[] mPathMeasures;
    private List<LiveLightView> mViewList;
    private TimeInterpolator mInterpolator;
    private boolean mEnd;


    public LiveLightAnimPresenter(Context context, ViewGroup container) {
        LiveLightView.sOffsetY = 0;
        mContext = context;
        mContainer = container;
        ScreenDimenUtil screenDimenUtil = ScreenDimenUtil.getInstance();
        mStartX = screenDimenUtil.getScreenWdith() * 4 / 5;
        mStartY = screenDimenUtil.getScreenHeight() - DpUtil.dp2px(50);
        mPathMeasures = new PathMeasure[6];
        int dp50 = DpUtil.dp2px(50);
        int dp100 = DpUtil.dp2px(100);
        int dp200 = DpUtil.dp2px(200);
        int dp300 = DpUtil.dp2px(300);
        //第1条轨迹
        Path path = new Path();
        path.lineTo(0, 0);
        path.moveTo(mStartX, mStartY);
        path.rCubicTo(0, -dp100, -dp100, -dp200, -dp100, -dp300);
        mPathMeasures[0] = new PathMeasure(path, false);

        //第2条轨迹
        path = new Path();
        path.lineTo(0, 0);
        path.moveTo(mStartX, mStartY);
        path.rCubicTo(0, -dp200, -dp50, -dp200, -dp50, -dp300);
        mPathMeasures[1] = new PathMeasure(path, false);

        //第3条轨迹
        path = new Path();
        path.lineTo(0, 0);
        path.moveTo(mStartX, mStartY);
        path.rCubicTo(dp100, -dp100, 0, -dp300, -dp50, -dp300);
        mPathMeasures[2] = new PathMeasure(path, false);

        //第4条轨迹
        path = new Path();
        path.lineTo(0, 0);
        path.moveTo(mStartX, mStartY);
        path.rCubicTo(0, -dp100, dp100, -dp200, 0, -dp300);
        mPathMeasures[3] = new PathMeasure(path, false);

        //第5条轨迹
        path = new Path();
        path.lineTo(0, 0);
        path.moveTo(mStartX, mStartY);
        path.rCubicTo(0, -dp200, dp50, -dp200, dp50, -dp300);
        mPathMeasures[4] = new PathMeasure(path, false);

        //第6条轨迹
        path = new Path();
        path.lineTo(0, 0);
        path.moveTo(mStartX, mStartY);
        path.rCubicTo(-dp100, -dp100, 0, -dp300, dp50, -dp300);
        mPathMeasures[5] = new PathMeasure(path, false);
        mViewList = new ArrayList<>();
        mInterpolator = new AccelerateDecelerateInterpolator();
    }

    /**
     * 播放飘心动画，每次飘一个心
     */
    public void play() {
        if (mEnd) {
            return;
        }
        LiveLightView liveLightView = getIdleImageView();
        if (liveLightView != null) {
            liveLightView.setImageResource(LiveIconUtil.getLiveLightIcon(1 + RandomUtil.nextInt(6)));
            liveLightView.play(mPathMeasures[RandomUtil.nextInt(6)]);
        }
    }


    /**
     * 获得空闲的ImageView
     */
    private LiveLightView getIdleImageView() {
        if (mEnd) {
            return null;
        }
        for (LiveLightView view : mViewList) {
            if (view.isIdle()) {
                view.setIdle(false);
                return view;
            }
        }
        if (mViewList.size() < 10) {
            LiveLightView view = new LiveLightView(mContext);
            view.setLayoutParams(new ViewGroup.LayoutParams(DpUtil.dp2px(26), DpUtil.dp2px(26)));
            view.setIdle(false);
            view.setInterpolator(mInterpolator);
            mViewList.add(view);
            mContainer.addView(view);
            return view;
        }
        return null;
    }

    public void release() {
        mEnd = true;
        if (mViewList != null) {
            for (LiveLightView view : mViewList) {
                view.cancel();
            }
            mViewList.clear();
        }
        mContext = null;
        mContainer = null;
        mPathMeasures = null;
    }
}
