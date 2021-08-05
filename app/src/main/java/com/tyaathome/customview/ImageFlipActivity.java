package com.tyaathome.customview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import com.tyaathome.customview.view.ImageFlipView;

import androidx.appcompat.app.AppCompatActivity;

public class ImageFlipActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotate_image);
        ImageFlipView view = findViewById(R.id.view);
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(view, "rotate", 270);
        rotateAnimator.setDuration(1500);
        ObjectAnimator topFlipAnimator = ObjectAnimator.ofFloat(view, "topFlip", -45);
        topFlipAnimator.setDuration(1500);
        ObjectAnimator bottomFlipAnimator = ObjectAnimator.ofFloat(view, "bottomFlip", 45);
        bottomFlipAnimator.setDuration(1500);

        AnimatorSet set = new AnimatorSet();
        set.playSequentially(topFlipAnimator, rotateAnimator, bottomFlipAnimator);
        set.setStartDelay(2000);
        set.start();
    }
}