package learn.cxb.com.coder13;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * 各自为战型，记录每根手指的轨迹
 */
public class MultiTouchView3 extends View {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    int trackingPointerId;
    List<Path> mPaths = new ArrayList<>(10);

    SparseArray<Path> mPathSparseArray = new SparseArray<>(10);// 这里应该是以pointerId为key


    public MultiTouchView3(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (int i = 0; i < mPathSparseArray.size(); i++) {
            Path path = mPathSparseArray.valueAt(i);
            canvas.drawPath(path, paint);
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        // e.getActionIndex() 只在POINTER_UP与POINTER_DOWN中有用
        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                Path path = new Path();
                int pointerId = e.getPointerId(e.getActionIndex());
                path.moveTo(e.getX(e.getActionIndex()), e.getY(e.getActionIndex()));
                mPathSparseArray.append(pointerId, path);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                pointerId = e.getPointerId(e.getActionIndex());
                mPathSparseArray.delete(pointerId);
                break;
            case MotionEvent.ACTION_MOVE:
                // e.getActionIndex() 在此无效（0），需要遍历 e.getPointerCount() 取 index
                for (int i = 0; i < e.getPointerCount(); i++) {
                    pointerId = e.getPointerId(i);        // 获取pointerId
                    mPathSparseArray.get(pointerId).lineTo(e.getX(i), e.getY(i));
                }
                break;
        }
        invalidate();
        return true;
    }


}
