package com.expert.cleanup.sers;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;

public class AutoStartReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, Intent intent)
    {
        if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equals(Intent.ACTION_LOCKED_BOOT_COMPLETED))
        {
            NotifyService.start(context.getApplicationContext());
        }
    }
}