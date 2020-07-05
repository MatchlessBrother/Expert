package com.expert.cleanup.nets.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/*****操作软键盘的工具类******/
public class SoftKeyboardUtils
{
    /*************判定当前界面是否显示了软键盘************/
    public static boolean isShowKeyboard(Activity activity)
    {
        Rect rect = new Rect();
        View rootView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        rootView.getWindowVisibleDisplayFrame(rect);
        int rootViewScreenHeight = rootView.getHeight();
        int keyboardHeight = rootViewScreenHeight - rect.bottom;
        boolean visible = keyboardHeight >= rootViewScreenHeight / 3;
        return visible;
    }

    /*******************获取软键盘的高度*****************/
    public static int getKeyboardHeight(Activity activity)
    {
        Rect rect = new Rect();
        View rootView = ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
        rootView.getWindowVisibleDisplayFrame(rect);
        int rootViewScreenHeight = rootView.getHeight();
        int keyboardHeight = rootViewScreenHeight - rect.bottom;
        return keyboardHeight;
    }

    /***************使用此方法针对具体View开启软键盘**************/
    public static boolean showKeyboard(Activity activity, View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != view)
        {
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
            return true;
        }
        else
        {
           if(null != activity.getCurrentFocus())
           {
               inputMethodManager.showSoftInput(activity.getCurrentFocus(), InputMethodManager.SHOW_FORCED);
               return true;
           }
           else
           {
               if(isShowKeyboard(activity))
                   return false;
               else
               {
                   inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                   return true;
               }
           }
        }
    }

    /***************使用此方法针对具体View关闭软键盘**************/
    public static boolean hideKeyboard(Activity activity, View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null != view)
        {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
            return true;
        }
        else
        {
            if(null != activity.getCurrentFocus())
            {
                inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                return true;
            }
            else
            {
                if(isShowKeyboard(activity))
                {
                    inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    return true;
                }
                else
                    return false;
            }
        }
    }

    /*******监听软键盘弹出时用到的回调接口*******/
    public interface SoftKeyboardActionalListener
    {
        void hasShow(boolean isShow);
    }

    /*****************************************判断软键盘是否弹出?在不用的时候记得移除OnGlobalLayoutListener*******************************************/
    public static ViewTreeObserver.OnGlobalLayoutListener softKeyboardActionalListener(final View rootView, final SoftKeyboardActionalListener listener)
    {
        final ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver.OnGlobalLayoutListener()
        {
            public void onGlobalLayout()
            {
                Rect rect = new Rect();
                rootView.getWindowVisibleDisplayFrame(rect);
                int visibilityContentHeight = rootView.getRootView().getHeight();
                int heightDifference = visibilityContentHeight - rect.bottom;
                boolean visible = heightDifference > visibilityContentHeight / 3;
                if (listener != null)
                    listener.hasShow(visible);
            }
        };
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);
        return layoutListener;
    }
}