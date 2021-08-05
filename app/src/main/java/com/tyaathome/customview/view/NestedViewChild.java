package com.tyaathome.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingChild3;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.ViewCompat;

/**
 * Created by Mandosbor on 2021/06/07.
 */
public class NestedViewChild extends ScrollView implements NestedScrollingChild3 {

    private NestedScrollingChildHelper childHelper;
    private float downY;
    private float scrollY;
    private int[] consumed = new int[2];

    public NestedViewChild(Context context) {
        super(context);
        init();
    }

    public NestedViewChild(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        childHelper.startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
        boolean result = true;
        if(!(ev.getActionMasked() == MotionEvent.ACTION_MOVE)) {
            result = super.onTouchEvent(ev);
        }
        float y = ev.getY();
        int dy = 0;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                downY = y;
                scrollY = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                float yDiff = y - downY;
                scrollY -= yDiff;
                downY = y;
                dy = Math.round(fixOffsetY());
                System.out.println("NestedViewChild dy: " + dy + ", " + scrollY);
                if(childHelper.dispatchNestedPreScroll(0, (int) dy, consumed, null)) {
                    int unconsumedY = consumed[1];
                    if(unconsumedY != 0) {
                        childHelper.dispatchNestedScroll(0, 0, 0, unconsumedY, null);
                        result = super.onTouchEvent(ev);
                    }
                }
                break;
        }
        return result;
    }

    private float fixOffsetY() {
        int scrollRange = getScrollRange();
        float result = 0;
//        if(scrollY > getScrollRange()) {
//            result = scrollY - scrollRange;
//            scrollY = scrollRange;
//        } else if(scrollY < 0) {
//            result = scrollY;
//            scrollY = 0;
//        }
        result = scrollY;
//        if(scrollY < 0) {
//            scrollY = 0;
//        }
        return result;
    }

    private int getScrollRange() {
        if(getChildCount() > 0) {
            return getChildAt(0).getHeight() - getHeight();
        }
        return 0;
    }

    private void init() {
        childHelper = new NestedScrollingChildHelper(this);
        childHelper.setNestedScrollingEnabled(true);
    }

    @Override
    public void dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, @Nullable int[] offsetInWindow, int type, @NonNull int[] consumed) {
        childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type, consumed);
    }

    @Override
    public boolean startNestedScroll(int axes, int type) {
        return childHelper.startNestedScroll(axes, type);
    }

    @Override
    public void stopNestedScroll(int type) {
        childHelper.stopNestedScroll(type);
    }

    @Override
    public boolean hasNestedScrollingParent(int type) {
        return childHelper.hasNestedScrollingParent(type);
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, @Nullable int[] offsetInWindow,
                                        int type) {
        return childHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, @Nullable int[] consumed, @Nullable int[] offsetInWindow, int type) {
        return childHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow, type);
    }
}
