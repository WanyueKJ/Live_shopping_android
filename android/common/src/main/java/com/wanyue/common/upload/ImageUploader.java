package com.wanyue.common.upload;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.api.CommonAPI;
import com.wanyue.common.utils.ListUtil;
import java.io.File;
import java.util.List;
import java.util.concurrent.Callable;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

public class ImageUploader {
    private Luban.Builder mLubanBuilder;
    private Context mContext;
    private Function<JSONObject, String>mTransFromFunction;

    public ImageUploader(Context context) {
        mContext = context;
    }

    /*压缩图片*/
    public Observable<List<File>> compress(List<String>pathList){
      return  Observable.just(pathList).observeOn(Schedulers.io()).map(new Function<List<String>, List<File>>() {
            @Override
            public List<File> apply(List<String> list) throws Exception {
                initLubanBuilder();
                return mLubanBuilder.load(list).get();
            }
        }).observeOn(AndroidSchedulers.mainThread());
    }



    private void initLubanBuilder() {
        mLubanBuilder=Luban.with(mContext)
                .ignoreBy(8)//8k以下不压缩
                .setTargetDir(CommonAppConfig.CAMERA_IMAGE_PATH);
    }


    /*打包上传*/
    public  Observable<JSONObject>uploadFileArray(List<File> fileList){
        int size= ListUtil.getSize(fileList);
        Observable<JSONObject>[]array=new Observable[size];
        for(int i=0;i<size;i++){
            File file=fileList.get(i);
            array[i]= CommonAPI.upload(file);
        }
      return Observable.mergeArray(array);
    }


    /*压缩打包上传*/
    public  Observable<String>uploadFileArraycompress(List<String>pathList){
        if(mTransFromFunction==null){
           mTransFromFunction=new Function<JSONObject, String>() {
               @Override
               public String apply(JSONObject jsonObject) throws Exception {
                   if(jsonObject!=null){
                     return jsonObject.getString("url");
                   }
                   return "";
               }
           };
        }
      return  compress(pathList).flatMap(new Function<List<File>, ObservableSource<JSONObject>>() {
            @Override
            public ObservableSource<JSONObject> apply(List<File> fileList) throws Exception {
                return uploadFileArray(fileList);
            }
        }).map(mTransFromFunction);
    }


    /*合并成带逗号的字符串*/
    public Single<StringBuilder> collect(Observable<String>observable){
       return observable.collect(new Callable<StringBuilder>() {
            @Override
            public StringBuilder call() throws Exception {
                return new StringBuilder();
            }
        }, new BiConsumer<StringBuilder, String>() {
            @Override
            public void accept(StringBuilder builder, String url) throws Exception {
                if(!TextUtils.isEmpty(url)){
                    builder.append(url).append(",");
                }
            }
        }).onErrorReturn(new Function<Throwable, StringBuilder>() {
            @Override
            public StringBuilder apply(Throwable throwable) throws Exception {
                return new StringBuilder();
            }
        });
    }

    /*----FileBundle-----*/


    public  Observable<List<FileBundle>> compressFileBundle(List<FileBundle>pathList){
      return   Observable.just(pathList).observeOn(Schedulers.io()).map(new Function<List<FileBundle>, List<FileBundle>>() {
            @Override
            public List<FileBundle> apply(List<FileBundle> fileBundles) throws Exception {
                initLubanBuilder();
                for(FileBundle fileBundle:fileBundles){
                    File file=fileBundle.file;
                    if(file!=null){
                       file= mLubanBuilder.load(file).get().get(0);
                       fileBundle.file=file;
                    }
                }
                return fileBundles;
            }
        });
    }

    public  Observable<FileBundle>uploadFileBundleArray(List<FileBundle> bundleList){
        int size= ListUtil.getSize(bundleList);
        Observable<FileBundle>[]array=new Observable[size];
        for(int i=0;i<size;i++){
            FileBundle bundle=bundleList.get(i);
            array[i]= CommonAPI.upload(bundle);
        }
        return Observable.mergeArray(array);
    }




    public void release(){
        mContext=null;
    }

}
