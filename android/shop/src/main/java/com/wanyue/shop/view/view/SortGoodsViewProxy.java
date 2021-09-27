package com.wanyue.shop.view.view;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.bean.GoodsSearchArgs;
import com.wanyue.shop.view.widet.ThreeCheckImageView;

public class SortGoodsViewProxy extends RxViewProxy implements View.OnClickListener,CompoundButton.OnCheckedChangeListener {
    private static final String UP_SORT="asc";//升序
    private static final String DOWN_SORT="desc";//降序

    private TextView mTvGoodsClass;
    private ViewGroup mBtnPrice;
    private ViewGroup mBtnSale;
    private ThreeCheckImageView mImgSale;
    private CheckBox mCheckNewProduct;
    private ThreeCheckImageView mImgPrice;
    private TextView mTvPrice;
    private TextView mTvSale;

    private int mTextDefaultColor;
    private int mTextSelectColor;

    private GoodsSearchArgs mArgs;
    private ParameterChangeListner mParameterChangeListner;

    @Override
    public int getLayoutId() {
        return R.layout.view_sort_goods;
    }

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mTvPrice =  findViewById(R.id.tv_price);
        mTvSale = findViewById(R.id.tv_sale);
        mTvGoodsClass =  findViewById(R.id.tv_goods_class);
        mBtnPrice =  findViewById(R.id.btn_price);
        mBtnSale =  findViewById(R.id.btn_sale);
        mImgSale =  findViewById(R.id.img_sale);
        mCheckNewProduct =  findViewById(R.id.check_new_product);
        mImgPrice = (ThreeCheckImageView) findViewById(R.id.img_price);
        if(mArgs!=null){
           String className= mArgs.className;
           if(!TextUtils.isEmpty(className)){
               mTvGoodsClass.setText(className);
               mTvGoodsClass.setTextColor(ResourceUtil.getColor(getActivity(),R.color.global));
           }
        }
        mBtnPrice.setOnClickListener(this);
        mBtnSale.setOnClickListener(this);
        mTextDefaultColor= ResourceUtil.getColor(getActivity(),R.color.textColor2);
        mTextSelectColor= ResourceUtil.getColor(getActivity(),R.color.textColor);


        mCheckNewProduct.setOnCheckedChangeListener(this);
        mImgSale.setOnCheckChangeClickListner(mOnCheckChangeClickListner);
        mImgPrice.setOnCheckChangeClickListner(mOnCheckChangeClickListner);
    }


    private ThreeCheckImageView.OnCheckChangeClickListner mOnCheckChangeClickListner=new ThreeCheckImageView.OnCheckChangeClickListner() {
        @Override
        public void onCheckChange(View view, int state) {
            int id=view.getId();
            if(id==R.id.img_sale){
              setTextViewColorByState(mTvSale,state);
              setSortSale(state);
            }else if(id==R.id.img_price){
              setTextViewColorByState(mTvPrice,state);
              setSortPrice(state);
            }
        }
    };

    private void setSortPrice(int state) {
        if(mArgs==null){
            return;
        }
        if(state==ThreeCheckImageView.UN_CHECKED){
            mArgs.priceCondition=null;
        }else if(state==ThreeCheckImageView.INDETERMINATE_CHECKED){
            mArgs.priceCondition=UP_SORT;
            clearThreeCheckImageViewSelect(mImgSale);
        }else if(state==ThreeCheckImageView.CHECKED){
            mArgs.priceCondition=DOWN_SORT;
            clearThreeCheckImageViewSelect(mImgSale);
        }
    }

    /*当选中一个的时候，清空其他条件的选中状态*/
    private void clearThreeCheckImageViewSelect(ThreeCheckImageView checkImageView) {
        if(checkImageView!=null){
           checkImageView.setChecked(ThreeCheckImageView.UN_CHECKED);
        }
    }

    private void setSortSale(int state) {
        if(mArgs==null){
            return;
        }
        if(state==ThreeCheckImageView.UN_CHECKED){
            mArgs.saleCondition=null;
        }else if(state==ThreeCheckImageView.INDETERMINATE_CHECKED){
            mArgs.saleCondition=UP_SORT;
            clearThreeCheckImageViewSelect(mImgPrice);
        }else if(state==ThreeCheckImageView.CHECKED){
            mArgs.saleCondition=DOWN_SORT;
            clearThreeCheckImageViewSelect(mImgPrice);
        }
    }


    private void setTextViewColorByState(TextView view, int state) {
            if(state==ThreeCheckImageView.UN_CHECKED){
                view.setTextColor(mTextDefaultColor);
            }else{
                view.setTextColor(mTextSelectColor);
            }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.btn_sale){
            if(mImgSale!=null){
               mImgSale.change();
                if(mParameterChangeListner!=null){
                   mParameterChangeListner.argsChange(mArgs);
                }
            }
        }else if(id==R.id.btn_price){
            if(mImgPrice!=null){
               mImgPrice.change();
               if(mParameterChangeListner!=null){
                    mParameterChangeListner.argsChange(mArgs);
                }
            }
        }
    }

    public void setArgs(GoodsSearchArgs args) {
        mArgs = args;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(mArgs==null){
            return;
        }
            mArgs.isNew=isChecked?1:0;
         if(mParameterChangeListner!=null){
            mParameterChangeListner.argsChange(mArgs);
         }
    }

    public void setParameterChangeListner(ParameterChangeListner parameterChangeListner) {
        mParameterChangeListner = parameterChangeListner;
    }


   public  static interface ParameterChangeListner{
     public void argsChange(GoodsSearchArgs args);
   }
}
