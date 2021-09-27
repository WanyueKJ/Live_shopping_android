package com.wanyue.common.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.wanyue.common.CommonApplication;
import com.wanyue.common.R;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;

/**
 * Created by  on 2018/8/29.
 * 服务器Home.getConfig接口有时候返回的数据无法解析，导致崩溃，
 * 这个类是用来收集服务器返回的错误的信息的
 */

public class ErrorActivity extends BaseActivity {

    public static void forward(String title, String errorInfo) {
        Intent intent = new Intent(CommonApplication.sInstance, ErrorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("title", title);
        intent.putExtra("error", errorInfo);
        CommonApplication.sInstance.startActivity(intent);
    }

    private TextView mTextView;
    private String mErrorInfo;

    @Override
    public void init() {
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        mErrorInfo = intent.getStringExtra("error");
        setTitle(title);
        mTextView = (TextView) findViewById(R.id.text);
        mTextView.setText(mErrorInfo);
        findViewById(R.id.btn_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyError();
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_error;
    }


    private void copyError() {
        if (TextUtils.isEmpty(mErrorInfo)) {
            return;
        }
        ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", mErrorInfo);
        clipboardManager.setPrimaryClip(clipData);
        ToastUtil.show(WordUtil.getString(R.string.copy_success));
    }
}
