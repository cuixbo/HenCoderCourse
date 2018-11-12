package module.cxb.com.coder07;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import module.cxb.com.coderlib.Utils;

public class CameraView extends View {


    TextPaint mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    Paint.FontMetrics mMetrics;
    Rect mBounds = new Rect();
    Bitmap mBitmap;
    RectF mRectF;
    int width = 600;
    Camera mCamera;

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mPaint.setTextSize(48);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Quicksand-Regular.ttf"));
        mBitmap = Utils.getAvatar(getResources(), width);
        mMetrics = mPaint.getFontMetrics();
        mCamera = new Camera();
        mCamera.rotateX(45);
        mCamera.setLocation(0, 0, Utils.getZForCamera());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCamera(canvas);
//        test(canvas);

    }

    void drawCamera(Canvas canvas) {

        // 画上面
        canvas.save();
        canvas.translate(width / 2 + 100, width / 2 + 100);
        canvas.rotate(-20);
//        mCamera.applyToCanvas(canvas);
        canvas.clipRect(-width, -width, width, 0);
        canvas.rotate(20);
        canvas.translate(-width / 2 - 100, -width / 2 - 100);
        canvas.drawBitmap(mBitmap, 100, 100, mPaint);
        canvas.restore();

        // 画下面
        canvas.save();
        canvas.translate(width / 2 + 100, width / 2 + 100);
        canvas.rotate(-20);
        mCamera.applyToCanvas(canvas);
        canvas.clipRect(-width, 0, width, width);
        canvas.rotate(20);
        canvas.translate(-width / 2 - 100, -width / 2 - 100);
        canvas.drawBitmap(mBitmap, 100, 100, mPaint);
        canvas.restore();
    }

    void test(Canvas canvas) {
        canvas.translate(200, 200);
        canvas.rotate(45);
        canvas.translate(-200, -200);
        canvas.drawBitmap(mBitmap, 0, 0, mPaint);
//        canvas.drawBitmap(mBitmap, 0, 0, mPaint);

    }


}
