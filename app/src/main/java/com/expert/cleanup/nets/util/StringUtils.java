package com.expert.cleanup.nets.util;

/**此类用于检索字符串属性**/
public class StringUtils
{
    /*************检索字符串内容是否为空**********/
    public final static boolean isEmpty(String str)
    {

        if(null == str || "".equals(str))
            return true;
        else
            return false;
    }
}