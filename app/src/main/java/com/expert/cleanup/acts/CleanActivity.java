package com.expert.cleanup.acts;

import java.util.List;
import android.os.Bundle;
import android.view.View;
import com.expert.cleanup.R;
import java.util.ArrayList;
import com.google.gson.Gson;
import android.widget.Toast;
import java.util.Collections;
import android.content.Intent;
import android.content.Context;
import io.reactivex.Observable;
import android.widget.TextView;
import android.widget.ImageView;
import com.expert.cleanup.BaseApp;
import android.widget.LinearLayout;
import androidx.annotation.Nullable;
import com.expert.cleanup.models.Clean;
import java.util.concurrent.TimeUnit;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import com.gyf.immersionbar.ImmersionBar;
import android.content.pm.PackageManager;
import com.google.gson.reflect.TypeToken;
import io.reactivex.schedulers.Schedulers;
import android.content.pm.ApplicationInfo;
import io.reactivex.disposables.Disposable;
import com.expert.cleanup.models.base.CleanApp;
import com.airbnb.lottie.LottieAnimationView;
import com.expert.cleanup.adaps.CleanAdapter;
import com.expert.cleanup.sers.NotifyService;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.expert.cleanup.nativetos.base.UnityTool;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.expert.cleanup.nativetos.base.WhiteListTool;
import com.google.firebase.analytics.FirebaseAnalytics;
import androidx.recyclerview.widget.LinearLayoutManager;
import io.reactivex.android.schedulers.AndroidSchedulers;
import com.expert.cleanup.acts.base.WhiteUserActivity;

public class CleanActivity extends AppCompatActivity
{
    private View mStatebar;
    private TextView mCleanBtn;
    private TextView mCleanNote;
    private ImageView mCleanMenu;
    private TextView mCleanpercent;
    private long mTotalOfGarbageSize;
    private CleanAdapter mCleanAdapter;
    private TextView mCleanpercentunity;
    private RecyclerView mCleanRecyclerview;
    private LinearLayout mCleanpercentunityAll;
    private LottieAnimationView mLottieAnimationView;

