package com.expert.cleanup.nets.util;

import android.view.View;
import android.app.Dialog;
import android.content.Context;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import android.view.ViewConfiguration;
import androidx.appcompat.app.AlertDialog;

/*****代替系统AlertDialog的新型弹出框*****/
public class BaseDialog extends AlertDialog
{
    private boolean mCloseable = true;
    private BaseDialog.OnClickOutsideListener mOnClickOutsideListener;

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    public BaseDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public BaseDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public boolean onTouchEvent(MotionEvent event)
    {
        if (mCloseable && isOutOfBounds(getContext(), event) && null != mOnClickOutsideListener)
        {
            mOnClickOutsideListener.onClickOutside(this);
        }
        return super.onTouchEvent(event);
    }

    public void setCanceledOnTouchOutside(boolean cancel)
    {
        super.setCanceledOnTouchOutside(cancel);
        mCloseable = cancel;
    }

    public boolean getCanceledOnTouchOutside()
    {
        return mCloseable;
    }

    /*************这里判断用户的触摸点是否在弹出框的外围************/
    private boolean isOutOfBounds(Context context, MotionEvent event)
    {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop) || (x > (decorView.getWidth() + slop)) || (y > (decorView.getHeight() + slop));
    }

    /***用户触摸弹出框外围时触发的类事件***/
    public interface OnClickOutsideListener
    {
        void onClickOutside(Dialog dialog);
    }

    public BaseDialog.OnClickOutsideListener getOnClickOutsideListener()
    {
        return mOnClickOutsideListener;
    }

    public void setOnClickOutsideListener(BaseDialog.OnClickOutsideListener onClickOutsideListener)
    {
        mOnClickOutsideListener = onClickOutsideListener;
    }
}