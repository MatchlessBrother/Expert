package com.expert.cleanup.acts;

import java.util.List;
import java.util.Arrays;
import android.os.Bundle;
import android.view.View;
import com.expert.cleanup.R;
import java.util.ArrayList;
import android.widget.Toast;
import java.util.Collections;
import android.content.Intent;
import android.content.Context;
import io.reactivex.Observable;
import android.widget.TextView;
import android.widget.ImageView;
import android.os.BatteryManager;
import com.expert.cleanup.BaseApp;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import android.content.IntentFilter;
import java.util.concurrent.TimeUnit;
import com.expert.cleanup.models.Battery;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import com.gyf.immersionbar.ImmersionBar;
import android.content.pm.PackageManager;
import android.content.pm.ApplicationInfo;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.disposables.Disposable;
import com.airbnb.lottie.LottieAnimationView;
import com.expert.cleanup.sers.NotifyService;
import com.expert.cleanup.adaps.BatteryAdapter;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.expert.cleanup.nativetos.base.WhiteListTool;
import com.google.firebase.analytics.FirebaseAnalytics;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import com.expert.cleanup.acts.base.WhiteUserActivity;

public class BatteryActivity extends AppCompatActivity
{
    private View mStatebar;
    private TextView mBatteryBtn;
    private ImageView mBatteryMenu;
    private boolean mBatteryChecked;
    private ImageView mBatteryCheck;
    private TextView mBatteryPercent;
    private LinearLayout mBatteryCheckAll;
    private BatteryAdapter mBatteryAdapter;
    private RecyclerView mBatteryRecyclerview;
    private LottieAnimationView mLottieAnimationView;

    private boolean isLoadedAds;
    private int mTriedNumsForShow;
    private Disposable mDisposable;

