package com.expert.cleanup.acts;

import android.os.Bundle;
import android.view.View;
import com.expert.cleanup.R;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;
import java.text.DecimalFormat;
import android.widget.ImageView;
import com.expert.cleanup.BaseApp;
import android.os.BatteryManager;
import android.animation.Animator;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import android.content.IntentFilter;
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
import android.animation.AnimatorListenerAdapter;
import android.view.animation.TranslateAnimation;
import com.expert.cleanup.nativetos.HardwareTool;
import android.view.animation.DecelerateInterpolator;
import com.expert.cleanup.nativetos.base.WhiteListTool;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expert.cleanup.acts.base.DeviceInfoActivity;
import static com.expert.cleanup.acts.BoostActivity.getNextBoostTime;
import static com.expert.cleanup.acts.CpuActivity.getNextCoolDownTime;
import static com.expert.cleanup.acts.BatteryActivity.getNextHibernateTime;

public class CleaningActivity extends AppCompatActivity
{
    private View mStatebar;
    private boolean isLoadedAds;
    private TextView mCleanResult;
    private ImageView mCleaningMenu;
    private LinearLayout mCleanedAll;
    private ImageView mCleanedStart1;
    private ImageView mCleanedStart2;
    private ImageView mCleanedSweetor;
    private TextView mCleanResultDetail;
    private ImageView mCleaningWindmill;
    private TextView mCleaningWindmillNote;
    private LinearLayout mCleaningWindmillAll;

    private int mTriedNumsForShow;
    private Disposable mDisposable;
    private ImageView mCleanedNote;
    private ScaleAnimation mRedPointSa;
    private boolean mBackEnable = false;
    private LinearLayout mCleanedContent;
    private TextView mCleanedRedBoostAll;
    private TextView mCleanedRedBatteryAll;
    private RelativeLayout mCleanedInfoAll;
    private RelativeLayout mCleanedBoostAll;
    private TextView mCleanedRedCpucoolerAll;
    private RelativeLayout mCleanedBatteryAll;
    private RelativeLayout mCleanedCpucoolerAll;

