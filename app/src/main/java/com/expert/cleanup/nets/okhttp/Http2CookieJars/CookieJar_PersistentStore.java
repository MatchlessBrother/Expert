package com.expert.cleanup.nets.okhttp.Http2CookieJars;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/*******Cookie不仅存在于运行内存中,还存在于本地硬盘中*******/

/********App关闭后Cookie永不消失,App卸载之后才会消失********/
public class CookieJar_PersistentStore implements CookieJar
{
    private Context mContext;
    private Cookie_PersistentStoreTools mPersistentStoreTools;

    public CookieJar_PersistentStore(Context context)
    {
        mContext = context;
        mPersistentStoreTools = new Cookie_PersistentStoreTools(mContext);
    }

    @Override
    /**************直接将所有cookie存储于本地硬盘Sp中*************/
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies)
    {
        if(null != cookies && cookies.size() > 0)
        {
            for(Cookie cookie : cookies)
            {
                mPersistentStoreTools.add(url,cookie);
            }
        }
    }

    @Override
    /****************直接从本地硬盘Sp中读取所有cookie*************/
    public List<Cookie> loadForRequest(HttpUrl url)
    {
        return mPersistentStoreTools.getCookiesForUrl(url);
    }
}