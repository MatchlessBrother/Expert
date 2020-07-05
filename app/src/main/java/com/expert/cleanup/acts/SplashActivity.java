package com.expert.cleanup.acts;

import android.os.Bundle;
import com.expert.cleanup.R;
import android.content.Intent;
import io.reactivex.Observable;
import com.expert.cleanup.BaseApp;
import android.widget.ProgressBar;
import androidx.annotation.Nullable;
import java.util.concurrent.TimeUnit;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class SplashActivity extends AppCompatActivity
{
    private ProgressBar mProgressBar;

    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        long needWaiteTime = 0l;/******************/
        mProgressBar = findViewById(R.id.progressbar);
        if(BaseApp.getInstance().isCachedFbInterstitial() || BaseApp.getInstance().isCachedGgInterstitial())
            needWaiteTime = 1000l;
        else
            needWaiteTime = 6000l;
        Observable.interval(0,needWaiteTime / 100,TimeUnit.MILLISECONDS).take(101).
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>()
        {
            public void accept(Long aLong) throws Exception
            {
                int value = Integer.valueOf(aLong.toString());
                mProgressBar.setProgress(value);
                if(value == 100)
                {
                    startActivity(new Intent( SplashActivity.this,MainActivity.class));
                    finish();
                }
            }
        });
    }
}