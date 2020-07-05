package com.expert.cleanup.nets.util;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.LocaleList;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.Callable;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static android.content.Context.TELEPHONY_SERVICE;

public class PhoneHelper
{
    /***获取手机分辨率***/
    public static String getResolution(final Context context)
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                DisplayMetrics dm = context.getResources().getDisplayMetrics();
                int width = dm.widthPixels;
                int height = dm.heightPixels;
                return width + "x" + height;
            }
        }));
    }

    public static String getIPAddress(final Context context)
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                NetworkInfo info = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
                if (info != null && info.isConnected())
                {
                    if (info.getType() == ConnectivityManager.TYPE_MOBILE)//当前使用2G/3G/4G网络
                    {
                        try
                        {
                            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); )
                            {
                                NetworkInterface intf = en.nextElement();
                                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); )
                                {
                                    InetAddress inetAddress = enumIpAddr.nextElement();
                                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address)
                                    {
                                        return inetAddress.getHostAddress();
                                    }
                                }
                            }
                        }
                        catch(SocketException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else if (info.getType() == ConnectivityManager.TYPE_WIFI)//当前使用无线网络
                    {
                        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                        return ipAddress;
                    }
                }
                else
                {
                    //当前无网络连接,请在设置中打开网络
                }
                return "";
            }
        }));
    }

    public static boolean isDeviceRooted()
    {
        Boolean result = safetyFunction(new Callable<Boolean>()
        {
            public Boolean call() throws Exception
            {
                return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
            }
        });
        return result != null && result;
    }

    public static String getSerialNumber(Context context)
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
                {
                    return Build.SERIAL;
                }
                else
                {
                    return getAndroidId(context);
                }
            }
        }));
    }

    /********获取手机厂商********/
    public static String getDeviceBrand()
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                return Build.BRAND;
            }
        }));
    }

    /********获取手机型号********/
    public static String getSystemModel()
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                return Build.MODEL;
            }
        }));
    }

    public static String getSystemLanguage()
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                Locale locale = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    locale = LocaleList.getDefault().get(0);
                else
                    locale = Locale.getDefault();
                return locale.getLanguage() + "-" + locale.getCountry();
            }
        }));
    }

    private static boolean checkRootMethod1()
    {
        String buildTags = Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private static boolean checkRootMethod2()
    {
        String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths)
        {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private static boolean checkRootMethod3()
    {
        Process process = null;
        try
        {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "su"});
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        }
        catch (Throwable t)
        {
            return false;
        }
        finally
        {
            if(process != null)
                process.destroy();
        }
    }

    public static String getCurrentTimeZone()
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                TimeZone tz = TimeZone.getDefault();
                String strTz = tz.getDisplayName(false, TimeZone.SHORT);
                return strTz;
            }
        }));
    }

    /********获取当前手机系统版本号********/
    public static String getSystemVersion()
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                return Build.VERSION.RELEASE;
            }
        }));
    }

    public static String getVersionName(final Context context)
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                String versionName = "1.0.0";
                try
                {
                    PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                    versionName = packageInfo.versionName;
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                return versionName;
            }
        }));
    }

    public static String getApkVersion(final Context context)
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                return String.valueOf(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode);
            }
        }));
    }

    /**
     * 将得到的int类型的IP转换为String类型
     */
    public static String intIP2StringIP(final int ip)
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                return (ip & 0xFF) + "." +
                       ((ip >> 8) & 0xFF) + "." +
                       ((ip >> 16) & 0xFF) + "." +
                       (ip >> 24 & 0xFF);
            }
        }));
    }

    public static String getAndroidId(final Context context)
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            }
        }));
    }

    public static boolean isHasSimCard(final Context context)
    {
        Boolean result = safetyFunction(new Callable<Boolean>()
        {
            public Boolean call() throws Exception
            {
                TelephonyManager telMgr = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
                int simState = telMgr.getSimState();
                boolean result = true;
                switch (simState)
                {
                    case TelephonyManager.SIM_STATE_ABSENT:
                        result = false;
                        break;
                    case TelephonyManager.SIM_STATE_UNKNOWN:
                        result = false;
                        break;
                }
                return result;
            }
        });
        return result != null && result;
    }

    /**
     * 存储空间
     * @param context
     * @return
     */
    public static String getRamTotalMemory(final Context context)
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                String path = Environment.getDataDirectory().getPath();
                StatFs statFs = new StatFs(path);
                long blockSize = statFs.getBlockSize();
                long totalBlocks = statFs.getBlockCount();
                long rom_length = totalBlocks * blockSize;
                return String.valueOf(rom_length) + "byte";
            }
        }));
    }

    /**
     * 运行内存
     * @param context
     * @return
     */
    public static String getRomTotalMemory(final Context context)
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                try
                {
                    boolean isStart = false;
                    FileInputStream fis = new FileInputStream(new File("/proc/meminfo"));
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fis));
                    String memTotal = bufferedReader.readLine();
                    StringBuffer sb = new StringBuffer();
                    for (char c : memTotal.toCharArray())
                    {
                        if (c >= '0' && c <= '9')
                            isStart = true;
                        if (isStart && c != ' ')
                            sb.append(c);
                    }
                    return sb.toString();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    return "";
                }
            }
        }));
    }

    public static String getBatteryTemperature(final Context context)
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                Intent intent = new ContextWrapper(context.getApplicationContext()).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                return intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10 + "";
            }
        }));
    }

    public static String getBatteryLevel(final Context context)
    {
        return wrapString(safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                {
                    BatteryManager batteryManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);
                    return batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY) + "";
                }
                else
                {
                    Intent intent = new ContextWrapper(context.getApplicationContext()).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                    return (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1) + "";
                }
            }
        }));
    }

    public static String getBatteryStatus(final Context context)
    {
        String result = safetyFunction(new Callable<String>()
        {
            public String call() throws Exception
            {
                Intent intent = new ContextWrapper(context.getApplicationContext()).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
                return BatteryManager.BATTERY_STATUS_CHARGING == status ? "1" : "0";
            }
        });
        return result == null ? "0" : result;
    }

    private static String wrapString(String source)
    {
        return TextUtils.isEmpty(source) ? "-2" : source;
    }

    private static <T> T safetyFunction(Callable<T> callable)
    {
        try
        {
            return callable.call();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String getOutsideIP(OkHttpClient client)
    {
        String outsideIp = "";
        try
        {
            outsideIp = client.newCall(new Request.Builder().get().url("https://api.ipify.org").build()).execute().body().string();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return outsideIp;
    }

    /******************获取App安装时间*****************/
    public static long getInstallTime(Context context)
    {
        try
        {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            long firstInstallTime = packageInfo.firstInstallTime;
            return firstInstallTime;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }

    /*****************获取App最近更新时间***************/
    public static long getLastUpdateTime(Context context)
    {
        try
        {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(),0);
            long lastUpdateTime = packageInfo.lastUpdateTime;
            return lastUpdateTime;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return System.currentTimeMillis();
    }
}