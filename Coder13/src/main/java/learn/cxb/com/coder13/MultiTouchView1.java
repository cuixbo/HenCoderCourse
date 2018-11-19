package learn.cxb.com.coder13;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import module.cxb.com.coderlib.Utils;

/**
 * 接力型，活跃的pointer为最后一个手指
 */
public class MultiTouchView1 extends View {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    float downX, downY;
    float originalOffsetX, originalOffsetY;
    float offsetX, offsetY;
    int trackingPointerId;

    public MultiTouchView1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        bitmap = Utils.getBitmap(context.getResources(), R.drawable.avatar_rengwuxian);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
    }

    /**
     * pointerId 会复用，id复用后，pointIndex会根据id重新分配。
     * index用与遍历的
     * <p>
     * e.getActionIndex(); 用于ACTION_POINTER_DOWN 和 ACTION_POINTER_UP 获取pointerIndex
     * e.getPointerId(pointerIndex)  return PointerId;
     * e.findPointerIndex(pointerId)  return PointerIndex;
     * e.getX(pointerIndex)  return x;
     * e.getX()==e.getX(0)
     *
     * @param e
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                trackingPointerId = e.getPointerId(0);
                downX = e.getX();
                downY = e.getY();
                originalOffsetX = offsetX;
                originalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_MOVE:
                int pointerIndex = e.findPointerIndex(trackingPointerId);
                float dx = e.getX(pointerIndex) - downX;
                float dy = e.getY(pointerIndex) - downY;
                offsetX = originalOffsetX + dx;
                offsetY = originalOffsetY + dy;
                Log.e("xbc",String.format("%d,%f,%f,%f,%f",pointerIndex,dx,dy,offsetX,offsetY));
                break;
            case MotionEvent.ACTION_POINTER_DOWN://记录活跃手指
                int actionIndex = e.getActionIndex();
                trackingPointerId = e.getPointerId(actionIndex);
                downX = e.getX(actionIndex);
                downY = e.getY(actionIndex);
                originalOffsetX = offsetX;
                originalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_POINTER_UP://重新记录活跃手指，交给inde最大那个
                actionIndex = e.getActionIndex();
                int pointerId = e.getPointerId(actionIndex);
                if (pointerId == trackingPointerId) {//抬起的手指正是当前活跃的手指
                    int newIndex;
                    if (actionIndex == e.getPointerCount() - 1) {//正好是最后一个手指
                        newIndex = e.getPointerCount() - 1 - 1;
                    } else {
                        newIndex = e.getPointerCount() - 1;
                    }
                    trackingPointerId = e.getPointerId(newIndex);
                    downX = e.getX(actionIndex);
                    downY = e.getY(actionIndex);
                    originalOffsetX = offsetX;
                    originalOffsetY = offsetY;
                }

                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }


//    /**
//     * id 会复用，id复用后，index会根据id重新分配。
//     * index用来遍历的
//     *
//     * @param e
//     * @return
//     */
//    @SuppressLint("ClickableViewAccessibility")
//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//        /**
//         *   e.getActionIndex(); 用于ACTION_POINTER_DOWN 和 ACTION_POINTER_UP 获取pointerIndex
//         *   e.getPointerId(pointerIndex)  return PointerId;
//         *   e.findPointerIndex(pointerId)  return PointerIndex;
//         *   e.getX(pointerIndex)  return x;
//         */
//        int index = e.getActionIndex();
//        switch (e.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                downX = e.getX();
//                downY = e.getY();
//                Log.e("xbc", "down:" + index);
//                trackingPointerId = e.getPointerId(0);
//                originalOffsetX = offsetX;
//                originalOffsetY = offsetY;
//                break;
//            case MotionEvent.ACTION_MOVE:
//                int pIndex = e.findPointerIndex(trackingPointerId);
//                pIndex = pIndex > 0 ? pIndex : 0;
//                offsetX = originalOffsetX + e.getX(pIndex) - downX;
//                offsetY = originalOffsetY + e.getY(pIndex) - downY;
//                System.out.println("xbc,move:");
//                Log.e("xbc", "move:" + index + ",id:" + trackingPointerId + ",index:" + e.findPointerIndex(trackingPointerId));
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                downX = e.getX(index);
//                downY = e.getY(index);
//                originalOffsetX = offsetX;
//                originalOffsetY = offsetY;
//                trackingPointerId = e.getPointerId(index);
//                Log.e("xbc", "pointer_down:" + index + ",id:" + trackingPointerId);
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                int pointerId = e.getPointerId(index);
//                if (pointerId == trackingPointerId) {
//                    int newIndex;
//                    if (index == e.getPointerCount() - 1) {
//                        newIndex = e.getPointerId(e.getPointerCount() - 2);
//                    } else {
//                        newIndex = e.getPointerId(e.getPointerCount() - 1);
//                    }
//                    trackingPointerId = e.getPointerId(newIndex);
//                    downX = e.getX(index);
//                    downY = e.getY(index);
//                    originalOffsetX = offsetX;
//                    originalOffsetY = offsetY;
//                }
////                trackingPointerId = e.getPointerId(e.getPointerCount() - 1);
//                Log.e("xbc", "pointer_up:" + index + ",id:" + trackingPointerId);
//                break;
//            case MotionEvent.ACTION_UP:
//                Log.e("xbc", "up:" + index);
//                break;
//        }
//        invalidate();
//        return true;
//    }

    //接力型，活动pointer为最后一根
}
