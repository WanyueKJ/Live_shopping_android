package com.wanyue.common.custom.refresh;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.wanyue.common.R;
import com.wanyue.common.adapter.RefreshAdapter;
import com.wanyue.common.custom.ItemDecoration;
import com.wanyue.common.http.HttpCallback;
import com.wanyue.common.proxy.BaseProxyMannger;
import com.wanyue.common.proxy.BaseViewProxy;
import com.wanyue.common.proxy.ViewProxyMannger;
import com.wanyue.common.server.observer.DefaultObserver;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ResourceUtil;
import com.wanyue.common.utils.ToastUtil;
import com.wanyue.common.utils.WordUtil;

import java.util.List;
import io.reactivex.Observable;

public class RxRefreshView<T> extends FrameLayout implements View.OnClickListener {

    public static  final int STATE_NO_DATA=1;
    public static  final int STATE_ERROR=2;
    public static  final int STATE_HAVE_DATA=3;


    private Context mContext;
    private int mLayoutRes;
    private View mContentView;
    private SmartRefreshLayout mSmartRefreshLayout;
    private MaterialHeader mHeader;
    private ClassicsFooter mFooter;
    private RecyclerView mRecyclerView;
    private ViewGroup mEmptyLayout;//没有数据的View
    private View mLoadFailureView;//加载失败View

    private FrameLayout mVpDataContainer;

    private ImageView mImgEmpty;


    private boolean mRefreshEnable;//下拉刷新是否可用
    private boolean mLoadMoreEnable;//上拉加载是否可用
    private int mPageCount;//页数
    private int mItemCount;//每页的Item个数

    private DataListner<T> dataListner;
    private DataAdapter<T> dataAdapter;

    private boolean mIsMirror;

    private String mNoDataTip;

    private int mIconId;




    public RxRefreshView(Context context) {
        this(context, null);
    }

    public RecyclerView getRecyclerView(){
      return mRecyclerView;
    }

