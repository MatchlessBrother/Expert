package com.expert.cleanup.nets.okhttp.Https1Authentications;

import android.util.Log;

import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

/************因饿汉式双重检查锁有缺陷，因此暂用此模式***********/
public class UnVerificateHttpsFactory extends VerificateHttpsImp
{
    private UnVerificateHttpsFactory(){}
    public static UnVerificateHttpsFactory mInstance;

    public static synchronized UnVerificateHttpsFactory getInstance()
    {
        if(mInstance == null)
            mInstance = new UnVerificateHttpsFactory();
        return mInstance;
    }

    @Override
    /**************获取OkHttpClient中TLS/SSL的自定义无需验证工厂**************/
    public SSLSocketFactory getSslSocketFactory()
    {
        try
        {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,  new TrustManager[] { new Defaulted_X509TrustManager() }, new SecureRandom());
            return sslContext.getSocketFactory();
        }
        catch (Exception e)
        {
            Log.i("UnVerificate",e.getStackTrace().toString());
        }
        return null;
    }

    /********************************************调用DEMO********************************************/
    //SSLSocketFactory sslSocketFactory = UnVerificateHttpsFactory.getInstance().getSslSocketFactory();
}