    private TextView mBatteryNote;
    private TextView mBatterypercentdes;
    public static final int GOTO_WHITELIST = 0x0001;

    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mBatteryAdapter = new BatteryAdapter();
        setContentView(R.layout.activity_battery);
        mStatebar  =  findViewById(R.id.statebar);
        mBatteryBtn = findViewById(R.id.battery_btn);
        mBatteryMenu = findViewById(R.id.battery_menu);
        mBatteryNote = findViewById(R.id.battery_note);
        mBatteryCheck = findViewById(R.id.batterycheck);
        mBatteryPercent = findViewById(R.id.batterypercent);
        mBatteryCheckAll = findViewById(R.id.batterycheckall);
        ImmersionBar.with(this).statusBarView(mStatebar).init();
        mBatterypercentdes = findViewById(R.id.batterypercentdes);
        mLottieAnimationView = findViewById(R.id.battery_lottieview);
        mBatteryRecyclerview = findViewById(R.id.battery_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);/***/
        mBatteryRecyclerview.setLayoutManager(linearLayoutManager);/***********/
        mBatteryRecyclerview.setAdapter(mBatteryAdapter);/*********************/
        updateUiPage();/*******************************************************/
        mBatteryAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            public void onItemChildClick(BaseQuickAdapter adapter,View view,int position)
            {
                Battery battery = (Battery)adapter.getItem(position);
                battery.setSelect(!battery.isSelect());
                adapter.notifyDataSetChanged();
                if(battery.isSelect())
                {
                    for(int index = 0;index < mBatteryAdapter.getData().size();index++)
                    {
                        if(!mBatteryAdapter.getItem(index).isSelect())
                        {
                            mBatteryCheck.setBackgroundResource(R.mipmap.icon_uncheck_all);
                            return;
                        }
                        if(index == mBatteryAdapter.getData().size() - 1)
                        {
                            mBatteryCheck.setBackgroundResource(R.mipmap.icon_check_all);
                        }
                    }
                }
                else
                {
                    for(int index = 0;index < mBatteryAdapter.getData().size();index++)
                    {
                        if(mBatteryAdapter.getItem(index).isSelect())
                        {
                            mBatteryCheck.setBackgroundResource(R.mipmap.icon_uncheck_all);
                            return;
                        }
                        if(index == mBatteryAdapter.getData().size() - 1)
                        {
                            mBatteryCheck.setBackgroundResource(R.mipmap.ic_uncheck);
                        }
                    }
                }
            }
        });

        mBatteryCheck.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(mBatteryChecked)
                {
                    mBatteryChecked = false;
                    mBatteryCheck.setBackgroundResource(R.mipmap.ic_uncheck);
                    for(int index = 0;index < mBatteryAdapter.getData().size();index++)
                        mBatteryAdapter.getItem(index).setSelect(false);
                    mBatteryAdapter.notifyDataSetChanged();
                }
                else
                {
                    mBatteryChecked = true;
                    mBatteryCheck.setBackgroundResource(R.mipmap.icon_check_all);
                    for(int index = 0;index < mBatteryAdapter.getData().size();index++)
                        mBatteryAdapter.getItem(index).setSelect(true);
                    mBatteryAdapter.notifyDataSetChanged();
                }
            }
        });

        mBatteryMenu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                finish();
            }
        });

        mBatteryBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(BaseApp.getInstance().isHaveNet(BatteryActivity.this))
                {
                    for(int index = 0;index < mBatteryAdapter.getData().size();index++)
                    {
                        Battery battery = mBatteryAdapter.getData().get(index);
                        if(battery.isSelect())
                            break;
                        if(index == mBatteryAdapter.getData().size() - 1)
                        {
                            Toast.makeText(BatteryActivity.this,"请选择需要优化的App！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }/********************************************************************************************/
                    List<String> newPackageName = new ArrayList<>(getLatestApp(BatteryActivity.this));
                    for(Battery battery : mBatteryAdapter.getData())
                    {
                        for(String packageName : getLatestApp(BatteryActivity.this))
                        {
                            if(battery.isSelect() && battery.getPackageName().equals(packageName))
                            {
                                newPackageName.remove(packageName);
                            }
                        }
                    }
                    setBatteryApps(BatteryActivity.this,0);/**************************/
                    BaseApp.getInstance().killExtraProcess();/**********************************************/
                    Intent intent = new Intent(BatteryActivity.this,BatteringActivity.class);
                    intent.putExtra("justpowersaving",false);/******************************/
                    intent.putExtra("iselectricitied",false);/******************************/
                    intent.putExtra("appNumbers",getLatestApp(BatteryActivity.this).size());
                    intent.putExtra("battery",registerReceiver(null, new IntentFilter(Intent.
                    ACTION_BATTERY_CHANGED)).getIntExtra(BatteryManager.EXTRA_LEVEL, 0) + "");
                    startActivity(intent);/************************************************/
                    overridePendingTransition(R.anim.act_into_anim,R.anim.act_out_anim);
                    if(newPackageName.size() == 0)
                    {
                        setNextHibernateTime(BatteryActivity.this);
                        setLatestApp(BatteryActivity.this,new ArrayList<>());
                        NotifyService.startUpdateNotify(getApplicationContext());
                    }
                    else
                    {
                        List<String> resultApps = new ArrayList<>();
                        for(String app : newPackageName)
                        {
                            if(!WhiteUserActivity.getWhiteUser(BatteryActivity.this).contains(app))
                            {
                                resultApps.add(app);
                            }
                        }
                        if(resultApps.size() == 0)
                            setNextHibernateTime(BatteryActivity.this);
                        setLatestApp(BatteryActivity.this,resultApps);
                        NotifyService.startUpdateNotify(getApplicationContext());
                    }
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
                mBatteryBtn.setEnabled(true);
                mBatteryBtn.setText(getString(R.string.hibernate));
                mBatteryBtn.setBackgroundResource(R.drawable.common_green_btn);

                mLottieAnimationView.cancelAnimation();
                mBatteryPercent.setVisibility(View.VISIBLE);
                mBatterypercentdes.setVisibility(View.VISIBLE);
                mLottieAnimationView.setVisibility(View.GONE);
            }
        });

        mBatteryNote.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivityForResult(new Intent(BatteryActivity.this,WhiteUserActivity.class),GOTO_WHITELIST);
            }
        });
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
        Bundle bundle = new Bundle();/*************************************/
        mFirebaseAnalytics.logEvent("batteryactivity_show",bundle);
    }

    private void updateUiPage()
    {
        Intent intent = null;
        try
        {
            intent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        final int batteryValue = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);/***********/
        mBatteryPercent.setText(batteryValue + "%");/******************************************/
        mBatterypercentdes.setText(getString(R.string.electricity) +" " + batteryValue +"% "+getString(R.string.left));/******************/
        Observable.just("BackgroudTask").map(new Function<String, List<Battery>>()
        {
            public List<Battery> apply(String str) throws Exception
            {
                PackageManager pm = getPackageManager();
                List<Battery> batteryUsers = new ArrayList<>();
                List<ApplicationInfo> applicationList = pm.getInstalledApplications(0);
                for(ApplicationInfo applicationInfo : applicationList)/****************/
                {
                    if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !applicationInfo.packageName.equals(getPackageName()) && !WhiteUserActivity.getWhiteUser(BatteryActivity.this).contains(applicationInfo.packageName))
                    {
                        try
                        {
                            Battery battery = new Battery();
                            battery.setSelect(true);/******/
                            battery.setLogo(applicationInfo.loadIcon(pm));
                            battery.setPackageName(applicationInfo.packageName);
                            battery.setName(applicationInfo.loadLabel(pm).toString());
                            batteryUsers.add(battery);/***********************************/
                        }
                        catch (Exception e){}
                    }
                }
                return batteryUsers;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Battery>>()
        {
            public void accept(List<Battery> batteryUsers) throws Exception
            {
                mBatteryChecked = true;
                mBatteryAdapter.getData().clear();
                mBatteryCheck.setBackgroundResource(R.mipmap.icon_check_all);
                List<String> packageNames = getLatestApp(BatteryActivity.this);
                if(packageNames.size() != 0)/*********************************/
                {
                    for(Battery battery : batteryUsers)
                    {
                        for(String packageName : packageNames)
                        {
                            if(battery.getPackageName().equals(packageName))
                                mBatteryAdapter.addData(battery);
                        }
                    }
                    mBatteryAdapter.notifyDataSetChanged();
                }
                else
                {
                    int maxNumbers = getBatteryApps(BatteryActivity.this);
                    if(maxNumbers == 0)
                    {
                        maxNumbers = (int)((Math.random() * WhiteListTool.getWhiteListSize(BatteryActivity.this)) % 7 + 6);
                        if(maxNumbers > WhiteListTool.getWhiteListSize(BatteryActivity.this))
                            maxNumbers = WhiteListTool.getWhiteListSize(BatteryActivity.this);
                        setBatteryApps(BatteryActivity.this,maxNumbers);
                    }
                    Collections.shuffle(batteryUsers);
                    for(Battery battery : batteryUsers)
                    {
                        if(maxNumbers-- <= 0)break;
                        mBatteryAdapter.addData(battery);
                        packageNames.add(battery.getPackageName());
                    }
                    mBatteryAdapter.notifyDataSetChanged();
                    setLatestApp(BatteryActivity.this,packageNames);
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

    public static int getBatteryApps(Context context)
    {
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getInt("batteryapps",0);
    }

    public static void setNextHibernateTime(Context context)
    {
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putLong("hibernatetime",System.currentTimeMillis() + 5 * 60 * 1000l).commit();
    }

    public static long getNextHibernateTime(Context context)
    {
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getLong("hibernatetime",System.currentTimeMillis());
    }

    public static List<String> getLatestApp(Context context)
    {
        String str = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getString("battery_latestapps","");
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
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putString("battery_latestapps",stringBuffer.toString()).commit();
    }

    public static void setBatteryApps(Context context,int appNumbers)
    {
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putInt("batteryapps",appNumbers).commit();
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