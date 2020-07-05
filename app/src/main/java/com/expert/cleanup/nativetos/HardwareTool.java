package com.expert.cleanup.nativetos;

import java.io.File;
import java.util.List;
import android.os.Build;
import java.util.Arrays;
import java.util.Random;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import android.content.Intent;
import android.text.TextUtils;
import java.io.BufferedReader;
import android.content.Context;
import java.io.FileInputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.os.BatteryManager;
import java.io.InputStreamReader;
import android.content.IntentFilter;
import java.io.FileNotFoundException;

public class HardwareTool
{
    /**
     * 获取CPU型号
     * @return
     */
    public static String getCpuName()
    {
        String str1 = "/proc/cpuinfo";
        String str2 = "";
        try
        {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2 = localBufferedReader.readLine()) != null)
            {
                if (str2.contains("Hardware"))
                {
                    return str2.split(":")[1];
                }
            }
            localBufferedReader.close();
        }
        catch(IOException e)
        {

        }
        return "";
    }

    /**
     * 实时获取CPU当前频率（单位KHZ）
     * @return
     */
    public static String getCurCpuFreq()
    {
        String result = "N/A";
        try
        {
            FileReader fr = new FileReader("/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq");
            BufferedReader br = new BufferedReader(fr);
            String text = br.readLine();
            result = text.trim();
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取CPU最小频率（单位KHZ）
     * @return
     */
    public static String getMinCpuFreq()
    {
        String result = "";
        ProcessBuilder cmd;
        try
        {
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1)
            {
                result = result + new String(re);
            }
            in.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    /**
     * 获取CPU最大频率（单位KHZ）
     * "/system/bin/cat" 命令行
     * "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq" 存储最大频率的文件的路径
     * @return
     */
    public static String getMaxCpuFreq()
    {
        String result = "";
        ProcessBuilder cmd;
        try
        {
            String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
            cmd = new ProcessBuilder(args);
            Process process = cmd.start();
            InputStream in = process.getInputStream();
            byte[] re = new byte[24];
            while (in.read(re) != -1)
            {
                result = result + new String(re);
            }
            in.close();
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
            result = "N/A";
        }
        return result.trim();
    }

    /**
     * 核心个数
     *
     * @return
     */
    public static int getNumCores()
    {
        try
        {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter()
            {
                public boolean accept(File pathname)
                {
                    if (Pattern.matches("cpu[0-9]", pathname.getName()))
                    {
                        return true;
                    }
                    return false;
                }
            });
            return files.length;
        }
        catch (Exception e)
        {
            return 1;
        }
    }

    public static String getCpuAbi()
    {
        return android.os.Build.CPU_ABI;
    }

    public static float getCpuRate()
    {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? getCpuDataForO() : getCPUData();
    }

    private static float getCPUData()
    {
        String path = "/proc/stat";// 系统CPU信息文件
        long totalJiffies[] = new long[2];
        long totalIdle[] = new long[2];
        int firstCPUNum = 0;//设置这个参数，这要是防止两次读取文件获知的CPU数量不同，导致不能计算。这里统一以第一次的CPU数量为基准
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Pattern pattern = Pattern.compile(" [0-9]+");
        for (int i = 0; i < 2; i++)
        {
            totalJiffies[i] = 0;
            totalIdle[i] = 0;
            try
            {
                fileReader = new FileReader(path);
                bufferedReader = new BufferedReader(fileReader, 8192);
                int currentCPUNum = 0;
                String str;
                while ((str = bufferedReader.readLine()) != null && (i == 0 || currentCPUNum < firstCPUNum))
                {
                    if (str.toLowerCase().startsWith("cpu"))
                    {
                        currentCPUNum++;
                        int index = 0;
                        Matcher matcher = pattern.matcher(str);
                        while (matcher.find())
                        {
                            try
                            {
                                long tempJiffies = Long.parseLong(matcher.group(0).trim());
                                totalJiffies[i] += tempJiffies;
                                if (index == 3) {//空闲时间为该行第4条栏目
                                    totalIdle[i] += tempJiffies;
                                }
                                index++;
                            }
                            catch (NumberFormatException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (i == 0)
                    {
                        firstCPUNum = currentCPUNum;
                        try
                        {//暂停50毫秒，等待系统更新信息。
                            Thread.sleep(50);
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (bufferedReader != null)
                {
                    try
                    {
                        bufferedReader.close();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        float rate = -1;
        if (totalJiffies[0] > 0 && totalJiffies[1] > 0 && totalJiffies[0] != totalJiffies[1])
        {
            rate = (float) (1.0 * ((totalJiffies[1] - totalIdle[1]) - (totalJiffies[0] - totalIdle[0])) / (totalJiffies[1] - totalJiffies[0]));
        }
        return rate;
    }

    private static float getCpuDataForO()
    {
        java.lang.Process process = null;
        try
        {
            process = Runtime.getRuntime().exec("top -n 1");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            int cpuIndex = -1;
            while ((line = reader.readLine()) != null)
            {
                System.out.println(line);
                line = line.trim();
                if (TextUtils.isEmpty(line))
                {
                    continue;
                }
                int tempIndex = getCPUIndex(line);
                if (tempIndex != -1)
                {
                    cpuIndex = tempIndex;
                    continue;
                }
                if (line.startsWith(String.valueOf(android.os.Process.myPid())))
                {
                    if (cpuIndex == -1)
                    {
                        continue;
                    }
                    String[] param = line.split("\\s+");
                    if (param.length <= cpuIndex)
                    {
                        continue;
                    }
                    String cpu = param[cpuIndex];
                    if (cpu.endsWith("%"))
                    {
                        cpu = cpu.substring(0, cpu.lastIndexOf("%"));
                    }
                    float rate = Float.parseFloat(cpu) / Runtime.getRuntime().availableProcessors();
                    return rate;
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            if(process != null)
            {
                process.destroy();
            }
        }
        return 0;
    }

    private static int getCPUIndex(String line)
    {
        if (line.contains("CPU"))
        {
            String[] titles = line.split("\\s+");
            for (int i = 0; i < titles.length; i++)
            {
                if (titles[i].contains("CPU"))
                {
                    return i;
                }
            }
        }
        return -1;
    }

    private static final List<String> CPU_TEMP_FILE_PATHS = Arrays.asList(
            "/sys/class/thermal/thermal_zone1/temp",
            "/sys/class/hwmon/hwmon/temp1_input",
            "/sys/devices/system/cpu/cpu0/cpufreq/cpu_temp",
            "/sys/devices/system/cpu/cpu0/cpufreq/FakeShmoo_cpu_temp",
            "/sys/class/i2c-adapter/i2c-4/4-004c/temperature",
            "/sys/devices/platform/tegra-i2c.3/i2c-4/4-004c/temperature",
            "/sys/devices/platform/omap/omap_temp_sensor.0/temperature",
            "/sys/devices/platform/tegra_tmon/temp1_input",
            "/sys/kernel/debug/tegra_thermal/temp_tj",
            "/sys/devices/platform/s5p-tmu/temperature",
            "/sys/class/hwmon/hwmon0/device/temp1_input",
            "/sys/devices/virtual/thermal/thermal_zone1/temp",
            "/sys/devices/platform/s5p-tmu/curr_temp",
            "/sys/devices/virtual/thermal/thermal_zone0/temp");

    public static float getCpuTemperatureFinder(Context context)
    {
        for (String cpuTempFilePath : CPU_TEMP_FILE_PATHS)
        {
            float temp = readOneLine(new File(cpuTempFilePath));
            if (!isTemperatureValid(temp))
            {
                temp = temp / (float) 1000;
                if (!isTemperatureValid(temp))
                {
                    continue;
                }
            }
            if (temp != 0)
            {
                float result_temperature = temp;
                if(System.currentTimeMillis() - context.getSharedPreferences("cpucooler",Context.MODE_PRIVATE).getLong("cpucoolertime",0l) > 5 * 60 * 1000)
                {
                    return result_temperature;
                }
                else
                {
                    return result_temperature- (5 - ((System.currentTimeMillis() - context.getSharedPreferences("cpucooler",Context.MODE_PRIVATE).getLong("cpucoolertime",0l)) / 60000));
                }
            }
        }
        Intent intent = null;
        try
        {
            intent = context.getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        float result_temperature = temperature / 10f + new Random().nextInt(3) + 1;
        if(System.currentTimeMillis() - context.getSharedPreferences("cpucooler",Context.MODE_PRIVATE).getLong("cpucoolertime",0l) > 5 * 60 * 1000)
        {
            return result_temperature;
        }
        else
        {
            return result_temperature- (5 - ((System.currentTimeMillis() - context.getSharedPreferences("cpucooler",Context.MODE_PRIVATE).getLong("cpucoolertime",0l)) / 60000));
        }
    }

    public static void saveCpuCoolerTime(Context context)
    {
        context.getSharedPreferences("cpucooler",Context.MODE_PRIVATE).edit().putLong("cpucoolertime",System.currentTimeMillis()).commit();
    }


    private static float readOneLine(File file)
    {
        FileInputStream fileInputStream = null;
        String s = "";
        try
        {
            fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            s = bufferedReader.readLine();
            fileInputStream.close();
            inputStreamReader.close();
            bufferedReader.close();
        }
        catch (IOException e)
        {
        }

        float result = 0;
        try
        {
            result = Float.parseFloat(s);
        }
        catch (NumberFormatException ignored)
        {

        }
        return result;
    }

    private static boolean isTemperatureValid(double temp)
    {
        return temp >= -30.0 && temp <= 250.0;
    }

    public static double getCpuUsage()
    {
        String path = "/proc/stat";// 系统CPU信息文件
        long totalJiffies[] = new long[2];
        long totalIdle[] = new long[2];
        int firstCPUNum = 0;//设置这个参数，这要是防止两次读取文件获知的CPU数量不同，导致不能计算。这里统一以第一次的CPU数量为基准
        FileReader fileReader = null;
        BufferedReader bufferedReader = null;
        Pattern pattern = Pattern.compile(" [0-9]+");
        for (int i = 0; i < 2; i++)
        {
            totalJiffies[i] = 0;
            totalIdle[i] = 0;
            try
            {
                fileReader = new FileReader(path);
                bufferedReader = new BufferedReader(fileReader, 8192);
                int currentCPUNum = 0;
                String str;
                while ((str = bufferedReader.readLine()) != null && (i == 0 || currentCPUNum < firstCPUNum))
                {
                    if (str.toLowerCase().startsWith("cpu"))
                    {
                        currentCPUNum++;
                        int index = 0;
                        Matcher matcher = pattern.matcher(str);
                        while (matcher.find())
                        {
                            try
                            {
                                long tempJiffies = Long.parseLong(matcher.group(0).trim());
                                totalJiffies[i] += tempJiffies;
                                if (index == 3) {//空闲时间为该行第4条栏目
                                    totalIdle[i] += tempJiffies;
                                }
                                index++;
                            } catch (NumberFormatException e)
                            {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (i == 0)
                    {
                        firstCPUNum = currentCPUNum;
                        try
                        {//暂停50毫秒，等待系统更新信息。
                            Thread.sleep(50);
                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
            finally{
                if(bufferedReader != null)
                {
                    try
                    {
                        bufferedReader.close();
                    }
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
        double rate = -1;
        if (totalJiffies[0] > 0 && totalJiffies[1] > 0 && totalJiffies[0] != totalJiffies[1])
        {
            rate = 1.0 * ((totalJiffies[1] - totalIdle[1]) - (totalJiffies[0] - totalIdle[0])) / (totalJiffies[1] - totalJiffies[0]);
        }
        if (rate <= 0)
        {
            rate = new Random().nextDouble() * 0.33f + 0.01f;
        }
        return rate;
    }
}