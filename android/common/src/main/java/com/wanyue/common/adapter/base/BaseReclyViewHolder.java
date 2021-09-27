package com.wanyue.common.adapter.base;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.chad.library.adapter.base.BaseViewHolder;
import com.wanyue.common.glide.ImgLoader;
import com.wanyue.common.utils.ViewUtil;

import java.io.File;

public  class BaseReclyViewHolder extends BaseViewHolder {
        private int objectPosition;
        private View.OnClickListener mOnClickListener;


        public BaseReclyViewHolder(View view) {
            super(view);
        }
        public BaseReclyViewHolder setImageUrl(String url, int id){
            ImageView imageView=getView(id);
            if(imageView==null|| TextUtils.isEmpty(url)){
                return this;
            }
            ImgLoader.display(imageView.getContext(),url,imageView);
            return this;
        }

    public void setImageResouceId(int resoureId,int id){
        ImageView imageView=getView(id);
        if(imageView==null ){
            return;
        }

        ImgLoader.display(imageView.getContext(),resoureId,imageView);
    }


    public void setImageDrawable(int resoureId,int id){
        ImageView imageView=getView(id);
        if(imageView==null ){
            return;
        }
        ImgLoader.display(imageView.getContext(),resoureId,imageView);
    }

    public void setVideoThumb(String url,int id){
        ImageView imageView=getView(id);
        if(imageView==null||TextUtils.isEmpty(url) ){
            return;
        }
        ImgLoader.displayVideoThumb(imageView.getContext(),url,imageView);
    }


    public void setObjectPosition(int objectPosition) {
        this.objectPosition = objectPosition;
    }

    public int getObjectPosition() {
        return objectPosition;
    }

    public void setVideoThumbRemote(String url, int id){
        ImageView imageView=getView(id);
        if(imageView==null||TextUtils.isEmpty(url) ){
            return;
        }
        ImgLoader.displayVideoThumbRemote(imageView.getContext(),url,imageView);
    }

    public void setImageResouceFile(File file, int id){
        ImageView imageView=getView(id);
        if(imageView==null ){
            return;
        }
        ImgLoader.display(imageView.getContext(),file,imageView);
    }

    public void setOnChildClickListner(int id,View.OnClickListener childClickListner){
            View view=getView(id);
            if(view==null){
                return;
            }
            view.setTag(getLayoutPosition());
            view.setOnClickListener(childClickListner);
    }

    @Override
    public BaseViewHolder addOnClickListener(int... viewIds) {
        if(viewIds!=null&&viewIds.length==1){
            View view=getView(viewIds[0]);
            if(view!=null&&view.hasOnClickListeners()){
                return this;
            }
        }
        return super.addOnClickListener(viewIds);
    }
}