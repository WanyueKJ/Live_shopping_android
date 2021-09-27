package com.wanyue.common.custom;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ShadowDrawable extends Drawable {

 private Paint mPaint;
 private int mShadowRadius;  // 阴影圆角
 private int mShape;         // 背景形状
 private int mShapeRadius;   // 背景圆角
 private int mOffsetX;       // 阴影的水平偏移量
 private int mOffsetY;       // 阴影的垂直偏移量
 private int mBgColor[];     // 背景颜色
 private RectF mRect;

 public final static int SHAPE_ROUND = 1;    // 表示圆角矩形
 public final static int SHAPE_CIRCLE = 2;   // 表示圆

 private ShadowDrawable(int shape, int[] bgColor, int shapeRadius, int shadowColor, int shadowRadius, int offsetX, int offsetY) {
   this.mShape = shape;
   this.mBgColor = bgColor;
   this.mShapeRadius = shapeRadius;
   this.mShadowRadius = shadowRadius;
   this.mOffsetX = offsetX;
   this.mOffsetY = offsetY;
   mPaint = new Paint();
   mPaint.setColor(Color.TRANSPARENT);
   mPaint.setAntiAlias(true);
   mPaint.setShadowLayer(shadowRadius, offsetX, offsetY, shadowColor);
     mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP));
 }

 @Override
 public void setBounds(int left, int top, int right, int bottom) {
   super.setBounds(left, top, right, bottom);
   mRect = new RectF(left + mShadowRadius - mOffsetX, top + mShadowRadius - mOffsetY, right - mShadowRadius - mOffsetX,
       bottom - mShadowRadius - mOffsetY);
 }

 @Override
 public void draw(@NonNull Canvas canvas) {
   if (mShape == SHAPE_ROUND) {
     canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, mPaint);
     Paint newPaint = new Paint();

     if (mBgColor != null) {
       if (mBgColor.length == 1) {
         newPaint.setColor(mBgColor[0]);
       } else {
         newPaint.setShader(new LinearGradient(mRect.left, mRect.height() / 2, mRect.right, mRect.height() / 2, mBgColor,
             null, Shader.TileMode.CLAMP));
       }
     }
     newPaint.setAntiAlias(true);
     canvas.drawRoundRect(mRect, mShapeRadius, mShapeRadius, newPaint);
   } else {
     canvas.drawCircle(mRect.centerX(), mRect.centerY(), Math.min(mRect.width(), mRect.height())/ 2, mPaint);
   }
 }

 @Override
 public void setAlpha(int alpha) {
   mPaint.setAlpha(alpha);
 }

 @Override
 public void setColorFilter(@Nullable ColorFilter colorFilter) {
   mPaint.setColorFilter(colorFilter);
 }

 @Override
 public int getOpacity() {
   return PixelFormat.TRANSLUCENT;
 }
}