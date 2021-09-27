package com.wanyue.shop.view.widet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.wanyue.shop.R;

public class RatingStar extends View {

  private int normalId;
  private int focusId;
  private Bitmap normalImg;
  private Bitmap focusImg;
  private int number;
  private int w1;
  private int h1;
  private int marginLeft;
  private int marginTop;
  private int marginBottom;
  private int marginRight;
  private int height;
  private int width;
  private int p;
  private float w0;
  private int position;
  private int mGrade;

  private OnRatioListner mOnRatioListner;
  private boolean isEnableSelect;
  private boolean isRight;


  public RatingStar(Context context) {
    this(context,null);
  }

  public RatingStar(Context context, @Nullable AttributeSet attrs) {
    this(context, attrs,0);
  }

  public RatingStar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatingStar);
    normalId = array.getResourceId(R.styleable.RatingStar_starNormal,0);
    focusId = array.getResourceId(R.styleable.RatingStar_starFocus,0);
    normalImg = BitmapFactory.decodeResource(getResources(), normalId);
    focusImg = BitmapFactory.decodeResource(getResources(), focusId);
    number = array.getInteger(R.styleable.RatingStar_starNumber,5);
    array.recycle();
    position = -1;
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    w1 = normalImg.getWidth();
    h1 = normalImg.getHeight();

    //中间间隔
    p = 30;
    marginTop = 20;
    marginBottom = 20;
    marginLeft = 20;
    marginRight = 20;
    height = h1 + marginTop + marginBottom;
    width = w1 *number+(number-1)*p +marginLeft+marginRight;
    setMeasuredDimension(width, height);
  }


  public void setNumber(int number) {
    this.number = number;
    requestLayout();
  }

  public void setRight(boolean right) {
    isRight = right;
  }

  public void setOnRatioListner(OnRatioListner onRatioListner) {
    mOnRatioListner = onRatioListner;
  }

  public void setNormalImg(Bitmap normalImg) {
    this.normalImg = normalImg;
  }

  public void setFocusImg(Bitmap focusImg) {
    this.focusImg = focusImg;
  }

  public void setEnableSelect(boolean enableSelect) {
    isEnableSelect = enableSelect;

  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    if(isRight){

      for (int i = 0; i < number; i++) {
        if (i <= position){
          canvas.drawBitmap(focusImg,i*w1+marginLeft+i*p,marginTop,null);
          mGrade = i+1;
        }else{
          /*if(!isEnableSelect){
            return;
          }*/
          canvas.drawBitmap(normalImg,i*w1+marginLeft+i*p,marginTop,null);
        }
      }
    }else{

      for (int i = 0; i < number; i++) {
        if (i <= position){
          canvas.drawBitmap(focusImg,i*w1+marginLeft+i*p,marginTop,null);
          mGrade = i+1;
        }else{
          /*if(!isEnableSelect){
            return;
          }*/
          canvas.drawBitmap(normalImg,i*w1+marginLeft+i*p,marginTop,null);
        }
      }
    }

    for (int i = 0; i < number; i++) {
      if (i <= position){
        canvas.drawBitmap(focusImg,i*w1+marginLeft+i*p,marginTop,null);
        mGrade = i+1;
      }else{
        /*if(!isEnableSelect){
          return;
        }*/
        canvas.drawBitmap(normalImg,i*w1+marginLeft+i*p,marginTop,null);
      }
    }
    Log.e("msg","我被调用了！");
  }


  public void setPosition(int position) {
    if(position<0||this.position==position){
      return;
    }
    this.position = position;
    invalidate();
  }

  public int getPosition() {
    return position;
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    float x = event.getX();//相对于控件自身的距离
    //event.getRawX() 相对于屏幕的距离
    switch (event.getAction()){
      case MotionEvent.ACTION_DOWN:
      case MotionEvent.ACTION_MOVE:
        if(!isEnableSelect){
          return true;
        }
        //case MotionEvent.ACTION_UP:
        w0 = getWidth()/number;
        position = (int) (x/w0);
        if(position<=-1){
          position=0;
          return true;
        }

        //性能优化，减少onDraw()调用
        if (mGrade == position+1){
          return true;
        }

        if(position>=number){
          position=number-1;
          return true;
        }

        if(mOnRatioListner!=null){
          mOnRatioListner.onCheck(position);
        }

        invalidate();
        break;
    }
    return true;
  }





  public static interface OnRatioListner{
    public void onCheck(int poisition);
  }


}