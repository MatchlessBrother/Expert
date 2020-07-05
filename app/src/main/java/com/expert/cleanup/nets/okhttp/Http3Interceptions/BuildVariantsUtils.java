package com.expert.cleanup.nets.okhttp.Http3Interceptions;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

/**判定安装包的BuildVariants值*/
public class BuildVariantsUtils
{
    /**判定当前运行的软件是Debug模式还是Release模式,true为Debug,false为Release**/
    public static boolean isDebug(Context context)
    {
        ApplicationInfo applicationInfo = context.getApplicationInfo();
        if(null != applicationInfo)
            return (applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        return false;
    }

    /******************************获取软件版本号*****************************/
    public static int getVersionCode(Context mContext)
    {
        int versionCode = 0;
        try
        {
            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),0).versionCode;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionCode;
    }

    /******************************获取软件版本名*****************************/
    public static String getVersionName(Context context)
    {
        String versionName = "1.0.0";
        try
        {
            versionName = context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }
        return versionName;
    }
    /****************************安装指定路径的App****************************/
    public static void install(Context context, String filePath)
    {
        File apkFile = new File(filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(context,
                    context.getPackageName() + ".baseFileProvider",apkFile);
            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
        }
        else
        {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }
}