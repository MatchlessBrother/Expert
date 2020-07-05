package com.expert.cleanup.acts;

import java.util.List;
import java.util.Arrays;
import android.view.View;
import android.os.Bundle;
import com.expert.cleanup.R;
import java.util.ArrayList;
import java.util.Collections;
import android.content.Intent;
import java.text.DecimalFormat;
import android.content.Context;
import android.widget.TextView;
import io.reactivex.Observable;
import android.widget.ImageView;
import com.expert.cleanup.BaseApp;
import com.expert.cleanup.models.Cpu;
import androidx.annotation.Nullable;
import java.util.concurrent.TimeUnit;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import com.gyf.immersionbar.ImmersionBar;
import android.content.pm.PackageManager;
import io.reactivex.schedulers.Schedulers;
import android.content.pm.ApplicationInfo;
import io.reactivex.disposables.Disposable;
import com.expert.cleanup.adaps.CpuAdapter;
import com.airbnb.lottie.LottieAnimationView;
import com.expert.cleanup.sers.NotifyService;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.expert.cleanup.nativetos.HardwareTool;
import com.expert.cleanup.nativetos.base.WhiteListTool;
import com.google.firebase.analytics.FirebaseAnalytics;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.expert.cleanup.acts.base.WhiteUserActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class CpuActivity extends AppCompatActivity
{
    private View mStatebar;
    private TextView mCpuBtn;
    private ImageView mCpuMenu;
    private String mTemperature;
    private TextView mCpupercent;
    private CpuAdapter mCpuAdapter;
    private RecyclerView mCpuRecyclerview;
    private LottieAnimationView mLottieAnimationView;

    private TextView mCpuNote;
    private boolean isLoadedAds;
    private int mTriedNumsForShow;
    private Disposable mDisposable;
    private TextView mCpupercentdes;

    public static final int GOTO_WHITELIST = 0x0001;

    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cpu);
        mCpuAdapter = new CpuAdapter();/****/
        mCpuBtn = findViewById(R.id.cpu_btn);
        mCpuMenu = findViewById(R.id.cpu_menu);
        mStatebar = findViewById(R.id.statebar);
        mCpuNote = findViewById(R.id.cpu_note);
        mCpupercent = findViewById(R.id.cpupercent);
        mCpupercentdes = findViewById(R.id.cpupercentdes);
        mCpuRecyclerview = findViewById(R.id.cpu_recyclerview);
        mLottieAnimationView = findViewById(R.id.cpu_lottieview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);/***/
        mCpuRecyclerview.setLayoutManager(linearLayoutManager);/***************/
        ImmersionBar.with(this).statusBarView(mStatebar).init();/**************/
        mCpuRecyclerview.setAdapter(mCpuAdapter);/*****************************/
        updateUiPage();/*******************************************************/
        mCpuMenu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                finish();
            }
        });

        mCpuNote.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivityForResult(new Intent(CpuActivity.this, WhiteUserActivity.class),GOTO_WHITELIST);
            }
        });

        mCpuBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(BaseApp.getInstance().isHaveNet(CpuActivity.this))
                {
                    BaseApp.getInstance().killExtraProcess();/***************************************/
                    Intent intent = new Intent(CpuActivity.this,CpuingActivity.class);
                    intent.putExtra("justcpucooler",false);/***************************/
                    intent.putExtra("iscooler",false);/********************************/
                    intent.putExtra("appNumbers",getLatestApp(CpuActivity.this).size());
                    intent.putExtra("temperature",mTemperature);/*****************************/
                    startActivity(intent);/**********************************************************/
                    overridePendingTransition(R.anim.act_into_anim,R.anim.act_out_anim);
                    HardwareTool.saveCpuCoolerTime(CpuActivity.this);
                    setLatestApp(CpuActivity.this,new ArrayList<String>());
                    setNextCoolDownTime(CpuActivity.this);/************/
                    setCoolerCpuApps(CpuActivity.this,0);
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
                mCpuBtn.setEnabled(true);
                mCpuBtn.setText(getString(R.string.cooldown));
                mCpuBtn.setBackgroundResource(R.drawable.common_green_btn);

                mCpupercent.setVisibility(View.VISIBLE);
                mLottieAnimationView.cancelAnimation();
                mCpupercentdes.setVisibility(View.VISIBLE);
                mLottieAnimationView.setVisibility(View.GONE);
            }
        });
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getApplicationContext());
        Bundle bundle = new Bundle();/*************************************/
        mFirebaseAnalytics.logEvent("cpuactivity_show",bundle);
    }

    public void updateUiPage()
    {
        mTemperature = new DecimalFormat("#.0").format(HardwareTool.getCpuTemperatureFinder(this)) + "℃" + "/" +  (new DecimalFormat("#.0").format(32+ HardwareTool.getCpuTemperatureFinder(this) * 1.8))+ "℉";
        mCpupercent.setText(mTemperature);/************************************************/
        Observable.just("BackgroudTask").map(new Function<String, List<Cpu>>()
        {
            public List<Cpu> apply(String str) throws Exception
            {
                List<Cpu> cpus = new ArrayList<>();
                PackageManager pm = getPackageManager();
                List<ApplicationInfo> applicationList = pm.getInstalledApplications(0);
                for(ApplicationInfo applicationInfo : applicationList)/***************/
                {
                    if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !applicationInfo.packageName.equals(getPackageName()) && !WhiteUserActivity.getWhiteUser(CpuActivity.this).contains(applicationInfo.packageName))
                    {
                        try
                        {
                            Cpu cpu = new Cpu();
                            cpu.setLogo(applicationInfo.loadIcon(pm));
                            cpu.setPackageName(applicationInfo.packageName);
                            cpu.setName(String.valueOf(applicationInfo.loadLabel(pm)));
                            cpus.add(cpu);/*******************************************/
                        }
                        catch (Exception e){}
                    }
                }
                return cpus;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Cpu>>()
        {
            public void accept(List<Cpu> cpus) throws Exception
            {
                mCpuAdapter.getData().clear();
                List<String> packageNames = getLatestApp(CpuActivity.this);
                if(packageNames.size() != 0)/*****************************/
                {
                    for(Cpu cpu : cpus)
                    {
                        for(String packageName : packageNames)
                        {
                            if(cpu.getPackageName().equals(packageName))
                                mCpuAdapter.addData(cpu);
                        }
                    }
                    mCpuAdapter.notifyDataSetChanged();
                }
                else
                {
                    int maxNumbers = getCoolerCpuApps(CpuActivity.this);
                    if(maxNumbers == 0)
                    {
                        maxNumbers = (int)((Math.random() * WhiteListTool.getWhiteListSize(CpuActivity.this)) % 7 + 6);
                        if(maxNumbers > WhiteListTool.getWhiteListSize(CpuActivity.this))
                            maxNumbers = WhiteListTool.getWhiteListSize(CpuActivity.this);
                        setCoolerCpuApps(CpuActivity.this,maxNumbers);
                    }
                    Collections.shuffle(cpus);/************************/
                    for(Cpu cpu : cpus)
                    {
                        if(maxNumbers-- <= 0)break;
                        mCpuAdapter.addData(cpu);
                        packageNames.add(cpu.getPackageName());
                    }
                    mCpuAdapter.notifyDataSetChanged();
                    setLatestApp(CpuActivity.this,packageNames);
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

    public static int getCoolerCpuApps(Context context)
    {
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getInt("coolercpuapps",0);
    }

    public static void setNextCoolDownTime(Context context)
    {
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putLong("cooldowntime",System.currentTimeMillis() + 5 * 60 * 1000l).commit();
    }

    public static long getNextCoolDownTime(Context context)
    {
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getLong("cooldowntime",System.currentTimeMillis());
    }

    public static List<String> getLatestApp(Context context)
    {
        String str = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getString("cpu_latestapps","");
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
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putString("cpu_latestapps",stringBuffer.toString()).commit();
    }

    public static void setCoolerCpuApps(Context context,int appNumbers)
    {
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putInt("coolercpuapps",appNumbers).commit();
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
