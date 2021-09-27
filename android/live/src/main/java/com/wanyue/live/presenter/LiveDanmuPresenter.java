package com.wanyue.live.presenter;

import android.content.Context;
import android.view.ViewGroup;

import com.wanyue.live.bean.LiveDanMuBean;
import com.wanyue.live.views.DanmuViewHolder;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by  on 2018/10/12.
 * 弹幕
 */

public class LiveDanmuPresenter implements DanmuViewHolder.ActionListener {

    private Context mContext;
    private ViewGroup mDanmuContainer;
    private boolean[] mLines;//弹幕的轨道
    private List<DanmuViewHolder> mList;
    private ConcurrentLinkedQueue<LiveDanMuBean> mQueue;

    public LiveDanmuPresenter(Context context, ViewGroup danmuContainer) {
        mContext = context;
        mDanmuContainer = danmuContainer;
        mLines = new boolean[]{true, true, true};
        mList = new LinkedList<>();
        mQueue = new ConcurrentLinkedQueue<>();
    }

    /**
     * 显示弹幕的方法
     */
    public void showDanmu(LiveDanMuBean bean) {
        int lineNum = -1;
        for (int i = 0; i < mLines.length; i++) {
            if (mLines[i]) {
                mLines[i] = false;
                lineNum = i;
                break;
            }
        }
        if (lineNum == -1) {
            mQueue.offer(bean);
            return;
        }
        DanmuViewHolder danmuHolder = null;
        for (DanmuViewHolder holder : mList) {
            if (holder.isIdle()) {
                holder.setIdle(false);
                danmuHolder = holder;
                break;
            }
        }
        if (danmuHolder == null) {
            danmuHolder = new DanmuViewHolder(mContext, mDanmuContainer);
            danmuHolder.setActionListener(this);
            mList.add(danmuHolder);
        }
        danmuHolder.show(bean, lineNum);
    }

    /**
     * 获取下一个弹幕
     */
    private void getNextDanmu() {
        LiveDanMuBean bean = mQueue.poll();
        if (bean != null) {
            showDanmu(bean);
        }
    }

    public void reset() {
        if (mLines != null) {
            for (boolean line : mLines) {
                line = true;
            }
        }
    }

    public void release() {
        if (mList != null) {
            for (DanmuViewHolder vh : mList) {
                vh.release();
            }
            mList.clear();
        }
        if (mQueue != null) {
            mQueue.clear();
        }
    }

    @Override
    public void onCanNext(int lineNum) {
        mLines[lineNum] = true;
        getNextDanmu();
    }

    @Override
    public void onAnimEnd(DanmuViewHolder vh) {
        if (mQueue.size() == 0) {
            if (vh != null) {
                vh.release();
                if (mList != null) {
                    mList.remove(vh);
                }
            }
        }
    }

}
