package com.wanyue.common.proxy.function;

import android.content.Context;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.wanyue.common.R;
import com.wanyue.common.adapter.base.BaseReclyViewHolder;
import com.wanyue.common.adapter.base.BaseRecyclerAdapter;
import com.wanyue.common.custom.ZoomView;
import com.wanyue.common.utils.ListUtil;
import com.wanyue.common.utils.WordUtil;
import java.util.List;

public class GalleryAdapter  extends BaseRecyclerAdapter<String, BaseReclyViewHolder> {
    private ImageView.ScaleType scaleType;
    private boolean isCanZoom=true;
    private Context mContext;

    public GalleryAdapter(List<String> data, Context context) {
        super(data);
        this.mContext=context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_recly_gallery;
    }
    @Override
    protected void convert(BaseReclyViewHolder helper, String item) {
        ZoomView imageView=helper.getView(R.id.zoom_view);
        imageView.setTransitionName(WordUtil.getString(R.string.transition_image)+helper.getLayoutPosition());
        imageView.setIsZoomEnabled(isCanZoom);
        if(scaleType!=null){
            imageView.setScaleType(scaleType);
        }
        ImageView zoomView=helper.getView(R.id.zoom_view);
        Glide.with(mContext).load(item).into(zoomView);
    }

    public boolean removeItem(int position) {
        if(mData==null||mData.size()==position){
            return false;
        }
        super.remove(position);
        return true;
    }

    public void setCanZoom(boolean canZoom) {
        isCanZoom = canZoom;
        if(!ListUtil.haveData(mData)){
           notifyReclyDataChange();
        }
    }

    public void setScaleType(ImageView.ScaleType scaleType) {
        this.scaleType = scaleType;
        if(!ListUtil.haveData(mData)){
            notifyReclyDataChange();
        }
    }


}
