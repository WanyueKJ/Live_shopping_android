package com.wanyue.shop.evaluate.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.Constants;
import com.wanyue.common.activity.BaseActivity;
import com.wanyue.common.custom.refresh.RxRefreshView;
import com.wanyue.common.http.ParseHttpCallback;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.utils.BitmapUtil;
import com.wanyue.common.utils.DpUtil;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.StringUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.shop.R;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.business.ShopState;
import com.wanyue.shop.evaluate.adapter.EvaluateListAdapter;
import com.wanyue.shop.evaluate.bean.EvaluateBean;
import com.wanyue.shop.view.dialog.GalleryDialogFragment;
import com.wanyue.shop.view.widet.RatingStar;
import com.wanyue.shop.view.widet.ViewGroupLayoutBaseAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class EvaluateListActivity extends BaseActivity {
    private RatingStar mStar;
    private TextView mTvFeedbackRate;
    private RadioGroup mVpBtnEvaluate;
    private RadioButton mBtnTotal;
    private RadioButton mBtnBest;
    private RadioButton mBtnNormal;
    private RadioButton mBtnBad;
    private ViewGroup mVpTopContainer;

    private RxRefreshView<EvaluateBean> mRefreshView;
    private EvaluateListAdapter mEvaluateListAdapter;

    private String mId;
    private int mType=ShopState.COMMENTS_TOTAL;
    Function<List<JSONObject>, List<EvaluateBean>>mFunction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void init() {
        setTabTitle("商品评分");
        mId=getIntent().getStringExtra(Constants.KEY_ID);
        mStar = findViewById(R.id.star);
        mVpTopContainer = findViewById(R.id.vp_top_container);
        mTvFeedbackRate = findViewById(R.id.tv_feedback_rate);
        mVpBtnEvaluate =  findViewById(R.id.vp_btn_evaluate);
        mBtnTotal = findViewById(R.id.btn_total);
        mBtnBest =  findViewById(R.id.btn_best);
        mBtnNormal = findViewById(R.id.btn_normal);
        mBtnBad =  findViewById(R.id.btn_bad);
        mRefreshView =  findViewById(R.id.refreshView);
        mRefreshView.setIconId(R.drawable.icon_empty_no_evaluate);


        int size = DpUtil.dp2px(10);
        Bitmap starNormal = BitmapUtil.thumbImageWithMatrix(getResources(), R.drawable.icon_evaluate_default, size, size);
        Bitmap starFocus = BitmapUtil.thumbImageWithMatrix(getResources(), R.drawable.icon_evaluate_select, size, size);

        mStar.setNormalImg(starNormal);
        mStar.setFocusImg(starFocus);


        mVpBtnEvaluate.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId==R.id.btn_total){
                       checkType(ShopState.COMMENTS_TOTAL);
                    }else if(checkedId==R.id.btn_best){
                        checkType(ShopState.COMMENTS_GOODS);
                    }else if(checkedId==R.id.btn_normal){
                        checkType(ShopState.COMMENTS_NORMAL);
                    }else if(checkedId==R.id.btn_bad){
                        checkType(ShopState.COMMENTS_BAD);
                    }
            }
        });

        mEvaluateListAdapter=new EvaluateListAdapter(null,getResources());
        mEvaluateListAdapter.setStringOnItemClickListener(new ViewGroupLayoutBaseAdapter.OnItemClickListener<String>() {
            @Override
            public void onItemClicked(ViewGroupLayoutBaseAdapter<String> adapter, View v, String item, int position) {
                 List<String>urlList=adapter.getData();
                 showGally(urlList,position);
            }
        });

        mRefreshView.setAdapter(mEvaluateListAdapter);
        mRefreshView.setReclyViewSetting(RxRefreshView.ReclyViewSetting.createLinearSetting(this,1));
        mRefreshView.setDataListner(new RxRefreshView.DataListner<EvaluateBean>() {
            @Override
            public Observable<List<EvaluateBean>> loadData(int p) {
                return getData(p);
            }

            @Override
            public void compelete(List<EvaluateBean> data) {
            }

            @Override
            public void error(Throwable e) {
            }
        });
        getEvaluateConifg();

    }

    private void showGally(List<String> urlList,int position) {
        GalleryDialogFragment galleryDialogFragment = new GalleryDialogFragment();
        galleryDialogFragment.setGalleryViewProxy(urlList, position, getViewProxyMannger());
        galleryDialogFragment.show(getSupportFragmentManager());

    }
/*├─ sum_count	number	非必须		评论总数
├─ good_count	number	非必须		好评总数
├─ in_count	number	非必须		中评总数
├─ poor_count	number	非必须		差评总数
├─ reply_chance	string	非必须		好评率
├─ reply_star*/

    private void getEvaluateConifg() {
        ShopAPI.getEvaluateConfig(mId).compose(this.<JSONObject>bindUntilOnDestoryEvent())
        .subscribe(new DefaultObserver<JSONObject>() {
            @Override
            public void onNext(JSONObject jsonObject) {
                mVpTopContainer.setVisibility(View.VISIBLE);
                int reply_star=jsonObject.getIntValue("reply_star");
                mStar.setPosition(reply_star-1);

                String sum_count=jsonObject.getIntValue("sum_count")+"";
                mBtnTotal.setText(getString(R.string.evaluate_btn_tip0,sum_count));

                String good_count=jsonObject.getIntValue("good_count")+"";
                mBtnBest.setText(getString(R.string.evaluate_btn_tip1,good_count));

                String in_count=jsonObject.getIntValue("in_count")+"";
                mBtnNormal.setText(getString(R.string.evaluate_btn_tip2,in_count));

                String poor_count=jsonObject.getIntValue("poor_count")+"";
                mBtnBad.setText(getString(R.string.evaluate_btn_tip3,poor_count));

                String reply_chance=jsonObject.getString("reply_chance");
                mTvFeedbackRate.setText(reply_chance+"%");
            }
        });
    }

    private void checkType(int type) {
        mType=type;
        if(mRefreshView!=null){
           mRefreshView.initData();
        }
    }

    private Observable<List<EvaluateBean>> getData(int p) {
        initTransForm();
        return ShopAPI.getEvaluateList(mId,p,mType).map(mFunction).compose(this.<List<EvaluateBean>>bindUntilOnDestoryEvent());
    }

    private void initTransForm() {
        if(mFunction==null){
            mFunction= new Function<List<JSONObject>, List<EvaluateBean>>() {
                @Override
                public List<EvaluateBean> apply(List<JSONObject> jsonObjectList) throws Exception {
                    if(!ListUtil.haveData(jsonObjectList)){
                        return new ArrayList<>(1);
                    }
                    List<EvaluateBean>list=new ArrayList<>(jsonObjectList.size());
                    for(JSONObject jsonObject:jsonObjectList){
                        EvaluateBean evaluateBean=jsonObject.toJavaObject(EvaluateBean.class);
                        list.add(evaluateBean);
                        String pics=jsonObject.getString("pics");
                        if(!TextUtils.isEmpty(pics)){
                            List<String>picList=  StringUtil.split(pics);
                            evaluateBean.setPictureList(picList);
                        }
                    }
                    return list;
                }
            };
        }

    }


    @Override
    protected void onFirstResume() {
        super.onFirstResume();
        if(mRefreshView!=null){
           mRefreshView.initData();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_evaluate_listctivity;
    }

    public static void forward(Context context,String goodsId){
        Intent intent= new Intent(context,EvaluateListActivity.class);
        intent.putExtra(Constants.KEY_ID,goodsId);
        context.startActivity(intent);
    }
}

