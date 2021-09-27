package com.wanyue.live.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.fxc.roundcornerlayout.RoundCornerLinearLayout;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.BaseHttpCallBack;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.interfaces.ActivityResultCallback;
import com.wanyue.common.interfaces.ImageResultCallback;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ProcessImageUtil;
import com.wanyue.common.utils.RouteUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.common.views.AbsViewHolder;
import com.wanyue.live.R;
import com.wanyue.live.activity.LiveActivity;
import com.wanyue.live.activity.LiveAnchorActivity;
import com.wanyue.live.activity.LiveChooseClassActivity;
import com.wanyue.live.bean.LiveBean;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.live.model.LiveModel;

import java.io.File;

/**
 * Created by  on 2018/10/7.
 * 开播前准备
 */

public class LiveReadyViewHolder extends AbsViewHolder implements View.OnClickListener {

    private ProcessImageUtil mImageUtil;
    private TextView mCity;
    private int mLiveClassID;//直播频道id
    private ActivityResultCallback mActivityResultCallback;
    private boolean mOpenLocation = true;
    private int mLiveSdk=Constants.LIVE_SDK_TX;
    private ImageView mAvatar;
    private TextView mCoverText;
    private EditText mEditTitle;
    private RoundCornerLinearLayout mBtnChooseClass;
    private Button mBtnStartLive;
    private TextView mTvLiveClass;
    private String mThumbUrl;
    private long mLastClickBackTime;

    public LiveReadyViewHolder(Context context, ViewGroup parentView, int liveSdk, int haveStore) {
        super(context, parentView, liveSdk, haveStore);
    }

    @Override
    protected void processArguments(Object... args) {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_ready;
    }

