package com.wanyue.shop.view.view;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.lxj.xpopup.XPopup;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoHelper;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.to.aboomy.pager2banner.Banner;
import com.to.aboomy.pager2banner.IndicatorView;
import com.wanyue.common.adapter.base.BaseMutiRecyclerAdapter;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.bean.GoodsBannerBean;
import com.wanyue.shop.bean.GoodsParseBean;
import com.wanyue.shop.bean.StoreGoodsBean;
import com.wanyue.shop.model.GoodDetailModel;
import com.wanyue.shop.video.GSYVideoPlayer;
import com.wanyue.shop.view.activty.GoodsDetailActivity;
import com.wanyue.shop.view.pop.ShareGoodsPop;
import java.util.ArrayList;
import java.util.List;

public class GoodsPannelViewProxy extends BaseGoodItemViewProxy implements View.OnClickListener {
    private Banner mBanner;
    private TextView mTvPrice;
    private ImageView mBtnShare;
    private TextView mTvTitle;
    private TextView mTvOtPrice;
    private TextView mTvStock;
    private TextView mTvSaleNum;
    private TextView mTvIntegral;
    private TextView mTvSpecsTip;
    private TextView mTvSpecs;
    private ViewGroup mBtnSpecs;

    private ViewGroup mVpIntegral;
    private ViewGroup mBtnCoupon;
    private StoreGoodsBean mStoreGoodsBean;
    private ImageAdapter mImageAdapter;

    private GoodDetailModel  mGoodDetailModel;

