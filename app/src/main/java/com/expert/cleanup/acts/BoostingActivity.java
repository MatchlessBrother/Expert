package com.expert.cleanup.acts;

import android.os.Bundle;
import android.view.View;
import com.expert.cleanup.R;
import android.content.Intent;
import android.content.Context;
import java.text.DecimalFormat;
import android.widget.TextView;
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
import com.expert.cleanup.nativetos.base.UnityTool;
import android.view.animation.DecelerateInterpolator;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expert.cleanup.nativetos.base.WhiteListTool;
import com.expert.cleanup.acts.base.DeviceInfoActivity;
import static com.expert.cleanup.acts.CleanActivity.getNextCleanTime;
import static com.expert.cleanup.acts.CpuActivity.getNextCoolDownTime;
import static com.expert.cleanup.acts.BatteryActivity.getNextHibernateTime;

public class BoostingActivity extends AppCompatActivity
{
    private View mStatebar;
    private boolean isLoadedAds;
    private ImageView mBoostingMenu;
    private LinearLayout mBoostedAll;
    private ImageView mBoostedSnowflake1;
    private ImageView mBoostedSnowflake2;
    private TextView mBoostedResultDetail;
    private ImageView mBoostedThermometer;
    private TextView mBoostingTemperature;
    private ImageView mBoostingOutWindmill;
    private ImageView mBoostingInWindmill;
    private LinearLayout mBoostingWindmillAll;
    private TextView mBoostingTemperature_note;

    private ImageView mBoostedNote;
    private int mTriedNumsForShow;
    private Disposable mDisposable;
    private ScaleAnimation mRedPointSa;
    private LinearLayout mBoostedContent;
    private boolean mBackEnable = false;
    private TextView mBoostedRedCleanAll;
    private TextView mBoostedRedCpuAll;
    private RelativeLayout mBoostedCpuAll;
    private TextView mBoostedRedBatteryAll;
    private RelativeLayout mBoostedCleanAll;
    private RelativeLayout mBoostedInfoAll;
    private RelativeLayout mBoostedBatteryAll;

