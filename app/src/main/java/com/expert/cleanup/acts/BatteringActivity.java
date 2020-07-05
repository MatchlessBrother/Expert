package com.expert.cleanup.acts;

import android.os.Bundle;
import android.view.View;
import com.expert.cleanup.R;
import android.content.Intent;
import android.widget.TextView;
import java.text.DecimalFormat;
import android.widget.ImageView;
import com.expert.cleanup.BaseApp;
import android.animation.Animator;
import android.widget.LinearLayout;
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
import android.animation.AnimatorListenerAdapter;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import com.expert.cleanup.nativetos.HardwareTool;
import android.view.animation.DecelerateInterpolator;
import com.expert.cleanup.nativetos.base.WhiteListTool;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expert.cleanup.acts.base.DeviceInfoActivity;
import static com.expert.cleanup.acts.BoostActivity.getNextBoostTime;
import static com.expert.cleanup.acts.CleanActivity.getNextCleanTime;
import static com.expert.cleanup.acts.CpuActivity.getNextCoolDownTime;

public class BatteringActivity extends AppCompatActivity
{
    private View mStatebar;
    private boolean isLoadedAds;
    private ImageView mBatteringMenu;
    private ImageView mBatteriedMoon1;
    private ImageView mBatteriedMoon2;
    private ImageView mBatteriedMoon3;
    private TextView mBatteriedResult;
    private TextView mBatteringPercent;
    private ImageView mBatteriedBattery;
    private ImageView mBatteringInWindmill;
    private TextView mBatteringPercentNote;
    private LinearLayout mBatteriedAll;
    private TextView mBatteriedResultDetail;
    private ImageView mBatteringOut1Windmill;
    private ImageView mBatteringOut2Windmill;
    private ImageView mBatteringOut3Windmill;
    private LinearLayout mBatteringWindmillAll;

    private int mTriedNumsForShow;
    private Disposable mDisposable;
    private ImageView mBatteriedNote;
    private ScaleAnimation mRedPointSa;
    private boolean mBackEnable = false;
    private TextView mBatteriedRedBoostAll;
    private LinearLayout mBatteriedContent;
    private TextView mBatteriedRedCleanAll;
    private RelativeLayout mBatteriedBoostAll;
    private RelativeLayout mBatteriedCleanAll;
    private TextView mBatteriedRedCpucoolerAll;
    private RelativeLayout mBatteriedCpucoolerAll;
    private RelativeLayout mBatteriedInfoAll;

