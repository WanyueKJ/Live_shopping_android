package com.wanyue.common.dialog;

import android.app.Dialog;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.fragment.app.FragmentManager;
import com.wanyue.common.Constants;
import com.wanyue.common.R;
import com.wanyue.common.proxy.BaseProxyMannger;
import com.wanyue.common.proxy.BaseViewProxy;

public abstract class AbsContainerDialogFragment extends AbsDialogFragment {
    private ViewGroup mContainer;
    private BaseViewProxy mBaseViewProxy;
    private BaseProxyMannger mBaseProxyMannger;
    private boolean isTransparent;

    @Override
    protected int getLayoutId() {
        return R.layout.simple_frame_layout;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }

    @Override
    public void init() {
        super.init();
        mContainer = (ViewGroup) findViewById(R.id.container);
        Dialog dialog=getDialog();
        if(mBaseViewProxy==null){
            return;
        }
        mBaseViewProxy.getArgMap().put(Constants.KEY_DIALOG,dialog);
        if(isTransparent){
           mContainer.setBackground(null);
        }
        if(mBaseViewProxy!=null&&mBaseProxyMannger!=null){
           mBaseProxyMannger.addViewProxy(mContainer,mBaseViewProxy,mBaseViewProxy.getDefaultTag());
        }

    }
    public void setViewProxy(BaseViewProxy baseViewProxy, BaseProxyMannger baseProxyMannger) {
        mBaseViewProxy = baseViewProxy;
        mBaseProxyMannger=baseProxyMannger;
    }
    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    public void setWindowAttributes(Window window) {
       window.setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        WindowManager.LayoutParams params = window.getAttributes();
        window.setAttributes(params);
        window.getDecorView().setPadding(0, 0, 0, 0); //消除边距
        setWindowManagerParams(window,params);

        /*隐藏导航栏*/
        /*window.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(uiOptions);
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);*/
    }


    public void setTransparent(boolean transparent) {
        isTransparent = transparent;
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);

    }

    public abstract void setWindowManagerParams(Window window, WindowManager.LayoutParams params) ;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBaseProxyMannger!=null&&mBaseViewProxy!=null){
           mBaseProxyMannger.removeAllBindThisView(mContainer);
        }
        mBaseProxyMannger=null;
        mBaseViewProxy=null;
    }
}
