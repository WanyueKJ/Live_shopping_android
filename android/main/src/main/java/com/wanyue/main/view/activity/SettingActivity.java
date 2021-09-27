package com.wanyue.main.view.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.HtmlConfig;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.activity.WebViewActivity;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.bean.ConfigBean;
import com.wanyue.common.business.acmannger.ActivityMannger;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.utils.DialogUitl;
import com.wanyue.common.utils.GlideCatchUtil;
import com.wanyue.common.utils.LanguageUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.UMengUtil;
import com.wanyue.common.utils.VersionUtil;
import com.wanyue.common.utils.WordUtil;

import com.wanyue.live.activity.RoomManageActivity;
import com.wanyue.main.R;
import java.io.File;


public class SettingActivity extends BaseActivity {
    private TextView mVersion;
    private TextView mCacheSize;
    private Handler mHandler;

    @Override
    public int getLayoutId() {
        return R.layout.activity_setting;
    }

    @Override
    public void init() {
        setTabTitle(WordUtil.getString(R.string.setting));
        mVersion = findViewById(R.id.version);
        mCacheSize = findViewById(R.id.cache_size);
        mVersion.setText(VersionUtil.getVersion());
        mCacheSize.setText(getCacheSize());
        TextView lang = findViewById(R.id.lang);
        lang.setText(LanguageUtil.isEn() ? R.string.lang_en : R.string.lang_zh);
    }

    public void settingClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_about_us) {
            WebViewActivity.forward(this, HtmlConfig.CONNECT_US,false);
        } else if (i == R.id.btn_check_update) {
            checkVersion();
        } else if (i == R.id.btn_clear_cache) {
            clearCache();
        } else if (i == R.id.btn_logout) {
            logout();
        } else if (i == R.id.btn_lang) {
            chooseLanguage();
        }else if (i==R.id.btn_room_manage){
            RoomManageActivity.forward(mContext);
        }
    }


    /**
     * 切换语言
     */
    public void chooseLanguage() {
        DialogUitl.showStringArrayDialog(this, new Integer[]{R.string.lang_en, R.string.lang_zh}, new DialogUitl.StringArrayDialogCallback() {
            @Override
            public void onItemClick(String text, int tag) {
                String targetLang = null;
                if (tag == R.string.lang_en) {
                    targetLang = Constants.LANG_EN;
                } else if (tag == R.string.lang_zh) {
                    targetLang = Constants.LANG_ZH;
                }
                if (TextUtils.isEmpty(targetLang) || targetLang.equals(LanguageUtil.getInstance().getLanguage())) {
                    return;
                }
                LanguageUtil.getInstance().updateLanguage(targetLang);
                //ImMessageUtil.getInstance().refreshMsgTypeString();
              /*  CommonHttpUtil.getConfig(new CommonCallback<ConfigBean>() {
                    @Override
                    public void callback(ConfigBean bean) {
                        Intent intent = new Intent(mContext, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });*/

            }
        });
    }
    /**
     * 检查更新
     */


    private void checkVersion() {
        ToastUtil.show(R.string.version_latest);
      /*  CommonAPI.getConfig().compose(this.<ConfigBean>bindUntilEvent(ActivityEvent.DESTROY)).subscribe(new DefaultObserver<ConfigBean>() {
            @Override
            public void onNext(ConfigBean configBean) {
                if (VersionUtil.isLatest(configBean.getVersion())) {
                    ToastUtil.show(R.string.version_latest);
                } else {
                    VersionUtil.showDialog(mContext, configBean.getUpdateDes(), configBean.getDownloadApkUrl());
                }
            }
        });*/
    }

    /**
     * 退出登录
     */
    private void logout() {
        CommonAppConfig.clearLoginInfo();
        //退出IM
        //ImPushUtil.getInstance().logout();
        //友盟统计登出
        UMengUtil.onLogout();
        ActivityMannger.getInstance().clearAllActivity();
        startActivity(LoginActivity.class);

    }


    /**
     * 获取缓存
     */
    private String getCacheSize() {
        return GlideCatchUtil.getInstance().getCacheSize();
    }

    /**
     * 清除缓存
     */
    private void clearCache() {
        final Dialog dialog = DialogUitl.loadingDialog(mContext, WordUtil.getString(R.string.setting_clear_cache_ing));
        dialog.show();
        GlideCatchUtil.getInstance().clearImageAllCache();
        File gifGiftDir = new File(CommonAppConfig.GIF_PATH);
        if (gifGiftDir.exists() && gifGiftDir.length() > 0) {
            gifGiftDir.delete();
        }

        if (mHandler == null) {
            mHandler = new Handler();
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (mCacheSize != null) {
                    mCacheSize.setText(getCacheSize());
                }
                ToastUtil.show(R.string.setting_clear_cache);
            }
        }, 2000);
    }


    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }

        super.onDestroy();
    }
}
