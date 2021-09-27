package com.wanyue.shop.view.pop;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import com.lxj.xpopup.XPopup;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.business.HtmlHelper;
import com.wanyue.common.mob.MobCallback;
import com.wanyue.common.mob.MobShareUtil;
import com.wanyue.common.utils.ClickUtil;
import com.wanyue.common.utils.L;
import com.wanyue.shop.R;
import com.wanyue.shop.bean.GoodsParseBean;
import com.wanyue.shop.bean.StoreGoodsBean;
import com.wanyue.shop.model.GoodDetailModel;

public class ShareGoodsPop extends BaseBottomPopView implements View.OnClickListener {
    private TextView mBtnShareWechat;
    private TextView mBtnPill;
    private MobShareUtil mMobShareUtil;
    public ShareGoodsPop(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        mBtnShareWechat = findViewById(R.id.btn_share_wechat);
        mBtnPill = findViewById(R.id.btn_pill);
        mBtnShareWechat.setOnClickListener(this);
        mBtnPill.setOnClickListener(this);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_share_goods;
    }

    @Override
    public void onClick(View v) {
        if(!ClickUtil.canClick()){
            return;
        }
        int id=v.getId();
        if(id==R.id.btn_share_wechat){
            shareToWechat();
        }else if(id==R.id.btn_pill){
            openPillWindow();

        }
    }


    /*打开海报*/
    private void openPillWindow() {
        new XPopup.Builder(getContext())
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .asCustom(new GoodsBillPop(getContext()))
                .show();
        dismiss();
    }
    private void shareToWechat() {
        if(mMobShareUtil==null){
           mMobShareUtil =new MobShareUtil();
        }
        GoodsParseBean goodsParseBean=GoodDetailModel.getGoodsParse((FragmentActivity) getContext());
        if(goodsParseBean!=null&&goodsParseBean.getGoodsInfo()!=null){
           StoreGoodsBean storeGoodsBean=goodsParseBean.getGoodsInfo();
            String path= HtmlHelper.newHtmlHelper().appendUrl("/pages/goods_details/index?")
                    .appendParm("id",storeGoodsBean.getId(),true)
                    .appendParm("spid", CommonAppConfig.getUserBean().getId(),false).create();
            L.e("path=="+path);
           mMobShareUtil.shareSmallProgram(storeGoodsBean.getName(), storeGoodsBean.getImage(), path, new MobCallback() {
              @Override
              public void onSuccess(Object data) {
              }
              @Override
              public void onError() {
              }
              @Override
              public void onCancel() {

              }
              @Override
              public void onFinish() {
              }
          });
          dismiss();
        }

    }
}
