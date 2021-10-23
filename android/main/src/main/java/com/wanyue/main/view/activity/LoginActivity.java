package com.wanyue.main.view.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.HtmlConfig;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.activity.WebViewActivity;
import com.wanyue.common.bean.ConfigBean;
import com.wanyue.common.bean.DataListner;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.business.TimeModel;
import com.wanyue.common.business.acmannger.ActivityMannger;
import com.wanyue.common.http.BaseHttpCallBack;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.mob.LoginData;
import com.wanyue.common.mob.MobBean;
import com.wanyue.common.mob.MobCallback;
import com.wanyue.common.mob.MobLoginUtil;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.RouteUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.ValidatePhoneUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.activity.LiveAnchorActivity;
import com.wanyue.main.R;
import com.wanyue.main.R2;
import com.wanyue.main.adapter.LoginTypeAdapter;
import com.wanyue.main.api.MainAPI;
import com.wanyue.main.bean.LoginCommitBean;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

@Route(path = RouteUtil.PATH_LOGIN)
public class LoginActivity extends BaseActivity implements TimeModel.TimeListner, OnItemClickListener<MobBean> {
    //登录
    @BindView(R2.id.tv_phone)
    EditText mTvPhone;
    @BindView(R2.id.tv_code)
    EditText mTvCode;
    @BindView(R2.id.btn_get_code)
    TextView mBtnGetCode;
    @BindView(R2.id.btn_login)
    Button mBtnLogin;
    @BindView(R2.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R2.id.btn_tip)
    TextView mBtnTip;
    @BindView(R2.id.tip_group)
    LinearLayout mTipGroup;
    @BindView(R2.id.img_launcher)
    ImageView mImgLauncher;

    private TimeModel mTimeModel;
    private LoginCommitBean mLoginCommitBean;
    private MobLoginUtil mLoginUtil;

    //登录改动

    @Override
    public void init() {
        mBtnTip.setText(getString(R.string.login_tip_2));
        initCommitData();
        initThirdData();
        mImgLauncher.setImageResource(CommonAppConfig.getAppIconRes());
    }

 //初始化
    private void initCommitData() {
        mLoginCommitBean=new LoginCommitBean();
        mLoginCommitBean.setDataListner(new DataListner() {
            @Override
            public void compelete(boolean isCompelete) {
                mBtnLogin.setEnabled(isCompelete);
            }
        });
    }

    /*初始化第三方登录的数据*/
    private void initThirdData() {
        List<MobBean> list = MobBean.getLoginTypeList(Constants.MOB_WX);
        if (ListUtil.haveData(list)) {
            mRecyclerView = findViewById(R.id.recyclerView);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
            LoginTypeAdapter adapter = new LoginTypeAdapter(mContext, list);
            adapter.setOnItemClickListener(this);
            mRecyclerView.setAdapter(adapter);
            mLoginUtil = new MobLoginUtil();
        }
    }

    /*监听手机号输入框信息*/
    @OnTextChanged(value = R2.id.tv_phone, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchPhoneTextChange(CharSequence sequence, int start, int before, int count){
        String phoneString=sequence.toString();
        mLoginCommitBean.setPhoneString(phoneString);
        mBtnGetCode.setEnabled(ValidatePhoneUtil.validateMobileNumber(phoneString));
    }

    /*监听验证码号输入框信息*/
    @OnTextChanged(value = R2.id.tv_code, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchCodeTextChange(CharSequence sequence, int start, int before, int count){
        String codeString=sequence.toString();
        mLoginCommitBean.setCheckString(codeString);
    }
    /*获取验证码*/
    @OnClick(R2.id.btn_get_code)
    public void getCode() {
       final String phoneNum=mTvPhone.getText().toString();
        if(!ValidatePhoneUtil.validateMobileNumber(phoneNum)){
            mTvPhone.setError(WordUtil.getString(R.string.login_phone_error));
            mTvPhone.requestFocus();
            return;
        }
        MainAPI.getVerifyKey(new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
                if(info!=null){
                 String infoString= info.getString("key");
                 getVerifyCode(phoneNum,infoString);
                }else{
                    ToastUtil.show(msg);
                }
            }
        });
    }

