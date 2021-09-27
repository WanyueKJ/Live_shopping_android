package com.wanyue.live.dialog;

import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.Constants;
import com.wanyue.common.adapter.RefreshAdapter;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.custom.CommonRefreshView;
import com.wanyue.common.dialog.AbsDialogFragment;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.interfaces.OnItemClickListener;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.live.R;
import com.wanyue.live.activity.LiveAudienceActivity;
import com.wanyue.live.adapter.LiveGoodsAdapter;
import com.wanyue.live.http.LiveHttpConsts;
import com.wanyue.live.http.LiveHttpUtil;
import com.wanyue.live.http.LiveShopAPI;
import com.wanyue.live.model.LiveModel;
import com.wanyue.shop.view.activty.GoodsDetailActivity;
import com.wanyue.shop.view.view.GoodsHandleNoCartViewProxy;
import java.util.Arrays;
import java.util.List;

/**
 * Created by  on 2019/8/29.
 */

public class LiveGoodsDialogFragment extends AbsDialogFragment implements OnItemClickListener<GoodsBean> {

    private CommonRefreshView mRefreshView;
    private LiveGoodsAdapter mAdapter;
    private TextView mTitle;
    private String mLiveUid;

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_live_goods;
    }

    @Override
    protected int getDialogStyle() {
        return R.style.dialog2;
    }

    @Override
    protected boolean canCancel() {
        return true;
    }

    @Override
    protected void setWindowAttributes(Window window) {
        window.setWindowAnimations(R.style.bottomToTopAnim);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = DpUtil.dp2px(320);
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mLiveUid = bundle.getString(Constants.LIVE_UID);
        }
        mTitle =  findViewById(R.id.title);
        mRefreshView = (CommonRefreshView) findViewById(R.id.refreshView);
        mRefreshView.setEmptyLayoutId(R.layout.view_no_data_goods);
        mRefreshView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRefreshView.setDataHelper(new CommonRefreshView.DataHelper<GoodsBean>() {
            @Override
            public RefreshAdapter<GoodsBean> getAdapter() {
                if (mAdapter == null) {
                    mAdapter = new LiveGoodsAdapter(mContext);
                    mAdapter.setOnItemClickListener(LiveGoodsDialogFragment.this);
                }
                return mAdapter;
            }
            @Override
            public void loadData(int p, HttpCallback callback) {
                LiveShopAPI.getSale(p,mLiveUid, callback);
            }
            @Override
            public List<GoodsBean> processData(String[] info) {
                List<GoodsBean>goodsBeanList= JSON.parseArray(Arrays.toString(info),GoodsBean.class);
                return goodsBeanList;
            }
            @Override
            public void onRefreshSuccess(List<GoodsBean> list, int listCount) {
            }
            @Override
            public void onRefreshFailure() {

            }
            @Override
            public void onLoadMoreSuccess(List<GoodsBean> loadItemList, int loadItemCount) {
            }

            @Override
            public void onLoadMoreFailure() {

            }
        });
        mRefreshView.initData();
        getSaleNums();
    }
    private void getSaleNums() {
        LiveShopAPI.getShopSaleNum(LiveModel.getContextLiveUid(getActivity()), new ParseHttpCallback<JSONObject>() {
            @Override
            public void onSuccess(int code, String msg, JSONObject info) {
                if(isSuccess(code)){
                  String nums=  info.getString("nums");
                    if (mTitle != null) {
                        mTitle.setText(WordUtil.getString(R.string.goods_tip_39 ,nums));
                    }
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        mContext = null;
        LiveHttpUtil.cancel(LiveHttpConsts.GET_SALE);
        LiveHttpUtil.cancel(LiveHttpConsts.SHOP_SALE_NUMS);
        super.onDestroy();
    }

    @Override
    public void onItemClick(GoodsBean bean, int position) {
        if(mContext !=null&&mContext instanceof LiveAudienceActivity){
            ((LiveAudienceActivity)mContext).openWindowTag();
        }
        GoodsDetailActivity.forward(getContext(),bean.getId(), mLiveUid,GoodsHandleNoCartViewProxy.class);

    }
}
