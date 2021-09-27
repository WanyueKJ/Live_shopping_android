package com.wanyue.main.view.activity;


import android.app.Dialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.BaseHttpCallBack;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.interfaces.ImageResultCallback;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.ProcessImageUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.main.R;
import com.wanyue.main.api.MainAPI;
import java.io.File;


public class EditProfileActivity extends BaseActivity implements View.OnClickListener {
    public static final int EDIT=1;

    private ImageView mAvatar;
    private EditText mName;
    private ProcessImageUtil mImageUtil;
    private File mAvatarFile;
    private String mAvatarRemoteFileName;
    private String mNameVal;
    private Dialog mLoading;
    private UserBean mUserBean;

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_profile;
    }

    @Override
    public void init() {
        setTitle(WordUtil.getString(R.string.edit_profile));
        mAvatar = findViewById(R.id.avatar);
        mName = findViewById(R.id.name);
        findViewById(R.id.btn_save).setOnClickListener(this);
        findViewById(R.id.btn_avatar).setOnClickListener(this);
        mImageUtil = new ProcessImageUtil(this);
        mImageUtil.setImageResultCallback(new ImageResultCallback() {
            @Override
            public void beforeCamera() {

            }
            @Override
            public void onSuccess(File file) {
                if (file != null) {
                    ImgLoader.display(mContext, file, mAvatar);
                    mAvatarFile = file;
                }
            }

            @Override
            public void onFailure() {
            }
        });
        UserBean u = CommonAppConfig.getUserBean();
        if (u != null) {
            showData(u);
        } else {
            MainAPI.getBaseInfo(new HttpCallback() {
                @Override
                public void onSuccess(int code, String msg, String[] info) {
                    if(BaseHttpCallBack.isSuccess(code)&&info.length>0){
                        String json=info[0];
                        UserBean userBean=JSON.parseObject(json,UserBean.class);
                        CommonAppConfig.setUserBean(userBean,json);
                        showData(userBean);
                    }
                }
            });
        }
    }

    private void showData(UserBean u) {
        mUserBean = u;
        mAvatarRemoteFileName=u.getAvatar();
        ImgLoader.displayAvatar(mContext, mAvatarRemoteFileName, mAvatar);
        String name = u.getUserNiceName();
        if (!TextUtils.isEmpty(name)) {
            if (name.length() > 7) {
                name = name.substring(0, 7);
            }
            mName.setText(name);
            mName.setSelection(name.length());
        }
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_save) {
            save();
        } else if (i == R.id.btn_avatar) {
            editAvatar();
        }
    }

    private void editAvatar() {
        DialogUitl.showStringArrayDialog(mContext, new Integer[]{
                R.string.camera, R.string.alumb}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                if (tag == R.string.camera) {
                    mImageUtil.getImageByCamera();
                } else {
                    mImageUtil.getImageByAlumb();
                }
            }
        });
    }

    /**
     * 保存
     */
    public void save() {
        mNameVal = mName.getText().toString().trim();
        if (TextUtils.isEmpty(mNameVal)) {
            ToastUtil.show(R.string.edit_profile_name_empty);
            return;
        }

        if(mNameVal.length()>7){
            ToastUtil.show(R.string.edit_profile_nickname);
            return;
        }

        if (mUserBean != null && mNameVal.equals(mUserBean.getUserNiceName()) && mAvatarFile == null) {
            ToastUtil.show(R.string.edit_profile_not_update);
            return;
        }
        if (mAvatarFile != null) {
            uploadAvatarImage();
        } else {
            submit();
        }
    }


    private void uploadAvatarImage() {
        CommonAPI.upload(mAvatarFile, new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
                if(isSuccess(code)){
                    mAvatarRemoteFileName=info.getString("url");
                    submit();
                }
            }
            @Override
            public Dialog createLoadingDialog() {
                return DialogUitl.loadingDialog(mContext);
            }
            @Override
            public boolean showLoadingDialog() {
                return true;
            }
        });
    }

    private void submit() {
        MainAPI.updateUserInfo(mAvatarRemoteFileName,mNameVal).subscribe(new DefaultObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                disMissLoadDialog();
                if(aBoolean){
                    Intent intent=getIntent();
                    intent.putExtra(Constants.URL,mAvatarRemoteFileName);
                    intent.putExtra(Constants.DATA,mNameVal);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
            @Override
            public void onError(Throwable e) {
                disMissLoadDialog();
                super.onError(e);
            }
            @Override
            public void onComplete() {
                disMissLoadDialog();
                super.onComplete();
            }
        });
    }

    private void disMissLoadDialog() {
        if(mLoading!=null&&mLoading.isShowing()){
            mLoading.dismiss();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }
}
