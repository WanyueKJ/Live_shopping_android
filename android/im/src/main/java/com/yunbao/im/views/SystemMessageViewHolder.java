package com.yunbao.im.views;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.yunbao.common.adapter.RefreshAdapter;
import com.yunbao.common.custom.CommonRefreshView;
import com.yunbao.common.http.HttpCallback;
import com.yunbao.common.views.AbsViewHolder;
import com.yunbao.im.R;
import com.yunbao.im.adapter.SystemMessageAdapter;
import com.yunbao.im.bean.SystemMessageBean;
import com.yunbao.im.http.ImHttpConsts;
import com.yunbao.im.http.ImHttpUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by  on 2018/11/28.
 */

public class SystemMessageViewHolder extends AbsViewHolder implements View.OnClickListener {

    private CommonRefreshView mRefreshView;
    private SystemMessageAdapter mAdapter;
    private ActionListener mActionListener;


    public SystemMessageViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_sys_msg;
    }

    @Override
    public void init() {
        findViewById(R.id.btn_back).setOnClickListener(this);
        mRefreshView = (CommonRefreshView) findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_sys_msg);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<SystemMessageBean>() {
            @Override
            public RefreshAdapter<SystemMessageBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new SystemMessageAdapter(mContext);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                ImHttpUtil.getSystemMessageList(p, callback);
            }

            @Override
            public List<SystemMessageBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), SystemMessageBean.class);
            }

            @Override
            public void onRefreshSuccess(List<SystemMessageBean> list, int listCount) {

            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<SystemMessageBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
    }

    public void loadData() {
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }

    public void release() {
        mActionListener=null;
        ImHttpUtil.cancel(ImHttpConsts.GET_SYSTEM_MESSAGE_LIST);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btn_back) {
            if (mActionListener != null) {
                mActionListener.onBackClick();
            }

        }
    }

    public void setActionListener(ActionListener actionListener) {
        mActionListener = actionListener;
    }

    public interface ActionListener{
        void onBackClick();
    }
}
