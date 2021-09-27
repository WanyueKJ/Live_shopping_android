package com.wanyue.live.views;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.wanyue.common.adapter.RefreshAdapter;
import com.wanyue.common.custom.CommonRefreshView;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.views.AbsCommonViewHolder;
import com.wanyue.live.R;
import com.wanyue.live.activity.RoomManageDetailActivity;
import com.wanyue.live.adapter.LiveAdminRoomAdapter;
import com.wanyue.live.bean.LiveAdminRoomBean;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;

import java.util.Arrays;
import java.util.List;

/**
 * Created by  on 2019/4/23.
 * 我的房间
 */

public class LiveMyRoomViewHolder extends AbsCommonViewHolder implements OnItemClickListener<LiveAdminRoomBean> {

    private CommonRefreshView mRefreshView;
    private LiveAdminRoomAdapter mAdapter;

    public LiveMyRoomViewHolder(Context context, ViewGroup parentView) {
        super(context, parentView);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_my_room;
    }

    @Override
    public void init() {
        mRefreshView = (CommonRefreshView) findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_admin_room);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<LiveAdminRoomBean>() {
            @Override
            public RefreshAdapter<LiveAdminRoomBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new LiveAdminRoomAdapter(mContext);
                    mAdapter.setOnItemClickListener(LiveMyRoomViewHolder.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                LiveHttpUtil.getMyAdminRoomList(p, callback);
            }

            @Override
            public List<LiveAdminRoomBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), LiveAdminRoomBean.class);
            }

            @Override
            public void onRefreshSuccess(List<LiveAdminRoomBean> list, int listCount) {

            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<LiveAdminRoomBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
    }

    @Override
    public void loadData() {
        if (isFirstLoadData()) {
            if (mRefreshView != null) {
                mRefreshView.initData();
            }
        }
    }

    @Override
    public void onDestroy() {
        LiveHttpUtil.cancel(LiveHttpConsts.GET_MY_ADMIN_ROOM_LIST);
        super.onDestroy();
    }

    @Override
    public void onItemClick(LiveAdminRoomBean bean, int position) {
        RoomManageDetailActivity.forward(mContext, bean);
    }
}