    private Disposable mDisposing;
    private int mTriedNumsForShowing;
    private LottieAnimationView mCleaningLottieview;

    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cleaning);
        mStatebar = findViewById(R.id.statebar);/***/
        mCleanedAll = findViewById(R.id.cleaned_all);
        mCleanedNote = findViewById(R.id.cleaned_note);
        mCleanResult = findViewById(R.id.clean_result);
        mCleaningMenu = findViewById(R.id.cleaning_menu);
        mCleanedStart2 = findViewById(R.id.cleaned_start2);
        mCleanedStart1 = findViewById(R.id.cleaned_start1);
        mCleanedSweetor = findViewById(R.id.cleaned_sweetor);
        ImmersionBar.with(this).statusBarView(mStatebar).init();
        mCleanedBoostAll = findViewById(R.id.cleaned_boost_all);
        mCleaningWindmill = findViewById(R.id.cleaning_windmill);
        mCleanResultDetail = findViewById(R.id.clean_result_detail);
        mCleaningLottieview = findViewById(R.id.cleaning_lottieview);
        mCleanedRedBoostAll = findViewById(R.id.cleaned_red_boost_all);
        mCleaningWindmillAll = findViewById(R.id.cleaning_windmill_all);
        mCleaningWindmillNote = findViewById(R.id.cleaning_windmill_note);
        /******************************************************************/
        mCleanedContent = findViewById(R.id.cleaned_content);
        mCleanedInfoAll = findViewById(R.id.cleaned_info_all);
        mCleanedBatteryAll = findViewById(R.id.cleaned_battery_all);
        mCleanedCpucoolerAll = findViewById(R.id.cleaned_cpucooler_all);
        mCleanedRedBatteryAll = findViewById(R.id.cleaned_red_battery_all);
        mCleanedRedCpucoolerAll = findViewById(R.id.cleaned_red_cpucooler_all);
        /******************************************************************/
        if(getIntent().getBooleanExtra("justcleanedup",true))
        {
            if(null != mCleaningWindmillAll && null != mCleanedAll && null != mCleaningWindmill)
            {
                mCleaningWindmillAll.setVisibility(View.GONE);
                mCleanedAll.setVisibility(View.VISIBLE);
                mCleaningWindmill.clearAnimation();
            }
            cleaned();
        }
        else
        {
            mDisposing = null;
            mTriedNumsForShowing = 0;
            mCleaningLottieview.playAnimation();
        }

        mCleaningLottieview.addAnimatorListener(new AnimatorListenerAdapter()
        {
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                if(null != mCleanedAll && null != mCleaningWindmill && null != mCleaningWindmillAll)
                {
                    if(null != mDisposing && !mDisposing.isDisposed())
                        mDisposing.dispose();
                    mCleaningWindmillAll.setVisibility(View.GONE);
                    mCleaningLottieview.setVisibility(View.GONE);
                    mCleanedAll.setVisibility(View.VISIBLE);
                    mCleaningWindmill.clearAnimation();
                    isLoadedAds = false;
                    cleaned();
                }
            }
        });

        mCleanedCpucoolerAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(mBackEnable)
                {
                    if(getNextCoolDownTime(CleaningActivity.this) > System.currentTimeMillis())
                    {
                        Intent intent = new Intent(CleaningActivity.this, CpuingActivity.class);
                        intent.putExtra("temperature",new DecimalFormat("#.0").format(
                        HardwareTool.getCpuTemperatureFinder(CleaningActivity.this)) + "℃" +
                        "/" +  (new DecimalFormat("#.0").format(32+ HardwareTool.
                        getCpuTemperatureFinder(CleaningActivity.this) * 1.8))+ "℉");
                        intent.putExtra("justcpucooler",false);
                        intent.putExtra("iscooler",true);
                        intent.putExtra("appNumbers",0);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(CleaningActivity.this,CpuActivity.class));
                        finish();
                    }
                }
            }
        });

        mCleanedBatteryAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(mBackEnable)
                {
                    if(getNextHibernateTime(CleaningActivity.this) > System.currentTimeMillis())
                    {
                        Intent intent = new Intent(CleaningActivity.this,BatteringActivity.class);
                        intent.putExtra("battery",CleaningActivity.this.registerReceiver(null,new
                        IntentFilter(Intent.ACTION_BATTERY_CHANGED)).getIntExtra(BatteryManager.EXTRA_LEVEL, 0) + "");
                        intent.putExtra("justpowersaving",false);
                        intent.putExtra("iselectricitied",true);
                        intent.putExtra("appNumbers",0);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(CleaningActivity.this,BatteryActivity.class));
                        finish();
                    }
                }
            }
        });

        mCleanedBoostAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(mBackEnable)
                {
                    if(getNextBoostTime(CleaningActivity.this) > System.currentTimeMillis())
                    {
                        Intent intent = new Intent(CleaningActivity.this,BoostingActivity.class);
                        intent.putExtra("justboost",false);
                        intent.putExtra("appNumbers",0);
                        intent.putExtra("isboost",true);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(CleaningActivity.this,BoostActivity.class));
                        finish();
                    }
                }
            }
        });

        mCleanedInfoAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(mBackEnable)
                {
                    startActivity(new Intent(CleaningActivity.this, DeviceInfoActivity.class));
                    finish();
                }
            }
        });

        mCleaningMenu.setOnClickListener(new View.OnClickListener()
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
        mFirebaseAnalytics.logEvent("cleaningactivity1_show",bundle);
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
        if(null != mCleanedRedCpucoolerAll.getAnimation())mCleanedRedCpucoolerAll.clearAnimation();
        if(getNextCoolDownTime(this) > System.currentTimeMillis())
        {
            mCleanedRedCpucoolerAll.setVisibility(View.INVISIBLE);
        }
        else
        {
            mCleanedRedCpucoolerAll.setVisibility(View.VISIBLE);
            if(CpuActivity.getLatestApp(this).size() > 0)
            {
                mCleanedRedCpucoolerAll.startAnimation(mRedPointSa);
                mCleanedRedCpucoolerAll.setText(/*CpuActivity.getLatestApp(this).size() +*/ "");
            }
            else
            {
                if(CpuActivity.getCoolerCpuApps(this) != 0)
                {
                    mCleanedRedCpucoolerAll.startAnimation(mRedPointSa);
                    mCleanedRedCpucoolerAll.setText(/*CpuActivity.getCoolerCpuApps(this) +*/ "");
                }
                else
                {
                    int cpuApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(this)) % 7 + 6);
                    if(cpuApps > WhiteListTool.getWhiteListSize(this))
                        cpuApps = WhiteListTool.getWhiteListSize(this);
                    CpuActivity.setCoolerCpuApps(this,cpuApps);
                    if(0 != cpuApps)
                    {
                        mCleanedRedCpucoolerAll.startAnimation(mRedPointSa);
                        mCleanedRedCpucoolerAll.setText(/*cpuApps +*/ "");
                    }
                    else
                        mCleanedRedCpucoolerAll.setVisibility(View.INVISIBLE);
                }
            }
        }
        /******************************************************************/
        if(null != mCleanedRedBatteryAll.getAnimation())mCleanedRedBatteryAll.clearAnimation();
        if(getNextHibernateTime(this) > System.currentTimeMillis())
        {
            mCleanedRedBatteryAll.setVisibility(View.INVISIBLE);
        }
        else
        {
            mCleanedRedBatteryAll.setVisibility(View.VISIBLE);
            if(BatteryActivity.getLatestApp(this).size() > 0)
            {
                mCleanedRedBatteryAll.startAnimation(mRedPointSa);
                mCleanedRedBatteryAll.setText(/*BatteryActivity.getLatestApp(this).size() +*/ "");
            }
            else
            {
                if(BatteryActivity.getBatteryApps(this) != 0)
                {
                    mCleanedRedBatteryAll.startAnimation(mRedPointSa);
                    mCleanedRedBatteryAll.setText(/*BatteryActivity.getBatteryApps(this) +*/ "");
                }
                else
                {
                    int batteryApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(this)) % 7 + 6);
                    if(batteryApps > WhiteListTool.getWhiteListSize(this))
                        batteryApps = WhiteListTool.getWhiteListSize(this);
                    BatteryActivity.setBatteryApps(this,batteryApps);
                    if(0 != batteryApps)
                    {
                        mCleanedRedBatteryAll.startAnimation(mRedPointSa);
                        mCleanedRedBatteryAll.setText(/*batteryApps +*/ "");
                    }
                    else
                        mCleanedRedBatteryAll.setVisibility(View.INVISIBLE);
                }
            }
        }
        /******************************************************************/
        if(null != mCleanedRedBoostAll.getAnimation())mCleanedRedBoostAll.clearAnimation();
        if(getNextBoostTime(this) > System.currentTimeMillis())
        {
            mCleanedRedBoostAll.setVisibility(View.INVISIBLE);
        }
        else
        {
            mCleanedRedBoostAll.setVisibility(View.VISIBLE);
            if(BoostActivity.getLatestApp(this).size() > 0)
            {
                mCleanedRedBoostAll.startAnimation(mRedPointSa);
                mCleanedRedBoostAll.setText(/*BoostActivity.getLatestApp(this).size() +*/ "");
            }
            else
            {
                if(BoostActivity.getBoostApps(this) != 0)
                {
                    mCleanedRedBoostAll.startAnimation(mRedPointSa);
                    mCleanedRedBoostAll.setText(/*BoostActivity.getBoostApps(this) +*/ "");
                }
                else
                {
                    int boostApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(this)) % 7 + 6);
                    if(boostApps > WhiteListTool.getWhiteListSize(this))
                        boostApps = WhiteListTool.getWhiteListSize(this);
                    BoostActivity.setBoostApps(this,boostApps);
                    if(0 != boostApps)
                    {
                        mCleanedRedBoostAll.startAnimation(mRedPointSa);
                        mCleanedRedBoostAll.setText(/*boostApps +*/ "");
                    }
                    else
                        mCleanedRedBoostAll.setVisibility(View.INVISIBLE);
                }
            }
        }
        NotifyService.startUpdateNotify(getApplicationContext());
    }

    protected void onPause()
    {
        super.onPause();
        if(null != mCleanedRedCpucoolerAll.getAnimation())mCleanedRedCpucoolerAll.clearAnimation();
        if(null != mCleanedRedBatteryAll.getAnimation())mCleanedRedBatteryAll.clearAnimation();
        if(null != mCleanedRedBoostAll.getAnimation())mCleanedRedBoostAll.clearAnimation();
    }

    private void cleaning()
    {
        if(null != mCleaningWindmill)
        {
            RotateAnimation rotateAnimation = new RotateAnimation(0,4320,
            Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
            LinearInterpolator linearInterpolator = new LinearInterpolator();
            rotateAnimation.setDuration(6000l);
            rotateAnimation.setRepeatMode(Animation.RESTART);
            rotateAnimation.setRepeatCount(Animation.INFINITE);
            rotateAnimation.setInterpolator(linearInterpolator);
            mCleaningWindmill.startAnimation(rotateAnimation);
        }
    }

    private void cleaned()
    {
        ((BaseApp)getApplication()).startGgInterstitial();
        if(null != mCleanedAll && null != mCleanedStart1 && null != mCleanedStart2 && null != mCleanResultDetail)
        {
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getApplicationContext());
            Bundle bundle = new Bundle();/*************************************/
            mFirebaseAnalytics.logEvent("cleaningactivity2_show",bundle);
            mFirebaseAnalytics.logEvent("resultaction_show",bundle);
            if(getIntent().getBooleanExtra("justcleanedup",true))
            {
                mCleanResult.setVisibility(View.GONE);
                mCleanResultDetail.setText(getString(R.string.cleannote));
            }
            else
            {
                if(getIntent().getBooleanExtra("iscleaned",false))
                {
                    mCleanResult.setVisibility(View.GONE);
                    mCleanResultDetail.setText(getString(R.string.cleannote));
                }
                else
                {
                    mCleanResult.setVisibility(View.VISIBLE);
                    if(null != getIntent().getStringExtra("garbageSize") && !"".equals(getIntent().getStringExtra("garbageSize")))
                        mCleanResultDetail.setText(getIntent().getStringExtra("garbageSize") + " " + getString(R.string.junkcleaned));
                    else
                        mCleanResultDetail.setText( "0KB " + getString(R.string.junkcleaned));
                    setCleanSize(this,getIntent().getLongExtra("garbageSizeOfByte",0l));
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
            });mCleanedAll.startAnimation(translateAnimation);
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

    public static long getCleanSize(Context context)
    {
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getLong("cleansize",0l);
    }

    public static void setCleanSize(Context context, long cleanSize)
    {
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putLong("cleansize",cleanSize).commit();
    }
}