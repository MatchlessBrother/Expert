package com.expert.cleanup.nets.client;

import java.util.List;
import retrofit2.Retrofit;
import okhttp3.Interceptor;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import android.content.Context;
import com.expert.cleanup.nets.okhttp.Http1BasicConfig.OkhttpClientFactory;
import com.expert.cleanup.nets.okhttp.Http3Interceptions.RetrysInterceptor;
import com.expert.cleanup.nets.retrofit.Http1BaseConfig.RetrofitFactory;

public class NetClient
{
    private NetAllUrl mNetAllUrl;
    private volatile Retrofit mRetrofit;
    private static NetClient mNetClient;
    private volatile OkHttpClient mOkHttpClient;

    public synchronized static NetClient getInstance(Context context)
    {
        if(null == mNetClient)
            mNetClient = new NetClient(context);
        return mNetClient;
    }

    private NetClient(Context context)
    {
        List<Interceptor> appInterceptorList = new ArrayList<Interceptor>();
        appInterceptorList.add(new RetrysInterceptor(3,6000,8000));/*******/
        mOkHttpClient = OkhttpClientFactory.create(context,30,30,30,"",0,appInterceptorList,null);
        mRetrofit = RetrofitFactory.create(mOkHttpClient,NetAllUrl.SERVICE_URL);/****************/
    }

    public NetAllUrl getNetAllUrl()
    {
        if(null == mNetAllUrl)
            mNetAllUrl = mRetrofit.create(NetAllUrl.class);
        return mNetAllUrl;
    }

    public Retrofit getRetrofit()
    {
        return mRetrofit;

    }

    public OkHttpClient getOkHttpClient()
    {
        return mOkHttpClient;

    }

    public void setRetrofit(Retrofit retrofit)
    {
        mRetrofit = retrofit;

    }

    public void setOkHttpClient(OkHttpClient okHttpClient)
    {
        mOkHttpClient = okHttpClient;

    }
}