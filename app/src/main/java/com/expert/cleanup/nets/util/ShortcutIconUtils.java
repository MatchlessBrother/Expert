package com.expert.cleanup.nets.util;

import java.util.Timer;
import android.os.Build;
import android.os.Bundle;
import java.util.TimerTask;
import android.os.Parcelable;
import android.content.Intent;
import android.content.Context;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.content.pm.ShortcutInfo;
import android.content.SharedPreferences;
import android.content.pm.ShortcutManager;
import com.google.firebase.analytics.FirebaseAnalytics;

public class ShortcutIconUtils
{
    public static Timer mTimer;
    public static TimerTask mTimerTask;

    /*******************************************生成快捷图标******************************************/
    public static void createShortcutIcon(final Context context, final int appIcon, final String appName)
    {
        ComponentName componentName = new ComponentName(context.getPackageName(), "com.expert.cleanup.acts.SpaActivity");
        Intent intent = new Intent();/****/
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        {
            addShortcutIcon(context, appIcon, appName, intent);
        }
        else
        {
            addShortcutIcon(context,appIcon,appName,intent,componentName);
        }
    }

    public static void cancelOfFailedToGenerateShortcutIcon()
    {
       if(null != mTimer)
           mTimer.cancel();
       if(null != mTimerTask)
           mTimerTask.cancel();
    }

    public static void failedToGenerateShortcutIcon(final Context context)
    {
        mTimer = new Timer();
        mTimerTask = new TimerTask()
        {
            public void run()
            {
                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
                Bundle bundle = new Bundle();/*************************************/
                mFirebaseAnalytics.logEvent("generatedunsuccessfully",bundle);
            }
        };
        mTimer.schedule(mTimerTask,10 * 60 * 1000l);
    }

    public static void succeedToGenerateShortcutIcon(final Context context)
    {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
        {
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
            Bundle bundle = new Bundle();/****************************************/
            mFirebaseAnalytics.logEvent("generatedsuccessfully_lowversion",bundle);
        }
        else
        {
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context.getApplicationContext());
            Bundle bundle = new Bundle();/*****************************************/
            mFirebaseAnalytics.logEvent("generatedsuccessfully_highversion",bundle);
        }
    }

    /*****************************************生成快捷图标老方式****************************************/
    private static void addShortcutIcon(Context context, int appIcon, String appName, Intent shortcutIntent)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("IconsHaveBeenCreated", false)) {
            succeedToGenerateShortcutIcon(context);
            Intent intent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
            intent.putExtra("duplicate", false);/*************************************/
            intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);/*********************/
            intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);/************/
            sharedPreferences.edit().putBoolean("IconsHaveBeenCreated", true).commit();
            Parcelable iconParcelable = Intent.ShortcutIconResource.fromContext(context, appIcon);
            intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconParcelable);/***************/
            context.getApplicationContext().sendBroadcast(intent);/*****************************/
        }
    }

    /*****************************************生成快捷图标新方式********************************************************************/
    private static void addShortcutIcon(Context context, int appIcon, String appName, Intent shortcutIntent, ComponentName componentName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("IconsHaveBeenCreated", false)) {
            failedToGenerateShortcutIcon(context);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                ShortcutManager shortcutManager = (ShortcutManager) context.getSystemService(Context.SHORTCUT_SERVICE);
                if (shortcutManager != null && shortcutManager.isRequestPinShortcutSupported()) {
                    shortcutIntent.putExtra("duplicate", false);
                    shortcutIntent.setAction(Intent.ACTION_VIEW);
                    ShortcutInfo info = new ShortcutInfo.Builder(context, "app_id")
                            .setIntent(shortcutIntent)
                            .setActivity(componentName)
                            .setIcon(Icon.createWithBitmap(BitmapFactory.decodeResource(context.getResources(), appIcon)))
                            .setShortLabel(appName)
                            .build();
                    PendingIntent shortcutCallbackIntent = PendingIntent.getBroadcast(context, 0, new Intent(context,ShortIconReceive.class), PendingIntent.FLAG_UPDATE_CURRENT);
                    shortcutManager.requestPinShortcut(info, shortcutCallbackIntent.getIntentSender());
                }
            }
        }
    }
}