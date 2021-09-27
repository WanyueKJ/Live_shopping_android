package com.wanyue.shop.view.pop;

import android.content.Context;

import androidx.annotation.NonNull;
import com.lxj.xpopup.core.CenterPopupView;

public class BaseCenterPopView extends CenterPopupView {

    public BaseCenterPopView(@NonNull Context context) {
        super(context);
    }

    @Override
    public void destroy(){
        if(dialog!=null) {
            dialog.dismiss();
        }
        onDetachedFromWindow();
        if(popupInfo!=null){
            popupInfo.atView = null;
            popupInfo.watchView = null;
            popupInfo.xPopupCallback = null;
            popupInfo = null;
        }
    }
}
