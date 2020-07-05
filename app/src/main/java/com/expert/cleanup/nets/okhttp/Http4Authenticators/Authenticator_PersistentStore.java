package com.expert.cleanup.nets.okhttp.Http4Authenticators;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

/*******Authenticator不仅存在于运行内存中,还存在于本地硬盘中*******/

/********App关闭后Authenticator永不消失,App卸载之后才会消失********/
public class Authenticator_PersistentStore implements Authenticator
{
    /**************************************************************/
    private Context mContext;
    /****************************用户账号**************************/
    private volatile String mUserName;
    /****************************用户密码**************************/
    private volatile String mUserPassword;
    /****************************单例模式**************************/
    private static volatile Authenticator_PersistentStore mInstance;
    private static final String NAME_SP = "authenticator_sharedpreferences";

    /**************************************单例模式*************************************/
    public synchronized static Authenticator_PersistentStore getInstance(Context context)
    {
        if(null == mInstance)
            mInstance = new Authenticator_PersistentStore(context);
        return mInstance;
    }

    private Authenticator_PersistentStore(Context context)
    {
        mContext = context;
        mUserName = getAuthenticatorOfName(mContext);
        mUserPassword = getAuthenticatorOfPassword(mContext);
    }

    public String getUserName()
    {
        mUserName = getAuthenticatorOfName(mContext);
        return (null != mUserName ? mUserName.trim() : "");
    }

    public String getUserPassword()
    {
        mUserPassword = getAuthenticatorOfPassword(mContext);
        return (null != mUserPassword ? mUserPassword.trim() : "");
    }

    public void clearAuthenticator()
    {
        setUserNameAndPassword("","");

    }

    public void setUserName(String userName)
    {
        mUserName = (null != userName ? userName.trim() : "");
        setAuthenticatorOfName(mContext,mUserName);
    }

    public void setUserPassword(String userPassword)
    {
        mUserPassword = (null != userPassword ? userPassword.trim() : "");
        setAuthenticatorOfPassword(mContext,mUserPassword);
    }

    public void setUserNameAndPassword(String userName, String userPassword)
    {
       setUserName(userName);
       setUserPassword(userPassword);
    }

    /*************给请求数据添加Authorization请求头,以便通过服务器的身份验证************/
    public Request authenticate(Route route, Response response) throws IOException
    {
        if (response.request().header("Authorization") != null)
            return null;
        if(null == mUserName || mUserName.trim().equals("") || null == mUserPassword || mUserPassword.trim().equals(""))
            return  null;
        return response.request().newBuilder().header("Authorization", Credentials.basic(mUserName,mUserPassword)).build();
    }

    /********************将AuthenticatorOfName存储在Sharepreference里面*****************/
    public void setAuthenticatorOfName(Context context, String authenticatorOfName)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME_SP, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("authenticatorofname", null != authenticatorOfName ? authenticatorOfName.trim() : "").apply();
    }

    /*********************将AuthenticatorOfName从Sharepreference里面取出****************/
    public String getAuthenticatorOfName(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME_SP, Context.MODE_PRIVATE);
        return sharedPreferences.getString("authenticatorofname","");
    }

    /******************将AuthenticatorOfPassword存储在Sharepreference里面***************/
    public void setAuthenticatorOfPassword(Context context, String AuthenticatorOfPassword)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME_SP, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("authenticatorofpassword", null != AuthenticatorOfPassword ? AuthenticatorOfPassword.trim() : "").apply();
    }

    /*******************将AuthenticatorOfPassword从Sharepreference里面取出**************/
    public String getAuthenticatorOfPassword(Context context)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(NAME_SP, Context.MODE_PRIVATE);
        return sharedPreferences.getString("authenticatorofpassword","");
    }
}