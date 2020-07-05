package com.expert.cleanup.nets.okhttp.Https1Authentications;

import android.util.Log;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/***************因饿汉式双重检查锁有缺陷，因此暂用此模式************/
public class DoubleVerificateHttpsFactory extends VerificateHttpsImp
{
    private DoubleVerificateHttpsFactory(){}
    public static DoubleVerificateHttpsFactory mInstance;

    public static synchronized DoubleVerificateHttpsFactory getInstance()
    {
        if(mInstance == null)
            mInstance = new DoubleVerificateHttpsFactory();
        return mInstance;
    }

    @Override
    /*************************************获取OkHttpClient中TLS/SSL的自定义双向验证工厂***********************************/
    public SSLSocketFactory getSslSocketFactory(String keyPassWord, String keyStorePassWord, InputStream bksInputStream, InputStream... cerInputStreams)
    {
        try
        {
            KeyStore bksKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            bksKeyStore.load(bksInputStream,keyPassWord.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(bksKeyStore,keyStorePassWord.toCharArray());
            /***********************************************************************************************************/
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore cerKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            cerKeyStore.load(null);
            for(int index = 0;index < cerInputStreams.length;index++)
            {
                String certificateAlias = Integer.toString(index);
                cerKeyStore.setCertificateEntry(certificateAlias,certificateFactory.generateCertificate(cerInputStreams[index]));
                try
                {
                    if(null != cerInputStreams[index])
                    {
                        cerInputStreams[index].close();
                    }
                }
                catch(Exception e)
                {
                    Log.i("DoubleVerificate",e.getStackTrace().toString());
                }
            }
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(cerKeyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(),trustManagerFactory.getTrustManagers(),new SecureRandom());
            return sslContext.getSocketFactory();
        }
        catch (Exception e)
        {
            Log.i("DoubleVerificate",e.getStackTrace().toString());
        }
        return null;
    }

    /**********************************************************************************************************************调用DEMO*****************************************************************************************************************************/
   // SSLSocketFactory  sslSocketFactory = DoubleVerificateHttpsFactory.getInstance().getSslSocketFactory(BKS证书库中证书的密码,BKS证书库的密码,Context.getAssets().open("xxx.bks")BKS证书库的数据流,new Buffer().writeUtf8("CER证书的字符串内容").inputStream());
}