    public RxRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RxRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CommonRefreshView);
        mRefreshEnable = ta.getBoolean(R.styleable.CommonRefreshView_crv_refreshEnable, true);
        mLoadMoreEnable = ta.getBoolean(R.styleable.CommonRefreshView_crv_loadMoreEnable, true);
        mIsMirror= ta.getBoolean(R.styleable.CommonRefreshView_crv_mirror, false);
        if(mIsMirror){
            mLayoutRes=R.layout.view_refresh_mirror;
            mLoadMoreEnable=false;
        }else{
            mLayoutRes = ta.getResourceId(R.styleable.CommonRefreshView_crv_layout, R.layout.view_refresh_default);
        }
        mItemCount = ta.getInteger(R.styleable.CommonRefreshView_crv_itemCount, 10);
        ta.recycle();
        init();
    }

    public void scrollPosition(int position){
        if(mRecyclerView!=null){
           mRecyclerView.scrollToPosition(position);
        }
    }

    private void init() {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(mLayoutRes, this, false);
        mContentView = view;
        addView(view);
        mSmartRefreshLayout = (SmartRefreshLayout) view.findViewById(R.id.refreshLayout);
        mSmartRefreshLayout.setEnableLoadMoreWhenContentNotFull(false);//是否在列表不满一页时候开启上拉加载功能
        mSmartRefreshLayout.setEnableFooterFollowWhenLoadFinished(true);//是否在全部加载结束之后Footer跟随内容
        mSmartRefreshLayout.setEnableOverScrollBounce(false);//设置是否开启越界回弹功能（默认true）
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mVpDataContainer=view.findViewById(R.id.vp_data_container);
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                L.e("mSmartRefreshLayout==onRefresh");
                if(mIsMirror){
                    loadMore();
                }else{
                    refresh();
                }

            }
        });
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshlayout) {
                L.e("mSmartRefreshLayout==onLoadMore");
                if(mIsMirror){
                    refresh();
                }else{
                    loadMore();
                }
            }
        });

        mSmartRefreshLayout.setEnableRefresh(mRefreshEnable);
        mSmartRefreshLayout.setEnableLoadMore(mLoadMoreEnable);
        mLoadFailureView = view.findViewById(R.id.load_failure);


        int textColor = ContextCompat.getColor(mContext, R.color.textColor);
        mHeader = findViewById(R.id.header);
        /*if(mHeader!=null){
            mHeader.setAccentColor(textColor);
        }*/

        mFooter = findViewById(R.id.footer);
        if(mFooter!=null){
           mFooter.setAccentColor(textColor);
           mFooter.setTextSizeTitle(14);
        }
    }

    public void setRecycledViewPool(RecyclerView.RecycledViewPool recycledViewPool){
        mRecyclerView.setRecycledViewPool(recycledViewPool);
    }


    public void autoRefresh(){
        mSmartRefreshLayout.autoRefresh();
    }
    public void autoLoadMore(){
        mSmartRefreshLayout.autoLoadMore();
    }
    public boolean isRefreshing(){
        return mSmartRefreshLayout.getState()== RefreshState.Refreshing;
    }

    public void setHasFixedSize(boolean b) {
        if(mRecyclerView!=null){
           mRecyclerView.setHasFixedSize(b);
        }
    }

    public static abstract class DataListner<T>{
        public abstract Observable<List<T>> loadData(int p);
        public abstract void compelete(List<T> data);
        public  void before(List<T> data){
        }
        public  void isRefresh(List<T> data,boolean isRefresh){

        }

        public abstract void error(Throwable e);
    }

    private RefreshDataLisnter<T> mRefreshDataLisnter;

    public void setRefreshDataLisnter(RefreshDataLisnter refreshDataLisnter) {
        mRefreshDataLisnter = refreshDataLisnter;
    }

    public interface RefreshDataLisnter<T>{
        public void refreshData(List<T> t);
    }


    private DefaultObserver freshObserver= new DefaultObserver<List<T>>() {
        private  int mDataCount;
        @Override
        public void onNext(List<T> data) {

            RecyclerView.Adapter recyclerViewAdapter = mRecyclerView.getAdapter();
            if (recyclerViewAdapter == null) {
                return;
            }
            mDataCount = data==null?0:data.size();
            if(mRefreshDataLisnter!=null){
               mRefreshDataLisnter.refreshData(data);
            }

            if(dataListner!=null){
               dataListner.before(data);
            }

            if(dataListner!=null){
               dataListner.isRefresh(data,true);
            }

            if(dataAdapter!=null){
               dataAdapter.setData(data);
            }

            if(dataListner!=null){
               dataListner.compelete(dataAdapter.getArray());
            }

            if(mDataCount==0){
              statusChange(STATE_NO_DATA);
            }else{
                statusChange(STATE_HAVE_DATA);
            }
        }
        @Override
        public void onError(Throwable e) {
            super.onError(e);
            statusChange(STATE_ERROR);
            if(dataListner!=null){
               dataListner.error(e);
            }
        }
        @Override
        public void onComplete() {
            super.onComplete();

            if (mSmartRefreshLayout != null) {
                if(mIsMirror){
                    mSmartRefreshLayout.finishLoadMore(true);
                }else{
                    mSmartRefreshLayout.finishRefresh(true);
                    if (mDataCount < mItemCount) {
                        mSmartRefreshLayout.finishLoadMoreWithNoMoreData();
                    }
                }

            }
        }
    };

    private void statusChange(int state) {
        if(state==STATE_ERROR){
            /*initErrorLayout();
            if (mLoadFailureView != null) {
                if (mLoadFailureView.getVisibility() != View.VISIBLE) {
                    if (mRecyclerView != null) {
                        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
                        if (adapter != null && adapter.getItemCount() > 0) {
                            ToastUtil.show(R.string.load_failure);
                        } else {
                            mLoadFailureView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        mLoadFailureView.setVisibility(View.VISIBLE);
                    }
                } else {
                    ToastUtil.show(R.string.load_failure);
                }
            }

            if (mEmptyLayout != null){
                mEmptyLayout.setVisibility(View.INVISIBLE);
            }*/
        }else if(state==STATE_NO_DATA){
            initEmptyLayout();
            if (mLoadFailureView != null) {
                mLoadFailureView.setVisibility(View.INVISIBLE);
            }
            if (mEmptyLayout != null){
                mEmptyLayout.setVisibility(View.VISIBLE);
            }
        }else{
            if (mLoadFailureView != null) {
                mLoadFailureView.setVisibility(View.INVISIBLE);
            }
            if (mEmptyLayout != null){
                mEmptyLayout.setVisibility(View.INVISIBLE);
            }
        }
    }


    private void requestData(DefaultObserver observer) {
        if(dataListner!=null&& dataListner.loadData(mPageCount)!=null){
            dataListner.loadData(mPageCount).subscribe(observer);
        }
    }



    /*架设接口，适配各种adapter，提高拓展性*/
    public interface DataAdapter<E>{
        public void setData(List<E> data);
        public void appendData(List<E> data);
        public void appendData(int index,List<E> data);
        public List<E> getArray();
        public RecyclerView.Adapter returnRecyclerAdapter();
        public void notifyReclyDataChange();
    }


    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void addItemDecoration(ItemDecoration itemDecoration) {
        if(mRecyclerView.getItemDecorationCount()>0&&mRecyclerView.getItemDecorationAt(0)==itemDecoration){
           mRecyclerView.removeItemDecorationAt(0);
        }
        mRecyclerView.addItemDecoration(itemDecoration);
    }

    public void removeItemDecoration(ItemDecoration itemDecoration) {
        mRecyclerView.removeItemDecoration(itemDecoration);
    }


    public void setNoDataTip(String text){
        mNoDataTip=text;
        setNodataTip();
    }

    private void initEmptyLayout() {
        if(mEmptyLayout==null){
          if(mEmptyViewProxy!=null){
              boolean isInitSucc= initEmptyViewProxy();
              if(isInitSucc){
                  return;
              }
          }
          ViewStub stub = findViewById(R.id.empty_no_data);
            if(stub !=null){
               mEmptyLayout = (FrameLayout)stub.inflate();
               mImgEmpty = (ImageView) mEmptyLayout.findViewById(R.id.img_empty);
               if(mIconId!=0){
                 mImgEmpty.setImageDrawable(ResourceUtil.getDrawable(mIconId,true));
               }
               setNodataTip();
            }
        }
    }

    private void initErrorLayout() {
        if(mLoadFailureView==null){
           ViewStub stub =findViewById(R.id.empty_load_failed);
            if(stub !=null){
                mLoadFailureView = stub.inflate();
                View btnReload = mLoadFailureView.findViewById(R.id.btn_reload);
                if (btnReload != null) {
                    btnReload.setOnClickListener(this);
                }
            }
        }
    }




    private void setNodataTip() {
        if(mEmptyLayout==null){
            return;
        }
        TextView textView=mEmptyLayout.findViewById(R.id.tv_no_data);
        if(textView!=null&&!TextUtils.isEmpty(mNoDataTip)){
            textView.setText(mNoDataTip);
        }
    }

    public void setNoDataTip(int text){
        setNoDataTip(WordUtil.getString(text));
    }


    public void setAdapter(DataAdapter<T> adapter){
        dataAdapter=(DataAdapter<T>) adapter;
        mRecyclerView.setAdapter(dataAdapter.returnRecyclerAdapter());
    }

    public void initData() {
        refresh();
    }

    private void refresh() {
        if (dataListner != null) {
            mPageCount = 1;
            dataListner.loadData(mPageCount).subscribe(freshObserver);
        }
    }

    DefaultObserver loadMoreObserver=new DefaultObserver<List<T>>() {
        private int mDataCount;

        @Override
        public void onNext(List<T> data) {
            if (dataAdapter == null) {
                mPageCount--;
                return;
            }

            mDataCount = data==null?0:data.size();
            if(dataAdapter!=null){
               if(mIsMirror){
                 dataAdapter.appendData(0,data);
               }else{
                dataAdapter.appendData(data);
               }
                if(dataListner!=null){
                   dataListner.compelete(dataAdapter.getArray());
                }

                if(dataListner!=null){
                    dataListner.isRefresh(data,false);
                }

              int size= dataAdapter.getArray().size();
              if(size>0){
                  statusChange(STATE_HAVE_DATA);
              }else{
                  statusChange(STATE_NO_DATA);
              }
            }else{
             statusChange(STATE_NO_DATA);
            }

        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
            statusChange(STATE_ERROR);
            if(dataListner!=null){
                dataListner.error(e);
            }
        }

        @Override
        public void onComplete() {
            super.onComplete();
            if (mSmartRefreshLayout != null) {
                if(mIsMirror){
                  mSmartRefreshLayout.finishRefresh();
                }else{
                    if (mDataCount < mItemCount) {
                        mSmartRefreshLayout.finishLoadMoreWithNoMoreData();
                    } else {
                        mSmartRefreshLayout.finishLoadMore(true);
                    }
                }

            }
        }
    };


    private void loadMore() {
        if (dataListner != null) {
            mPageCount++;
            dataListner.loadData(mPageCount).subscribe(loadMoreObserver);
        }
    }

    public int getPageCount() {
        return mPageCount;
    }

    public void setPageCount(int pageCount) {
        mPageCount = pageCount;
    }


    public int getItemCount() {
        return mItemCount;
    }

    public void setItemCount(int itemCount) {
        mItemCount = itemCount;
    }

    public void setRefreshEnable(boolean enable) {
        if (mSmartRefreshLayout != null) {
            mSmartRefreshLayout.setEnableRefresh(enable);
        }
    }

    public void setLoadMoreEnable(boolean enable) {
        if (mSmartRefreshLayout != null) {
            mSmartRefreshLayout.setEnableLoadMore(enable);
        }
    }
    //设置是否可以滑动
    public void setCanScroll(boolean canScroll){
        if(mRecyclerView!=null&&mRecyclerView.getLayoutManager()!=null&&mRecyclerView.getLayoutManager() instanceof IControllLayoutMannger ){
            IControllLayoutMannger iControllLayoutMannger= (IControllLayoutMannger) mRecyclerView.getLayoutManager();
            iControllLayoutMannger.setScrollEnabled(canScroll);
        }
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_reload) {
            refresh();
        }
    }


    public interface DataHelper<T> {
        RefreshAdapter<T> getAdapter();

        void loadData(int p, HttpCallback callback);

        List<T> processData(String[] info);

        /**
         * 下拉刷新成功
         *
         * @param list      Adapter的全部数据的List
         * @param listCount Adapter的全部数据的个数
         */
        void onRefreshSuccess(List<T> list, int listCount);

        /**
         * 下拉刷新失败
         */
        void onRefreshFailure();

        /**
         * 上拉加载成功
         *
         * @param loadItemList  本次加载到的数据
         * @param loadItemCount 加载到的数据个数
         */
        void onLoadMoreSuccess(List<T> loadItemList, int loadItemCount);

        /**
         * 加载失败
         */
        void onLoadMoreFailure();
    }

    /**
     * 空数据的布局
     */
    public void setEmptyLayoutId(int noDataLayoutId) {
        if (mEmptyLayout != null) {
            mEmptyLayout.removeAllViews();
            if (noDataLayoutId > 0) {
                View v = LayoutInflater.from(mContext).inflate(noDataLayoutId, mEmptyLayout, false);
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
                params.gravity = Gravity.CENTER;
                v.setLayoutParams(params);
                mEmptyLayout.addView(v);
            }
        }
    }

    private BaseViewProxy mEmptyViewProxy;
    private BaseProxyMannger mViewProxyMannger;


    public void setEmptyViewProxy(ViewProxyMannger mannger, BaseViewProxy viewProxy){
        mEmptyViewProxy=viewProxy;
        mViewProxyMannger=mannger;

    }

    private boolean initEmptyViewProxy() {
        if(mViewProxyMannger==null||mEmptyViewProxy==null){
           return false;
        }
        mViewProxyMannger.addViewProxy(mVpDataContainer,mEmptyViewProxy,mEmptyViewProxy.getDefaultTag());
        mEmptyLayout=mEmptyViewProxy.getContentView();
        mEmptyLayout.setVisibility(View.INVISIBLE);
        return true;
    }


    public void setDataListner(DataListner<T> dataListner) {
        this.dataListner = dataListner;
    }


    public View getContentView() {
        return mContentView;
    }


    @Deprecated
    public void setRecyclerViewAdapter(RefreshAdapter adapter) {
        if (mRecyclerView != null) {
            mRecyclerView.setAdapter(adapter);
        }
    }


    public void setIconId(int iconId) {
        mIconId = iconId;
    }

    public void setReclyViewSetting(ReclyViewSetting reclyViewSetting){
        mRecyclerView.setLayoutManager(reclyViewSetting.layoutManager);
        if(mRecyclerView.getItemDecorationCount()==0&&reclyViewSetting.itemDecoration!=null){
           mRecyclerView.addItemDecoration(reclyViewSetting.itemDecoration);
        }
        mRecyclerView.setHasFixedSize(reclyViewSetting.hasFixedSize);
    }

    public static class ReclyViewSetting{
        private RecyclerView.LayoutManager layoutManager;
        private ItemDecoration itemDecoration;
        private  boolean hasFixedSize;

        public ReclyViewSetting(RecyclerView.LayoutManager layoutManager, ItemDecoration itemDecoration,boolean hasFixedSize) {
            this.layoutManager = layoutManager;
            this.itemDecoration = itemDecoration;
            this.hasFixedSize=hasFixedSize;
        }


        public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        public static ReclyViewSetting createLinearSetting(Context context){
            ControllLayoutManager linearLayoutManager=new ControllLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            ItemDecoration decoration = new ItemDecoration(context, 0xffdd00, 5, 5);
            ReclyViewSetting reclyViewSetting=new ReclyViewSetting(linearLayoutManager,decoration,true);
            return reclyViewSetting;
        }

        public static ReclyViewSetting createLinearSetting(Context context,int span){
            ControllLayoutManager linearLayoutManager=new ControllLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            ItemDecoration decoration=null;
            if(span!=0){
               decoration= new ItemDecoration(context, 0xffdd00, span, span);
            }
            ReclyViewSetting reclyViewSetting=new ReclyViewSetting(linearLayoutManager,decoration,true);
            return reclyViewSetting;
        }

        public static ReclyViewSetting createGridSetting(Context context,int spanCount){
            return createGridSetting(context,spanCount,5);
        }

        public static ReclyViewSetting createGridSetting(Context context,int spanCount,int divider){
            GridLayoutManager gridLayoutManager= new GridLayoutManager(context,spanCount);
            ItemDecoration decoration = new ItemDecoration(context, 0xffdd00, divider, divider);
            ReclyViewSetting reclyViewSetting=new ReclyViewSetting(gridLayoutManager,decoration,true);
            return reclyViewSetting;
        }

        public static ReclyViewSetting creatStaggeredGridSetting(Context context,int spanCount){
            StaggeredGridLayoutManager gridLayoutManager=new StaggeredGridLayoutManager(spanCount,StaggeredGridLayoutManager.VERTICAL);
            ReclyViewSetting reclyViewSetting=new ReclyViewSetting(gridLayoutManager,null,true);
            return reclyViewSetting;
        }
        public void settingRecyclerView(RecyclerView recyclerView){
            if(recyclerView.getLayoutManager()!=null){
                return;
            }
            recyclerView.setLayoutManager(layoutManager);
            if(recyclerView.getItemDecorationCount()==0&&itemDecoration!=null){
                recyclerView.addItemDecoration(itemDecoration);
            }
            recyclerView.setHasFixedSize(hasFixedSize);
        }
    }

}
