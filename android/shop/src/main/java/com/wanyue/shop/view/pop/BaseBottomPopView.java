package com.wanyue.shop.view.pop;

import android.content.Context;
import androidx.annotation.NonNull;
import com.lxj.xpopup.core.BottomPopupView;
public abstract class BaseBottomPopView extends BottomPopupView  {
    public BaseBottomPopView(@NonNull Context context) {
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


    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
