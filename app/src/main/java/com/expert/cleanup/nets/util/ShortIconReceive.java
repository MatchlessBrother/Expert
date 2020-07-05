package com.expert.cleanup.nets.util;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;

public class ShortIconReceive extends BroadcastReceiver
{
    public void onReceive(Context context, Intent intent)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("IconsHaveBeenCreated", true).commit();
        ShortcutIconUtils.cancelOfFailedToGenerateShortcutIcon();
        ShortcutIconUtils.succeedToGenerateShortcutIcon(context);
    }
}