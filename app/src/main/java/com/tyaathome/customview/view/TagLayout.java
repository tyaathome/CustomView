package com.tyaathome.customview.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mandosbor on 2021/05/20.
 */
public class TagLayout extends ViewGroup {

    private List<Rect> boundList = new ArrayList<>();
    public TagLayout(Context context) {
        super(context);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        measureChildren();
//        measureChildWithMargins();
//        resolveSize()
        int heightUsed = 0;
        int maxWidthUsed = 0;
        // 当行使用的大小
        int lineWidthUsed = 0;
        int lineHeightUsed = 0;
        int parentWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int parentWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int a = MeasureSpec.EXACTLY;
        int b = MeasureSpec.AT_MOST;
        int c = MeasureSpec.UNSPECIFIED;
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
            // 计算当行最大的高度
            lineHeightUsed = Math.max(lineHeightUsed, getRealHeight(child));
            // 当父控件放不下时控件换行摆放
            if(parentWidthSize < getRealWidth(child)+lineWidthUsed) {
                heightUsed += lineHeightUsed;
                lineHeightUsed = 0;
                lineWidthUsed = 0;
                // 换行重新测量
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                lineHeightUsed = Math.max(lineHeightUsed, getRealHeight(child));
            }
            Rect bound;
            if(boundList.size() > i) {
                bound = boundList.get(i);
            } else {
                bound = new Rect();
                boundList.add(bound);
            }
            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            int left = lineWidthUsed + params.leftMargin;
            int top = heightUsed + params.topMargin;
            int right = left + child.getMeasuredWidth();
            int bottom = top + child.getMeasuredHeight();
            bound.set(left, top, right, bottom);
            lineWidthUsed += getRealWidth(child);
            maxWidthUsed = Math.max(maxWidthUsed, lineWidthUsed);
        }
        setMeasuredDimension(maxWidthUsed, heightUsed);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if(boundList.size() > i) {
                Rect bound = boundList.get(i);
                child.layout(bound.left, bound.top, bound.right, bound.bottom);
            }
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    private int getRealWidth(View view) {
        LayoutParams params = view.getLayoutParams();
        if(!(params instanceof MarginLayoutParams)) {
            return view.getMeasuredWidth();
        }

        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) params;
        return marginLayoutParams.leftMargin + marginLayoutParams.rightMargin + view.getMeasuredWidth();
    }

    private int getRealHeight(View view) {
        LayoutParams params = view.getLayoutParams();
        if(!(params instanceof MarginLayoutParams)) {
            return view.getMeasuredHeight();
        }

        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) params;
        return marginLayoutParams.topMargin + marginLayoutParams.bottomMargin + view.getMeasuredHeight();
    }
}
