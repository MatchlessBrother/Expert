package com.expert.cleanup.acts.base;

import android.os.Bundle;
import android.view.Window;
import android.view.Gravity;
import android.content.Intent;
import java.text.DecimalFormat;
import android.os.BatteryManager;
import com.expert.cleanup.BaseApp;
import android.view.WindowManager;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import androidx.annotation.Nullable;

import com.expert.cleanup.acts.BatteringActivity;
import com.expert.cleanup.acts.BatteryActivity;
import com.expert.cleanup.acts.BoostActivity;
import com.expert.cleanup.acts.BoostingActivity;
import com.expert.cleanup.acts.CleanActivity;
import com.expert.cleanup.acts.CleaningActivity;
import com.expert.cleanup.acts.CpuActivity;
import com.expert.cleanup.acts.CpuingActivity;
import com.expert.cleanup.acts.MainActivity;
import com.expert.cleanup.nativetos.HardwareTool;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import static com.expert.cleanup.acts.BoostActivity.getNextBoostTime;
import static com.expert.cleanup.acts.CleanActivity.getNextCleanTime;
import static com.expert.cleanup.acts.CpuActivity.getNextCoolDownTime;
import static com.expert.cleanup.acts.BatteryActivity.getNextHibernateTime;

public class NotifyActivity extends AppCompatActivity
{
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Window window = getWindow();/****************/
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams params = window.getAttributes();
        params.format = PixelFormat.TRANSPARENT;params.x = 0;params.y = 0;params.width = 1;params.height = 1;
        params.windowAnimations = android.R.style.Animation_Translucent;window.setAttributes(params);processDetail();
    }

    public void processDetail()
    {
        try
        {
            Intent intent = getIntent();
            switch(intent.getStringExtra("mode"))
            {
                case "cpu":
                {
                    BaseApp.finishAllActivity();
                    if(getNextCoolDownTime(this) > System.currentTimeMillis())
                    {
                        Intent cpuIntent = new Intent(this, CpuingActivity.class);
                        cpuIntent.putExtra("temperature",new DecimalFormat("#.0").format(
                        HardwareTool.getCpuTemperatureFinder(this)) + "℃" +
                        "/" +  (new DecimalFormat("#.0").format(32+ HardwareTool.
                        getCpuTemperatureFinder(this) * 1.8))+ "℉");
                        cpuIntent.putExtra("justcpucooler",false);
                        cpuIntent.putExtra("iscooler",true);
                        cpuIntent.putExtra("appNumbers",0);
                        startActivities(new Intent[]{new Intent(this, MainActivity.class),cpuIntent});
                    }
                    else
                    {
                        startActivities(new Intent[]{new Intent(this,MainActivity.class),new Intent(this, CpuActivity.class)});
                    }
                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle bundle = new Bundle();/*************************************/
                    mFirebaseAnalytics.logEvent("notify_cpu_click",bundle);
                    break;
                }
                case "boost":
                {
                    BaseApp.finishAllActivity();
                    if(getNextBoostTime(this) > System.currentTimeMillis())
                    {
                        Intent boostIntent = new Intent(this, BoostingActivity.class);
                        boostIntent.putExtra("justboost",false);
                        boostIntent.putExtra("appNumbers",0);
                        boostIntent.putExtra("isboost",true);
                        startActivities(new Intent[]{new Intent(this,MainActivity.class),boostIntent});
                    }
                    else
                    {
                        startActivities(new Intent[]{new Intent(this,MainActivity.class),new Intent(this, BoostActivity.class)});
                    }
                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle bundle = new Bundle();/*************************************/
                    mFirebaseAnalytics.logEvent("notify_boost_click",bundle);
                    break;
                }
                case "clean":
                {
                    BaseApp.finishAllActivity();
                    if(getNextCleanTime(this) > System.currentTimeMillis())
                    {
                        Intent cleanIntent = new Intent(this, CleaningActivity.class);
                        cleanIntent.putExtra("justcleanedup",false);
                        cleanIntent.putExtra("iscleaned",true);
                        cleanIntent.putExtra("garbageSize","");
                        cleanIntent.putExtra("appNumbers",0);
                        startActivities(new Intent[]{new Intent(this,MainActivity.class),cleanIntent});
                    }
                    else
                    {
                        startActivities(new Intent[]{new Intent(this,MainActivity.class),new Intent(this, CleanActivity.class)});
                    }
                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle bundle = new Bundle();/*************************************/
                    mFirebaseAnalytics.logEvent("notify_clean_click",bundle);
                    break;
                }
                case "battery":
                {
                    BaseApp.finishAllActivity();
                    if(getNextHibernateTime(this) > System.currentTimeMillis())
                    {
                        Intent batteryIntent = new Intent(this, BatteringActivity.class);
                        batteryIntent.putExtra("battery",this.registerReceiver(null,new
                        IntentFilter(Intent.ACTION_BATTERY_CHANGED)).getIntExtra(BatteryManager.EXTRA_LEVEL, 0) + "");
                        batteryIntent.putExtra("justpowersaving",false);
                        batteryIntent.putExtra("iselectricitied",true);
                        batteryIntent.putExtra("appNumbers",0);
                        startActivities(new Intent[]{new Intent(this,MainActivity.class),batteryIntent});
                    }
                    else
                    {
                        startActivities(new Intent[]{new Intent(this,MainActivity.class),new Intent(this, BatteryActivity.class)});
                    }
                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle bundle = new Bundle();/*************************************/
                    mFirebaseAnalytics.logEvent("notify_battery_click",bundle);
                    break;
                }
                case "flashlight":
                {
                    BaseApp.finishAllActivity();
                    startActivities(new Intent[]{new Intent(this,MainActivity.class),new Intent(this,FlashLightActivity.class)});
                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle bundle = new Bundle();/*************************************/
                    mFirebaseAnalytics.logEvent("notify_flashlight_click",bundle);
                    break;
                }
                case "launcher":
                {
                    BaseApp.finishAllActivity();
                    startActivity(new Intent(this,MainActivity.class));
                    FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                    Bundle bundle = new Bundle();/***************************************************************/
                    mFirebaseAnalytics.logEvent("notify_main_click",bundle);/************************************/
                }
            }
        }
        catch(Exception exception)
        {
            exception.printStackTrace();
        }
    }

    protected void onPause()
    {
        super.onPause();
        finish();
    }
}