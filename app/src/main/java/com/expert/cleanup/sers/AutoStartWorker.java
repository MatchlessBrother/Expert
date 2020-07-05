package com.expert.cleanup.sers;

import androidx.work.Worker;
import android.content.Context;
import androidx.work.WorkManager;
import androidx.annotation.NonNull;
import java.util.concurrent.TimeUnit;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import androidx.work.PeriodicWorkRequest;
import androidx.work.ExistingPeriodicWorkPolicy;

import com.expert.cleanup.BaseApp;

public class AutoStartWorker extends Worker
{
    private Context mContext;
    private WorkerParameters mWorkerParameters;
    public static final String AutoStartWorkerTag = "AutoStartWorkerTag";
    public static final String AutoStartWorkerUniqueName = "AutoStartWorkerUniqueName";

    @NonNull
    public ListenableWorker.Result doWork()
    {
        if(null != BaseApp.getInstance())BaseApp.getInstance().registerAssistOfExtraAd(mContext);
        NotifyService.start(mContext);
        return Result.success();
    }

    public static void cancelWorkerByPeriodic()
    {
        WorkManager.getInstance().cancelAllWorkByTag(AutoStartWorkerTag);
    }

    public static void startWorkerByPeriodic(long intervalTime)
    {
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
        AutoStartWorker.class,intervalTime,TimeUnit.MILLISECONDS).addTag(AutoStartWorkerTag).build();
        WorkManager.getInstance().enqueueUniquePeriodicWork(AutoStartWorkerUniqueName,ExistingPeriodicWorkPolicy.KEEP,periodicWorkRequest);
    }

    public AutoStartWorker(@NonNull Context context,@NonNull WorkerParameters workerParams)
    {
        super(context,workerParams);
        mWorkerParameters = workerParams;
        mContext = context.getApplicationContext();
    }
}