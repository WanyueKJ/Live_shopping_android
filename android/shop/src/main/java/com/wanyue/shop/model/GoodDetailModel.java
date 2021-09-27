package com.wanyue.shop.model;

import android.text.TextUtils;
import android.util.ArrayMap;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.utils.JsonUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.shop.bean.GoodsParseBean;
import com.wanyue.common.bean.SpecsValueBean;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GoodDetailModel extends ViewModel {
    private GoodsParseBean mGoodsParseBean;
    private String mSpecsKey;
    private SpecsValueBean mSelectSpecValue;
    private int mSelectNum=1;
    private String mLiveUid;

    private MutableLiveData<String>specsKeyLiveData;

    public static SpecsValueBean getSpecsValueBean(FragmentActivity activity) {
       String key= getSpecsKeyByContext(activity);
       GoodsParseBean goodsParseBean=getGoodsParse(activity);
       if(TextUtils.isEmpty(key)||goodsParseBean==null||goodsParseBean.getProductRealyValue()==null){
          return null;
       }
       return goodsParseBean.getProductRealyValue().get(key);
    }

    public void setLiveUid(String liveUid) {
        mLiveUid = liveUid;
    }

    public String getLiveUid() {
        return mLiveUid;
    }

    public void setGoodsParseBean(GoodsParseBean goodsParseBean) {
        mGoodsParseBean = goodsParseBean;
        if(mGoodsParseBean==null){
            return;
        }
        ArrayMap<String, SpecsValueBean> map=mGoodsParseBean.getProductRealyValue();
        if(mSpecsKey==null&&map!=null){
            mSpecsKey=getDefaultSpecsKey(map);
            mSelectSpecValue=map.get(mSpecsKey);
        }
    }

    public void transFormData(GoodsParseBean goodsParseBean, JSONObject jsonObject){
        String specsValueJson=jsonObject.getString("productValue");
        int jsontype= JsonUtil.getJsonType(specsValueJson);
        if(jsontype==JsonUtil.JSON_TYPE_OBJECT){
            ArrayMap<String, JSONObject> temp= JSON.parseObject(specsValueJson,ArrayMap.class);
            ArrayMap<String,SpecsValueBean>map=new ArrayMap<>();
            Set<String>set=temp.keySet();
            Iterator<String> iterable=set.iterator();
            while (iterable.hasNext()){
             String key=iterable.next();
             JSONObject jsonObject1=temp.get(key);
             SpecsValueBean specsValueBean=jsonObject1.toJavaObject(SpecsValueBean.class);
             map.put(key,specsValueBean);
            }
            goodsParseBean.setProductRealyValue(map);
        }else if(jsontype==JsonUtil.JSON_TYPE_ARRAY){
            List<SpecsValueBean> list=JSON.parseArray(specsValueJson,SpecsValueBean.class);
            int size=list.size();
            ArrayMap<String,SpecsValueBean>map=new ArrayMap<>();
            for(int i=0;i<size;i++){
                map.put(Integer.toString(i),list.get(i));
            }
            goodsParseBean.setProductRealyValue(map);
        }
    }


    /*默认选择第一个规格*/
    public static String getDefaultSpecsKey(Map<String,SpecsValueBean>map){
        if(map==null||map.size()<=0){
            return null;
        }
        Set<String>keySet= map.keySet();
        String key=keySet.iterator().next();
        return key;
    }

    public void setSelectSpecValue(SpecsValueBean selectSpecValue) {
        mSelectSpecValue = selectSpecValue;
    }

    public void setSpecsKey(String specsKey) {
        if(specsKey==null||StringUtil.equals(mSpecsKey,specsKey)){
            return;
        }
        mSpecsKey = specsKey;
        if(specsKeyLiveData!=null){
           specsKeyLiveData.setValue(mSpecsKey);
        }
    }

    public MutableLiveData<String> getSpecsKeyLiveData() {
        if(specsKeyLiveData==null){
           specsKeyLiveData=new MutableLiveData<>();
        }
        return specsKeyLiveData;
    }

    public SpecsValueBean getSelectSpecValue() {
        return mSelectSpecValue;
    }


    public GoodsParseBean getGoodsParseBean() {
        return mGoodsParseBean;
    }
    public String getSpecsKey() {
        return mSpecsKey;
    }
    public void setSelectNum(int selectNum) {
        mSelectNum = selectNum;
    }

    public int getSelectNum() {
        return mSelectNum;
    }

    public static GoodsParseBean getGoodsParse(FragmentActivity appCompatActivity){
        if(appCompatActivity==null){
            return null;
        }
        GoodDetailModel liveModel= ViewModelProviders.of(appCompatActivity).get(GoodDetailModel.class);
        if(liveModel!=null){
            return liveModel.getGoodsParseBean();
        }
        return null;
    }


    public static String getSpecsKeyByContext(FragmentActivity appCompatActivity){
        if(appCompatActivity==null){
            return null;
        }
        GoodDetailModel liveModel= ViewModelProviders.of(appCompatActivity).get(GoodDetailModel.class);
        if(liveModel!=null){
            return liveModel.getSpecsKey();
        }
        return null;
    }



    public static SpecsValueBean getSelectSpecValue(FragmentActivity appCompatActivity){
        if(appCompatActivity==null){
            return null;
        }
        GoodDetailModel liveModel= ViewModelProviders.of(appCompatActivity).get(GoodDetailModel.class);
        if(liveModel!=null){
           return liveModel.getSelectSpecValue();
        }
        return null;
    }
    public  static void setSelectNumByContext(FragmentActivity appCompatActivity, int selectNum) {
        if(selectNum<1){
            selectNum=1;
        }
        GoodDetailModel liveModel= ViewModelProviders.of(appCompatActivity).get(GoodDetailModel.class);
        liveModel.setSelectNum(selectNum);
    }

    public static int getSelectNumByContext(FragmentActivity appCompatActivity){
       GoodDetailModel liveModel= ViewModelProviders.of(appCompatActivity).get(GoodDetailModel.class);
       return liveModel.getSelectNum();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mSpecsKey=null;
        mGoodsParseBean=null;
        mSelectNum=1;
    }
}
