package com.expert.cleanup.acts.base;

import android.os.Bundle;
import android.view.Window;
import android.app.Activity;
import android.view.Gravity;
import android.view.WindowManager;
import android.graphics.PixelFormat;
import androidx.annotation.Nullable;

public class SingleInstanceActivity extends Activity
{
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Window window = getWindow();/*****/
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.format = PixelFormat.TRANSPARENT;params.x = 0;params.y = 0;params.width = 1;params.height = 1;
        params.windowAnimations = android.R.style.Animation_Translucent;window.setAttributes(params);/******/
    }

    protected void onPause()
    {
        super.onPause();
        finish();
    }
}