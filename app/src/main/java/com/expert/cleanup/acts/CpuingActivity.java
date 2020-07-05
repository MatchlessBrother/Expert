package com.expert.cleanup.acts;

import android.os.Bundle;
import android.view.View;
import com.expert.cleanup.R;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageView;
import com.expert.cleanup.BaseApp;
import android.os.BatteryManager;
import android.animation.Animator;
import android.widget.LinearLayout;
import android.content.IntentFilter;
import androidx.annotation.Nullable;
import android.widget.RelativeLayout;
import android.view.animation.Animation;
import com.gyf.immersionbar.ImmersionBar;
import io.reactivex.disposables.Disposable;
import android.view.animation.ScaleAnimation;
import com.airbnb.lottie.LottieAnimationView;
import android.view.animation.RotateAnimation;
import com.expert.cleanup.sers.NotifyService;
import androidx.appcompat.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.animation.AnimatorListenerAdapter;
import android.view.animation.DecelerateInterpolator;
import com.expert.cleanup.nativetos.base.WhiteListTool;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expert.cleanup.acts.base.DeviceInfoActivity;
import static com.expert.cleanup.acts.BoostActivity.getNextBoostTime;
import static com.expert.cleanup.acts.CleanActivity.getNextCleanTime;
import static com.expert.cleanup.acts.BatteryActivity.getNextHibernateTime;

public class CpuingActivity extends AppCompatActivity
{
    private View mStatebar;
    private boolean isLoadedAds;
    private ImageView mCpuingMenu;
    private LinearLayout mCpuedAll;
    private ImageView mCpuedSnowflake1;
    private ImageView mCpuedSnowflake2;
    private TextView mCpuedResultDetail;
    private ImageView mCpuedThermometer;
    private TextView mCpuingTemperature;
    private ImageView mCpuingOutWindmill;
    private ImageView mCpuingInWindmill;
    private LinearLayout mCpuingWindmillAll;
    private TextView mCpuingTemperature_note;

    private ImageView mCpuedNote;
    private int mTriedNumsForShow;
    private Disposable mDisposable;
    private ScaleAnimation mRedPointSa;
    private LinearLayout mCpuedContent;
    private boolean mBackEnable = false;
    private TextView mCpuedRedCleanAll;
    private TextView mCpuedRedBatteryAll;
    private RelativeLayout mCpuedCleanAll;
    private RelativeLayout mCpuedBatteryAll;
    private TextView mCpuedRedBoostAll;
    private RelativeLayout mCpuedBoostAll;
    private RelativeLayout mCpuedInfoAll;

