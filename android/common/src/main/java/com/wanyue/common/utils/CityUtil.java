package com.wanyue.common.utils;

import android.os.Handler;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.wanyue.common.CommonApplication;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.bean.AreaParseInfo;
import com.wanyue.common.http.ParseArrayHttpCallBack;
import com.wanyue.common.interfaces.CommonCallback;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cn.qqtheme.framework.entity.City;
import cn.qqtheme.framework.entity.County;
import cn.qqtheme.framework.entity.Province;

/**
 * Created by  on 2018/6/28.
 */

public class CityUtil {

    private ArrayList<Province> mProvinceList;
    private static CityUtil sInstance;
    private Handler mHandler;

    private CityUtil() {
        mProvinceList = new ArrayList<>();
        mHandler = new Handler();
    }

    public static CityUtil getInstance() {
        if (sInstance == null) {
            synchronized (CityUtil.class) {
                if (sInstance == null) {
                    sInstance = new CityUtil();
                }
            }
        }
        return sInstance;
    }

    public void getCityListFromAssets(final CommonCallback<ArrayList<Province>> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader br = null;
                try {
                    InputStream is = CommonApplication.sInstance.getAssets().open("city.json");
                    br = new BufferedReader(new InputStreamReader(is, "utf-8"));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    String result = sb.toString();
                    if (!TextUtils.isEmpty(result)) {
                        if (mProvinceList == null) {
                            mProvinceList = new ArrayList<>();
                        }
                        mProvinceList.addAll(JSON.parseArray(result, Province.class));
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.callback(mProvinceList);
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.callback(null);
                            }
                        }
                    });
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }


    /*从远程获取数据*/
    public void getCityListFromRemote(final CommonCallback<ArrayList<Province>> callback){
        if(ListUtil.haveData(mProvinceList)){
           callback.callback(mProvinceList);
           return;
        }
        CommonAPI.getCityInfo(new ParseArrayHttpCallBack<AreaParseInfo>() {
            @Override
            public void onSuccess(int code, String msg, List<AreaParseInfo> info) {
                ArrayList<Province>list=new ArrayList<>();
                mProvinceList=list;
                for(AreaParseInfo areaParseInfo:info){
                    Province province=new Province();
                    province.setAreaId(areaParseInfo.getV());
                    province.setAreaName(areaParseInfo.getN());
                    List<City>cityList=new ArrayList<>();
                    province.setCities(cityList);
                    list.add(province);
                    List<AreaParseInfo>childCity=areaParseInfo.getC();
                    if(!ListUtil.haveData(childCity)){
                        return;
                    }
                    for(AreaParseInfo parseInfo:childCity){
                        City city=new City();
                        city.setAreaId(parseInfo.getV());
                        city.setAreaName(parseInfo.getN());
                        city.setProvinceId(province.getAreaId());
                        List<County>countyList=new ArrayList<>();
                        city.setCounties(countyList);
                        cityList.add(city);

                        List<AreaParseInfo>childCountList=parseInfo.getC();
                        if(!ListUtil.haveData(childCountList)){
                            return;
                        }
                        for(AreaParseInfo parseInfo2:childCountList){
                            County county=new County();
                            county.setAreaId(parseInfo2.getV());
                            county.setAreaName(parseInfo2.getN());
                            county.setCityId(city.getAreaId());
                            countyList.add(county);
                        }
                    }

                }
                callback.callback(mProvinceList);
            }
        });
    }


    public ArrayList<Province> getCityList() {
        return mProvinceList;
    }

}
