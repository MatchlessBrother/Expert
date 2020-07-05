package com.expert.cleanup.baer;

import android.util.Log;
import android.view.View;
import com.expert.cleanup.R;
import android.view.ViewGroup;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import android.content.Context;
import android.widget.ImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import androidx.annotation.Nullable;
import android.content.res.TypedArray;
import androidx.constraintlayout.widget.ConstraintLayout;
import static com.expert.cleanup.baer.BannerHelper.isPrintLog;

public class BannerView extends ConstraintLayout implements View.OnClickListener
{
    private int mHeightMode;
    private int mRetryNumber;
    private Context mContext;
    private String mPlacementId;
    private boolean mCloseEnable;
    /*************************************/
    private AdView mAdView;
    private ImageView mCloseButton;
    private ViewGroup mRootViewGroup;
    private BannerListener mBannerListener;
    /*************************************/

    public BannerView(Context context)
    {
        this(context,null);

    }

    public BannerView(Context context, @Nullable AttributeSet attrs)
    {
        this(context,attrs,0);

    }

    public BannerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)
    {
        super(context,attrs,defStyleAttr);
        mContext = context;/*******************************************************************************************************/
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs,R.styleable.Banner,defStyleAttr,R.style.DefaultBannerConfigs);
        mCloseEnable = typedArray.getBoolean(R.styleable.Banner_closeEnable,true);
        mRetryNumber = typedArray.getInt(R.styleable.Banner_retryNumber,0);
        mHeightMode = typedArray.getInt(R.styleable.Banner_heightMode,0);
        mPlacementId = typedArray.getString(R.styleable.Banner_placementId);
        if(null == mPlacementId || "".equals(mPlacementId.trim()))
            mPlacementId = mContext.getString(R.string.fb_banner_id);
        typedArray.recycle();
        /***************************************************************************************************************************/
        View bannerView = LayoutInflater.from(mContext).inflate(R.layout.layout_bannerview,this,true);
        mRootViewGroup = (ViewGroup)bannerView.findViewById(R.id.banner_all);/*************/
        mCloseButton = (ImageView)bannerView.findViewById(R.id.banner_close);/*************/
        mCloseButton.setVisibility(View.GONE);
        mCloseButton.setOnClickListener(this);
    }

    public void setAllConfigs(String placementId,int heightMode,boolean closeEnable,int retryNumber)
    {
        mHeightMode = heightMode;
        mPlacementId = placementId;
        mCloseEnable = closeEnable;
        mRetryNumber = retryNumber;
    }

    public void setCloseEnable(boolean closeEnable)
    {
        mCloseEnable = closeEnable;

    }

    public void setPlacementId(String placementId)
    {
        mPlacementId = placementId;

    }

    public void setRetryNumber(int retryNumber)
    {
        mRetryNumber = retryNumber;

    }

    public void setHeightMode(int heightMode)
    {
        mHeightMode = heightMode;

    }

    public void onClick(View view)
    {
        if(view.getId() == R.id.banner_close)
        {
            setVisibility(View.GONE);
            mRootViewGroup.setVisibility(View.GONE);
        }
    }

    public void loadAd()
    {
        switch(mHeightMode){
            case 0:mAdView = new AdView(mContext,mPlacementId,AdSize.BANNER_HEIGHT_50);break;
            case 1:mAdView = new AdView(mContext,mPlacementId,AdSize.BANNER_HEIGHT_90);break;
            case 2:mAdView = new AdView(mContext,mPlacementId,AdSize.RECTANGLE_HEIGHT_250);break;
        }/***********************************************************/
        mBannerListener = new BannerListener(this,mRootViewGroup,mAdView,mCloseButton,mCloseEnable,mRetryNumber);
        for(int index = 0;index < mRootViewGroup.getChildCount();index++){
            if(mRootViewGroup.getChildAt(index) instanceof AdView)
            {
                mRootViewGroup.removeViewAt(index);
            }
        }
        mRootViewGroup.addView(mAdView,0);
        mAdView.setAdListener(mBannerListener);
        if(null != mAdView)
        {
            mAdView.loadAd();
        }
        else
        {
            if(isPrintLog)
                Log.i("banner","mAdView为Null导致无法请求FaceBook横幅广告资源！");
        }
    }

    public void destroy()
    {
        if(null != mAdView)
        {
            mAdView.destroy();
            mAdView = null;
        }
    }
}