    private Disposable mDisposing;
    private int mTriedNumsForShowing;
    private LottieAnimationView mBoostingLottieview;

    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boosting);
        mStatebar = findViewById(R.id.statebar);
        mBoostedAll = findViewById(R.id.boosted_all);
        mBoostedNote = findViewById(R.id.boosted_note);
        mBoostingMenu = findViewById(R.id.boosting_menu);
        mBoostedSnowflake1 = findViewById(R.id.boosted_snowflake1);
        mBoostedSnowflake2 = findViewById(R.id.boosted_snowflake2);
        ImmersionBar.with(this).statusBarView(mStatebar).init();
        mBoostedThermometer = findViewById(R.id.boosted_thermometer);
        mBoostedResultDetail = findViewById(R.id.boosted_result_detail);
        mBoostingLottieview = findViewById(R.id.boosting_lottieview);
        mBoostingInWindmill = findViewById(R.id.boosting_in_windmill);
        mBoostingTemperature = findViewById(R.id.boosting_temperature);
        mBoostingWindmillAll = findViewById(R.id.boosting_windmill_all);
        mBoostingOutWindmill = findViewById(R.id.boosting_out_windmill);
        mBoostingTemperature_note = findViewById(R.id.boosting_temperature_note);
        mBoostingTemperature.setText(getIntent().getStringExtra("temperature"));
        /********************************************************************/
        mBoostedCpuAll = findViewById(R.id.boosted_cpu_all);
        mBoostedContent = findViewById(R.id.boosted_content);
        mBoostedInfoAll = findViewById(R.id.boosted_info_all);
        mBoostedCleanAll = findViewById(R.id.boosted_clean_all);
        mBoostedBatteryAll = findViewById(R.id.boosted_battery_all);
        mBoostedRedCpuAll = findViewById(R.id.boosted_red_cpu_all);
        mBoostedRedCleanAll = findViewById(R.id.boosted_red_clean_all);
        mBoostedRedBatteryAll = findViewById(R.id.boosted_red_battery_all);
        /********************************************************************/
        if(getIntent().getBooleanExtra("justboost",true))
        {
            if(null != mBoostingWindmillAll && null != mBoostingOutWindmill && null != mBoostedAll)
            {
                mBoostingWindmillAll.setVisibility(View.GONE);
                mBoostedAll.setVisibility(View.VISIBLE);
                mBoostingOutWindmill.clearAnimation();
                mBoostingOutWindmill.invalidate();
            }
            boosted();
        }
        else
        {
            mDisposing = null;
            mTriedNumsForShowing = 0;
            mBoostingLottieview.playAnimation();
        }

        mBoostingLottieview.addAnimatorListener(new AnimatorListenerAdapter()
        {
            public void onAnimationEnd(Animator animation)
            {
                super.onAnimationEnd(animation);
                if(null != mBoostedAll && null != mBoostingWindmillAll)
                {
                    if(null != mDisposing && !mDisposing.isDisposed())
                        mDisposing.dispose();
                    mBoostingWindmillAll.setVisibility(View.GONE);
                    mBoostingLottieview.setVisibility(View.GONE);
                    mBoostedAll.setVisibility(View.VISIBLE);
                    isLoadedAds = false;
                    boosted();
                }
            }
        });

        mBoostedCleanAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(mBackEnable)
                {
                    if(getNextCleanTime(BoostingActivity.this) > System.currentTimeMillis())
                    {
                        Intent intent = new Intent(BoostingActivity.this,CleaningActivity.class);
                        intent.putExtra("justcleanedup",false);
                        intent.putExtra("iscleaned",true);
                        intent.putExtra("garbageSize","");
                        intent.putExtra("appNumbers",0);
                        startActivity(intent);
                        finish();
                    }

                    else
                    {
                        startActivity(new Intent(BoostingActivity.this,CleanActivity.class));
                        finish();
                    }
                }
            }
        });

        mBoostedBatteryAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(mBackEnable)
                {
                    if(getNextHibernateTime(BoostingActivity.this) > System.currentTimeMillis())
                    {
                        Intent intent = new Intent(BoostingActivity.this,BatteringActivity.class);
                        intent.putExtra("battery",BoostingActivity.this.registerReceiver(null,new
                        IntentFilter(Intent.ACTION_BATTERY_CHANGED)).getIntExtra(BatteryManager.EXTRA_LEVEL, 0) + "");
                        intent.putExtra("justpowersaving",false);
                        intent.putExtra("iselectricitied",true);
                        intent.putExtra("appNumbers",0);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(BoostingActivity.this,BatteryActivity.class));
                        finish();
                    }
                }
            }
        });

        mBoostedCpuAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(mBackEnable)
                {
                    if(getNextCoolDownTime(BoostingActivity.this) > System.currentTimeMillis())
                    {
                        Intent intent = new Intent(BoostingActivity.this, CpuingActivity.class);
                        intent.putExtra("temperature",new DecimalFormat("#.0").format(
                        HardwareTool.getCpuTemperatureFinder(BoostingActivity.this)) + "℃" +
                        "/" +  (new DecimalFormat("#.0").format(32+ HardwareTool.
                        getCpuTemperatureFinder(BoostingActivity.this) * 1.8))+ "℉");
                        intent.putExtra("justcpucooler",false);
                        intent.putExtra("iscooler",true);
                        intent.putExtra("appNumbers",0);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(BoostingActivity.this,CpuActivity.class));
                        finish();
                    }
                }
            }
        });

        mBoostedInfoAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(mBackEnable)
                {
                    startActivity(new Intent(BoostingActivity.this,DeviceInfoActivity.class));
                    finish();
                }
            }
        });

        mBoostingMenu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(mBackEnable)
                    finish();
            }
        });

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getApplicationContext());
        Bundle bundle = new Bundle();/*************************************/
        mFirebaseAnalytics.logEvent("boostingactivity1_show",bundle);
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
        if(null != mBoostedRedCleanAll.getAnimation())mBoostedRedCleanAll.clearAnimation();
        if(getNextCleanTime(this) > System.currentTimeMillis())
        {
            mBoostedRedCleanAll.setVisibility(View.INVISIBLE);
        }
        else
        {
            mBoostedRedCleanAll.setVisibility(View.VISIBLE);
            if(CleanActivity.getLatestApp(this).size() > 0)
            {
                mBoostedRedCleanAll.startAnimation(mRedPointSa);
                mBoostedRedCleanAll.setText(/*CleanActivity.getLatestApp(this).size() +*/ "");
            }
            else
            {
                if(CleanActivity.getCleanApps(this) != 0)
                {
                    mBoostedRedCleanAll.startAnimation(mRedPointSa);
                    mBoostedRedCleanAll.setText(/*CleanActivity.getCleanApps(this) +*/ "");
                }
                else
                {
                    int cleanApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(this)) % 7 + 6);
                    if(cleanApps > WhiteListTool.getWhiteListSize(this))
                        cleanApps = WhiteListTool.getWhiteListSize(this);
                    CleanActivity.setCleanApps(this,cleanApps);
                    if(0 != cleanApps)
                    {
                        mBoostedRedCleanAll.startAnimation(mRedPointSa);
                        mBoostedRedCleanAll.setText(/*cleanApps +*/ "");
                    }
                    else
                        mBoostedRedCleanAll.setVisibility(View.INVISIBLE);
                }
            }
        }
        /******************************************************************/
        if(null != mBoostedRedBatteryAll.getAnimation())mBoostedRedBatteryAll.clearAnimation();
        if(getNextHibernateTime(this) > System.currentTimeMillis())
        {
            mBoostedRedBatteryAll.setVisibility(View.INVISIBLE);
        }
        else
        {
            mBoostedRedBatteryAll.setVisibility(View.VISIBLE);
            if(BatteryActivity.getLatestApp(this).size() > 0)
            {
                mBoostedRedBatteryAll.startAnimation(mRedPointSa);
                mBoostedRedBatteryAll.setText(/*BatteryActivity.getLatestApp(this).size() +*/ "");
            }
            else
            {
                if(BatteryActivity.getBatteryApps(this) != 0)
                {
                    mBoostedRedBatteryAll.startAnimation(mRedPointSa);
                    mBoostedRedBatteryAll.setText(/*BatteryActivity.getBatteryApps(this) +*/ "");
                }
                else
                {
                    int batteryApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(this)) % 7 + 6);
                    if(batteryApps > WhiteListTool.getWhiteListSize(this))
                        batteryApps = WhiteListTool.getWhiteListSize(this);
                    BatteryActivity.setBatteryApps(this,batteryApps);
                    if(0 != batteryApps)
                    {
                        mBoostedRedBatteryAll.startAnimation(mRedPointSa);
                        mBoostedRedBatteryAll.setText(/*batteryApps +*/ "");
                    }
                    else
                        mBoostedRedBatteryAll.setVisibility(View.INVISIBLE);
                }
            }
        }
        /******************************************************************/
        if(null != mBoostedRedCpuAll.getAnimation())mBoostedRedCpuAll.clearAnimation();
        if(getNextCoolDownTime(this) > System.currentTimeMillis())
        {
            mBoostedRedCpuAll.setVisibility(View.INVISIBLE);
        }
        else
        {
            mBoostedRedCpuAll.setVisibility(View.VISIBLE);
            if(CpuActivity.getLatestApp(this).size() > 0)
            {
                mBoostedRedCpuAll.startAnimation(mRedPointSa);
                mBoostedRedCpuAll.setText(/*CpuActivity.getLatestApp(this).size() +*/ "");
            }
            else
            {
                if(CpuActivity.getCoolerCpuApps(this) != 0)
                {
                    mBoostedRedCpuAll.startAnimation(mRedPointSa);
                    mBoostedRedCpuAll.setText(/*CpuActivity.getCoolerCpuApps(this) +*/ "");
                }
                else
                {
                    int cpuApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(this)) % 7 + 6);
                    if(cpuApps > WhiteListTool.getWhiteListSize(this))
                        cpuApps = WhiteListTool.getWhiteListSize(this);
                    CpuActivity.setCoolerCpuApps(this,cpuApps);
                    if(0 != cpuApps)
                    {
                        mBoostedRedCpuAll.startAnimation(mRedPointSa);
                        mBoostedRedCpuAll.setText(/*cpuApps +*/"");
                    }
                    else
                        mBoostedRedCpuAll.setVisibility(View.INVISIBLE);
                }
            }
        }
        NotifyService.startUpdateNotify(getApplicationContext());
    }

    protected void onPause()
    {
        super.onPause();
        if(null != mBoostedRedBatteryAll.getAnimation())mBoostedRedBatteryAll.clearAnimation();
        if(null != mBoostedRedCleanAll.getAnimation())mBoostedRedCleanAll.clearAnimation();
        if(null != mBoostedRedCpuAll.getAnimation())mBoostedRedCpuAll.clearAnimation();
    }

    private void boosting()
    {
        if(null != mBoostingOutWindmill)
        {
            RotateAnimation bigRotateAnimation = new RotateAnimation(4320,0,
            Animation.RELATIVE_TO_PARENT,0.5f,Animation.RELATIVE_TO_PARENT,0.5f);
            LinearInterpolator linearInterpolator = new LinearInterpolator();
            bigRotateAnimation.setDuration(4000l);
            bigRotateAnimation.setRepeatMode(Animation.RESTART);
            bigRotateAnimation.setRepeatCount(Animation.INFINITE);
            bigRotateAnimation.setInterpolator(linearInterpolator);
            mBoostingOutWindmill.startAnimation(bigRotateAnimation);
        }
    }

    private void boosted()
    {
        ((BaseApp)getApplication()).startGgInterstitial();
        if(null != mBoostedAll && null != mBoostedResultDetail)
        {
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getApplicationContext());
            Bundle bundle = new Bundle();/*************************************/
            mFirebaseAnalytics.logEvent("boostingactivity2_show",bundle);
            mFirebaseAnalytics.logEvent("resultaction_show",bundle);
            if(getIntent().getBooleanExtra("justboost",true))
                mBoostedResultDetail.setText(getString(R.string.boostnote));
            else
            {
                if(getIntent().getBooleanExtra("isboost",false))
                {
                    mBoostedResultDetail.setText(getString(R.string.boostnote));
                }
                else
                {
                    int boostSize = 0;
                    int appNum = getIntent().getIntExtra("appNumbers",0);
                    for(int index = 0;index < appNum;index++)/**********/
                        boostSize += (12 + Math.random() * 55 );/*******/
                    setBoostSize(this,boostSize * 1024l * 1024l + (int)(Math.random() * 1024));
                    mBoostedResultDetail.setText(UnityTool.sizeFormatInt(getBoostSize(this)) + " " + getString(R.string.boostdetail));
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
            });mBoostedAll.startAnimation(translateAnimation);
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

    public static long getBoostSize(Context context)
    {
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getLong("boostsize",0l);
    }

    public static void setBoostSize(Context context, long boostSize)
    {
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putLong("boostsize",boostSize).commit();
    }
}