package com.wanyue.main.view.proxy;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanyue.common.http.ParseArrayHttpCallBack;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.ViewUtil;
import com.wanyue.main.R;
import com.wanyue.main.adapter.ClassifyAdapter;
import com.wanyue.main.adapter.ClassifyIndexAdapter;
import com.wanyue.main.api.MainAPI;
import com.wanyue.main.bean.ClassifyBean;
import com.wanyue.main.bean.ClassifySectionBean;
import com.wanyue.main.view.activity.WindowActivity;
import com.wanyue.shop.bean.GoodsSearchArgs;
import com.wanyue.shop.view.activty.ShopSearchActivity;

import java.util.ArrayList;
import java.util.List;

public class MainHomeClassifyVIewProxy extends RxViewProxy {
    private FrameLayout mVpSearchContainer;
    private RecyclerView mReclyviewNavigation;
    private RecyclerView mReclyviewClassify;
    private EditText mEtSearch;

    private List<ClassifyBean>mData;
    private ClassifyIndexAdapter mClassifyIndexAdapter;
    private ClassifyAdapter mClassifyAdapter;
    private GridLayoutManager mGridLayoutManager;
    @Override
    public int getLayoutId() {
        return R.layout.view_main_home_classify;
    }

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        setDefaultStatusBarPadding();
        mVpSearchContainer =  findViewById(R.id.vp_search_container);
        mReclyviewNavigation =  findViewById(R.id.reclyview_navigation);
        mReclyviewClassify =  findViewById(R.id.reclyview_classify);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mClassifyIndexAdapter=new ClassifyIndexAdapter(null);
        mReclyviewNavigation.setAdapter(mClassifyIndexAdapter);
        mReclyviewNavigation.setLayoutManager(linearLayoutManager);
        mClassifyIndexAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
               classifyScrollPosition(position);
            }
        });
        initSearch();
        mGridLayoutManager=new GridLayoutManager(getActivity(),3);
        initClassifyReclyView();
    }

    /*初始化右边列表*/
    private void initClassifyReclyView() {
        mReclyviewClassify.setLayoutManager(mGridLayoutManager);
        mClassifyAdapter=new ClassifyAdapter(R.layout.item_recly_section_classify_normal,R.layout.item_recly_section_classify_head,null);
        mClassifyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                 if(mClassifyAdapter==null){
                        return;
                  }
                ClassifySectionBean sectionBean=mClassifyAdapter.getItem(position);
                if(!sectionBean.isHeader&&sectionBean.t!=null){
                  ClassifyBean classifyBean=  sectionBean.t;
                  GoodsSearchArgs goodsSearchArgs=new GoodsSearchArgs();
                  goodsSearchArgs.cid=classifyBean.getPid();
                  goodsSearchArgs.sid=classifyBean.getId();
                  goodsSearchArgs.className=classifyBean.getName();
                  ShopSearchActivity.forward(getActivity(),goodsSearchArgs);
                }
            }
        });


        mReclyviewClassify.setAdapter(mClassifyAdapter);
        mReclyviewClassify.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int mState;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState){
                mState=newState;
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (mState == RecyclerView.SCROLL_STATE_IDLE) {
                    return;
                }
                L.e("dy=="+dy);
                int firstPosition=mGridLayoutManager.findFirstVisibleItemPosition();
                if(mClassifyAdapter==null||mClassifyIndexAdapter==null){
                    return;
                }
                ClassifySectionBean sectionBean=mClassifyAdapter.getData().get(firstPosition);
                if(sectionBean.isHeader){
                    mClassifyIndexAdapter.setSelectIndex(sectionBean.getIndex());
                }
                int lastCompleteVisibleItemPosition=mGridLayoutManager.findLastCompletelyVisibleItemPosition();
                if ( lastCompleteVisibleItemPosition>=mClassifyAdapter.size() - 1) {
                    mClassifyIndexAdapter.setSelectIndex(mClassifyIndexAdapter.size()-1);
                }
            }
        });
    }

    private void initSearch() {
        mEtSearch = findViewById(R.id.et_search);
        mEtSearch.setHint(R.string.search_goods);
        ViewUtil.setEditextEnable(mEtSearch);
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    forwardSearch(v.getText().toString());
                    v.clearFocus();
                    return true;
                }
                return false;
            }
        });
    }
    private void forwardSearch(String string) {
        GoodsSearchArgs args=new GoodsSearchArgs();
        args.keyword=string;
        ShopSearchActivity.forward(getActivity(),args);
    }

    private void classifyScrollPosition(int position) {
        if(mData==null){
            return;
        }
        ClassifyBean classifyBean=mData.get(position);
        mGridLayoutManager.scrollToPositionWithOffset(classifyBean.getIndex(), 0);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser&&mData==null){
           MainAPI.getCategory(new ParseArrayHttpCallBack<ClassifyBean>() {
            @Override
            public void onSuccess(int code, String msg, List<ClassifyBean> info) {
                 if(isSuccess(code)&& ListUtil.haveData(info)){
                     setData(info);
                   }
                }
            });
        }
    }

    private void setData(List<ClassifyBean> info) {
        mData=info;
        if(mClassifyIndexAdapter!=null){
           mClassifyIndexAdapter.setData(mData);
        }
        initSectionBeanData(info);
    }


    /*初始化右边列表数据*/
    private void initSectionBeanData(List<ClassifyBean> info) {
        List<ClassifySectionBean>sectionBeanList=new ArrayList<>();
        int size=info.size();
        int index=0;
        for(int i=0;i<size;i++){
            ClassifyBean classifyBean=info.get(i);
            classifyBean.setIndex(index);
            ClassifySectionBean classifySectionBean=new ClassifySectionBean(true,classifyBean.getName());
            classifySectionBean.setIndex(i);
            index++;
            sectionBeanList.add(classifySectionBean);
            List<ClassifyBean>childArray=classifyBean.getChildren();
            if(!ListUtil.haveData(childArray)){
                return;
            }
            for(ClassifyBean classifyBeanChild :childArray){
                classifyBeanChild.setIndex(index);
                index++;
                classifySectionBean=new ClassifySectionBean(classifyBeanChild);
                sectionBeanList.add(classifySectionBean);
            }
        }
        if(mClassifyAdapter!=null){
           mClassifyAdapter.setData(sectionBeanList);
        }
    }
}