    private Disposable mDisposing;
    private int mTriedNumsForShowing;
    private LottieAnimationView mCpuingLottieview;

    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpuing);
        mStatebar = findViewById(R.id.statebar);
        mCpuedAll = findViewById(R.id.cpued_all);
        mCpuingMenu = findViewById(R.id.cpuing_menu);
        mCpuedNote = findViewById(R.id.cpued_note);
        mCpuedInfoAll = findViewById(R.id.cpued_info_all);
        mCpuedBoostAll = findViewById(R.id.cpued_boost_all);
        mCpuedSnowflake1 = findViewById(R.id.cpued_snowflake1);
        mCpuedSnowflake2 = findViewById(R.id.cpued_snowflake2);
        mCpuingLottieview = findViewById(R.id.cpuing_lottieview);
        ImmersionBar.with(this).statusBarView(mStatebar).init();
        mCpuedThermometer = findViewById(R.id.cpued_thermometer);
        mCpuingInWindmill = findViewById(R.id.cpuing_in_windmill);
        mCpuingTemperature = findViewById(R.id.cpuing_temperature);
        mCpuedRedBoostAll = findViewById(R.id.cpued_red_boost_all);
        mCpuingWindmillAll = findViewById(R.id.cpuing_windmill_all);
        mCpuingOutWindmill = findViewById(R.id.cpuing_out_windmill);
        mCpuedResultDetail = findViewById(R.id.cpued_result_detail);

        mCpuingTemperature_note = findViewById(R.id.cpuing_temperature_note);
        mCpuingTemperature.setText(getIntent().getStringExtra("temperature"));
        /********************************************************************/
        mCpuedContent = findViewById(R.id.cpued_content);
        mCpuedCleanAll = findViewById(R.id.cpued_clean_all);
        mCpuedBatteryAll = findViewById(R.id.cpued_battery_all);
        mCpuedRedCleanAll = findViewById(R.id.cpued_red_clean_all);
        mCpuedRedBatteryAll = findViewById(R.id.cpued_red_battery_all);

        /********************************************************************/
        if(getIntent().getBooleanExtra("justcpucooler",true))
        {
            if(null != mCpuingWindmillAll && null != mCpuingOutWindmill && null != mCpuedAll)
            {
                mCpuingWindmillAll.setVisibility(View.GONE);
                mCpuedAll.setVisibility(View.VISIBLE);
                mCpuingOutWindmill.clearAnimation();
                mCpuingOutWindmill.invalidate();
            }
            cpued();
        }
        else
        {
            mDisposing = null;
            mTriedNumsForShowing = 0;
            mCpuingLottieview.playAnimation();
        }

        mCpuingLottieview.addAnimatorListener(new AnimatorListenerAdapter()
        {
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                if(null != mCpuedAll /*&& null != mCpuingInWindmill && null != mCpuingOutWindmill*/ && null != mCpuingWindmillAll)
                {
                    if(null != mDisposing && !mDisposing.isDisposed())
                        mDisposing.dispose();
                    mCpuingWindmillAll.setVisibility(View.GONE);
                    mCpuingLottieview.setVisibility(View.GONE);
                    mCpuedAll.setVisibility(View.VISIBLE);/**/
                    isLoadedAds = false;
                    cpued();
                }
            }
        });

        mCpuedCleanAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(mBackEnable)
                {
                    if(getNextCleanTime(CpuingActivity.this) > System.currentTimeMillis())
                    {
                        Intent intent = new Intent(CpuingActivity.this,CleaningActivity.class);
                        intent.putExtra("justcleanedup",false);
                        intent.putExtra("iscleaned",true);
                        intent.putExtra("garbageSize","");
                        intent.putExtra("appNumbers",0);
                        startActivity(intent);
                        finish();
                    }

                    else
                    {
                        startActivity(new Intent(CpuingActivity.this,CleanActivity.class));
                        finish();
                    }
                }
            }
        });

        mCpuedBatteryAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(mBackEnable)
                {
                    if(getNextHibernateTime(CpuingActivity.this) > System.currentTimeMillis())
                    {
                        Intent intent = new Intent(CpuingActivity.this,BatteringActivity.class);
                        intent.putExtra("battery",CpuingActivity.this.registerReceiver(null,new
                        IntentFilter(Intent.ACTION_BATTERY_CHANGED)).getIntExtra(BatteryManager.EXTRA_LEVEL, 0) + "");
                        intent.putExtra("justpowersaving",false);
                        intent.putExtra("iselectricitied",true);
                        intent.putExtra("appNumbers",0);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(CpuingActivity.this,BatteryActivity.class));
                        finish();
                    }
                }
            }
        });

        mCpuedBoostAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(mBackEnable)
                {
                    if(getNextBoostTime(CpuingActivity.this) > System.currentTimeMillis())
                    {
                        Intent intent = new Intent(CpuingActivity.this,BoostingActivity.class);
                        intent.putExtra("justboost",false);
                        intent.putExtra("appNumbers",0);
                        intent.putExtra("isboost",true);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(CpuingActivity.this,BoostActivity.class));
                        finish();
                    }
                }
            }
        });

        mCpuedInfoAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(mBackEnable)
                {
                    startActivity(new Intent(CpuingActivity.this,DeviceInfoActivity.class));
                    finish();
                }
            }
        });

        mCpuingMenu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(mBackEnable)
                    finish();
            }
        });

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getApplicationContext());
        Bundle bundle = new Bundle();/*************************************/
        mFirebaseAnalytics.logEvent("cpuingactivity1_show",bundle);
        mRedPointSa = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        mRedPointSa.setInterpolator(new LinearInterpolator());
        mRedPointSa.setRepeatCount(Animation.INFINITE);
        mRedPointSa.setRepeatMode(Animation.REVERSE);
        mRedPointSa.setDuration(500l);
    }

    protected void onResume()
    {
        super.onResume();
        /******************************************************************/
        if(null != mCpuedRedCleanAll.getAnimation())mCpuedRedCleanAll.clearAnimation();
        if(getNextCleanTime(this) > System.currentTimeMillis())
        {
            mCpuedRedCleanAll.setVisibility(View.INVISIBLE);
        }
        else
        {
            mCpuedRedCleanAll.setVisibility(View.VISIBLE);
            if(CleanActivity.getLatestApp(this).size() > 0)
            {
                mCpuedRedCleanAll.startAnimation(mRedPointSa);
                mCpuedRedCleanAll.setText(/*CleanActivity.getLatestApp(this).size() +*/ "");
            }
            else
            {
                if(CleanActivity.getCleanApps(this) != 0)
                {
                    mCpuedRedCleanAll.startAnimation(mRedPointSa);
                    mCpuedRedCleanAll.setText(/*CleanActivity.getCleanApps(this) +*/ "");
                }
                else
                {
                    int cleanApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(this)) % 7 + 6);
                    if(cleanApps > WhiteListTool.getWhiteListSize(this))
                        cleanApps = WhiteListTool.getWhiteListSize(this);
                    CleanActivity.setCleanApps(this,cleanApps);
                    if(0 != cleanApps)
                    {
                        mCpuedRedCleanAll.startAnimation(mRedPointSa);
                        mCpuedRedCleanAll.setText(/*cleanApps +*/ "");
                    }
                    else
                        mCpuedRedCleanAll.setVisibility(View.INVISIBLE);
                }
            }
        }
        /******************************************************************/
        if(null != mCpuedRedBatteryAll.getAnimation())mCpuedRedBatteryAll.clearAnimation();
        if(getNextHibernateTime(this) > System.currentTimeMillis())
        {
            mCpuedRedBatteryAll.setVisibility(View.INVISIBLE);
        }
        else
        {
            mCpuedRedBatteryAll.setVisibility(View.VISIBLE);
            if(BatteryActivity.getLatestApp(this).size() > 0)
            {
                mCpuedRedBatteryAll.startAnimation(mRedPointSa);
                mCpuedRedBatteryAll.setText(/*BatteryActivity.getLatestApp(this).size() +*/ "");
            }
            else
            {
                if(BatteryActivity.getBatteryApps(this) != 0)
                {
                    mCpuedRedBatteryAll.startAnimation(mRedPointSa);
                    mCpuedRedBatteryAll.setText(/*BatteryActivity.getBatteryApps(this) +*/ "");
                }
                else
                {
                    int batteryApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(this)) % 7 + 6);
                    if(batteryApps > WhiteListTool.getWhiteListSize(this))
                        batteryApps = WhiteListTool.getWhiteListSize(this);
                    BatteryActivity.setBatteryApps(this,batteryApps);
                    if(0 != batteryApps)
                    {
                        mCpuedRedBatteryAll.startAnimation(mRedPointSa);
                        mCpuedRedBatteryAll.setText(/*batteryApps +*/ "");
                    }
                    else
                        mCpuedRedBatteryAll.setVisibility(View.INVISIBLE);
                }
            }
        }
        /******************************************************************/
        if(null != mCpuedRedBoostAll.getAnimation())mCpuedRedBoostAll.clearAnimation();
        if(getNextBoostTime(this) > System.currentTimeMillis())
        {
            mCpuedRedBoostAll.setVisibility(View.INVISIBLE);
        }
        else
        {
            mCpuedRedBoostAll.setVisibility(View.VISIBLE);
            if(BoostActivity.getLatestApp(this).size() > 0)
            {
                mCpuedRedBoostAll.startAnimation(mRedPointSa);
                mCpuedRedBoostAll.setText(/*BoostActivity.getLatestApp(this).size() +*/ "");
            }
            else
            {
                if(BoostActivity.getBoostApps(this) != 0)
                {
                    mCpuedRedBoostAll.startAnimation(mRedPointSa);
                    mCpuedRedBoostAll.setText(/*BoostActivity.getBoostApps(this) +*/ "");
                }
                else
                {
                    int boostApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(this)) % 7 + 6);
                    if(boostApps > WhiteListTool.getWhiteListSize(this))
                        boostApps = WhiteListTool.getWhiteListSize(this);
                    BoostActivity.setBoostApps(this,boostApps);
                    if(0 != boostApps)
                    {
                        mCpuedRedBoostAll.startAnimation(mRedPointSa);
                        mCpuedRedBoostAll.setText(/*boostApps +*/ "");
                    }
                    else
                        mCpuedRedBoostAll.setVisibility(View.INVISIBLE);
                }
            }
        }
        NotifyService.startUpdateNotify(getApplicationContext());
    }

    protected void onPause()
    {
        super.onPause();
        if(null != mCpuedRedCleanAll.getAnimation())mCpuedRedCleanAll.clearAnimation();
        if(null != mCpuedRedBoostAll.getAnimation())mCpuedRedBoostAll.clearAnimation();
        if(null != mCpuedRedBatteryAll.getAnimation())mCpuedRedBatteryAll.clearAnimation();
    }

    private void cpuing()
    {
        if(null != mCpuingOutWindmill)
        {
            RotateAnimation bigRotateAnimation = new RotateAnimation(4320,0,
            Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_PARENT,0.5f);
            LinearInterpolator linearInterpolator = new LinearInterpolator();
            bigRotateAnimation.setDuration(4000l);
            bigRotateAnimation.setRepeatMode(Animation.RESTART);
            bigRotateAnimation.setRepeatCount(Animation.INFINITE);
            bigRotateAnimation.setInterpolator(linearInterpolator);
            mCpuingOutWindmill.startAnimation(bigRotateAnimation);
        }
    }

    private void cpued()
    {
        ((BaseApp)getApplication()).startGgInterstitial();
        if(null != mCpuedAll && null != mCpuedResultDetail)
        {
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getApplicationContext());
            Bundle bundle = new Bundle();/*************************************/
            mFirebaseAnalytics.logEvent("cpuingactivity2_show",bundle);
            mFirebaseAnalytics.logEvent("resultaction_show",bundle);
            if(getIntent().getBooleanExtra("justcpucooler",true))
                mCpuedResultDetail.setText(getString(R.string.cpunote1));
            else
            {
                if(getIntent().getBooleanExtra("iscooler",false))
                {
                    mCpuedResultDetail.setText(getString(R.string.cpunote1));
                }
                else
                {
                    mCpuedResultDetail.setText(getString(R.string.cpunote2));
                }
            }
            /*****************************************************************************/
            mDisposable = null;
            mTriedNumsForShow = 0;
            TranslateAnimation translateAnimation = new TranslateAnimation(
            TranslateAnimation.RELATIVE_TO_PARENT,0f,TranslateAnimation.RELATIVE_TO_PARENT,0f,
            TranslateAnimation.RELATIVE_TO_PARENT,1f,TranslateAnimation.RELATIVE_TO_PARENT,0f);
            DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
            translateAnimation.setInterpolator(decelerateInterpolator);
            translateAnimation.setRepeatCount(0);
            translateAnimation.setDuration(1000l);
            translateAnimation.setFillAfter(true);
            translateAnimation.setAnimationListener(new Animation.AnimationListener()
            {
                public void onAnimationRepeat(Animation animation){}
                public void onAnimationStart(Animation animation){}
                public void onAnimationEnd(Animation animation)
                {
                    mBackEnable = true;
                }
            });mCpuedAll.startAnimation(translateAnimation);
        }
    }

    protected void onDestroy()
    {
        super.onDestroy();
        if(null != mDisposable && !mDisposable.isDisposed())
            mDisposable.dispose();
    }

    public void onBackPressed()
    {
        if(mBackEnable)
            super.onBackPressed();
    }
}