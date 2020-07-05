package com.expert.cleanup.nets.okhttp.Http1BasicConfig;

import android.content.Context;

import com.expert.cleanup.nets.okhttp.Http3Interceptions.LogInterceptor;
import com.expert.cleanup.nets.okhttp.Http3Interceptions.RetrysInterceptor;
import com.expert.cleanup.nets.okhttp.Https1Authentications.Defaulted_X509TrustManager;
import com.expert.cleanup.nets.okhttp.Https1Authentications.Defaulted___HostNameVerifier;
import com.expert.cleanup.nets.okhttp.Https1Authentications.UnVerificateHttpsFactory;
import com.expert.cleanup.nets.util.BuildVariantsUtils;
import com.expert.cleanup.nets.util.MemoryUtils;
import com.expert.cleanup.nets.util.StringUtils;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**快捷创建OkHttpClient的工厂**/
public class OkhttpClientFactory
{
    /***********创建默认配置的OkHttpClient***********/
    public static OkHttpClient create(Context context)
    {
        /******************************设置OkHttpClient各种超时的时限******************************/
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(8, TimeUnit.SECONDS);
        builder.writeTimeout(8, TimeUnit.SECONDS);
        builder.connectTimeout(6, TimeUnit.SECONDS);
        /***************************设置OkHttpClient网络缓存的路径和大小***************************/
        String cachePath = null;
        if(MemoryUtils.whetherHasTheSDcard())
            cachePath = context.getExternalFilesDir(context.getPackageName() + "_NetCache").toString();
        else
            cachePath = context.getFilesDir().toString() + File.separator + context.getPackageName() +"_NetCache";
        File file  = new File(cachePath);
        if(!file.exists()) file.mkdirs();
        builder.cache(new Cache(file,18 *1024 *1024));
        /*******************************给OkHttpClient添加应用拦截器*******************************/
        builder.addInterceptor(new RetrysInterceptor(2,1000,2000));
        /*******************************给OkHttpClient添加网络拦截器*******************************/
        if(BuildVariantsUtils.isDebug(context))
        {
            LogInterceptor logInterceptor = new LogInterceptor();
            logInterceptor.setLevel(LogInterceptor.Level.BODY);
            builder.addNetworkInterceptor(logInterceptor);
        }
        return builder.sslSocketFactory(UnVerificateHttpsFactory.getInstance().getSslSocketFactory(),
        new Defaulted_X509TrustManager()).hostnameVerifier(new Defaulted___HostNameVerifier()).build();
    }

    /*********创建半自定义配置的OkHttpClient*********/
    public static OkHttpClient create(Context context, int connectTimeout, int readTimeout, int writeTimeout, String cachePath, long cacheSize, List<Interceptor> appInterceptors, List<Interceptor> netInterceptors)
    {
        /******************************设置OkHttpClient各种超时的时限******************************/
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(readTimeout > 0 ? readTimeout : 8, TimeUnit.SECONDS);
        builder.writeTimeout(writeTimeout > 0 ? writeTimeout : 8, TimeUnit.SECONDS);
        builder.connectTimeout(connectTimeout > 0 ? connectTimeout : 6, TimeUnit.SECONDS);
        /***************************设置OkHttpClient网络缓存的路径和大小***************************/
        if(StringUtils.isEmpty(cachePath.trim()))
        {
            if(MemoryUtils.whetherHasTheSDcard())
                cachePath = context.getExternalFilesDir(context.getPackageName() + "_NetCache").toString();
            else
                cachePath = context.getFilesDir().toString() + File.separator + context.getPackageName() +"_NetCache";
        }
        File file  = new File(cachePath);
        if(!file.exists()) file.mkdirs();
        if(cacheSize <= 0) cacheSize = 18 *1024 *1024;
        builder.cache(new Cache(file,cacheSize));
        /*******************************给OkHttpClient添加应用拦截器*******************************/
        if(null != appInterceptors && appInterceptors.size() > 0)
        {
            for(Interceptor interceptor : appInterceptors)
                builder.addInterceptor(interceptor);
        }
        else
            builder.addInterceptor(new RetrysInterceptor(2,1000,2000));
        /*******************************给OkHttpClient添加网络拦截器*******************************/
        if(BuildVariantsUtils.isDebug(context))
        {
            if(null != netInterceptors && netInterceptors.size() > 0)
            {
                for(Interceptor interceptor : netInterceptors)
                    builder.addNetworkInterceptor(interceptor);
            }
            else
            {
                LogInterceptor logInterceptor = new LogInterceptor();
                logInterceptor.setLevel(LogInterceptor.Level.BODY);
                builder.addNetworkInterceptor(logInterceptor);
            }
        }
        return builder.sslSocketFactory(UnVerificateHttpsFactory.getInstance().getSslSocketFactory(),
        new Defaulted_X509TrustManager()).hostnameVerifier(new Defaulted___HostNameVerifier()).build();
    }
}