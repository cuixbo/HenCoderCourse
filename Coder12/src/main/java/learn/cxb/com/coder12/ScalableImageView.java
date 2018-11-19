package learn.cxb.com.coder12;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.OverScroller;

import module.cxb.com.coderlib.Utils;

/**
 * 思路流程：
 * 1.画图片->图片居中
 * 2.实现双击缩放
 * 3.实现缩放动画
 * 4.实现移动->限制移动边界
 * 5.实现滑动效果
 * 6.实现双指捏合缩放->限制缩放边界
 * <p>
 * 问题：
 * 1.修复 放大后再移动，再缩放图片，位置发生偏移、双击缩放边界问题
 * 2.双击缩放，轴心缩放
 * 3.
 *
 *
 * <p>
 * 长图还是宽图，图片宽高比与屏幕宽高比
 * 图片缩放，要么宽度适配，要么高度适配
 * 两个缩放级别：1.小比例缩放（长图适配高度，宽图适配宽度）2.大比例缩放（长图适配宽度，宽图适配高度）
 * 最大缩放比例：大比例缩放比例*系数
 * 拖动弹性
 */
public class ScalableImageView extends View {
    private static final float OVER_SCALE_FACTOR = 2.5f;//最大缩放比例系数

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;

    float originalOffsetX, originalOffsetY;//绘制图片居中时，图片左上起点的坐标偏移。
    float offsetX, offsetY;//图片移动的坐标偏移
    boolean big;//是否已经放大。控制最小，最大缩放比例。
    float bigScale, smallScale;//最大，最小缩放比例。
    ObjectAnimator scaleAnimator;//缩放动画
    //    float scaleFraction;//缩放动画系数
    float currentScale;
    boolean isInScaleProgress;
    GestureDetector mGestureDetector;
    OverScroller scroller;
    ScaleGestureDetector mScaleGestureDetector;
    FlyingRunner mFlyingRunner = new FlyingRunner();
    HenOnGestureListener mHenOnGestureListener = new HenOnGestureListener();
    HenOnScaleGestureListener mHenOnScaleGestureListener = new HenOnScaleGestureListener();

    public ScalableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
//        bitmap = Utils.getAvatar(context.getResources(), 600);
//        bitmap = Utils.getBitmap(context.getResources(), R.drawable.avatar_rengwuxian);
        bitmap = Utils.getBitmap(context.getResources(), R.drawable.ic_height_long);
        mGestureDetector = new GestureDetector(context, mHenOnGestureListener);
        scroller = new OverScroller(context);
        mScaleGestureDetector = new ScaleGestureDetector(context, mHenOnScaleGestureListener);
        mScaleGestureDetector.setQuickScaleEnabled(false);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        originalOffsetX = (getWidth() - bitmap.getWidth()) / 2f;
        originalOffsetY = (getHeight() - bitmap.getHeight()) / 2f;

        if ((float) bitmap.getWidth() / bitmap.getHeight() > (float) getWidth() / getHeight()) {//图片宽高比>屏幕宽高比
            // 这说明图片属于宽图片型，需要适配宽度。
            smallScale = (float) getWidth() / bitmap.getWidth();//宽图片型，图片最小缩放比例：缩至与图片宽度与屏幕宽度一致。
            bigScale = (float) getHeight() / bitmap.getHeight() * OVER_SCALE_FACTOR;//宽图片型，图片最大缩放比例：缩至与图片高度与屏幕高度一致。
        } else {
            // 这说明图片属于长图片型，需要适配高度。
            smallScale = (float) getHeight() / bitmap.getHeight();//长图片型，图片最小缩放比例：缩至与图片高度与屏幕高度一致。
            bigScale = (float) getWidth() / bitmap.getWidth() * OVER_SCALE_FACTOR;//长图片型，图片最大缩放比例：缩至与图片宽度与屏幕宽度一致。
        }
        currentScale = smallScale;

        Log.e("xbc", "onSizeChanged:" + originalOffsetX + "," + originalOffsetY);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.e("xbc", "mScaleGestureDetector.isInProgress():" + mScaleGestureDetector.isInProgress());
        boolean result = mScaleGestureDetector.onTouchEvent(event);
        if (!mScaleGestureDetector.isInProgress()) {
            isInScaleProgress = false;
            result = mGestureDetector.onTouchEvent(event);
        } else {
            isInScaleProgress = true;
        }
        return result;
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) return;
        float scaleFraction = (currentScale - smallScale) / (bigScale - smallScale);
        canvas.translate(offsetX * scaleFraction, offsetY * scaleFraction);//* scaleFraction是为了让缩放时 偏移也跟着偏
        //实时缩放比例
