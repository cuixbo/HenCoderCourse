package learn.cxb.com.coder15;

import android.content.ClipData;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DragToCollectLayout extends RelativeLayout {

    ImageView avatarView;
    ImageView logoView;
    LinearLayout collectorLayout;

    OnLongClickListener dragStarter = new OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            ClipData clipData = ClipData.newPlainText("name", v.getContentDescription());
            return v.startDrag(clipData, new DragShadowBuilder(v), v, 0);
        }
    };

    CollectListener dragListener = new CollectListener();

    public DragToCollectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        avatarView = findViewById(R.id.avatarView);
        logoView = findViewById(R.id.logoView);
        collectorLayout = findViewById(R.id.collectorLayout);


        avatarView.setOnLongClickListener(dragStarter);
        logoView.setOnLongClickListener(dragStarter);
        collectorLayout.setOnDragListener(dragListener);

    }

    private class CollectListener implements OnDragListener {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            Log.e("xbc", "action:" + event.getAction() + "，" + v.hashCode());

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED://开始
                    v.setVisibility(VISIBLE);
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED://有view进入
                    v.setBackgroundColor(0xffff0033);
                    break;
                case DragEvent.ACTION_DRAG_ENDED://结束

                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(0xff78909C);
                    break;
                case DragEvent.ACTION_DROP:
                    v.setBackgroundColor(0xff33ff33);
                    TextView textView = new TextView(getContext());
                    textView.setTextSize(16);
                    textView.setText(event.getClipData().getItemAt(0).getText());
                    ((LinearLayout) v).addView(textView);
                    break;
            }
            return true;
        }
    }
}
