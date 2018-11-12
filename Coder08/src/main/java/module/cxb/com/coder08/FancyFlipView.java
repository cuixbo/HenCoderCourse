package module.cxb.com.coder08;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import module.cxb.com.coderlib.Utils;

public class FancyFlipView extends View {

    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint.FontMetrics mMetrics;
    Rect mBounds = new Rect();
    Bitmap mBitmap;
    RectF mRectF;
    int width = 600;
    int padding = 200;
    Camera mCamera;

    int rotateDegree = 0;
    int flipDegreeBottom = 0;
    int flipDegreeTop = 0;

    public FancyFlipView(Context context, @androidx.annotation.Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mBitmap = Utils.getAvatar(getResources(), width);
        mCamera = new Camera();
//        mCamera.rotateX(45);
        mCamera.setLocation(0, 0, Utils.getZForCamera());
    }


    public int getRotateDegree() {
        return rotateDegree;
    }

    public void setRotateDegree(int rotateDegree) {
        this.rotateDegree = rotateDegree;
        invalidate();
    }

    public int getFlipDegreeBottom() {
        return flipDegreeBottom;
    }

    public void setFlipDegreeBottom(int flipDegreeBottom) {
        this.flipDegreeBottom = flipDegreeBottom;
        invalidate();
    }

    public int getFlipDegreeTop() {
        return flipDegreeTop;
    }

    public void setFlipDegreeTop(int flipDegreeTop) {
        this.flipDegreeTop = flipDegreeTop;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画顶部
        canvas.save();
        canvas.translate((padding + width / 2), (padding + width / 2));
        canvas.rotate(-rotateDegree);
        mCamera.save();
        mCamera.rotateX(flipDegreeTop);
        mCamera.applyToCanvas(canvas);
        mCamera.restore();
        canvas.clipRect(-width, -width, width, 0);
        canvas.rotate(rotateDegree);
        canvas.translate(-(padding + width / 2), -(padding + width / 2));
        canvas.drawBitmap(mBitmap, padding, padding, mPaint);
        canvas.restore();

        // 画底部
        canvas.save();
        canvas.translate((padding + width / 2), (padding + width / 2));
        canvas.rotate(-rotateDegree);
        mCamera.save();
        mCamera.rotateX(flipDegreeBottom);
        mCamera.applyToCanvas(canvas);
        mCamera.restore();
        canvas.clipRect(-width, 0, width, width);
        canvas.rotate(rotateDegree);
        canvas.translate(-(padding + width / 2), -(padding + width / 2));
        canvas.drawBitmap(mBitmap, padding, padding, mPaint);
        canvas.restore();
        // 折叠底部  Camera 45度
        // 旋转折叠  canvas逆时针270度
        // 折叠顶部  Camera 反向 45度

    }


}
