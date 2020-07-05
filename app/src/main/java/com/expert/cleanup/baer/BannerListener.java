package com.expert.cleanup.baer;

import android.util.Log;
import android.view.View;
import com.facebook.ads.Ad;
import android.view.ViewGroup;
import com.facebook.ads.AdView;
import android.widget.ImageView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import static com.expert.cleanup.baer.BannerHelper.isPrintLog;

public class BannerListener implements AdListener
{
    private AdView mAdView;
    private int mRetryCount;
    private int mTempRetryCount;
    private boolean mCloseEnable;
    private BannerView mBannerView;
    private ImageView mCloseButton;
    private ViewGroup mRootViewGroup;

    public BannerListener(BannerView bannerView,ViewGroup rootViewGroup,AdView adView,ImageView closeButton,boolean closeEnable,int retryCount)
    {
        mAdView = adView;
        mBannerView = bannerView;
        mCloseEnable = closeEnable;
        mCloseButton = closeButton;
        mRootViewGroup = rootViewGroup;
        mTempRetryCount = mRetryCount = retryCount;
    }

    public void onAdLoaded(Ad var1)
    {
        if(isPrintLog)
            Log.i("banner","成功请求到FaceBook横幅广告资源！");
        mTempRetryCount = mRetryCount;
        mBannerView.setVisibility(View.VISIBLE);
        mRootViewGroup.setVisibility(View.VISIBLE);
        if(!mCloseEnable)
            mCloseButton.setVisibility(View.GONE);
        else
            mCloseButton.setVisibility(View.VISIBLE);
    }

    public void onAdClicked(Ad var1)
    {
        if(isPrintLog)
            Log.i("banner","点击了FaceBook横幅广告资源！");
    }

    public void onLoggingImpression(Ad var1)
    {
        if(isPrintLog)
            Log.i("banner","即将显示FaceBook横幅广告资源！");
        mTempRetryCount = mRetryCount;
        mBannerView.setVisibility(View.VISIBLE);
        mRootViewGroup.setVisibility(View.VISIBLE);
        if(!mCloseEnable)
            mCloseButton.setVisibility(View.GONE);
        else
            mCloseButton.setVisibility(View.VISIBLE);
    }

    public void onError(Ad var1,AdError adError)
    {
        if(isPrintLog)
            Log.i("banner","请求FaceBook横幅广告资源失败：" + adError.getErrorMessage());
        if(mTempRetryCount > 0)
        {
            mTempRetryCount--;
            if(null != mAdView)
            {
                if(isPrintLog)
                    Log.i("banner","重试请求FaceBook横幅广告资源！");
                mAdView.loadAd();
            }
            else
            {
                if(isPrintLog)
                    Log.i("banner","mAdView为Null导致无法重试FaceBook横幅广告资源！");
                mTempRetryCount = mRetryCount;
            }
        }
        else
        {
            if(isPrintLog)
                Log.i("banner","请求FaceBook横幅广告资源次数已用完故无法重试！");
            mTempRetryCount = mRetryCount;
        }
    }
}