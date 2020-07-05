package com.expert.cleanup.nets.okhttp.Http3Interceptions;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/***************自定义重连服务器拦截器***************/
public class RetrysInterceptor implements Interceptor
{
    /***********最大重连次数**********/
    private Integer mMaxNum;
    /***********当前重连次数**********/
    /****首次连接不包含在重连次数中***/
    private Integer mCurrentNum = 0;
    /*****首次重连延迟时间(毫秒)******/
    private long mDelayTime = 1000;
    /*****再次重连叠加延迟时间(毫秒)**/
    private long mIncreaseDelayTime = 2000;

    public RetrysInterceptor(Integer mMaxNum)
    {
       this(mMaxNum,0);

    }

    public RetrysInterceptor(Integer mMaxNum, long mDelayTime)
    {
        this(mMaxNum,mDelayTime,0);

    }

    public RetrysInterceptor(Integer mMaxNum, long mDelayTime, long mIncreaseDelayTime)
    {
        this.mMaxNum = mMaxNum;
        this.mDelayTime = mDelayTime;
        this.mIncreaseDelayTime = mIncreaseDelayTime;
    }

    public Response intercept(Chain chain) throws IOException
    {
        Request request = chain.request();
        Response response = chain.proceed(request);
        /*************************请求失败**********************/
        while(!response.isSuccessful() && mCurrentNum < mMaxNum)
        {
            try
            {
                Thread.sleep(mDelayTime + mCurrentNum++ * mIncreaseDelayTime);
                response = chain.proceed(request.newBuilder().build());
            }
            catch (InterruptedException e)
            {
                Log.i("RetrysInterceptor",e.getStackTrace().toString());
            }
        }
        return response;
    }
}