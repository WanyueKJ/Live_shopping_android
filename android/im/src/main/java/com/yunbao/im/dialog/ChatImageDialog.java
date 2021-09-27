package com.yunbao.im.dialog;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.yunbao.im.R;
import com.yunbao.im.adapter.ChatImagePreviewAdapter;
import com.yunbao.im.bean.ImChatImageBean;
import com.yunbao.im.bean.ImMessageBean;
import com.yunbao.common.utils.ScreenDimenUtil;

import java.io.File;
import java.util.List;

/**
 * Created by  on 2018/11/28.
 */

public class ChatImageDialog extends PopupWindow {

    private Context mContext;
    private View mParent;
    private View mBg;
    private RecyclerView mRecyclerView;
    private ImageView mCover;
    private float mScale;
    private int mScreenWidth;
    private int mScreenHeight;
    private ValueAnimator mAnimator;
    private int mStartX;
    private int mStartY;
    private int mDistanceX;
    private int mDistanceY;
    private List<ImMessageBean> mList;
    private int mPosition;
    private ActionListener mActionListener;

    public ChatImageDialog(Context context, View parent) {
        mContext = context;
        mParent = parent;
        ScreenDimenUtil util = ScreenDimenUtil.getInstance();
        mScreenWidth = util.getScreenWdith();
        mScreenHeight = util.getScreenHeight();
        setContentView(initView());
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new ColorDrawable());
        setOutsideTouchable(true);
        setClippingEnabled(false);
        setFocusable(true);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if (mAnimator != null) {
                    mAnimator.cancel();
                }
                if (mActionListener != null) {
                    mActionListener.onImageDialogDismiss();
                }
                mActionListener = null;
                mRecyclerView = null;
            }
        });
    }

    private View initView() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_chat_image, null);
        mBg = v.findViewById(R.id.bg);
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mCover = (ImageView) v.findViewById(R.id.cover);
        mAnimator = ValueAnimator.ofFloat(0, 1);
        mAnimator.setDuration(300);
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v = (float) animation.getAnimatedValue();
                mCover.setTranslationX(mStartX + mDistanceX * v);
                mCover.setTranslationY(mStartY + mDistanceY * v);
                mCover.setScaleX(1 + (mScale - 1) * v);
                mCover.setScaleY(1 + (mScale - 1) * v);
                mBg.setAlpha(v);
            }
        });
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mRecyclerView != null && mList != null && mList.size() > 0) {
                    ChatImagePreviewAdapter adapter = new ChatImagePreviewAdapter(mContext, mList);
                    adapter.setActionListener(new ChatImagePreviewAdapter.ActionListener() {
                        @Override
                        public void onImageClick() {
                            dismiss();
                        }
                    });
                    mRecyclerView.setAdapter(adapter);
                    if (mPosition >= 0 && mPosition < mList.size()) {
                        mRecyclerView.scrollToPosition(mPosition);
                    }
                    if (mCover != null) {
                        mCover.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mCover != null) {
                                    mCover.setVisibility(View.INVISIBLE);
                                }
                            }
                        }, 300);
                    }
                }
            }
        });
        return v;
    }

    public void show(ImChatImageBean bean, File imageFile, int x, int y, int imageWidth, int imageHeight, Drawable drawable) {
        if (mCover == null || bean == null || imageFile == null || imageWidth <= 0 || imageHeight <= 0 || drawable == null) {
            return;
        }
        showAtLocation(mParent, Gravity.BOTTOM, 0, 0);
        mList = bean.getList();
        mPosition = bean.getPosition();
        ViewGroup.LayoutParams params = mCover.getLayoutParams();
        params.width = imageWidth;
        params.height = imageHeight;
        mCover.requestLayout();
        mCover.setTranslationX(x);
        mCover.setTranslationY(y);
        mCover.setImageDrawable(drawable);
        mScale = mScreenWidth / ((float) imageWidth);
        mStartX = x;
        mStartY = y;
        int targetX = mScreenWidth / 2 - imageWidth / 2;
        int targetY = mScreenHeight / 2 - imageHeight / 2;
        mDistanceX = targetX - mStartX;
        mDistanceY = targetY - mStartY;
        mAnimator.start();
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener {
        void onImageDialogDismiss();
    }
}
