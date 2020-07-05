package com.expert.cleanup.nets.retrofit.Http1BaseConfig;

import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**快捷创建Retrofit的工厂**/
public class RetrofitFactory
{
    /********************************创建半自定义配置的Retrofit***************************/
    public static Retrofit create(OkHttpClient okHttpClient, String baseUrl)
    {
        Retrofit.Builder builder = new Retrofit.Builder();
        /**************************设置Retrofit的OkHttpClient属性*************************/
        if(null != okHttpClient)
            builder.client(okHttpClient);
        /*****************************设置Retrofit的BaseUrl属性***************************/
        if(null != baseUrl && !baseUrl.trim().equals(""))
            builder.baseUrl(baseUrl);
        /***************************设置Retrofit响应数据的转换工厂************************/
        builder.addConverterFactory(GsonConverterFactory.create());
        /****************************设置Retrofit可支持的编码工厂*************************/
        builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return builder.build();
    }

    /********************************创建半自定义配置的Retrofit***************************/
    public static Retrofit create(OkHttpClient okHttpClient, String baseUrl, List<Converter.Factory> converterFactories, List<CallAdapter.Factory> callAdapterFactories)
    {
        Retrofit.Builder builder = new Retrofit.Builder();
        /**************************设置Retrofit的OkHttpClient属性*************************/
        if(null != okHttpClient)
            builder.client(okHttpClient);
        /*****************************设置Retrofit的BaseUrl属性***************************/
        if(null != baseUrl && !baseUrl.trim().equals(""))
            builder.baseUrl(baseUrl);
        /***************************设置Retrofit响应数据的转换工厂************************/
        if(null != converterFactories && converterFactories.size() > 0)
        {
            for(Converter.Factory factory : converterFactories)
                builder.addConverterFactory(factory);
        }
        else
            builder.addConverterFactory(GsonConverterFactory.create());
        /****************************设置Retrofit可支持的编码工厂*************************/
        if(null != callAdapterFactories && callAdapterFactories.size() > 0)
        {
            for(CallAdapter.Factory factory : callAdapterFactories)
                builder.addCallAdapterFactory(factory);
        }
        else
            builder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        return builder.build();
    }
}