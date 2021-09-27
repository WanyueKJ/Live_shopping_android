package com.wanyue.shop.model;


import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.wanyue.common.http.ParseSingleHttpCallback;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.utils.DebugUtil;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.SpUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.bean.ShopCartBean;
import com.wanyue.shop.bean.ShopCartStoreBean;
import com.wanyue.shop.bean.ShopcartParseBean;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ShopCartModel extends ViewModel {
    private static final String EVENT_SHOP_CART="shop_cart";
    private static final String EVENT_NUM_BACK="num_back";


    public static final int NOTIFY_ALL=-1; //更新全部
    public static final int NOTIFY_DEFAULT=-2; //还原到最初的状态,刷新接口并重新切换到正常模式

    public static final int NOTIFY_LOCK_SCROLL=-3;
    public static final int NOTIFY_UNLOCK_SCROLL=-4;
    public static final int NOTIFY_ONTHER=0;

    private double mTotalPrice;
    private boolean mIsHaveChecked;

    private MutableLiveData<Boolean>mCheckLiveData; //是否有可选择的内容,暂时判断是否可以进行下单或者删除
    private MutableLiveData<String> mPriceData; //监听价格变化
    private MutableLiveData<Integer>mNotifyItemChangeLiveData; //是否需要刷新数据;

    public static int shopCartNum=-1; //全局的购物车数量
    private boolean mIsEditMode; //是否是编辑模式,默认是false
    private List<ShopCartStoreBean> mShopCartData; //数据


    public MutableLiveData<String> getPriceData() {
        if(mPriceData==null){
           mPriceData=new MutableLiveData<>();
        }
        return mPriceData;
    }


    private void watchHaveChecked(boolean isHaveChecked) {
        if(mIsHaveChecked==isHaveChecked){
            return;
        }
        mIsHaveChecked=isHaveChecked;
        if(mCheckLiveData!=null){
           mCheckLiveData .setValue(mIsHaveChecked);
        }
    }

    public MutableLiveData<Boolean> getCheckLiveData() {
        if(mCheckLiveData==null){
           mCheckLiveData=new MutableLiveData<>();
        }
        return mCheckLiveData;
    }

    public MutableLiveData<Integer> getNotifyItemChangeLiveData() {
        if(mNotifyItemChangeLiveData==null){
            mNotifyItemChangeLiveData=new MutableLiveData<>();
        }
        return mNotifyItemChangeLiveData;
    }

    public static int getShopCartNum() {
        return shopCartNum;
    }

    /*请求接口刷新购物车数量*/
    public  static void  requestShopcartCount(){
        if(shopCartNum==-1){
           shopCartNum=0;
           loadShopCartCount();
        }
        ShopAPI.getShopCartCount(new ParseSingleHttpCallback<Integer>("count") {
            @Override
            public void onSuccess(Integer data) {
                if(shopCartNum==data){
                    return;
                }
                shopCartNum=data;
                SpUtil.getInstance().setIntegerValue("cart_num",shopCartNum);
                LiveEventBus.get(EVENT_SHOP_CART,Integer.class).post(shopCartNum);
            }
        });
    }
    public  static void  loadShopCartCount(){
      Integer integer= SpUtil.getInstance().getIntValue("cart_num",shopCartNum);
      if(integer!=null||integer!=shopCartNum){
         shopCartNum= integer;
          LiveEventBus.get(EVENT_SHOP_CART,Integer.class).post(shopCartNum);
      }
    }

    private void notifyPostion(int position) {
        if(mNotifyItemChangeLiveData!=null){
          mNotifyItemChangeLiveData.setValue(position);
        }
    }

    /*删除购物车*/
    public void  deleteGoodsArray(String[] allSelectId,@NotNull LifecycleProvider<?> lifecycleOwner){

        ShopAPI.delCart(allSelectId).compose(lifecycleOwner.<Boolean>bindToLifecycle())
            .subscribe(new DefaultObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                if(aBoolean){
                   ToastUtil.show(R.string.delete_tip);
                    notifyPostion(NOTIFY_DEFAULT);
                    requestShopcartCount();
                }
            }
        });
    }

    /*根据业务需要将服务端返回的普通店铺,商品，混合过期商品转化为一个列表显示*/
    public static List<MultiItemEntity>transFormListData(ShopcartParseBean shopcartParseBean){
        List<MultiItemEntity>list=new ArrayList<>();
        if(shopcartParseBean==null){
            return list;
        }
        List<ShopCartStoreBean>data=shopcartParseBean.getValid();
        for(ShopCartStoreBean shopCartStoreBean:data){
            list.add(shopCartStoreBean);
            shopCartStoreBean.format();
        }
        List<ShopCartBean>invalidList=shopcartParseBean.getInvalid();
        if(!ListUtil.haveData(invalidList)){ //没有失效商品的时候不展示失效商品了
           return list;
        }
        for(ShopCartBean shopCartBean:invalidList){
           shopCartBean.setInvalid(true);
        }
        ShopCartStoreBean storeBean=new ShopCartStoreBean();
        storeBean.setList(invalidList);
        storeBean.setInvalid(true);
        list.add(storeBean);
        storeBean.format();
         return list;
    }


    //是否全部选中
    public boolean isAllSelect(){
        if(!ListUtil.haveData(mShopCartData)){
            return false;
        }
        boolean isAllSelect=true;
        for(ShopCartStoreBean temp:mShopCartData){
            if(!temp.isChecked()){
                return false;
            }
        }
        return  isAllSelect;
    }


    /*生成购物车选择的拼接购物车id*/
    public String[] getAllSelectCartId(){
        if(!ListUtil.haveData(mShopCartData)){
            return null;
        }
        List<String>array=new ArrayList<>();
        for(ShopCartStoreBean shopCartStoreBean:mShopCartData){
            List<ShopCartBean>shopCartBeanList=shopCartStoreBean.getList();
            if(!ListUtil.haveData(shopCartBeanList)){
                continue;
            }
            for(ShopCartBean shopCartBean:shopCartBeanList){
                if(!shopCartBean.isChecked()){
                    continue;
                }
                array.add(shopCartBean.getId());
            }
        }
        String[] stringArray = new String[array.size()];
        return array.toArray(stringArray);
    }


    /*生成购物车选择的拼接商品id*/
    public String[] getAllSelectGoodsId(){
        if(!ListUtil.haveData(mShopCartData)){
            return null;
        }
        List<String>array=new ArrayList<>();
        for(ShopCartStoreBean shopCartStoreBean:mShopCartData){
            List<ShopCartBean>shopCartBeanList=shopCartStoreBean.getList();
            if(!ListUtil.haveData(shopCartBeanList)){
                continue;
            }
            for(ShopCartBean shopCartBean:shopCartBeanList){
                if(!shopCartBean.isChecked()){
                    continue;
                }
                array.add(shopCartBean.getProductId());
            }
        }
        String[] stringArray = new String[array.size()];
        return array.toArray(stringArray);
    }

    /*计算所有总价格*/
    public void measureTotalPrice(){
        if(!ListUtil.haveData(mShopCartData)){
            return;
        }
        double price=0.00;
        boolean isHaveChecked=false;

        for(ShopCartStoreBean storeBean:mShopCartData){
           List<ShopCartBean>shopCartBeanList=storeBean.getList();
           if(!ListUtil.haveData(shopCartBeanList)){
               continue;
           }
           for(ShopCartBean shopCartBean:shopCartBeanList){
               if(!shopCartBean.isChecked()){
                   continue;
               }
               double tempPrice=shopCartBean.getProductPrice();
               price=price+(tempPrice*shopCartBean.getCartNum());
               isHaveChecked=true;
           }
        }
        watchHaveChecked(isHaveChecked);
        changeTotalPrice(price);
    }

    /*监听总价格变化*/
    public void changeTotalPrice(double price){
        if(mTotalPrice==price){
            return;
        }
        mTotalPrice=price;
        if(mPriceData!=null){
           mPriceData.setValue(StringUtil.getFormatPrice(mTotalPrice));
        }
    }

    /*修改购物车数量*/
    public void modifyCartNum(final ShopCartBean shopCartBean,final int num){
        if(shopCartBean.getCartNum()==num){
            return;
        }
        notifyPostion(NOTIFY_LOCK_SCROLL);
        ShopAPI.modifyCartNum(shopCartBean.getId(),num).subscribe(new DefaultObserver<Boolean>() {
            @Override
            public void onNext(Boolean aBoolean) {
                if(aBoolean){ //成功修改购物车数量
                    if(shopCartBean!=null){
                       shopCartBean.setCartNum(num);
                       if(shopCartBean.isChecked()){ //是选中状态下计算价格
                          measureTotalPrice();
                       }
                       requestShopcartCount();
                    }
                }else{ //失败即时回退到原来的地方
                    LiveEventBus.get(EVENT_NUM_BACK,ShopCartBean.class).post(shopCartBean);
                }
                notifyPostion(NOTIFY_UNLOCK_SCROLL);
            }
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                notifyPostion(NOTIFY_UNLOCK_SCROLL);
            }
        });
    }

    /*设置进去购物车数据*/
    public void setShopCartData(List<ShopCartStoreBean> shopCartData) {
        mShopCartData = shopCartData;
        measureTotalPrice();
    }
    public boolean isEditMode() {
        return mIsEditMode;
    }

    public void setEditMode(boolean editMode) {
        mIsEditMode = editMode;
    }

    public void addShopCartNum(int goodsNum){
        shopCartNum=shopCartNum+goodsNum;
        LiveEventBus.get(EVENT_SHOP_CART,Integer.class).post(shopCartNum);
    }

    /*监听购物车数据变化*/
    public  static void obserShopCartNum(LifecycleOwner owner, Observer<Integer> observer){
        LiveEventBus.get(EVENT_SHOP_CART,Integer.class).observe(owner,observer);
    }

    public  static void obserShopCartNum2(LifecycleOwner owner, Observer<Integer> observer){
        LiveEventBus.get(EVENT_SHOP_CART,Integer.class).observeSticky(owner,observer);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        ShopAPI.cancle(ShopAPI.CART_COUNT);
        mNotifyItemChangeLiveData=null;
        mShopCartData=null;
        mPriceData=null;
        mTotalPrice=0;
    }

    /*设置全选全不选*/
    public void setAllSelected(boolean isTargetCheck) {
        if(!ListUtil.haveData(mShopCartData)){
            return;
        }
        setLocalSelectData(isTargetCheck,NOTIFY_ALL);
    }

    //设置本地购物车数据是否选中
    private void setLocalSelectData(boolean isSelected, int position) {
        if(position==NOTIFY_ALL){
            for(ShopCartStoreBean storeBean:mShopCartData){
                storeBean.setChecked(isSelected);
                List<ShopCartBean>shopCartStoreBeanList=storeBean.getList();
                if(!ListUtil.haveData(shopCartStoreBeanList)){
                    return;
                }
                for(ShopCartBean shopCartBean:shopCartStoreBeanList){
                    shopCartBean.setChecked(isSelected);
                }
            }
        }
        notifyPostion(position);
    }

    public static void obsverNumberBackEvent(LifecycleOwner lifecycleOwner,Observer<ShopCartBean>observer){
        if(lifecycleOwner==null||observer==null){
            DebugUtil.sendException("obsverNumberBackEvent!=null 或者 observer！=null!");
            return;
        }
        LiveEventBus.get(ShopCartModel.EVENT_NUM_BACK,ShopCartBean.class).observe(lifecycleOwner,observer);
    }

    /*设置店铺的选中状态*/
    public void setStoreChecked(ShopCartStoreBean storeBean, boolean isTargetCheck) {
        storeBean.setChecked(isTargetCheck);
        List<ShopCartBean>list=storeBean.getList();
        int size=list.size();
        for(int i=0;i<size;i++){
            ShopCartBean shopCartBean=list.get(i);
            shopCartBean.setChecked(isTargetCheck);
        }
        notifyPostion(NOTIFY_ONTHER);
        measureTotalPrice();
    }

    public void setGoodsChecked(ShopCartBean shopCartBean, boolean isTargetCheck) {
        shopCartBean.setChecked(isTargetCheck);
        ShopCartStoreBean storeBean=shopCartBean.getStore();
        if(storeBean!=null){
           boolean isHaveNocheck= storeBean.isHaveNoCheckGoods();
           storeBean.setChecked(!isHaveNocheck);
        }
        notifyPostion(NOTIFY_ONTHER);
        measureTotalPrice();
    }
}
