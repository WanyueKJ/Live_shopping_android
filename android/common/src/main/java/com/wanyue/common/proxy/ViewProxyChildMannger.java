package com.wanyue.common.proxy;

import android.content.Intent;
import android.view.LayoutInflater;

import androidx.fragment.app.FragmentActivity;

import io.reactivex.annotations.Nullable;

public class ViewProxyChildMannger extends BaseProxyMannger {
    private BaseProxyMannger mParentMannger;
    private BaseViewProxy mParentViewProxy;
    public ViewProxyChildMannger(FragmentActivity activity, BaseViewProxy baseViewProxy, @Nullable BaseProxyMannger proxyMannger) {
        super(activity);
        mParentMannger=proxyMannger;
        mParentViewProxy=baseViewProxy;
    }

    @Override
    protected void startActivityForResult(Intent intent, int requestCode, BaseViewProxy baseViewProxy) {
        if(mParentMannger!=null){
           mParentMannger.startActivityForResult(intent,requestCode,baseViewProxy);
        }
    }

    @Override
    public LayoutInflater getLayoutInflater() {
        if(mParentMannger!=null){
            return mParentMannger.getLayoutInflater();
        }
        return null;
    }


    public BaseViewProxy getParentViewProxy() {
        return mParentViewProxy;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mParentMannger=null;
        mParentViewProxy=null;
    }
}
