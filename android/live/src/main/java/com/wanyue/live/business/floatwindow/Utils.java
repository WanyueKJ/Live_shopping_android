package com.wanyue.live.business.floatwindow;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class Utils {
   public  static   int subWidth;
   public static int subHeight;

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }



    public static ArrayList<FrameLayout.LayoutParams> initFloatParamList(Context context){
        int[]windowSize=getWindowSize(context);
        return initFloatParamList(context,windowSize[0],windowSize[1]);
    }



    public static ArrayList<FrameLayout.LayoutParams> initFloatParamList(Context context, int layoutWidth, int layoutHeight) {
        ArrayList<FrameLayout.LayoutParams> list = new ArrayList<FrameLayout.LayoutParams>(2);
        // 底部最大的布局
        FrameLayout.LayoutParams layoutParams0 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        list.add(layoutParams0);

        final int midMargin = Utils.dip2px(context, 10);
        final int lrMargin = Utils.dip2px(context, 15);
        final int bottomMargin = Utils.dip2px(context, 50);

        subWidth=Utils.dip2px(context, 120);
        subHeight = Utils.dip2px(context, 180);
     for (int i = 0; i < 1; i++) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(subWidth, subHeight);
            layoutParams.leftMargin = layoutWidth - lrMargin - subWidth;
            layoutParams.topMargin =lrMargin;
            //layoutParams.topMargin = layoutHeight - (bottomMargin + midMargin * (i + 1) + subHeight * i) - subHeight;
            list.add(layoutParams);
        }

      /* for (int i = 0; i < 3; i++) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(subWidth, subHeight);
            layoutParams.leftMargin = lrMargin;
            layoutParams.topMargin = layoutHeight - (bottomMargin + midMargin * (i + 1) + subHeight * i) - subHeight;
            list.add(layoutParams);
        }*/
        return list;
    }

    /**
     * 四宫格布局参数
     *
     * @param context
     * @param layoutWidth
     * @param layoutHeight
     * @return
     */
    public static ArrayList<RelativeLayout.LayoutParams> initGrid4Param(Context context, int layoutWidth, int layoutHeight) {
        int margin = dip2px(context, 10);
        int bottomMargin = dip2px(context, 50);

        ArrayList<RelativeLayout.LayoutParams> list = new ArrayList<>();
        int grid4W = (layoutWidth - margin * 2) / 2;
        int grid4H = (layoutHeight - margin * 2 - bottomMargin) / 2;

        RelativeLayout.LayoutParams layoutParams0 = new RelativeLayout.LayoutParams(grid4W, grid4H);
        layoutParams0.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams0.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams0.topMargin = margin;
        layoutParams0.leftMargin = margin;

        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(grid4W, grid4H);
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams1.topMargin = margin;
        layoutParams1.rightMargin = margin;

        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(grid4W, grid4H);
        layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams2.bottomMargin = margin + bottomMargin;
        layoutParams2.leftMargin = margin;

        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(grid4W, grid4H);
        layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams3.bottomMargin = margin + bottomMargin;
        layoutParams3.rightMargin = margin;

        list.add(layoutParams0);
        list.add(layoutParams1);
        list.add(layoutParams2);
        list.add(layoutParams3);
        return list;
    }

    /**
     * 九宫格布局参数
     *
     * @param context
     * @param layoutWidth
     * @param layoutHeight
     * @return
     */



    public static ArrayList<RelativeLayout.LayoutParams> initGrid9Param(Context context, int layoutWidth, int layoutHeight) {
        int margin = dip2px(context, 10);
        int bottomMargin = dip2px(context, 50);

        ArrayList<RelativeLayout.LayoutParams> list = new ArrayList<>();

        int grid9W = (layoutWidth - margin * 2) / 3;
        int grid9H = (layoutHeight - margin * 2 - bottomMargin) / 3;

        RelativeLayout.LayoutParams layoutParams0 = new RelativeLayout.LayoutParams(grid9W, grid9H);
        layoutParams0.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams0.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams0.topMargin = margin;
        layoutParams0.leftMargin = margin;

        RelativeLayout.LayoutParams layoutParams1 = new RelativeLayout.LayoutParams(grid9W, grid9H);
        layoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams1.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams1.topMargin = margin;

        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(grid9W, grid9H);
        layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams2.topMargin = margin;
        layoutParams2.rightMargin = margin;

        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(grid9W, grid9H);
        layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams3.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams3.leftMargin = margin;
        layoutParams3.topMargin = margin + grid9H;

        RelativeLayout.LayoutParams layoutParams4 = new RelativeLayout.LayoutParams(grid9W, grid9H);
        layoutParams4.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams4.topMargin = margin + grid9H;

        RelativeLayout.LayoutParams layoutParams5 = new RelativeLayout.LayoutParams(grid9W, grid9H);
        layoutParams5.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        layoutParams5.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams5.topMargin = margin + grid9H;
        layoutParams5.rightMargin = margin;

        RelativeLayout.LayoutParams layoutParams6 = new RelativeLayout.LayoutParams(grid9W, grid9H);
        layoutParams6.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams6.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams6.bottomMargin = margin + bottomMargin;
        layoutParams6.leftMargin = margin;

        RelativeLayout.LayoutParams layoutParams7 = new RelativeLayout.LayoutParams(grid9W, grid9H);
        layoutParams7.addRule(RelativeLayout.CENTER_HORIZONTAL);
        layoutParams7.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams7.bottomMargin = margin + bottomMargin;

        RelativeLayout.LayoutParams layoutParams8 = new RelativeLayout.LayoutParams(grid9W, grid9H);
        layoutParams8.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams8.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        layoutParams8.bottomMargin = margin + bottomMargin;
        layoutParams8.rightMargin = margin;

        list.add(layoutParams0);
        list.add(layoutParams1);
        list.add(layoutParams2);
        list.add(layoutParams3);
        list.add(layoutParams4);
        list.add(layoutParams5);
        list.add(layoutParams6);
        list.add(layoutParams7);
        list.add(layoutParams8);

        return list;
    }

    public static int[] getWindowSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        int[] sizeArray={width,height};
        return sizeArray;
    }


}
