package com.wanyue.common.custom;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.OverScroller;
import android.widget.Scroller;

public class ZoomView extends AppCompatImageView implements View.OnTouchListener,
		ViewTreeObserver.OnGlobalLayoutListener {

	/**
	 * Interface definition for a callback to be invoked when the Photo is
	 * tapped with a single tap.
	 * 
	 * @author tomasz.zawada@gmail.com
	 */
	public static interface OnPhotoTapListener {
		/**
		 * A callback to receive where the user taps on a photo. You will only
		 * receive a callback if the user taps on the actual photo, tapping on
		 * 'whitespace' will be ignored.
		 * 
		 * @param view
		 *            - View the user tapped.
		 * @param x
		 *            - where the user tapped from the of the Drawable, as
		 *            percentage of the Drawable width.
		 * @param y
		 *            - where the user tapped from the top of the Drawable, as
		 *            percentage of the Drawable height.
		 */
		public void onPhotoTap(View view, float x, float y);
	}

	/**
	 * Interface definition for a callback to be invoked when the ImageView is
	 * tapped with a single tap.
	 * 
	 * @author tomasz.zawada@gmail.com
	 */
	public static interface OnViewTapListener {
		/**
		 * A callback to receive where the user taps on a ImageView. You will
		 * receive a callback if the user taps anywhere on the view, tapping on
		 * 'whitespace' will not be ignored.
		 * 
		 * @param view
		 *            - View the user tapped.
		 * @param x
		 *            - where the user tapped from the left of the View.
		 * @param y
		 *            - where the user tapped from the top of the View.
		 */
		public void onViewTap(View view, float x, float y);
	}
	public interface SingleTapConfirmedListener {
		public void onSingleTapConfirmed();
	}

	/**
	 * 
	 * The MultiGestureDetector manages the multi-finger pinch zoom, pan and tap
	 * 
	 * @author tomasz.zawada@gmail.com
	 * 
	 */
	private class MultiGestureDetector extends
			GestureDetector.SimpleOnGestureListener implements
			ScaleGestureDetector.OnScaleGestureListener {
		private final ScaleGestureDetector scaleGestureDetector;
		private final GestureDetector gestureDetector;

		private VelocityTracker velocityTracker;
		private boolean isDragging;

		private float lastTouchX;
		private float lastTouchY;
		private float lastPointerCount;
		private final float scaledTouchSlop;
		private final float scaledMinimumFlingVelocity;
		
		private final Matrix doubleTapMatrix = new Matrix();
		
		private Float lastRotation;//上一次的角度
		private float startRotation;//开始时的角度
		private float startScale;//开始时的缩放
		private boolean isRepeatPointerDown = false;//第二个手指是否是抬起后重新按下

		public MultiGestureDetector(Context context) {
			scaleGestureDetector = new ScaleGestureDetector(context, this);

			gestureDetector = new GestureDetector(context, this);
			gestureDetector.setOnDoubleTapListener(this);
			final ViewConfiguration configuration = ViewConfiguration
					.get(context);
			scaledMinimumFlingVelocity = configuration
					.getScaledMinimumFlingVelocity();
			scaledTouchSlop = configuration.getScaledTouchSlop();
		}
	

		public boolean isScaling() {
			return scaleGestureDetector.isInProgress();
		}

		public boolean onTouchEvent(MotionEvent event) {
			if(!isZoomEnabled){
				return false;
			}
			if (gestureDetector.onTouchEvent(event)) {
				return true;
			}
			scaleGestureDetector.onTouchEvent(event);
			if(isZoomEnabled){
			/*
			 * Get the center x, y of all the pointers
			 */
			float x = 0, y = 0;
			final int pointerCount = event.getPointerCount();
			for (int i = 0; i < pointerCount; i++) {
				x += event.getX(i);
				y += event.getY(i);
			}
			x = x / pointerCount;
			y = y / pointerCount;

			/*
			 * If the pointer count has changed cancel the drag
			 */
			if (pointerCount != lastPointerCount) {
				isDragging = false;
				if (velocityTracker != null) {
					velocityTracker.clear();
				}
				lastTouchX = x;
				lastTouchY = y;
			}
			lastPointerCount = pointerCount;
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				if (velocityTracker == null) {
					velocityTracker = VelocityTracker.obtain();
				} else {
					velocityTracker.clear();
				}
				velocityTracker.addMovement(event);

				lastTouchX = x;
				lastTouchY = y;
				lastRotation = null;
				isDragging = false;
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				startRotation = rotation(event);
				isDragging = false;
				break;
			case MotionEvent.ACTION_MOVE: {
				final float dx = x - lastTouchX, dy = y - lastTouchY;
				//只有一个手指的时候才可以拖拽
				if (isDragging == false && event.getPointerCount() == 1) {
					// Use Pythagoras to see if drag length is larger than
					// touch slop
					isDragging = Math.sqrt((dx * dx) + (dy * dy)) >= scaledTouchSlop;
				}
				
				if(event.getPointerCount() > 1){
					float rotation = rotation(event);
					if(scaleNoRotation && isNewGesture){//如果只能缩放的话 判断是否进入旋转状态
						if(Math.abs(rotation - startRotation) > 10){
							scaleNoRotation = false;
							isNewGesture = false;
							startRotationMatrix.set(getImageMatrix());
						}
					}
					if(!scaleNoRotation){
						if(lastRotation != null){
							if(isRepeatPointerDown){
								lastRotation = rotation;
								isRepeatPointerDown = false;
							}else{
								float deltaRotation = rotation - lastRotation;
								if(Math.abs(deltaRotation) > 1){
									if(getDrawable() != null){
										deltaDegree += deltaRotation;
										showMatrix.set(getImageMatrix());
										showMatrix.postRotate(deltaRotation, getWidth()/2,getHeight()/2);
										setImageMatrix(showMatrix);
									}
									lastRotation = rotation;
								}
							}
						}else{
							lastRotation = rotation;
						}
					}
				}

				if (isDragging) {
					if (getDrawable() != null) {
						showMatrix.set(getImageMatrix());
						showMatrix.postTranslate(dx, dy);
						backMatrix.reset();
						checkMatrixBounds(showMatrix);
						showMatrix.postConcat(backMatrix);
						setImageMatrix(showMatrix);

						/**
						 * Here we decide whether to let the ImageView's parent
						 * to start taking over the touch event.
						 * 
						 * First we check whether this function is enabled. We
						 * never want the parent to take over if we're scaling.
						 * We then check the edge we're on, and the direction of
						 * the scroll (i.e. if we're pulling against the edge,
						 * aka 'overscrolling', let the parent take over).
						 */
						if (allowParentInterceptOnEdge
								&& !multiGestureDetector.isScaling()) {
							if ((scrollEdge == EDGE_BOTH)
									|| ((scrollEdge == EDGE_LEFT) && (dx >= 1f))
									|| ((scrollEdge == EDGE_RIGHT) && (dx <= -1f))) {

								if (getParent() != null) {
									getParent()
											.requestDisallowInterceptTouchEvent(
													false);
								}
							}
						}
					}

					lastTouchX = x;
					lastTouchY = y;

					if (velocityTracker != null) {
						velocityTracker.addMovement(event);
					}
				}
				break;
			}
			case MotionEvent.ACTION_POINTER_UP:
				isDragging = true;
				isRepeatPointerDown = true;
				break;
			case MotionEvent.ACTION_UP: {
				isRepeatPointerDown = false;
				if (isDragging) {
					lastTouchX = x;
					lastTouchY = y;

					// Compute velocity within the last 1000ms
					if (velocityTracker != null) {
						velocityTracker.addMovement(event);
						velocityTracker.computeCurrentVelocity(1000);
						final float vX = velocityTracker.getXVelocity(), vY = velocityTracker
								.getYVelocity();
						
						// If the velocity is greater than minVelocity perform
						// a fling
						if ((Math.max(Math.abs(vX), Math.abs(vY)) >= scaledMinimumFlingVelocity)
								&& (getDrawable() != null)) {
							currentFlingRunnable = new FlingRunnable(getContext());
							currentFlingRunnable.fling(getWidth(), getHeight(),
									(int) -vX, (int) -vY);
							post(currentFlingRunnable);
						}
					}

				}
			}
			case MotionEvent.ACTION_CANCEL:
				lastPointerCount = 0;
				if (velocityTracker != null) {
					velocityTracker.recycle();
					velocityTracker = null;
				}
				break;
			}
			}
			return true;
		}

		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			if(canScale){
				float scale = getScaleWithoutRotation();
				float scaleFactor = detector.getScaleFactor();
				float maxScale = horizontalMaxScale;
				float minScale = horizontalMinScale;
				float curD = changeDegreeIn360(curDegree);
				if(!isDegreeHorizontal(curD)){
					maxScale = verticalMaxScale;
					minScale = verticalMinScale;
				}
				if(scaleNoRotation && getDrawable() != null && !((( scale >= maxScale) && (scaleFactor > 1f)) || ((scale < minScale) && (scaleFactor <1f)))){
					if(isNewGesture){
						//如果缩放超过开始的十分之一就算这次手势是缩放，不能旋转
						if(Math.abs(scale - startScale) > startScale * 0.1 ){
							isNewGesture = false;
						}
					}
					showMatrix.set(getImageMatrix());
					showMatrix.postScale(scaleFactor, scaleFactor, detector.getFocusX(), detector.getFocusY());
					backMatrix.reset();
					checkMatrixBounds(showMatrix);
					setImageMatrix(showMatrix);
				}
			}
			return true;
		}

		@Override
		public boolean onScaleBegin(ScaleGestureDetector detector) {
			startScale = getScaleWithoutRotation();
			return true;
		}

		@Override
		public void onScaleEnd(ScaleGestureDetector detector) {
		}

		@Override
		public boolean onDoubleTap(MotionEvent event) {
			if(canDoubleTap){
				try {
					float scale = getScaleWithoutRotation();
					float x = event.getX();
					float y = event.getY();
	
					float midScale = horizontalMidScale;
					float maxScale = horizontalMaxScale;
					float minScale = horizontalNormalScale;
					if(!isTargetDegreeHorizontal(startDegree)){
						midScale = verticalMidScale;
						maxScale = verticalMaxScale;
						minScale = verticalNormalScale;
					}
					RectF rect = getDisplayRect();
					doubleTapMatrix.reset();
					float[] point = new float[]{rect.centerX()-x, rect.centerY()-y};
					if(scale < midScale){
						doubleTapMatrix.postScale(midScale/scale, midScale/scale);
						doubleTapMatrix.mapPoints(point);
//						ScrollState start = new ScrollState(rect.centerX(), rect.centerY(), scale, scale, curDegree);
//						ScrollState end = new ScrollState(point[0]+x, point[1]+y, midScale, midScale, startDegree);
//						ImageScroller scroller = new ImageScroller(start, end, 300, getDrawable(), this, bitmapChangeListener);
						post(new BackRunnable(startDegree, curDegree, midScale, scale, rect.centerX(), rect.centerY(),point[0]+x, point[1]+y));
					}else if( scale >= midScale && scale < maxScale){
						doubleTapMatrix.postScale(maxScale/scale, maxScale/scale);
						doubleTapMatrix.mapPoints(point);
						post(new BackRunnable(startDegree, curDegree, maxScale, scale, rect.centerX(), rect.centerY(),point[0]+x, point[1]+y));
					}else{
						doubleTapMatrix.postScale(minScale/scale, minScale/scale);
						doubleTapMatrix.mapPoints(point);
						post(new BackRunnable(startDegree, curDegree, minScale, scale, rect.centerX(), rect.centerY(),point[0]+x, point[1]+y));
					}
				} catch (Exception e) {
				}
			}
			return true;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent event) {
			return false;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent event) {
			if(mSingleTapConfirmedListener != null) {
                mSingleTapConfirmedListener.onSingleTapConfirmed();
            }
			if(isFling){
				
				return false;
			}
			if(canSingleTap){
				if (photoTapListener != null) {
					final RectF displayRect = getDisplayRect();
	
					if (null != displayRect) {
						final float x = event.getX(), y = event.getY();
	
						// Check to see if the user tapped on the photo
						if (displayRect.contains(x, y)) {
							float xResult = (x - displayRect.left)
									/ displayRect.width();
							float yResult = (y - displayRect.top)
									/ displayRect.height();
	
							photoTapListener.onPhotoTap(ZoomView.this, xResult,
									yResult);
							return true;
						}
					}
				}
				if (viewTapListener != null) {
					viewTapListener.onViewTap(ZoomView.this, event.getX(),
							event.getY());
				}
			}
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
			if (longClickListener != null) {
				longClickListener.onLongClick(ZoomView.this);
			}
		}
	}

	/**
	 * 
	 * The ScrollerProxy encapsulates the Scroller and OverScroller classes.
	 * OverScroller is available since API 9.
	 * 
	 * @author tomasz.zawada@gmail.com
	 * 
	 */
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	private static class ScrollerProxy {

		private boolean isOld;
		private Object scroller;

		public ScrollerProxy(Context context) {
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
				isOld = true;
				scroller = new Scroller(context);
			} else {
				isOld = false;
				scroller = new OverScroller(context);
			}
		}

		public boolean computeScrollOffset() {
			return isOld ? ((Scroller) scroller).computeScrollOffset()
					: ((OverScroller) scroller).computeScrollOffset();
		}

		public void fling(int startX, int startY, int velocityX, int velocityY,
				int minX, int maxX, int minY, int maxY, int overX, int overY) {

			if (isOld) {
				((Scroller) scroller).fling(startX, startY, velocityX,
						velocityY, minX, maxX, minY, maxY);
			} else {
				((OverScroller) scroller).fling(startX, startY, velocityX,
						velocityY, minX, maxX, minY, maxY, overX, overY);
			}
		}

		public void forceFinished(boolean finished) {
			if (isOld) {
				((Scroller) scroller).forceFinished(finished);
			} else {
				((OverScroller) scroller).forceFinished(finished);
			}
		}

		public int getCurrX() {
			return isOld ? ((Scroller) scroller).getCurrX()
					: ((OverScroller) scroller).getCurrX();
		}

		public int getCurrY() {
			return isOld ? ((Scroller) scroller).getCurrY()
					: ((OverScroller) scroller).getCurrY();
		}
	}

	private static final int EDGE_NONE = -1;
	private static final int EDGE_LEFT = 0;
	private static final int EDGE_RIGHT = 1;
	private static final int EDGE_BOTH = 2;

	/**
	 * 缩放比例，这里的比例并不是以图片自身大小为基准，而是以图片fill_center时的大小为基准。
	 * 这里只是一个比例系数
	 */
	public static float DEFAULT_MAX_SCALE = 3.0f;//最大比例
	public static float DEFAULT_MID_SCALE = 1.75f;//中间比例
	public static float DEFAULT_NORMAL_SCALE = 1f;//正常显示的比例，也是回弹时的比例，也是显示的最小比例
	public static float DEFAULT_MIN_SCALE = 0.75f;//最小缩放比例，手势操作时的最小缩小比例
	

	//图片fill_center时的缩放比例，是其他比例的基准
	private float horizontalBaseScale = 0;
	private float verticalBaseScale = 0;
	
	private float horizontalNormalScale = DEFAULT_NORMAL_SCALE;//显示时的最小大小
	private float horizontalMidScale = DEFAULT_MID_SCALE;//显示时的中等大小
	private float horizontalMaxScale = DEFAULT_MAX_SCALE;//显示时的最大大小
	private float horizontalMinScale = DEFAULT_MIN_SCALE;//缩放时的最小缩小大小
	private float verticalNormalScale = DEFAULT_NORMAL_SCALE;
	private float verticalMidScale = DEFAULT_MID_SCALE;
	private float verticalMaxScale = DEFAULT_MAX_SCALE;
	private float verticalMinScale = DEFAULT_MIN_SCALE;

	private boolean allowParentInterceptOnEdge = true;

	private MultiGestureDetector multiGestureDetector;

	// These are set so we don't keep allocating them on the heap
	private final Matrix baseMatrix = new Matrix();
	private final RectF displayRect = new RectF();
	private final float[] matrixValues = new float[9];
	private final Matrix showMatrix = new Matrix();
	private final Matrix startRotationMatrix = new Matrix();//开始旋转时的矩阵 主要用来结束回弹时使用
	//作用是根据变换后的matrix计算出变换后图片的偏移情况，
	//如果图片大于控件，则拖拽时保证图片不会小于边界
	//如果图片小于控件，则缩放移动时不会离开中心
	//需要将这个matrix后乘到变换matrix上
	private final Matrix backMatrix = new Matrix();
	
	private float floatDeviation = 0.001f;//比较2个float时的误差，大于这个误差才算大，否则算是float的误差
	
	//是否是新的一次手势
	private boolean isNewGesture;
	private boolean horizontalImage;//图片是横图还是竖图
	private boolean horizontalScaleHorizontal;//水平时，是否以水平方向为缩放基准
	private boolean verticalScaleHorizontal;//竖直时，是否以水平方向为缩放基准
	
	private float curDegree;//当前图片的旋转角度
	private float deltaDegree;//这次手势操作中旋转的总角度
	private int startDegree;//手势开始时的图片旋转角度 
	
	//旋转和缩放是互斥的，所以这里用一个变量表示是旋转还是缩放
	private boolean scaleNoRotation;
	
	// Listeners
	private OnPhotoTapListener photoTapListener;
	private OnViewTapListener viewTapListener;
	private OnLongClickListener longClickListener;

	private int top, right, bottom, left;
	private FlingRunnable currentFlingRunnable;
	private int scrollEdge = EDGE_BOTH;

	private boolean isZoomEnabled;
	private ScaleType scaleType = ScaleType.FIT_CENTER;
	
	private boolean canSingleTap = true;
	private boolean canDoubleTap = true;
	private boolean canScale = true;
	
	private SingleTapConfirmedListener mSingleTapConfirmedListener;
	
