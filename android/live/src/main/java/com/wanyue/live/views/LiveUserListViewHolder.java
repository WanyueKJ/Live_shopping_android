package com.wanyue.live.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.adapter.RefreshAdapter;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.custom.CommonRefreshView;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.views.AbsLivePageViewHolder;

import com.wanyue.live.R;
import com.wanyue.live.activity.LiveActivity;
import com.wanyue.live.adapter.LiveUserAdapter;
import com.wanyue.live.bean.LiveUserListBean;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.live.model.LiveModel;


import java.util.Arrays;
import java.util.List;

/**
 * 用户列表
 */
public class LiveUserListViewHolder extends AbsLiveDialogViewHolder implements OnItemClickListener<LiveUserListBean> {
    private CommonRefreshView mRefreshView;
    private LiveUserAdapter mLiveUserAdapter;
    private String mLiveUid;
    private String mStream;
    private String mSelfUid;

    public LiveUserListViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    public void init() {
        super.init();
        mLiveUid = LiveModel.getContextLiveUid((FragmentActivity) mContext);
        mStream = ((LiveActivity) mContext).getStream();
        findViewById(R.id.blank).setOnClickListener(this);
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.VERTICAL, false));
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<LiveUserListBean>() {
            @Override
            public RefreshAdapter<LiveUserListBean> getAdapter() {
                if (mLiveUserAdapter == null) {
                    mLiveUserAdapter = new LiveUserAdapter(mContext);
                    mLiveUserAdapter.setOnItemClickListener(LiveUserListViewHolder.this);
                }
                return mLiveUserAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                LiveHttpUtil.getUserList(mLiveUid, mStream,p, callback);
            }

            @Override
            public List<LiveUserListBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), LiveUserListBean.class);
            }

            @Override
            public void onRefreshSuccess(List<LiveUserListBean> list, int listCount) {
            }

            @Override
            public void onRefreshFailure() {
            }

            @Override
            public void onLoadMoreSuccess(List<LiveUserListBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
mSelfUid=CommonAppConfig.getUid();
    }

    @Override
    public void loadData() {
        if (mRefreshView != null) {
            mRefreshView.initData();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_user_list;
    }

    @Override
    public void onDestroy() {
        LiveHttpUtil.cancel(LiveHttpConsts.GET_USER_LIST);
        super.onDestroy();
    }

    @Override
    public void onItemClick(LiveUserListBean bean, int position) {
        if (bean!=null&&!bean.getUid().equals(mSelfUid)){
            ((LiveActivity)mContext).showUserManngerDialog(bean.getUid(),0);
        }

    }
}
