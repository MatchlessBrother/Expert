package com.expert.cleanup.nativetos;

import java.io.File;
import android.os.StatFs;
import android.os.Environment;
import android.content.Context;
import android.app.ActivityManager;

import com.expert.cleanup.acts.BoostingActivity;
import com.expert.cleanup.acts.CleaningActivity;

import static com.expert.cleanup.acts.BoostActivity.getNextBoostTime;
import static com.expert.cleanup.acts.CleanActivity.getNextCleanTime;

public class MemoryTool
{
    /**
     * 显示SD卡的可用和总容量
     */
    public static long[] getSDTotalAvailable()
    {
        long[] sizes = new long[2];
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
        {
            File file = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(file.getPath());
            long blockSize = statFs.getBlockSizeLong();
            long totalBlocks = statFs.getBlockCountLong();
            long availableBlocks = statFs.getAvailableBlocksLong();
            sizes[0] = totalBlocks * blockSize;
            sizes[1] = availableBlocks * blockSize;
        }
        else
        {
            sizes[0] = 1;
            sizes[1] = 0;
        }
        return sizes;
    }

    /**
     * 显示ROM的可用和总容量
     */
    public static long[] getROMTotalAvailable(Context context)
    {
        long[] sizes = new long[2];
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSize = statFs.getBlockSizeLong();
        long totalBlocks = statFs.getBlockCountLong();
        long availableBlocks = statFs.getAvailableBlocksLong();
        sizes[0] = totalBlocks * blockSize;
        if(getNextCleanTime(context) > System.currentTimeMillis())
            sizes[1] = availableBlocks * blockSize + CleaningActivity.getCleanSize(context);
        else
            sizes[1] = availableBlocks * blockSize;
        return sizes;
    }

    /**
     * 显示RAM的可用和总容量
     */
    public static long[] getRAMTotalAvailable(Context context)
    {
        long[] sizes = new long[2];
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        sizes[0] = mi.totalMem;
        if(getNextBoostTime(context) > System.currentTimeMillis())
            sizes[1] = mi.availMem + BoostingActivity.getBoostSize(context);
        else
            sizes[1] = mi.availMem;
        return sizes;
    }
}