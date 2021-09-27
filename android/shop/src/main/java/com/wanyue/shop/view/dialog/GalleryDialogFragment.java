package com.wanyue.shop.view.dialog;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.dialog.AbsDialogFragment;
import com.wanyue.common.proxy.BaseProxyMannger;
import com.wanyue.common.proxy.function.GalleryViewProxy;
import com.wanyue.shop.R;
import java.util.List;

public class GalleryDialogFragment extends AbsDialogFragment {
    private FrameLayout mContainer;
    private BaseProxyMannger mBaseProxyMannger;
    private List<String> mGalleryData;
    private int mPostion;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_gallery;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.materialDialog;
    }

    @Override
    protected boolean canCancel() {
        return false;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView=window.getDecorView();
            if (window != null && decorView != null) {
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                  | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        window.setWindowAnimations(R.style.dialog_animation);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width =  WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
    }
    @Override
    public void init() {
        super.init();
        mContainer =  findViewById(R.id.container);
        int statusHeight= CommonAppConfig.statuBarHeight();
        mContainer.setPadding(mContainer.getPaddingLeft(),statusHeight,mContainer.getPaddingRight(),mContainer.getPaddingBottom());
        if(mBaseProxyMannger!=null&&mGalleryData!=null){
          GalleryViewProxy galleryViewProxy=new GalleryViewProxy();
          galleryViewProxy.putArgs(Constants.DATA,mGalleryData);
          galleryViewProxy.putArgs(Constants.POSITION,mPostion);
          galleryViewProxy.setAddPosition(0);
         mBaseProxyMannger.addViewProxy(mContainer,galleryViewProxy,galleryViewProxy.getDefaultTag());
        }
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setGalleryViewProxy(List<String>galleryData, int position,BaseProxyMannger baseProxyMannger) {
        mGalleryData=galleryData;
        mPostion=position;
        mBaseProxyMannger=baseProxyMannger;
    }
}
