package com.tyaathome.customview.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.tyaathome.customview.Utils.Utils;

import androidx.annotation.Nullable;

/**
 * Created by Administrator on 2021/05/31.
 */
public class MultiTouchView extends View {



//    public MultiTouchView(Context context) {
//        super(context);
//    }
//
//    public MultiTouchView(Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getActionMasked()) {
//            case MotionEvent.ACTION_DOWN:
//                printAllPointer(event, "ACTION_DOWN");
//                break;
//            case MotionEvent.ACTION_POINTER_DOWN:
//                printAllPointer(event, "ACTION_POINTER_DOWN");
//                break;
//            case MotionEvent.ACTION_POINTER_UP:
//                printAllPointer(event, "ACTION_POINTER_UP");
//                break;
//            case MotionEvent.ACTION_UP:
//                printAllPointer(event, "ACTION_UP");
//                break;
//        }
//        return true;
//    }
//
//    private void printAllPointer(MotionEvent event, String action) {
//        String log = "MultiTouchView " + action + " ";
//        for(int i = 0; i < 6; i++) {
//            String id = getPointerId(event, i);
//            //Log.d("MultiTouchView", action + " index: " + i + " id: " + id);
//            log += "(" + i + " " + id + "),";
//        }
//
////        for (int i = 0; i < event.getPointerCount(); i++) {
////            log += i + " ";
////        }
////        log += ") id: (";
////        for (int i = 0; i < event.getPointerCount(); i++) {
////            int id = event.getPointerId(i);
////            log += id + " ";
////        }
////        log += ")";
//
//        System.out.println(log);
//    }
//
//    private String getPointerId(MotionEvent event, int index) {
//        try {
//            return String.valueOf(event.getPointerId(index));
//        } catch (Exception e) {
//            return "null";
//        }
//    }

    private Bitmap bitmap;
    private int IMAGE_SIZE = (int) Utils.dp2px(200);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private float offsetX, offsetY;
    private float preOffsetX, preOffsetY;
    private float downX, downY;
    private int currentIndex = 0;

    public MultiTouchView(Context context) {
        super(context);
        init(context);
    }

    public MultiTouchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        bitmap = Utils.getAvatar(context, IMAGE_SIZE);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(offsetX, offsetY);
        canvas.translate((getWidth() - bitmap.getWidth()) / 2f, (getHeight() - bitmap.getHeight()) / 2f);
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                currentIndex = 0;
                downX = event.getX();
                downY = event.getY();
                preOffsetX = offsetX;
                preOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_MOVE:
                offsetX = preOffsetX + event.getX(currentIndex) - downX;
                offsetY = preOffsetY + event.getY(currentIndex) - downY;
                invalidate();
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                downX = event.getX(event.getActionIndex());
                downY = event.getY(event.getActionIndex());
                currentIndex = event.getActionIndex();
                preOffsetX = offsetX;
                preOffsetY = offsetY;
                print(event);
                break;
            case MotionEvent.ACTION_POINTER_UP:
                // ACTION_POINTER_UP??????getPointerCount()??????????????????????????????????????????
                // getActionIndex()?????????????????????pointer??????????????????
                // event.getX(index)???index??????????????????????????????????????????????????????????????????index????????? event.getPointerCount() - 1 - 1(???????????????)

                // ??????????????????????????????????????????????????????index
                currentIndex = event.getPointerCount() - 1 - 1;
                // ?????????????????????????????????????????????????????????????????????????????????????????????
                // ?????????????????????????????????????????????????????????
                if (event.getActionIndex() == event.getPointerCount() - 1) {
                    // ????????????????????????????????????(?????????????????????)????????????????????????????????????
                    downX = event.getX(event.getPointerCount() - 2);
                    downY = event.getY(event.getPointerCount() - 2);
                } else {
                    // ????????????????????????????????????(?????????????????????)??????????????????????????????????????????
                    downX = event.getX(event.getPointerCount() - 1);
                    downY = event.getY(event.getPointerCount() - 1);
                }
                preOffsetX = offsetX;
                preOffsetY = offsetY;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    private void print(MotionEvent event) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < event.getPointerCount(); i++) {
            stringBuilder.append("index: " + i + "(x: " + event.getX(i) + " , y: " + event.getY(i) + ") ");
        }
        Log.e("TouchOneView", stringBuilder.toString());
    }
}
