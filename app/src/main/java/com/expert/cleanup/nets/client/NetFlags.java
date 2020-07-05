package com.expert.cleanup.nets.client;

public class NetFlags
{
    public static final String RequestSuccess = "0";//成功
    public static final String RequestFailForNull = "-1";//安卓Id为空
    public static final String RequestFailForTooLong = "-2";//请求参数长度超过限制
    public static final String RequestFailForBlackList = "-3";//安卓Id加入黑名单失败
    public static final String RequestFailForDataWritingFailure = "-4";//将数据写入Redis失败
}