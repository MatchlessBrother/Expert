package com.expert.cleanup.nets.util;

import com.google.gson.Gson;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**Gson解析工具常用的解析功能**/
public class GsonUtils
{
    /******************将一个Object对象转成相应的Json字符串***************/
    public static String objectToGsonString(Object object)
    {
        return new Gson().toJson(object).toString().trim();
    }

    /************通过指定的jsonString字符串和指定的类名得到类对象*********/
    public static <T> T getObj(String jsonString, Class<T> clazz)
    {
        T t = null;
        try
        {
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, clazz);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return t;
    }

    /********通过指定的JSONArray对象和指定的类名得到类对象List集合********/
    public static <T> List<T> getList(JSONArray array, Class<T> clazz)
    {
        List<T> list = new ArrayList<T>();
        try
        {
            if (array != null && array.length() > 0)
            {
                for (int index = 0; index < array.length(); index++)
                {
                    T obj = getObj(array.getJSONObject(index).toString(), clazz);
                    list.add(obj);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }

    /*****通过指定的mapJsonString字符串和指定的类类型得到类对象Map集合*****
     ******其实这个方法还可以获取到更多类型的数据,自行扩展吧！！！！！！***/
    public static <T> T getMap(String mapJsonString, Type type)
    {
        Gson gson = new Gson();
        T    map  = (T) gson.fromJson(mapJsonString, type);
        return map;
    }
}