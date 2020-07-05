package com.expert.cleanup;

import java.util.Map;
import java.util.List;
import java.util.Stack;
import java.util.Timer;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.os.Bundle;
import android.view.View;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.ArrayList;
import com.facebook.ads.Ad;
import android.view.Window;
import android.app.Activity;
import android.view.Gravity;
import android.content.Intent;
import android.graphics.Color;
import android.app.Application;
import android.content.Context;
import io.reactivex.Observable;
import android.net.NetworkInfo;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.widget.TextView;
import com.facebook.ads.AdError;
import android.provider.Settings;
import android.app.PendingIntent;
import android.view.WindowManager;
import androidx.multidex.MultiDex;
import com.appsflyer.AppsFlyerLib;
import android.util.DisplayMetrics;
import android.app.ActivityManager;
import com.facebook.ads.AdSettings;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import androidx.annotation.Nullable;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.graphics.BitmapFactory;
import com.umeng.commonsdk.UMConfigure;
import io.reactivex.functions.Consumer;
import android.net.ConnectivityManager;
import android.content.pm.ShortcutInfo;
import android.content.SharedPreferences;
import com.umeng.analytics.MobclickAgent;
import androidx.work.PeriodicWorkRequest;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import io.reactivex.schedulers.Schedulers;
import android.content.pm.ShortcutManager;
import com.nispok.snackbar.SnackbarManager;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdActivity;
import com.expert.cleanup.acts.CpuActivity;
import com.expert.cleanup.acts.SpaActivity;
import com.facebook.ads.InterstitialAdListener;
import com.expert.cleanup.acts.MainActivity;
import com.expert.cleanup.sers.NotifyService;
import com.expert.cleanup.acts.BoostActivity;
import com.expert.cleanup.acts.CleanActivity;
import com.facebook.ads.AudienceNetworkActivity;
import com.expert.cleanup.sers.AutoStartWorker;
import com.expert.cleanup.acts.SplashActivity;
import com.google.android.gms.ads.InterstitialAd;
import com.appsflyer.AppsFlyerConversionListener;
import com.expert.cleanup.acts.BatteryActivity;
import com.expert.cleanup.acts.base.NoteActivity;
import com.expert.cleanup.acts.base.PointActivity;
import com.expert.cleanup.nets.util.BaseDialog;
import com.expert.cleanup.receivers.ScreenStateReceive;
import com.expert.cleanup.nets.config.AdConfig;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.expert.cleanup.nets.SwitchAppReceiver;
import com.nispok.snackbar.listeners.ActionClickListener;
import io.reactivex.android.schedulers.AndroidSchedulers;
import com.expert.cleanup.acts.base.FlashLightActivity;
import com.expert.cleanup.acts.base.DeviceInfoActivity;
import com.android.installreferrer.api.InstallReferrerClient;
import com.expert.cleanup.nets.config.AdConfigProvider;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import static com.facebook.ads.AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CALLBACK_MODE;

public class BaseApp extends Application
{
    public boolean isNormalJump;
    private static BaseApp mInstance;
    private static AdConfig mAdConfig;
    private Activity mCurrentActivity;
    private FirebaseAnalytics mFirebaseAnalytics;
    private static Stack<Activity> mActivityStack;
    private UncaughtExceptionHandlerForApp handler;
    private static final String AF_DEV_KEY = "WHjbt52x8UZMNPWHdnCk6c";

    //放开resume控制
    //放开开平，差点，拔电控制
    //继承googleservice插件即可
    //需要fb，gg，umeng的版位编号

    public void onCreate()
    {
        super.onCreate();
        mInstance = this;
        setupShortcuts();
        initGgInterstitialConfigs();
        initFbInterstitialConfigs();
        mActivityStack = new Stack<>();
        MultiDex.install(this);
        setNeedGetSystemAlertDialogPermission(true);
        AudienceNetworkAds.initialize(this);
        mAdConfig = AdConfigProvider.getLocalAdConfig(this);
        registerAssistOfExtraAd(this);/********************/
        AdSettings.setIntegrationErrorMode(INTEGRATION_ERROR_CALLBACK_MODE);
        uploadInstallReferrence();/****************************************/
        UMConfigure.setLogEnabled(true);/**********************************/
        UMConfigure.init(this,"qwodjqowhjediqhwe81223","GoogleStore",UMConfigure.DEVICE_TYPE_PHONE,null);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getApplicationContext());
        AutoStartWorker.startWorkerByPeriodic(PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS);
        if(ScreenStateReceive.getNextShowTime(this) == 0l)
            ScreenStateReceive.setNextShowTime(this);
        if(ScreenStateReceive.getNotifyNextShowTime(this) == 0l)
            ScreenStateReceive.setNotifyNextShowTime(this);
        MobileAds.initialize(this, new OnInitializationCompleteListener()
        {
            public void onInitializationComplete(InitializationStatus initializationStatus)
            {

            }
        });

