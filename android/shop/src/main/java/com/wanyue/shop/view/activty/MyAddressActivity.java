package com.wanyue.shop.view.activty;

import android.view.View;
import androidx.lifecycle.Observer;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.server.RequestFactory;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.adapter.AddressAdapter;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.AddressInfoBean;
import com.wanyue.shop.business.ShopEvent;
import java.util.List;
import io.reactivex.Observable;

public class MyAddressActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {
    private RxRefreshView mRefreshView;
    private AddressAdapter mAddressAdapter;

    @Override
    public void init() {
        setTabTitle(R.string.address_mannger);
        setTabBackGound(R.color.white);
        mRefreshView = findViewById(R.id.refreshView);
        mRefreshView.setIconId(R.drawable.icon_empty_no_address);
        mAddressAdapter= new AddressAdapter(null, this);
        mAddressAdapter.setOnItemClickListener(this);
        mRefreshView.setAdapter(mAddressAdapter);
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createLinearSetting(this,10));
        mRefreshView.setDataListner(new RxRefreshView.DataListner<AddressInfoBean>() {
            @Override
            public Observable<List<AddressInfoBean>> loadData(int p) {
                return getData(p);
            }
            @Override
            public void compelete(List<AddressInfoBean> data) {
            }
            @Override
            public void error(Throwable e) {
                e.printStackTrace();
            }
        });
        LiveEventBus.get(ShopEvent.ADDRESS_CHANGE, AddressInfoBean.class).observe(this, new Observer<AddressInfoBean>() {
            @Override
            public void onChanged(AddressInfoBean addressInfoBean) {
                if(mRefreshView!=null){
                   mRefreshView.initData();
                }
            }
        });
        findViewById(R.id.btn_add_addr).setOnClickListener(this);
    }

    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        if(mRefreshView!=null){
           mRefreshView.initData();
        }
    }

    private Observable<List<AddressInfoBean>> getData(int p) {
        return ShopAPI.getAddressInfoList(p).compose(this.<List<AddressInfoBean>>bindToLifecycle());
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_my_address;
    }

    public void toCreateNewAddress(View view){
        startActivity(CreateAddressActivity.class);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RequestFactory.getRequestManager().cancle(ShopAPI.SET_DEFAULT_ADDRESS);
        RequestFactory.getRequestManager().cancle(ShopAPI.ADDRESS_DELETE);
    }
    @Override
    public void onClick(View v) {
        toCreateNewAddress(v);
    }
    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if(mAddressAdapter==null|| !ClickUtil.canClick()){
            return;
        }
       AddressInfoBean addressInfoBean= mAddressAdapter.getItem(position);
       LiveEventBus.get(ShopEvent.ADDRESS_CHANGE, AddressInfoBean.class).post(addressInfoBean);
       finish();
    }
}
