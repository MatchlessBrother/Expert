package com.expert.cleanup.sers;

import java.util.Timer;
import android.view.View;
import android.os.IBinder;
import java.util.TimerTask;
import android.app.Service;
import com.expert.cleanup.R;
import android.content.Intent;
import android.content.Context;
import io.reactivex.Observable;
import android.app.Notification;
import android.app.PendingIntent;
import android.widget.RemoteViews;
import android.graphics.BitmapFactory;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import com.expert.cleanup.acts.CpuActivity;
import io.reactivex.schedulers.Schedulers;
import com.expert.cleanup.acts.MainActivity;
import com.expert.cleanup.acts.BoostActivity;
import com.expert.cleanup.acts.CleanActivity;
import com.expert.cleanup.acts.base.NotifyActivity;
import com.expert.cleanup.nativetos.base.WhiteListTool;
import com.expert.cleanup.acts.BatteryActivity;
import com.expert.cleanup.receivers.ScreenStateReceive;
import io.reactivex.android.schedulers.AndroidSchedulers;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;
import static com.expert.cleanup.acts.BoostActivity.getNextBoostTime;
import static com.expert.cleanup.acts.CleanActivity.getNextCleanTime;
import static com.expert.cleanup.acts.CpuActivity.getNextCoolDownTime;
import static com.expert.cleanup.acts.BatteryActivity.getNextHibernateTime;

public class NotifyService extends Service
{
    private Timer mTimer;
    public static final String CHANNEL_ID = "channel_id_expert";

