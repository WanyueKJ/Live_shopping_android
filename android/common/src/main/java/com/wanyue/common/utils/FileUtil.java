package com.wanyue.common.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.text.TextUtils;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.upload.UploadBean;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by  on 2018/10/20.
 */

public class FileUtil {


    /**
     * 把字符串保存成文件
     */

    public static void saveStringToFile(File dir, String content, String fileName) {
        PrintWriter writer = null;
        try {
            FileOutputStream os = new FileOutputStream(new File(dir, fileName));
            writer = new PrintWriter(os);
            writer.write(content);
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public  static String saveVideoCoverPath(String videoPath){
        if(TextUtils.isEmpty(videoPath)) {
            return null;
        }

        Bitmap bitmap=null;
        MediaMetadataRetriever mmr = null;
        try {
            mmr = new MediaMetadataRetriever();
            mmr.setDataSource(videoPath);
            bitmap = mmr.getFrameAtTime(0, MediaMetadataRetriever.OPTION_CLOSEST);
        } catch (Exception e) {
            bitmap = null;
            e.printStackTrace();
        } finally {
            if (mmr != null) {
                mmr.release();
            }
        }
        final String coverImagePath = videoPath.replace(".mp4", ".jpg");
        File imageFile = new File(coverImagePath);
        FileOutputStream fos = null;
        try {
            if(imageFile.exists()){
                imageFile.delete();
                imageFile.createNewFile();
            }
            fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            imageFile = null;
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (bitmap != null) {
            bitmap.recycle();
        }

        if (imageFile == null){
            return null;
        }
        return coverImagePath;
    }


    public static Observable<Boolean>comPressorVideoFile(final Context context,UploadBean uploadBean){
       /* if(uploadBean==null||uploadBean.getOriginFile()==null){
            return Observable.just(false);
        }
      return Observable.just(uploadBean).subscribeOn(Schedulers.io()).subscribeOn(AndroidSchedulers.mainThread()).map(new Function<UploadBean, Boolean>() {
            @Override
            public Boolean apply(UploadBean uploadBean) throws Exception {
                String filePath= SiliCompressor.with(context).compressVideo(uploadBean.getOriginFile().getAbsolutePath(), CommonAppConfig.VIDEO_PATH);
                if(!TextUtils.isEmpty(filePath)){
                    uploadBean.setOriginFile(new File(filePath));
                }
                return !TextUtils.isEmpty(filePath);
            }
        });*/
       return null;
    }

}
