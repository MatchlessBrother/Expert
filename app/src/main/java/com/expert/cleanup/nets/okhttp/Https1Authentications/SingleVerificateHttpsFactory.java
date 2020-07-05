package com.expert.cleanup.nets.okhttp.Https1Authentications;

import android.util.Log;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/***************因饿汉式双重检查锁有缺陷，因此暂用此模式************/
public class SingleVerificateHttpsFactory extends VerificateHttpsImp
{
    private SingleVerificateHttpsFactory(){}
    public static SingleVerificateHttpsFactory mInstance;

    public static synchronized SingleVerificateHttpsFactory getInstance()
    {
        if(mInstance == null)
            mInstance = new SingleVerificateHttpsFactory();
        return mInstance;
    }

    @Override
    /**************获取OkHttpClient中TLS/SSL的自定义单向验证工厂**************/
    public SSLSocketFactory getSslSocketFactory(InputStream... cerInputStreams)
    {
        try
        {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            for(int index = 0;index < cerInputStreams.length;index++)
            {
                String certificateAlias = Integer.toString(index);
                keyStore.setCertificateEntry(certificateAlias,certificateFactory.generateCertificate(cerInputStreams[index]));
                try
                {
                    if(null != cerInputStreams[index])
                    {
                        cerInputStreams[index].close();
                    }
                }
                catch(Exception e)
                {
                    Log.i("SingleVerificate",e.getStackTrace().toString());
                }
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null,trustManagerFactory.getTrustManagers(),new SecureRandom());
            return sslContext.getSocketFactory();
        }
        catch (Exception e)
        {
            Log.i("SingleVerificate",e.getStackTrace().toString());
        }
        return null;
    }

    /*************************************************************************调用DEMO*******************************************************************************/
    //SSLSocketFactory  sslSocketFactory = SingleVerificateHttpsFactory.getInstance().getSslSocketFactory(new Buffer().writeUtf8("CER证书的字符串内容").inputStream());
}