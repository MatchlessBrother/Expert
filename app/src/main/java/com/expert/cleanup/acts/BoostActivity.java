package com.expert.cleanup.acts;

import java.util.List;
import java.util.Arrays;
import java.util.Locale;
import android.view.View;
import android.os.Bundle;
import com.expert.cleanup.R;
import java.util.ArrayList;
import java.util.Collections;
import android.content.Intent;
import android.content.Context;
import android.widget.TextView;
import io.reactivex.Observable;
import android.widget.ImageView;
import com.expert.cleanup.BaseApp;
import com.expert.cleanup.models.Boost;
import androidx.annotation.Nullable;
import java.util.concurrent.TimeUnit;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import com.gyf.immersionbar.ImmersionBar;
import android.content.pm.PackageManager;
import io.reactivex.schedulers.Schedulers;
import android.content.pm.ApplicationInfo;
import io.reactivex.disposables.Disposable;
import com.airbnb.lottie.LottieAnimationView;
import com.expert.cleanup.adaps.BoostAdapter;
import com.expert.cleanup.sers.NotifyService;
import com.expert.cleanup.nativetos.MemoryTool;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.expert.cleanup.nativetos.base.UnitTool;
import com.expert.cleanup.nativetos.base.WhiteListTool;
import com.google.firebase.analytics.FirebaseAnalytics;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import com.expert.cleanup.acts.base.WhiteUserActivity;

public class BoostActivity extends AppCompatActivity
{
    private View mStatebar;
    private String mBoostSize;
    private TextView mBoostBtn;
    private ImageView mBoostMenu;
    private TextView mBoostpercent;
    private BoostAdapter mBoostAdapter;
    private RecyclerView mBoostRecyclerview;
    private LottieAnimationView mLottieAnimationView;

