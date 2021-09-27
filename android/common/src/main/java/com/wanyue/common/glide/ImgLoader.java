package com.wanyue.common.glide;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.wanyue.common.R;

import java.io.File;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * Created by  on 2017/8/9.
 */

public class ImgLoader {

    private static final boolean SKIP_MEMORY_CACHE = false;

    private static BlurTransformation sBlurTransformation;

    static {
        sBlurTransformation= new BlurTransformation(40);
    }


    public static void display(Context context, String url, ImageView imageView) {
        if(context==null){
            return;
        }
        //url=url.replace("https","http");
        Glide.with(context).asDrawable().load(url).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).skipMemoryCache(SKIP_MEMORY_CACHE).into(imageView);
    }

    public static void displayGif(Context context, String url, ImageView imageView) {
        if(context==null){
            return;
        }
        Glide.with(context).asGif().load(url).skipMemoryCache(SKIP_MEMORY_CACHE).into(imageView);
    }

    public static void displayWithError(Context context, String url, ImageView imageView, int errorRes) {
        if(context==null){
            return;
        }
        Glide.with(context).asDrawable().load(url).skipMemoryCache(SKIP_MEMORY_CACHE).error(errorRes).into(imageView);
    }

    public static void displayAvatar(Context context, String url, ImageView imageView) {
        if(context==null){
            return;
        }
        displayWithError(context, url, imageView, R.mipmap.icon_avatar_placeholder);
    }

    public static void display(Context context, File file, ImageView imageView) {
        if(context==null){
            return;
        }
        Glide.with(context).asDrawable().load(file).skipMemoryCache(SKIP_MEMORY_CACHE).into(imageView);
    }

    public static void display(Context context, int res, ImageView imageView) {
        if(context==null){
            return;
        }
        Glide.with(context).asDrawable().load(res).skipMemoryCache(SKIP_MEMORY_CACHE).into(imageView);
    }

    public static void clear(Context context, ImageView imageView) {
        Glide.with(context).clear(imageView);
    }

    public static void clearMemory(Context context) {
        Glide.get(context).clearMemory();

    }

    /**
     * 显示视频封面缩略图
     */

    public static void displayVideoThumb(Context context, String videoPath, ImageView imageView) {
        if(context==null){
            return;
        }
        Glide.with(context).asDrawable().load(Uri.fromFile(new File(videoPath))).skipMemoryCache(SKIP_MEMORY_CACHE).into(imageView);
    }

    public static void displayVideoThumbRemote(Context context, String videoPath, ImageView imageView) {
        if(context==null){
            return;
        }
        Glide.with(context).asDrawable().load(Uri.parse(videoPath)).skipMemoryCache(SKIP_MEMORY_CACHE).into(imageView);
    }

    public static void displayDrawable(Context context, String url, final DrawableCallback callback) {
        if(context==null){
            return;
        }
        Glide.with(context).asDrawable().load(url).skipMemoryCache(SKIP_MEMORY_CACHE).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                if (callback != null) {
                    callback.onLoadSuccess(resource);
                }
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                if (callback != null) {
                    callback.onLoadFailed();
                }
            }
        });
    }


    public static void display(Context context, String url, ImageView imageView, int placeholderRes) {
        if(context==null){
            return;
        }
        Glide.with(context).asDrawable().load(url).skipMemoryCache(SKIP_MEMORY_CACHE).placeholder(placeholderRes).into(imageView);
    }

    /**
     * 显示模糊的毛玻璃图片
     */
    public static void displayBlur(Context context, String url, ImageView imageView) {
        if (context == null) {
            return;
        }
        Glide.with(context).asDrawable().load(url)
                .skipMemoryCache(SKIP_MEMORY_CACHE)
                .apply(RequestOptions.bitmapTransform(sBlurTransformation))
                .into(imageView);
    }



    public interface DrawableCallback {
        void onLoadSuccess(Drawable drawable);

        void onLoadFailed();
    }


}
