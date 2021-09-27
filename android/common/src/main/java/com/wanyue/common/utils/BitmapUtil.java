package com.wanyue.common.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.load.engine.Resource;
import com.wanyue.common.CommonAppConfig;
import com.wanyue.common.CommonApplication;
import com.wanyue.common.R;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;

import static com.makeramen.roundedimageview.RoundedDrawable.drawableToBitmap;

/**
 * Created by  on 2018/6/22.
 */

public class BitmapUtil {

    private static BitmapUtil sInstance;
    private Resources mResources;
    private BitmapFactory.Options mOptions;

    private BitmapUtil() {
        mResources = CommonApplication.sInstance.getResources();
        mOptions = new BitmapFactory.Options();
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        mOptions.inDither=true;
        mOptions.inSampleSize = 1;
    }

    public static BitmapUtil getInstance() {
        if (sInstance == null) {
            synchronized (BitmapUtil.class) {
                if (sInstance == null) {
                    sInstance = new BitmapUtil();
                }
            }
        }
        return sInstance;
    }


    public Bitmap decodeBitmap(int imgRes) {
        Bitmap bitmap = null;
        try {
            byte[] bytes = IOUtils.toByteArray(mResources.openRawResource(imgRes));
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, mOptions);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new SoftReference<>(bitmap).get();
    }

    /**
     * 把Bitmap保存成图片文件
     *
     * @param bitmap
     */
    public String saveBitmap(Bitmap bitmap) {
        String path = null;
        File dir = new File(CommonAppConfig.CAMERA_IMAGE_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File imageFile = new File(dir, DateFormatUtil.getCurTimeString() + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            path = imageFile.getAbsolutePath();
        } catch (Exception e) {
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
        return path;
    }


    /**根据指定的宽度比例值和高度比例值进行缩放*/
    public static Bitmap bitmapZoomByScale(Bitmap srcBitmap, float scaleWidth, float scaleHeight) {
        int width = srcBitmap.getWidth();
        int height = srcBitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix, true);
        if(bitmap != null) {
            return bitmap;
        }else {
            return srcBitmap;
        }
    }

    public static Bitmap thumbImageWithMatrix(Resources resource, int resouceId, float destWidth, float destHeight) {
        Bitmap bitmapOrg = BitmapFactory.decodeResource(resource, resouceId);
        float bitmapOrgW = bitmapOrg.getWidth();
        float bitmapOrgH = bitmapOrg.getHeight();

        float bitmapNewW = (int) destWidth;
        float bitmapNewH = (int) destHeight;

        float scaleW=bitmapNewW/bitmapOrgW;
        float scaleH=bitmapNewH/bitmapOrgH;
        float realScale=0;
        if(scaleW>scaleH){
            realScale=scaleH;
        }else{
            realScale=scaleW;
        }

        Matrix matrix = new Matrix();
        matrix.postScale(realScale, realScale);

        Bitmap newBitMap= Bitmap.createBitmap(bitmapOrg, 0, 0, (int) bitmapOrgW, (int) bitmapOrgH, matrix, true);
        bitmapOrg.recycle();
        return newBitMap;
    }


    public static Bitmap getBitmapFromResource(Context context, int id, int height, int width){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;//只读取图片，不加载到内存中
        BitmapFactory.decodeResource(context.getResources(), id, options);
        options.inSampleSize = calculateSampleSize(height, width, options);
        options.inJustDecodeBounds = false;//加载到内存中
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), id, options);
        return bitmap;
    }

    /**
     * 计算所需图片的缩放比例
     * @param height	高度
     * @param width		宽度
     * @param options	options选项
     * @return
     */
    private static int calculateSampleSize(int height, int width, BitmapFactory.Options options){
        int realHeight = options.outHeight;
        int realWidth = options.outWidth;
        int heigthScale = realHeight / height;
        int widthScale = realWidth / width;
        if(widthScale > heigthScale){
            return widthScale;
        }else{
            return heigthScale;
        }
    }

    public static Drawable zoomDrawable(Resources resources,int id,int h) {
        Drawable drawable= resources.getDrawable(id);
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        float scale=(float) h/(float)height;
        drawable.setBounds(0,0, (int) (width*scale),h);
       return drawable;
    }

    public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }

    private static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }
}
