package com.expert.cleanup.nets.okhttp.Https1Authentications;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**********************默认X509TrustManager实现类******************/
public class Defaulted_X509TrustManager implements X509TrustManager
{
    @Override
    public X509Certificate[] getAcceptedIssuers()
    {
        return new X509Certificate[0];
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException
    {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException
    {
    }
}