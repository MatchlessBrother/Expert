package com.expert.cleanup.nets.util;

import android.content.Context;
import android.net.ConnectivityManager;

/****网络请求工具****/
public class NetUtils
{
    /*************判断当前手机是否有正确连接网络***********/
    public static boolean WhetherConnectNet(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null && connectivityManager.getActiveNetworkInfo() != null)
            return connectivityManager.getActiveNetworkInfo().isAvailable();
        return false;
    }
}