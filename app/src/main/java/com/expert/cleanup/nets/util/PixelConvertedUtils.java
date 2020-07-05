package com.expert.cleanup.nets.util;

import android.content.Context;
import android.util.DisplayMetrics;

/****此类用于像素格式的转换*****/
public class PixelConvertedUtils
{
	/**********************dp转px***********************/
	public final static int dpTpx(Context context, int dp)
	{
	    final float density = context.getResources().getDisplayMetrics().density;
	    return (int)(dp * density + 0.5f);
	}

	/**********************px转dp***********************/
	public final static int pxTdp(Context context, int px)
	{
		final float density = context.getResources().getDisplayMetrics().density;
		return (int)(px / density + 0.5f);
	}

	/**********************sp转px***********************/
	public final static int spTpx(Context context, int sp)
	{
		final float density = context.getResources().getDisplayMetrics().scaledDensity;
		return (int)(sp * density + 0.5f);
	}

	/**********************px转sp***********************/
	public final static int pxTsp(Context context, int px)
	{
		final float density = context.getResources().getDisplayMetrics().scaledDensity;
		return (int)(px / density + 0.5f);
	}

	/**********************横向in转px*******************/
	public final static int inXpx(Context context, int in)
	{
		final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return (int)(in * displayMetrics.xdpi + 0.5f);
	}

	/**********************横向px转in*******************/
	public final static float pxXin(Context context, int px)
	{
		final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return px / displayMetrics.xdpi;
	}

	/**********************纵向in转px*******************/
	public final static int inYpx(Context context, int in)
	{
		final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return (int)(in * displayMetrics.ydpi + 0.5f);
	}

	/**********************纵向px转in*******************/
	public final static float pxYin(Context context, int px)
	{
		final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return px / displayMetrics.ydpi;
	}

	/**********************横向mm转px*******************/
	public final static int mmXpx(Context context, int mm)
	{
		final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return (int)(mm * displayMetrics.xdpi / 25.4f  + 0.5f);
	}

	/**********************横向px转mm*******************/
	public final static float pxXmm(Context context, int px)
	{
		final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return px * 25.4f / displayMetrics.xdpi;
	}

	/**********************纵向mm转px*******************/
	public final static int mmYpx(Context context, int mm)
	{
		final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return (int)(mm * displayMetrics.ydpi / 25.4f  + 0.5f);
	}

	/**********************纵向px转mm*******************/
	public final static float pxYmm(Context context, int px)
	{
		final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return px * 25.4f / displayMetrics.ydpi;
	}

	/**********************横向pt转px*******************/
	public final static int ptXpx(Context context, int pt)
	{
		final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return (int)(pt * displayMetrics.xdpi / 72.0f  + 0.5f);
	}

	/**********************横向px转pt*******************/
	public final static float pxXpt(Context context, int px)
	{
		final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return px * 72.0f / displayMetrics.xdpi;
	}

	/**********************纵向pt转px*******************/
	public final static int ptYpx(Context context, int pt)
	{
		final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return (int)(pt * displayMetrics.ydpi / 72.0f  + 0.5f);
	}

	/**********************纵向px转pt*******************/
	public final static float pxYpt(Context context, int px)
	{
		final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		return px * 72.0f / displayMetrics.ydpi;
	}
}