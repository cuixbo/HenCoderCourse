package module.cxb.com.coder07;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

public class SportsView extends View {
    private static final float RING_WIDTH = 20 * 3;
    private static final float RADIUS = 150 * 3;
    private static final int CIRCLE_COLOR = Color.parseColor("#90A4AE");
    private static final int HIGHLIGHT_COLOR = Color.parseColor("#FF4081");

    TextPaint mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    Paint.FontMetrics mMetrics;
    Rect mBounds = new Rect();

    public SportsView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mPaint.setTextSize(300);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Quicksand-Regular.ttf"));
        mMetrics = mPaint.getFontMetrics();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //draw ring gray
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(RING_WIDTH);
        mPaint.setColor(CIRCLE_COLOR);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, RADIUS, mPaint);

        //draw ring highlight
        mPaint.setColor(HIGHLIGHT_COLOR);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(getWidth() / 2 - RADIUS,
                       getHeight() / 2 - RADIUS,
                       getWidth() / 2 + RADIUS,
                       getHeight() / 2 + RADIUS,
                       -90,
                       210,
                       false,
                       mPaint);
        String text = "ajE∮Ã";
        int baseline = getHeight() / 2;
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(1);
//        canvas.drawText(text, getWidth() / 2, baseline, mPaint);
        canvas.drawLine(0, baseline, getWidth(), baseline, mPaint);//baseline

        float offset = (mMetrics.ascent + mMetrics.descent) / 2;
        canvas.drawText(text, getWidth() / 2, baseline - offset, mPaint);
        canvas.drawLine(0, baseline - offset, getWidth(), baseline - offset, mPaint);//center
    }


    void test(Canvas canvas) {
        mPaint.setTextAlign(Paint.Align.LEFT);
        int baseline = 200;
        String text = "ajE∮Ã";
        canvas.drawText(text, 0, baseline, mPaint);
        canvas.drawLine(0, baseline, getWidth(), baseline, mPaint);//baseline

        mPaint.setTextSize(660);
        mPaint.getTextBounds(text, 0, text.length(), mBounds);
        canvas.drawText(text, 0, 400, mPaint);
        System.out.println("Bounds:" + mBounds.left + "," + mBounds.top + "," + mBounds.right + "," + mBounds.bottom);

        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(360);
        mMetrics = mPaint.getFontMetrics();
        baseline = 800;
        canvas.drawText(text, getWidth() / 2, baseline, mPaint);
        canvas.drawRect(mBounds, mPaint);

        mPaint.getTextBounds(text, 0, text.length(), mBounds);

        mPaint.setColor(Color.RED);
        canvas.drawLine(0, baseline, getWidth(), baseline, mPaint);//baseline

        mPaint.setColor(Color.GREEN);
        canvas.drawLine(0, baseline + mMetrics.ascent, getWidth(), baseline + mMetrics.ascent, mPaint);//ascenderLine

        mPaint.setColor(Color.GREEN);
        canvas.drawLine(0, baseline + mMetrics.descent, getWidth(), baseline + mMetrics.descent, mPaint);//ascenderLine

        mPaint.setColor(Color.BLUE);
        canvas.drawLine(0, baseline + mMetrics.top, getWidth(), baseline + mMetrics.top, mPaint);//top

        mPaint.setColor(Color.BLUE);
        canvas.drawLine(0, baseline + mMetrics.bottom, getWidth(), baseline + mMetrics.bottom, mPaint);//bottom

        System.out.println("mMetrics.leading:" + mMetrics.leading);
        System.out.println("Bounds:" + mBounds.left + "," + mBounds.top + "," + mBounds.right + "," + mBounds.bottom);

        mPaint.setColor(0x8800FF00);
        float width = mPaint.measureText(text);
        canvas.translate(getWidth() / 2 - width / 2, baseline);
        canvas.drawRect(mBounds, mPaint);
    }
}
