package com.wanyue.shop.view.pop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.http.ParseSingleHttpCallback;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.ImageUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.GoodsParseBean;
import com.wanyue.shop.bean.StoreGoodsBean;
import com.wanyue.shop.model.GoodDetailModel;

public class GoodsBillPop extends BaseCenterPopView implements View.OnClickListener {

    private ViewGroup mContentView;
    private ImageView mImgThumb;
    private TextView mTvTitle;
    private TextView mTvPrice;
    private ImageView mImgSmallProgroam;
    private StoreGoodsBean mStoreGoodsBean;
    private TextView mBtnSave;

    private ImageView mBtnClose;


    public GoodsBillPop(@NonNull Context context) {
        super(context);
    }

    private void getCode() {
        if(mStoreGoodsBean!=null){
            String id=mStoreGoodsBean.getId();
            ShopAPI.getGoodsCode(id, new ParseSingleHttpCallback<String>("code") {
                @Override
                public void onSuccess(String data) {
                    ImgLoader.display(getContext(),data,mImgSmallProgroam);
                    mBtnSave.setEnabled(true);
                    mBtnSave.setAlpha(1F);
                }
            });
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_goods_pill;
    }

    @Override
    protected void init() {
        super.init();
        mContentView =findViewById(R.id.contentView);
        mImgThumb =  findViewById(R.id.img_thumb);
        mTvTitle =   findViewById(R.id.tv_title);
        mTvPrice =   findViewById(R.id.tv_price);
        mBtnSave =   findViewById(R.id.btn_save);

        mImgSmallProgroam =  findViewById(R.id.img_small_progroam);
        GoodsParseBean goodsParseBean= GoodDetailModel.getGoodsParse((FragmentActivity) getContext());
        if(goodsParseBean!=null){
           mStoreGoodsBean=goodsParseBean.getGoodsInfo();
        }

        if(mStoreGoodsBean!=null){
            mTvTitle.setText(mStoreGoodsBean.getName());
            mTvPrice.setText(StringUtil.getPrice(mStoreGoodsBean.getPrice()));
            ImgLoader.display(getContext(),mStoreGoodsBean.getImage(),mImgThumb);
        }
        mBtnClose = findViewById(R.id.btn_close);
        mBtnSave.setOnClickListener(this);
        mBtnClose.setOnClickListener(this);
        getCode();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        ShopAPI.cancle(ShopAPI.PRODUCT_CODE);
    }

    @Override
    public void onClick(View v) {
        if(!ClickUtil.canClick()){
            return;
        }
        int id=v.getId();
        if(id==R.id.btn_save){
            save();
        }else if(id==R.id.btn_close){
            dismiss();
        }
    }
    private void save() {
        Bitmap bitmap= ImageUtil.convertViewToBitmap2(mContentView);
        Activity activity= (Activity) getContext();
        ImageUtil.saveAlbum(activity,bitmap, System.currentTimeMillis()+"_pill.jpg");
        ToastUtil.show(R.string.save_succ);
        dismiss();
    }
}
