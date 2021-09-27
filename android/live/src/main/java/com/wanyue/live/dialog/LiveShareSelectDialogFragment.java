package com.wanyue.live.dialog;

import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.HtmlConfig;
import com.wanyue.common.business.HtmlHelper;
import com.wanyue.common.custom.DrawableTextView;
import com.wanyue.common.dialog.AbsDialogFragment;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.mob.MobCallback;
import com.wanyue.common.mob.MobShareUtil;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.L;
import com.wanyue.live.R;
import com.wanyue.live.bean.LiveBean;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.live.model.LiveModel;

public class LiveShareSelectDialogFragment extends AbsDialogFragment implements View.OnClickListener {

    private TextView mBtnShareWechat;
    private TextView mBtnPill;
    private String mQrCode;

    private MobShareUtil mMobShareUtil;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_share_select;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = DpUtil.dp2px(200);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void init() {
        super.init();
        mBtnShareWechat = (DrawableTextView) findViewById(R.id.btn_share_wechat);
        mBtnPill = (DrawableTextView) findViewById(R.id.btn_pill);
        mBtnShareWechat.setOnClickListener(this);
        mBtnPill.setOnClickListener(this);
        getLiveQrCode();
    }

    private void getLiveQrCode() {
       String mLiveUid= LiveModel.getContextLiveUid(getActivity());
        LiveHttpUtil.getLiveQrCode(mLiveUid, new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
                if(ParseHttpCallback.isSuccess(code)){
                   mQrCode=info.getString("code");
                    mBtnShareWechat.setEnabled(true);
                    mBtnPill.setEnabled(true);
                    mBtnShareWechat.setAlpha(1F);
                    mBtnPill.setAlpha(1F);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LiveHttpUtil.cancel(LiveHttpConsts.LIVE_QR_CODE);
    }

    @Override
    public void onClick(View v) {
        if(!ClickUtil.canClick()){
            return;
        }
        int id=v.getId();
        if(id==R.id.btn_share_wechat){
            clickShareSmallProgram();
        }else if(id==R.id.btn_pill){
            clickPill();
        }
    }

    private void clickShareSmallProgram() {
        LiveBean liveBean=LiveModel.getContextLiveBean(getActivity());
        if(liveBean==null){
            return;
        }
        if(mMobShareUtil==null){
           mMobShareUtil=new MobShareUtil();
        }
      String path=  HtmlHelper.newHtmlHelper().appendUrl("pages/newpages/live/index?").
                 appendParm("l",liveBean.getUid(),true)
                .appendParm("pid", CommonAppConfig.getUserBean().getId(),false).create();
        L.e("path=="+path);
        mMobShareUtil.shareSmallProgram(liveBean.getTitle(), liveBean.getThumb(), path,new MobCallback() {
            @Override
            public void onSuccess(Object data) {
                dismiss();
            }
            @Override
            public void onError() {

            }
            @Override
            public void onCancel() {

            }
            @Override
            public void onFinish() {

            }
        });
    }

    private void clickPill() {
        LiveShareSmallProgramDialogFragment liveShareSmallProgramDialogFragment=new LiveShareSmallProgramDialogFragment();
        liveShareSmallProgramDialogFragment.setQrCode(mQrCode);
        liveShareSmallProgramDialogFragment.show(getActivity().getSupportFragmentManager());
        dismiss();
    }
}
