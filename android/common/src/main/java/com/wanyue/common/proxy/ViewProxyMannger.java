package com.wanyue.common.proxy;

import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;

public class ViewProxyMannger extends BaseProxyMannger{
    protected BaseViewProxy mRequsetResultViewProxy;
    private boolean mShouldOnStartJudgeVisibiy;

    public ViewProxyMannger(FragmentActivity activity) {
        super(activity);
    }

    /*是否需要在onstart方法去判断是否调用 ViewProxy setUserVisibleHint方法*/
    public void setShouldOnStartJudgeVisibiy(boolean shouldOnStartJudgeVisibiy) {
        mShouldOnStartJudgeVisibiy = shouldOnStartJudgeVisibiy;
    }

    /*获取初始化inflater*/
    @Override
    public LayoutInflater getLayoutInflater() {
        if(mLayoutInflater==null){
           mLayoutInflater=LayoutInflater.from(mActivity);
        }
        return mLayoutInflater;
    }


   /* *//*观察activity生命周期*//*
    protected void watchActivityLife(int state) {
        super.watchActivityLife(state);

       switch (mCurrentActivityState){
           case LifeObserver.CREATE:
               onCreate();
               break;
           case LifeObserver.START:
              onStart();
               break;
           case LifeObserver.RESUME:
               onResume();
               break;
           case LifeObserver.PAUSE:
               onPause();
               break;
           case LifeObserver.STOP:
               onStop();
               break;
           case LifeObserver.DESTROY:
               onDestroy();
               break;
       }
    }
*/

    @Override
    public void onStart() {
        super.onStart();
        if(mShouldOnStartJudgeVisibiy&&mUserVisibleViewProxy!=null){
           mUserVisibleViewProxy.setUserVisibleHint(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(mRequsetResultViewProxy!=null){
           mRequsetResultViewProxy.onActivityResult(requestCode,resultCode,data);
        }
           mRequsetResultViewProxy=null;
    }



    /*请求acitivy，并携带参数*/
     @Override
     protected  void startActivityForResult(@RequiresPermission Intent intent, int requestCode, BaseViewProxy baseViewProxy){
        mRequsetResultViewProxy=baseViewProxy;
        if(mActivity!=null){
          mActivity.startActivityForResult(intent,requestCode);
        }
    }

}
