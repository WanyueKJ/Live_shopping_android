package com.wanyue.common.business;

import android.app.Activity;
import android.content.Context;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.business.acmannger.ActivityMannger;
import com.wanyue.common.http.BaseHttpCallBack;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.RouteUtil;
import io.reactivex.Observable;
public class JumpInterceptor {
  public static Observable<Boolean> shouldInterceptor(Context context) {
      if (!CommonAppConfig.isLogin()){
           RouteUtil.forwardLogin();
           return Observable.just(false);
      }
      return null;
  }

  public static void shouldInterceptor(int code){
      if(!canClick()){
          return;
      }
      if(code==BaseHttpCallBack.NO_LOGIN||code==BaseHttpCallBack.EXPIRE_LOGIN||code==BaseHttpCallBack.LOGIN_ERROR){
          interceptorAndFinshActivity();
      }
  }

    private static long sLastClickTime;
    public static boolean canClick() {
        long curTime = System.currentTimeMillis();
        if (curTime - sLastClickTime < 500) {
            return false;
        }
        sLastClickTime = curTime;
        return true;
    }

    public static void interceptorAndFinshActivity(){
      Activity activity= ActivityMannger.getInstance().getMainStackTopActivity();
      boolean isBaseActivity=ActivityMannger.getInstance().isBaseActivity(activity);
      if(activity!=null&&!isBaseActivity&&!activity.isFinishing()){
         activity.finish();
      }
       RouteUtil.forwardLogin();
    }


}