//	ScrollState startScroll;
//	ScrollState endScroll;

	public boolean isCanSingleTap() {
		return canSingleTap;
	}

	public void setCanSingleTap(boolean canSingleTap) {
		this.canSingleTap = canSingleTap;
	}

	public boolean isCanDoubleTap() {
		return canDoubleTap;
	}

	public void setCanDoubleTap(boolean canDoubleTap) {
		this.canDoubleTap = canDoubleTap;
	}

	public boolean isCanScale() {
		return canScale;
	}

	public void setCanScale(boolean canScale) {
		this.canScale = canScale;
	}

	public ZoomView(Context context) {
		this(context, null);
	}

	public ZoomView(Context context, AttributeSet attr) {
		this(context, attr, 0);
	}

	public ZoomView(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);

		super.setScaleType(ScaleType.MATRIX);

		setOnTouchListener(this);

		multiGestureDetector = new MultiGestureDetector(context);

		setIsZoomEnabled(true);
	}

	public float getCurRotation(){
		return curDegree;
	}
	
	public float getStartRotation(){
		return startDegree;
	}
	
	/**
	 * Gets the Display Rectangle of the currently displayed Drawable. The
	 * Rectangle is relative to this View and includes all scaling and
	 * translations.
	 * 
	 * @return - RectF of Displayed Drawable
	 */
	public final RectF getDisplayRect() {
		checkMatrixBounds(getImageMatrix());
		return getDisplayRect(getDisplayMatrix());
	}

	/**
	 * 获取图片水平时的基准缩放大小
	 * @return
	 */
	public float getHorizontalBaseScaleWithoutRotation(){
		return horizontalBaseScale;
	}
	
	/**
	 * 获取图片垂直时的基准缩放大小
	 * @return
	 */
	public float getVerticalBaseScaleWithoutRotation(){
		return verticalBaseScale;
	}
	
	/**
	 * Return the current scale type in use by the ImageView.
	 */
	@Override
	public final ScaleType getScaleType() {
		return scaleType;
	}

	/**
	 * Controls how the image should be resized or moved to match the size of
	 * the ImageView. Any scaling or panning will happen within the confines of
	 * this {@link ScaleType}.
	 * 
	 * @param scaleType
	 *            - The desired scaling mode.
	 */
	@Override
	public final void setScaleType(ScaleType scaleType) {
		if (scaleType == ScaleType.MATRIX) {
			throw new IllegalArgumentException(scaleType.name()
					+ " is not supported in ZoomImageView");
		}

		if (scaleType != this.scaleType) {
			this.scaleType = scaleType;
			update();
		}
	}

	/**
	 * Returns true if the ZoomImageView is set to allow zooming of Photos.
	 * 
	 * @return true if the ZoomImageView allows zooming.
	 */
	public final boolean isZoomEnabled() {
		return isZoomEnabled;
	}

	/**
	 * Allows you to enable/disable the zoom functionality on the ImageView.
	 * When disable the ImageView reverts to using the FIT_CENTER matrix.
	 * 
	 * @param isZoomEnabled
	 *            - Whether the zoom functionality is enabled.
	 */
	public final void setIsZoomEnabled(boolean isZoomEnabled) {
		this.isZoomEnabled = isZoomEnabled;
		update();
	}

	/**
	 * Whether to allow the ImageView's parent to intercept the touch event when
	 * the photo is scroll to it's horizontal edge.
	 */
	public void setAllowParentInterceptOnEdge(boolean allowParentInterceptOnEdge) {
		this.allowParentInterceptOnEdge = allowParentInterceptOnEdge;
	}

	@Override
	public void setImageBitmap(Bitmap bitmap) {
		super.setImageBitmap(bitmap);
		update();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		update();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		update();
	}

	@Override
	public void setImageURI(Uri uri) {
		super.setImageURI(uri);
		update();
	}

	/**
	 * Register a callback to be invoked when the Photo displayed by this view
	 * is long-pressed.
	 * 
	 * @param listener
	 *            - Listener to be registered.
	 */
	@Override
	public final void setOnLongClickListener(OnLongClickListener listener) {
		longClickListener = listener;
	}

	/**
	 * Register a callback to be invoked when the Photo displayed by this View
	 * is tapped with a single tap.
	 * 
	 * @param listener
	 *            - Listener to be registered.
	 */
	public final void setOnPhotoTapListener(OnPhotoTapListener listener) {
		photoTapListener = listener;
	}
	public void setOnSingleTapConfirmedListener(SingleTapConfirmedListener l) {
		this.mSingleTapConfirmedListener = l;
	}

	/**
	 * Register a callback to be invoked when the View is tapped with a single
	 * tap.
	 * 
	 * @param listener
	 *            - Listener to be registered.
	 */
	public final void setOnViewTapListener(OnViewTapListener listener) {
		viewTapListener = listener;
	}

	@Override
	public final void onGlobalLayout() {
		if (isZoomEnabled) {
			final int top = getTop();
			final int right = getRight();
			final int bottom = getBottom();
			final int left = getLeft();

			/**
			 * We need to check whether the ImageView's bounds have changed.
			 * This would be easier if we targeted API 11+ as we could just use
			 * View.OnLayoutChangeListener. Instead we have to replicate the
			 * work, keeping track of the ImageView's bounds and then checking
			 * if the values change.
			 */
			if ((top != this.top) || (bottom != this.bottom)
					|| (left != this.left) || (right != this.right)) {
				// Update our base matrix, as the bounds have changed
				updateBaseMatrix(getDrawable());

				// Update values as something has changed
				this.top = top;
				this.right = right;
				this.bottom = bottom;
				this.left = left;
			}
		}
	}
	
	boolean backing = false;//是否正在回弹
	boolean isIgnore = false;//是否忽略手势，如果正在回弹时，触发手势会把这次手势完全屏蔽掉

	@Override
	public final boolean onTouch(View v, MotionEvent ev) {
		boolean handled = false;
		if(backing){//防止图片回弹时的手势
			switch (ev.getAction()) {
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				isIgnore = false;
				break;
			default :
				isIgnore = true;
			}
			if (v.getParent() != null) {
				v.getParent().requestDisallowInterceptTouchEvent(true);
			}
			return true;
		}
		if(isIgnore){//如果正在回弹时触发手势，回弹结束后之后的手势也需要屏蔽防止造成图片角度突然变化不连贯。之后再手指抬起时才结束屏蔽
			switch (ev.getAction()) {
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				isIgnore = false;
			}
			return true;
		}
		
		if (isZoomEnabled) {
			switch (ev.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				
				// First, disable the Parent from intercepting the touch
				// event
				if (v.getParent() != null) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
				}

				// If we're flinging, and the user presses down, cancel
				// fling
				if (currentFlingRunnable != null) {
					currentFlingRunnable.cancelFling();
					currentFlingRunnable = null;
				}
				curDegree = startDegree;
				scaleNoRotation = true;
				isNewGesture = true;
				deltaDegree = 0;
				break;
			case MotionEvent.ACTION_MOVE:
				curDegree = startDegree + deltaDegree;
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				float curScale = getScaleWithoutRotation();
				float targetScale = horizontalNormalScale;
				int targetDegree = getTargetDegree();
				targetDegree = (int)changeDegreeIn360(targetDegree);
				targetDegree = getAbsDegree(targetDegree);
				curDegree = changeDegreeIn360(curDegree);
				if(targetDegree == 0 && curDegree > 180){
					targetDegree = 360;
				}
				boolean isHorizontal = isTargetDegreeHorizontal(targetDegree);
				if(!isHorizontal){
					targetScale = verticalNormalScale;
				}
				RectF currentRect = getDisplayRect();
				if(currentRect == null){
					deltaDegree = 0;
					break;
				}
				float centerFromX = currentRect.centerX();
				float centerFromY = currentRect.centerY();
				//如果缩放大小小于最小缩放值回弹到中心
				if(targetScale - curScale > floatDeviation){
					//角度太小 完全回弹
					v.post(new BackRunnable(targetDegree, curDegree, targetScale, curScale, centerFromX, centerFromY));
					handled = true;
					deltaDegree = 0;
					break;
				}
				float center2X = centerFromX;
				float center2Y = centerFromY;
				int viewWidth = getWidth();
				int viewHeight = getHeight();
				//当前旋转角度和目标角度不同
				if(Float.compare(targetDegree, curDegree)!=0){
					//如果旋转后回弹方向没变则大小不变
					if(Math.abs(changeDegreeIn360(targetDegree - startDegree)) < 45 ){
						targetScale = curScale;
						RectF backRect = getDisplayRect(startRotationMatrix);
						center2X = backRect.centerX();
						center2Y = backRect.centerY();
						if(isHorizontal){//如果返回的是水平状态
							if(backRect.width() > viewWidth){
								if(backRect.left > 0){
									center2X -= backRect.left;
								}
								if(backRect.right < viewWidth){
									center2X += (viewWidth - backRect.right);
								}
							}else{
								center2X = 1f * viewWidth / 2;
							}
							if(backRect.height() > viewHeight){
								if(backRect.top > 0){
									center2Y -= backRect.top;
								}
								if(backRect.bottom < viewHeight){
									center2Y += (viewHeight - backRect.bottom);
								}
							}else{
								center2Y = 1f*viewHeight/2;
							}
						}else{//如果返回的是竖直状态
							if(backRect.width() > viewWidth){
								if(backRect.left > 0){
									center2X -= backRect.left;
								}
								if(backRect.right < viewWidth){
									center2X += (viewWidth - backRect.right);
								}
							}else{
								center2X = 1f * viewWidth / 2;
							}
							if(backRect.height() > viewHeight){
								if(backRect.top > 0){
									center2Y -= backRect.top;
								}
								if(backRect.bottom < viewHeight){
									center2Y += (viewHeight - backRect.bottom);
								}
							}else{
								center2Y = 1f * viewHeight / 2;
							}
						}
					}else{//如果方向需要改变，中心变为view中心
						center2Y = 1f * viewHeight / 2;
						center2X = 1f * viewWidth / 2;
					}
					v.post(new BackRunnable(targetDegree, curDegree, targetScale, curScale, centerFromX, centerFromY, center2X, center2Y, false));
					handled = true;
					deltaDegree = 0;
					break;
				}
				//当前角度没变 并且缩放大小大于最小缩放大小，则缩放大小不变
				targetScale = curScale;
				boolean needBack = false;
				if(isHorizontal){//如果当前是水平显示
					if(horizontalScaleHorizontal){//如果是水平缩放
						if(currentRect.left > 1){
							needBack = true;
							center2X = centerFromX - currentRect.left;
						}
						if(viewWidth - currentRect.right > 1 ){
							needBack = true;
							center2X = centerFromX + (viewWidth - currentRect.right);
						}
						if(currentRect.height() > viewHeight){
							if(currentRect.top > 0){
								center2Y -= currentRect.top;
								needBack = true;
							}
							if(currentRect.bottom < viewHeight){
								center2Y += (viewHeight - currentRect.bottom);
								needBack = true;
							}
						}else{
							center2Y = 1f*viewHeight/2;
							if(Math.abs(center2Y - centerFromY) > 1){
								needBack = true;
							}
						}
					}else{//如果是竖直为基准缩放
						if(currentRect.top > 1){
							needBack = true;
							center2Y  = centerFromY - currentRect.top;
						}
						if(viewHeight - currentRect.bottom > 1 ){
							needBack = true;
							center2Y = centerFromY + (viewHeight - currentRect.bottom);
						}
						if(currentRect.width() > viewWidth){
							if(currentRect.left > 0){
								center2X -= currentRect.left;
								needBack = true;
							}
							if(currentRect.right < viewWidth){
								center2X += (viewWidth - currentRect.right);
								needBack = true;
							}
						}else{
							center2X = 1f*viewWidth/2;
							if(Math.abs(center2X - centerFromX) > 1){
								needBack = true;
							}
						}
					}
				}else{//如果是竖直显示
					if(verticalScaleHorizontal){//如果是水平缩放
						if(currentRect.left > 1){
							needBack = true;
							center2X -= currentRect.left;
						}
						if(viewWidth - currentRect.right > 1){
							needBack = true;
							center2X += (viewWidth - currentRect.right);
						}
						if(currentRect.height() > viewHeight){
							if(currentRect.top > 0){
								center2Y -= currentRect.top;
								needBack = true;
							}
							if(currentRect.bottom < viewHeight){
								center2Y += (viewHeight - currentRect.bottom);
								needBack = true;
							}
						}else{
							center2Y = 1f*viewHeight/2;
							if(Math.abs(center2Y - centerFromY) > 1){
								needBack = true;
							}
						}
					}else{//如果是以竖直为基准
						if(currentRect.top > 1){
							needBack = true;
							center2Y -= currentRect.top;
						}
						if(viewHeight - currentRect.bottom > 1){
							needBack = true;
							center2Y += (viewHeight - currentRect.bottom);
						}
						if(currentRect.width() > viewWidth){
							if(currentRect.left > 0){
								center2X -= currentRect.left;
								needBack = true;
							}
							if(currentRect.right < viewWidth){
								center2X += (viewWidth - currentRect.right);
								needBack = true;
							}
						}else{
							center2X = 1f*viewWidth/2;
							if(Math.abs(center2X - centerFromX) > 1){
								needBack = true;
							}
						}
					}
				}
				if(needBack){
					v.post(new BackRunnable(targetDegree, curDegree, targetScale, curScale, currentRect.centerX(), currentRect.centerY(),center2X, center2Y, false));
					handled = true;
				}
				//清理手势旋转角度
				deltaDegree = 0;
				break;
			}

//			// Finally, try the scale/drag/tap detector
//			if ((multiGestureDetector != null)
//					&& multiGestureDetector.onTouchEvent(ev)) {
//				handled = true;
//			}
		}
		// Finally, try the scale/drag/tap detector
		if ((multiGestureDetector != null)
				&& multiGestureDetector.onTouchEvent(ev)) {
			handled = true;
		}
