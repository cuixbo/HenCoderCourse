package module.cxb.com.coder06;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class PieChart extends View {

    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    RectF mRectF;
    int radius = 400;
    int length = 50;
    int PULLED_OUT_INDEX = 2;
    Path mPath = new Path();
    int[] angles = {60, 100, 120, 80};
    int[] colors = {Color.parseColor("#2979FF"), Color.parseColor("#C2185B"),
            Color.parseColor("#009688"), Color.parseColor("#FF8F00")};

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setStrokeWidth(2);

        mRectF = new RectF(getWidth() / 2 - radius,
                           getHeight() / 2 - radius,
                           getWidth() / 2 + radius,
                           getHeight() / 2 + radius);

//        mPath.arcTo(mRectF, (float) (90 + angle / 2), (float) (360 - angle));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int currentAngle = 0;
        for (int i = 0; i < angles.length; i++) {
            mPaint.setColor(colors[i]);

            canvas.save();
            if (i == PULLED_OUT_INDEX) {
                canvas.translate(length * (float) Math.cos(Math.toRadians(currentAngle + angles[i] / 2)),
                                 length * (float) Math.sin(Math.toRadians(currentAngle + angles[i] / 2)));
            }
            canvas.drawArc(mRectF, currentAngle, angles[i], true, mPaint);
            canvas.restore();
            currentAngle += angles[i];
        }

    }
}
