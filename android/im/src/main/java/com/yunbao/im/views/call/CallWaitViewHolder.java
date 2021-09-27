package com.yunbao.im.views.call;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.yunbao.common.Constants;
import com.yunbao.common.bean.UserBean;
import com.yunbao.common.glide.ImgLoader;
import com.yunbao.common.utils.ToastUtil;
import com.yunbao.common.utils.WordUtil;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.im.R;
import com.yunbao.im.business.ICallPresnter;
import com.yunbao.im.config.CallConfig;
import com.yunbao.im.custom.CallButtonLayout;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/*弹出的等待窗口*/
public class CallWaitViewHolder extends AbsViewHolder {

    private RoundedImageView imgAvator;
    private TextView tvName;
    private TextView tvState;
    private CallButtonLayout buttonLayout1;
    private CallButtonLayout buttonLayout2;
    private CallButtonLayout buttonLayout3;

    private AbsCallViewHolder absCallViewHolder;

    private UserBean userBean;
    //通话类型 语音and视频
    private int callState;

    //应答类型 拨打或者接听
    private int replyState;

    private LinearLayout llTools;

    public CallWaitViewHolder(Context context, ViewGroup parentView,int callState,UserBean userBean) {
        super(context, parentView,callState,userBean);
    }

    @Override
    protected void processArguments(Object... args) {
        super.processArguments(args);
        callState= (int) args[0];
        userBean= (UserBean) args[1];

    }


    @Override
    protected int getLayoutId() {
        return R.layout.view_media_wait;
    }

    @Override
    public void init() {
        imgAvator = (RoundedImageView) findViewById(R.id.img_avator);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvState = (TextView) findViewById(R.id.tv_state);
        buttonLayout1 = (CallButtonLayout) findViewById(R.id.button_layout_1);
        buttonLayout2 = (CallButtonLayout) findViewById(R.id.button_layout_2);
        buttonLayout3 = (CallButtonLayout) findViewById(R.id.button_layout_3);
        llTools = (LinearLayout) findViewById(R.id.ll_tools);

        setUserBean(userBean);
    }

    public void setUserBean(UserBean userBean){
        this.userBean=userBean;
        ImgLoader.display(mContext,userBean.getAvatar(),imgAvator);
        tvName.setText(userBean.getUserNiceName());
    }

    public void changeState(int state){
        boolean isVideo=callState== Constants.CHAT_TYPE_VIDEO;
        this.replyState=state;
        if(state== CallConfig.CALL){
            tvState.setText(mContext.getString(R.string.wait_onther_accept));
            buttonLayout1.setButtonEntity(null);
            buttonLayout2.setButtonEntity(new CallButtonLayout.CallButtonEntity(
                    WordUtil.getString(R.string.cancel),
                    R.mipmap.icon_call_refuse,cancleClickLisnter
            ));
            buttonLayout3.setButtonEntity(null);
        } else if(state== CallConfig.WAIT){
            //第一个按钮的title根据是否是视频通话判断
           String stateTitle=isVideo?mContext.getString(R.string.invite_your_video_call_tip):mContext.getString(R.string.invite_you_call_audio_tip);
            tvState.setText(stateTitle);
            //第三个按钮的icon根据是否是视频通话判断
           int btn3Icon=isVideo?R.mipmap.icon_video_call:R.mipmap.icon_audio_call;
           buttonLayout1.setButtonEntity(
                    new CallButtonLayout.CallButtonEntity(mContext.getString(R.string.refuse)
                    , R.mipmap.icon_call_refuse,
                       refuseClickLisnter
                    ));
            buttonLayout2.setButtonEntity(null);
            buttonLayout3.setButtonEntity( new CallButtonLayout.CallButtonEntity(mContext.getString(R.string.answer)
            ,btn3Icon,acceptClickLisnter
            ));
        }
    }

    public void setState(String contentState,boolean hideTools){
        if(llTools.getVisibility()==View.VISIBLE&&hideTools){
           llTools.setVisibility(View.GONE);
        }
        tvState.setText(contentState);
    }

    public View.OnClickListener cancleClickLisnter= new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(absCallViewHolder!=null){
               absCallViewHolder.cancleAndBrocast();
            }
        }
    };

    public View.OnClickListener acceptClickLisnter= new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(absCallViewHolder!=null){
               absCallViewHolder.acceptAndBrocast();
            }
        }
    };

    public View.OnClickListener refuseClickLisnter= new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            if(absCallViewHolder!=null){
                absCallViewHolder.refuseAndBrocast();
            }
        }
    };

    private Disposable disposable;
    public void startTimeOutWatch(){
        disposable= Observable.timer(10, TimeUnit.SECONDS).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                if(replyState==CallConfig.CALL){
                    ToastUtil.show(R.string.no_response);
                }else{
                    ToastUtil.show(R.string.overtime);
                    ICallPresnter callPresnter=absCallViewHolder.getPresnter();
                    if(callPresnter!=null){
                        callPresnter.exitRoom();
                    }
                }
            }
        });
    }


    public void setAbsCallViewHolder(AbsCallViewHolder absCallViewHolder) {
        this.absCallViewHolder = absCallViewHolder;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        absCallViewHolder=null;
        if(disposable!=null&&!disposable.isDisposed()){
            disposable.dispose();
            disposable=null;
        }
    }
}