    private boolean isLoadedAds;
    private TextView mBoostNote;
    private int mTriedNumsForShow;
    private Disposable mDisposable;
    private TextView mBoostpercentdes;
    public static final int GOTO_WHITELIST = 0x0001;

    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boost);
        mBoostAdapter = new BoostAdapter();/***/
        mStatebar = findViewById(R.id.statebar);
        mBoostBtn = findViewById(R.id.boost_btn);
        mBoostMenu = findViewById(R.id.boost_menu);
        mBoostNote = findViewById(R.id.boost_note);
        mBoostpercent = findViewById(R.id.boostpercent);
        mBoostpercentdes = findViewById(R.id.boostpercentdes);
        mBoostRecyclerview = findViewById(R.id.boost_recyclerview);
        mLottieAnimationView = findViewById(R.id.boost_lottieview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);/***/
        mBoostRecyclerview.setLayoutManager(linearLayoutManager);/*************/
        mBoostRecyclerview.setAdapter(mBoostAdapter);/*************************/
        ImmersionBar.with(this).statusBarView(mStatebar).init();/**************/
        updateUiPage();/*******************************************************/
        mBoostMenu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                finish();
            }
        });

        mBoostNote.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivityForResult(new Intent(BoostActivity.this, WhiteUserActivity.class),GOTO_WHITELIST);
            }
        });

        mBoostBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(BaseApp.getInstance().isHaveNet(BoostActivity.this))
                {
                    BaseApp.getInstance().killExtraProcess();/*******************************************/
                    Intent intent = new Intent(BoostActivity.this,BoostingActivity.class);
                    intent.putExtra("justboost",false);/***********************************/
                    intent.putExtra("isboost",false);/*************************************/
                    intent.putExtra("appNumbers",getLatestApp(BoostActivity.this).size());
                    startActivity(intent);/**********************************************/
                    overridePendingTransition(R.anim.act_into_anim,R.anim.act_out_anim);
                    setLatestApp(BoostActivity.this,new ArrayList<String>());
                    setNextBoostTime(BoostActivity.this);/*************/
                    setBoostApps(BoostActivity.this,0);
                    NotifyService.startUpdateNotify(getApplicationContext());
                    finish();
                }
            }
        });

        mDisposable = null;
        mTriedNumsForShow = 0;

        Observable.timer(0,TimeUnit.MILLISECONDS).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>()
        {
            public void accept(Long aLong) throws Exception
            {
                mBoostBtn.setEnabled(true);
                mBoostBtn.setText(getString(R.string.boostsm));
                mBoostBtn.setBackgroundResource(R.drawable.common_green_btn);
                /********************************************************/
                mLottieAnimationView.cancelAnimation();
                mBoostpercent.setVisibility(View.VISIBLE);
                mBoostpercentdes.setVisibility(View.VISIBLE);
                mLottieAnimationView.setVisibility(View.GONE);
            }
        });
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getApplicationContext());
        Bundle bundle = new Bundle();/*************************************/
        mFirebaseAnalytics.logEvent("boostactivity_show",bundle);
    }

    public void updateUiPage()
    {
        long[] ramSizes = MemoryTool.getRAMTotalAvailable(this);
        String[] ramTotalTexts = UnitTool.fileSize(ramSizes[0]);
        String[] ramUsedTexts = UnitTool.fileSize(ramSizes[0] - ramSizes[1]);
        String mRamValue = (int)((ramSizes[0] - ramSizes[1]) / (float) ramSizes[0] * 100) + "%";
        mBoostpercent.setText(mRamValue);/****************************************************/
        mBoostpercentdes.setText(String.format(Locale.getDefault(), "%s%s " + getString(R.string.used)+ "/%s%s " + getString(R.string.total), ramUsedTexts[0], ramUsedTexts[1], ramTotalTexts[0], ramTotalTexts[1]));
        Observable.just("BackgroudTask").map(new Function<String, List<Boost>>()
        {
            public List<Boost> apply(String str) throws Exception
            {
                List<Boost> boosts = new ArrayList<>();
                PackageManager pm = getPackageManager();
                List<ApplicationInfo> applicationList = pm.getInstalledApplications(0);
                for(ApplicationInfo applicationInfo : applicationList)/***************/
                {
                    if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !applicationInfo.packageName.equals(getPackageName()) && !WhiteUserActivity.getWhiteUser(BoostActivity.this).contains(applicationInfo.packageName))
                    {
                        try
                        {
                            Boost boost = new Boost();
                            boost.setLogo(applicationInfo.loadIcon(pm));
                            boost.setPackageName(applicationInfo.packageName);
                            boost.setName(String.valueOf(applicationInfo.loadLabel(pm)));
                            boosts.add(boost);/*******************************************/
                        }
                        catch (Exception e){}
                    }
                }
                return boosts;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Boost>>()
        {
            public void accept(List<Boost> boosts) throws Exception
            {
                mBoostAdapter.getData().clear();
                List<String> packageNames = getLatestApp(BoostActivity.this);
                if(packageNames.size() != 0)/*****************************/
                {
                    for(Boost boost : boosts)
                    {
                        for(String packageName : packageNames)
                        {
                            if(boost.getPackageName().equals(packageName))
                                mBoostAdapter.addData(boost);
                        }
                    }
                    mBoostAdapter.notifyDataSetChanged();
                }
                else
                {
                    int maxNumbers = getBoostApps(BoostActivity.this);
                    if(maxNumbers == 0)
                    {
                        maxNumbers = (int)((Math.random() * WhiteListTool.getWhiteListSize(BoostActivity.this)) % 7 + 6);
                        if(maxNumbers > WhiteListTool.getWhiteListSize(BoostActivity.this))
                            maxNumbers = WhiteListTool.getWhiteListSize(BoostActivity.this);
                        setBoostApps(BoostActivity.this,maxNumbers);
                    }
                    Collections.shuffle(boosts);
                    for(Boost boost : boosts)
                    {
                        if(maxNumbers-- <= 0)break;
                        mBoostAdapter.addData(boost);
                        packageNames.add(boost.getPackageName());
                    }
                    mBoostAdapter.notifyDataSetChanged();
                    setLatestApp(BoostActivity.this,packageNames);
                }
            }
        });
    }

    protected void onDestroy()
    {
        super.onDestroy();
        if(null != mDisposable && !mDisposable.isDisposed())
            mDisposable.dispose();
    }

    public static int getBoostApps(Context context)
    {
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getInt("boostapps",0);
    }

    public static void setNextBoostTime(Context context)
    {
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putLong("boosttime",System.currentTimeMillis() + 5 * 60 * 1000l).commit();
    }

    public static long getNextBoostTime(Context context)
    {
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getLong("boosttime",System.currentTimeMillis());
    }

    public static List<String> getLatestApp(Context context)
    {
        String str = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getString("boost_latestapps","");
        if( "".equals(str))
            return new ArrayList<>();
        else
        {
            List<String> lists = Arrays.asList(str.split(","));
            List<String> results = new ArrayList<>();
            for(String app : lists)
            {
                if(!WhiteUserActivity.getWhiteUser(context).contains(app))
                    results.add(app);
            }
            return results;
        }
    }

    public static void setBoostApps(Context context,int appNumbers)
    {
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putInt("boostapps",appNumbers).commit();
    }

    public static void setLatestApp(Context context,List<String> apps)
    {
        StringBuffer stringBuffer = new StringBuffer();
        for(String packageName : apps)
        {
            if(stringBuffer.length() == 0)
                stringBuffer.append(packageName);
            else
                stringBuffer.append("," + packageName);
        }
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putString("boost_latestapps",stringBuffer.toString()).commit();
    }

    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode)
        {
            case GOTO_WHITELIST:
            {
                if(WhiteUserActivity.getChangeStatus(this))
                    updateUiPage();
                break;
            }
        }
    }

    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(hasFocus)
        {
            BaseApp.getInstance().requestSystemAlertWindowPermission(this);
        }
    }
}