    private Disposable mDisposing;
    private int mTriedNumsForShowing;
    private LottieAnimationView mBatteringLottieview;

    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battering);
        mStatebar = findViewById(R.id.statebar);
        mBatteriedAll = findViewById(R.id.batteried_all);
        mBatteriedNote = findViewById(R.id.batteried_note);
        mBatteringMenu = findViewById(R.id.battering_menu);
        mBatteriedMoon1 = findViewById(R.id.batteried_moon1);
        mBatteriedMoon2 = findViewById(R.id.batteried_moon2);
        mBatteriedMoon3 = findViewById(R.id.batteried_moon3);
        mBatteriedResult = findViewById(R.id.batteried_result);
        ImmersionBar.with(this).statusBarView(mStatebar).init();
        mBatteriedBattery = findViewById(R.id.batteried_battery);
        mBatteringPercent = findViewById(R.id.battering_percent);
        mBatteriedBoostAll = findViewById(R.id.batteried_boost_all);
        mBatteringLottieview = findViewById(R.id.battering_lottieview);
        mBatteringInWindmill = findViewById(R.id.battering_in_windmill);
        mBatteringWindmillAll = findViewById(R.id.battering_windmill_all);
        mBatteringPercentNote = findViewById(R.id.battering_percent_note);
        mBatteriedRedBoostAll = findViewById(R.id.batteried_red_boost_all);
        mBatteringOut1Windmill = findViewById(R.id.battering_out1_windmill);
        mBatteringOut2Windmill = findViewById(R.id.battering_out2_windmill);
        mBatteringOut3Windmill = findViewById(R.id.battering_out3_windmill);
        mBatteriedResultDetail = findViewById(R.id.batteried_result_detail);
        /******************************************************************/
        mBatteriedContent = findViewById(R.id.batteried_content);
        mBatteriedInfoAll = findViewById(R.id.batteried_info_all);
        mBatteriedCleanAll = findViewById(R.id.batteried_clean_all);
        mBatteriedCpucoolerAll = findViewById(R.id.batteried_cpucooler_all);
        mBatteriedRedCleanAll = findViewById(R.id.batteried_red_clean_all);
        mBatteriedRedCpucoolerAll = findViewById(R.id.batteried_red_cpucooler_all);
        /******************************************************************/
        mBatteringPercent.setText(getIntent().getStringExtra("battery") + "%");
        if(getIntent().getBooleanExtra("justpowersaving",true))
        {
            if(null != mBatteringWindmillAll && null != mBatteriedAll &&
               null != mBatteringOut1Windmill && null != mBatteringOut2Windmill && null != mBatteringOut3Windmill)
            {
                mBatteringWindmillAll.setVisibility(View.GONE);
                mBatteriedAll.setVisibility(View.VISIBLE);
                mBatteringOut1Windmill.clearAnimation();
                mBatteringOut1Windmill.invalidate();
                mBatteringOut2Windmill.clearAnimation();
                mBatteringOut2Windmill.invalidate();
                mBatteringOut3Windmill.clearAnimation();
                mBatteringOut3Windmill.invalidate();
            }
            batteried();
        }
        else
        {
            mDisposing = null;
            mTriedNumsForShowing = 0;
            mBatteringLottieview.playAnimation();
        }

        mBatteringLottieview.addAnimatorListener(new AnimatorListenerAdapter()
        {
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                if(null != mBatteringWindmillAll  && null != mBatteriedAll/* && null != mBatteringInWindmill &&
                   null != mBatteringOut1Windmill && null != mBatteringOut2Windmill && null != mBatteringOut3Windmill*/)
                {
                    if(null != mDisposing && !mDisposing.isDisposed())
                        mDisposing.dispose();
                    mBatteringWindmillAll.setVisibility(View.GONE);
                    mBatteringLottieview.setVisibility(View.GONE);
                    mBatteriedAll.setVisibility(View.VISIBLE);
                    isLoadedAds = false;
                    batteried();
                }
            }
        });

        mBatteriedCpucoolerAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(mBackEnable)
                {
                    if(getNextCoolDownTime(BatteringActivity.this) > System.currentTimeMillis())
                    {
                        Intent intent = new Intent(BatteringActivity.this, CpuingActivity.class);
                        intent.putExtra("temperature",new DecimalFormat("#.0").format(
                        HardwareTool.getCpuTemperatureFinder(BatteringActivity.this)) + "℃" +
                        "/" +  (new DecimalFormat("#.0").format(32+ HardwareTool.
                        getCpuTemperatureFinder(BatteringActivity.this) * 1.8))+ "℉");
                        intent.putExtra("justcpucooler",false);
                        intent.putExtra("iscooler",true);
                        intent.putExtra("appNumbers",0);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                       startActivity(new Intent(BatteringActivity.this,CpuActivity.class));
                       finish();
                    }
                }
            }
        });

        mBatteriedCleanAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(mBackEnable)
                {
                    if(getNextCleanTime(BatteringActivity.this) > System.currentTimeMillis())
                    {
                        Intent intent = new Intent(BatteringActivity.this,CleaningActivity.class);
                        intent.putExtra("justcleanedup",false);
                        intent.putExtra("iscleaned",true);
                        intent.putExtra("garbageSize","");
                        intent.putExtra("appNumbers",0);
                        startActivity(intent);
                        finish();
                    }

                    else
                    {
                       startActivity(new Intent(BatteringActivity.this,CleanActivity.class));
                       finish();
                    }
                }
            }
        });

        mBatteriedBoostAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(mBackEnable)
                {
                    if(getNextBoostTime(BatteringActivity.this) > System.currentTimeMillis())
                    {
                        Intent intent = new Intent(BatteringActivity.this,BoostingActivity.class);
                        intent.putExtra("justboost",false);
                        intent.putExtra("appNumbers",0);
                        intent.putExtra("isboost",true);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(BatteringActivity.this,BoostActivity.class));
                        finish();
                    }
                }
            }
        });

        mBatteriedInfoAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(mBackEnable)
                {
                    startActivity(new Intent(BatteringActivity.this, DeviceInfoActivity.class));
                    finish();
                }
            }
        });

        mBatteringMenu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(mBackEnable)
                {
                    finish();
                }
            }
        });

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getApplicationContext());
        Bundle bundle = new Bundle();/*************************************/
        mFirebaseAnalytics.logEvent("batteringactivity1_show",bundle);
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
        if(null != mBatteriedRedCpucoolerAll.getAnimation())mBatteriedRedCpucoolerAll.clearAnimation();
        if(getNextCoolDownTime(this) > System.currentTimeMillis())
        {
            mBatteriedRedCpucoolerAll.setVisibility(View.INVISIBLE);
        }
        else
        {
            mBatteriedRedCpucoolerAll.setVisibility(View.VISIBLE);
            if(CpuActivity.getLatestApp(this).size() > 0)
            {
                mBatteriedRedCpucoolerAll.startAnimation(mRedPointSa);
                mBatteriedRedCpucoolerAll.setText(/*CpuActivity.getLatestApp(this).size() +*/ "");
            }
            else
            {
                if(CpuActivity.getCoolerCpuApps(this) != 0)
                {
                    mBatteriedRedCpucoolerAll.startAnimation(mRedPointSa);
                    mBatteriedRedCpucoolerAll.setText(/*CpuActivity.getCoolerCpuApps(this) +*/ "");
                }
                else
                {
                    int cpuApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(this)) % 7 + 6);
                    if(cpuApps > WhiteListTool.getWhiteListSize(this))
                        cpuApps = WhiteListTool.getWhiteListSize(this);
                    CpuActivity.setCoolerCpuApps(this,cpuApps);
                    if(0 != cpuApps)
                    {
                        mBatteriedRedCpucoolerAll.startAnimation(mRedPointSa);
                        mBatteriedRedCpucoolerAll.setText(/*cpuApps +*/ "");
                    }
                    else
                        mBatteriedRedCpucoolerAll.setVisibility(View.INVISIBLE);
                }
            }
        }
        /******************************************************************/
        if(null != mBatteriedRedCleanAll.getAnimation())mBatteriedRedCleanAll.clearAnimation();
        if(getNextCleanTime(this) > System.currentTimeMillis())
        {
            mBatteriedRedCleanAll.setVisibility(View.INVISIBLE);
        }
        else
        {
            mBatteriedRedCleanAll.setVisibility(View.VISIBLE);
            if(CleanActivity.getLatestApp(this).size() > 0)
            {
                mBatteriedRedCleanAll.startAnimation(mRedPointSa);
                mBatteriedRedCleanAll.setText(/*CleanActivity.getLatestApp(this).size() +*/ "");
            }
            else
            {
                if(CleanActivity.getCleanApps(this) != 0)
                {
                    mBatteriedRedCleanAll.startAnimation(mRedPointSa);
                    mBatteriedRedCleanAll.setText(/*CleanActivity.getCleanApps(this) +*/ "");
                }
                else
                {
                    int cleanApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(this)) % 7 + 6);
                    if(cleanApps > WhiteListTool.getWhiteListSize(this))
                        cleanApps = WhiteListTool.getWhiteListSize(this);
                    CleanActivity.setCleanApps(this,cleanApps);
                    if(0 != cleanApps)
                    {
                        mBatteriedRedCleanAll.startAnimation(mRedPointSa);
                        mBatteriedRedCleanAll.setText(/*cleanApps +*/ "");
                    }
                    else
                        mBatteriedRedCleanAll.setVisibility(View.INVISIBLE);
                }
            }
        }
        /******************************************************************/
        if(null != mBatteriedRedBoostAll.getAnimation())mBatteriedRedBoostAll.clearAnimation();
        if(getNextBoostTime(this) > System.currentTimeMillis())
        {
            mBatteriedRedBoostAll.setVisibility(View.INVISIBLE);
        }
        else
        {
            mBatteriedRedBoostAll.setVisibility(View.VISIBLE);
            if(BoostActivity.getLatestApp(this).size() > 0)
            {
                mBatteriedRedBoostAll.startAnimation(mRedPointSa);
                mBatteriedRedBoostAll.setText(/*BoostActivity.getLatestApp(this).size() +*/ "");
            }
            else
            {
                if(BoostActivity.getBoostApps(this) != 0)
                {
                    mBatteriedRedBoostAll.startAnimation(mRedPointSa);
                    mBatteriedRedBoostAll.setText(/*BoostActivity.getBoostApps(this) +*/ "");
                }
                else
                {
                    int boostApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(this)) % 7 + 6);
                    if(boostApps > WhiteListTool.getWhiteListSize(this))
                        boostApps = WhiteListTool.getWhiteListSize(this);
                    BoostActivity.setBoostApps(this,boostApps);
                    if(0 != boostApps)
                    {
                        mBatteriedRedBoostAll.startAnimation(mRedPointSa);
                        mBatteriedRedBoostAll.setText(/*boostApps +*/ "");
                    }
                    else
                        mBatteriedRedBoostAll.setVisibility(View.INVISIBLE);
                }
            }
        }
        NotifyService.startUpdateNotify(getApplicationContext());
    }

    protected void onPause()
    {
        super.onPause();
        if(null != mBatteriedRedCleanAll.getAnimation())mBatteriedRedCleanAll.clearAnimation();
        if(null != mBatteriedRedBoostAll.getAnimation())mBatteriedRedBoostAll.clearAnimation();
        if(null != mBatteriedRedCpucoolerAll.getAnimation())mBatteriedRedCpucoolerAll.clearAnimation();
    }

    private void battering()
    {
        if(null != mBatteringOut1Windmill && null != mBatteringOut2Windmill && null != mBatteringOut3Windmill)
        {
            RotateAnimation bigRotateAnimation1 = new RotateAnimation(360,0,
            Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            LinearInterpolator linearInterpolator1 = new LinearInterpolator();
            bigRotateAnimation1.setDuration(30000l);
            bigRotateAnimation1.setRepeatMode(Animation.RESTART);
            bigRotateAnimation1.setRepeatCount(Animation.INFINITE);
            bigRotateAnimation1.setInterpolator(linearInterpolator1);
            mBatteringOut1Windmill.startAnimation(bigRotateAnimation1);
            /*******************************************************************/
            RotateAnimation bigRotateAnimation2 = new RotateAnimation(660,0,
            Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            LinearInterpolator linearInterpolator2 = new LinearInterpolator();
            bigRotateAnimation2.setDuration(20000l);
            bigRotateAnimation2.setRepeatMode(Animation.RESTART);
            bigRotateAnimation2.setRepeatCount(Animation.INFINITE);
            bigRotateAnimation2.setInterpolator(linearInterpolator2);
            bigRotateAnimation2.setStartOffset(400);
            mBatteringOut2Windmill.startAnimation(bigRotateAnimation2);
            /*******************************************************************/
            RotateAnimation bigRotateAnimation3 = new RotateAnimation(880,0,
            Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            LinearInterpolator linearInterpolator3 = new LinearInterpolator();
            bigRotateAnimation3.setDuration(10000l);
            bigRotateAnimation3.setRepeatMode(Animation.RESTART);
            bigRotateAnimation3.setRepeatCount(Animation.INFINITE);
            bigRotateAnimation3.setInterpolator(linearInterpolator3);
            bigRotateAnimation3.setStartOffset(800);
            mBatteringOut3Windmill.startAnimation(bigRotateAnimation3);
        }
    }

    private void batteried()
    {
        ((BaseApp)getApplication()).startGgInterstitial();
        if(null != mBatteriedAll && null != mBatteriedResult && null != mBatteriedResultDetail)
        {
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getApplicationContext());
            Bundle bundle = new Bundle();/*************************************/
            mFirebaseAnalytics.logEvent("batteringactivity2_show",bundle);
            mFirebaseAnalytics.logEvent("resultaction_show",bundle);
            if(getIntent().getBooleanExtra("justpowersaving",true))
            {
                mBatteriedResult.setVisibility(View.GONE);
                mBatteriedResultDetail.setText(getString(R.string.batterynote));
            }
            else
            {
                if(getIntent().getBooleanExtra("iselectricitied",false))
                {
                    mBatteriedResult.setVisibility(View.GONE);
                    mBatteriedResultDetail.setText(getString(R.string.batterynote));
                }
                else
                {
                    mBatteriedResultDetail.setTextSize(16);
                    mBatteriedResult.setText(getString(R.string.hibernated) + " " + getIntent().getIntExtra("appNumbers",0) + " " + getString(R.string.apps));
                    mBatteriedResultDetail.setText(getString(R.string.batterydetail));
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
            });mBatteriedAll.startAnimation(translateAnimation);
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