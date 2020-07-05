package com.expert.cleanup.nativetos.base;

import java.io.File;
import java.util.Locale;
import java.math.BigDecimal;

public class UnitTool
{
    private static final String[] UNITS = {"B", "KB", "MB", "GB", "TB"};

    public static String[] fileSizeMB(float size)
    {
        size = size / 1024f / 1024f;
        BigDecimal bigDecimal = new BigDecimal(size);
        size = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return new String[]{String.valueOf(size), UNITS[2]};
    }

    public static String sizeFormat(float size)
    {
        int index = 0;
        while (size > 1024 && index < 4)
        {
            size = size / 1024;
            index++;
        }
        BigDecimal bigDecimal = new BigDecimal(size);
        size = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return String.format(Locale.getDefault(), "%s%s", String.valueOf(size), UNITS[index]);
    }

    public static String[] fileSize(float size)
    {
        int index = 0;
        while (size > 1024 && index < 4)
        {
            size = size / 1024;
            index++;
        }
        BigDecimal bigDecimal = new BigDecimal(size);
        size = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        return new String[]{String.valueOf(size), UNITS[index]};
    }

    public static String getName(String path)
    {
        if (path != null && path.length() > 0)
        {
            return path.substring(path.lastIndexOf(".") + 1);
        }
        return "";
    }

    public static long fileSize(File file)
    {
        long size = 0;
        if (file.exists())
        {
            if (file.isDirectory())
            {
                File[] subFiles = file.listFiles();
                if (subFiles != null)
                {
                    for (File subFile : subFiles)
                    {
                        size += fileSize(subFile);
                    }
                }
            }
            else
            {
                if (file.canWrite() && file.canRead())
                {
                    size += file.length();
                }
            }
        }
        return size;
    }
}