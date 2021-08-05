package com.tyaathome.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2021/06/16.
 */
public class ScaleLayout extends FrameLayout implements ScaleGestureDetector.OnScaleGestureListener {

    private ScaleGestureDetector mScaleGestureDetector;
    private GestureDetector gestureDetector;
    private float mScale = 1f;
    private SparseArray<RectF> mBoundList = new SparseArray<>();
    private static int sTouchSlop;
    private float mLastMotionX;
    private float mLastMotionY;


    public ScaleLayout(Context context) {
        super(context);
        init();
    }

    public ScaleLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setWillNotDraw(false);
        mScaleGestureDetector = new ScaleGestureDetector(getContext(), this);
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                    float distanceY) {
                scrollBy((int)distanceX, (int)distanceY);
                return true;
            }
        });
        ViewConfiguration viewConfiguration = ViewConfiguration.get(getContext());
        sTouchSlop = viewConfiguration.getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = false;
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                final float x = ev.getX();
                final float y = ev.getY();
                float diffX = Math.abs(ev.getX() - mLastMotionX);
                float diffY = Math.abs(ev.getY() - mLastMotionY);
                if(diffX > sTouchSlop || diffY > sTouchSlop) {
                    final ViewParent parent = getParent();
                    if (parent != null) {
                        parent.requestDisallowInterceptTouchEvent(true);
                    }
                    result = true;
                }
                mLastMotionY = y;
                mLastMotionX = x;
                break;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mScaleGestureDetector.onTouchEvent(event);
        if(!mScaleGestureDetector.isInProgress()) {
            result = gestureDetector.onTouchEvent(event);
        }
        return result;
    }

    private void setScale(float scale) {
        mScale = scale;
        //invalidate();
        requestLayout();
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        float currentScale = mScale * mScaleGestureDetector.getScaleFactor();
        setScale(currentScale);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        float widthSpace = 0;
        float heightSpace = 0;
        float mostHeightInRow = 0;
        float allMeasureHeight = 0;
        for(int i = 0; i < getChildCount(); i++) {
            System.out.println("iii: " + i);
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            int childMeasureWidth = (int) (child.getMeasuredWidth() * mScale);
            int childMeasureHeight = (int) (child.getMeasuredHeight() * mScale);

            RectF bound = mBoundList.get(i);
            if(bound == null) {
                bound = new RectF();
            }
            bound.left = widthSpace;
            bound.top = heightSpace;
            bound.right = bound.left + childMeasureWidth;
            bound.bottom = bound.top + childMeasureHeight;
            mBoundList.append(i, bound);
            mostHeightInRow = Math.max(mostHeightInRow, childMeasureHeight);
            widthSpace += childMeasureWidth;
            // 换行
            if(widthSpace + childMeasureWidth > width) {
                widthSpace = 0;
                heightSpace += mostHeightInRow;
                mostHeightInRow = 0;
            }

        }
        setMeasuredDimension(width , height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //super.onLayout(changed, left, top, right, bottom);
        for(int i = 0; i < getChildCount(); i++) {
            RectF bound = mBoundList.get(i);
            if(bound == null) {
                continue;
            }
            View child = getChildAt(i);
            child.layout((int)bound.left, (int)bound.top, (int)bound.right, (int)bound.bottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //canvas.scale(mScale, mScale);
    }
}
