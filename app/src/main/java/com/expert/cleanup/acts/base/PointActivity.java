package com.expert.cleanup.acts.base;

import android.os.Bundle;
import android.view.Window;
import android.app.Activity;
import android.view.Gravity;
import io.reactivex.Observable;
import com.expert.cleanup.BaseApp;
import android.view.WindowManager;
import android.graphics.PixelFormat;
import androidx.annotation.Nullable;
import java.util.concurrent.TimeUnit;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class PointActivity  extends Activity
{
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Window window = getWindow();/*****/
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.format = PixelFormat.TRANSPARENT;params.x = 0;params.y = 0;params.width = 1;params.height = 1;
        params.windowAnimations = android.R.style.Animation_Translucent;window.setAttributes(params);/******/
        BaseApp.getInstance().mFbInterstitialAdForDirect.show();/*******************************************/
        Observable.timer(6000L,TimeUnit.MILLISECONDS,Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>()
        {
            public void accept(Long aLong) throws Exception
            {
                if(!isFinishing())finish();
            }
        });
    }
}