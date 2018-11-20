package learn.cxb.com.coder15;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class DragHelperGridView extends ViewGroup {

    private static final int COLUMNS = 2;
    private static final int ROWS = 3;

    ViewDragHelper dragHelper;

    public DragHelperGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        dragHelper = ViewDragHelper.create(this, new DragCallback());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            int left = i % 2 == 0 ? 0 : getWidth() / 2;
            int top = i / 2 * getHeight() / ROWS;
            int right = left + getWidth() / 2;
            int bottom = top + getHeight() / ROWS;
            child.layout(left, top, right, bottom);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return dragHelper.shouldInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        dragHelper.processTouchEvent(event);
        return true; // 这里需要消费
    }

    @Override
    public void computeScroll() {
        if (dragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class DragCallback extends ViewDragHelper.Callback {

        int left, top;

        @Override
        public boolean tryCaptureView(@NonNull View view, int i) {
            Log.e("xbc", "tryCaptureView");
            return true;
        }

        @Override
        public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
//            Log.e("xbc", "clampViewPositionHorizontal");
            return left;
        }

        @Override
        public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
//            Log.e("xbc", "clampViewPositionVertical");
            return top;
        }

        @Override
        public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
            Log.e("xbc", "onViewCaptured:");
            capturedChild.setElevation(getElevation() + 1);// 选中View增加海拔
            left = capturedChild.getLeft();
            top = capturedChild.getTop();
        }

        @Override
        public void onViewDragStateChanged(int state) {
            Log.e("xbc", "onViewDragStateChanged:" + state);
            if (state == ViewDragHelper.STATE_IDLE) {// 恢复海拔
                View capturedView = dragHelper.getCapturedView();
                if (capturedView != null) {
                    capturedView.setElevation(getElevation() - 1);
                }
            }
        }

        @Override
        public void onViewPositionChanged(@NonNull View changedView, int left, int top, int dx, int dy) {
//            Log.e("xbc", "onViewPositionChanged");
        }

        @Override
        public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
            Log.e("xbc", "onViewReleased:" + left + "," + top);
            //选中View进行释放，回归原位
            dragHelper.settleCapturedViewAt(left, top);
            postInvalidateOnAnimation();
        }
    }


}
