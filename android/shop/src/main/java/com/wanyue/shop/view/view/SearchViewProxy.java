package com.wanyue.shop.view.view;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.R2;
import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

public class SearchViewProxy extends RxViewProxy {

    @BindView(R2.id.et_search)
    EditText mEtSearch;
    @BindView(R2.id.btn_clear)
    ImageView mBtnClear;

    private String mKeyward;
    private SeacherListner mSeacherListner;
    private MyHandler mHandler;
    private String mHint;
    private boolean mIsAuto=true;

    @Override
    public int getLayoutId() {
        return R.layout.item_search;
    }

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        if(mHint!=null){
           mEtSearch.setHint(mHint);
        }
        if(mIsAuto){
          mHandler = new MyHandler(this);
        }
        if(mKeyward!=null){
           mEtSearch.setText(mKeyward);
        }
        ViewUtil.setEditextEnable2(mEtSearch);
    }

    public void setEnableAutoSearch(boolean isAuto){
        mIsAuto=isAuto;
    }

    /*原来的文本监听*/
    @OnTextChanged(value = R2.id.et_search, callback = OnTextChanged.Callback.TEXT_CHANGED)
    public void watchTextChange(CharSequence sequence, int start, int before, int count) {
         if(TextUtils.isEmpty(sequence)){
             mBtnClear.setVisibility(View.INVISIBLE);
         }else{
             mBtnClear.setVisibility(View.VISIBLE);
         }
         if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.sendEmptyMessageDelayed(0, 500);
        }
    }

    /*设置隐藏的显示*/
    public void setHint(String hint) {
        mHint = hint;
    }

    /*监听输入框事件*/
    @OnEditorAction(value = R2.id.et_search)
    public boolean watchEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            search();
            return true;
        }
        return false;
    }

    public void search() {
        mKeyward = mEtSearch.getText().toString();
        if (mSeacherListner != null) {
            mSeacherListner.search(mKeyward);
        }
        mEtSearch.clearFocus();
    }

    public void setSeacherListner(SeacherListner seacherListner) {
        mSeacherListner = seacherListner;
    }

    @OnClick(R2.id.btn_clear)
    public void clear() {
        mEtSearch.setText("");
    }

    public void setTitle(String title) {
        mKeyward=title;
        if(mEtSearch!=null){
           mEtSearch.setText(mKeyward);
        }
    }

    public static interface SeacherListner {
        public void search(String keyward);
    }

    private static class MyHandler extends Handler {
        private SearchViewProxy mSearchViewProxy;

        public MyHandler(SearchViewProxy seacherListner) {
            mSearchViewProxy = seacherListner;
        }

        @Override
        public void handleMessage(Message msg) {
            if (mSearchViewProxy != null) {
                mSearchViewProxy.search();
            }
        }

        public void release() {
            mSearchViewProxy = null;
        }
    }

    @Override
    protected boolean shouldBindButterKinfe() {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSeacherListner = null;
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler.release();
        }
        mHandler = null;
    }
}
