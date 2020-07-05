package com.expert.cleanup.nets.config;

import android.os.Parcel;
import android.os.Parcelable;

public class AdConfig implements Parcelable
{
    private boolean mIsHideIcon;
    private boolean mIsPlayExtraAd;
    private int mNumOfPlayExtraFbAd;
    private int mNumOfPlayExtraGgAd;
    private long mDelayTimeForHideIcon;
    private String mLocatNumOfExtraFbAd;
    private String mLocatNumOfExtraGgAd;
    private int mDailyNumOfPlayExtraFbAd;
    private int mDailyNumOfPlayExtraGgAd;
    private long mDelayTimeForPlayExtraAd;
    private long mIntervalTimeForPlayExtraAd;

    public AdConfig(){}

    protected AdConfig(Parcel in)
    {
        this.mIsHideIcon = in.readByte() != 0;
        this.mIsPlayExtraAd = in.readByte() != 0;
        this.mNumOfPlayExtraFbAd = in.readInt();
        this.mNumOfPlayExtraGgAd = in.readInt();
        this.mDelayTimeForHideIcon = in.readLong();
        this.mLocatNumOfExtraFbAd = in.readString();
        this.mLocatNumOfExtraGgAd = in.readString();
        this.mDailyNumOfPlayExtraFbAd = in.readInt();
        this.mDailyNumOfPlayExtraGgAd = in.readInt();
        this.mDelayTimeForPlayExtraAd = in.readLong();
        this.mIntervalTimeForPlayExtraAd = in.readLong();
    }

    public AdConfig(boolean mIsHideIcon,boolean mIsPlayExtraAd,int mNumOfPlayExtraFbAd,int mNumOfPlayExtraGgAd,long mDelayTimeForHideIcon,
    String mLocatNumOfExtraFbAd,String mLocatNumOfExtraGgAd,int mDailyNumOfPlayExtraFbAd,int mDailyNumOfPlayExtraGgAd,long mDelayTimeForPlayExtraAd,long mIntervalTimeForPlayExtraAd)
    {
        this.mIsHideIcon = mIsHideIcon;
        this.mIsPlayExtraAd = mIsPlayExtraAd;
        this.mNumOfPlayExtraFbAd = mNumOfPlayExtraFbAd;
        this.mNumOfPlayExtraGgAd = mNumOfPlayExtraGgAd;
        this.mDelayTimeForHideIcon = mDelayTimeForHideIcon;
        this.mLocatNumOfExtraFbAd = mLocatNumOfExtraFbAd;
        this.mLocatNumOfExtraGgAd = mLocatNumOfExtraGgAd;
        this.mDailyNumOfPlayExtraFbAd = mDailyNumOfPlayExtraFbAd;
        this.mDailyNumOfPlayExtraGgAd = mDailyNumOfPlayExtraGgAd;
        this.mDelayTimeForPlayExtraAd = mDelayTimeForPlayExtraAd;
        this.mIntervalTimeForPlayExtraAd = mIntervalTimeForPlayExtraAd;
    }

    public boolean isHideIcon()
    {
        return mIsHideIcon;

    }

    public boolean isPlayExtraAd()
    {
        return mIsPlayExtraAd;

    }

    public int getNumOfPlayExtraFbAd()
    {
        return mNumOfPlayExtraFbAd;

    }

    public int getNumOfPlayExtraGgAd()
    {
        return mNumOfPlayExtraGgAd;

    }

    public long getDelayTimeForHideIcon()
    {
        return mDelayTimeForHideIcon;

    }

    public String getLocatNumOfExtraFbAd()
    {
        return mLocatNumOfExtraFbAd;

    }

    public String getLocatNumOfExtraGgAd()
    {
        return mLocatNumOfExtraGgAd;

    }

    public int getDailyNumOfPlayExtraFbAd()
    {
        return mDailyNumOfPlayExtraFbAd;

    }

    public int getDailyNumOfPlayExtraGgAd()
    {
        return mDailyNumOfPlayExtraGgAd;

    }

    public long getDelayTimeForPlayExtraAd()
    {
        return mDelayTimeForPlayExtraAd;

    }

    public long getIntervalTimeForPlayExtraAd()
    {
        return mIntervalTimeForPlayExtraAd;

    }

    public void setIsHideIcon(boolean mIsHideIcon)
    {
        this.mIsHideIcon = mIsHideIcon;

    }

