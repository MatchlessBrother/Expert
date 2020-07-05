package com.expert.cleanup.acts.base;

import android.os.Bundle;
import android.view.View;
import com.expert.cleanup.R;
import android.widget.ImageView;

import com.gyf.immersionbar.ImmersionBar;

import androidx.appcompat.app.AppCompatActivity;
import com.expert.cleanup.nativetos.base.FlashTool;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FlashLightActivity extends AppCompatActivity
{
    private View mStatebar;
    private ImageView mImgTorch;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashlight);
        mImgTorch = findViewById(R.id.img_torch);
        mStatebar  =  findViewById(R.id.statebar);
        ImmersionBar.with(this).statusBarView(mStatebar).init();
        mImgTorch.setImageDrawable(getResources().getDrawable(R.mipmap.flashlight_open));
        final FlashTool flashUtils = new FlashTool(this);
        flashUtils.converse();
        flashUtils.open();

        mImgTorch.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if (flashUtils.isStatus())
                {
                    flashUtils.close();
                    mImgTorch.setImageDrawable(getResources().getDrawable(R.mipmap.flashlight_close));
                }
                else
                {
                    flashUtils.open();
                    mImgTorch.setImageDrawable(getResources().getDrawable(R.mipmap.flashlight_open));
                }
            }
        });

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        Bundle bundle = new Bundle();/*************************************/
        mFirebaseAnalytics.logEvent("flashlightactivity_show",bundle);
    }

    protected void onDestroy()
    {
        super.onDestroy();
    }
}