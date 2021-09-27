package com.wanyue.shop.view.view;

import android.util.ArrayMap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.lifecycle.ViewModelProviders;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.R2;
import com.wanyue.shop.adapter.SpecSelectAdapter;
import com.wanyue.shop.bean.GoodsParseBean;
import com.wanyue.shop.bean.SpecsBean;
import com.wanyue.common.bean.SpecsValueBean;
import com.wanyue.shop.bean.StoreGoodsBean;
import com.wanyue.shop.model.GoodDetailModel;
import com.wanyue.shop.view.widet.LinearLayoutForListView;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/*选择商品属性*/
public class SpecsSelectViewProxy extends RxViewProxy implements View.OnClickListener, SpecSelectAdapter.OnKeyChangeListnter {
    @BindView(R2.id.btn_close)
    ImageView mBtnClose;
    @BindView(R2.id.img_avator)
    RoundedImageView mImgAvator;
    @BindView(R2.id.tv_title)
    TextView mTvTitle;
    @BindView(R2.id.tv_price)
    TextView mTvPrice;

    @BindView(R2.id.reclyView)
    LinearLayoutForListView mListView;

    @BindView(R2.id.tv_stock)
    TextView mTvStock;
    @BindView(R2.id.tv_num_tip)
    TextView mTvNumTip;
    @BindView(R2.id.vp_number_container)
    ViewGroup mVpNumberContainer;

    private GoodDetailModel mGoodDetailModel;
    private GoodsNumViewProxy mGoodsNumViewProxy;
    private ArrayMap<String,SpecsValueBean>mData;
    private SpecsValueBean mSpecsValueBean;
    private StoreGoodsBean storeGoodsBean;
    @Override
    public int getLayoutId() {
        return R.layout.view_specs_select;
    }
    @Override
    protected boolean shouldBindButterKinfe() {
        return true;
    }
    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        contentView.setOnClickListener(this);
        mGoodDetailModel = ViewModelProviders.of(getActivity()).get(GoodDetailModel.class);
        mGoodsNumViewProxy=new GoodsNumViewProxy();
        mGoodsNumViewProxy.setDefaultNum(mGoodDetailModel.getSelectNum());
        mGoodsNumViewProxy.setNumberChangeListnter(new GoodsNumViewProxy.NumberChangeListnter<GoodsNumViewProxy>() {
            @Override
            public void change(GoodsNumViewProxy goodsNumViewProxy, int num) {
                if (mGoodDetailModel != null) {
                    mGoodDetailModel.setSelectNum(num);
                }
            }
        });
        getViewProxyChildMannger().addViewProxy(mVpNumberContainer,mGoodsNumViewProxy,mGoodsNumViewProxy.getDefaultTag());
        refreshData();
    }
    @OnClick(R2.id.btn_close)
    public void close(View view) {
        getViewProxyMannger().removeViewProxy(this);
    }
    /*刷新数据*/
    public void refreshData() {
        GoodsParseBean goodsParseBean = GoodDetailModel.getGoodsParse(getActivity());
        if (goodsParseBean == null) {
            return;
        }
        storeGoodsBean= goodsParseBean.getGoodsInfo();
        if (storeGoodsBean != null) {
            mTvTitle.setText(storeGoodsBean.getName());
        }
        mData=goodsParseBean.getProductRealyValue();
        mSpecsValueBean = GoodDetailModel.getSpecsValueBean(getActivity());
        putSelectSpecsValueData();
        initSpecAdapter(goodsParseBean);
        /*还原存储的选择数目*/
        int num = mGoodDetailModel.getSelectNum();
        mGoodsNumViewProxy.setDefaultNum(num);
    }

    private void initSpecAdapter(GoodsParseBean goodsParseBean) {
        String key=null;
        if(mGoodDetailModel!=null){
           key=mGoodDetailModel.getSpecsKey();
        }
        List<SpecsBean> specsBeanList = goodsParseBean.getProductAttr();
        SpecSelectAdapter adapter = new SpecSelectAdapter(specsBeanList,key, getViewProxyMannger().getLayoutInflater());
        adapter.setOnKeyChangeListnter(this);
        mListView.setAdapter(adapter);
    }

    private void putSelectSpecsValueData( ) {
        if (mSpecsValueBean != null) {
            mTvPrice.setText(StringUtil.getPrice(mSpecsValueBean.getPrice()));
            ImgLoader.display(getActivity(), mSpecsValueBean.getImage(), mImgAvator);
            if (storeGoodsBean != null) {
                mTvStock.setText(getString(R.string.stock_tip, mSpecsValueBean.getStock(), storeGoodsBean.getUnitName()));
            } else {
                mTvStock.setText(getString(R.string.stock_tip2, mSpecsValueBean.getStock()));
            }
        }else{
            if(storeGoodsBean!=null){
               mTvPrice.setText(StringUtil.getPrice(storeGoodsBean.getPrice()));
               ImgLoader.display(getActivity(), storeGoodsBean.getImage(), mImgAvator);
               mTvStock.setText(getString(R.string.stock_tip, storeGoodsBean.getStock(),storeGoodsBean.getUnitName()));
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
    @Override
    public void change(String[] selectKeyArray) {
        if(mData!=null&&selectKeyArray!=null){
           int size=mData.size();
           for(int i=0;i<size;i++){
              String key=mData.keyAt(i);
              if(containerAllSelecy(key,selectKeyArray)){
                  mSpecsValueBean=mData.get(key);
                  if(mGoodDetailModel!=null){
                     mGoodDetailModel.setSelectSpecValue(mSpecsValueBean);
                     mGoodDetailModel.setSpecsKey(key);
                  }
                  putSelectSpecsValueData();
              }
           }
        }
    }
    /*判断是否包含所有的选项,若包含则判定是选择了这个规格*/
    private boolean containerAllSelecy(String key, String[] selectKeyArray) {
        boolean isAllContain=true;
        for(String tempKeyChild:selectKeyArray){
           if(!StringUtil.contains(key,tempKeyChild)){
               isAllContain=false;
              break;
           }
        }
        return isAllContain;
    }
}
