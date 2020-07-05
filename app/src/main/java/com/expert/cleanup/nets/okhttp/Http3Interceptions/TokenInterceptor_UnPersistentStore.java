package com.expert.cleanup.nets.okhttp.Http3Interceptions;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**********************Token只存在于运行内存中***********************/

/***********************App关闭后Token自动消失***********************/
public class TokenInterceptor_UnPersistentStore implements Interceptor
{
    /***********************Token失效时,自动获取新Token并重新发起请求的最大次数********************/
    private Integer mMaxNumOfTryOn = 1;
    /***********************************在运行内存中存储Token所用的集合****************************/
    private Map<String, String> mDataMap = null;
    /*********************************************单例模式*****************************************/
    private volatile static TokenInterceptor_UnPersistentStore mInstance = null;
    /**********************************更新Token时使用的具体逻辑类*********************************/
    private TokenInterceptor_UpdateTokenInterface mUpdateTokenImp = new TokenInterceptor_UpdateTokenInterface()
    {
        public String getNewestTokenStr()
        {
            return "";
        }

        public Boolean whetherTheTokenIsExpired(Response response)
        {
            return false;
        }
    };

    /********************************************单例模式******************************************/
    public synchronized static TokenInterceptor_UnPersistentStore getInstance()
    {
        if(null == mInstance)
            mInstance = new TokenInterceptor_UnPersistentStore();
        return mInstance;
    }

    private TokenInterceptor_UnPersistentStore()
    {
        mDataMap = new ConcurrentHashMap<String, String>();
    }

    /**********************************************************************************************/
    /**********************************************************************************************/
    /***********************得到Token失效时自动获取新Token并重新发起请求的最大次数*****************/
    public Integer getMaxNumOfTryOn()
    {
        return mMaxNumOfTryOn;

    }

    /******************************将Token从运行缓存（mDataMap）中取出*****************************/
    public String getToken(String url)
    {
        return mDataMap.containsKey(url) ? mDataMap.get(url) : "";
    }

    /**************************************更新Token的缓存数据*************************************/
    public void updateToken(String host, String token)
    {
        setToken(host.trim(),token.trim());

    }

    /**********************设置Token失效时自动获取新Token并重新发起请求的最大次数******************/
    public void setMaxNumOfTryOn(Integer maxNumOfTryOn)
    {
        mMaxNumOfTryOn = maxNumOfTryOn;

    }

    /******************************设置更新Token时所使用的具体逻辑类*******************************/
    public void setUpdateTokenImp(TokenInterceptor_UpdateTokenInterface updateTokenImp)
    {
        mUpdateTokenImp = updateTokenImp;
    }

    /**********************************************************************************************/
    /**********************************************************************************************/
    /**************************************更新Token的缓存数据*************************************/
    private void updateToken(Response response)
    {
        Headers headers = response.headers();
        for(int index = 0;index < headers.size();index++)
        {
            String headerName = headers.name(index);
            if(headerName.toLowerCase().equals("Token".toLowerCase()))
                setToken(response.request().url().host(),headers.value(index));
        }
    }

    /******************************将Token存储在运行缓存（mDataMap）里面***************************/
    private void setToken(String url, String token)
    {
        url = null != url ? url.trim() : "";
        token = null != token ? token.trim() : "";
        mDataMap.put(url,token);
    }

    @Override
    public Response intercept(Chain chain) throws IOException
    {
        Response response = null;
        Request request = chain.request();
        /************************************添加Token到请求头中，以便通过服务器验证************************************/
        if(mDataMap.containsKey(request.url().host()) && !getToken(request.url().host()).trim().equals(""))
            response = chain.proceed(request.newBuilder().header("Token",getToken(request.url().host())).build());
        else
            response = chain.proceed(request);
        /**************************************保存Token到运行缓存中，以便下次使用**************************************/
        response = updateTokenProcess(chain,request,response,mMaxNumOfTryOn);
        return response;
    }

    /***********************************更新Token缓存数据的具体流程********************************/
    private Response updateTokenProcess(Chain chain, Request request , Response response, Integer tryOnMaxNum) throws IOException
    {
        if(!response.isSuccessful())
        {
            if(mUpdateTokenImp.whetherTheTokenIsExpired(response) && tryOnMaxNum > 0)
            {
                /********从服务器获取最新Token字符串的过程必须与当前线程同步,否则后续逻辑无法串联********/
                String newestToken = mUpdateTokenImp.getNewestTokenStr();
                if(null == newestToken || newestToken.trim().equals(""))
                    return response;
                setToken(response.request().url().host(),newestToken);
                response = chain.proceed(request.newBuilder().header("Token",newestToken.trim()).build());
                /*******************使用递归的方式在请求头里添加Token并重新发起网络请求******************/
                response = updateTokenProcess(chain,request,response,--tryOnMaxNum);
            }
            else
                updateToken(response);
        }
        else
            updateToken(response);
        return response;
    }
}