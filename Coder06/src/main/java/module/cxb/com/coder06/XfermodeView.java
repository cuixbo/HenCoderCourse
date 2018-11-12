package module.cxb.com.coder06;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.View;

public class XfermodeView extends View {
    private static int W = 256;
    private static int ROW_MAX = 4;   // number of samples per row

    private Bitmap mSrcB;
    private Bitmap mDstB;
    private Shader mBG;     // background checker-board pattern
    private RectF bounds;
    Paint paint;

    private static final Xfermode[] sModes = {
            new PorterDuffXfermode(PorterDuff.Mode.CLEAR),
            new PorterDuffXfermode(PorterDuff.Mode.SRC),
            new PorterDuffXfermode(PorterDuff.Mode.DST),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OVER),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_IN),
            new PorterDuffXfermode(PorterDuff.Mode.DST_IN),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.DST_OUT),
            new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP),
            new PorterDuffXfermode(PorterDuff.Mode.XOR),
            new PorterDuffXfermode(PorterDuff.Mode.DARKEN),
            new PorterDuffXfermode(PorterDuff.Mode.LIGHTEN),
            new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY),
            new PorterDuffXfermode(PorterDuff.Mode.SCREEN)
    };

    private static final String[] sLabels = {
            "Clear", "Src", "Dst", "SrcOver",
            "DstOver", "SrcIn", "DstIn", "SrcOut",
            "DstOut", "SrcATop", "DstATop", "Xor",
            "Darken", "Lighten", "Multiply", "Screen"
    };

    // create a bitmap with a circle, used for the "dst" image
    static Bitmap makeDst(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xffD73964);
        c.drawOval(new RectF(w * 5 / 16f, h / 16f, w * 15 / 16f, h * 11 / 16f), p);
        return bm;
    }

    // create a bitmap with a rect, used for the "src" image
    static Bitmap makeSrc(int w, int h) {
        Bitmap bm = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bm);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);

        p.setColor(0xff4696EC);
        c.drawRect(w / 16f, h * 6 / 16f, w * 10 / 16f, h * 15 / 16f, p);
        return bm;
    }

    public XfermodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bounds = new RectF(0, 0, W, W);
        W = getWidth() / ROW_MAX;
        mSrcB = makeSrc(W, W);
        mDstB = makeDst(W, W);
        paint = new Paint();
//        paint.setFilterBitmap(false);
        int width = 2;
        int[] pxs = new int[width * width];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < width; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        pxs[i * width + j] = 0xFFCCCCCC;
                    } else {
                        pxs[i * width + j] = 0xFFFFFFFF;
                    }
                } else {
                    if (j % 2 == 0) {
                        pxs[i * width + j] = 0xFFFFFFFF;
                    } else {
                        pxs[i * width + j] = 0xFFCCCCCC;
                    }
                }
            }
        }
//        Log.e("xbc", Arrays.toString(pxs));

        // make a ckeckerboard pattern
        Bitmap bm = Bitmap.createBitmap(pxs, width, width,
                                        Bitmap.Config.RGB_565);
        mBG = new BitmapShader(bm,
                               Shader.TileMode.REPEAT,
                               Shader.TileMode.REPEAT);
        Matrix m = new Matrix();
        m.setScale(6, 6);
        mBG.setLocalMatrix(m);
        paint.setShader(mBG);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setShader(mBG);
//        canvas.drawRect(0, 0, W, H, paint);
        Paint labelP = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelP.setTextSize(24);
        labelP.setTextAlign(Paint.Align.LEFT);

        for (int i = 0; i < sModes.length; i++) {
            int j = i / 4;
            int x = i % 4;

            bounds = new RectF(x * W, j * W, (x + 1) * W, (j + 1) * W);
            paint.setStyle(Paint.Style.STROKE);
            paint.setShader(null);
            canvas.drawRect(bounds, paint);

            paint.setStyle(Paint.Style.FILL);
            paint.setShader(mBG);
            canvas.drawRect(bounds, paint);

            int layerId = canvas.saveLayer(bounds, paint);
            canvas.drawBitmap(mDstB, x * W, j * W, paint);//圆
            paint.setXfermode(sModes[i]);
            canvas.drawBitmap(mSrcB, x * W, j * W, paint);//方
            paint.setXfermode(null);
            canvas.restoreToCount(layerId);

            // draw the label
            canvas.drawText(sLabels[i],
                            x * W, j * W + 20, labelP);
        }
    }
}
