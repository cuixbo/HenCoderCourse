package module.cxb.com.coder06;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class Dashboard extends View {

    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    RectF mRectF;
    int padding = 150;
    int radius = 400;
    Path mPath = new Path();

    PathDashPathEffect mPathDashPathEffect;
    Path mDashPath = new Path();
    PathMeasure mPathMeasure;
    int count = 20;
    int dashWidth = 6;
    int angle = 120;
    int length = 300;


    public Dashboard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(6);

        mRectF = new RectF(getWidth() / 2 - radius,
                           getHeight() / 2 - radius,
                           getWidth() / 2 + radius,
                           getHeight() / 2 + radius);

        mPath.arcTo(mRectF, (float) (90 + angle / 2), (float) (360 - angle));
        mPathMeasure = new PathMeasure(mPath, false);
        float length = mPathMeasure.getLength();

        mDashPath.addRect(0, 0, dashWidth, 30, Path.Direction.CW);
        mPathDashPathEffect = new PathDashPathEffect(mDashPath, (length - dashWidth) / count, 0, PathDashPathEffect.Style.ROTATE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        canvas.drawCircle(200, 200, 130, mPaint);

//        canvas.drawArc(mRectF,(float) 150,(float) 240,false,mPaint);
        mPaint.setPathEffect(mPathDashPathEffect);
        canvas.drawPath(mPath, mPaint);
        mPaint.setPathEffect(null);
        canvas.drawPath(mPath, mPaint);
        int mark = 5;
        canvas.drawLine(
                getWidth() / 2,
                getHeight() / 2,
                getWidth() / 2 + length * (float) Math.cos(Math.toRadians(getAngleFromMark(mark))),
                getHeight() / 2 + length * (float) Math.sin(Math.toRadians(getAngleFromMark(mark))),
                mPaint
        );
    }

    float getAngleFromMark(int mark) {
        return angle / 2 + 90 + (360 - angle) / count * mark;
    }
}
