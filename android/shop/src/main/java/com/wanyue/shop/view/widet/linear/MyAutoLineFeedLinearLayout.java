package com.wanyue.shop.view.widet.linear;
 
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.wanyue.shop.R;

/**
 * @author Qzl
 * @desc 自定义自动换行LinearLayout
 * @email 2538096489@qq.com
 * @time 2019-02-25 16:02
 * @class hzz
 * @package com.gsww.hzz.uikit.view
 */
public class MyAutoLineFeedLinearLayout extends LinearLayout {
    //设置一行显示多少个，默认为三个
    int mAutoLineNum = 3;
    /**
     * 里面内容的行间距 下一行距离上一行的间距
     */
    float mAutoLineTop = 0;
    /**
     * 每一行显示的view的总宽度
     */
    private int childWidth = 0;
    /**
     * 屏幕的最大宽度
     */
    private int maxWidth = 0;


 
    public MyAutoLineFeedLinearLayout(Context context) {
        super(context);
    }
 
    public MyAutoLineFeedLinearLayout(Context context, int horizontalSpacing, int verticalSpacing) {
        super(context);
    }
 
    public MyAutoLineFeedLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        //获取自定义属性的值
        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.AutoLineFeedLinearLayout);
        mAutoLineNum = arr.getInt(R.styleable.AutoLineFeedLinearLayout_auto_line_num,4);
        mAutoLineTop = arr.getDimension(R.styleable.AutoLineFeedLinearLayout_auto_line_top,0);
        //释放资源
        arr.recycle();
    }
 
    /**
     * @desc 测量方法，计算显示这些view所需要的高度
     * @author 强周亮
     * @time 2019-02-26 09:48
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //获取屏幕的宽度
        maxWidth = MeasureSpec.getSize(widthMeasureSpec);
        int y = getTotalHeight();
        //设置容器所需的宽高
        setMeasuredDimension(maxWidth, y);
    }
 
    /**
     * @desc 获取布局总高度
     * @author 强周亮
     * @time 2019-02-26 11:48
     */
    private int getTotalHeight() {
        //每一次测量时，重新计算每行所有view的总宽度，用来做均分显示
        childWidth = 0;
        //获取总共的子view的个数
        int childCount = getChildCount();
        //计算子view所占的宽度
        int x = 0;
        //计算子view所占的高度
        int y = 0;
        //记录总共有多少行
        int row = 0;
        //记录是第几个孩子
        int indexChild = -1;
        for (int index = 0; index < childCount; index++){
            final View child = getChildAt(index);
            if (child.getVisibility() != View.GONE){
                indexChild++;
                child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
                //此处增加换行判断，用于计算所需的高度
                int width = child.getMeasuredWidth();
                if(width==0){
                   width=getMeasuredWidth()/mAutoLineNum;
                }

                int height = child.getMeasuredHeight();
                x += width;
                if (indexChild < mAutoLineNum) {
                    childWidth += width;
                };
                y = (row+1)*(int)(height+ mAutoLineTop);
                if ((indexChild != 0 && indexChild%mAutoLineNum== 0) || x > maxWidth){
                    x = width;
                    row++;
                    y = (row+1)*(int)(height+ mAutoLineTop);
                }
            }
        }
        return y;
    }
 
    /**
     * @desc 绘制要显示的view
     * @author 强周亮
     * @time 2019-02-26 09:53
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int x = 0,y,row  = 0,span = 0,maxWidth = r - 1;
        span = getSpan(span, maxWidth);
        childLayout(childCount, x, row, span, maxWidth);
    }
 
    /**
     * @desc 绘制每一个孩子
     * @author 强周亮
     * @time 2019-02-26 11:50
     */
    private void childLayout(int childCount, int x, int row, int span, int maxWidth) {
        int y;//记录是第几个孩子
        int indexChild = -1;
        for (int i = 0; i < childCount; i++) {
            final View child = this.getChildAt(i);
            if (child.getVisibility() != View.GONE){
                indexChild++;
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                x += width+span;
                y = (int) (row+1)*(int)(height+ mAutoLineTop);
                if ((indexChild != 0 && indexChild%mAutoLineNum== 0) || x > maxWidth){
                    x = width+span;
                    row++;
                    y = (int) (row+1)*(int)(height+ mAutoLineTop);
                }
                child.layout(x - width, y - height, x, y);
            }
        }
    }
 
    /**
     * @desc 获取每一行之间view的间距
     * @author 强周亮
     * @time 2019-02-26 11:49
     */
    private int getSpan(int span, int maxWidth) {
        if (childWidth < maxWidth){
            span = (maxWidth-childWidth)/(mAutoLineNum+1);
        }
        return span;
    }
}