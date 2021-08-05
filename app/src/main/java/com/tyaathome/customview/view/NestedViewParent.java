package com.tyaathome.customview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;

/**
 * Created by Mandosbor on 2021/06/07.
 */
public class NestedViewParent extends LinearLayout implements NestedScrollingParent3 {

    private NestedScrollingParentHelper parentHelper;
    private View bannerLayout;
    private View floatTabLayout;
    private ViewGroup nestedView;
    private int orginalBannerHeight;
    private int layoutTop;


    public NestedViewParent(Context context) {
        super(context);
        init();
    }

    public NestedViewParent(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        parentHelper = new NestedScrollingParentHelper(this);
        setOrientation(LinearLayout.VERTICAL);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        int height = getMeasuredHeight() - floatTabLayout.getMeasuredHeight();
        final ViewGroup.LayoutParams lp = nestedView.getLayoutParams();
        final int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                getPaddingLeft() + getPaddingRight(), lp.width);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        nestedView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

        setMeasuredDimension(layoutWidth, getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int left = nestedView.getLeft();
        int right = nestedView.getRight();
        int top = floatTabLayout.getBottom();
        int bottom = top + nestedView.getMeasuredHeight();
        nestedView.layout(left, top, right, bottom);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        bannerLayout = getChildAt(0);
        floatTabLayout = getChildAt(1);
        nestedView = (ViewGroup) getChildAt(2);
        postDelayed(() -> {
            orginalBannerHeight = bannerLayout.getMeasuredHeight();
            layoutTop = bannerLayout.getTop();
        }, 100);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed, int type,
                               @NonNull int[] consumed) {

    }

    @Override
    public boolean onStartNestedScroll(@NonNull View child, @NonNull View target, int axes,
                                       int type) {
        return target == nestedView;
    }

    @Override
    public void onNestedScrollAccepted(@NonNull View child, @NonNull View target, int axes,
                                       int type) {
        parentHelper.onNestedScrollAccepted(child, target, axes, type);
    }

    @Override
    public void onStopNestedScroll(@NonNull View target, int type) {
        parentHelper.onStopNestedScroll(target, type);
    }

    @Override
    public void onNestedScroll(@NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        //this.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

//        bannerLayout.setTop(bannerLayout.getTop()-dy);
//
//        ViewGroup.LayoutParams lp = bannerLayout.getLayoutParams();
//        int height = 0;
//        if(dy < bannerLayout.getHeight()) {
//            height = bannerLayout.getHeight() - dy;
//        }
//        if(height > orginalBannerHeight) {
//            height = orginalBannerHeight;
//        }
//        lp.height = height;
//        bannerLayout.setLayoutParams(lp);
        //scrollTo(0, getScrollY()+dy);
        //bannerLayout.scrollBy(0, dy);
//        floatTabLayout.setTranslationY(floatTabLayout.getTranslationY() - dy);
        //requestLayout();


//        setY(getY() - dy);
//        requestLayout();
        int offsetY = 0;
        if(bannerLayout.getTop() - layoutTop + bannerLayout.getHeight() - dy <= 0) {
            offsetY = bannerLayout.getTop() + bannerLayout.getHeight();
        } else if(bannerLayout.getTop() - layoutTop - dy > 0) {
            offsetY = bannerLayout.getTop();
        } else {
            offsetY = dy;
        }
        ViewCompat.offsetTopAndBottom(bannerLayout, -offsetY);
        ViewCompat.offsetTopAndBottom(floatTabLayout, -offsetY);
        ViewCompat.offsetTopAndBottom(nestedView, -offsetY);
        consumed[1] = offsetY - dy;
    }

}
