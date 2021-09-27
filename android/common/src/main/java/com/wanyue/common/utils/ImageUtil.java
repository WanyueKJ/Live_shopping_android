package com.wanyue.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.wanyue.common.CommonAppConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class ImageUtil {
    public static Drawable[] getDrawalesByResource(Context context,int...resourceArray){
        if(resourceArray==null) {
            return null;
        }
        int length=resourceArray.length;
        Drawable[] drawables=new Drawable[resourceArray.length];
        for(int i=0;i<length;i++){
            drawables[i]= ContextCompat.getDrawable(context,resourceArray[i]);
        }
        return drawables;
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache(); // 或者可以使用下面的方法　　　　 // view.setDrawingCacheEnabled(true);　　　　 // Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
        return bitmap;
    }

    public static Bitmap convertViewToBitmap2(View view) {
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache(); // 或者可以使用下面的方法　　　　 // view.setDrawingCacheEnabled(true);　　　　 // Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache());
        return bitmap;
    }


    public static Bitmap convertViewToBitmap(View view, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(bitmap));
        return bitmap;
    }

    public static void getScreenRectOfView(View view, Rect outRect) {
        int pos[] = new int[2];
        view.getLocationOnScreen(pos);
        outRect.set(pos[0], pos[1], pos[0] + view.getWidth(), pos[1] + view.getHeight());
    }









    public static String saveAlbum(Activity activity,Bitmap bitmap, String fileNmae) {
        File appDir = new File(CommonAppConfig.CAMERA_IMAGE_PATH);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        File file = new File(appDir, fileNmae);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            onSaveSuccess(activity,file);
            return file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    private static void onSaveSuccess(final Activity activity, final File file) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity. sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

            }
        });
    }







}
