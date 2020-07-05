package com.expert.cleanup.nets.okhttp.Http2CookieJars;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;

/**用Sp对Cookie做本地持久化处理的工具类*/
public  class Cookie_PersistentStoreTools
{
    /***在硬盘中存储Cookie所用的SharedPreferences****/
    private final SharedPreferences mCookies_Sp;
    /******在运行内存中存储Cookie所用的双重集合******/
    private final Map<String, ConcurrentHashMap<String, Cookie>> mCookies;
    /********************************************************************/
    /*************在硬盘中存储Cookie所用SharedPreferences的名字**********/
    private static final String COOKIE_SP = "cookie_sp";
    private static final String LOG_TAG = "Cookie_PersistentStoreTools".substring(0,23);

    /**********初始化此方案时就加载本地持久化数据*********/
    public Cookie_PersistentStoreTools(Context context)
    {
        mCookies = new HashMap<>();
        mCookies_Sp = context.getSharedPreferences(COOKIE_SP, Context.MODE_PRIVATE);
        /*************************将硬盘的Cookies缓存到运行内存中******************/
        Map<String, ?> cookiesSpMap = mCookies_Sp.getAll();
        for (Map.Entry<String, ?> entry : cookiesSpMap.entrySet())
        {
            String[] cookieNames = TextUtils.split((String) entry.getValue(), ",");
            for (String cookieName : cookieNames)
            {
                String encodedCookie = mCookies_Sp.getString(cookieName, null);
                if (encodedCookie != null)
                {
                    Cookie decodedCookie = decodeCookie(encodedCookie);
                    if (decodedCookie != null)
                    {
                        if (!mCookies.containsKey(entry.getKey()))
                            mCookies.put(entry.getKey(), new ConcurrentHashMap<String, Cookie>());
                        mCookies.get(entry.getKey()).put(cookieName, decodedCookie);
                    }
                }
            }
        }
    }

    /***************获取mCookies中Map的键名Key************/
    protected String getCookieToken(Cookie cookie)
    {
        return cookie.name() + "@" + cookie.domain();
    }

    /**********添加cookie到缓存并做本地持久化处理*********/
    public void add(HttpUrl url, Cookie cookie)
    {
        /**如果cookie过期则更新此cookie到缓存,再将mCookies做本地持久化处理**/
        String key = getCookieToken(cookie);
        if (!cookie.persistent())
        {
            if (!mCookies.containsKey(url.host()))
                mCookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
            mCookies.get(url.host()).put(key, cookie);
        }
        else
        {
            if (mCookies.containsKey(url.host()))
                mCookies.get(url.host()).remove(key);
        }
        /***********************将Cookies持久化到本地硬盘中******************/
        SharedPreferences.Editor cookiesSpEdit = mCookies_Sp.edit();
        cookiesSpEdit.putString(url.host(), TextUtils.join(",", mCookies.get(url.host()).keySet()));
        cookiesSpEdit.putString(key, encodeCookie(new Cookie_PersistentStoreObj(cookie)));
        cookiesSpEdit.apply();
    }

    /**********删除相关cookie的运行缓存和本地缓存*********/
    public boolean remove(HttpUrl url, Cookie cookie)
    {
        String key = getCookieToken(cookie);
        if (mCookies.containsKey(url.host()) && mCookies.get(url.host()).containsKey(key))
        {
            mCookies.get(url.host()).remove(key);
            SharedPreferences.Editor cookiesSpEdit = mCookies_Sp.edit();
            if (mCookies_Sp.contains(key))
                cookiesSpEdit.remove(key);
            cookiesSpEdit.putString(url.host(), TextUtils.join(",", mCookies.get(url.host()).keySet()));
            cookiesSpEdit.apply();
            return true;
        }
        else
            return false;
    }

    /**********删除所有cookie的运行缓存和本地缓存*********/
    public boolean removeAll()
    {
        SharedPreferences.Editor prefsWriter = mCookies_Sp.edit();
        prefsWriter.clear();
        prefsWriter.apply();
        mCookies.clear();
        return true;
    }

    /************获取全部url的所有cookies(集合)***********/
    public List<Cookie> getCookiesForAll()
    {
        ArrayList<Cookie> result = new ArrayList<>();
        for (String key : mCookies.keySet())
            result.addAll(mCookies.get(key).values());
        return result;
    }

    /************获取指定url的所有cookies(集合)***********/
    public List<Cookie> getCookiesForUrl(HttpUrl url)
    {
        ArrayList<Cookie> result = new ArrayList<>();
        if (mCookies.containsKey(url.host()))
            result.addAll(mCookies.get(url.host()).values());
        return result;
    }

    /**************二进制数组转十六进制字符串*************/
    protected String byteArrayToHexString(byte[] bytes)
    {
        StringBuilder stringBuilder = new StringBuilder(bytes.length * 2);
        for (byte element : bytes)
        {
            int value = element & 0xff;
            if (value < 16)
                stringBuilder.append('0');
            stringBuilder.append(Integer.toHexString(value));
        }
        return stringBuilder.toString().toUpperCase(Locale.US);
    }

    /**************十六进制字符串转二进制数组*************/
    protected byte[] hexStringToByteArray(String hexString)
    {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int index = 0; index < len; index += 2)
            data[index / 2] = (byte) ((Character.digit(hexString.charAt(index), 16) << 4) + Character.digit(hexString.charAt(index + 1), 16));
        return data;
    }

    /****************Action：cookie序列化成String***************
     ****************@param cookie 要序列化的cookie************
     ****************@return 序列化之后的String***************/
    protected String encodeCookie(Cookie_PersistentStoreObj cookie)
    {
        if (cookie == null) return null;
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        try
        {
            ObjectOutputStream objectOS = new ObjectOutputStream(byteArrayOS);
            objectOS.writeObject(cookie);
        }
        catch (IOException e)
        {
            Log.d(LOG_TAG,"IOException in encodeCookie", e);
            return null;
        }
        return byteArrayToHexString(byteArrayOS.toByteArray());
    }

    /****************Action：String反序列化成cookie**************
     ****************@param cookieString 反序列化的String*******
     ****************@return 反序列化之后的cookie***************/
    protected Cookie decodeCookie(String cookieString)
    {
        Cookie cookie = null;
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((Cookie_PersistentStoreObj) objectInputStream.readObject()).getCookies();
        }
        catch (IOException e)
        {
            Log.w(LOG_TAG, "IOException in decodeCookie", e);
        }
        catch (ClassNotFoundException e)
        {
            Log.w(LOG_TAG, "ClassNotFoundException in decodeCookie", e);
        }
        return cookie;
    }
}