        AppsFlyerConversionListener conversionDataListener = new AppsFlyerConversionListener()
        {
            public void onInstallConversionDataLoaded(Map<String, String> map)
            {
                for(Map.Entry<String, String> entry : map.entrySet())
                {
                    Log.i("Flyer",entry.getKey() + ":" +  entry.getValue());
                }
            }

            public void onInstallConversionFailure(String s)
            {

            }

            public void onAppOpenAttribution(Map<String, String> map)
            {

            }

            public void onAttributionFailure(String s)
            {

            }
        };

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks()
        {
            public void onActivityStarted(@NonNull Activity activity)
            {
                mActivePageNum++;
                if(activity.getClass().getName().toLowerCase().trim().contains(AudienceNetworkActivity.class.getSimpleName().
                toLowerCase().trim()) && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && null != mAdConfig && mAdConfig.isPlayExtraAd())
                {
                    String label = " ";
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launchern);
                    activity.setTaskDescription(new ActivityManager.TaskDescription(label,bitmap));/***/
                }
                else
                {
                    /******切记这里一定要把主App的启动图标和名称复制过来****/
                    /*******以便普通情况下App能正常显示在RecentList列表*****/
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    {
                        String label = getString(R.string.app_name);
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher);
                        activity.setTaskDescription(new ActivityManager.TaskDescription(label,bitmap));/***/
                    }
                }
            }

            public void onActivityResumed(@NonNull Activity activity)
            {
                isNormalJump = false;
                mCurrentActivity = activity;
                MobclickAgent.onResume(activity);
                /*if(activity instanceof AdActivity)
                {

                }
                else if(activity instanceof AudienceNetworkActivity)
                {

                }
                else
                {
                    if(mActivePageNum == 1 && activity instanceof SplashActivity)
                    {
                        if(!mIsStartCacheFbInterstitial && !isCachedFbInterstitial())
                        {
                            Log.i("cp_ad", "开始缓存Fb插屏广告");
                            startCacheFbInterstitial();
                        }
                        if(!mIsStartCacheGgInterstitial && !isCachedGgInterstitial())
                        {
                            Log.i("cp_ad", "开始缓存Gg插屏广告");
                            startCacheGgInterstitial();
                        }
                    }
                    else if(mActivePageNum == 2 && activity instanceof MainActivity && mJustSuspendedActivity instanceof SplashActivity)
                    {
                        if(isCachedFbInterstitial())
                        {
                            Log.i("cp_ad", "显示缓存Fb插屏广告");
                            showFbInterstitial();
                        }
                        else
                        {
                            mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
                            Bundle bundle = new Bundle();*//*******************************************//*
                            mFirebaseAnalytics.logEvent("fb_not_show",bundle);*//*******************//*
                            *//*************************************************************************//*
                            if(isCachedGgInterstitial())
                            {
                                Log.i("cp_ad", "显示缓存Gg插屏广告");
                                showGgInterstitial();
                            }
                            else
                            {
                                mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
                                Bundle bundle2 = new Bundle();*//*******************************************//*
                                mFirebaseAnalytics.logEvent("gg_not_show",bundle2);*//*******************//*
                                *//*if(!mIsStartCacheGgInterstitial)
                                {
                                    Log.i("cp_ad", "开始缓存Gg插屏广告");
                                    startCacheGgInterstitial();
                                }*//*
                            }
                        }
                    }
                    else if(activity instanceof NoteActivity || activity instanceof FlashLightActivity || activity instanceof DeviceInfoActivity)
                    {
                        if(isCachedGgInterstitial())
                        {
                            Log.i("cp_ad", "显示缓存Gg插屏广告");
                            showGgInterstitial();
                        }
                        else
                        {
                            mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
                            Bundle bundle2 = new Bundle();*//*******************************************//*
                            mFirebaseAnalytics.logEvent("gg_not_show",bundle2);*//*******************//*
                            if(!mIsStartCacheGgInterstitial)
                            {
                                Log.i("cp_ad", "开始缓存Gg插屏广告");
                                startCacheGgInterstitial();
                            }
                        }
                    }
                    else if(activity instanceof CleanActivity || activity instanceof CpuActivity || activity instanceof BoostActivity || activity instanceof BatteryActivity)
                    {
                        if(!mIsStartCacheGgInterstitial && !isCachedGgInterstitial())
                        {
                            Log.i("cp_ad", "开始缓存Gg插屏广告");
                            startCacheGgInterstitial();
                        }
                    }
                }*/
            }

            public void onActivityPaused(@NonNull Activity activity)
            {
                MobclickAgent.onPause(activity);
                mJustSuspendedActivity = activity;
                if((activity.getClass().getSimpleName().equals(AudienceNetworkActivity.class.getSimpleName())))activity.onBackPressed();
            }

            public void onActivityStopped(@NonNull Activity activity)
            {
                mActivePageNum--;
                if(mActivePageNum == 0 && !isNormalJump)
                {
                    finishAllActivityExcept(AudienceNetworkActivity.class.getSimpleName());
                    setNeedGetSystemAlertDialogPermission(true);
                }
                if(activity instanceof AdActivity)
                {
                    if(!activity.isFinishing())
                        activity.finish();
                    mIsCachedGgAd = false;
                   /*Observable.timer(2000,TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>()
                    {
                        public void accept(Long aLong) throws Exception
                        {
                            Log.i("cp_ad", "开始缓存Gg插屏广告");
                            startCacheGgInterstitial();
                        }
                    });*/
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
                    Bundle bundle = new Bundle();/*******************************************/
                    mFirebaseAnalytics.logEvent("all_close_gginterstitial",bundle);
                }
                else if(activity instanceof AudienceNetworkActivity)
                {
                    /*if(!activity.isFinishing())
                        activity.finish();
                    Observable.timer(2000,TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>()
                    {
                        public void accept(Long aLong) throws Exception
                        {
                            Log.i("cp_ad", "开始缓存Fb插屏广告");
                            startCacheFbInterstitial();
                        }
                    });*/
                    if(hasActivity(PointActivity.class.getSimpleName()))
                        finishActivity(PointActivity.class.getSimpleName());
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
                    Bundle bundle = new Bundle();/*******************************************/
                    mFirebaseAnalytics.logEvent("all_close_fbinterstitial",bundle);
                }
            }

            public void onActivityDestroyed(@NonNull Activity activity)
            {
                removeActivity(activity.getClass().getSimpleName());
            }

            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle)
            {
                addActivity(activity);
            }

            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle)
            {

            }
        });

        AppsFlyerLib.getInstance().init(AF_DEV_KEY,conversionDataListener,getApplicationContext());
        AppsFlyerLib.getInstance().startTracking(this);
        new Timer().schedule(new TimerTask()
        {
            public void run()
            {
                if(System.currentTimeMillis() >= getNextUpdateTime(BaseApp.this))
                {
                    setNextUpdateTime(BaseApp.this);
                    CleanActivity.setCleanApps(getApplicationContext(),0);
                    CleanActivity.setLatestApp(getApplicationContext(),new ArrayList<>());
                    getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).edit().putLong("cleantime",0).commit();
                    /*****************************************************************************************************/
                    BoostActivity.setBoostApps(getApplicationContext(), 0);
                    BoostActivity.setLatestApp(getApplicationContext(),new ArrayList<>());
                    getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).edit().putLong("boosttime",0).commit();
                    /*****************************************************************************************************/
                    BatteryActivity.setBatteryApps(getApplicationContext(), 0);
                    BatteryActivity.setLatestApp(getApplicationContext(),new ArrayList<>());
                    getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).edit().putLong("hibernatetime",0).commit();
                    /*****************************************************************************************************/
                    CpuActivity.setCoolerCpuApps(getApplicationContext(),0);
                    CpuActivity.setLatestApp(getApplicationContext(),new ArrayList<>());
                    getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).edit().putLong("cooldowntime",0).commit();
                    NotifyService.startUpdateNotify(getApplicationContext());/***************************/
                }
            }
        }
        ,0l,60 * 1000l);
        handler = UncaughtExceptionHandlerForApp.getInstance();
        handler.registerUncaughtExceptionHandler(this);
    }

    public void startGgInterstitial()
    {
        if(isCachedGgInterstitial())
        {
            Log.i("cp_ad", "显示缓存Gg插屏广告");
            showGgInterstitial();
        }
        else
        {
            /*if(!mIsStartCacheGgInterstitial)
            {
                Log.i("cp_ad", "开始缓存Gg插屏广告");
                startCacheGgInterstitial();
            }*/
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
            Bundle bundle2 = new Bundle();/*******************************************/
            mFirebaseAnalytics.logEvent("gg_not_show",bundle2);/*******************/
        }
    }

    public static BaseApp getInstance()
    {
        return mInstance;

    }

    public void uploadInstallReferrence()
    {
        final InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener()
        {
            public void onInstallReferrerSetupFinished(int responseCode)
            {
                try
                {
                    if(responseCode == InstallReferrerClient.InstallReferrerResponse.OK)
                    {
                       /* ReferrerDetails details = referrerClient.getInstallReferrer();
                        details.getReferrerClickTimestampSeconds();
                        details.getInstallBeginTimestampSeconds();
                        details.getInstallReferrer();
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("n",getPackageName());*//***//*
                        jsonObject.put("c", details.getInstallReferrer());
                        jsonObject.put("a", PhoneHelper.getAndroidId(BaseApp.this));
                        jsonObject.put("e", details.getInstallBeginTimestampSeconds());
                        jsonObject.put("d", details.getReferrerClickTimestampSeconds());
                        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), AscEncryptHelper.encrypt(jsonObject.toString(),secretKey));
                        Call<ResponseBody> call = NetClient.getInstance(getApplicationContext()).getNetAllUrl().uploadInstallationData_Restore(body);*//*****************************//*
                        call.enqueue(new Callback<ResponseBody>()
                        {
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                            {

                            }

                            public void onFailure(Call<ResponseBody> call, Throwable t)
                            {

                            }
                        });*/
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

            public void onInstallReferrerServiceDisconnected()
            {

            }
        });
    }

    public AdConfig getAdConfig()
    {
        return mAdConfig;

    }

    public void setAdConfig(AdConfig adConfig)
    {
        mAdConfig = adConfig;

    }

    /***********移除所有Activity**********/
    public static void removeAllActivity()
    {
        if(mActivityStack == null)return;
        mActivityStack.clear();
        System.gc();
    }

    /***********关闭所有Activity**********/
    public static void finishAllActivity()
    {
        if(mActivityStack == null)
            return;

        Iterator<Activity> iterator = mActivityStack.iterator();
        while(iterator.hasNext())
        {
            Activity activity = iterator.next();
            if(null != activity)
            {
                iterator.remove();
                activity.finish();
            }
        }
    }

    /***********获取最近使用的Activity********/
    public static Activity getCurrentActivity()
    {
        if(mActivityStack == null)
            return null;

        if(mActivityStack.size() != 0)
        {
            Activity activity = mActivityStack.get(mActivityStack.size() - 1);
            return activity;
        }
        return null;
    }

    /***记录所有开启的Activity从而实现程序的完全退出***/
    public static void addActivity(Activity activity)
    {
        if(mActivityStack == null)mActivityStack = new Stack<Activity>();
        if(!hasActivity(activity.getClass().getSimpleName()))
            mActivityStack.add(activity);
    }

    /*********记录Activity的栈，判断是否包含此Activity*********/
    public static boolean hasActivity(String activitySimpleName)
    {
        for(Activity activity : mActivityStack)
        {
            if(activity != null)
            {
                if(activity.getClass().getSimpleName().equals(activitySimpleName))
                    return true;
            }
        }
        return false;
    }

    /**************根据ActivityName移除此Activity**************/
    public static void removeActivity(String activitySimpleName)
    {
        if(mActivityStack == null)
            return;

        Iterator<Activity> iterator = mActivityStack.iterator();
        while(iterator.hasNext())
        {
            Activity activity = iterator.next();
            if(null != activity && activity.getClass().getSimpleName().equals(activitySimpleName))
            {
                iterator.remove();
            }
        }
    }

    /**************根据ActivityName关闭此Activity**************/
    public static void finishActivity(String activitySimpleName)
    {
        if(mActivityStack == null)
            return;

        Iterator<Activity> iterator = mActivityStack.iterator();
        while(iterator.hasNext())
        {
            Activity activity = iterator.next();
            if(null != activity && activity.getClass().getSimpleName().equals(activitySimpleName))
            {
                iterator.remove();
                if(!activity.isFinishing())activity.finish();
            }
        }
    }

    /********根据ActivityName移除除该Activity外的其他所有Activity*******/
    public static void removeAllActivityExcept(String activitySimpleName)
    {
        if(mActivityStack == null)
            return;

        Iterator<Activity> iterator = mActivityStack.iterator();
        while(iterator.hasNext())
        {
            Activity activity = iterator.next();
            if(null != activity && (!activity.getClass().getSimpleName().equals(activitySimpleName)))
            {
                iterator.remove();
            }
        }
    }

    /********根据ActivityName关闭除该Activity外的其他所有Activity*******/
    public static void finishAllActivityExcept(String activitySimpleName)
    {
        if(mActivityStack == null)
            return;

        Iterator<Activity> iterator = mActivityStack.iterator();
        while(iterator.hasNext())
        {
            Activity activity = iterator.next();
            if(null != activity && (!activity.getClass().getSimpleName().equals(activitySimpleName)))
            {
                iterator.remove();
                activity.finish();
            }
        }
    }

    public void registerAssistOfExtraAd(Context context)
    {
        try
        {
            if(null != mAdConfig && mAdConfig.isPlayExtraAd())
            {
                IntentFilter switchAppIntentFilter = new IntentFilter();
                switchAppIntentFilter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
                context.getApplicationContext().registerReceiver(SwitchAppReceiver.getInstance(),switchAppIntentFilter);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static long getNextUpdateTime(Context context)
    {
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getLong("nextupdatetime",0l);
    }

    public static void setNextUpdateTime(Context context)
    {
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putLong("nextupdatetime",System.currentTimeMillis() + 5 * 60 * 1000l).commit();
    }

    /********************************************************************/
    /*******************************插屏广告******************************/
    /********************************************************************/
    public int mActivePageNum;
    private Activity mJustSuspendedActivity;
    private boolean isPrintInterstitialLog = true;
    /*******************************GG插屏广告***************************/
    private Timer mGgTimer;
    private TimerTask mGgTimerTask;
    private int mCurrentGgRetryCount;
    private volatile boolean mIsCachedGgAd;
    private String mInterstitialAdPlacementId;
    public AdListener mInterstitialAdListener;
    private volatile InterstitialAd mInterstitialAd;
    private volatile boolean mIsStartCacheGgInterstitial;
    public static final long mAdRetryIntervalTime = 3000l;//毫秒

    public void showGgInterstitial()
    {
        if(isCachedGgInterstitial())
        {
            mInterstitialAd.show();
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
            Bundle bundle = new Bundle();/******************************************/
            mFirebaseAnalytics.logEvent("start_show_gginterstitial",bundle);
        }
    }

    private void initGgInterstitialConfigs()
    {
        if(isPrintInterstitialLog)
        Log.i("gg","Google插屏广告初始化！");
        mInterstitialAdPlacementId = getString(R.string.gg_interstitial);
        if(null == mInterstitialAdListener)
        {
            mInterstitialAdListener = new AdListener()
            {
                public void onAdLoaded()
                {
                    super.onAdLoaded();
                    mIsCachedGgAd = true;
                    if(isPrintInterstitialLog)
                    Log.i("gg","成功请求到Google插屏广告资源！");
                    if(null != mGgTimer)mGgTimer.cancel();/**************/
                    mIsStartCacheGgInterstitial = false;/****************/
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
                    Bundle bundle = new Bundle();/********************************************/
                    mFirebaseAnalytics.logEvent("successful_cache_gginterstitial",bundle);/***/
                }

                public void onAdOpened()
                {
                    super.onAdOpened();
                    if(isPrintInterstitialLog)
                    Log.i("gg","Google插屏广告正在展示！");
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
                    Bundle bundle = new Bundle();/*******************************************/
                    mFirebaseAnalytics.logEvent("successful_show_gginterstitial",bundle);
                }

                public void onAdClosed()
                {
                    super.onAdClosed();
                    mIsCachedGgAd = false;
                    if(isPrintInterstitialLog)
                    Log.i("gg","关闭Google插屏广告页面！");
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
                    Bundle bundle = new Bundle();/*******************************************/
                    mFirebaseAnalytics.logEvent("normal_close_gginterstitial",bundle);
                }

                public void onAdClicked()
                {
                    super.onAdClicked();
                    if(isPrintInterstitialLog)
                    Log.i("gg","点击Google插屏广告页面！");
                }

                public void onAdImpression()
                {
                    super.onAdImpression();
                    if(isPrintInterstitialLog)
                    Log.i("gg","Google插屏广告即将展示！");
                }

                public void onAdLeftApplication()
                {
                    super.onAdLeftApplication();
                    if(isPrintInterstitialLog)
                    Log.i("gg","从Google插屏广告页面跳转到第三方应用！");
                }

                public void onAdFailedToLoad(int errorCode)
                {
                    super.onAdFailedToLoad(errorCode);
                    if(isPrintInterstitialLog)
                    {
                        switch(errorCode)
                        {
                            case AdRequest.ERROR_CODE_NO_FILL:Log.i("gg","请求Google插屏广告发生错误：ERROR_CODE_NO_FILL");break;
                            case AdRequest.ERROR_CODE_NETWORK_ERROR:Log.i("gg","请求Google插屏广告发生错误：ERROR_CODE_NETWORK_ERROR");break;
                            case AdRequest.ERROR_CODE_INTERNAL_ERROR:Log.i("gg","请求Google插屏广告发生错误：ERROR_CODE_INTERNAL_ERROR");break;
                            case AdRequest.ERROR_CODE_INVALID_REQUEST:Log.i("gg","请求Google插屏广告发生错误：ERROR_CODE_INVALID_REQUEST");break;
                        }
                    }
                    if(mCurrentGgRetryCount > 0)
                    {
                        mCurrentGgRetryCount--;
                        if(isPrintInterstitialLog)
                        Log.i("gg","等待"+ mAdRetryIntervalTime +"毫秒后发起Google插屏广告重试！");
                        Observable.just("Wait"+mAdRetryIntervalTime+"Millisecond!").doOnNext(new Consumer<String>()
                        {
                            public void accept(String str) throws Exception
                            {
                                Log.i("sss", "ThreadName:" + Thread.currentThread().getName());
                                Thread.sleep(mAdRetryIntervalTime);
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>()
                        {
                            public void accept(String s) throws Exception
                            {
                                if(isPrintInterstitialLog)
                                Log.i("gg","发起Google插屏广告重试请求！");
                                if(null != mGgTimer)
                                    mGgTimer.cancel();
                                mGgTimer = new Timer();
                                mIsStartCacheGgInterstitial = true;/***************************/
                                if(null != mInterstitialAd)mInterstitialAd.setAdListener(null);
                                mInterstitialAd = new InterstitialAd(getApplicationContext());
                                mInterstitialAd.setAdUnitId(mInterstitialAdPlacementId);
                                mInterstitialAd.setImmersiveMode(true);/**************/
                                mInterstitialAd.setAdListener(mInterstitialAdListener);
                                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                                mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                                Bundle bundle = new Bundle();/**********************************/
                                mFirebaseAnalytics.logEvent("start_cache_gginterstitial",bundle);
                                mGgTimerTask = new TimerTask(){public void run(){
                                    mIsStartCacheGgInterstitial = false;
                                }};
                                mGgTimer.schedule(mGgTimerTask,6000L);/*****************/
                            }
                        });
                    }
                    else
                    {
                        if(isPrintInterstitialLog)
                        Log.i("gg","Google插屏广告重试次数已用完故无法重试！");
                        if(null != mGgTimer)mGgTimer.cancel();/***********************/
                        mIsStartCacheGgInterstitial = false;/*************************/
                    }
                }
            };
        }
    }

    public void startCacheGgInterstitial()
    {
        if(null != mGgTimer)
            mGgTimer.cancel();
        mGgTimer = new Timer();
        mCurrentGgRetryCount = 0;/***************************************/
        mIsStartCacheGgInterstitial = true;/***************************/
        if(null != mInterstitialAd)mInterstitialAd.setAdListener(null);
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(mInterstitialAdPlacementId);
        mInterstitialAd.setImmersiveMode(true);/**************/
        mInterstitialAd.setAdListener(mInterstitialAdListener);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();/**********************************/
        mFirebaseAnalytics.logEvent("start_cache_gginterstitial",bundle);
        mGgTimerTask = new TimerTask(){public void run(){
            mIsStartCacheGgInterstitial = false;
        }};
        mGgTimer.schedule(mGgTimerTask,6000L);/*****************/
    }

    public boolean isCachedGgInterstitial()
    {
        if(null != mInterstitialAd && mIsCachedGgAd)
            return true;
        return false;
    }
    /*******************************FB插屏广告***************************/
    private Timer mFbTimer;
    private TimerTask mFbTimerTask;
    private int mCurrentFbRetryCount;
    private volatile boolean mIsStartCacheFbInterstitial;
    public static final long mFbAdRetryIntervalTime = 3000l;
    public InterstitialAdListener mFbInterstitialAdListener;
    private volatile com.facebook.ads.InterstitialAd mFbInterstitialAd;

    public void showFbInterstitial()
    {
        if(isCachedFbInterstitial())
        {
            mFbInterstitialAd.show();
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
            Bundle bundle = new Bundle();/*******************************************/
            mFirebaseAnalytics.logEvent("start_show_fbinterstitial",bundle);
        }
    }

    private void initFbInterstitialConfigs()
    {
        if(isPrintInterstitialLog)
            Log.i("fb","FaceBook插屏广告初始化！");
        if(null == mFbInterstitialAdListener)
        {
            mFbInterstitialAdListener = new InterstitialAdListener()
            {
                public void onAdLoaded(Ad ad)
                {
                    if(isPrintInterstitialLog)
                        Log.i("fb","成功请求到FaceBook插屏广告资源！");
                    if(null != mFbTimer) mFbTimer.cancel();/*******************/
                    mIsStartCacheFbInterstitial = false;/**********************/
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
                    Bundle bundle = new Bundle();/*******************************************/
                    mFirebaseAnalytics.logEvent("successful_cache_fbinterstitial",bundle);
                }

                public void onAdClicked(Ad ad)
                {
                    if(isPrintInterstitialLog)
                        Log.i("fb","点击FaceBook插屏广告页面！");
                }

                public void onLoggingImpression(Ad ad)
                {
                    if(isPrintInterstitialLog)
                        Log.i("fb","FaceBook插屏广告即将展示！");
                }

                public void onInterstitialDisplayed(Ad ad)
                {
                    if(isPrintInterstitialLog)
                        Log.i("fb","FaceBook插屏广告正在展示！");
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
                    Bundle bundle = new Bundle();/*******************************************/
                    mFirebaseAnalytics.logEvent("successful_show_fbinterstitial",bundle);
                }

                public void onInterstitialDismissed(Ad ad)
                {
                    if(isPrintInterstitialLog)
                        Log.i("fb","关闭FaceBook插屏广告页面！");
                    mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
                    Bundle bundle = new Bundle();/*******************************************/
                    mFirebaseAnalytics.logEvent("normal_close_fbinterstitial",bundle);/***/
                }

                public void onError(Ad ad,AdError adError)
                {
                    if(isPrintInterstitialLog)
                        Log.i("fb","请求FaceBook插屏广告发生错误：" + adError.getErrorMessage());
                    if(mCurrentFbRetryCount > 0)
                    {
                        mCurrentFbRetryCount--;
                        if(isPrintInterstitialLog)
                            Log.i("fb","等待"+ mFbAdRetryIntervalTime +"毫秒后发起FaceBook插屏广告重试！");
                        Observable.just("Wait"+mFbAdRetryIntervalTime+"Millisecond!").doOnNext(new Consumer<String>()
                        {
                            public void accept(String str) throws Exception
                            {
                                Log.i("sss", "ThreadName:" + Thread.currentThread().getName());
                                Thread.sleep(mFbAdRetryIntervalTime);
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>()
                        {
                            public void accept(String s) throws Exception
                            {
                                if(isPrintInterstitialLog)
                                    Log.i("fb","发起FaceBook插屏广告重试请求！");
                                if(null != mFbTimer)
                                    mFbTimer.cancel();
                                mFbTimer = new Timer();
                                mIsStartCacheFbInterstitial = true;/***************************/
                                if(null != mFbInterstitialAd)mFbInterstitialAd.setAdListener(null);
                                mFbTimerTask = new TimerTask(){
                                    public void run()
                                    {
                                        mIsStartCacheFbInterstitial = false;
                                    }
                                };
                                mFbTimer.schedule(mFbTimerTask,6000L);/**************************/
                                mFbInterstitialAd = new com.facebook.ads.InterstitialAd(getApplicationContext(),getString(R.string.fb_placement_in_id));
                                mFbInterstitialAd.setAdListener(mFbInterstitialAdListener);/*****************************/mFbInterstitialAd.loadAd();
                                mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                                Bundle bundle = new Bundle();/**************************/
                                mFirebaseAnalytics.logEvent("start_cache_fbinterstitial",bundle);
                            }
                        });
                    }
                    else
                    {
                        if(isPrintInterstitialLog)
                            Log.i("fb","FaceBook插屏广告重试次数已用完故无法重试！");
                        if(null != mFbTimer) mFbTimer.cancel();/*****************************/
                        mIsStartCacheFbInterstitial = false;/********************************/
                    }
                }
            };
        }

        if(null == mFbInterstitialAdListenerForDirect)
        {
            mFbInterstitialAdListenerForDirect = new InterstitialAdListener()
            {
                public void onAdLoaded(Ad ad)
                {
                    if(isPrintInterstitialLog)
                        Log.i("fb","成功请求到FaceBook插屏广告资源！");
                    try
                    {
                        Intent intt = new Intent(getApplicationContext(), PointActivity.class);
                        intt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intt,0);
                        pendingIntent.send();/**************************/
                    }
                    catch(PendingIntent.CanceledException e)
                    {
                        e.printStackTrace();
                    }
                }

                public void onAdClicked(Ad ad)
                {
                    if(isPrintInterstitialLog)
                        Log.i("fb","点击FaceBook插屏广告页面！");
                }

                public void onLoggingImpression(Ad ad)
                {
                    if(isPrintInterstitialLog)
                        Log.i("fb","FaceBook插屏广告即将展示！");
                }

                public void onInterstitialDisplayed(Ad ad)
                {
                    if(isPrintInterstitialLog)
                        Log.i("fb","FaceBook插屏广告正在展示！");
                }

                public void onInterstitialDismissed(Ad ad)
                {
                    if(isPrintInterstitialLog)
                        Log.i("fb","关闭FaceBook插屏广告页面！");
                }

                public void onError(Ad ad,AdError adError)
                {
                    if(isPrintInterstitialLog)
                        Log.i("fb","请求FaceBook插屏广告发生错误：" + adError.getErrorMessage());
                    if(mCurrentFbRetryCountForDirect > 0)
                    {
                        mCurrentFbRetryCountForDirect--;
                        if(isPrintInterstitialLog)
                            Log.i("fb","等待"+ mFbAdRetryIntervalTime +"毫秒后发起FaceBook插屏广告重试！");
                        Observable.just("Wait"+mFbAdRetryIntervalTime+"Millisecond!").doOnNext(new Consumer<String>()
                        {
                            public void accept(String str) throws Exception
                            {
                                Log.i("sss", "ThreadName:" + Thread.currentThread().getName());
                                Thread.sleep(mFbAdRetryIntervalTime);
                            }
                        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>()
                        {
                            public void accept(String s) throws Exception
                            {
                                if(isPrintInterstitialLog)
                                    Log.i("fb","发起FaceBook插屏广告重试请求！");
                                if(null != mFbInterstitialAdForDirect)
                                mFbInterstitialAdForDirect.setAdListener(null);
                                mFbInterstitialAdForDirect = new com.facebook.ads.
                                InterstitialAd(getApplicationContext(),getString(R.string.fb_placement_out_id));
                                mFbInterstitialAdForDirect.setAdListener(mFbInterstitialAdListenerForDirect);
                                mFbInterstitialAdForDirect.loadAd();
                            }
                        });
                    }
                    else
                    {
                        if(isPrintInterstitialLog)
                            Log.i("fb","FaceBook插屏广告重试次数已用完故无法重试！");
                    }
                }
            };
        }
    }

    public void startCacheFbInterstitial()
    {
        if(null != mFbTimer)
            mFbTimer.cancel();
        mFbTimer = new Timer();
        mCurrentFbRetryCount = 1;/***************************************/
        mIsStartCacheFbInterstitial = true;/*****************************/
        if(null != mFbInterstitialAd)mFbInterstitialAd.setAdListener(null);
        mFbTimerTask = new TimerTask(){
            public void run()
            {
                mIsStartCacheFbInterstitial = false;
            }
        };
        mFbTimer.schedule(mFbTimerTask,6000L);/**************************/
        mFbInterstitialAd = new com.facebook.ads.InterstitialAd(getApplicationContext(),getString(R.string.fb_placement_in_id));
        mFbInterstitialAd.setAdListener(mFbInterstitialAdListener);/*****************************/mFbInterstitialAd.loadAd();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();/**************************/
        mFirebaseAnalytics.logEvent("start_cache_fbinterstitial",bundle);
    }

    public boolean isCachedFbInterstitial()
    {
        if(null == mFbInterstitialAd)
            return false;
        else if(!mFbInterstitialAd.isAdLoaded() || mFbInterstitialAd.isAdInvalidated())
        {
            mFbInterstitialAd.destroy();
            return false;
        }
        return true;
    }

    private int mCurrentFbRetryCountForDirect = 0;
    public InterstitialAdListener mFbInterstitialAdListenerForDirect;
    public com.facebook.ads.InterstitialAd mFbInterstitialAdForDirect;
    public void startExtraFbInterstitial()
    {
        mCurrentFbRetryCountForDirect = 1;
        if(null != mFbInterstitialAdForDirect)
        mFbInterstitialAdForDirect.setAdListener(null);
        mFbInterstitialAdForDirect = new com.facebook.ads.
        InterstitialAd(getApplicationContext(),getString(R.string.fb_placement_out_id));
        mFbInterstitialAdForDirect.setAdListener(mFbInterstitialAdListenerForDirect);
        mFbInterstitialAdForDirect.loadAd();
    }

    public boolean isNeedGetSystemAlertDialogPermission()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("systemalertdialog",false);
    }

    public void setNeedGetSystemAlertDialogPermission(boolean isNeed)
    {
        getSharedPreferences(getPackageName(), Context.MODE_PRIVATE).edit().putBoolean("systemalertdialog",isNeed).commit();
    }

    public boolean isHaveNet(Activity activity)
    {
        if(!BaseApp.getInstance().isNetworkConnected())
        {
            SnackbarManager.show(
            com.nispok.snackbar.Snackbar
            .with(getApplicationContext())
            .text("Network connection error...")
            .actionLabel("Set").actionColor(Color.parseColor("#ffff0000"))
            .actionListener(new ActionClickListener()
            {
                public void onActionClicked(com.nispok.snackbar.Snackbar snackbar)
                {
                    isNormalJump = true;
                    activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            }),activity);
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(BaseApp.getInstance());
            Bundle bundle = new Bundle();/*******************************************/
            mFirebaseAnalytics.logEvent("NotNet_ShowAds",bundle);
            return false;
        }
        return true;
    }

    public boolean isNetworkConnected()
    {
        ConnectivityManager mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivityManager.getActiveNetworkInfo();
        if (info != null)
        {
            return info.isAvailable();
        }
        return false;
    }

    public void requestSystemAlertWindowPermission(Activity activity)
    {
        if(null != getAdConfig() && getAdConfig().isPlayExtraAd() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Settings.canDrawOverlays(this) && isNeedGetSystemAlertDialogPermission())
        {
            final BaseDialog permissionDialog = new BaseDialog(activity);
            permissionDialog.setCanceledOnTouchOutside(true);
            setNeedGetSystemAlertDialogPermission(false);
            permissionDialog.show();

            View view = LayoutInflater.from(activity).inflate(R.layout.inflater_permissiondialogdefault,null);
            permissionDialog.setContentView(view);
            TextView content = (TextView)view.findViewById(R.id.permissiondialog_content);
            content.setText(getString(R.string.permissioncontent));
            content.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
            content.setTextColor(Color.argb(255,255,255,255));
            content.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialogdefaultblackbg));
            TextView btn = (TextView)view.findViewById(R.id.permissiondialog_btn);
            btn.setText(getString(R.string.setup));
            btn.setTextSize(TypedValue.COMPLEX_UNIT_DIP,15);
            btn.setTextColor(Color.argb(255,216,80,126));
            btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_dialogdefaultblackbg));
            btn.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    isNormalJump = true;
                    Intent intent = new Intent();/******************************/
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);/************/
                    intent.setAction(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    permissionDialog.dismiss();
                    startActivity(intent);
                }
            });

            Window window = permissionDialog.getWindow();
            window.getDecorView().setPadding(0,0,0,0);
            window.setWindowAnimations(R.style.BottomOpenDialogAnim);
            window.getDecorView().setBackgroundResource(android.R.color.transparent);
            DisplayMetrics displayMetrics = new DisplayMetrics();/******************/
            ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = displayMetrics.widthPixels;
            params.gravity = Gravity.BOTTOM;
            window.setAttributes(params);
        }
    }

    private void setupShortcuts()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1)
        {
            ShortcutManager mShortcutManager = getSystemService(ShortcutManager.class);
            List<ShortcutInfo> infos = new ArrayList<>();
            Intent intent0 = new Intent(this,MainActivity.class);
            intent0.setAction("com.expert.cleanup.acts.main");
            intent0.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            Intent intent1 = new Intent(this,SpaActivity.class);
            intent1.setAction("com.expert.cleanup.acts.spa");
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ShortcutInfo info1 = new ShortcutInfo.Builder(this,"spa")
                    .setLongLabel(getString(R.string.start_app_name))
                    .setShortLabel(getString(R.string.start_app_name))
                    .setIcon(Icon.createWithResource(this, R.mipmap.s_open))
                    .setIntent(intent1)
                    .build();
            infos.add(info1);
            /**************************************************************************/
            Intent intent2 = new Intent(this,FlashLightActivity.class);
            intent2.setAction("com.expert.cleanup.acts.flashlight");
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ShortcutInfo info2 = new ShortcutInfo.Builder(this,"flashlight")
                    .setLongLabel(getString(R.string.flashlight))
                    .setShortLabel(getString(R.string.flashlight))
                    .setIcon(Icon.createWithResource(this, R.mipmap.s_flashlight))
                    .setIntents(new Intent[]{intent0,intent2})
                    .build();
            infos.add(info2);
            /****************************************************************************/
            Intent intent3 = new Intent(this,DeviceInfoActivity.class);
            intent3.setAction("com.expert.cleanup.acts.deviceinfo");
            intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ShortcutInfo info3 = new ShortcutInfo.Builder(this,"deceinfo")
                    .setLongLabel(getString(R.string.infos))
                    .setShortLabel(getString(R.string.infos))
                    .setIcon(Icon.createWithResource(this, R.mipmap.s_info))
                    .setIntents(new Intent[]{intent0,intent3})
                    .build();
            infos.add(info3);
            mShortcutManager.setDynamicShortcuts(infos);
        }
    }

    public void killExtraProcess()
    {
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> applicationList = pm.getInstalledApplications(0);
        ActivityManager activityManager =(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for(ApplicationInfo applicationInfo : applicationList)/*************************************/
        {
            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !applicationInfo.packageName.equals(getPackageName()))
            {
                activityManager.killBackgroundProcesses(applicationInfo.packageName);
            }
        }
    }
}