package com.expert.cleanup.nets;

import android.content.Intent;
import io.reactivex.Observable;
import android.content.Context;
import com.expert.cleanup.BaseApp;
import android.app.PendingIntent;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import android.content.BroadcastReceiver;
import io.reactivex.schedulers.Schedulers;
import com.facebook.ads.AudienceNetworkActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import com.expert.cleanup.acts.base.SingleInstanceActivity;

import static com.facebook.FacebookSdk.getApplicationContext;

/***********当用户切换到RecentApps页面时自动跳转到桌面**********/
public class SwitchAppReceiver extends BroadcastReceiver
{
    private static final String SYSTEM_REASON = "reason";
    private static final String SYSTEM_RECENT_APPS = "recent";
    private static final String SYSTEM_LONG_RECENT_APPS = "assist";
    private static SwitchAppReceiver mInstance = new SwitchAppReceiver();

    private SwitchAppReceiver()
    {

    }

    public static SwitchAppReceiver getInstance()
    {
        return mInstance;

    }

    public synchronized void onReceive(final Context context, Intent intent)
    {
        if(null != intent && null != intent.getAction() && intent.getAction().trim().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS) &&
           null != intent.getStringExtra(SYSTEM_REASON) && BaseApp.getInstance().mActivePageNum != 0 && BaseApp.getCurrentActivity() instanceof AudienceNetworkActivity)
        {
            /**********************************点击SwitchApp按钮************************************/
            if(intent.getStringExtra(SYSTEM_REASON).toLowerCase().trim().contains(SYSTEM_RECENT_APPS))
            {
                try
                {
                    Intent intt = new Intent(getApplicationContext(),SingleInstanceActivity.class);
                    intt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intt,0);
                    pendingIntent.send();/***************************************************************************************/
                }
                catch(PendingIntent.CanceledException e)
                {
                    e.printStackTrace();
                }
                Observable.just("waite a moment!").map(new Function<String,String>()
                {
                    public String apply(String noteStr) throws Exception
                    {

                        Thread.sleep(400);
                        return noteStr;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>()
                {
                    public void accept(String noteStr) throws Exception
                    {
                        Intent homeIntent = new Intent();
                        homeIntent.setAction(Intent.ACTION_MAIN);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(homeIntent);
                    }
                });
            }
            /***********************************长按SwitchApp按钮***********************************/
            else if(intent.getStringExtra(SYSTEM_REASON).toLowerCase().trim().contains(SYSTEM_LONG_RECENT_APPS))
            {
                try
                {
                    Intent intt = new Intent(getApplicationContext(),SingleInstanceActivity.class);
                    intt.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intt,0);
                    pendingIntent.send();/***************************************************************************************/
                }
                catch(PendingIntent.CanceledException e)
                {
                    e.printStackTrace();
                }
                Observable.just("waite a moment!").map(new Function<String,String>()
                {
                    public String apply(String noteStr) throws Exception
                    {
                        Thread.sleep(400);
                        return noteStr;
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<String>()
                {
                    public void accept(String s) throws Exception
                    {
                        Intent homeIntent = new Intent();
                        homeIntent.setAction(Intent.ACTION_MAIN);
                        homeIntent.addCategory(Intent.CATEGORY_HOME);
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(homeIntent);
                    }
                });
            }
        }
    }
}