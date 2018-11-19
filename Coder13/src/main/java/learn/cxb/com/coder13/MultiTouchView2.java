package learn.cxb.com.coder13;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import module.cxb.com.coderlib.Utils;

/**
 * 协作型，使用多指的中心点处理事件
 */
public class MultiTouchView2 extends View {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    float downX, downY;
    float originalOffsetX, originalOffsetY;
    float offsetX, offsetY;
    int trackingPointerId;
    float focusX, focusY;
    SweepGradient mSweepGradient;
    int radius = 400;
    int sweep = 270;

    public MultiTouchView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        bitmap = Utils.getBitmap(context.getResources(), R.drawable.avatar_rengwuxian);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSweepGradient = new SweepGradient(getWidth() / 2,
                                           getHeight() / 2,
                                           new int[]{Color.rgb(99, 212, 155), Color.rgb(228, 179, 106), Color.rgb(238, 111, 68)},
                                           new float[]{0.0f * sweep / 360, 0.5f * sweep / 360, 1.0f * sweep / 360}
        );

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint);
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent e) {

        // 找焦点
        float sumX = 0, sumY = 0;
        boolean isPointerUp = e.getActionMasked() == MotionEvent.ACTION_POINTER_UP;
        int pointerCount = e.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            if (isPointerUp && i == e.getActionIndex()) {
                // 多点的手指抬起,需要刨除调，重新计算焦点。
            } else {
                sumX += e.getX(i);
                sumY += e.getY(i);
            }
        }
        if (isPointerUp) {
            pointerCount--;
        }

        focusX = sumX / pointerCount;
        focusY = sumY / pointerCount;

        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP://收到这个事件时，e.getPointerCount()还没有发生改变。
                downX = focusX;
                downY = focusY;
                originalOffsetX = offsetX;//初始偏移
                originalOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = originalOffsetX + focusX - downX;
                offsetY = originalOffsetY + focusY - downY;
                break;

        }
        invalidate();
        return true;
    }

    private void drawGradient(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(60);
        paint.setShader(mSweepGradient);

        canvas.rotate(180, getWidth() / 2, getHeight() / 2);
        canvas.drawArc(
                getWidth() / 2 - radius,
                getHeight() / 2 - radius,
                getWidth() / 2 + radius,
                getHeight() / 2 + radius,
                0,
                sweep,
                false,
                paint
        );
    }

}
