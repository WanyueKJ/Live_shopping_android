package com.wanyue.common.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.Constants;
import com.wanyue.common.R;
import com.wanyue.common.bean.ConfigBean;
import com.wanyue.common.dialog.CommonShareDialogFragment;
import com.wanyue.common.mob.MobShareUtil;
import com.wanyue.common.mob.ShareData;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.LanguageUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;

import java.lang.ref.WeakReference;

/**
 * Created by  on 2018/9/25.
 */

public class WebViewActivity extends BaseActivity {

    private ProgressBar mProgressBar;
    private WebView mWebView;
    private final int CHOOSE = 100;//Android 5.0以下的
    private final int CHOOSE_ANDROID_5 = 200;//Android 5.0以上的
    private ValueCallback<Uri> mValueCallback;
    private ValueCallback<Uri[]> mValueCallback2;
    private String mShareCode;
    private MobShareUtil mMobShareUtil;

    @Override
    public int getLayoutId() {
        return R.layout.activity_webview;
    }

    @Override
    public void init() {
        WeakReference<Context>  contextWeakReference=new WeakReference<>(mContext);
        String url = getIntent().getStringExtra(Constants.URL);
        L.e("H5--->" + url);
        LinearLayout rootView = (LinearLayout) findViewById(R.id.rootView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mWebView = new WebView(contextWeakReference.get());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.topMargin = DpUtil.dp2px(1);
        mWebView.setLayoutParams(params);
        mWebView.setOverScrollMode(View.OVER_SCROLL_NEVER);
        rootView.addView(mWebView);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                L.e("H5-------->" + url);
                if (url.startsWith(Constants.COPY_PREFIX)) {
                    String content = url.substring(Constants.COPY_PREFIX.length());
                    if (!TextUtils.isEmpty(content)) {
                        copy(content);
                    }
                } else if (url.startsWith(Constants.SHARE_PREFIX)) {
                    String content = url.substring(Constants.SHARE_PREFIX.length());
                    if (!TextUtils.isEmpty(content)) {
                        mShareCode = content;
                        openShareWindow();
                    }
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                setTitle(view.getTitle());
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                } else {
                    mProgressBar.setProgress(newProgress);
                }
            }
            //以下是在各个Android版本中 WebView调用文件选择器的方法
            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> valueCallback) {
                openImageChooserActivity(valueCallback);
            }
            // For Android  >= 3.0
            public void openFileChooser(ValueCallback valueCallback, String acceptType) {
                openImageChooserActivity(valueCallback);
            }

            //For Android  >= 4.1
            public void openFileChooser(ValueCallback<Uri> valueCallback,
                                        String acceptType, String capture) {
                openImageChooserActivity(valueCallback);
            }

            // For Android >= 5.0
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onShowFileChooser(WebView webView,
                                             ValueCallback<Uri[]> filePathCallback,
                                             FileChooserParams fileChooserParams) {
                mValueCallback2 = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                startActivityForResult(intent, CHOOSE_ANDROID_5);
                return true;
            }
        });
        mWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.loadUrl(url);
    }


    private void openImageChooserActivity(ValueCallback<Uri> valueCallback) {
        mValueCallback = valueCallback;
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT < 19) {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            intent.setAction(Intent.ACTION_PICK);
            intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, WordUtil.getString(R.string.choose_flie)), CHOOSE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (requestCode) {
            case CHOOSE://5.0以下选择图片后的回调
                processResult(resultCode, intent);
                break;
            case CHOOSE_ANDROID_5://5.0以上选择图片后的回调
                processResultAndroid5(resultCode, intent);
                break;
        }
    }

    private void processResult(int resultCode, Intent intent) {
        if (mValueCallback == null) {
            return;
        }
        if (resultCode == RESULT_OK && intent != null) {
            Uri result = intent.getData();
            mValueCallback.onReceiveValue(result);
        } else {
            mValueCallback.onReceiveValue(null);
        }
        mValueCallback = null;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void processResultAndroid5(int resultCode, Intent intent) {
        if (mValueCallback2 == null) {
            return;
        }
        if (resultCode == RESULT_OK && intent != null) {
            mValueCallback2.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(resultCode, intent));
        } else {
            mValueCallback2.onReceiveValue(null);
        }
        mValueCallback2 = null;
    }

    protected boolean canGoBack() {
        return mWebView != null && mWebView.canGoBack();
    }


    @Override
    public void onBackPressed() {
        if (isNeedExitActivity()) {
            finish();
        } else {
            if (canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
        }
    }


    private boolean isNeedExitActivity() {
        if (mWebView != null) {
            String url = mWebView.getUrl();
            if (!TextUtils.isEmpty(url)) {
                return url.contains("auth/index")//实名认证成功页面
                        || url.contains("skillauth/apply");//技能申请提交成功页面

            }
        }
        return false;
    }

    public static void forward(Context context, String url, boolean addArgs) {
        if (addArgs) {
            url = StringUtil.contact(url,"&lang=", LanguageUtil.getInstance().getLanguage());
            url = StringUtil.contact(url, "&uid=", CommonAppConfig.getUid(), "&token=", CommonAppConfig.getToken());
        }
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(Constants.URL, url);
        context.startActivity(intent);
    }

    public static void forward(Context context, String url) {
        forward(context, url, true);
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {
            ViewGroup parent = (ViewGroup) mWebView.getParent();
            if (parent != null) {
                parent.removeView(mWebView);
            }
            mWebView.destroy();
        }
        super.onDestroy();
    }

    /**
     * 复制到剪贴板
     */

    private void copy(String content) {
        ClipboardManager cm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", content);
        cm.setPrimaryClip(clipData);
        ToastUtil.show(getString(R.string.copy_success));
    }
    /**
     * 分享
     */

    private void openShareWindow() {
        CommonShareDialogFragment fragment = new CommonShareDialogFragment();
        fragment.setActionListener(new CommonShareDialogFragment.ActionListener() {
            @Override
            public void onItemClick(String type) {
                ConfigBean configBean = CommonAppConfig.getConfig();
                ShareData data = new ShareData();
              /*  data.setTitle(configBean.getAgentShareTitle());
                data.setDes(configBean.getAgentShareDes());*/
                data.setImgUrl(CommonAppConfig.getUserBean().getAvatarThumb());
                String webUrl =  mShareCode;
                data.setWebUrl(webUrl);
                if (mMobShareUtil == null) {
                    mMobShareUtil = new MobShareUtil();
                }
                mMobShareUtil.execute(type, data, null);
            }
        });
                fragment.show(getSupportFragmentManager(), "CommonShareDialogFragment");
    }

}
