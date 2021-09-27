package com.wanyue.common.proxy.function;


import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.wanyue.common.Constants;
import com.wanyue.common.R;
import com.wanyue.common.custom.ViewPagerSnapHelper;
import com.wanyue.common.proxy.RxViewProxy;
import com.wanyue.common.utils.ResourceUtil;
import java.util.List;
public class GalleryViewProxy extends RxViewProxy {

    private RecyclerView mReclyView;
    private ViewPagerSnapHelper mPagerSnapHelper;
    private GalleryAdapter mGalleryAdapter;
    private TextView mTvPageCount;
    private ViewPagerSnapHelper.OnPageSelectListner mOnPageSelectListner;
    private int mPosition;
    private boolean mIsEnable=true; //是否允许放大
    private boolean mIsScroll=true; //是否滑动
    private ImageView.ScaleType mScaleType; //图片适配类型
    private BaseQuickAdapter.OnItemClickListener mOnItemClickListener;

    @Override
    public int getLayoutId() {
        return R.layout.view_gallery;
    }

    @Override
    protected void initView(ViewGroup contentView) {
        super.initView(contentView);

        mReclyView=findViewById(R.id.reclyView);
        mTvPageCount = (TextView) findViewById(R.id.tv_page_count);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollHorizontally() {
                if(!mIsScroll){
                  return false;
                }
                return super.canScrollHorizontally();
            }
            @Override
            public boolean canScrollVertically() {
                if(!mIsScroll){
                   return false;
                }
                return super.canScrollVertically();
            }
        };

        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mReclyView.setLayoutManager(linearLayoutManager);

        mPagerSnapHelper= new ViewPagerSnapHelper();
        mPagerSnapHelper.attachToRecyclerView(mReclyView);

        List<String>data= (List<String>) getArgMap().get(Constants.DATA);
        Integer integer= (Integer) getArgMap().get(Constants.POSITION);
        mPosition= integer==null?mPosition:integer;

        mGalleryAdapter=new GalleryAdapter(null,getActivity());
        mGalleryAdapter.setOnItemClickListener((BaseQuickAdapter.OnItemClickListener) mOnItemClickListener);
        TextView textView=new TextView(getActivity());
        textView.setTextColor(ResourceUtil.getColor(getActivity(),R.color.gray1));
        textView.setTextSize(15);
        textView.setGravity(Gravity.CENTER);
        mGalleryAdapter.setEmptyView(textView);


        mReclyView.scrollToPosition(mPosition);
        mPagerSnapHelper.setPageSelectListner(new ViewPagerSnapHelper.OnPageSelectListner() {
            @Override
            public void onPageSelect(int position) {
                mPosition=position;
                setPageSize();
                if(mOnPageSelectListner!=null){
                   mOnPageSelectListner.onPageSelect(position);
                }
            }
        });
        setCanScroll(mIsScroll);
        setScaleType(mScaleType);
        enableZoom(mIsEnable);
        mReclyView.setAdapter(mGalleryAdapter);
        mGalleryAdapter.setData(data);
        setPageSize();
    }


    private void setPageSize() {
        if(mGalleryAdapter==null||mGalleryAdapter.size()==0){
            mTvPageCount.setText("0/0");
            mTvPageCount.setVisibility(View.INVISIBLE);
        }else{
            int size=mGalleryAdapter.size();
            if(mPosition>=size){
               mPosition=size-1;
            }
           mTvPageCount.setText((mPosition+1)+"/"+mGalleryAdapter.size());
        }
    }
    @Override
    protected boolean shouldBindButterKinfe() {
        return false;
    }

    public void setData(List<String>photoList,int position){

        mPosition=position;
        if(mGalleryAdapter!=null){
          mGalleryAdapter.setData(photoList);
        }
        setPageSize();
        mReclyView.scrollToPosition(position);
    }

    public void setPageSelectListner(ViewPagerSnapHelper.OnPageSelectListner pageSelectListner){
        this.mOnPageSelectListner=pageSelectListner;
    }

    public int size(){
        return mGalleryAdapter.size();
    }

    public void setCanScroll(boolean isCanScroll){
         mIsScroll=isCanScroll;

    }

    /*设置画廊的图片类型*/
    public void setScaleType(ImageView.ScaleType scaleType){
        if(scaleType==null){
            return;
        }
        mScaleType=scaleType;
        if(mGalleryAdapter!=null){
           mGalleryAdapter.setScaleType(scaleType);
        }
    }

    public void setCanScorll(boolean isCanScroll){
        setCanScroll(isCanScroll);
    }

    /*是否开启放大功能*/
    public void enableZoom(boolean isEnable){
        mIsEnable=isEnable;
        if(mGalleryAdapter!=null){
           mGalleryAdapter.setCanZoom(isEnable);
        }
    }

    public int remove(int position){
        int removePosition=mGalleryAdapter.removeItem(position)? position:-1;
        if(position==mGalleryAdapter.size()){  //删除的时候无法走改写的监听,需要手动判断一下
            position=position-1;
        }
        int size=mGalleryAdapter.size();
        if(mPosition>size-1){
           mPosition=size-1;
        }
        if(mOnPageSelectListner!=null){
           mOnPageSelectListner.onPageSelect(position);
        }
        setPageSize();
        return removePosition;
    }

    public void setOnItemClickListener(BaseQuickAdapter.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void scrollToPosition(int position){
        if(mReclyView==null||mGalleryAdapter==null||mGalleryAdapter.size()<=position){
            return;
        }
        mPosition=position;
        mReclyView.scrollToPosition(mPosition);
        setPageSize();
    }

    public void add(String url){
        if(mGalleryAdapter!=null){
           mGalleryAdapter.addData(url);
        }
        setPageSize();
    }
}
