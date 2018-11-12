package module.cxb.com.coder06;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

public class AvatarView extends View {
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    RectF mRectF;
    int radius = 400;
    int PULLED_OUT_INDEX = 2;
    Path mPath = new Path();
    Bitmap mBitmap;

    private static final int WIDTH = 800;
    private static final int PADDING = 50;
    private static final int EDGE_WIDTH = 10;

    Xfermode mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);

    public AvatarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
//        mPaint.setColor();
//        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(EDGE_WIDTH);
        mRectF = new RectF(getWidth() / 2 - WIDTH / 2, getHeight() / 2 - WIDTH / 2, getWidth() / 2 + WIDTH / 2, getHeight() / 2 + WIDTH / 2);
        mBitmap = getAvatar(WIDTH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawOval(mRectF, mPaint);
        int layerId = canvas.saveLayer(mRectF, mPaint);
        canvas.drawOval(getWidth() / 2 - WIDTH / 2 + EDGE_WIDTH, getHeight() / 2 - WIDTH / 2 + EDGE_WIDTH,
                        getWidth() / 2 + WIDTH / 2 - EDGE_WIDTH, getHeight() / 2 + WIDTH / 2 - EDGE_WIDTH, mPaint);
        mPaint.setXfermode(mXfermode);
        canvas.drawBitmap(mBitmap, getWidth() / 2 - WIDTH / 2, getHeight() / 2 - WIDTH / 2, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(layerId);

//        BitmapShader bitmapShader=new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
//
//        mPaint.setShader(bitmapShader);
//
//        canvas.drawOval(mRectF, mPaint);
    }

    Bitmap getAvatar(int width) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), R.drawable.avatar_rengwuxian, options);
        options.inJustDecodeBounds = false;
        options.inDensity = options.outWidth;
        options.inTargetDensity = width;
        return BitmapFactory.decodeResource(getResources(), R.drawable.avatar_rengwuxian, options);
    }

}