    private void getVerifyCode(String phoneNum,String infoString) {
        MainAPI.getVerifyCode(phoneNum, "login", infoString, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                ToastUtil.show(msg);
                if(isSuccess(code)){
                    getLoginCodeSucc();
                }
            }
        });
    }

    private void getLoginCodeSucc() {
        initTimeModel();
        mTimeModel.start();
        mBtnGetCode.setEnabled(false);
    }

    @OnClick(R2.id.btn_register)
    public void toRegister(){
        startActivityForResult(RegisterActivity.class,RegisterActivity.REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RegisterActivity.REGISTER&&resultCode==RESULT_OK){
            String phone=data.getStringExtra(Constants.DATA);
            mTvPhone.setText(phone);
        }
    }

    @OnClick(R2.id.btn_login)
    public void login() {
        if(mLoginCommitBean==null){
            return;
        }
        String phoneString=mLoginCommitBean.getPhoneString();
        String codeString=mLoginCommitBean.getCheckString();

        MainAPI.loginByCode(phoneString,codeString, mParseHttpCallback);
    }

    private ParseHttpCallback mParseHttpCallback= new ParseHttpCallback<JSONObject>() {
        @Override
        public void onSuccess(int code, String msg, JSONObject info) {
            if(isSuccess(code)){
                ToastUtil.show(com.wanyue.common.R.string.login_auth_success);
                loginingSucc(info);
            }else{
                ToastUtil.show(msg);
            }
        }
    };

    private void loginingSucc(JSONObject jsonObject) {
        String token = jsonObject.getString("token");
        CommonAppConfig.setLoginInfo( "0",token, true);
        askUserInfo();
    }

    private void askUserInfo() {
        MainAPI.getBaseInfo(new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if(BaseHttpCallBack.isSuccess(code)&&info.length>0){
                    String json=info[0];
                    UserBean userBean=JSON.parseObject(json,UserBean.class);
                    CommonAppConfig.setUserBean(userBean,json);
                    Activity activity= ActivityMannger.getInstance().getBaseActivity();
                    if(activity==null){
                       startActivity(MainActivity.class);
                    }
                    finish();
                }
            }
        });
    }

    private void initTimeModel() {
        if(mTimeModel==null){
            mTimeModel=new TimeModel()
                    .setTotalUseTime(60)
                    .setState(TimeModel.COUNT_DOWN)
                    .setAfterString("s后重新获取");
            mTimeModel.addTimeListner(this);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mTimeModel!=null){
           mTimeModel.clear();
        }
    }


    private long mLastClickBackTime;
    @Override
    public void onBackPressed() {
        long curTime = System.currentTimeMillis();
        if (curTime - mLastClickBackTime > 2000) {
            mLastClickBackTime = curTime;
            ToastUtil.show(R.string.main_click_next_exit);
            return;
        }
        ActivityMannger.getInstance().clearAllActivity();
    }

    @Override
    protected boolean shouldBindButterKinfe() {
        return true;
    }

    @Override
    public void time(String string) {
        mBtnGetCode.setText(string);
    }

    @Override
    public void compelete() {
        mBtnGetCode.setEnabled(true);
        mBtnGetCode.setText(R.string.login_get_code);
    }

    /*第三方登陆*/
    @Override
    public void onItemClick(final MobBean bean, int position) {
        if (mLoginUtil == null) {
            return;
        }
        final Dialog dialog = DialogUitl.loginAuthDialog(mContext);
        dialog.show();
        mLoginUtil.execute(bean.getType(), new MobCallback() {
            @Override
            public void onSuccess(Object data) {
                if (data != null) {
                    loginByThird(bean.getType(),(LoginData) data);
                }
            }
            @Override
            public void onError() {
                disMissLoadingDialog(dialog);

            }
            @Override
            public void onCancel() {
                disMissLoadingDialog(dialog);
            }
            @Override
            public void onFinish() {
                disMissLoadingDialog(dialog);
            }
        });
    }



    private void disMissLoadingDialog(Dialog dialog) {
        if (dialog != null&&dialog.isShowing()) {
            dialog.dismiss();
            dialog=null;
        }
    }
    /*点击用户协议*/
    @OnClick(R2.id.btn_tip)
    public void clickTip() {
        WebViewActivity.forward(this, HtmlConfig.LOGIN_PRIVCAY,false);
    }

    private void loginByThird(String loginType,LoginData data) {
        MainAPI.loginByThird(data,mParseHttpCallback);
    }
}
