package com.example.cxb.coder10;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by xiaobocui on 2018/11/4.
 */

public class TagLayout extends ViewGroup {
    public static final String TAG = "TagLayout";
    private ArrayList<Rect> childrenBounds = new ArrayList<>(10);

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int pWidth = 0;
        int pHeight = 0;
        int usedWidth = 0;
        int usedHeight = 0;
        int maxHeight = 0;
        int maxWidth = 0;
        childrenBounds.clear();

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specWidth = MeasureSpec.getSize(widthMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, usedHeight);
            Log.e(TAG,"1."+child.getMeasuredWidth()+","+child.getMeasuredHeight());
            if (usedWidth + child.getMeasuredWidth() > specWidth) {//超范围了，要折行
                usedHeight += maxHeight;
                usedWidth = 0;
                maxHeight = 0;
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, usedHeight);
                Log.e(TAG,"2."+child.getMeasuredWidth()+","+child.getMeasuredHeight());
            }
            Rect bounds = new Rect(usedWidth, usedHeight, usedWidth + child.getMeasuredWidth(), usedHeight + child.getMeasuredHeight());
            childrenBounds.add(bounds);
            usedWidth += child.getMeasuredWidth();
            maxWidth = Math.max(maxWidth, usedWidth);
            maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
        }

        pWidth = maxWidth;
        pHeight = usedHeight + maxHeight;
//        Log.e(TAG, "onMeasure:" + pWidth + "," + pHeight);
        setMeasuredDimension(pWidth, pHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            Rect bound = childrenBounds.get(i);
            child.layout(bound.left, bound.top, bound.right, bound.bottom);
//            Log.e(TAG, "onLayout:" + String.format("%d,%d,%d,%d", bound.left, bound.top, bound.right, bound.bottom));
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}
