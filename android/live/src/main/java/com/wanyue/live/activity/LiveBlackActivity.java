package com.wanyue.live.activity;

import android.content.Context;
import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.AbsActivity;
import com.wanyue.common.adapter.RefreshAdapter;
import com.wanyue.common.custom.CommonRefreshView;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.adapter.LiveBlackAdapter;
import com.wanyue.live.bean.LiveShutUpBean;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import java.util.Arrays;
import java.util.List;

/**
 * Created by  on 2019/4/27.
 * 直播间拉黑用户列表
 */

public class LiveBlackActivity extends AbsActivity implements OnItemClickListener<LiveShutUpBean> {

    public static void forward(Context context, String liveUid) {
        Intent intent = new Intent(context, LiveBlackActivity.class);
        intent.putExtra(Constants.LIVE_UID, liveUid);
        context.startActivity(intent);
    }

    private CommonRefreshView mRefreshView;
    private LiveBlackAdapter mAdapter;
    private String mLiveUid;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shut_up;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.live_user_black_list));
        mLiveUid = getIntent().getStringExtra(Constants.LIVE_UID);
        if (TextUtils.isEmpty(mLiveUid)) {
            return;
        }
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_black);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<LiveShutUpBean>() {
            @Override
            public RefreshAdapter<LiveShutUpBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new LiveBlackAdapter(mContext);
                    mAdapter.setOnItemClickListener(LiveBlackActivity.this);
                }
                return mAdapter;
            }

            @Override
            public void loadData(int p, HttpCallback callback) {
                LiveHttpUtil.getLiveBlackList(mLiveUid, p, callback);
            }

            @Override
            public List<LiveShutUpBean> processData(String[] info) {
                return JSON.parseArray(Arrays.toString(info), LiveShutUpBean.class);
            }

            @Override
            public void onRefreshSuccess(List<LiveShutUpBean> list, int listCount) {

            }

            @Override
            public void onRefreshFailure() {

            }

            @Override
            public void onLoadMoreSuccess(List<LiveShutUpBean> loadItemList, int loadItemCount) {

            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
        mRefreshView.initData();
    }

    @Override
    public void onItemClick(final LiveShutUpBean bean, int position) {
        LiveHttpUtil.liveCancelBlack(mLiveUid, bean.getUid(), new HttpCallback() {
            @Override
            public void onSuccess(int code, String msg, String[] info) {
                if (code == 0) {
                    if (mAdapter != null) {
                        mAdapter.removeItem(bean.getUid());
                    }
                }
                ToastUtil.show(msg);
            }
        });

    }

    @Override
    protected void onDestroy() {
        LiveHttpUtil.cancel(LiveHttpConsts.GET_LIVE_BLACK_LIST);
        LiveHttpUtil.cancel(LiveHttpConsts.LIVE_CANCEL_BLACK);
        super.onDestroy();
    }
}
