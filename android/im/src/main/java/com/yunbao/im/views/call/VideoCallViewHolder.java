package com.yunbao.im.views.call;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.custom.CheckImageView;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.im.R;
import com.yunbao.im.business.IVideoCallView;
import com.yunbao.im.business.TimeModel;
import com.yunbao.im.custom.FloatFrameLayout;
import com.yunbao.im.custom.FlowVideoLayout;
import com.yunbao.im.event.VideoAllCloseEvent;
import com.yunbao.im.utils.Utils;
import org.greenrobot.eventbus.EventBus;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class VideoCallViewHolder extends AbsCallViewHolder implements IVideoCallView<TXCloudVideoView> {

    //代码里面设置排列,数组index 为1的永远是最上层的小窗口view
    private FlowVideoLayout[]flowVideoLayouts;
    private FlowVideoLayout  flowLayoutMain;
    private FlowVideoLayout  flowLayoutVit;
    private ArrayList<FrameLayout.LayoutParams>layoutParams;
    private CheckImageView ciMute;
    private CheckImageView ciCamera;
    private FrameLayout container;
    private TextView tvCameraToggle;
    private RoundedImageView imgAvator;
    private TextView tvUserName;
    private TextView tvCallTime;
    private FrameLayout flWindowTools;
    private View.OnClickListener toBigClickLisnter;
    private View.OnClickListener showToolsOnclick;
    private TimeModel.TimeListner timeListner;

    public VideoCallViewHolder(Context context, ViewGroup parentView, int roomId, UserBean userBean) {
        super(context, parentView, roomId, userBean);
    }
    @Override
    public int stateCall() {
        return Constants.CHAT_TYPE_VIDEO;
    }
    @Override
    protected int getLayoutId() {
        return R.layout.view_video_call;
    }

    @Override
    public void accept() {
        super.accept();

        if(presnter!=null){
            presnter.startSDKLocalPreview(true);
        }
    }

    @Override
    public void init() {
        super.init();
        flowVideoLayouts=new FlowVideoLayout[2];
        flowLayoutMain = (FlowVideoLayout) findViewById(R.id.flowLayout_main);
        flowLayoutVit = (FlowVideoLayout) findViewById(R.id.flowLayout_vit);
        container = (FrameLayout) findViewById(R.id.container);
        ciMute =  findViewById(R.id.ci_mute);
        ciCamera =  findViewById(R.id.ci_camera);
        tvCameraToggle = (TextView) findViewById(R.id.tv_camera_toggle);
        imgAvator = (RoundedImageView) findViewById(R.id.img_avator);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvCallTime = (TextView) findViewById(R.id.tv_call_time);
        flWindowTools = (FrameLayout) findViewById(R.id.fl_window_tools);
        //flWindowTools.setOnClickListener(this);
        setUserData();
        setOnClickListner(R.id.btn_mute,this);
        setOnClickListner(R.id.btn_cal_flip,this);
        setOnClickListner(R.id.btn_camera,this);

        layoutParams=Utils.initFloatParamList(mContext);
        toBigClickLisnter=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setBig(v);
            }
        };
        showToolsOnclick=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setToolWindowVisible();
            }
        };

        setBig(flowLayoutMain);
        timeListner=new TimeModel.TimeListner() {
            @Override
            public void time(String string) {
                tvCallTime.setText(string);
            }
        };
        TimeModel.getInstance().addTimeListner(timeListner);
    }

    private boolean toolsWindowIsVisible() {
       return flWindowTools.getVisibility()==View.VISIBLE;
    }


    private void setUserData() {
        if(userBean!=null){
            ImgLoader.display(mContext,userBean.getAvatar(),imgAvator);
            tvUserName.setText(userBean.getUserNiceName());
        }
    }

    /*设置变大的是哪个视频窗口,变小的可以在acitivty内自由悬浮*/
    private void setBig(View view) {
       if(view==flowLayoutMain){
           flowVideoLayouts[0]=flowLayoutMain;
           flowVideoLayouts[1]=flowLayoutVit;
       }else{
           flowVideoLayouts[1]=flowLayoutMain;
           flowVideoLayouts[0]=flowLayoutVit;
       }
           setFlowLayoutParm();
    }

    //设置activity内的悬浮参数
    private void setFlowLayoutParm() {
        int length=flowVideoLayouts.length;
        for(int i=0;i<length;i++){
            View view=flowVideoLayouts[i];
            flowVideoLayouts[i].setLayoutParams(layoutParams.get(i));
            if(i==1){
                container.bringChildToFront(view);
            }
        }

        FlowVideoLayout flowLayout=flowVideoLayouts[0];
        flowLayout.setMoveable(false);
        flowLayout.setOnClick(showToolsOnclick);

        flowLayout=flowVideoLayouts[1];
        flowLayout.setOnClick(toBigClickLisnter);
        flowLayout.setMoveable(true);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id=v.getId();
        if(id==R.id.btn_mute){
           boolean isMute= !presnter.getCallState().isMute;
           presnter.isMute(isMute);
           ciMute.setChecked(isMute);
        }else if(id==R.id.btn_cal_flip){
            boolean isFront= !presnter.getCallState().isFront; //callPresnter层存储相关变量进行toggle
            presnter.isFront(isFront);
        }else if(id==R.id.btn_camera){
            boolean isOpenCamera= !presnter.getCallState().isOpenCamera;
            presnter.openCamera(isOpenCamera);
            ciCamera.setChecked(isOpenCamera);
            if(isOpenCamera){
               tvCameraToggle.setText(R.string.close_camera);
            }else{
               tvCameraToggle.setText(R.string.open_camera);
            }
        }
    }


    private Disposable disposable;
    //计时隐藏
    private void setToolWindowVisible() {
        flWindowTools.setVisibility(View.VISIBLE);
        if(disposable!=null&&!disposable.isDisposed()){
           disposable.dispose();
        }
        disposable= Observable.timer(3, TimeUnit.SECONDS).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                flWindowTools.setVisibility(View.INVISIBLE);
            }
        });
    }

    @Override
    public TXCloudVideoView getVideoView(String id) {
        return flowLayoutVit.getVideoView();
    }
    @Override
    public TXCloudVideoView getMainVideoView() {
        return flowLayoutMain.getVideoView();
    }

    //每个接口声明都写了注释
    @Override
    public void ontherOpenVideo(boolean isOpen) {
        if(presnter!=null&&presnter.getCallState().isVideo&&!isOpen){
           allVideoClose();
        }
    }

    /*当两方视频都关闭的时候进行跳转语音通话界面，暂时还没有处理,仅提供了入口*/
    private void allVideoClose() {
        EventBus.getDefault().post(new VideoAllCloseEvent());
    }

    //为抽象类里面的方法提供需要系统级别的悬浮View
    @Override
    public View exportFlowView() {
       FlowVideoLayout flowVideoLayout= flowVideoLayouts[1];
       if(flowVideoLayout!=null){
         return flowVideoLayout.getVideoView();
       }
        return null;
    }

    @Override
    public void restoreFlowView(FloatFrameLayout floatFrameLayout) {
        View view= floatFrameLayout.getChildAt(0);
        floatFrameLayout.removeAllViews();
        FlowVideoLayout flowVideoLayout= flowVideoLayouts[1];
        if(flowVideoLayout!=null){
            flowVideoLayout.addView(view);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        TimeModel.getInstance().removeTimeListner(timeListner);
    }
}
