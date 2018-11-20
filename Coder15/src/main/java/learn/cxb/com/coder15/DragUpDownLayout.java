package learn.cxb.com.coder15;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class DragUpDownLayout extends FrameLayout {

    View view;
    ViewDragHelper dragHelper;
    ViewDragHelper.Callback dragCallback = new DragCallback();
    ViewConfiguration viewConfiguration;
    float minVel;

    public DragUpDownLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        dragHelper = ViewDragHelper.create(this, dragCallback);
        viewConfiguration = ViewConfiguration.get(context);
        minVel = viewConfiguration.getScaledMinimumFlingVelocity();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        view = findViewById(R.id.view);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class DragCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(@NonNull View child, int i) {
            return child == view;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
            return top;
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            Log.e("xbc", "yvel:" + yvel);
            if (Math.abs(yvel) < minVel) {// 速度小于最小快滑速度
                dragHelper.settleCapturedViewAt(0, yvel > 0 ? 0 : 0);//fixme ???
            } else {
                if (releasedChild.getTop() < getHeight() - releasedChild.getBottom()) {
                    dragHelper.settleCapturedViewAt(0, 0);
                } else {
                    dragHelper.settleCapturedViewAt(0, getHeight() - releasedChild.getHeight());
                }
            }
            postInvalidateOnAnimation();
        }
    }


}
