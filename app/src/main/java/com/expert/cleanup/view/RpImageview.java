package com.expert.cleanup.view;

import com.expert.cleanup.R;
import android.graphics.Color;
import android.graphics.Paint;
import android.content.Context;
import android.util.AttributeSet;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatImageView;

public class RpImageview extends AppCompatImageView
{
    private int mWidth;
    private int mHeight;
    private int mRpWidth;
    private int mBoderColor;
    private Context mContext;
    private Paint mMainPaint;
    private Paint mBoderPaint;

    public RpImageview(Context context)
    {
        this(context,null);
    }

    public RpImageview(Context context,AttributeSet attrs)
    {
        this(context,attrs,0);
    }

    public RpImageview(Context context,AttributeSet attrs,int defStyleAttr)
    {
        super(context,attrs,defStyleAttr);
        mContext = context;
        mMainPaint = new Paint();
        mMainPaint.setDither(true);
        mMainPaint.setAntiAlias(true);
        mBoderPaint = new Paint();
        mBoderPaint.setDither(true);
        mBoderPaint.setAntiAlias(true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs,
        R.styleable.RpImageview,defStyleAttr,R.style.defaultRpImageview);
        mRpWidth = typedArray.getInt(R.styleable.RpImageview_rpWidth,0);
        mBoderColor = typedArray.getColor(R.styleable.RpImageview_boderColor,Color.parseColor("#ffffffff"));
        mBoderPaint.setColor(mBoderColor);/****************************************************************/
        typedArray.recycle();/*****************************************************************************/
    }

    public void setmRpWidth(int mRpWidth)
    {
        this.mRpWidth = mRpWidth;
    }

    public void setmBoderColor(int mBoderColor)
    {
        this.mBoderColor = mBoderColor;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}