package com.wanyue.shop.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.wanyue.common.adapter.base.BaseMutiRecyclerAdapter;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.bean.GoodsBean;
import com.wanyue.common.bean.SpecsValueBean;
import com.wanyue.common.custom.CheckImageView;
import com.wanyue.common.proxy.BaseProxyMannger;
import com.wanyue.common.proxy.BaseViewProxy;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.WordUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.bean.ShopCartBean;
import com.wanyue.shop.bean.ShopCartStoreBean;
import com.wanyue.shop.model.ShopCartModel;
import com.wanyue.shop.view.activty.GoodsDetailActivity;
import com.wanyue.shop.view.view.GoodsNumViewProxy;
import com.wanyue.shop.view.view.ShopCartGoodsNumViewProxy;
import java.util.List;

public class ShopCartAdapter <T extends MultiItemEntity> extends BaseMutiRecyclerAdapter<T, BaseReclyViewHolder>{
    private BaseProxyMannger mBaseProxyMannger;
    private Context mContext;
    private ShopCartModel mShopCartModel;

    public ShopCartAdapter(List<T> data,BaseProxyMannger baseProxyMannger,Context context,ShopCartModel shopCartModel) {
        super(data);
        mContext=context;
        mBaseProxyMannger=baseProxyMannger;
        mShopCartModel=shopCartModel;

        /*过期状态的layout*/
        addItemType(ShopCartStoreBean.TYPE_INVALID, R.layout.item_recly_shop_cart_invalid_title);
        addItemType(ShopCartBean.TYPE_INVALID,R.layout.item_relcy_shop_cart_invaild_goods);

        /*正常状态的layout*/
        addItemType(ShopCartBean.TYPE_VALID,R.layout.item_relcy_shop_cart_goods);
        addItemType(ShopCartStoreBean.TYPE_VALID,R.layout.item_recly_shop_cart_store);

        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                MultiItemEntity multiItemEntity=getItem(position);
                if(multiItemEntity==null){
                    return;
                }
                int itemType=multiItemEntity.getItemType();
                if(multiItemEntity instanceof ShopCartStoreBean){
                    if(itemType==ShopCartStoreBean.TYPE_INVALID){
                       clickInVaildHead(multiItemEntity,position);
                    }else{
                       clickStore(multiItemEntity,view,position);
                    }
                }else if(multiItemEntity instanceof ShopCartBean){
                    if(itemType==ShopCartBean.TYPE_VALID){
                       clickGoods(multiItemEntity,view,position);
                    }
                }
            }
        });

        setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                MultiItemEntity multiItemEntity=getItem(position);
                if(multiItemEntity==null){
                    return;
                }
                int type=multiItemEntity.getItemType();
                if(type==ShopCartStoreBean.TYPE_VALID){
                    clickStore(multiItemEntity,view,position);
                }else if(type==ShopCartBean.TYPE_VALID){
                    clickCheckGoods(multiItemEntity,view,position);
                }else if(type==ShopCartStoreBean.TYPE_INVALID){
                    deleteAllInVidGood();
                }
            }
        });
    }


    private DeleteInvaildListner mDeleteInvaildListner;
    private void deleteAllInVidGood() {
        if(mDeleteInvaildListner!=null){
           mDeleteInvaildListner.deleteInvaild();
        }
    }


    public void setDeleteInvaildListner(DeleteInvaildListner deleteInvaildListner) {
        mDeleteInvaildListner = deleteInvaildListner;
    }

    public interface DeleteInvaildListner{
        public void deleteInvaild();
    }

    /*点击商品item*/
    private void clickGoods(MultiItemEntity multiItemEntity, View view, int position) {
        ShopCartBean shopCartBean=(ShopCartBean) multiItemEntity;
        String id=shopCartBean.getProductId();
        GoodsDetailActivity.forward(mContext,id);
    }

    /*点击切换商品选中状态*/
    private void clickCheckGoods(MultiItemEntity multiItemEntity, View view, int position) {
        ShopCartBean shopCartBean=(ShopCartBean) multiItemEntity;
        boolean isTargetCheck=!shopCartBean.isChecked();
        if(mShopCartModel!=null){
           mShopCartModel.setGoodsChecked(shopCartBean,isTargetCheck);
        }
        notifyItemChanged(position);
        ShopCartStoreBean storeBean=shopCartBean.getStore();
        if(storeBean==null){
         return;
        }
       int index=mData.indexOf(storeBean);
       if(index!=-1){
          notifyItemChanged(index);
       }
    }

    /*点击店铺*/
    private void clickStore(MultiItemEntity multiItemEntity, View view, int position) {
        ShopCartStoreBean storeBean= (ShopCartStoreBean) multiItemEntity;
        boolean isTargetCheck=!storeBean.isChecked();
        List<ShopCartBean>list=storeBean.getList();
        int size=ListUtil.getSize(list);
        if(size<=0){
            if(storeBean.isChecked()!=false){
                storeBean.setChecked(false);
                notifyItemChanged(position);
            }
            return;
        }
        if(mShopCartModel!=null){
           mShopCartModel.setStoreChecked(storeBean,isTargetCheck);
        }
        notifyItemRangeChanged(position,size+1);
    }

    /*点击失效商品进行开关*/
    private void clickInVaildHead(MultiItemEntity multiItemEntity, int position) {
        ShopCartStoreBean storeBean= (ShopCartStoreBean) multiItemEntity;
        if (storeBean.isExpanded()) {
            collapse(position);
        } else {
            expand(position);
        }
    }

    @Override
    protected void convert(@NonNull BaseReclyViewHolder helper, T item) {
        switch (item.getItemType()){
            case ShopCartStoreBean.TYPE_INVALID:
                convertStoreInvalid(helper,item);
                break;
            case ShopCartStoreBean.TYPE_VALID:
                convertStoreValid(helper,item);
                break;
            case ShopCartBean.TYPE_INVALID:
                convertGoodsInvalid(helper,item);
                break;
            case ShopCartBean.TYPE_VALID:
                convertGoodsValid(helper,item);
                break;
            default:
                break;
        }
    }


    /*正常商品的店铺*/
    private void convertStoreValid(BaseReclyViewHolder helper, T item) {
        ShopCartStoreBean shopCartStoreBean= (ShopCartStoreBean) item;
        helper.setText(R.id.tv_name,shopCartStoreBean.getName());
        CheckImageView checkImageView=helper.getView(R.id.check_image);
        checkImageView.setChecked(shopCartStoreBean.isChecked());
    }

    /*正常商品展示*/
    private void convertGoodsValid(BaseReclyViewHolder helper, T item) {
        ShopCartBean shopCartBean= (ShopCartBean) item;
        GoodsBean goodsBean=shopCartBean.getProductInfo();
        View container=helper.getView(R.id.container);
        if(goodsBean!=null){
            helper.setText(R.id.tv_title,goodsBean.getName());
            helper.setText(R.id.tv_price,goodsBean.getUnitPrice());
            helper.setImageUrl(goodsBean.getThumb(),R.id.img_thumb);
            SpecsValueBean specsValueBean=goodsBean.getAttrInfo();
            if(specsValueBean!=null){
              helper.setText(R.id.tv_field, WordUtil.getString(R.string.goods_field_tip,specsValueBean.getSuk()));
            }
        }

        CheckImageView checkImageView=helper.getView(R.id.check_image);
        checkImageView.setChecked(shopCartBean.isChecked());
        ViewGroup nameContainer=helper.getView(R.id.vp_number_container);
        helper.addOnClickListener(R.id.check_image);
        if(mBaseProxyMannger==null||container==null){
            return;
        }

        ShopCartGoodsNumViewProxy goodsNumViewProxy=null;
        String tag=Integer.toString(container.hashCode());
        BaseViewProxy baseViewProxy=mBaseProxyMannger.getViewProxyByTag(tag);
        if(baseViewProxy!=null){
            goodsNumViewProxy= (ShopCartGoodsNumViewProxy)baseViewProxy ;
        }else{
            goodsNumViewProxy=new ShopCartGoodsNumViewProxy();
            goodsNumViewProxy.setEableDelay(true);
            goodsNumViewProxy.setNumberChangeListnter(mNumberChangeListnter);
            mBaseProxyMannger.addViewProxy(nameContainer,goodsNumViewProxy,tag);
        }
           goodsNumViewProxy.setShopCartBean(shopCartBean);
    }

    /*过期商品展示*/
    private void convertGoodsInvalid(BaseReclyViewHolder helper, T item) {
        ShopCartBean shopCartBean= (ShopCartBean) item;
        GoodsBean goodsBean=shopCartBean.getProductInfo();
        if(goodsBean!=null){
            helper.setText(R.id.tv_title,goodsBean.getName());
            //helper.setText(R.id.tv_price,goodsBean.getUnitPrice());
            helper.setImageUrl(goodsBean.getThumb(),R.id.img_thumb);
        }

    }

    GoodsNumViewProxy.NumberChangeListnter<ShopCartGoodsNumViewProxy> mNumberChangeListnter= new GoodsNumViewProxy.NumberChangeListnter<ShopCartGoodsNumViewProxy> () {
        @Override
        public void change(ShopCartGoodsNumViewProxy goodsNumViewProxy, int num) {
           if(mShopCartModel!=null&&goodsNumViewProxy.haveData()){
              ShopCartBean shopCartBean=goodsNumViewProxy.getShopCartBean();
              mShopCartModel.modifyCartNum(shopCartBean,num);
           }
        }
    };
    /*过期商品统统归类为过期商品店铺*/
    private void convertStoreInvalid(BaseReclyViewHolder helper, T item) {
        final ShopCartStoreBean level0Bean= (ShopCartStoreBean) item;
        View arrowView=helper.getView(R.id.img_arrow);
        if(level0Bean.isExpanded()){
            arrowView.setRotation(180);
        }else{
            arrowView.setRotation(0);
        }
        helper.addOnClickListener(R.id.btn_delete);
    }


}
