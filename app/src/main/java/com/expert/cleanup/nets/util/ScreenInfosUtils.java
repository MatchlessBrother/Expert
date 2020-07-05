package com.expert.cleanup.nets.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

/*******获取屏幕宽高度*******/
public class ScreenInfosUtils
{
    /***************************获取屏幕宽度**************************/
    public static int getScreenWidth(Context context)
    {
        Point point = new Point();
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(point);
        return point.x;
    }

    /***************************获取屏幕密度**************************/
    public static DisplayMetrics getDisplayMetrics(Context context)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    /*********************获取屏幕高度(包含导航栏)********************/
    public static int getScreenHeightWithNavigation(Context context)
    {
        Point point = new Point();
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            windowManager.getDefaultDisplay().getSize(point);
            point.y += getNavigationBarHeight(context);
        }
        else
            windowManager.getDefaultDisplay().getRealSize(point);
        return point.y;
    }

    /********************获取屏幕高度(不包含导航栏)*******************/
    public static int getScreenHeightWithoutNavigation(Context context)
    {
        Point point = new Point();
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getSize(point);
        return point.y;
    }

    /************************判定状态栏是否显示***********************/
    public static boolean isShowStatusBar(Activity context)
    {
        return (context.getWindow().getAttributes().flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == 0 ? true : false;
    }

    /**************获取状态栏高度（在初始化界面之前）*****************/
    public static int getStatusBarHeightBeforeInit(Context context)
    {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId != 0)
            return context.getResources().getDimensionPixelSize(resourceId);
        else
            return 0;
    }

    /**************获取状态栏高度（在初始化界面之后）*****************/
    public static int getStatusBarHeightAfterInit(Activity context)
    {
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    /****************判定标题栏是否显示(有问题,不准确)****************/
    public static boolean isShowTitleBar(Activity activity)
    {
        return activity.getWindow().hasFeature(Window.FEATURE_NO_TITLE) ? false : true;
    }

    /********获取标题栏高度(必须在Activity显示之后才能正确获取)*******/
    public static int getTitleBarHeight(Activity context)
    {
        Rect titleContentRect = new Rect();
        Rect contentRect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(titleContentRect);
        context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(contentRect);
        //return context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop() - titleContentRect.top;
        return titleContentRect.height() - contentRect.height();
    }

    /****************隐藏导航栏和状态栏以达到全屏效果*****************/
    public static void hideNavigationBarAndStatusBar(Activity activity)
    {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19)
        {
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        }
        else if (Build.VERSION.SDK_INT >= 19)
        {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.STATUS_BAR_HIDDEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    /**************导航栏和状态栏是否隐藏,是否是全屏效果**************/
    public static boolean ishideNavigationBarAndStatusBar(Activity activity)
    {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19)
        {
            View v = activity.getWindow().getDecorView();
            if((v.getSystemUiVisibility() & View.GONE) == View.GONE)
                return true;
            else
                return false;
        }
        else if (Build.VERSION.SDK_INT >= 19)
        {
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN | View.STATUS_BAR_HIDDEN;
            if((decorView.getSystemUiVisibility() & uiOptions) == uiOptions)
                return true;
            else
                return false;
        }
        else
            return false;
    }

    /****************************隐藏标题栏***************************/
    public static void hideTitleBar(Activity activity)
    {
        if(activity instanceof AppCompatActivity)
            ((AppCompatActivity)activity).getSupportActionBar().hide();
        else
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /************************判定导航栏是否显示***********************/
    public static boolean isShowNavigationBar(Activity context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            Point size = new Point();
            Point realSize = new Point();
            context.getWindowManager().getDefaultDisplay().getSize(size);
            context.getWindowManager().getDefaultDisplay().getRealSize(realSize);
            return realSize.y != size.y;
        }
        else
        {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if(menu || back)
                return false;
            else
                return true;
        }
    }

    /**************************获取导航栏高度*************************/
    public static int getNavigationBarHeight(Context context)
    {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1)
        {
            if (context.getResources().getIdentifier("config_showNavigationBar", "bool", "android")  !=  0)
                return context.getResources().getDimensionPixelSize(context.getResources().getIdentifier("navigation_bar_height", "dimen", "android"));
            else
                return 0;
        }
        else
        {
            Point pointWithNavigation = new Point();
            Point pointWithoutNavigation = new Point();
            WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getRealSize(pointWithNavigation);
            windowManager.getDefaultDisplay().getSize(pointWithoutNavigation);
            return pointWithNavigation.y - pointWithoutNavigation.y;
        }
    }

    /**************************获取应用区域高度***********************/
    public static int getAppExpertHeight(Activity context)
    {
        Rect rect = new Rect();
        context.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.height();
    }

    /*************************获取View区域高度************************/
    public static int getViewExpertHeight(Activity context)
    {
        Rect rect = new Rect();
        context.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getDrawingRect(rect);
        return rect.height();
    }

    /************判定当前屏幕是否为竖屏,竖屏true,横屏false************/
    public static boolean isScreenPortrait(Context context)
    {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    /*******************是否亮屏********************/
    public static boolean isScreenOn(Context context)
    {
        PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        return powerManager.isScreenOn();
    }
}