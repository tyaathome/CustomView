package com.tyaathome.customview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tyaathome.customview.Utils.Utils;

import androidx.annotation.Nullable;

/**
 * Created by Administrator on 2021/04/23.
 */
public class MyTextView extends View {

    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private static final String TEXT = "余忆童稚时，能张目对日，明察秋毫，见藐小之物必细察其纹理，故时有物外之趣。夏蚊成雷，私拟作群鹤舞于空中，心之所向，则或千或百，果然鹤也；昂首观之，项为之强。又留蚊于素帐中，徐喷以烟，使之冲烟而飞鸣，作青云白鹤观，果如鹤唳云端，为之怡然称快。余常于土墙凹凸处，花台小草丛杂处，蹲其身，使与台齐；定神细视，以丛草为林，以虫蚁为兽，以土砾凸者为丘，凹者为壑，神游其中，怡然自得。一日，见二虫斗草间，观之，兴正浓，忽有庞然大物，拔山倒树而来，盖一癞虾蟆，舌一吐而二虫尽为所吞。余年幼，方出神，不觉呀然一惊。神定，捉虾蟆，鞭数十，驱之别院。";
    //private static final String TEXT = "abababababababababababababababababababababababab";
    private Rect textBounds = new Rect();
    private float[] measuredWidth = new float[1];
    //private int index = 0;
    private int oldIndex = 0;
    // 当前绘制文字的位置
    private float currentX;
    private float currentY;
    private RectF rect = new RectF();
    private float padding;
    private boolean isDown = false;
    private float oldX = 0;
    private float oldY = 0;

    {
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(Utils.dp2px(70));
        paint.setStrokeWidth(Utils.dp2px(3));
        paint.setStyle(Paint.Style.STROKE);
        padding = Utils.dp2px(5);
        rect.set(0, 0, Utils.dp2px(100), Utils.dp2px(100));
    }

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画在中心
//        float x = getWidth()/2f;
//        float y = getHeight()/2f - (textPaint.getFontMetrics().descent + textPaint.getFontMetrics().ascent)/2f;
//        canvas.drawText(TEXT, x, y, textPaint);

        // 绘制左对齐
//        textPaint.setTextAlign(Paint.Align.LEFT);
//        textPaint.setTextSize(Utils.dp2px(20));
//        float x = 0f;
//        float y = Utils.dp2px(20);
//        canvas.drawText(TEXT, x, y, textPaint);
//        textPaint.setTextSize(Utils.dp2px(100));
//        textPaint.getTextBounds(TEXT, 0, TEXT.length(), textBounds);
//        //x = -textBounds.left;
//        x = 0;
//        y += textPaint.getFontSpacing();
//        canvas.drawText(TEXT, x, y, textPaint);
//        textPaint.setTextAlign(Paint.Align.CENTER);
//        textBounds.top += y;
//        textBounds.bottom += y;
//        canvas.drawRect(textBounds, paint);
//        float top = y + textPaint.getFontMetrics().top;
//        float ascent = y + textPaint.getFontMetrics().ascent;
//        float descent = y + textPaint.getFontMetrics().descent;
//        float bottom = y + textPaint.getFontMetrics().bottom;
//        canvas.drawLine(0, top, Utils.dp2px(300), top, paint);
//        canvas.drawLine(0, ascent, Utils.dp2px(300), ascent, paint);
//        canvas.drawLine(0, descent, Utils.dp2px(300), descent, paint);
//        canvas.drawLine(0, bottom, Utils.dp2px(300), bottom, paint);
//        canvas.drawLine(0, y, Utils.dp2px(300), y, paint);

//        rect.left = getWidth()/2f - Utils.dp2px(100);
//        rect.top = Utils.dp2px(40);
//        rect.right = rect.left + Utils.dp2px(100);
//        rect.bottom = rect.top + Utils.dp2px(100);
//        canvas.drawRect(rect, paint);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize(Utils.dp2px(20));
        //float y = textPaint.getFontSpacing();
        //canvas.drawText(TEXT, 0, y, textPaint);
//        int index = textPaint.breakText(TEXT, true, rect.left, measuredWidth);
//        canvas.drawText(TEXT, 0, index, 0, y, textPaint);



        reDraw(canvas);
    }

    private void reDraw(Canvas canvas) {
        canvas.drawRect(rect, paint);
        if(currentY == 0) {
            currentY = textPaint.getFontSpacing();
        }
        float fontSpacing = textPaint.getFontSpacing();
        while(oldIndex < TEXT.length()-1) {
            int index;
            if(currentY < rect.top || currentY > rect.bottom +fontSpacing) {
                index = textPaint.breakText(TEXT, true, getWidth(), measuredWidth) + oldIndex;
                if(index > TEXT.length()-1) {
                    index = TEXT.length()-1;
                }
                canvas.drawText(TEXT, oldIndex, index, 0, currentY, textPaint);
                oldIndex = index;
            } else {
                if(rect.left > 0) {
                    index = textPaint.breakText(TEXT, true, rect.left, measuredWidth) + oldIndex;
                    if(index > TEXT.length()-1) {
                        index = TEXT.length()-1;
                    }
                    canvas.drawText(TEXT, oldIndex, index, 0, currentY, textPaint);
                    textPaint.getTextBounds(TEXT, oldIndex, index, textBounds);
                    oldIndex = index;
                }
                currentX = rect.right;
                if(rect.right < getWidth()) {
                    index = textPaint.breakText(TEXT, true, getWidth() - currentX, measuredWidth) + oldIndex;
                    if(index > TEXT.length()-1) {
                        index = TEXT.length()-1;
                    }
                    canvas.drawText(TEXT, oldIndex, index, currentX, currentY, textPaint);
                    oldIndex = index;
                }
                currentX = 0;
            }
            currentY += textPaint.getFontSpacing();
            //break;
        }
        currentY = 0;
        oldIndex = 0;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                oldX = event.getX();
                oldY = event.getY();
                if (isClick(oldX, oldY)) {
                    isDown = true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (isDown) {
                    float x = event.getX();
                    float y = event.getY();
                    float cX = x - oldX;
                    float cY = y - oldY;
                    rect.left += cX;
                    rect.top += cY;
                    rect.right += cX;
                    rect.bottom += cY;
                    oldX = x;
                    oldY = y;
                    invalidate();
                }
                break;
            }
            case MotionEvent.ACTION_UP:
                isDown = false;
                break;
        }

        return isDown;
    }

    private boolean isClick(float x, float y) {
        return x >= rect.left && x <= rect.right && y >= rect.top && y <= rect.bottom;
    }
}