    @Override
    public void init() {
        mCity =  findViewById(R.id.city);
        mAvatar =  findViewById(R.id.avatar);
        mCoverText =  findViewById(R.id.cover_text);
        mEditTitle = findViewById(R.id.edit_title);
        mBtnChooseClass =  findViewById(R.id.btn_choose_class);
        mBtnStartLive =  findViewById(R.id.btn_start_live);
        mTvLiveClass =  findViewById(R.id.tv_live_class);
        mCity.setText(CommonAppConfig.getUserBean().getCity());
        findViewById(R.id.btn_locaiton).setOnClickListener(this);
        mOpenLocation = true;
        mImageUtil = ((LiveActivity) mContext).getProcessImageUtil();
        mImageUtil.setImageResultCallback(new ImageResultCallback() {
            @Override
            public void beforeCamera() {
                ((LiveAnchorActivity) mContext).beforeCamera();
            }
            @Override
            public void onSuccess(final File file) {
                if (file != null) {
                    ImgLoader.display(mContext, file, mAvatar);
                    uploadThumb(file);
                }
            }
            @Override
            public void onFailure() {
            }
        });

        mEditTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
             checkStartLiveButtonEnale();
            }
        });

        mBtnChooseClass.setOnClickListener(this);
        findViewById(R.id.avatar_group).setOnClickListener(this);
        findViewById(R.id.btn_camera).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);
        findViewById(R.id.btn_beauty).setOnClickListener(this);
        findViewById(R.id.btn_start_live).setOnClickListener(this);
        mActivityResultCallback = new ActivityResultCallback() {
            @Override
            public void onSuccess(Intent intent) {
                mLiveClassID = intent.getIntExtra(Constants.CLASS_ID, 0);
                mTvLiveClass.setText(intent.getStringExtra(Constants.CLASS_NAME));
                checkStartLiveButtonEnale();
            }
        };
    }

    private void uploadThumb(final File file) {
        CommonAPI.upload(file, new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
                if(BaseHttpCallBack.isSuccess(code)){
                    mThumbUrl=info.getString("url");
                    if (file != null) {
                        ImgLoader.display(mContext,file,mAvatar);
                        mCoverText.setText(WordUtil.getString(R.string.live_cover_2));
                        mCoverText.setBackground(ContextCompat.getDrawable(mContext, R.drawable.bg_live_cover));
                    }
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

    private void checkStartLiveButtonEnale() {
        String title=mEditTitle.getText().toString();
        if(TextUtils.isEmpty(title)||mLiveClassID<=0&&mThumbUrl!=null){
            mBtnStartLive.setEnabled(false);
            mBtnStartLive.setAlpha(0.4F);
        }else{
            mBtnStartLive.setEnabled(true);
            mBtnStartLive.setAlpha(1F);
        }
    }

    @Override
    public void onClick(View v) {
        if (!canClick()) {
            return;
        }
        int i = v.getId();
        if (i == R.id.avatar_group) {
            setAvatar();
        } else if (i == R.id.btn_camera) {
            toggleCamera();
        } else if (i == R.id.btn_close) {
           finish();
        } else if (i == R.id.btn_choose_class) {
            chooseLiveClass();
        } else if (i == R.id.btn_beauty) {
            beauty();
        }  else if (i == R.id.btn_start_live) {
            startLive(v);
        } else if (i == R.id.btn_locaiton) {
            switchLocation();
        }
    }

    private void showOutLoginDialog() {
        DialogUitl.showSimpleDialog(mContext, mContext.getString(R.string.if_out_login_tip), new DialogUitl.SimpleCallback() {
            @Override
            public void onConfirmClick(Dialog dialog, String content) {
                CommonAppConfig.clearLoginInfo();
                RouteUtil.forwardLogin();
                finish();
            }
        });
    }


    public void finish(){
        if(mContext!=null &&mContext instanceof Activity){
            ((Activity) mContext).finish();
        }
    }

    /**
     * 打开 关闭位置
     */

    private void switchLocation() {

    }

    private void toggleLocation() {

    }

    /**
     * 设置头像
     */
    private void setAvatar() {
        if (mLiveSdk == Constants.LIVE_SDK_TX) {
            mImageUtil.getImageByAlumb();
        } else {
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
    }

    /**
     * 切换镜头
     */
    private void toggleCamera() {
        ((LiveAnchorActivity) mContext).toggleCamera();
    }



    /**
     * 选择直播频道
     */
    private void chooseLiveClass() {
        Intent intent = new Intent(mContext, LiveChooseClassActivity.class);
        intent.putExtra(Constants.CLASS_ID, mLiveClassID);
        mImageUtil.startActivityForResult(intent, mActivityResultCallback);
    }

    /**
     * 设置美颜
     */

    private void beauty() {
        ((LiveAnchorActivity) mContext).beauty();
    }


    public void hide() {
        if (mContentView != null && mContentView.getVisibility() == View.VISIBLE) {
            mContentView.setVisibility(View.INVISIBLE);
        }
    }

    public void show() {
        if (mContentView != null && mContentView.getVisibility() != View.VISIBLE) {
            mContentView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 点击开始直播按钮
     */


    private void startLive(View view) {
        boolean startPreview = ((LiveAnchorActivity) mContext).isStartPreview();
        if (!startPreview) {
            ToastUtil.show(R.string.please_wait);
            return;
        }
        if (mLiveClassID == 0) {
            ToastUtil.show(R.string.live_choose_live_class);
            return;
        }
        createRoom(view);
    }

    /**
     * 请求创建直播间接口，开始直播
     * @param view
     */

    private void createRoom(final View view) {
        if (mLiveClassID == 0) {
            ToastUtil.show(R.string.live_choose_live_class);
            return;
        }
        view.setEnabled(false);
        final String title = mEditTitle.getText().toString().trim();
        LiveHttpUtil.createRoom(title, mLiveClassID, mThumbUrl, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                view.setEnabled(true);
                if (HttpCallback.isSuccess(code)&& info.length > 0) {
                    L.e("开播", "createRoom------->" + info[0]);
                    LiveBean liveBean= LiveModel.getContextLiveBean((FragmentActivity) mContext);
                    if(liveBean!=null){
                        liveBean.setTitle(title);
                        liveBean.setThumb(mThumbUrl);
                    }
                    ((LiveAnchorActivity) mContext).startLiveSuccess(info[0], -1, -1);
                    ((LiveAnchorActivity) mContext).openShop(true);
                } else {
                    ToastUtil.show(msg);
                }
            }

            @Override
            public void onError() {
                super.onError();
                view.setEnabled(true);
            }
        });
    }

    @Override
    public void release() {
        mImageUtil = null;
        mActivityResultCallback = null;
    }

    @Override
    public void onDestroy() {
        LiveHttpUtil.cancel(LiveHttpConsts.CREATE_ROOM);
        CommonAPI.cancel(CommonAPI.UP_IMAGE);
    }


    public void onBackPressed() {
       /* long curTime = System.currentTimeMillis();
        if (curTime - mLastClickBackTime > 2000) {
            mLastClickBackTime = curTime;
            ToastUtil.show(R.string.main_click_next_exit);
            return;
        }*/
        ((Activity)mContext).finish();
    }
}
