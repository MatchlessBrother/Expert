package com.expert.cleanup.acts.base;

import java.util.List;
import android.os.Bundle;
import android.view.View;
import com.expert.cleanup.R;
import java.util.ArrayList;
import java.util.Collections;
import android.content.Intent;
import io.reactivex.Observable;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.ImageView;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import io.reactivex.schedulers.Schedulers;

import com.expert.cleanup.acts.MainActivity;
import com.expert.cleanup.adaps.base.DialogAdapter;
import com.expert.cleanup.nativetos.ScreenInfosTool;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.expert.cleanup.receivers.ScreenStateReceive;
import com.google.firebase.analytics.FirebaseAnalytics;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class TableAdsActivity extends AppCompatActivity
{
    private TextView tv;
    private TextView mBtnOpen;
    private ImageView mCloseBtn;
    private RecyclerView mRecyclerview;
    private DialogAdapter mDialogAdapter;
    private FirebaseAnalytics mFirebaseAnalytics;

    private int random2 = (int)(20+Math.random()*(40-20+1));
    private int random3 = (int)(30+Math.random()*(60-30+1));
    private int random1 = (int)(100+Math.random()*(400-100+1));
    private int random4 = (int)(100+Math.random()*(400-100+1));
    private int random5 = (int)(100+Math.random()*(400-100+1));

    private String str2 = "Mass cache accumulated. Clean your phone right away.";
    private String str3 = "Memory usage is too high. Release memory by using Cleaner straight away.";
    private String str4 = "Apk files found. Clean your phone immediately.";
    private String str5 = "Battery-consuming apps are draining your power. Check and slow down battery draining.";

    private String str1 = "During last 153 mins, detected high CPU utility, boost your phone now.";
    private String str6 = "CPU temperature is 39% higher than avg. , cooling it down.";
    private String str7 = "Apps start slowly 57% than average speed, boost your phone to improve.";
    private String str8 = "Noticed lags occured in past 134 mins, boost your phone at once.";

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setLayout(ScreenInfosTool.getScreenWidth(this) - (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,getResources().getDisplayMetrics()),
                              ScreenInfosTool.getScreenWidth(this) - (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,10,getResources().getDisplayMetrics()));
        setContentView(R.layout.activity_table_ads);
        mRecyclerview = findViewById(R.id.recyclerview);
        ScreenStateReceive.setNextShowTime(this);
        mCloseBtn = findViewById(R.id.closeicon);
        mBtnOpen = findViewById(R.id.btn_open);
        tv = findViewById(R.id.tv_count);
        setFinishOnTouchOutside(false);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mDialogAdapter = new DialogAdapter();/***/
        mRecyclerview.setAdapter(mDialogAdapter);

        setDescriptionText((int)(Math.random() * 8 + 1));
        setApps((int)(Math.random() * 7 + 6));

        mCloseBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                finish();
            }
        });

        mBtnOpen.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(TableAdsActivity.this);
                Bundle bundle = new Bundle();/*******************************************/
                mFirebaseAnalytics.logEvent("dialog_click",bundle);/******************/
                Intent intent = new Intent(TableAdsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(TableAdsActivity.this);
        Bundle bundle = new Bundle();/**************************************/
        mFirebaseAnalytics.logEvent("dialog_show",bundle);/*****************/
    }

    private void setDescriptionText(int tag)
    {
        switch (tag)
        {
            case 1:
                tv.setText("During last " + random1 + " mins,detected high CPU utility. Boost your phone now.");
                break;
            case 2:
                tv.setText(str2);
                break;
            case 3:
                tv.setText(str3);
                break;
            case 4:
                tv.setText(str4);
                break;
            case 5:
                tv.setText(str5);
                break;
            case 6:
                tv.setText("CPU temperature is "+random2+"% higher than avg. during last "+random5+" mins.. Cool it down.");
                break;
            case 7:
                tv.setText("Apps start "+random3+"% slower than avg. speed. Boost your phone to improve.");
                break;
            case 8:
                tv.setText("Noticed lags occurred in past "+random4+" mins.. boost your phone at once.");
                break;
            default:
                break;
        }
    }

    private void setApps(final int appNums)
    {
        Observable.just("BackgroudTask").map(new Function<String, List<ApplicationInfo>>()
        {
            public List<ApplicationInfo> apply(String str) throws Exception
            {
                List<ApplicationInfo> apps = new ArrayList<>();
                PackageManager pm = getPackageManager();
                List<ApplicationInfo> applicationList = pm.getInstalledApplications(0);
                for(ApplicationInfo applicationInfo : applicationList)/***************/
                {
                    if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !applicationInfo.packageName.equals(getPackageName()) && !WhiteUserActivity.getWhiteUser(TableAdsActivity.this).contains(applicationInfo.packageName))
                    {
                       apps.add(applicationInfo);
                    }
                }
                return apps;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<ApplicationInfo>>()
        {
            public void accept(List<ApplicationInfo> apps) throws Exception
            {
                mDialogAdapter.getData().clear();
                int resultAppNums = appNums;
                Collections.shuffle(apps);
                while(apps.size() > 0)
                {
                    if(resultAppNums-- > 0)
                        mDialogAdapter.getData().add(apps.remove(0).loadIcon(getPackageManager()));
                    else
                        break;
                }
                mDialogAdapter.notifyDataSetChanged();
            }
        });
    }
}