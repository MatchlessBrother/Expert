package com.expert.cleanup.receivers;

import android.os.Build;
import com.expert.cleanup.R;
import android.content.Intent;
import io.reactivex.Observable;
import android.content.Context;
import android.app.Notification;
import android.app.PendingIntent;
import android.provider.Settings;
import com.expert.cleanup.BaseApp;
import android.content.IntentFilter;
import java.util.concurrent.TimeUnit;
import android.graphics.BitmapFactory;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import io.reactivex.functions.Consumer;
import android.content.BroadcastReceiver;
import io.reactivex.schedulers.Schedulers;
import com.expert.cleanup.acts.MainActivity;
import com.expert.cleanup.nets.util.PhoneHelper;
import com.expert.cleanup.acts.base.TableAdsActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

public class ScreenStateReceive extends BroadcastReceiver
{
    public Context mContext;
    public static final String CHANNEL_ID = "channel_id_expert_trunon";
    public static ScreenStateReceive mInstance = new ScreenStateReceive();

    private ScreenStateReceive()
    {

    }

    public void register(Context context)
    {
        if(null == mInstance)
            mInstance = new ScreenStateReceive();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        intentFilter.addAction(Intent.ACTION_USER_PRESENT);
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        try
        {
            context.getApplicationContext().registerReceiver(mInstance,intentFilter);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void unRegister(Context context)
    {
        if(null != mInstance)
        {
            try
            {
                context.getApplicationContext().unregisterReceiver(mInstance);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void setNextShowTime(Context context)
    {
         context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putLong("showdialogadtime",System.currentTimeMillis() +  2 * 60 * 60 * 1000l).commit();
    }

    public static long getNextShowTime(Context context)
    {
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getLong("showdialogadtime",0l);
    }

    public static void setNotifyNextShowTime(Context context)
    {
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putLong("show_notify_time",System.currentTimeMillis() +  60 * 60 * 1000l).commit();
    }

    public static long getNotifyNextShowTime(Context context)
    {
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getLong("show_notify_time",0l);
    }

    public synchronized void onReceive(Context context,Intent intent)
    {
        mContext = context.getApplicationContext();
        if(null != intent)
        {
            switch(intent.getAction())
            {
                case Intent.ACTION_SCREEN_OFF:
                {
                    if(System.currentTimeMillis() > getNextShowTime(context))
                    {
                        //((BaseApp)context.getApplicationContext()).startCacheOutAd();
                    }
                    /*if(BaseApp.getInstance().getAdConfig().isPlayExtraAd() &&
                    PhoneHelper.getInstallTime(context.getApplicationContext()) + BaseApp.getInstance().getAdConfig().getDelayTimeForPlayExtraAd() * 1000 <= System.currentTimeMillis())
                    {
                         if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        {
                            if(Settings.canDrawOverlays(mContext))
                            {
                                BaseApp.getInstance().startExtraFbInterstitial();
                            }
                        }
                        else
                            BaseApp.getInstance().startExtraFbInterstitial();
                    }*/
                    break;
                }
                case Intent.ACTION_USER_PRESENT:
                {
                    if(System.currentTimeMillis() > getNextShowTime(context) && BaseApp.getInstance().mActivePageNum == 0)
                    {
                        try
                        {
                            Intent intentt = new Intent(context.getApplicationContext(),TableAdsActivity.class);
                            intentt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);/**********************************/
                            PendingIntent pendingIntent = PendingIntent.getActivity(context./******************/
                            getApplicationContext(),0,intentt,PendingIntent.FLAG_UPDATE_CURRENT);
                            pendingIntent.send();
                        }
                        catch (PendingIntent.CanceledException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    if(System.currentTimeMillis() > getNotifyNextShowTime(context))
                    {
                        setNotifyNextShowTime(context);
                        sendOptNotify();
                    }

                    if(null != BaseApp.getInstance().getAdConfig() && BaseApp.getInstance().getAdConfig().isPlayExtraAd() &&
                    PhoneHelper.getInstallTime(context.getApplicationContext()) + BaseApp.getInstance().getAdConfig().getDelayTimeForPlayExtraAd() * 1000 <= System.currentTimeMillis())
                    {
                        /*Observable.timer(15000l,TimeUnit.MILLISECONDS,Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>()
                        {
                            public void accept(Long aLong) throws Exception
                            {
                                BaseApp.getInstance().startExtraFbInterstitial();
                            }
                        });*/
                       /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        {
                            if(Settings.canDrawOverlays(mContext))
                            {
                                BaseApp.getInstance().startExtraFbInterstitial();
                            }
                        }
                        else
                            BaseApp.getInstance().startExtraFbInterstitial();*/
                    }
                    break;
                }

                case Intent.ACTION_POWER_CONNECTED:
                {
                    if(BaseApp.getInstance().getAdConfig().isPlayExtraAd() &&
                    PhoneHelper.getInstallTime(context.getApplicationContext()) + BaseApp.getInstance().getAdConfig().getDelayTimeForPlayExtraAd() * 1000 <= System.currentTimeMillis())
                    {
                       /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        {
                            if(Settings.canDrawOverlays(mContext))
                            {
                                BaseApp.getInstance().startExtraFbInterstitial();
                            }
                        }
                        else
                            BaseApp.getInstance().startExtraFbInterstitial();*/
                    }
                    break;
                }

                case Intent.ACTION_POWER_DISCONNECTED:
                {
                    if(BaseApp.getInstance().getAdConfig().isPlayExtraAd() &&
                    PhoneHelper.getInstallTime(context.getApplicationContext()) + BaseApp.getInstance().getAdConfig().getDelayTimeForPlayExtraAd() * 1000 <= System.currentTimeMillis())
                    {
                       /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                        {
                            if(Settings.canDrawOverlays(mContext))
                            {
                                BaseApp.getInstance().startExtraFbInterstitial();
                            }
                        }
                        else
                            BaseApp.getInstance().startExtraFbInterstitial();*/
                    }
                    break;
                }
            }
        }
    }

    public void sendOptNotify()
    {
        Notification notification = null;
        NotificationManager notificationManager = (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            Intent intent = new Intent(mContext, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext,0,intent,FLAG_UPDATE_CURRENT);
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID,mContext.getString(R.string.app_name),NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);/***********************************************************************************/
            notification = new Notification.Builder(mContext,CHANNEL_ID).setTicker(mContext.getString(R.string.app_name))
            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_launcher))
            .setContentText("Please optimize your system!")
            .setSmallIcon(R.mipmap.ic_small_launcher)
            .setContentTitle("Optimization tips")
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build();
        }
        else
        {
            Intent intent = new Intent(mContext,MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext,0,intent,FLAG_UPDATE_CURRENT);
            notification = new Notification.Builder(mContext).setTicker(mContext.getString(R.string.app_name))
            .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),R.mipmap.ic_launcher))
            .setContentText("Please optimize your system!")
            .setSmallIcon(R.mipmap.ic_small_launcher)
            .setPriority(Notification.PRIORITY_HIGH)
            .setContentTitle("Optimization tips")
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build();
        }
        notificationManager.notify(2,notification);
        Observable.timer(2000L, TimeUnit.MILLISECONDS).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>()
        {
            public void accept(Long aLong) throws Exception
            {
                notificationManager.cancel(2);
            }
        });
    }
}