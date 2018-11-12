package module.cxb.com.coder07;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import module.cxb.com.coderlib.Utils;

public class ImageTextView extends View {

    TextPaint mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    Paint.FontMetrics mMetrics;
    Rect mBounds = new Rect();
    String text = "它测量的是文字绘制时所占用的宽度（关键词：占用）。前面已经讲过，一个文字在界面中，往往需要占用比他的实际显示宽度更多一点的宽度，以此来让文字和文字之间保留一些间距，不会显得过于拥挤。上面的这幅图，我并没有设置 setLetterSpacing() ，这里的 letter spacing 是默认值 0，但你可以看到，图中每两个字母之间都是有空隙的。另外，下方那条用于表示文字宽度的横线，在左边超出了第一个字母 H 一段距离的，在右边也超出了最后一个字母 r（虽然右边这里用肉眼不太容易分辨），而就是两边的这两个「超出」，导致了 measureText() 比 getTextBounds() 测量出的宽度要大一些。上图中蓝色和红色的线，它们的作用是限制所有字形（ glyph ）的顶部和底部范围。 \n" +
            "除了普通字符，有些字形的显示范围是会超过 ascent 和 descent 的，而 top 和 bottom 则限制的是所有字形的显示范围，包括这些特殊字形。例如上图的第二行文字里，就有两个泰文的字形分别超过了 ascent 和 descent 的限制，但它们都在 top 和 bottom 两条线的范围内。具体到 Android 的绘制中， top 的值是图中蓝线和 baseline 的相对位移，它的值为负（因为它在 baseline 的上方）.";
    Bitmap mBitmap;
    int b_width = 400;
    RectF mRectF;
    float[] widths = new float[1];


    public ImageTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {
        mPaint.setTextSize(48);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "Quicksand-Regular.ttf"));
        mBitmap = Utils.getAvatar(getResources(), b_width);
        mMetrics = mPaint.getFontMetrics();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(getWidth() - b_width - 200, 200, getWidth() - 200, 200 + b_width);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, mRectF.left, mRectF.top, mPaint);

//        canvas.drawText();
//        int count = mPaint.breakText(text, 0, text.length(), true, getWidth(), widths);
//        canvas.drawText(text, 0, count, 0, -mMetrics.top, mPaint);
//
//        int oldIndex = count;
//        count = mPaint.breakText(text, oldIndex, text.length(), true, getWidth(), widths);
//        canvas.drawText(text, oldIndex, oldIndex + count, 0, -mMetrics.top + mPaint.getFontSpacing(), mPaint);

        int start = 0;
        int offset = 0;

        float ascender = 0;
        float descender = 0;
        float maxWidth = getWidth();
        while (start < text.length()) {
            ascender = -mMetrics.top + offset + mMetrics.ascent;
            descender = -mMetrics.top + offset + mMetrics.descent;

            if ((ascender > mRectF.top && ascender < mRectF.bottom) || (descender > mRectF.top && descender < mRectF.bottom)) {
                //绕
                maxWidth = mRectF.left;
            } else {//不绕
                maxWidth = getWidth();
            }

            //左
            int count = mPaint.breakText(text, start, text.length(), true, maxWidth, widths);
            canvas.drawText(text, start, start + count, 0, -mMetrics.top + offset, mPaint);
            start += count;
            //右
            if (maxWidth < getWidth()) {
                maxWidth = getWidth() - mRectF.right;
                count = mPaint.breakText(text, start, text.length(), true, maxWidth, widths);
                if (count > 0) {
                    canvas.drawText(text, start, start + count, mRectF.right, -mMetrics.top + offset, mPaint);
                    start += count;
                }
            }

            offset += mPaint.getFontSpacing();
        }
    }

}
