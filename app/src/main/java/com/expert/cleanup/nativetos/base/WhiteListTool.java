package com.expert.cleanup.nativetos.base;

import java.util.List;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import com.expert.cleanup.acts.base.WhiteUserActivity;

public class WhiteListTool
{
    public static int getWhiteListSize(Context context)
    {
        int total = 0;
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> applicationList = pm.getInstalledApplications(0);
        for(ApplicationInfo applicationInfo : applicationList)/***************/
        {
            if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !applicationInfo.packageName.equals(context.getPackageName()) && !WhiteUserActivity.getWhiteUser(context).contains(applicationInfo.packageName))
            {
                total++;
            }
        }
        return total;
    }
}