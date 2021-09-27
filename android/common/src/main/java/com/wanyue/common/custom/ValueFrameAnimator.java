package com.wanyue.common.custom;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import com.wanyue.common.utils.L;
import com.wanyue.common.utils.ListUtil;
import java.util.HashSet;
import java.util.Set;

public class ValueFrameAnimator {
    private Drawable[]drawables;
    private ValueAnimator valueAnimator;
    private ImageView imageView;
    private AnimListner animListner;
    private Interpolator singleInterpolator;
    private long durcation;
    private Set<ImageView>mImageViewSet;
    public static final int NO_LIMIT=1000000000;

    public ValueFrameAnimator() {
    }
    private ValueFrameAnimator setDrawables(@Nullable final Drawable[] drawables) {
        this.drawables = drawables;
        init();
        return this;
    }

    private void init() {
        if(valueAnimator==null){
            valueAnimator=ValueAnimator.ofFloat(0,drawables.length-1);
            valueAnimator.setDuration(durcation);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value= (float) animation.getAnimatedValue();
                   // L.e("value=="+value);
                    int intIndex=  Math.round(value);
                    //L.e("intIndex=="+intIndex);
                    if(intIndex>=drawables.length){
                        return;
                    }

                    if(imageView!=null){
                        L.e("intIndex=="+intIndex);
                        imageView.setImageDrawable(drawables[intIndex]);
                    }

                    if(ListUtil.haveData(mImageViewSet)){
                        for(ImageView imageView:mImageViewSet){
                            imageView.setImageDrawable(drawables[intIndex]);
                        }
                    }
                }
            });

            valueAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if(animListner!=null){
                       animListner.start();
                    }
                }
                @Override
                public void onAnimationEnd(Animator animation) {
                }
                @Override
                public void onAnimationCancel(Animator animation) {
                }
                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
        }else{
            valueAnimator.setDuration(durcation);
            valueAnimator.setFloatValues(0,drawables.length-1);
        }
    }

    public ValueFrameAnimator setSingleInterpolator(Interpolator interpolator){
        init();
        this.singleInterpolator=interpolator;
        if(singleInterpolator!=null){
           valueAnimator.setInterpolator(interpolator);
        }

        return this;
    }


    public ValueFrameAnimator setRepeat(int repeatCount){
        if(repeatCount<1){
            repeatCount=1;
        }
        init();
        valueAnimator.setRepeatCount(repeatCount);
        return this;
    }

    public static ValueFrameAnimator ofFrameAnim(Drawable...drawables){
        ValueFrameAnimator rxValueAnimator=new ValueFrameAnimator();
        rxValueAnimator.setDrawables(drawables);
        return rxValueAnimator;
    }

    public ValueFrameAnimator durcation(long durcation) {
        this.durcation = durcation;
        init();
        return this;
    }

    public ValueFrameAnimator anim(ImageView imageView){
        cancle();
        this.imageView=imageView;
        return this;
    }

    public ValueFrameAnimator addAnim(ImageView imageView){
        if(imageView==null){
            return this;
        }

         if(mImageViewSet==null){
            mImageViewSet=new HashSet<>();
         }
        mImageViewSet.add(imageView);
        return this;
    }

    public ValueFrameAnimator removeAnim(ImageView imageView){
        if(mImageViewSet!=null&&imageView!=null){
            mImageViewSet.remove(imageView);
        }
        return this;
    }

   public boolean isStarted(){
        return valueAnimator!=null&&valueAnimator.isStarted();
   }

    public void start(){
        init();

        valueAnimator.start();
    }

    public ValueFrameAnimator setAnimListner(AnimListner animListner) {
        this.animListner = animListner;
        return this;
    }
    public void pause(){
        if(valueAnimator!=null&&!valueAnimator.isRunning()){
            valueAnimator.pause();

        }
    }

    public void cancle(){
        if(valueAnimator!=null&&!valueAnimator.isRunning()){
            valueAnimator.cancel();
        }
    }

    public void clearImage(){
        if(imageView!=null){
            imageView.setImageDrawable(null);
            imageView=null;
        }

        if(mImageViewSet!=null){
            for(ImageView img:mImageViewSet){
                if(img!=null){
                    img.setImageDrawable(null);
                }
            }
            mImageViewSet.clear();
        }
    }

    public void resume(){
        if(valueAnimator!=null&&!valueAnimator.isPaused()){
            valueAnimator.resume();
        }
    }

    public void reverse(){
        init();
        if(valueAnimator!=null&&valueAnimator.isPaused()){
            valueAnimator.pause();
        }
        valueAnimator.reverse();
    }

    public void release(){
        drawables=null;
        if(valueAnimator!=null&&valueAnimator.isStarted()){
            valueAnimator.cancel();
        }
        valueAnimator=null;
    }

    public interface AnimListner{
        public void start();
    }

}

