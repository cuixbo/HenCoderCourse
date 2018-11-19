package learn.cxb.com.coder14;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.OverScroller;

/**
 * 模拟"两页"ViewPager
 * 使用 OverScroller，VelocityTracker
 * <p>
 * 页面内容滑动 scrollTo
 * 回弹效果 OverScroller
 * 快速滑动 VelocityTracker
 */
public class TwoPager extends ViewGroup {

    float downX, downY, downScrollX;
    float maxVelocity, minVelocity;

    ViewConfiguration viewConfiguration;
    OverScroller overScroller;
    VelocityTracker velocityTracker = VelocityTracker.obtain();

    public TwoPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        overScroller = new OverScroller(context);
        maxVelocity = ViewConfiguration.get(context).getScaledMaximumFlingVelocity();
        minVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.layout(l + getWidth() * i, t, r + getWidth() * i, b);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
            velocityTracker.clear();
        }
        velocityTracker.addMovement(e);// 追踪事件

        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downX = e.getX();
                downY = e.getY();
                downScrollX = getScrollX();// 已 ScrollX
                break;
            case MotionEvent.ACTION_MOVE:// 滑动
                float dx = downX - e.getX() + downScrollX;
                if (dx > getWidth()) {// 处理边界
                    dx = getWidth();
                } else if (dx < 0) {
                    dx = 0;
                }
//                Log.e("xbc", "dx:" + dx);
                scrollTo((int) dx, 0);
                break;
            case MotionEvent.ACTION_UP:// 处理回弹
                velocityTracker.computeCurrentVelocity(1000, maxVelocity);
                float xVelocity = velocityTracker.getXVelocity();

                int distance;
                // 滑动速度
                if (Math.abs(xVelocity) < minVelocity) {// 正常滑动
                    if (getScrollX() >= getWidth() / 2) {// 滑过一半
                        distance = getWidth() - getScrollX();
                    } else {
                        distance = 0 - getScrollX();
                    }
                } else {// 快速滑动
                    distance = xVelocity < 0 ? getWidth() - getScrollX() : 0 - getScrollX();
                }
                overScroller.startScroll(getScrollX(), 0, distance, 0);
                postInvalidateOnAnimation();
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (overScroller.computeScrollOffset()) {
            scrollTo(overScroller.getCurrX(), overScroller.getCurrY());
            postInvalidateOnAnimation();
        }
    }
}
