package com.wanyue.live.views;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.bean.UserBean;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.common.views.AbsLivePageViewHolder;
import com.wanyue.live.R;
import com.wanyue.live.activity.LiveActivity;
import com.wanyue.live.activity.LiveAdminListActivity;
import com.wanyue.live.adapter.LiveAdminListAdapter;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;

import java.util.List;

/**
 * Created by  on 2018/10/16.
 */

public class LiveAdminListViewHolder extends AbsLivePageViewHolder implements OnItemClickListener<UserBean> {

    private String mLiveUid;
    private TextView mTextView;
    private RecyclerView mRecyclerView;
    private LiveAdminListAdapter mLiveAdminListAdapter;
    private HttpCallback mHttpCallback;
    private String mTotalCount;


    public LiveAdminListViewHolder(Context context, ViewGroup parentView, String liveUid) {
        super(context, parentView);
        mLiveUid = liveUid;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_live_admin_list;
    }

    @Override
    public void init() {
        super.init();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mTextView = (TextView) findViewById(R.id.text);
        mHttpCallback = new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (isSuccess(code) && info.length > 0) {
                    JSONObject obj = JSON.parseObject(info[0]);
                    List<UserBean> list = JSON.parseArray(obj.getString("list"), UserBean.class);
                    if (mLiveAdminListAdapter == null) {
                        mLiveAdminListAdapter = new LiveAdminListAdapter(mContext, list);
                        mLiveAdminListAdapter.setOnItemClickListener(LiveAdminListViewHolder.this);
                        mRecyclerView.setAdapter(mLiveAdminListAdapter);
                    } else {
                        mLiveAdminListAdapter.setList(list);
                    }
                    mTotalCount = obj.getString("count");
                    showTip();
                }
            }
        };
    }

    private void showTip() {
        mTextView.setText(WordUtil.getString(R.string.live_admin_count) + "(" + mLiveAdminListAdapter.getItemCount() + "/" + mTotalCount + ")");
    }


    @Override
    public void loadData() {
        LiveHttpUtil.getAdminList(mLiveUid,1, mHttpCallback);
    }

    @Override
    public void hide() {
        if (mContext instanceof LiveAdminListActivity) {
            ((LiveAdminListActivity) mContext).onBackPressed();
        } else {
            super.hide();
        }
    }

    @Override
    public void onHide() {
        if (mLiveAdminListAdapter != null) {
            mLiveAdminListAdapter.clear();
        }
    }

    @Override
    public void onItemClick(final UserBean bean, int position) {
        LiveHttpUtil.setAdmin(mLiveUid, bean.getId(),false, new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (isSuccess(code)) {
                    //int res = JSON.parseObject(info[0]).getIntValue("isadmin");
                    //if (res == 0) {//被取消管理员
                        if (mLiveAdminListAdapter != null) {
                            mLiveAdminListAdapter.removeItem(bean.getId());
                            showTip();
                        }
                        if(mContext instanceof LiveActivity){
                            ((LiveActivity) mContext).sendSetAdminMessage(false, bean.getId(), bean.getUserNiceName());
                        }
                    //}
                }
                ToastUtil.show(msg);
            }
        });
    }

    @Override
    public void release() {
        if (mLiveAdminListAdapter != null) {
            mLiveAdminListAdapter.release();
        }
        mLiveAdminListAdapter = null;
        super.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LiveHttpUtil.cancel(LiveHttpConsts.GET_ADMIN_LIST);
        LiveHttpUtil.cancel(LiveHttpConsts.SET_ADMIN);
    }
}
