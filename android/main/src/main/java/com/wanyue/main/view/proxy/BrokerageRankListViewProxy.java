package com.wanyue.main.view.proxy;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.utils.JsonUtil;
import com.wanyue.main.R;
import com.wanyue.main.adapter.BrokerageRankAdapter;
import com.wanyue.main.api.MainAPI;
import com.wanyue.main.bean.BrokerageRankBean;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public abstract class BrokerageRankListViewProxy extends RxViewProxy {
    private RxRefreshView<BrokerageRankBean> mRefreshView;
    private BrokerageRankAdapter mBrokerageRankAdapter;
    private String mType;

    @Override
    public int getLayoutId() {
        return R.layout.view_brokerage_rank_list;
    }
    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mRefreshView = (RxRefreshView) findViewById(R.id.refreshView);
        mBrokerageRankAdapter=new BrokerageRankAdapter(null);
        mRefreshView.setAdapter(mBrokerageRankAdapter);
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createLinearSetting(getActivity(),0));
        mRefreshView.setDataListner(new RxRefreshView.DataListner<BrokerageRankBean>() {
            @Override
            public Observable<List<BrokerageRankBean>> loadData(int p) {
                return getData(p);
            }
            @Override
            public void compelete(List<BrokerageRankBean> data) {
            }
            @Override
            public void error(Throwable e) {
            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser&&mIsFirstUserVisble&&mRefreshView!=null){
            mRefreshView.initData();
        }
    }

    public void setType(String type) {
        mType = type;
    }

    private Observable<List<BrokerageRankBean>> getData(int p) {
        return MainAPI.getBrokerageRankList(p,mType).map(new Function<JSONObject, List<BrokerageRankBean>>() {
            @Override
            public List<BrokerageRankBean> apply(JSONObject jsonObject) throws Exception {
                int position =jsonObject.getIntValue("position");
                positionNotify(position);
                JSONArray jsonArray=jsonObject.getJSONArray("rank");
                List<BrokerageRankBean>list= JsonUtil.getData(jsonArray,BrokerageRankBean.class);
                return list;
            }
        }).compose(this.<List<BrokerageRankBean>>bindToLifecycle());
    }

    public abstract void positionNotify(int position);

}
