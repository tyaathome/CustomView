package com.tyaathome.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.OverScroller;

import androidx.viewpager.widget.ViewPager;

/**
 * Created by Mandosbor on 2021/06/05.
 */
public class MyViewPager extends ViewPager {

    private static final float MIN_FLING_VELOCITY = 400;
    private float offsetX;
    private float offsetY;
    private int scrollX;
    private int downScrollX;
    private float preOffsetX;
    private float preOffsetY;
    private int allScrollX;
    private OverScroller scroller;
    private VelocityTracker velocityTracker;
    private ViewConfiguration viewConfiguration;
    private float pagingSlop;
    private float maxVelocity;
    private float minVelocity;

    public MyViewPager(Context context) {
        super(context);
        init();
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        scroller = new OverScroller(getContext());
        viewConfiguration = ViewConfiguration.get(getContext());
        velocityTracker = VelocityTracker.obtain();
        pagingSlop = viewConfiguration.getScaledPagingTouchSlop();
        maxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        //minVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        final float density = getResources().getDisplayMetrics().density;
        minVelocity = (int) (MIN_FLING_VELOCITY * density);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width, height);
        if(getChildCount() == 0) {
            allScrollX = 0;
        } else {
            allScrollX = (getChildCount() - 1) * width;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int startWidth = 0;
        for(int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            int left = startWidth;
            int top = 0;
            int right = left + view.getMeasuredWidth();
            int bottom = top + view.getMeasuredHeight();
            view.layout(left, top, right, bottom);
            startWidth += view.getMeasuredWidth();
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean result = false;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                preOffsetX = event.getX();
                preOffsetY = event.getY();
                downScrollX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                float xDiff = preOffsetX - event.getX();
                float yDiff = preOffsetY - event.getY();
                if(xDiff > pagingSlop && xDiff * 0.5f > yDiff) {
                    result = true;
                    requestParentDisallowInterceptTouchEvent(true);
                }
                break;
        }
        return result;
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallowIntercept) {
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.addMovement(event);
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                preOffsetX = event.getX();
                preOffsetY = event.getY();
                downScrollX = getScrollX();
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = event.getX() - preOffsetX;
                offsetY = event.getY() - preOffsetY;
                scrollX = (int) (downScrollX - offsetX);
                fixScrollX();
                scrollTo(scrollX, 0);
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000, maxVelocity);
                float velocity = velocityTracker.getXVelocity();
                int targetPage = 0;
                int remainder = scrollX % getWidth();
                int currentPage = getScrollX() / getWidth();
                if(Math.abs(velocity) > minVelocity) {
                    targetPage = velocity > 0 ? currentPage : currentPage+1;
                } else {
                    targetPage = remainder > getWidth() / 2f ? currentPage+1 : currentPage;
                }
                // fix targetPage
                if(targetPage < 0) {
                    targetPage = 0;
                } else if(targetPage >= getChildCount()) {
                    targetPage = getChildCount()-1;
                }
                int distance = targetPage * getWidth() - scrollX;
                scroller.startScroll(getScrollX(), 0, distance, 0);
                postInvalidateOnAnimation();
                velocityTracker.clear();
                break;
        }
        return true;
    }

    private void fixScrollX() {
        if(scrollX < 0) {
            scrollX = 0;
        } else if(scrollX > allScrollX) {
            scrollX = allScrollX;
        }

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidateOnAnimation();
        }
    }
}