//		}

		return handled;
	}
	
	/**
	 * 判断目标角度是否是水平的
	 * @param degreeIn360
	 * @return
	 */
	private boolean isTargetDegreeHorizontal(int degreeIn360){
		if(degreeIn360 == 0 || degreeIn360 == 180 || degreeIn360 == 360){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 判断目标角度是否是在水平范围内
	 * @param degreeIn360
	 * @return
	 */
	private boolean isDegreeHorizontal(float degreeIn360){
		if(degreeIn360 >=0 && degreeIn360 < 45){
			return true;
		}
		if(degreeIn360 >= 135 && degreeIn360 < 225){
			return true;
		}
		if(degreeIn360 >= 315){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取当前真实的缩放大小，排除旋转对缩放的影响
	 * @return
	 */
	public float getScaleWithoutRotation(){
		showMatrix.set(getImageMatrix());
		showMatrix.postRotate(-curDegree);//旋转需要还原不然的话缩放不对
		showMatrix.getValues(matrixValues);
		return matrixValues[Matrix.MSCALE_X];
	}
	
	/**
	 * 根据开始时的角度和手势中旋转角度获取回弹时的目标角度
	 * 比如开始时是90度，顺时针移动超过45度则返回180度，少于45度返回90度
	 * @return
	 */
	private int getTargetDegree(){
		int targetDegree = 0;
		int num = (int)Math.abs(deltaDegree) / 45;
		if(num % 2 == 0){//如果是双数，表示没有超过一半 需要退回当前前一个位置
			if(deltaDegree > 0){
				targetDegree = startDegree + 45 * num; 
			}else{
				targetDegree = startDegree - 45 * num;
			}
		}else{//表示超过一半需要到下一个位置
			if(deltaDegree > 0){
				targetDegree = startDegree + 45 * (num + 1); 
			}else{
				targetDegree = startDegree - 45 * (num + 1);
			}
		}
		return targetDegree;
	}
	
	
	
	/**
	 * 将角度换算到360度之内，不包括360度
	 * 比如-90度变为270度
	 * @param degree
	 * @return
	 */
	public float changeDegreeIn360(float degree){
		while(degree < 0){
			degree += 360;
		}
		while(degree >= 360){
			degree -= 360;
		}
		return degree;
	}
	
	/**
	 * 将目标角度变成具体的0,90,180,270四个中的一个，防止出现偏差 没有360
	 * 目标角度是一个float，为了保证准确，将该值改为特定的int值
	 * @param degreeIn360
	 * @return
	 */
	private int getAbsDegree(float degreeIn360){
		if(Math.abs(degreeIn360 - 0) < 10){
			return 0;
		}
		if(Math.abs(degreeIn360 - 90) < 10){
			return 90;
		}
		if(Math.abs(degreeIn360 - 180) < 10){
			return 180;
		}
		if(Math.abs(degreeIn360 - 270) < 10){
			return 270;
		}
		if(Math.abs(degreeIn360 - 360) < 10){
			return 360;
		}
		return (int)degreeIn360;
	}
	

	 // 取旋转角度  
    private float rotation(MotionEvent event) {  
        double delta_x = (event.getX(0) - event.getX(1));  
        double delta_y = (event.getY(0) - event.getY(1));  
        double radians = Math.atan2(delta_y, delta_x);  
        return (float) Math.toDegrees(radians);  
    }  
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();

		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();

		getViewTreeObserver().removeGlobalOnLayoutListener(this);
	}

	protected Matrix getDisplayMatrix() {
		showMatrix.set(getImageMatrix());
		return showMatrix;
	}
	

	private final void update() {
		if (isZoomEnabled) {
			super.setScaleType(ScaleType.MATRIX);
			updateBaseMatrix(getDrawable());
		} else {
			updateBaseMatrix(getDrawable());
//			resetMatrix();
		}
	}

	
	/**
	 * 这里的作用主要是:
	 * 经过给定的Matrix变换后
	 * 判断当前图片状态是否已经达到边界
	 * 如果图片大于控件，则拖拽时保证图片不会小于边界（通过这里设定偏移matrix实现，给定matrix.postConcat(偏移matrix)）
	 * 如果图片小于控件，则缩放移动时不会离开中心（通过这里设定偏移matrix实现，给定matrix.postConcat(偏移matrix)）
	 */
	private void checkMatrixBounds(Matrix matrix) {
		final RectF rect = getDisplayRect(matrix);
		if (null == rect) {
			return;
		}

		final float height = rect.height(), width = rect.width();
		float deltaX = 0, deltaY = 0;

		final int viewHeight = getHeight();
		if (height <= viewHeight) {
			switch (scaleType) {
			case FIT_START:
				deltaY = -rect.top;
				break;
			case FIT_END:
				deltaY = viewHeight - height - rect.top;
				break;
			default:
				deltaY = ((viewHeight - height) / 2) - rect.top;
				break;
			}
		} else if (rect.top > 0) {
			deltaY = -rect.top;
		} else if (rect.bottom < viewHeight) {
			deltaY = viewHeight - rect.bottom;
		}

		final int viewWidth = getWidth();
		if (width <= viewWidth) {
			switch (scaleType) {
			case FIT_START:
				deltaX = -rect.left;
				break;
			case FIT_END:
				deltaX = viewWidth - width - rect.left;
				break;
			default:
				deltaX = ((viewWidth - width) / 2) - rect.left;
				break;
			}
			scrollEdge = EDGE_BOTH;
		} else if (rect.left > 0) {
			scrollEdge = EDGE_LEFT;
			deltaX = -rect.left;
		} else if (rect.right < viewWidth) {
			deltaX = viewWidth - rect.right;
			scrollEdge = EDGE_RIGHT;
		} else {
			scrollEdge = EDGE_NONE;
		}

		// Finally actually translate the matrix
		backMatrix.postTranslate(deltaX, deltaY);
	}

	/**
	 * Helper method that maps the supplied Matrix to the current Drawable
	 * 
	 * @param matrix
	 *            - Matrix to map Drawable against
	 * @return RectF - Displayed Rectangle
	 */
	private RectF getDisplayRect(Matrix matrix) {
		Drawable d = getDrawable();
		if (null != d) {
			displayRect
					.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
			matrix.mapRect(displayRect);
			return displayRect;
		}

		return null;
	}

	/**
	 * Resets the Matrix back to FIT_CENTER, and then displays it.s
	 */
	private void resetMatrix() {
		setImageMatrix(baseMatrix);
		checkMatrixBounds(baseMatrix);
	}

	/**
	 * Calculate Matrix for FIT_CENTER
	 * 
	 * @param d
	 *            - Drawable being displayed
	 */
	private void updateBaseMatrix(Drawable d) {
		if (null == d) {
			return;
		}
		startDegree = 0;
		curDegree = 0;
		final float viewWidth = getWidth();
		final float viewHeight = getHeight();
		//这里的作用主要是当设定图片的时候当前view还没有计算出宽高，所以需要等他宽高计算出来后才能计算他的matrix
		if(viewWidth == 0 || viewHeight == 0){
			ViewTreeObserver vto = getViewTreeObserver();
			vto.addOnPreDrawListener(new MyPreDrawFilter(this) {

				public void doBeforeDraw() {
					updateBaseMatrix(getDrawable());
				}
			});
			return ;
		}
		final int drawableWidth = d.getIntrinsicWidth();
		final int drawableHeight = d.getIntrinsicHeight();
		if(drawableHeight > drawableWidth){
			horizontalImage = false;
		}else{
			horizontalImage = true;
		}
		mPicCenterX = 1f * drawableWidth/2;
		mPicCenterY = 1f * drawableHeight/2;
		baseMatrix.reset();

		final float widthScale = viewWidth / drawableWidth;
		final float heightScale = viewHeight / drawableHeight;
		DEFAULT_MAX_SCALE = 3.0f;//最大比例
		if(widthScale > heightScale){
			horizontalScaleHorizontal = false;
			horizontalBaseScale = heightScale;
			if(horizontalBaseScale > 0){
				float maxScale = widthScale/horizontalBaseScale;
				if(maxScale > DEFAULT_MAX_SCALE){
					DEFAULT_MAX_SCALE = maxScale;
				}
			}
		}else{
			horizontalScaleHorizontal = true;
			horizontalBaseScale = widthScale;
			if(horizontalBaseScale > 0){
				float maxScale = heightScale/horizontalBaseScale;
				if(maxScale > DEFAULT_MAX_SCALE){
					DEFAULT_MAX_SCALE = maxScale;
				}
			}
		}
		horizontalNormalScale = DEFAULT_NORMAL_SCALE * horizontalBaseScale;
		horizontalMidScale = DEFAULT_MID_SCALE * horizontalBaseScale;
		horizontalMaxScale = DEFAULT_MAX_SCALE * horizontalBaseScale;
		horizontalMinScale = DEFAULT_MIN_SCALE * horizontalBaseScale;
		final float verticalWidthScale = viewHeight / drawableWidth;
		final float verticalHeightScale = viewWidth / drawableHeight;
		if(verticalWidthScale > verticalHeightScale){
			verticalScaleHorizontal = true;
			verticalBaseScale = verticalHeightScale;
		}else{
			verticalScaleHorizontal = false;
			verticalBaseScale = verticalWidthScale;
		}
		verticalNormalScale = DEFAULT_NORMAL_SCALE * verticalBaseScale;
		verticalMidScale = DEFAULT_MID_SCALE * verticalBaseScale;
		verticalMaxScale = DEFAULT_MAX_SCALE * verticalBaseScale;
		verticalMinScale = DEFAULT_MIN_SCALE * verticalBaseScale;
		if (scaleType == ScaleType.CENTER) {
			baseMatrix.postTranslate((viewWidth - drawableWidth) / 2F,
					(viewHeight - drawableHeight) / 2F);

		} else if (scaleType == ScaleType.CENTER_CROP) {
			float scale = Math.max(widthScale, heightScale);
			baseMatrix.postScale(scale, scale);
			baseMatrix.postTranslate(
					(viewWidth - (drawableWidth * scale)) / 2F,
					(viewHeight - (drawableHeight * scale)) / 2F);

		} else if (scaleType == ScaleType.CENTER_INSIDE) {
			float scale = Math.min(1.0f, Math.min(widthScale, heightScale));
			baseMatrix.postScale(scale, scale);
			baseMatrix.postTranslate(
					(viewWidth - (drawableWidth * scale)) / 2F,
					(viewHeight - (drawableHeight * scale)) / 2F);

		} else {
			RectF mTempSrc = new RectF(0, 0, drawableWidth, drawableHeight);
			RectF mTempDst = new RectF(0, 0, viewWidth, viewHeight);

			switch (scaleType) {
			case FIT_CENTER:
				baseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.CENTER);
				break;

			case FIT_START:
				baseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.START);
				break;

			case FIT_END:
				baseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.END);
				break;

			case FIT_XY:
				baseMatrix.setRectToRect(mTempSrc, mTempDst, Matrix.ScaleToFit.FILL);
				break;

			default:
				break;
			}
		}

		resetMatrix();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void postOnAnimation(View view, Runnable runnable) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			view.postOnAnimation(runnable);
		} else {
			view.postDelayed(runnable, 16);
		}
	}

	
