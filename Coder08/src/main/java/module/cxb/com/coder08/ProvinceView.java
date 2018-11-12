package module.cxb.com.coder08;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import module.cxb.com.coderlib.Utils;

public class ProvinceView extends View {

    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    String province = "北京市";

    public String getProvince() {
        return province;
    }

    public ProvinceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    {
        paint.setTextSize(Utils.dpToPixel(40));
        paint.setTextAlign(Paint.Align.CENTER);
    }

    public void setProvince(String province) {
        this.province = province;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(province, getWidth() / 2, getHeight() / 2, paint);
    }
}
