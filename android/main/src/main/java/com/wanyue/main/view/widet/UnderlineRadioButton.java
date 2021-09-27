package com.wanyue.main.view.widet;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import com.wanyue.main.R;

public class UnderlineRadioButton extends androidx.appcompat.widget.AppCompatRadioButton {
    //Paint即画笔，在绘图过程中起到了极其重要的作用，画笔主要保存了颜色，
    //样式等绘制信息，指定了如何绘制文本和图形，画笔对象有很多设置方法，
    //大体上可以分为两类，一类与图形绘制相关，一类与文本绘制相关
    private  Paint paint;
    private  boolean selfCheck;
    private  int underline_width;
    //下划线高度
    private int underlineHeight = 0;
    //下划线颜色
    private int underLineColor;
    //通过new创建实例是调用这个构造函数
    //这种情况下需要添加额外的一些函数供外部来控制属性，如set*(...);
    public UnderlineRadioButton(Context context) {
        this(context, null);
    }
    //通过XML配置但不定义style时会调用这个函数
    public UnderlineRadioButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        //获取自定义属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UnderlineTextView);
        //获取具体属性值
        underLineColor = typedArray.getColor(R.styleable.UnderlineTextView_underline_color, getTextColors().getDefaultColor());
        underlineHeight = (int)typedArray.getDimension(R.styleable.UnderlineTextView_underline_height,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        selfCheck=typedArray.getBoolean(R.styleable.UnderlineTextView_self_check,true);
        underline_width= (int)typedArray.getDimension(R.styleable.UnderlineTextView_underline_width,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
    }
    //通过XML配置且定义样式时会调用这个函数
    public UnderlineRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    //防止下划线高度大到一定值时会覆盖掉文字，需从写此方法
    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom + underlineHeight);
    }

    //绘制下划线
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //设置下划线颜色
        if(!isChecked()){
            return;
        }
        if(paint==null){
           paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        }
        paint.setColor(underLineColor);
        //float left, float top, float right, float bottom

        int gravity=getGravity();
        if(gravity== Gravity.CENTER||gravity== Gravity.CENTER_HORIZONTAL){
            float width=getWidth();
            float lineWidth=underline_width;
            float offect=(width-lineWidth)/2;
            float start= offect;
            float end= (lineWidth+width)/2;
            //canvas.drawRoundRect(rect,5,5,paint);
            canvas.drawRoundRect(start, getHeight() - underlineHeight-1,end, getHeight(),1,1,paint);
           //canvas.drawRect(start, getHeight() - underlineHeight,end, getHeight(), paint);
        }else{
            canvas.drawRect(0, getHeight() - underlineHeight, getWidth(), getHeight(), paint);
        }
    }
    @Override
    public void toggle() {
        if(!selfCheck){
           super.toggle();
        }
    }


    public void judgeChecked(){
        setChecked(!isChecked());
    }


    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if(checked){
         setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }else{
            setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }
}
