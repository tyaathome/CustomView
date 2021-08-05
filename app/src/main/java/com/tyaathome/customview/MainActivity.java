package com.tyaathome.customview;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTransparentStatusBar();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_taglayout).setOnClickListener(onClickListener);
        findViewById(R.id.btn_scaleableimage).setOnClickListener(onClickListener);
        findViewById(R.id.btn_viewpager).setOnClickListener(onClickListener);
        findViewById(R.id.btn_nestedview).setOnClickListener(onClickListener);
        findViewById(R.id.btn_textview).setOnClickListener(onClickListener);
        findViewById(R.id.btn_multitouch).setOnClickListener(onClickListener);
        findViewById(R.id.btn_rotateimage).setOnClickListener(onClickListener);
        findViewById(R.id.btn_scalelayout).setOnClickListener(onClickListener);
    }

    private void setTransparentStatusBar() {
        Window window = getWindow();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
            window.setDecorFitsSystemWindows(false);
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void gotoActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    private View.OnClickListener onClickListener = v -> {
        switch (v.getId()) {
            case R.id.btn_taglayout:
                gotoActivity(TagLayoutActivity.class);
                break;
            case R.id.btn_scaleableimage:
                gotoActivity(ScableableImageActivity.class);
                break;
            case R.id.btn_viewpager:
                gotoActivity(MyViewPagerActivity.class);
                break;
            case R.id.btn_nestedview:
                gotoActivity(MyNestedViewActivity.class);
                break;
            case R.id.btn_textview:
                gotoActivity(MyTextViewActivity.class);
                break;
            case R.id.btn_multitouch:
                gotoActivity(MultiTouchViewActivity.class);
                break;
            case R.id.btn_rotateimage:
                gotoActivity(ImageFlipActivity.class);
                break;
            case R.id.btn_scalelayout:
                gotoActivity(ScaleLayoutActivity.class);
                break;
        }
    };
}