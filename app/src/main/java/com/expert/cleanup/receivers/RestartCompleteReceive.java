package com.expert.cleanup.receivers;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import com.expert.cleanup.sers.NotifyService;

public class RestartCompleteReceive extends BroadcastReceiver
{
    public void onReceive(Context context, Intent intent)
    {
        if(null != intent && intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equals(Intent.ACTION_LOCKED_BOOT_COMPLETED)||
        intent.getAction().equals(Intent.ACTION_PACKAGE_DATA_CLEARED) || intent.getAction().equals(Intent.ACTION_PACKAGE_RESTARTED))
        {
            NotifyService.start(context.getApplicationContext());
        }
    }
}