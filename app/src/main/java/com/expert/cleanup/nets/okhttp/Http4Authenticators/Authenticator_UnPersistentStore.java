package com.expert.cleanup.nets.okhttp.Http4Authenticators;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/*****************Authenticator只存在于运行内存中*******************/

/******************App关闭后Authenticator自动消失*******************/
public class Authenticator_UnPersistentStore implements Authenticator
{
    /****************************用户账号**************************/
    private volatile String mUserName;
    /****************************用户密码**************************/
    private volatile String mUserPassword;
    /****************************单例模式**************************/
    private static volatile Authenticator_UnPersistentStore mInstance;

    /*******************************单例模式******************************/
    public synchronized static Authenticator_UnPersistentStore getInstance()
    {
        if(null == mInstance)
            mInstance = new Authenticator_UnPersistentStore();
        return mInstance;
    }

    private Authenticator_UnPersistentStore()
    {
       mUserName = "";
       mUserPassword = "";
    }

    public String getUserName()
    {
        return (null != mUserName ? mUserName.trim() : "");

    }

    public String getUserPassword()
    {
        return (null != mUserPassword ? mUserPassword.trim() : "");

    }

    public void clearAuthenticator()
    {
        setUserNameAndPassword("","");

    }

    public void setUserName(String userName)
    {
        mUserName = (null != userName ? userName.trim() : "");
    }

    public void setUserPassword(String userPassword)
    {
        mUserPassword = (null != userPassword ? userPassword.trim() : "");
    }

    public void setUserNameAndPassword(String userName, String userPassword)
    {
        setUserName(userName);
        setUserPassword(userPassword);
    }

    /**********给请求数据添加Authorization请求头,以便通过服务器的身份验证*********/
    public Request authenticate(Route route, Response response) throws IOException
    {
        if (response.request().header("Authorization") != null)
            return null;
        if(null == mUserName || mUserName.trim().equals("") || null == mUserPassword || mUserPassword.trim().equals(""))
            return  null;
        return response.request().newBuilder().header("Authorization",Credentials.basic(mUserName,mUserPassword)).build();
    }
}