//	//如果需要添加获取缩放大小和设定缩放大小的方法时需使用这个方法来进行校验
	private void checkZoomLevels(float minZoom, float normal, float midZoom, float maxZoom) {
		if (minZoom >= normal) {
			throw new IllegalArgumentException(
					"MinZoom should be less than normal");
		}else if (normal >= midZoom) {
			throw new IllegalArgumentException(
					"normal should be less than midZoom");
		}else if (midZoom >= maxZoom) {
			throw new IllegalArgumentException(
					"MidZoom should be less than MaxZoom");
		}
	}
	
	public ZoomState getZoomState(){
		ZoomState state =  new ZoomState();
		state.setCurDegree(curDegree);
		state.setDeltaDegree(deltaDegree);
		state.setStartDegree(startDegree);
		return state;
	}
	public void resetZoomState(ZoomState zoomState){
		this.startDegree = zoomState.getStartDegree();
		this.curDegree = zoomState.getCurDegree();
		this.deltaDegree = zoomState.getDeltaDegree();
	}
	
	public static class ZoomState{
		private int startDegree;
		private float curDegree;//当前图片的旋转角度
		private float deltaDegree;//这次手势操作中旋转的总角度
		
		public boolean isOrignal(){
			return startDegree == 0 && Float.compare(curDegree, 0) == 0 && Float.compare(deltaDegree, 0) == 0;
		}
		public int getStartDegree() {
			return startDegree;
		}
		public void setStartDegree(int startDegree) {
			this.startDegree = startDegree;
		}
		public float getCurDegree() {
			return curDegree;
		}
		public void setCurDegree(float curDegree) {
			this.curDegree = curDegree;
		}
		public float getDeltaDegree() {
			return deltaDegree;
		}
		public void setDeltaDegree(float deltaDegree) {
			this.deltaDegree = deltaDegree;
		}
	}
	
	/**
	 * 原始图片的中心点
	 */
	private float mPicCenterX;
	private float mPicCenterY;
	
	/**
	 * 回弹类
	 * 作用是将图片进行回弹
	 * 这里的centerX,centerY表示的是图片中心点的位置。这里是根据图片的中心点位置，以及旋转和缩放来进行回弹的。
	 * 这里的缩放是在没有旋转下的缩放，即水平状态下的缩放
	 * @author cx
	 *
	 */
	private class BackRunnable implements Runnable {
		
		private float targetRotation;
		private float targetScale;
		private float fromRotation;
		private float fromScale;
		private float centerFromX;//图片中心点的起始位置
		private float centerFromY;//图片中心点的起始位置
		private long mDuration;//回弹时间 单位毫秒
		private boolean isStop;//是否结束
		private long mStartTime = -1;//开始时间
		private Interpolator mInterpolator = new LinearInterpolator();//回弹速率
		private float center2X;//图片中心点的目标位置
		private float center2Y;//图片中心点的目标位置
		
		//图片回弹时是否保持图片的宽或高不离开边界，如果为true，则如果回弹开始时图片
		//中心不在view中心，并且图片脱离边界，则会直接从中心开始回弹，即从当前位置
		//消失，然后从中心显示然后开始回弹，会有一些不连贯，回弹最终结果保持图片不离开
		//边界，如果设定的目标超出边界，则真正回弹的目标点不会是设定的点，会在设定的目标点
		//上进行相应的移动变换。如果为false则平滑从当前位置回弹，最终到达目标点，可能会超出边界
		private boolean limitBounds;
		
		public BackRunnable(float targetRotation, float fromRotation, float targetScale, float fromScale, float centerFromX, float centerFromY){
			this.targetRotation = targetRotation;
			this.targetScale = targetScale;
			this.fromRotation = fromRotation;
			this.fromScale = fromScale;
			this.centerFromX = centerFromX;
			this.centerFromY = centerFromY;
			mDuration = 200;
			center2X = getWidth()/2;
			center2Y = getHeight()/2;
			limitBounds = false;
		}
		
		public BackRunnable(float targetRotation, float fromRotation, float targetScale, float fromScale, float centerFromX, float centerFromY, float center2X, float center2Y){
			this.targetRotation = targetRotation;
			this.targetScale = targetScale;
			this.fromRotation = fromRotation;
			this.fromScale = fromScale;
			this.centerFromX = centerFromX;
			this.centerFromY = centerFromY;
			mDuration = 300;
			this.center2X = center2X;
			this.center2Y = center2Y;
			limitBounds = true;
		}
		
		public BackRunnable(float targetRotation, float fromRotation, float targetScale, float fromScale, float centerFromX, float centerFromY, float center2X, float center2Y, boolean limitBounds){
			this.targetRotation = targetRotation;
			this.targetScale = targetScale;
			this.fromRotation = fromRotation;
			this.fromScale = fromScale;
			this.centerFromX = centerFromX;
			this.centerFromY = centerFromY;
			mDuration = 300;
			this.center2X = center2X;
			this.center2Y = center2Y;
			this.limitBounds = limitBounds;
		}
		
		

		public void run() {
			if(!backing){
				backing = true;
			}
			Matrix changeMatrix = null;
			float percent = 0;
			if(mStartTime == -1){//刚开始，初始化时间
				mStartTime = System.currentTimeMillis();
			}else{
				long normalizedTime = (1000 * (System.currentTimeMillis() - mStartTime)) / mDuration;
				normalizedTime = Math.max(Math.min(normalizedTime, 1000), 0);
				percent = mInterpolator.getInterpolation(normalizedTime / 1000f);
				final float deltaX = (center2X - centerFromX) * percent;
				final float deltaY = (center2Y - centerFromY) * percent;
				//缩放是相对于原始图片进行的，所以需要加上初始值
				float deltaScale =fromScale +(targetScale - fromScale)*percent;
				final float deltaRotation = fromRotation + (targetRotation - fromRotation)*percent;
				changeMatrix = new Matrix();//必须new 不然缩放会在当前基础上进行
				//缩放到目标大小
                changeMatrix.postTranslate(- mPicCenterX, - mPicCenterY);//将图片中心移动到缩放原点 为了防止在原地进行缩放导致图片中心缩放时发生偏移
				changeMatrix.postScale(deltaScale, deltaScale, 0, 0);//进行缩放
				changeMatrix.postRotate(deltaRotation, 0, 0);//进行旋转
                changeMatrix.postTranslate(mPicCenterX, mPicCenterY);//将缩放后的图片移动回原来的位置
				//将图片移动到任务起始位置
				changeMatrix.postTranslate(centerFromX - mPicCenterX, centerFromY - mPicCenterY);//将图片中心移动到起始点
				changeMatrix.postTranslate(deltaX, deltaY);
				backMatrix.reset();
				checkMatrixBounds(changeMatrix);
				if(limitBounds){
					changeMatrix.postConcat(backMatrix);
				}
				setImageMatrix(changeMatrix);
				curDegree = deltaRotation;
			}

			if (!isStop && percent != 1) {
				post(this);
			}else{
				backing = false;
				startDegree = getAbsDegree(targetRotation);
				curDegree = startDegree;
				isStop = true;
			}
		}
	}
	boolean isFling = false;

	private class FlingRunnable implements Runnable {
		private final ScrollerProxy scroller;
		private int currentX, currentY;

		public FlingRunnable(Context context) {
			scroller = new ScrollerProxy(context);
		}

		public void cancelFling() {
			scroller.forceFinished(true);
		}

		public void fling(int viewWidth, int viewHeight, int velocityX,
				int velocityY) {
			final RectF rect = getDisplayRect();
			if (null == rect) {
				return;
			}

			final int startX = Math.round(-rect.left);
			final int minX, maxX, minY, maxY;

			if (viewWidth < rect.width()) {
				minX = 0;
				maxX = Math.round(rect.width() - viewWidth);
			} else {
				minX = maxX = startX;
			}

			final int startY = Math.round(-rect.top);
			if (viewHeight < rect.height()) {
				minY = 0;
				maxY = Math.round(rect.height() - viewHeight);
			} else {
				minY = maxY = startY;
			}

			currentX = startX;
			currentY = startY;

			// If we actually can move, fling the scroller
			if ((startX != maxX) || (startY != maxY)) {
				scroller.fling(startX, startY, velocityX, velocityY, minX,
						maxX, minY, maxY, 0, 0);
			}
		}

		@Override
		public void run() {
			
			if (scroller.computeScrollOffset()) {
				final int newX = scroller.getCurrX();
				final int newY = scroller.getCurrY();
				showMatrix.set(getImageMatrix());
				showMatrix.postTranslate(currentX - newX, currentY - newY);
				setImageMatrix(showMatrix);
				currentX = newX;
				currentY = newY;
				isFling = true;
				// Post On animation
				postOnAnimation(ZoomView.this, this);
			}else{
				isFling = false;
			}
		}
	}
}
