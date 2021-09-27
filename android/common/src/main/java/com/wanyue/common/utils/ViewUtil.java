package com.wanyue.common.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tencent.cos.xml.utils.StringUtils;

public class ViewUtil {

  public static void setTextNoContentHide(TextView textView,String txt){
      if(textView==null) {
          return;
      }
      if (!TextUtils.isEmpty(txt)){
          textView.setText(txt);
          textView.setVisibility(View.VISIBLE);
      }
      else{
          textView.setVisibility(View.INVISIBLE);
      }
  }

    public static boolean equalsView(View view1,View view2){
      if(view1==null||view2==null||view1!=view2){
          return false;
      }
      return true;
    }




    public static void setTextNoContentGone(TextView textView,String txt){
        if(textView==null) {
            return;
        }
        if (!TextUtils.isEmpty(txt)){
            textView.setText(txt);
            textView.setVisibility(View.VISIBLE);
        }
        else{
            textView.setVisibility(View.GONE);
        }
    }


    public static void removeToParent(View view){
      if(view==null||view.getParent()==null){
          return;
      }
      ViewGroup vp= (ViewGroup) view.getParent();
      vp.removeView(view);
    }


    public static void scaleContents(Activity context,View paramView1,float scale) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(localDisplayMetrics);
        // 获得屏幕的宽高
        int i = localDisplayMetrics.widthPixels;
        int j = localDisplayMetrics.heightPixels;

        scaleViewAndChildren(paramView1, scale);
        Log.i("notcloud", "Scaling Factor=" + scale);
    }

    public static void scaleViewAndChildren(View paramView, float paramFloat) {

        ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
        if ((localLayoutParams.width != -1) && (localLayoutParams.width != -2)) {
            int width= (int) (paramFloat * localLayoutParams.width);
            localLayoutParams.width = width;
            L.e("size&& width=="+width);
        }
        if ((localLayoutParams.height != -1)
                && (localLayoutParams.height != -2)) {
            int height=(int) (paramFloat * localLayoutParams.height);
            L.e("size&& height=="+height);
            localLayoutParams.height = height;
        }

        if ((localLayoutParams instanceof ViewGroup.MarginLayoutParams)) {
            ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams) localLayoutParams;
            localMarginLayoutParams.leftMargin = ((int) (paramFloat * localMarginLayoutParams.leftMargin));
            localMarginLayoutParams.rightMargin = ((int) (paramFloat * localMarginLayoutParams.rightMargin));
            localMarginLayoutParams.topMargin = ((int) (paramFloat * localMarginLayoutParams.topMargin));
            localMarginLayoutParams.bottomMargin = ((int) (paramFloat * localMarginLayoutParams.bottomMargin));
        }
        paramView.setLayoutParams(localLayoutParams);
        paramView.setPadding((int) (paramFloat * paramView.getPaddingLeft()),
                (int) (paramFloat * paramView.getPaddingTop()),
                (int) (paramFloat * paramView.getPaddingRight()),
                (int) (paramFloat * paramView.getPaddingBottom()));
        if ((paramView instanceof TextView)) {
            TextView localTextView = (TextView) paramView;
            Log.d("Calculator",
                    "Scaling text size from " + localTextView.getTextSize()
                            + " to " + paramFloat * localTextView.getTextSize());
            localTextView.setTextSize(paramFloat * localTextView.getTextSize());
        }
        ViewGroup localViewGroup = null;
        if ((paramView instanceof ViewGroup)) {
            localViewGroup = (ViewGroup) paramView;
        }

        if (localViewGroup != null) {
            System.out.println("子元素的数量" + localViewGroup.getChildCount());
            for (int i = 0;; i++) {
                if (i >= localViewGroup.getChildCount()) {
                    break;
                }
                scaleViewAndChildren(localViewGroup.getChildAt(i), paramFloat);
            }
        }
    }


    public static void setChecked(CompoundButton button,boolean isCheck){
        if(button==null||button.isChecked()==isCheck){
            return;
        }
        button.setChecked(isCheck);
    }

    public static void setVisibility(View view,int visibly){
        if(view==null){
            return;
        }
        int state=view.getVisibility();
        if(state==visibly){
            return;
        }
        view.setVisibility(visibly);
    }

    public static void setTextAndViewsible(TextView view,String text){
        if(view==null){
            return;
        }


        view.setVisibility(View.VISIBLE);
        view.setText(text);
    }

    public static void setTextAndViewsibleByNumber(TextView view,Integer number){
        if(number==null){
            return;
        }
        if(number==0){
            setVisibility(view,View.INVISIBLE);
        }else{
            view.setText(Integer.toString(number));
            setVisibility(view,View.VISIBLE);
        }
    }




    public static void setEditextEnable(EditText editText){
        editText.setEnabled(true);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        editText.requestFocusFromTouch();
    }

    public static void setEditextEnable2(EditText editText){
        editText.setEnabled(true);
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);

    }

    public static <T> T getTag(View v,Class<T>cs) {
       Object object=v.getTag();
       if(object!=null&&object.getClass()==cs){
           T t= (T) object;
           return t;
       }
       return null;
    }

    public static void setAlpha(View view,float alpha) {
        if(view==null||view.getAlpha()==alpha){
            return;
        }
        if(alpha>1){   //大于1过滤
           alpha=1F;
        }
        view.setAlpha(alpha);

    }
}
