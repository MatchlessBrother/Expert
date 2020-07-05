package com.expert.cleanup.nets.okhttp.Http3Interceptions;
import okhttp3.Response;

/********此接口用于配合TokenInterceptor拦截器*********/

/********是实现即时刷新Token缓存数据的策略接口********/
public interface TokenInterceptor_UpdateTokenInterface
{
    /***通过同步方式的网络请求获取最新的Token字符串***/
    String getNewestTokenStr();

    /*******根据服务器响应数据判定Token是否失效*******/
    Boolean whetherTheTokenIsExpired(Response response);
}