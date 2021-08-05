package com.tyaathome.customview.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.OverScroller;

import com.tyaathome.customview.Utils.Utils;

import androidx.annotation.Nullable;

/**
 * Created by Mandosbor on 2021/05/27.
 */
public class ScaleableImage extends View implements Runnable {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap bitmap;
    private static final float IMAGE_WIDTH = Utils.dp2px(300);
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;

    private float originalOffsetX;
    private float originalOffsetY;
    private float currentOffsetX;
    private float currentOffsetY;
    private float oldOffsetX;
    private float oldOffsetY;

    private float bigScale = 1f;
    private float smallScale = 1f;

    private float currentScale = 1f;
    private float oldScale = 1f;
    private static final float SCALE_FACTOR = 2f;

    private boolean isBig;
    private ObjectAnimator animator;
    private ObjectAnimator reduceAnimation;

    private float fraction;

    private float reduceFraction;
    private OverScroller scroller;
    private static final float OVER_VALUE = Utils.dp2px(50);
    private VelocityTracker velocityTracker = VelocityTracker.obtain();

    public ScaleableImage(Context context) {
        super(context);
        init();
        //velocityTracker.computeCurrentVelocity();
    }

    public ScaleableImage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        scroller = new OverScroller(getContext());
        bitmap = Utils.getAvatar(getContext(), (int) IMAGE_WIDTH);
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                scroller.abortAnimation();
                return true;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                isBig = !isBig;
                oldScale = currentScale;
                if(isBig) {
                    //fixOffsets();
                    currentOffsetX = (e.getX() - getWidth() / 2f) - (e.getX() - getWidth() / 2f) * bigScale / smallScale;
                    currentOffsetY = (e.getY() - getHeight() / 2f) - (e.getY() - getHeight() / 2f) * bigScale / smallScale;
                    getAnimator().start();
                } else {
                    oldOffsetX = currentOffsetX;
                    oldOffsetY = currentOffsetY;
                    getAnimator().reverse();
                }
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                    float distanceY) {
                if(isBig) {
                    currentOffsetX -= distanceX;
                    currentOffsetY -= distanceY;
                    fixOffsets();
                    if(e2.getActionMasked() == MotionEvent.ACTION_UP) {
                        invalidate();
                    }
                    invalidate();
                    Log.d("ScaleableImage", currentOffsetX + ", " + currentOffsetY + " | " + distanceX + ", " + distanceY);
                }
                return false;
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                if(isBig) {
                    scroller.fling((int) currentOffsetX,
                            (int) currentOffsetY,
                            (int) velocityX,
                            (int) velocityY,
                            (int) (-(bitmap.getWidth() * currentScale - getWidth()) /2f),
                            (int) ((bitmap.getWidth() * currentScale - getWidth()) / 2f),
                            (int) (-(bitmap.getHeight() * currentScale - getHeight()) / 2f),
                            (int) ((bitmap.getHeight() * currentScale - getHeight()) / 2f)
//                            ,
//                            (int) OVER_VALUE,
//                            (int) OVER_VALUE
                    );
                    postOnAnimation(ScaleableImage.this);
                }
                return false;
            }
        });

        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleGestureDetector.SimpleOnScaleGestureListener() {
            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                if(isBig) {
                    currentScale = oldScale * detector.getScaleFactor();
                    if(currentScale < smallScale-0.2f) {
                        currentScale = smallScale-0.2f;
                    } else if (currentScale > bigScale) {
                        currentScale = bigScale;
                    }
                    fixOffsets();
                    invalidate();
                }
                return false;
            }

            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                oldScale = currentScale;
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
                super.onScaleEnd(detector);
                //fixOffsets();
                getReduceAnimation().start();
            }
        });
    }

    private void fixOffsets() {
        if(bitmap.getWidth() * currentScale > getWidth()) {
            currentOffsetX = Math.min((bitmap.getWidth() * currentScale - getWidth()) / 2f, currentOffsetX);
            currentOffsetX = Math.max(-(bitmap.getWidth() * currentScale - getWidth()) / 2f, currentOffsetX);
        } else {
            currentOffsetX = 0;
        }
        if(bitmap.getHeight() * currentScale > getHeight()) {
            currentOffsetY = Math.min((bitmap.getHeight() * currentScale - getHeight()) / 2f, currentOffsetY);
            currentOffsetY = Math.max(-(bitmap.getHeight() * currentScale - getHeight()) / 2f, currentOffsetY);
        } else {
            currentOffsetY = 0;
        }
    }

    private float getFraction() {
        return fraction;
    }

    private void setFraction(float fraction) {
        this.fraction = fraction;
        if(isBig) {
            currentScale = oldScale + (bigScale - oldScale) * fraction;
        } else {
            currentScale = oldScale - (oldScale - smallScale) * (1f - fraction);
            currentOffsetX = oldOffsetX * fraction;
            currentOffsetY = oldOffsetY * fraction;
        }
        Log.d("ScaleableImage", "currentScale: " + currentScale);
        invalidate();
    }

    private float getReduceFraction() {
        return reduceFraction;
    }

    private void setReduceFraction(float reduceFraction) {
        if(smallScale > currentScale) {
            this.reduceFraction = reduceFraction;
            currentScale = (smallScale - currentScale) * reduceFraction + currentScale;
            currentOffsetX = (1 - reduceFraction) * currentOffsetX;
            currentOffsetY = (1 - reduceFraction) * currentOffsetY;
            invalidate();
        }
    }

    private ObjectAnimator getAnimator() {
        if(animator == null) {
            animator = ObjectAnimator.ofFloat(this, "fraction", 0, 1);
        }
        return animator;
    }

    private ObjectAnimator getReduceAnimation() {
        if(reduceAnimation == null) {
            reduceAnimation = ObjectAnimator.ofFloat(this, "reduceFraction", 0, 1);
        }
        return reduceAnimation;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = scaleGestureDetector.onTouchEvent(event);
        if(!scaleGestureDetector.isInProgress()) {
            result = gestureDetector.onTouchEvent(event);
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        float scaleFraction = (currentScale - smallScale) / (bigScale - smallScale);
//        float x = currentOffsetX * scaleFraction;
//        float y = currentOffsetY * scaleFraction;
        canvas.translate(currentOffsetX, currentOffsetY);
        canvas.scale(currentScale, currentScale, getWidth()/2f, getHeight()/2f);
        canvas.translate(originalOffsetX, originalOffsetY);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        originalOffsetX = (getWidth() - bitmap.getWidth()) / 2f;
        originalOffsetY = (getHeight() - bitmap.getHeight()) / 2f;
        if(getWidth() / (float)bitmap.getWidth() > getHeight() / (float)bitmap.getHeight()) {
            bigScale = getWidth() / (float)bitmap.getWidth() * SCALE_FACTOR;
            smallScale = getHeight() / (float)bitmap.getHeight();
        } else {
            bigScale = getHeight() / (float)bitmap.getHeight() * SCALE_FACTOR;
            smallScale = getWidth() / (float)bitmap.getWidth();
        }
        currentScale = smallScale;
        invalidate();
    }

    @Override
    public void run() {
        if(scroller.computeScrollOffset()) {
            currentOffsetX = scroller.getCurrX();
            currentOffsetY = scroller.getCurrY();
            fixOffsets();
            invalidate();
            postOnAnimation(this);
        }
    }
}
