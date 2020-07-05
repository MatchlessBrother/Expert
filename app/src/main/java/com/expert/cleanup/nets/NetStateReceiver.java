package com.expert.cleanup.nets;

import android.util.Log;
import android.content.Intent;
import android.content.Context;
import android.net.NetworkInfo;
import android.content.IntentFilter;
import android.content.ComponentName;
import android.net.ConnectivityManager;
import android.content.BroadcastReceiver;

public class NetStateReceiver extends BroadcastReceiver
{
    private boolean mIsConnect4G;
    private boolean mIsConnectWifi;
    private NetworkInfo mNetworkInfo;
    private static boolean mIsAllowRetry;
    public static boolean isPrintNetLog = true;
    private ConnectivityManager mConnectivityManager;
    private static NetStateReceiver mInstance = new NetStateReceiver();
    public static final String CONNECTIVITY_ACTION = "android.net.CONNECTIVITY_CHANGE";

    private NetStateReceiver()
    {

    }

    public NetStateReceiver init(Context context)
    {
        IntentFilter netStateFilter = new IntentFilter();
        netStateFilter.addAction(NetStateReceiver.CONNECTIVITY_ACTION);
        context.getApplicationContext().registerReceiver(NetStateReceiver.getInstance(),netStateFilter);
        return this;
    }

    public static NetStateReceiver getInstance()
    {
        return mInstance;

    }

    public void setAllowRetry(boolean isAllowRetry)
    {
        mIsAllowRetry = isAllowRetry;

    }

    public void getConfigInfosImmediately(Context context)
    {
        setAllowRetry(true);
        Intent intent = new Intent(NetStateReceiver.CONNECTIVITY_ACTION);
        context.getApplicationContext().sendBroadcast(intent);/*********/
    }

    public synchronized void onReceive(Context context, Intent intent)
    {
        if(null != intent && null != intent.getAction() && (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION) || intent.getAction().equals(CONNECTIVITY_ACTION)))
        {
            mConnectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            mIsConnect4G = false;mIsConnectWifi = false;
            if(null != mNetworkInfo)
            {
                switch(mNetworkInfo.getType())
                {
                    case ConnectivityManager.TYPE_WIFI:
                    {
                        mIsConnectWifi = mNetworkInfo.isConnected();
                        break;
                    }
                    case ConnectivityManager.TYPE_MOBILE:
                    {
                        mIsConnect4G = mNetworkInfo.isConnected();
                        break;
                    }
                }
            }
            if((mIsConnectWifi || mIsConnect4G) && mIsAllowRetry)
            {
                mIsAllowRetry = false;
                Intent intt = new Intent();
                if(isPrintNetLog)Log.i("NetNotes","开始获取关于广告的配置信息！");/****************/
                intt.setComponent(new ComponentName(context.getApplicationContext(),GetDefConfigInfo.class));
                context.getApplicationContext().startService(intt);/****************************************/
            }
            else if((mIsConnectWifi || mIsConnect4G) && !mIsAllowRetry)
            {
                if(isPrintNetLog)
                    Log.i("NetNotes","已经成功获取过关于广告的配置信息故不再获取！");
            }
            else
            {
                if(isPrintNetLog)
                    Log.i("NetNotes","抱歉,因无网状态导致无法获取关于广告的配置信息！");
            }
        }
    }
}