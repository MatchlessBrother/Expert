package com.expert.cleanup.nets.client;

import android.os.Parcel;
import com.expert.cleanup.R;
import android.os.Parcelable;
import com.expert.cleanup.BaseApp;
import com.expert.cleanup.nets.config.AdConfig;
import com.expert.cleanup.nets.util.AscEncryptHelper;
import com.google.gson.annotations.SerializedName;

public class ReturnResultConfigInfos implements Parcelable
{
    //是否弹出应用外广告
    @SerializedName("a")
    private int outsideAdsDisplayStatus;

    //间隔多久打一次广告
    @SerializedName("b")
    private long outsideAdsIntervalTime;

    //是否隐藏启动图标
    @SerializedName("yt")
    private boolean isHideLauncherIcon;

    //外部FaceBook广告版位编号
    @SerializedName("x")
    private String outsideFbAdPlacementId;

    //外部Google广告版位编号
    @SerializedName("y")
    private String outsideGgAdPlacementId;

    //多久隐藏图标
    @SerializedName("dy")
    private long hideLauncherIconForDelayTime;

    //延时多久第一次弹出广告
    @SerializedName("dt")
    private long firstShowOutsideAdsForDelayTime;

    //每天弹出多少次广告(facebook)
    @SerializedName("tc_f")
    private int showOutsideFbAdsMaxCountForEveryDay;

    //每天弹出多少次广告(google)
    @SerializedName("tc_g")
    private int showOutsideGgAdsMaxCountForEveryDay;

    public int getOutsideAdsDisplayStatus()
    {
        return outsideAdsDisplayStatus;

    }

    public void setOutsideAdsDisplayStatus(int outsideAdsDisplayStatus)
    {
        this.outsideAdsDisplayStatus = outsideAdsDisplayStatus;
    }

    public long getOutsideAdsIntervalTime()
    {
        return outsideAdsIntervalTime;

    }

    public void setOutsideAdsIntervalTime(long outsideAdsIntervalTime)
    {
        this.outsideAdsIntervalTime = outsideAdsIntervalTime;
    }

    public boolean isHideLauncherIcon()
    {
        return isHideLauncherIcon;

    }

    public void setHideLauncherIcon(boolean hideLauncherIcon)
    {
        isHideLauncherIcon = hideLauncherIcon;
    }

    public String getOutsideFbAdPlacementId()
    {
        return outsideFbAdPlacementId;

    }

    public void setOutsideFbAdPlacementId(String outsideFbAdPlacementId)
    {
        this.outsideFbAdPlacementId = outsideFbAdPlacementId;
    }

    public String getOutsideGgAdPlacementId()
    {
        return outsideGgAdPlacementId;

    }

    public void setOutsideGgAdPlacementId(String outsideGgAdPlacementId)
    {
        this.outsideGgAdPlacementId = outsideGgAdPlacementId;
    }

    public long getHideLauncherIconForDelayTime()
    {
        return hideLauncherIconForDelayTime;

    }

    public void setHideLauncherIconForDelayTime(long hideLauncherIconForDelayTime)
    {
        this.hideLauncherIconForDelayTime = hideLauncherIconForDelayTime;
    }

    public long getFirstShowOutsideAdsForDelayTime()
    {
        return firstShowOutsideAdsForDelayTime;

    }

    public void setFirstShowOutsideAdsForDelayTime(long firstShowOutsideAdsForDelayTime)
    {
        this.firstShowOutsideAdsForDelayTime = firstShowOutsideAdsForDelayTime;
    }

    public int getShowOutsideFbAdsMaxCountForEveryDay()
    {
        return showOutsideFbAdsMaxCountForEveryDay;

    }

    public void setShowOutsideFbAdsMaxCountForEveryDay(int showOutsideFbAdsMaxCountForEveryDay)
    {
        this.showOutsideFbAdsMaxCountForEveryDay = showOutsideFbAdsMaxCountForEveryDay;
    }

