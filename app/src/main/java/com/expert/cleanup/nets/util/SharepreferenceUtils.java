package com.expert.cleanup.nets.util;

import android.content.Context;
import android.content.SharedPreferences;

/****此类用于快速存取Share数据***/
public class SharepreferenceUtils
{
    /********将某个Object对象以Json字符串的格式存储在Sharepreference里面********/
    public static void storageObject(Context context, String name, Object object)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(name, GsonUtils.objectToGsonString(object)).apply();
    }

    /***将存储在Sharepreference里面的某个Json字符串取出并转换成指定的实例对象***/
    public static <T> T extractObject(Context context, String name, Class<T> clazz)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
        return GsonUtils.getObj(sharedPreferences.getString(name,""),clazz);
    }
}