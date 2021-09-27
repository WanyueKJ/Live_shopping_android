package com.wanyue.live.presenter;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.business.acmannger.ActivityMannger;
import com.wanyue.common.http.BaseHttpCallBack;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.http.ParseSingleHttpCallback;
import com.wanyue.common.utils.L;
import com.wanyue.live.R;
import com.wanyue.live.activity.LiveAudienceActivity;
import com.wanyue.live.bean.LiveBean;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.MD5Util;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;

/**
 * Created by  on 2017/9/29.
 */

public class LiveRoomCheckLivePresenter {

    private Context mContext;
    private LiveBean mLiveBean;
    private ActionListener mActionListener;
    private Dialog mLoadingDialog;

    public LiveRoomCheckLivePresenter(Context context, ActionListener actionListener) {
        mContext = context;
        mActionListener = actionListener;
    }

    /**
     * 观众 观看直播
     */
    public void checkLive(LiveBean bean) {
        mLiveBean = bean;
        if(!DialogUitl.isShow(mLoadingDialog)&&mContext!=null){
           mLoadingDialog=DialogUitl.loadingDialog(mContext);
           mLoadingDialog.show();
        }
        LiveAudienceActivity activity= (LiveAudienceActivity) ActivityMannger.getInstance().getFirstClassTypeActivieActivity(LiveAudienceActivity.class);
        if(activity!=null){
           activity.onBackAndFinish();
        }
        LiveHttpUtil.checkLive(bean.getUid(), bean.getStream(), mCheckLiveCallback);
    }
    private BaseHttpCallBack mCheckLiveCallback = new ParseHttpCallback<JSONObject>() {

        @Override
        public void onError() {
            super.onError();
            DialogUitl.dismissDialog(mLoadingDialog);
        }
        @Override
        public void onSuccess(int code, String msg, JSONObject info) {
            if(!isSuccess(code)||info==null){
                DialogUitl.dismissDialog(mLoadingDialog);
               return;
            }
            int isLive=info.getIntValue("islive");
            if(isLive==1){
                forwardNormalRoom();
            }else{
                ToastUtil.show("直播已结束");
            }
        }
    };

    /**
     * 前往普通房间
     */
    private void forwardNormalRoom() {
        enterLiveRoom();
    }

    public void cancel() {
        mActionListener = null;
        LiveHttpUtil.cancel(LiveHttpConsts.CHECK_LIVE);
        LiveHttpUtil.cancel(LiveHttpConsts.ENTER_ROOM);
    }

    /**
     * 进入直播间
     */

    private void enterLiveRoom() {
        LiveHttpUtil.enterRoom(mLiveBean.getUid(), mLiveBean.getStream(), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                DialogUitl.dismissDialog(mLoadingDialog);
                if(isSuccess(code)&&info.length>0){
                    String data=info[0];
                    if (mActionListener != null) {
                        mActionListener.onLiveRoomChanged(mLiveBean,data);
                    }
                }else{
                    ToastUtil.show(msg);
                }
            }
            @Override
            public void onError() {
                super.onError();
                DialogUitl.dismissDialog(mLoadingDialog);
            }

        });
    }

    public interface ActionListener {
        void onLiveRoomChanged(LiveBean liveBean,String data);
    }
}
