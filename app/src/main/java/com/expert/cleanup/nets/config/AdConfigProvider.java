package com.expert.cleanup.nets.config;

import android.net.Uri;
import android.os.Bundle;
import android.database.Cursor;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.content.ContentValues;
import android.content.ContentProvider;
import android.content.SharedPreferences;

import com.expert.cleanup.nets.util.GsonUtils;

public class AdConfigProvider extends ContentProvider
{
    private static final String SHARE_KEY_NAME = "AdConfigKey";
    private static final String SHARE_LIB_NAME = "AdConfigShareLib";
    private static final String UPDATETIME_LIB_NAME = "UpdateTime_Lib";
    private static final String UPDATETIME_KEY_NAME = "UpdateTime_Key";
    private static final String SHOWINGEXTRAAD_LIB_NAME= "ShowingExtraAd_Lib";
    private static final String SHOWINGEXTRAAD_KEY_NAME= "ShowingExtraAd_Key";
    private static final String READE_AD_CONFIG_ACTION = "READE_AD_CONFIG_ACTION";
    private static final String WRITE_AD_CONFIG_ACTION = "WRITE_AD_CONFIG_ACTION";
    private static final String FIRSTEXTRAADSHOWSTATUS_LIB_NAME = "FirstExtraAdShowStatus_Lib";
    private static final String FIRSTEXTRAADSHOWSTATUS_KEY_NAME = "FirstExtraAdShowStatus_Key";
    private static final Uri URI = Uri.parse("content://com.expert.cleanup.nets.config.adconfigprovider");

    public boolean onCreate()
    {
        return true;

    }

    public String getType(@NonNull Uri uri)
    {
        return null;

    }

    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values)
    {
        return null;

    }

    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        return 0;

    }

    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs)
    {
        return 0;

    }

    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        return null;

    }

    /************************************************************************************************************************************************************/
    /************************************************************************************************************************************************************/

    public synchronized static long getNextUpdateTime(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(UPDATETIME_LIB_NAME,Context.MODE_MULTI_PROCESS);
        return sharedPreferences.getLong(UPDATETIME_KEY_NAME,0l);
    }

    public synchronized static boolean isShowingExtraAd(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHOWINGEXTRAAD_LIB_NAME,Context.MODE_MULTI_PROCESS);
        return sharedPreferences.getBoolean(SHOWINGEXTRAAD_KEY_NAME,true);
    }

    public synchronized static AdConfig getLocalAdConfig(Context context)
    {
        Bundle bundle = context.getContentResolver().call(AdConfigProvider.URI,READE_AD_CONFIG_ACTION,"",null);
        AdConfig adConfig = bundle.getParcelable(READE_AD_CONFIG_ACTION);
        return adConfig;
    }

    public synchronized static boolean isShowFirstExtraAd(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FIRSTEXTRAADSHOWSTATUS_LIB_NAME,Context.MODE_MULTI_PROCESS);
        return sharedPreferences.getBoolean(FIRSTEXTRAADSHOWSTATUS_KEY_NAME,false);
    }

    public synchronized static void setNextUpdateTime(Context context,long updateTime)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(UPDATETIME_LIB_NAME,Context.MODE_MULTI_PROCESS);
        sharedPreferences.edit().putLong(UPDATETIME_KEY_NAME,updateTime).commit();
    }

    public synchronized static void setLocalAdConfig(Context context,AdConfig adConfig)
    {
        Bundle bundle = new Bundle();
        bundle.putParcelable(WRITE_AD_CONFIG_ACTION,adConfig);
        context.getContentResolver().call(AdConfigProvider.URI,WRITE_AD_CONFIG_ACTION,"",bundle);
    }

    public Bundle call(@NonNull String method,@Nullable String arg,@Nullable Bundle bundle)
    {
        switch(method)
        {
            case WRITE_AD_CONFIG_ACTION:
            {
                Bundle resultBundle = new Bundle();
                AdConfig adConfig = bundle.getParcelable(WRITE_AD_CONFIG_ACTION);
                if(null != adConfig)storageObject(getContext().getApplicationContext(),SHARE_LIB_NAME,SHARE_KEY_NAME,adConfig);
                if(null != resultBundle && null != adConfig){resultBundle.putParcelable(WRITE_AD_CONFIG_ACTION,adConfig);return resultBundle;}
            }

            case READE_AD_CONFIG_ACTION:
            {
                Bundle resultBundle = new Bundle();
                AdConfig adConfig = extractObject(getContext().getApplicationContext(),SHARE_LIB_NAME,SHARE_KEY_NAME);
                if(null != resultBundle && null != adConfig){resultBundle.putParcelable(READE_AD_CONFIG_ACTION,adConfig);return resultBundle;}
            }
        }
        return new Bundle();
    }

    public static AdConfig extractObject(Context context,String shareLibName,String keyName)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareLibName,Context.MODE_PRIVATE);
        return GsonUtils.getObj(sharedPreferences.getString(keyName,""),AdConfig.class);
    }

    public synchronized static void setShowingExtraAd(Context context,boolean isShowingExtraAd)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHOWINGEXTRAAD_LIB_NAME,Context.MODE_MULTI_PROCESS);
        sharedPreferences.edit().putBoolean(SHOWINGEXTRAAD_KEY_NAME,isShowingExtraAd).commit();
    }

    public synchronized static void setFirstExtraAdShowStatus(Context context,boolean firstExtraAdShowStatus)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(FIRSTEXTRAADSHOWSTATUS_LIB_NAME,Context.MODE_MULTI_PROCESS);
        sharedPreferences.edit().putBoolean(FIRSTEXTRAADSHOWSTATUS_KEY_NAME,firstExtraAdShowStatus).commit();
    }

    public static AdConfig storageObject(Context context,String shareLibName,String keyName,AdConfig adConfig)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(shareLibName,Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(keyName,GsonUtils.objectToGsonString(adConfig)).commit();
        return adConfig;
    }
}