    private boolean isLoadedAds;
    private int mTriedNumsForShow;
    private Disposable mDisposable;
    public static final int GOTO_WHITELIST = 0x0001;

    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clean);
        mStatebar = findViewById(R.id.statebar);
        mTotalOfGarbageSize = 0;/**************/
        mCleanAdapter = new CleanAdapter();/***/
        mCleanBtn = findViewById(R.id.clean_btn);
        mCleanMenu = findViewById(R.id.clean_menu);
        mCleanNote = findViewById(R.id.clean_note);
        mCleanpercent = findViewById(R.id.cleanpercent);
        ImmersionBar.with(this).statusBarView(mStatebar).init();
        mCleanpercentunity = findViewById(R.id.cleanpercentunity);
        mCleanRecyclerview = findViewById(R.id.clean_recyclerview);
        mLottieAnimationView = findViewById(R.id.clean_lottieview);
        mCleanpercentunityAll = findViewById(R.id.cleanpercentunityall);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);/***/
        mCleanRecyclerview.setLayoutManager(linearLayoutManager);/*************/
        mCleanpercent.setText("0");mCleanpercentunity.setText("KB");
        mCleanRecyclerview.setAdapter(mCleanAdapter);/*************/
        updateUiPage();/*******************************************/
        mCleanAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            public void onItemChildClick(BaseQuickAdapter adapter,View view,int position)
            {
                Clean clean = (Clean)adapter.getItem(position);
                List<Clean> cleans = adapter.getData();
                clean.setSelect(!clean.isSelect());
                adapter.notifyDataSetChanged();
                mTotalOfGarbageSize = 0;
                for(Clean cln : cleans)
                if(cln.isSelect())
                mTotalOfGarbageSize+=
                cln.getGarbageSize();
                updateGarbageSize();
            }
        });

        mCleanMenu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                finish();
            }
        });

        mCleanBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(BaseApp.getInstance().isHaveNet(CleanActivity.this))
                {
                    for(int index = 0;index < mCleanAdapter.getData().size();index++)
                    {
                        Clean clean = mCleanAdapter.getData().get(index);
                        if(clean.isSelect())
                            break;
                        if(index == mCleanAdapter.getData().size() - 1)
                        {
                            Toast.makeText(CleanActivity.this,"请选择需要清理的App！",Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }/*******************************************************************************************/
                    List<CleanApp> newCleanApps = new ArrayList<>();
                    for(CleanApp cleanApp : getLatestApp(CleanActivity.this))
                    {
                        for(int index = 0;index < mCleanAdapter.getData().size();index++)
                        {
                            Clean clean = mCleanAdapter.getData().get(index);
                            if(clean.isSelect() && clean.getPackageName().equals(cleanApp.getName()))
                            {
                                break;
                            }
                            if(index == mCleanAdapter.getData().size() - 1)
                            {
                                newCleanApps.add(cleanApp);
                            }
                        }
                    }
                    setCleanApps(CleanActivity.this,0);/*********************************/
                    BaseApp.getInstance().killExtraProcess();/*************************************************/
                    Intent intent = new Intent(CleanActivity.this,CleaningActivity.class);
                    intent.putExtra("iscleaned",false);/*********************************/
                    intent.putExtra("justcleanedup",false);/*****************************/
                    intent.putExtra("appNumbers",getLatestApp(CleanActivity.this).size());
                    intent.putExtra("garbageSize",mCleanpercent.getText().toString() +
                    mCleanpercentunity.getText().toString());/***********************************/
                    intent.putExtra("garbageSizeOfByte",mTotalOfGarbageSize);/***********/
                    startActivity(intent);/**********************************************/
                    overridePendingTransition(R.anim.act_into_anim,R.anim.act_out_anim);
                    if(newCleanApps.size() == 0)
                    {
                        setNextCleanTime(CleanActivity.this);
                        setLatestApp(CleanActivity.this,new ArrayList<>());
                        NotifyService.startUpdateNotify(getApplicationContext());
                    }
                    else
                    {
                        List<CleanApp> resultApps = new ArrayList<>();
                        for(CleanApp app : newCleanApps)
                        {
                            if(!WhiteUserActivity.getWhiteUser(CleanActivity.this).contains(app.getName()))
                            {
                                resultApps.add(app);
                            }
                        }
                        if(resultApps.size() == 0)
                            setNextCleanTime(CleanActivity.this);
                        setLatestApp(CleanActivity.this,resultApps);
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
                mCleanBtn.setEnabled(true);
                mCleanBtn.setText(getString(R.string.startcleaning));
                mCleanBtn.setBackgroundResource(R.drawable.common_green_btn);

                mLottieAnimationView.cancelAnimation();
                mLottieAnimationView.setVisibility(View.GONE);

                mCleanpercent.setVisibility(View.VISIBLE);
                mCleanpercentunityAll.setVisibility(View.VISIBLE);
            }
        });

        mCleanNote.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivityForResult(new Intent(CleanActivity.this,WhiteUserActivity.class),GOTO_WHITELIST);
            }
        });

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this.getApplicationContext());
        Bundle bundle = new Bundle();/*************************************/
        mFirebaseAnalytics.logEvent("cleanactivity_show",bundle);
    }

    private void updateUiPage()
    {
        mTotalOfGarbageSize = 0;
        Observable.just("BackgroudTask").map(new Function<String, List<Clean>>()
        {
            public List<Clean> apply(String str) throws Exception
            {
                List<Clean> cleans = new ArrayList<>();
                PackageManager pm = getPackageManager();
                List<ApplicationInfo> applicationList = pm.getInstalledApplications(0);
                for(ApplicationInfo applicationInfo : applicationList)/****************/
                {
                    if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !applicationInfo.packageName.equals(getPackageName()) && !WhiteUserActivity.getWhiteUser(CleanActivity.this).contains(applicationInfo.packageName))
                    {
                        try
                        {
                            Clean clean = new Clean();
                            clean.setSelect(true);/******/
                            clean.setLogo(applicationInfo.loadIcon(pm));
                            clean.setGarbageSize(getRandomGarbageSize());
                            clean.setPackageName(applicationInfo.packageName);
                            clean.setName(String.valueOf(applicationInfo.loadLabel(pm)));
                            cleans.add(clean);/*****************************************/
                        }
                        catch (Exception e){}
                    }
                }
                return cleans;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<Clean>>()
        {
            public void accept(List<Clean> cleans) throws Exception
            {
                mCleanAdapter.getData().clear();
                List<CleanApp> cleanApps  =  getLatestApp(CleanActivity.this);
                if(cleanApps.size() != 0)/*********************************/
                {
                    for(Clean clean : cleans)
                    {
                        for(CleanApp cleanApp : cleanApps)
                        {
                            if(clean.getPackageName().equals(cleanApp.getName()))
                            {
                                clean.setGarbageSize(cleanApp.getGarbageSize());
                                mTotalOfGarbageSize += clean.getGarbageSize();
                                mCleanAdapter.addData(clean);
                            }
                        }
                    }
                    mCleanAdapter.notifyDataSetChanged();
                }
                else
                {
                    int maxNumbers = getCleanApps(CleanActivity.this);
                    if(maxNumbers == 0)
                    {
                        maxNumbers = (int)((Math.random() * WhiteListTool.getWhiteListSize(CleanActivity.this)) % 7 + 6);
                        if(maxNumbers > WhiteListTool.getWhiteListSize(CleanActivity.this))
                            maxNumbers = WhiteListTool.getWhiteListSize(CleanActivity.this);
                        setCleanApps(CleanActivity.this,maxNumbers);
                    }
                    Collections.shuffle(cleans);
                    for(Clean clean : cleans)
                    {
                        if(maxNumbers-- <= 0)break;
                        mCleanAdapter.addData(clean);
                        CleanApp cleanApp = new CleanApp();
                        cleanApp.setName(clean.getPackageName());
                        cleanApp.setGarbageSize(clean.getGarbageSize());
                        cleanApps.add(cleanApp);/**********************/
                        mTotalOfGarbageSize += clean.getGarbageSize();
                    }
                    mCleanAdapter.notifyDataSetChanged();
                    setLatestApp(CleanActivity.this,cleanApps);
                }
                updateGarbageSize();
            }
        });
    }

    protected void onDestroy()
    {
        super.onDestroy();
        if(null != mDisposable && !mDisposable.isDisposed())
            mDisposable.dispose();
    }

    private void updateGarbageSize()
    {
        if(UnityTool.sizeFormat(mTotalOfGarbageSize).contains(UnityTool.UNIT[1]))
        {
            String str = UnityTool.sizeFormat(mTotalOfGarbageSize);
            mCleanpercent.setText(str.split("KB")[0]);
            mCleanpercentunity.setText("KB");
        }
        else if(UnityTool.sizeFormat(mTotalOfGarbageSize).contains(UnityTool.UNIT[2]))
        {
            String str = UnityTool.sizeFormat(mTotalOfGarbageSize);
            mCleanpercent.setText(str.split("MB")[0]);
            mCleanpercentunity.setText("MB");
        }
        else if(UnityTool.sizeFormat(mTotalOfGarbageSize).contains(UnityTool.UNIT[3]))
        {
            String str = UnityTool.sizeFormat(mTotalOfGarbageSize);
            mCleanpercent.setText(str.split("GB")[0]);
            mCleanpercentunity.setText("GB");
        }
        else if(UnityTool.sizeFormat(mTotalOfGarbageSize).contains(UnityTool.UNIT[4]))
        {
            String str = UnityTool.sizeFormat(mTotalOfGarbageSize);
            mCleanpercent.setText(str.split("TB")[0]);
            mCleanpercentunity.setText("TB");
        }
        else
        {
            String str = UnityTool.sizeFormat(mTotalOfGarbageSize);
            mCleanpercent.setText(str.split("B")[0]);
            mCleanpercentunity.setText("B");
        }
    }

    /**********默认Byte格式*********/
    public int getRandomGarbageSize()
    {
        return (int)(Math.random() * 94371840 + 10485760);

    }

    public static int getCleanApps(Context context)
    {
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getInt("cleanapps",0);
    }

    public static void setNextCleanTime(Context context)
    {
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putLong("cleantime",System.currentTimeMillis() + 5 * 60 * 1000l).commit();
    }

    public static long getNextCleanTime(Context context)
    {
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getLong("cleantime",System.currentTimeMillis());
    }

    public static List<CleanApp> getLatestApp(Context context)
    {
        String str = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getString("clean_latestapps","{}");
        if( "{}".equals(str))
            return new ArrayList<>();
        else
        {
            List<CleanApp> lists = new Gson().fromJson(str,new TypeToken<ArrayList<CleanApp>>(){}.getType());
            List<CleanApp> results = new ArrayList<>();
            for(CleanApp app : lists)
            {
                if(!WhiteUserActivity.getWhiteUser(context).contains(app.getName()))
                    results.add(app);
            }
            return results;
        }
    }

    public static void setCleanApps(Context context,int appNumbers)
    {
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putInt("cleanapps",appNumbers).commit();
    }

    public static void setLatestApp(Context context,List<CleanApp> apps)
    {
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putString("clean_latestapps",new Gson().toJson(apps)).commit();
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