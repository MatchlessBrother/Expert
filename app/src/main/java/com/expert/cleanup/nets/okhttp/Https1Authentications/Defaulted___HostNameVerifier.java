package com.expert.cleanup.nets.okhttp.Https1Authentications;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/***********************默认HostNameVerifier实现类*******************/
public class Defaulted___HostNameVerifier implements HostnameVerifier
{
    /***发起Https请求时不去验证Url的主机名和Url的Ip地址***/
    public boolean verify(String s, SSLSession sslSession)
    {
        return true;
    }
}