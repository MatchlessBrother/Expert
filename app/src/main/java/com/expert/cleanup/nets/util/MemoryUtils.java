package com.expert.cleanup.nets.util;

import java.io.File;
import java.util.Map;
import java.util.List;
import android.os.StatFs;
import java.util.ArrayList;
import android.widget.Toast;
import android.os.Environment;
import android.content.Context;
import java.lang.reflect.Method;
import android.os.storage.StorageManager;
import java.lang.reflect.InvocationTargetException;

/**此类用于获得内置内存/外置内存的路径以及大小*/
public class MemoryUtils
{
	public static final String KEY_ROM_MEMORY = "ANDROID_DATA";/*********运行内存********/
	/**System.getenv()获取android系统内部的信息，比如关于内存卡，外存卡，运行内存的信息.*/

	/*********获取运行内存的路径**********/
	public static final String getRomPath()
	{
		String path = null;
		Map<String, String> map = System.getenv();
		if (map.containsKey(KEY_ROM_MEMORY))
		{
			path = map.get(KEY_ROM_MEMORY);
		}
		return path;
	}

	/****************检测SD卡是否安装***************/
	public static final boolean whetherHasTheSDcard()
	{
		if(null != Environment.getExternalStorageState() && Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
			return true;
		else
			return false;
	}

	/**获取最佳存储路径，有外置内存则返回外置内存的根目录，否则返回内置内存的根目录*/
	public static final String getBestStoragePath(Context context)
	{
		String bestStoragePath = getExternalSDcardPath(context);
		if(!StringUtils.isEmpty(bestStoragePath))
			return bestStoragePath;

		bestStoragePath = getInternalSDcardPath(context);
		if(!StringUtils.isEmpty(bestStoragePath))
			return bestStoragePath;
		return null;
	}

	/**获取FilePath的最佳存储路径，有外置内存则返回外置内存的FilePath根目录，否则返回内置内存的FilePath根目录*/
	public static final String getBestFilesPath(Context context)
	{
		String bestFilesPath = null;
		if(whetherHasTheSDcard())
			bestFilesPath = context.getExternalFilesDir("").getPath();
		else
			bestFilesPath = context.getFilesDir().getPath();
		return bestFilesPath;
	}

	/**获取CachePath最佳存储路径，有外置内存则返回外置内存的CachePath根目录，否则返回内置内存的CachePath根目录*/
	public static final String getBestCachesPath(Context context)
	{
		String bestCachesPath = null;
		if(whetherHasTheSDcard())
			bestCachesPath = context.getExternalCacheDir().getPath();
		else
			bestCachesPath = context.getCacheDir().getPath();
		return bestCachesPath;
	}

	/********************获取手机内置存储卡的根目录********************/
	public static final String getInternalSDcardPath(Context context)
	{
		List<StorageInfo> list = getAvaliableStorageList(context);
		for(StorageInfo info : list)
		{
			if(!info.mRemoveable)
			{
				return info.mPath;
			}
		}
		return null;
	}

	/*********************获取手机外置存储卡的根目录*******************/
	public static final String getExternalSDcardPath(Context context)
	{
		List<StorageInfo> list = getAvaliableStorageList(context);
		for(StorageInfo info : list)
		{
			if(info.mRemoveable)
			{
				return info.mPath;
			}
		}
		return null;
	}

	/********通过反射的方式得到当前手机所有正在使用的挂载点列表********/
	private static final List getAvaliableStorageList(Context context)
	{
		ArrayList storageList = new ArrayList();
		StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
		try
		{
			/**通过反射的方式来回调StorageManager.getVolumeList()方法，从而得到当前手机所有的挂载点信息**/
			Method getVolumeListMethod = StorageManager.class.getMethod("getVolumeList", new Class<?>[]{});
			getVolumeListMethod.setAccessible(true);
			Object[] resultInvokes = (Object[]) getVolumeListMethod.invoke(storageManager, new Object[]{});
			if (resultInvokes != null)
			{
				StorageInfo storageInfo = null;
				/**遍历当前手机所有的挂载点信息，从而得到正在使用的挂载点列表**/
				for (int index = 0; index < resultInvokes.length; index++)
				{
					Object obj = resultInvokes[index];
					Method getPathMethod = obj.getClass().getMethod("getPath", new Class[0]);
					String path = (String) getPathMethod.invoke(obj, new Object[0]);
					storageInfo = new StorageInfo(path);
					File file = new File(storageInfo.mPath);
					if (file.exists() && file.isDirectory() && file.canWrite())
					{
						String volumeState = null;
						Method getVolumeStateMethod = StorageManager.class.getMethod("getVolumeState", String.class);
						volumeState = (String) getVolumeStateMethod.invoke(storageManager, storageInfo.mPath);
						storageInfo.mState = volumeState;

						if (storageInfo.isMounted())
						{
							Method isRemovableMethod = obj.getClass().getMethod("isRemovable", new Class[0]);
							storageInfo.mRemoveable = ((Boolean) isRemovableMethod.invoke(obj, new Object[0])).booleanValue();
							storageList.add(storageInfo);
						}
					}
				}
			}
			else
				Toast.makeText(context, "亲，获取系统内存挂载信息失败了！", Toast.LENGTH_SHORT).show();
		}
		catch (NoSuchMethodException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		storageList.trimToSize();
		return storageList;
	}

	/*********************计算手机内置存储卡剩余大小***********************/
	public static final float getAvailableInternalSDcardSize(Context context)
	{
		return calculateSizeInMB(getInternalSDcardPath(context));
	}

	/***********************计算手机外置存储卡剩余大小*********************/
	public static final float getAvailableExternalSDcardSize(Context context)
	{
		return calculateSizeInMB(getExternalSDcardPath(context));
	}

	/**********************************计算运行内存剩余大小***********************************/
	public static final float getAvailableRomSize()
	{
		return calculateSizeInMB(getRomPath());
	}

	/***获得statFs对象以便得到具体内存的空间信息***/
	public static final StatFs getStatFs(String path)
	{
		try
		{
			return new StatFs(path);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	/*********计算具体内存的可用剩余大小.(以MB计算)********/
	public static final float calculateSizeInMB(String path)
	{
		StatFs stat = getStatFs(path);
		if (stat != null)
		{
			return (float)(Math.round((stat.getAvailableBlocks() * (stat.getBlockSize() / (1024f * 1024f)))*100))/100;
		}
		return 0.0f;
	}

	/**自定义手机内存-挂载点信息的浓缩类**/
	private static class StorageInfo
	{
		public String mPath;
		public String mState;
		public boolean mRemoveable;

		/***根据挂载点路径初始化实例***/
		public StorageInfo(String path)
			{
				mPath = path;
			}

		/**判定当前挂载点是否正在使用**/
		public boolean isMounted()
			{
				return "mounted".equals(mState);
			}
	}
}