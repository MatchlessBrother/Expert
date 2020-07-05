package com.expert.cleanup.nets.okhttp.Https1Authentications;

import java.io.InputStream;

import javax.net.ssl.SSLSocketFactory;

public abstract class VerificateHttpsImp
{
    /****************获取OkHttpClient中TLS/SSL的自定义无需验证工厂***************/
    public SSLSocketFactory getSslSocketFactory()
    {
        return null;
    }

    /****************获取OkHttpClient中TLS/SSL的自定义单向验证工厂***************/
    protected SSLSocketFactory getSslSocketFactory(InputStream... cerInputStreams)
    {
        return null;
    }

    /****************获取OkHttpClient中TLS/SSL的自定义双向验证工厂***************/
    protected SSLSocketFactory getSslSocketFactory(String keyPassWord, String keyStorePassWord, InputStream bksInputStream, InputStream... cerInputStreams) {
        return null;
    }
}