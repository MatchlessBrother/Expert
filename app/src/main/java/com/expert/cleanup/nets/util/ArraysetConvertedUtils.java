package com.expert.cleanup.nets.util;

import java.util.Arrays;
import java.util.List;

/******集合与数组间的转换工具*****/
public class ArraysetConvertedUtils
{
    /********************List集合转数组****************/
    public static Object[] SetToArray(List<Object> list)
    {
        Object[] array = new Object[list.size()];
        for(int index = 0;index < list.size();index++)
            array[index] = list.get(index);
        return array;
    }

    /********************数组转List集合*****************/
    public static List<Object> ArrayToSet(Object[] array)
    {
        return Arrays.asList(array);
    }
}