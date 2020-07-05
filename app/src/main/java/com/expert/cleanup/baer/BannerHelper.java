package com.expert.cleanup.baer;

import android.util.Log;
import android.view.View;
import android.app.Activity;

public class BannerHelper
{
    private BannerView mbannerView;
    private static BannerHelper mInstance;
    public static boolean isPrintLog = true;

    private BannerHelper()
    {


    }

    public void showBanner()
    {
        if(null != mbannerView)
        {
            mbannerView.loadAd();
        }
        else
        {
            if(isPrintLog)
                Log.i("banner","因未能找到BannerView导致无法加载横幅广告!");
        }
    }

    public void destroyBanner()
    {
        if(null != mbannerView)
        {
            mbannerView.destroy();
        }
    }

    public void initBanner(View view, int bannerViewId)
    {
        mbannerView = view.findViewById(bannerViewId);
    }

    public synchronized static BannerHelper getInstance()
    {
        if(null == mInstance)
            mInstance = new BannerHelper();
        return mInstance;
    }

    public void initBanner(Activity activity,int bannerViewId)
    {
        mbannerView = activity.findViewById(bannerViewId);
    }
}