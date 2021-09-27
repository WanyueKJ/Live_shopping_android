package com.wanyue.main.view.proxy;

import android.view.ViewGroup;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.main.R;
import com.wanyue.main.adapter.CoinRecordAdapter;
import com.wanyue.main.api.MainAPI;
import com.wanyue.main.bean.CommissionBean;
import com.wanyue.main.bean.CommissionSectionBean;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class CoinRecordListViewProxy extends RxViewProxy {
    private RxRefreshView mRefreshView;
    private CoinRecordAdapter mCoinRecordAdapter;
    private int mType;
    @Override
    public int getLayoutId() {
        return R.layout.view_single_refresh;
    }
    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mRefreshView = (RxRefreshView) findViewById(R.id.refreshView);
        mCoinRecordAdapter=new CoinRecordAdapter(null);
        mRefreshView.setAdapter(mCoinRecordAdapter);
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createLinearSetting(getActivity(),1));
        mRefreshView.setDataListner(new RxRefreshView.DataListner<MultiItemEntity>() {
            @Override
            public Observable<List<MultiItemEntity>> loadData(int p) {
                return getData(p);
            }
            @Override
            public void compelete(List<MultiItemEntity> data) {
            }
            @Override
            public void error(Throwable e) {

            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){

            if(mRefreshView!=null){
               mRefreshView.initData();
            }
        }
    }

    private Observable<List<MultiItemEntity>> getData(int p) {
        return MainAPI.getCommissionList(p,mType).map(new Function<List<CommissionSectionBean>, List<MultiItemEntity>>() {
            @Override
            public List<MultiItemEntity> apply(List<CommissionSectionBean> commissionSectionBeans) throws Exception {
                List<MultiItemEntity>list=new ArrayList<>();
                if(ListUtil.haveData(commissionSectionBeans)){
                    for(CommissionSectionBean sectionBean:commissionSectionBeans){
                        list.add(sectionBean);
                        List<CommissionBean>data=sectionBean.getList();
                        if(ListUtil.haveData(data)){
                            list.addAll(data);
                        }
                    }
                }
                return list;
            }
        }).compose(this.<List<MultiItemEntity>>bindUntilOnDestoryEvent());
    }

    public void setType(int type) {
        mType = type;
    }
}
