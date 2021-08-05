package com.tyaathome.customview.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;

import com.tyaathome.customview.R;
import com.tyaathome.customview.Utils.Utils;

import java.util.Random;

/**
 * Created by Mandosbor on 2021/05/22.
 */
public class TagView extends androidx.appcompat.widget.AppCompatTextView {

    public TagView(Context context) {
        super(context);
        init();
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        GradientDrawable drawable = (GradientDrawable) getResources().getDrawable(R.drawable.bg_round_corner);
        Random random = new Random();
        int backgroundColor = Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
        int textColor = Utils.isDeepColor(backgroundColor) ? Color.WHITE : Color.BLACK;
        drawable.setColor(backgroundColor);
        setBackground(drawable);
        setTextColor(textColor);
    }
}
