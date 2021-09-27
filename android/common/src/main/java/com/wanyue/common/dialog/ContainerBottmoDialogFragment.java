package com.wanyue.common.dialog;

import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.R;
import com.wanyue.common.utils.L;

public class ContainerBottmoDialogFragment extends AbsContainerDialogFragment {
    @Override
    public void setWindowManagerParams(Window window, WindowManager.LayoutParams params) {
        window.setType(WindowManager.LayoutParams.TYPE_APPLICATION_MEDIA);
        int screenHeight= CommonAppConfig.getWindowHeight();
        int statusSize= CommonAppConfig.statuBarHeight();
        int navionSize= CommonAppConfig.navigationBarHeight();
        L.e("screenHeight=="+screenHeight+"statusSize="+statusSize+"navionSize=="+navionSize);
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM|Gravity.LEFT;
        window.setWindowAnimations(com.wanyue.common.R.style.bottomToTopAnim);
    }

    @Override
    protected int getDialogStyle() {
        return R.style.inputDialog;
    }
}
