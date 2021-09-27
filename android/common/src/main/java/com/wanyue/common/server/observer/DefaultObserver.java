package com.wanyue.common.server.observer;

import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ToastUtil;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/*create by chenfangwei
 * */

public   abstract class DefaultObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {
    }
    @Override
    public void onError(Throwable e) {
        if(e!=null){
            String error="网络请求错误=="+e.getMessage();
           //DebugUtil.sendException(error);
            L.e(error);
            //ToastUtil.show(error);
        }
    }

    @Override
    public void onComplete() {

    }
}