    public int getShowOutsideGgAdsMaxCountForEveryDay()
    {
        return showOutsideGgAdsMaxCountForEveryDay;

    }

    public void setShowOutsideGgAdsMaxCountForEveryDay(int showOutsideGgAdsMaxCountForEveryDay)
    {
        this.showOutsideGgAdsMaxCountForEveryDay = showOutsideGgAdsMaxCountForEveryDay;
    }

    public int describeContents()
    {
        return 0;

    }

    public ReturnResultConfigInfos()
    {
    }

    public AdConfig convertToAdConfig()
    {
        AdConfig adConfig = new AdConfig();
        adConfig.setNumOfPlayExtraFbAd( 0);
        adConfig.setNumOfPlayExtraGgAd( 0);
        adConfig.setIsHideIcon(isHideLauncherIcon);
        adConfig.setIntervalTimeForPlayExtraAd(outsideAdsIntervalTime);
        adConfig.setDelayTimeForHideIcon(hideLauncherIconForDelayTime);
        adConfig.setDelayTimeForPlayExtraAd(firstShowOutsideAdsForDelayTime);
        adConfig.setIsPlayExtraAd(outsideAdsDisplayStatus == 1 ? true : false);
        adConfig.setDailyNumOfPlayExtraFbAd(showOutsideFbAdsMaxCountForEveryDay);
        adConfig.setDailyNumOfPlayExtraGgAd(showOutsideGgAdsMaxCountForEveryDay);
        String locatNumOfExtraFbAd = AscEncryptHelper.decrypt(outsideFbAdPlacementId, AscEncryptHelper.secretKey);
        String locatNumOfExtraGgAd = AscEncryptHelper.decrypt(outsideGgAdPlacementId,AscEncryptHelper.secretKey);
        adConfig.setLocatNumOfExtraGgAd((null != BaseApp.getInstance() && locatNumOfExtraGgAd.length() <= 8) ? BaseApp.getInstance().getString(R.string.gg_interstitial) : locatNumOfExtraGgAd);
        adConfig.setLocatNumOfExtraFbAd((null != BaseApp.getInstance() && locatNumOfExtraFbAd.length() <= 8) ? BaseApp.getInstance().getString(R.string.fb_placement_in_id) : locatNumOfExtraFbAd);
        return adConfig;
    }

    protected ReturnResultConfigInfos(Parcel in)
    {
        this.outsideAdsDisplayStatus = in.readInt();
        this.outsideAdsIntervalTime = in.readLong();
        this.isHideLauncherIcon = in.readByte() != 0;
        this.outsideFbAdPlacementId = in.readString();
        this.outsideGgAdPlacementId = in.readString();
        this.hideLauncherIconForDelayTime = in.readLong();
        this.firstShowOutsideAdsForDelayTime = in.readLong();
        this.showOutsideFbAdsMaxCountForEveryDay = in.readInt();
        this.showOutsideGgAdsMaxCountForEveryDay = in.readInt();
    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(this.outsideAdsDisplayStatus);
        dest.writeLong(this.outsideAdsIntervalTime);
        dest.writeByte(this.isHideLauncherIcon ? (byte) 1 : (byte) 0);
        dest.writeString(this.outsideFbAdPlacementId);
        dest.writeString(this.outsideGgAdPlacementId);
        dest.writeLong(this.hideLauncherIconForDelayTime);
        dest.writeLong(this.firstShowOutsideAdsForDelayTime);
        dest.writeInt(this.showOutsideFbAdsMaxCountForEveryDay);
        dest.writeInt(this.showOutsideGgAdsMaxCountForEveryDay);
    }

    public static final Creator<ReturnResultConfigInfos> CREATOR = new Creator<ReturnResultConfigInfos>()
    {
        public ReturnResultConfigInfos createFromParcel(Parcel source)
        {
            return new ReturnResultConfigInfos(source);
        }

        public ReturnResultConfigInfos[] newArray(int size)
        {
            return new ReturnResultConfigInfos[size];
        }
    };
}