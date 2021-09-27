package com.wanyue.live.activity;

import android.content.Intent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.AbsActivity;
import com.wanyue.common.bean.LiveClassBean;
import com.wanyue.common.http.ParseArrayHttpCallBack;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.adapter.LiveReadyClassAdapter;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import java.util.List;

/**
 * Created by  on 2018/10/7.
 * 选择直播频道
 */


public class LiveChooseClassActivity extends AbsActivity implements OnItemClickListener<LiveClassBean> {

    private RecyclerView mRecyclerView;
    private int mCheckId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_live_choose_class;
    }

    @Override
    protected void main() {
        setTitle(WordUtil.getString(R.string.live_class_choose));
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mCheckId = getIntent().getIntExtra(Constants.CLASS_ID, 0);
        getLiveClass();
    }

    private void getLiveClass() {
        LiveHttpUtil.getLiveClass(new ParseArrayHttpCallBack<LiveClassBean>() {
            @Override
            public void onSuccess(int code, String msg, List<LiveClassBean> info) {
                if(ParseArrayHttpCallBack.isSuccess(code)){
                    initLiveClassList(info);
                }
            }
        });
    }

    private void initLiveClassList(List<LiveClassBean> list) {
        if (list == null) {
            return;
        }
        for (int i = 0, size = list.size(); i < size; i++) {
            LiveClassBean bean = list.get(i);
            if (bean.getId() == mCheckId) {
                bean.setChecked(true);
            } else {
                bean.setChecked(false);
            }
        }
        LiveReadyClassAdapter adapter = new LiveReadyClassAdapter(mContext, list);
        adapter.setOnItemClickListener(LiveChooseClassActivity.this);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(LiveClassBean bean, int position) {
        Intent intent = new Intent();
        intent.putExtra(Constants.CLASS_ID, bean.getId());
        intent.putExtra(Constants.CLASS_NAME, bean.getName());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LiveHttpUtil.cancel(LiveHttpConsts.GET_LIVE_CLASS);
    }
}
