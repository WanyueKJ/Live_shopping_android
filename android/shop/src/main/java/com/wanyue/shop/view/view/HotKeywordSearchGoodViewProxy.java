package com.wanyue.shop.view.view;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.shop.R;
import com.wanyue.shop.adapter.KeywordAdapter;
import com.wanyue.shop.api.ShopAPI;
import com.wanyue.shop.view.widet.ViewGroupLayoutBaseAdapter;
import com.wanyue.shop.view.widet.linear.ListFlexboxLayout;
import java.util.List;

public class HotKeywordSearchGoodViewProxy extends RxViewProxy implements ViewGroupLayoutBaseAdapter.OnItemClickListener<String>, View.OnClickListener {
    private ListFlexboxLayout mListView;
    private KeywordAdapter mKeywordAdapter;
    private SearchViewProxy mSearchViewProxy;
    private SearchViewProxy.SeacherListner mSeacherListner;
    private ViewGroup mContainer;
    private TextView mTvSearch;

    @Override
    public int getLayoutId() {
        return R.layout.view_hot_keyword_search;
    }
    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);
        mContainer = (ViewGroup) findViewById(R.id.container);
        mTvSearch = (TextView) findViewById(R.id.tv_search);
        mSearchViewProxy=new SearchViewProxy();
        mSearchViewProxy.setAddPosition(0);
        mSearchViewProxy.setSeacherListner(mSeacherListner);
        mSearchViewProxy.setEnableAutoSearch(false);
        mSearchViewProxy.setHint("搜索商品名称关键字");
        getViewProxyChildMannger().addViewProxy(mContainer,mSearchViewProxy,mSearchViewProxy.getDefaultTag());
        mListView =  findViewById(R.id.listView);
        mKeywordAdapter=new KeywordAdapter(null);
        mListView.setAdapter(mKeywordAdapter);
        getHotKeyWord();
        mKeywordAdapter.setOnItemClickListener(this);
        mTvSearch.setOnClickListener(this);
    }

    private void getHotKeyWord() {
        ShopAPI.getKeywordList().compose(this.<List<String>>bindToLifecycle()).subscribe(new DefaultObserver<List<String>>() {
            @Override
            public void onNext(List<String> keywordBeans) {
                    if(mKeywordAdapter!=null){
                       mKeywordAdapter.setData(keywordBeans);
                    }
            }
        });
    }

    public void setSeacherListner(SearchViewProxy.SeacherListner seacherListner) {
        mSeacherListner = seacherListner;
    }
    @Override
    public void onItemClicked(ViewGroupLayoutBaseAdapter<String> adapter, View v, String item, int position) {
            if(mSearchViewProxy!=null&&item!=null){
               mSearchViewProxy.setTitle(item);
            }
    }
    @Override
    public void onClick(View v) {
        if(mSearchViewProxy!=null){
           mSearchViewProxy.search();
        }
    }
}
