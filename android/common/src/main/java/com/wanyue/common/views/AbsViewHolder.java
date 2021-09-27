package com.wanyue.common.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.wanyue.common.activity.AbsActivity;
import com.wanyue.common.interfaces.LifeCycleListener;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.L;

/**
 * Created by  on 2018/9/22.
 */

public abstract class AbsViewHolder implements LifeCycleListener {

    private String mTag;
    protected Context mContext;
    protected ViewGroup mParentView;
    protected View mContentView;

    public AbsViewHolder(Context context, ViewGroup parentView) {
        mTag = getClass().getSimpleName();
        mContext = context;
        mParentView = parentView;
        mContentView = LayoutInflater.from(context).inflate(getLayoutId(), mParentView, false);
        init();
    }

    public AbsViewHolder(Context context, ViewGroup parentView, Object... args) {
        mTag = getClass().getSimpleName();
        processArguments(args);
        mContext = context;
        mParentView = parentView;
        mContentView = LayoutInflater.from(context).inflate(getLayoutId(), mParentView, false);
        init();
    }

    protected void processArguments(Object... args) {

    }

    protected abstract int getLayoutId();

    public abstract void init();

    protected <T extends View> T findViewById(int res) {
        return mContentView.findViewById(res);
    }

    public View getContentView() {
        return mContentView;
    }

    protected boolean canClick() {
        return ClickUtil.canClick();
    }

    public void addToParent() {
        if (mParentView != null && mContentView != null) {
            mParentView.addView(mContentView);
        }
    }

    public void removeFromParent() {
        ViewParent parent = mContentView.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(mContentView);
        }
    }

    /**
     * 订阅Activity的生命周期
     */

    public void subscribeActivityLifeCycle() {
        if(mContext instanceof AbsActivity){
            ((AbsActivity)mContext).addLifeCycleListener(this);
        }
    }

    /**
     * 取消订阅Activity的生命周期
     */
    public void unSubscribeActivityLifeCycle() {
        if(mContext instanceof AbsActivity){
            ((AbsActivity)mContext).removeLifeCycleListener(this);
        }
    }


    public  <T extends Activity> void startActivity(Class<T> cs,int...flags) {
        if(mContext!=null&&cs!=null){
            Intent intent=new Intent(mContext,cs);
            if (flags!=null){
                for(int flag:flags){
                    intent.addFlags(flag);
                }
            }
            mContext.startActivity(intent);
        }
    }


    public void finishAcitivty(){
        if(mContext !=null&&mContext instanceof Activity){
            ((Activity) mContext).finish();
        }
    }

    @Override
    public void releaseActivty() {

    }

    /**
     * 释放资源
     */
    public void release() {
        L.e(mTag, "release-------->");
    }

    @Override
    public void onCreate() {
        L.e(mTag, "lifeCycle-----onCreate----->");
    }

    @Override
    public void onStart() {
        L.e(mTag, "lifeCycle-----onStart----->");
    }

    @Override
    public void onReStart() {
        L.e(mTag, "lifeCycle-----onReStart----->");
    }

    @Override
    public void onResume() {
        L.e(mTag, "lifeCycle-----onResume----->");
    }

    @Override
    public void onPause() {
        L.e(mTag, "lifeCycle-----onPause----->");
    }

    @Override
    public void onStop() {
        L.e(mTag, "lifeCycle-----onStop----->");
    }

    @Override
    public void onDestroy() {
        L.e(mTag, "lifeCycle-----onDestroy----->");
    }

}
