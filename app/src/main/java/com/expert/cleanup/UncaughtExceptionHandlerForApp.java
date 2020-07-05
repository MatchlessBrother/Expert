package com.expert.cleanup;

import java.io.File;
import java.io.Writer;
import java.util.Date;
import android.util.Log;
import android.os.Looper;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.StringWriter;
import android.content.Intent;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.io.FileNotFoundException;
import com.expert.cleanup.acts.MainActivity;
import com.expert.cleanup.nets.util.MemoryUtils;

public class UncaughtExceptionHandlerForApp implements Thread.UncaughtExceptionHandler
{
    private BaseApp mApplication;
    private String mExceptionalFilesBestStorePath;
    private static UncaughtExceptionHandlerForApp mHandler;

    private UncaughtExceptionHandlerForApp()
    {

    }

    public static UncaughtExceptionHandlerForApp getInstance()
    {
        if (mHandler == null)
            mHandler = new UncaughtExceptionHandlerForApp();
        return mHandler;
    }

    public void registerUncaughtExceptionHandler(BaseApp application)
    {
        mApplication = application;
        Thread.setDefaultUncaughtExceptionHandler(mHandler);
        mExceptionalFilesBestStorePath = MemoryUtils.getBestFilesPath(mApplication) +  File.separator + "exceptionalLogs";
    }

    public void uncaughtException(Thread thread, Throwable ex)
    {
       /**把异常的详细信息打印在控制台和Logcat面板上*/
       System.out.println(getParticularInfos(ex));
       Log.e("ProgramCrashes", getParticularInfos(ex));
       createExceptionalReportFile(getParticularInfos(ex));
       new Thread(new Runnable()
       {
           public void run()
           {
               Looper.prepare();
               /**这是简略的崩溃提示框*******************************************************************************************************************************************************/
               //showCrashDialog("My God,The program is wrong!Please restart it.",R.color.black,"Ok,I see.", R.color.black);
               /**这是详细的崩溃提示框*******************************************************************************************************************************************************/
               //showCrashDialog("异常提示：",R.color.white,R.color.crashdialog_titlebgcolor, "亲，网络发生异常啦！请您重新启动程序。\n由此给您带来的不便请敬请谅解，谢谢。", R.color.black,"知道了", R.color.crashdialog_btnstrcolor);
               mApplication.finishAllActivity();/************************/
               Intent intent = new Intent(mApplication,MainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);/***********/
               intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);/**********/
               mApplication.startActivity(intent); /**********************/
               android.os.Process.killProcess(android.os.Process.myPid());
               System.exit(1);
               Looper.loop();
           }
        }).start();
    }

    /********************************获取程序崩溃异常的详细信息，以便准确捕获问题所在，初步感觉异常的封装是一层一层的，要逐步解封才能得到最完善和最准确的崩溃信息详情****************************/
    private String getParticularInfos(Throwable ex)
    {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        printWriter.close();
        String result = writer.toString();
        return result;
    }

    public void createExceptionalReportFile(String exceptionStr)
    {
        File exceptionalFile = new File(mExceptionalFilesBestStorePath);
        if(!exceptionalFile.exists())
            exceptionalFile.mkdirs();

        exceptionalFile = new File(mExceptionalFilesBestStorePath + File.separator + new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date()) +".txt");
        FileOutputStream exceptionalFileOutputStream = null;
        try
        {
            exceptionalFileOutputStream = new FileOutputStream(exceptionalFile);
            exceptionalFileOutputStream.write(exceptionStr.getBytes());
            exceptionalFileOutputStream.close();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}