//        float scale = smallScale + (bigScale - smallScale) * scaleFraction;
//        currentScale;
        canvas.scale(currentScale, currentScale, getWidth() / 2, getHeight() / 2);
        canvas.drawBitmap(bitmap, originalOffsetX, originalOffsetY, paint);
    }

    public float getCurrentScale() {
        return currentScale;
    }

    public void setCurrentScale(float currentScale) {
        this.currentScale = currentScale;
        invalidate();
    }

    private ObjectAnimator getScaleAnimator() {
        if (scaleAnimator == null) {
            scaleAnimator = ObjectAnimator.ofFloat(this, "currentScale", 0f);
        }
        scaleAnimator.setFloatValues(smallScale, bigScale);
        return scaleAnimator;
    }

    private void fixOffsets() {
        // 处理移动边界，到达图片边界则不能再移动(上下左右)
        // X,Y最大的移动距离 maxDistance 必大于0
        float maxWDistance = (bitmap.getWidth() * bigScale - getWidth()) / 2;
        float maxHDistance = (bitmap.getHeight() * bigScale - getHeight()) / 2;

//            Log.e("xbc", String.format("offsetX:%f , offsetY:%f , maxW:%f , maxH:%f", offsetX, offsetY, maxWDistance, maxHDistance));

        //offsetX>0，取其小，offsetX<0,取其大
        if (offsetX > 0) {
            offsetX = Math.min(offsetX, maxWDistance);//向右移动
        } else {
            offsetX = Math.max(offsetX, -maxWDistance);//向左移动
        }

            /*
                下面这种写法的结果与上面一样
                offsetX = Math.min(offsetX, maxWDistance);
                offsetX = Math.max(offsetX, -maxWDistance);
            */

        //offsetY>0，取其小，offsetY<0,取其大
        if (offsetY > 0) {
            offsetY = Math.min(offsetY, maxHDistance);//向下移动
        } else {
            offsetY = Math.max(offsetY, -maxHDistance);//向上移动
        }
    }

    private void fitScales() {
        currentScale = Math.min(currentScale, bigScale);
        currentScale = Math.max(currentScale, smallScale);
        big = currentScale > smallScale;
    }

    class HenOnGestureListener extends SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent down, MotionEvent e2, float distanceX, float distanceY) {
            // distanceX  e1.x-e2.x
            if (big) {//只有图片被放大时可以移动
//            Log.e("xbc", String.format("distanceX:%f,distanceY%f", distanceX, distanceY));
                // 计算图片移动的坐标偏移值（相对于图片中点）
                // <--  offsetX<0
                // -->  offsetX>0
                offsetX -= distanceX;
                offsetY -= distanceY;
                fixOffsets();
                invalidate();
            }

            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent down, MotionEvent e2, float velocityX, float velocityY) {
            // scroller 处理滑动   scroller.computeScrollOffset()方法计算过程
            if (big) {//只有图片被放大时可以滑动
                Log.e("xbc", String.format("onFling:offsetX:%f , offsetY:%f", offsetX, offsetY));

                // X,Y最大的移动距离 maxDistance 必大于0
                float maxWDistance = (bitmap.getWidth() * bigScale - getWidth()) / 2;
                float maxHDistance = (bitmap.getHeight() * bigScale - getHeight()) / 2;

                int startX = (int) offsetX;//滑动起始点x
                int startY = (int) offsetY;//滑动起始点y
                int minX = (int) -maxWDistance;//x最小值 右侧最大滑动距离
                int maxX = (int) maxWDistance;//x最大值 左侧最大滑动距离
                int minY = (int) -maxHDistance;//y最小值 底部最大滑动距离
                int maxY = (int) maxHDistance;//y最大值 顶部最大滑动距离
                int overX = 100;
                int overY = 100;
                /*
                 * scroller.fling()其实是在制定一个类似动画的帧任务，通过postOnAnimation(runnable)方式从下一帧开始执行，runnable中递归执行下一帧
                 * scroller.computeScrollOffset()是在每一帧中计算当前动画帧任务执行的中间状态。
                 */
                scroller.fling(startX, startY, (int) velocityX, (int) velocityY, minX, maxX, minY, maxY, overX, overY);
                postOnAnimation(mFlyingRunner);//
            }
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            big = !big;
            if (big) {
                //处理双击缩放轴心
                fixAxis(e.getX(), e.getY(), bigScale);
                fixOffsets();//修复边界
                getScaleAnimator().start();
            } else {
                getScaleAnimator().reverse();
            }
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    }

    private void fixAxis(float x, float y, float scale) {
        offsetX = (x - getWidth() / 2) - (x - getWidth() / 2) * scale / smallScale;
        offsetY = (y - getHeight() / 2) - (y - getHeight() / 2) * scale / smallScale;
    }

    class FlyingRunner implements Runnable {

        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                offsetX = scroller.getCurrX();
                offsetY = scroller.getCurrY();
                invalidate();
                postOnAnimation(this);
            }
        }
    }

    private void fixScaleAxis(float x, float y, float scale) {
        offsetX = (x - getWidth() / 2) - (x - getWidth() / 2) * scale / smallScale;
        offsetY = (y - getHeight() / 2) - (y - getHeight() / 2) * scale / smallScale;
    }

    class HenOnScaleGestureListener implements ScaleGestureDetector.OnScaleGestureListener {

        float initScale;
        float axisX, axisY;

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            currentScale = initScale * detector.getScaleFactor();
//            fixScaleAxis(axisX, axisY, currentScale);//FIXME 需要处理捏合轴心
            invalidate();
            return false;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            initScale = currentScale;
            axisX = detector.getFocusX();
            axisY = detector.getFocusY();
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            Log.e("xbc", "onScaleEnd:" + currentScale);
            fitScales();
            invalidate();
        }
    }
}