    @Override
    public int getLayoutId() {
        return R.layout.view_goods_pannel;
    }

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mBanner =  findViewById(R.id.banner);
        mTvPrice =  findViewById(R.id.tv_price);
        mBtnShare =  findViewById(R.id.btn_share);
        mTvTitle =  findViewById(R.id.tv_title);
        mTvOtPrice =  findViewById(R.id.tv_ot_price);
        mTvStock = findViewById(R.id.tv_stock);
        mTvSaleNum =  findViewById(R.id.tv_sale_num);
        mVpIntegral =  findViewById(R.id.vp_integral);
        mBtnCoupon =  findViewById(R.id.btn_coupon);
        mTvIntegral = findViewById(R.id.tv_integral);
        mTvSpecsTip =  findViewById(R.id.tv_specs_tip);
        mTvSpecs = findViewById(R.id.tv_specs);
        mBtnSpecs = findViewById(R.id.btn_specs);
        mBtnSpecs.setOnClickListener(this);
        mBtnShare.setOnClickListener(this);
        mBtnCoupon.setOnClickListener(this);
        initBanner();
        mGoodDetailModel= ViewModelProviders.of(getActivity()).get(GoodDetailModel.class);
        if(mGoodDetailModel!=null){
           mGoodDetailModel.getSpecsKeyLiveData().observe(getActivity(), new Observer<String>() {
            @Override
            public void onChanged(String key) {
                if(mTvSpecs!=null){
                 mTvSpecs.setText(key);
                 }
              }
            });
        }
    }

    private void initBanner() {
        mImageAdapter=new ImageAdapter(null);
        mBanner.setAutoPlay(false).setIndicator(defaultIndicator())
        .setAdapter(mImageAdapter);
    }

    private IndicatorView defaultIndicator() {
        IndicatorView indicator = new IndicatorView(getActivity())
                .setIndicatorColor(ResourceUtil.getColor(getActivity(),R.color.gray1))
                .setIndicatorSelectorColor(ResourceUtil.getColor(getActivity(),R.color.global))
                .setIndicatorRatio(1F) //ratio，默认值是1 ，也就是说默认是圆点，根据这个值，值越大，拉伸越长，就成了矩形，小于1，就变扁了呗
                .setIndicatorRadius(3F) // radius 点的大小
                .setIndicatorSelectedRatio(1F)
                .setIndicatorSelectedRadius(3F)
                .setIndicatorStyle(IndicatorView.IndicatorStyle.INDICATOR_BIG_CIRCLE);
        return indicator;
    }

    public void setData(GoodsParseBean goodsParseBean){
        if(!isInit()||goodsParseBean==null){
            return;
        }
        mStoreGoodsBean=goodsParseBean.getGoodsInfo();
        mTvPrice.setText(StringUtil.getPrice(mStoreGoodsBean.getPrice()));
        mTvOtPrice.setText(getString(R.string.original_tip,mStoreGoodsBean.getOriginalPrice()));
        mTvTitle.setText(mStoreGoodsBean.getName());
        String uint=mStoreGoodsBean.getUnitName();
        mTvStock.setText(getString(R.string.stock_tip,mStoreGoodsBean.getStock(),uint));
        mTvSaleNum.setText(getString(R.string.sale_num_tip,mStoreGoodsBean.getSales(),uint));

        String integral=mStoreGoodsBean.getIntegral();
        try {
           double integralDouble=Double.parseDouble(integral);
            if(integralDouble==0){
                mVpIntegral.setVisibility(View.GONE);
            }else{
              mTvIntegral.setText(getString(R.string.integral_tip_1,integral));
            }
        }catch (Exception e){
            e.printStackTrace();
            mVpIntegral.setVisibility(View.GONE);
        }
        if(ListUtil.haveData(goodsParseBean.getProductAttr())){
            String key=GoodDetailModel.getSpecsKeyByContext(getActivity());
            mTvSpecs.setText(key);
        }else{
            mBtnSpecs.setVisibility(View.GONE);
        }

        List<GoodsBannerBean>bannerList=new ArrayList<>();
        List<String>thumbList=mStoreGoodsBean.getSlideImage();
        if(ListUtil.haveData(thumbList)){
            for(String thumb:thumbList){
                GoodsBannerBean bannerBean=new GoodsBannerBean();
                bannerBean.thumb=thumb;
                bannerList.add(bannerBean);
            }
        }
        String videoLink= mStoreGoodsBean.getVideoLink();
        if(!TextUtils.isEmpty(videoLink)){
            GoodsBannerBean bannerBean=new GoodsBannerBean();
            bannerBean.url=videoLink;
            bannerBean.thumb=mStoreGoodsBean.getImage();
            bannerBean.isVideo=true;
            bannerList.add(0,bannerBean);
        }

        if(mImageAdapter!=null){
           mImageAdapter.setData(bannerList);
        }
    }

    @Override
    public void onClick(View v) {
        if(!ClickUtil.canClick()){
            return;
        }
        int id=v.getId();
        if(id==R.id.btn_specs){
            openSelectSpecsSelectDialog();
        }else if(id==R.id.btn_share){
            share();
        }
    }
    /*分享*/
    private void share() {
        new XPopup.Builder(getActivity())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCustom(new ShareGoodsPop(getActivity()))
                .show();
    }

    private void openSelectSpecsSelectDialog() {
        FragmentActivity fragmentActivity=getActivity();
        if(fragmentActivity==null){
            return;
        }
        GoodsDetailActivity activity= (GoodsDetailActivity) fragmentActivity;
        activity.showSpecsSelectWindow();
        if(activity==null){
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mImageAdapter!=null){
           mImageAdapter.release();
        }
    }

    public class ImageAdapter extends BaseMutiRecyclerAdapter<GoodsBannerBean, BaseReclyViewHolder> {
        private GSYVideoHelper.GSYVideoHelperBuilder mVideoHelperBuilder;
        private GSYVideoHelper mSmallVideoHelper;

        public ImageAdapter(List<GoodsBannerBean> data) {
            super(data);
            addItemType(GoodsBannerBean.IS_PHOTO,R.layout.item_goods_banner);
            addItemType(GoodsBannerBean.IS_VIDEO,R.layout.item_goods_banner_video);
        }
        @Override
        protected void convert(BaseReclyViewHolder helper, GoodsBannerBean item) {
            switch (helper.getItemViewType()){
                case GoodsBannerBean.IS_PHOTO:
                    convertPhoto(helper,item);
                    break;
                case GoodsBannerBean.IS_VIDEO:
                    convertVideo(helper,item);
                    break;
            }
        }

        private void convertVideo(BaseReclyViewHolder helper,final  GoodsBannerBean item) {
            final int position=helper.getLayoutPosition();
            //helper.setImageUrl(item.thumb,R.id.cover);
            ViewGroup viewGroup= helper.getView(R.id.container);
            initPlayerView(viewGroup,item.url);
            ImageView imageView=new ImageView(getActivity());
            ImgLoader.display(getActivity(),item.thumb,imageView);
            View button=helper.getView(R.id.list_item_btn);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mSmallVideoHelper.setPlayPositionAndTag(position, TAG);
                    mVideoHelperBuilder.setUrl(item.url);
                    mSmallVideoHelper.startPlay();
                    notifyItemChanged(position);
                }
            });
            mSmallVideoHelper.addVideoPlayer(position, imageView, TAG, (ViewGroup) helper.getView(R.id.video_container), button );
        }

        private void initPlayerView(ViewGroup viewGroup,String url) {
           if(mVideoHelperBuilder==null){
               mVideoHelperBuilder = new GSYVideoHelper.GSYVideoHelperBuilder();
               mVideoHelperBuilder
                       .setHideStatusBar(true)
                       .setNeedLockFull(true)
                       .setCacheWithPlay(true)
                       .setShowFullAnimation(false)
                       .setRotateViewAuto(false)
                       .setLockLand(true);

               GSYVideoPlayer gsyVideoPlayer= new GSYVideoPlayer(getActivity());
               mSmallVideoHelper = new GSYVideoHelper(getActivity(),gsyVideoPlayer);
               mSmallVideoHelper.setGsyVideoOptionBuilder(mVideoHelperBuilder);
           }
        }

        public void  release(){
            if(mSmallVideoHelper!=null){
               mSmallVideoHelper.releaseVideoPlayer();
               GSYVideoManager.releaseAllVideos();
            }
        }
        private void convertPhoto(BaseReclyViewHolder helper, GoodsBannerBean item) {
            helper.setImageUrl(item.thumb,R.id.img);
        }
    }
}