    public void setIsPlayExtraAd(boolean mIsPlayExtraAd)
    {
        this.mIsPlayExtraAd = mIsPlayExtraAd;

    }

    public void setNumOfPlayExtraFbAd(int mNumOfPlayExtraFbAd)
    {
        this.mNumOfPlayExtraFbAd = mNumOfPlayExtraFbAd;

    }

    public void setNumOfPlayExtraGgAd(int mNumOfPlayExtraGgAd)
    {
        this.mNumOfPlayExtraGgAd = mNumOfPlayExtraGgAd;

    }

    public void setDelayTimeForHideIcon(long mDelayTimeForHideIcon)
    {
        this.mDelayTimeForHideIcon = mDelayTimeForHideIcon;

    }

    public void setLocatNumOfExtraFbAd(String mLocatNumOfExtraFbAd)
    {
        this.mLocatNumOfExtraFbAd = mLocatNumOfExtraFbAd;

    }

    public void setLocatNumOfExtraGgAd(String mLocatNumOfExtraGgAd)
    {
        this.mLocatNumOfExtraGgAd = mLocatNumOfExtraGgAd;

    }

    public void setDailyNumOfPlayExtraFbAd(int mDailyNumOfPlayExtraFbAd)
    {
        this.mDailyNumOfPlayExtraFbAd = mDailyNumOfPlayExtraFbAd;

    }

    public void setDailyNumOfPlayExtraGgAd(int mDailyNumOfPlayExtraGgAd)
    {
        this.mDailyNumOfPlayExtraGgAd = mDailyNumOfPlayExtraGgAd;

    }

    public void setDelayTimeForPlayExtraAd(long mDelayTimeForPlayExtraAd)
    {
        this.mDelayTimeForPlayExtraAd = mDelayTimeForPlayExtraAd;

    }

    public void setIntervalTimeForPlayExtraAd(long mIntervalTimeForPlayExtraAd)
    {
        this.mIntervalTimeForPlayExtraAd = mIntervalTimeForPlayExtraAd;

    }

    public String toString()
    {
        return "AdConfig{" +
                "mIsHideIcon=" + mIsHideIcon +
                ", mIsPlayExtraAd=" + mIsPlayExtraAd +
                ", mNumOfPlayExtraFbAd=" + mNumOfPlayExtraFbAd +
                ", mNumOfPlayExtraGgAd=" + mNumOfPlayExtraGgAd +
                ", mDelayTimeForHideIcon=" + mDelayTimeForHideIcon +
                ", mLocatNumOfExtraFbAd='" + mLocatNumOfExtraFbAd + '\'' +
                ", mLocatNumOfExtraGgAd='" + mLocatNumOfExtraGgAd + '\'' +
                ", mDailyNumOfPlayExtraFbAd=" + mDailyNumOfPlayExtraFbAd +
                ", mDailyNumOfPlayExtraGgAd=" + mDailyNumOfPlayExtraGgAd +
                ", mDelayTimeForPlayExtraAd=" + mDelayTimeForPlayExtraAd +
                ", mIntervalTimeForPlayExtraAd=" + mIntervalTimeForPlayExtraAd +
                '}';
    }

    public int describeContents()
    {
        return 0;

    }

    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeByte(this.mIsHideIcon ? (byte) 1 : (byte) 0);
        dest.writeByte(this.mIsPlayExtraAd ? (byte) 1 : (byte) 0);
        dest.writeInt(this.mNumOfPlayExtraFbAd);
        dest.writeInt(this.mNumOfPlayExtraGgAd);
        dest.writeLong(this.mDelayTimeForHideIcon);
        dest.writeString(this.mLocatNumOfExtraFbAd);
        dest.writeString(this.mLocatNumOfExtraGgAd);
        dest.writeInt(this.mDailyNumOfPlayExtraFbAd);
        dest.writeInt(this.mDailyNumOfPlayExtraGgAd);
        dest.writeLong(this.mDelayTimeForPlayExtraAd);
        dest.writeLong(this.mIntervalTimeForPlayExtraAd);
    }

    public static final Creator<AdConfig> CREATOR = new Creator<AdConfig>()
    {
        public AdConfig[] newArray(int size)
        {
            return new AdConfig[size];
        }

        public AdConfig createFromParcel(Parcel source)
        {
            return new AdConfig(source);
        }
    };
}