    public void onCreate()
    {
        super.onCreate();
        NotificationChannel mChannel = null;
        /**************************************************************************************************************************/
        Intent cpuIntent = new Intent(getApplicationContext(), NotifyActivity.class);
        cpuIntent.putExtra("mode","cpu");/******************************************/
        PendingIntent cpuPendingIntent = PendingIntent.getActivity(getApplicationContext(),1,cpuIntent, FLAG_UPDATE_CURRENT);
        /**************************************************************************************************************************/
        Intent boostIntent = new Intent(getApplicationContext(), NotifyActivity.class);
        boostIntent.putExtra("mode","boost");/**************************************/
        PendingIntent boostPendingIntent = PendingIntent.getActivity(getApplicationContext(),2,boostIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        /**************************************************************************************************************************/
        Intent cleanIntent = new Intent(getApplicationContext(), NotifyActivity.class);
        cleanIntent.putExtra("mode","clean");/**************************************/
        PendingIntent cleanPendingIntent = PendingIntent.getActivity(getApplicationContext(),3,cleanIntent, FLAG_UPDATE_CURRENT);
        /**************************************************************************************************************************/
        Intent batteryIntent = new Intent(getApplicationContext(), NotifyActivity.class);
        batteryIntent.putExtra("mode","battery");/**********************************/
        PendingIntent batteryPendingIntent = PendingIntent.getActivity(getApplicationContext(),4,batteryIntent, FLAG_UPDATE_CURRENT);
        /**************************************************************************************************************************/
        Intent flashlightIntent = new Intent(getApplicationContext(),NotifyActivity.class);
        flashlightIntent.putExtra("mode","flashlight");/**********************************/
        PendingIntent flashLightPendingIntent = PendingIntent.getActivity(getApplicationContext(),5,flashlightIntent, FLAG_UPDATE_CURRENT);
        /**************************************************************************************************************************/
        RemoteViews views = new RemoteViews(getPackageName(),R.layout.remote_views);
        views.setTextViewText(R.id.cpu_tv,"cpu");
        views.setTextViewText(R.id.boost_tv,"boost");
        views.setTextViewText(R.id.clean_tv,"clean");
        views.setTextViewText(R.id.battery_tv,"battery");
        views.setTextViewText(R.id.flashlight_tv,"flashlight");

        views.setImageViewResource(R.id.cpu_icon,R.mipmap.note_ic_cpu);
        views.setImageViewResource(R.id.boost_icon,R.mipmap.note_ic_boost);
        views.setImageViewResource(R.id.clean_icon,R.mipmap.note_ic_clean);
        views.setImageViewResource(R.id.battery_icon,R.mipmap.note_ic_battery);
        views.setImageViewResource(R.id.flashlight_icon,R.mipmap.note_ic_flashlight);

        views.setOnClickPendingIntent(R.id.cpu_all,cpuPendingIntent);
        views.setOnClickPendingIntent(R.id.boost_all,boostPendingIntent);
        views.setOnClickPendingIntent(R.id.clean_all,cleanPendingIntent);
        views.setOnClickPendingIntent(R.id.battery_all,batteryPendingIntent);
        views.setOnClickPendingIntent(R.id.flashlight_all,flashLightPendingIntent);

        if(getNextCoolDownTime(getApplicationContext()) > System.currentTimeMillis())
        {
            views.setViewVisibility(R.id.cpu_red_point,View.GONE);
        }
        else
        {
            views.setViewVisibility(R.id.cpu_red_point,View.VISIBLE);
            if(CpuActivity.getLatestApp(getApplicationContext()).size() > 0)
            {
                views.setTextViewText(R.id.cpu_red_point,/*CpuActivity.getLatestApp(getApplicationContext()).size() + */"");
            }
            else
            {
                if(CpuActivity.getCoolerCpuApps(getApplicationContext()) != 0)
                {
                    views.setTextViewText(R.id.cpu_red_point,/*CpuActivity.getCoolerCpuApps(getApplicationContext()) +*/ "");
                }
                else
                {
                    int cpuApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(getApplicationContext())) % 7 + 6);
                    if(cpuApps > WhiteListTool.getWhiteListSize(getApplicationContext()))
                        cpuApps = WhiteListTool.getWhiteListSize(getApplicationContext());
                    CpuActivity.setCoolerCpuApps(getApplicationContext(),cpuApps);
                    if(0 != cpuApps)
                        views.setTextViewText(R.id.cpu_red_point,/*cpuApps +*/ "");
                    else
                        views.setViewVisibility(R.id.cpu_red_point,View.GONE);
                }
            }
        }
        /******************************************************************/
        if(getNextCleanTime(getApplicationContext()) > System.currentTimeMillis())
        {
            views.setViewVisibility(R.id.clean_red_point,View.GONE);
        }
        else
        {
            views.setViewVisibility(R.id.clean_red_point,View.VISIBLE);
            if(CleanActivity.getLatestApp(getApplicationContext()).size() > 0)
            {
                views.setTextViewText(R.id.clean_red_point,/*CleanActivity.getLatestApp(getApplicationContext()).size() +*/ "");
            }
            else
            {
                if(CleanActivity.getCleanApps(getApplicationContext()) != 0)
                {
                    views.setTextViewText(R.id.clean_red_point,/*CleanActivity.getCleanApps(getApplicationContext()) +*/ "");
                }
                else
                {
                    int cleanApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(getApplicationContext())) % 7 + 6);
                    if(cleanApps > WhiteListTool.getWhiteListSize(getApplicationContext()))
                        cleanApps = WhiteListTool.getWhiteListSize(getApplicationContext());
                    CleanActivity.setCleanApps(getApplicationContext(),cleanApps);
                    if(0 != cleanApps)
                        views.setTextViewText(R.id.clean_red_point,/*cleanApps +*/ "");
                    else
                        views.setViewVisibility(R.id.clean_red_point,View.GONE);
                }
            }
        }
        /******************************************************************/
        if(getNextHibernateTime(getApplicationContext()) > System.currentTimeMillis())
        {
            views.setViewVisibility(R.id.battery_red_point,View.GONE);
        }
        else
        {
            views.setViewVisibility(R.id.battery_red_point,View.VISIBLE);
            if(BatteryActivity.getLatestApp(getApplicationContext()).size() > 0)
            {
                views.setTextViewText(R.id.battery_red_point,/*BatteryActivity.getLatestApp(getApplicationContext()).size() +*/ "");
            }
            else
            {
                if(BatteryActivity.getBatteryApps(getApplicationContext()) != 0)
                {
                    views.setTextViewText(R.id.battery_red_point,/*BatteryActivity.getBatteryApps(getApplicationContext()) +*/ "");
                }
                else
                {
                    int batteryApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(getApplicationContext())) % 7 + 6);
                    if(batteryApps > WhiteListTool.getWhiteListSize(getApplicationContext()))
                        batteryApps = WhiteListTool.getWhiteListSize(getApplicationContext());
                    BatteryActivity.setBatteryApps(getApplicationContext(),batteryApps);
                    if(0 != batteryApps)
                        views.setTextViewText(R.id.battery_red_point,/*batteryApps +*/ "");
                    else
                        views.setViewVisibility(R.id.battery_red_point,View.GONE);
                }
            }
        }
        /******************************************************************/
        if(getNextBoostTime(getApplicationContext()) > System.currentTimeMillis())
        {
            views.setViewVisibility(R.id.boost_red_point,View.GONE);
        }
        else
        {
            views.setViewVisibility(R.id.boost_red_point,View.VISIBLE);
            if(BoostActivity.getLatestApp(getApplicationContext()).size() > 0)
            {
                views.setTextViewText(R.id.boost_red_point,/*BoostActivity.getLatestApp(getApplicationContext()).size() +*/ "");
            }
            else
            {
                if(BoostActivity.getBoostApps(getApplicationContext()) != 0)
                {
                    views.setTextViewText(R.id.boost_red_point,/*BoostActivity.getBoostApps(getApplicationContext()) +*/ "");
                }
                else
                {
                    int boostApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(getApplicationContext())) % 7 + 6);
                    if(boostApps > WhiteListTool.getWhiteListSize(getApplicationContext()))
                        boostApps = WhiteListTool.getWhiteListSize(getApplicationContext());
                    BoostActivity.setBoostApps(getApplicationContext(),boostApps);
                    if(0 != boostApps)
                        views.setTextViewText(R.id.boost_red_point,/*boostApps +*/ "");
                    else
                        views.setViewVisibility(R.id.boost_red_point,View.GONE);
                }
            }
        }

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent, FLAG_UPDATE_CURRENT);
            mChannel = new NotificationChannel(CHANNEL_ID,getString(R.string.app_name),NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);/****************************************************/
            Notification notification = new Notification.Builder(getApplicationContext(),CHANNEL_ID).setTicker(getString(R.string.app_name))
            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
            .setContentText("Optimize your system automatically")
            .setContentTitle("System optimization")
            .setSmallIcon(R.mipmap.ic_small_launcher)
            .setContentIntent(pendingIntent)
            .setCustomContentView(views)
            .build();
            startForeground(1,notification);
        }
        /*else
        {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent, FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(getApplicationContext()).setTicker(getString(R.string.app_name))
            .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
            .setContentText("Optimize your system automatically")
            .setContentTitle("System optimization")
            .setSmallIcon(R.mipmap.ic_small_launcher)
            .setContentIntent(pendingIntent)
            .setContent(views)
            .build();
            startForeground(1,notification);
        }*/
    }

    public IBinder onBind(Intent intent)
    {
        return null;
    }

    public static void start(Context context)
    {
        Intent intent = new Intent(context,NotifyService.class);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            context.startForegroundService(intent);
        }
        else
        {
            context.startService(intent);
        }

    }

    public int onStartCommand(Intent intent, int flags, int startId)
    {
        ScreenStateReceive.mInstance.register(this);
        if(null != mTimer)
            mTimer.cancel();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask()
        {
            public void run()
            {
                startUpdateNotify(getApplicationContext());
            }
        },60000l,60000l);
        return Service.START_STICKY;
    }

    public static void startUpdateNotify(Context context)
    {
        Observable.just("update notifycation").map(new Function<String, Notification>()
        {
            public Notification apply(String s) throws Exception
            {
                return NotifyService.updateNotify(context);
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Notification>()
        {
            public void accept(Notification notification) throws Exception
            {
                NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(1,notification);
            }
        });
    }

    public static Notification updateNotify(Context context)
    {
        NotificationChannel mChannel = null;
        /**************************************************************************************************************************/
        Intent cpuIntent = new Intent(context, NotifyActivity.class);
        cpuIntent.putExtra("mode","cpu");/******************************************/
        PendingIntent cpuPendingIntent = PendingIntent.getActivity(context,1,cpuIntent, FLAG_UPDATE_CURRENT);
        /**************************************************************************************************************************/
        Intent boostIntent = new Intent(context, NotifyActivity.class);
        boostIntent.putExtra("mode","boost");/**************************************/
        PendingIntent boostPendingIntent = PendingIntent.getActivity(context,2,boostIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        /**************************************************************************************************************************/
        Intent cleanIntent = new Intent(context, NotifyActivity.class);
        cleanIntent.putExtra("mode","clean");/**************************************/
        PendingIntent cleanPendingIntent = PendingIntent.getActivity(context,3,cleanIntent, FLAG_UPDATE_CURRENT);
        /**************************************************************************************************************************/
        Intent batteryIntent = new Intent(context, NotifyActivity.class);
        batteryIntent.putExtra("mode","battery");/**********************************/
        PendingIntent batteryPendingIntent = PendingIntent.getActivity(context,4,batteryIntent, FLAG_UPDATE_CURRENT);
        /**************************************************************************************************************************/
        Intent flashlightIntent = new Intent(context,NotifyActivity.class);
        flashlightIntent.putExtra("mode","flashlight");/**********************************/
        PendingIntent flashLightPendingIntent = PendingIntent.getActivity(context,5,flashlightIntent, FLAG_UPDATE_CURRENT);
        /**************************************************************************************************************************/
        Intent launcherIntent = new Intent(context,NotifyActivity.class);
        launcherIntent.putExtra("mode","launcher");/*****************************************************************/
        PendingIntent launcherPendingIntent = PendingIntent.getActivity(context,6,launcherIntent,FLAG_UPDATE_CURRENT);
        /**************************************************************************************************************************/
        RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.remote_views);

        views.setTextViewText(R.id.cpu_tv,"cpu");
        views.setTextViewText(R.id.boost_tv,"boost");
        views.setTextViewText(R.id.clean_tv,"clean");
        views.setTextViewText(R.id.battery_tv,"battery");
        views.setTextViewText(R.id.flashlight_tv,"flashlight");

        views.setImageViewResource(R.id.cpu_icon,R.mipmap.note_ic_cpu);
        views.setImageViewResource(R.id.boost_icon,R.mipmap.note_ic_boost);
        views.setImageViewResource(R.id.clean_icon,R.mipmap.note_ic_clean);
        views.setImageViewResource(R.id.launcher,R.mipmap.ic_launcher);
        views.setImageViewResource(R.id.battery_icon,R.mipmap.note_ic_battery);
        views.setImageViewResource(R.id.flashlight_icon,R.mipmap.note_ic_flashlight);

        views.setOnClickPendingIntent(R.id.cpu_all,cpuPendingIntent);
        views.setOnClickPendingIntent(R.id.boost_all,boostPendingIntent);
        views.setOnClickPendingIntent(R.id.clean_all,cleanPendingIntent);
        views.setOnClickPendingIntent(R.id.launcher,launcherPendingIntent);
        views.setOnClickPendingIntent(R.id.notifyall,launcherPendingIntent);
        views.setOnClickPendingIntent(R.id.battery_all,batteryPendingIntent);
        views.setOnClickPendingIntent(R.id.flashlight_all,flashLightPendingIntent);

        if(getNextCoolDownTime(context) > System.currentTimeMillis())
        {
            views.setViewVisibility(R.id.cpu_red_point,View.GONE);
        }
        else
        {
            views.setViewVisibility(R.id.cpu_red_point,View.VISIBLE);
            if(CpuActivity.getLatestApp(context).size() > 0)
            {
                views.setTextViewText(R.id.cpu_red_point,/*CpuActivity.getLatestApp(context).size() +*/ "");
            }
            else
            {
                if(CpuActivity.getCoolerCpuApps(context) != 0)
                {
                    views.setTextViewText(R.id.cpu_red_point,/*CpuActivity.getCoolerCpuApps(context) +*/ "");
                }
                else
                {
                    int cpuApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(context)) % 7 + 6);
                    if(cpuApps > WhiteListTool.getWhiteListSize(context))
                        cpuApps = WhiteListTool.getWhiteListSize(context);
                    CpuActivity.setCoolerCpuApps(context,cpuApps);
                    if(0 != cpuApps)
                        views.setTextViewText(R.id.cpu_red_point,/*cpuApps +*/ "");
                    else
                        views.setViewVisibility(R.id.cpu_red_point,View.GONE);
                }
            }
        }
        /******************************************************************/
        if(getNextCleanTime(context) > System.currentTimeMillis())
        {
            views.setViewVisibility(R.id.clean_red_point,View.GONE);
        }
        else
        {
            views.setViewVisibility(R.id.clean_red_point,View.VISIBLE);
            if(CleanActivity.getLatestApp(context).size() > 0)
            {
                views.setTextViewText(R.id.clean_red_point,/*CleanActivity.getLatestApp(context).size() +*/ "");
            }
            else
            {
                if(CleanActivity.getCleanApps(context) != 0)
                {
                    views.setTextViewText(R.id.clean_red_point,/*CleanActivity.getCleanApps(context) +*/ "");
                }
                else
                {
                    int cleanApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(context)) % 7 + 6);
                    if(cleanApps > WhiteListTool.getWhiteListSize(context))
                        cleanApps = WhiteListTool.getWhiteListSize(context);
                    CleanActivity.setCleanApps(context,cleanApps);
                    if(0 != cleanApps)
                        views.setTextViewText(R.id.clean_red_point,/*cleanApps +*/ "");
                    else
                        views.setViewVisibility(R.id.clean_red_point,View.GONE);
                }
            }
        }
        /******************************************************************/
        if(getNextHibernateTime(context) > System.currentTimeMillis())
        {
            views.setViewVisibility(R.id.battery_red_point,View.GONE);
        }
        else
        {
            views.setViewVisibility(R.id.battery_red_point,View.VISIBLE);
            if(BatteryActivity.getLatestApp(context).size() > 0)
            {
                views.setTextViewText(R.id.battery_red_point,/*BatteryActivity.getLatestApp(context).size() +*/ "");
            }
            else
            {
                if(BatteryActivity.getBatteryApps(context) != 0)
                {
                    views.setTextViewText(R.id.battery_red_point,/*BatteryActivity.getBatteryApps(context) +*/ "");
                }
                else
                {
                    int batteryApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(context)) % 7 + 6);
                    if(batteryApps > WhiteListTool.getWhiteListSize(context))
                        batteryApps = WhiteListTool.getWhiteListSize(context);
                    BatteryActivity.setBatteryApps(context,batteryApps);
                    if(0 != batteryApps)
                        views.setTextViewText(R.id.battery_red_point,/*batteryApps +*/ "");
                    else
                        views.setViewVisibility(R.id.battery_red_point,View.GONE);
                }
            }
        }
        /******************************************************************/
        if(getNextBoostTime(context) > System.currentTimeMillis())
        {
            views.setViewVisibility(R.id.boost_red_point,View.GONE);
        }
        else
        {
            views.setViewVisibility(R.id.boost_red_point,View.VISIBLE);
            if(BoostActivity.getLatestApp(context).size() > 0)
            {
                views.setTextViewText(R.id.boost_red_point,/*BoostActivity.getLatestApp(context).size() +*/ "");
            }
            else
            {
                if(BoostActivity.getBoostApps(context) != 0)
                {
                    views.setTextViewText(R.id.boost_red_point,/*BoostActivity.getBoostApps(context) +*/ "");
                }
                else
                {
                    int boostApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(context)) % 7 + 6);
                    if(boostApps > WhiteListTool.getWhiteListSize(context))
                        boostApps = WhiteListTool.getWhiteListSize(context);
                    BoostActivity.setBoostApps(context,boostApps);
                    if(0 != boostApps)
                        views.setTextViewText(R.id.boost_red_point,/*boostApps +*/ "");
                    else
                        views.setViewVisibility(R.id.boost_red_point,View.GONE);
                }
            }
        }

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,FLAG_UPDATE_CURRENT);
            mChannel = new NotificationChannel(CHANNEL_ID,context.getString(R.string.app_name),NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(mChannel);/******************************************************/
            Notification notification = new Notification.Builder(context,CHANNEL_ID).setTicker(context.getString(R.string.app_name))
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
            .setContentText("Optimize your system automatically")
            .setContentTitle("System optimization")
            .setSmallIcon(R.mipmap.ic_small_launcher)
            .setContentIntent(pendingIntent)
            .setCustomContentView(views)
            .build();
            return notification;
        }
        else
        {
            Intent intent = new Intent(context,MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent, FLAG_UPDATE_CURRENT);
            Notification notification = new Notification.Builder(context).setTicker(context.getString(R.string.app_name))
            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher))
            .setContentText("Optimize your system automatically")
            .setContentTitle("System optimization")
            .setSmallIcon(R.mipmap.ic_small_launcher)
            .setContentIntent(pendingIntent)
            .setContent(views)
            .build();
            return notification;
        }
    }

    public void onDestroy()
    {
        super.onDestroy();
        if(null != mTimer)mTimer.cancel();
        try
        {
            ScreenStateReceive.mInstance.unRegister(this);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
