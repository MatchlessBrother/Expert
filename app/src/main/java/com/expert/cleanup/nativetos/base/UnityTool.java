package com.expert.cleanup.nativetos.base;

import java.util.Locale;
import java.math.BigDecimal;

public class UnityTool
{
    public static final String[] UNIT = {"B", "KB", "MB", "GB", "TB"};

    public static String sizeFormat(double size)
    {
        int index = 0;
        while (size > 1024 && index < 4)
        {
            size = size / 1024;
            index++;
        }
        BigDecimal bigDecimal = new BigDecimal(size);
        size = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return String.format(Locale.getDefault(), "%s%s", String.valueOf(size),UNIT[index]);
    }

    public static String sizeFormatInt(double size)
    {
        int index = 0;
        while (size > 1024 && index < 4)
        {
            size = size / 1024;
            index++;
        }
        BigDecimal bigDecimal = new BigDecimal(size);
        return String.format(Locale.getDefault(), "%s%s", String.valueOf(bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).intValue()),UNIT[index]);
    }
}