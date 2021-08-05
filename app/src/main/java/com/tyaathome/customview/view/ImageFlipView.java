package com.tyaathome.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.tyaathome.customview.Utils.Utils;

import androidx.annotation.Nullable;

/**
 * Created by Mandosbor on 2021/05/17.
 */
public class ImageFlipView extends View {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Bitmap bitmap = null;
    private int WIDTH;
    private int HEIGHT;
    private float leftPadding;
    private float topPadding;
    private float rotate = 0f;
    private float topFlip = 0f;

    private float bottomFlip = 0f;
    private Camera camera = new Camera();

    {
        WIDTH = (int) Utils.dp2px(200);
        HEIGHT = (int) Utils.dp2px(200);
        camera.rotateX(0);
        camera.setLocation(0, 0, Utils.getZForCamera());
    }

    public ImageFlipView(Context context) {
        super(context);
        init();
    }

    public ImageFlipView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        bitmap = Utils.getAvatar(getContext(), WIDTH);
    }

    public float getRotate() {
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
        invalidate();
    }

    public float getTopFlip() {
        return topFlip;
    }

    public void setTopFlip(float topFlip) {
        this.topFlip = topFlip;
        invalidate();
    }

    public float getBottomFlip() {
        return bottomFlip;
    }

    public void setBottomFlip(float bottomFlip) {
        this.bottomFlip = bottomFlip;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(bitmap != null) {
            leftPadding = (getWidth() - WIDTH) / 2f;
            topPadding = (getHeight() - HEIGHT) / 2f;

            canvas.save();
            canvas.translate(leftPadding + WIDTH / 2f, topPadding + HEIGHT / 2f);
            canvas.rotate(-rotate);
            camera.save();
            camera.rotateX(topFlip);
            camera.applyToCanvas(canvas);
            camera.restore();
            canvas.clipRect(-WIDTH, -HEIGHT, WIDTH, 0);
            canvas.rotate(rotate);
            canvas.translate(-leftPadding - WIDTH / 2f, -topPadding - HEIGHT / 2f);
            canvas.drawBitmap(bitmap, leftPadding, topPadding, paint);
            canvas.restore();

            canvas.save();
            canvas.translate(leftPadding + WIDTH / 2f, topPadding + HEIGHT / 2f);
            canvas.rotate(-rotate);
            camera.save();
            camera.rotateX(bottomFlip);
            camera.applyToCanvas(canvas);
            camera.restore();
            canvas.clipRect(-WIDTH, 0, WIDTH, HEIGHT);
            canvas.rotate(rotate);
            canvas.translate(-leftPadding - WIDTH / 2f, -topPadding - HEIGHT / 2f);
            canvas.drawBitmap(bitmap, leftPadding, topPadding, paint);
            canvas.restore();

//            canvas.save();
//            canvas.translate(getWidth()/2f, getHeight()/2f);
//            canvas.rotate(-rotate);
//            camera.applyToCanvas(canvas);
//            canvas.clipRect(-width, -width, width, 0);
//            canvas.rotate(rotate);
//            canvas.translate(-getWidth()/2f, -getHeight()/2f);
//            canvas.drawBitmap(bitmap, getWidth()/2f - width/2f, getHeight()/2f - width/2f, paint);
//            canvas.restore();
//
//            canvas.save();
//            canvas.translate(getWidth()/2f, getHeight()/2f);
//            canvas.rotate(-rotate);
//            canvas.clipRect(-width, 0, width, width);
//            canvas.rotate(rotate);
//            canvas.translate(-getWidth()/2f, -getHeight()/2f);
//            canvas.drawBitmap(bitmap, getWidth()/2f - width/2f, getHeight()/2f - width/2f, paint);
//            canvas.restore();
        }
    }

}
