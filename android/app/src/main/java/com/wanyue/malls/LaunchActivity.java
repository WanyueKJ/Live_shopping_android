package com.wanyue.malls;

import android.content.Intent;
import android.widget.ImageView;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.activity.AbsActivity;
import com.wanyue.common.custom.viewanimator.ViewAnimator;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.utils.RouteUtil;
import com.wanyue.main.view.activity.LoginActivity;
import com.wanyue.main.view.activity.MainActivity;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

@Route(path = RouteUtil.PATH_LAUNCHER)
public class LaunchActivity extends AbsActivity {

    private ImageView mImgCover;
    private Disposable mDisposable;
    private ViewAnimator mViewAnimator;


    @Override
    protected void main() {
        super.main();
    }

    @Override
    public void init() {
        mImgCover = findViewById(R.id.img_cover);
        Intent intent = getIntent();
        if (!isTaskRoot()
                && intent != null
                && intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                && intent.getAction() != null
                && intent.getAction().equals(Intent.ACTION_MAIN)) {
            finish();
            return;
        }
        ImgLoader.display(this,R.mipmap.screen,mImgCover);
        startLauncherAnim();
        mDisposable=Observable.just(1L).delay(1, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                startLauncher();
            }
        })  ;

    }


    private void startLauncherAnim() {
        mViewAnimator=ViewAnimator.animate(mImgCover).alpha(0,1).duration(500).start();
    }

    /*开始启动方法*/
    private void startLauncher() {
        /*倒计时开始*/
        if(CommonAppConfig.isLogin()){
            startActivity(MainActivity.class);
        }else{
            startActivity(LoginActivity.class);
        }
        finish();
    }

    @Override
    protected void releaseActivty() {
        super.releaseActivty();
        if(mDisposable!=null&&!mDisposable.isDisposed()){
           mDisposable.dispose();
        }
        if(mViewAnimator!=null){
            mViewAnimator.cancel();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_launch;
    }
}
