package com.expert.cleanup.acts.base;

import java.util.Set;
import java.util.List;
import java.util.Arrays;
import android.os.Bundle;
import android.view.View;
import java.util.HashSet;
import java.util.ArrayList;
import com.expert.cleanup.R;
import io.reactivex.Observable;
import android.content.Context;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

import com.expert.cleanup.acts.BatteryActivity;
import com.expert.cleanup.acts.BoostActivity;
import com.expert.cleanup.acts.CleanActivity;
import com.expert.cleanup.acts.CpuActivity;
import com.expert.cleanup.models.base.WhiteUser;
import com.gyf.immersionbar.ImmersionBar;
import android.content.pm.PackageManager;
import io.reactivex.schedulers.Schedulers;
import android.content.pm.ApplicationInfo;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.expert.cleanup.adaps.base.WhiteUserAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class WhiteUserActivity extends AppCompatActivity
{
    private View mStatebar;
    private ImageView mWhiteuserMenu;
    private WhiteUserAdapter mWhiteUserAdapter;
    private RecyclerView mWhiteuserRecyclerview;

    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whiteuser);
        setChangeStatus(this,false);/**************/
        mWhiteUserAdapter =  new WhiteUserAdapter();
        mStatebar = (View) findViewById(R.id.statebar);
        mWhiteuserMenu = findViewById(R.id.whiteuser_menu);
        ImmersionBar.with(this).statusBarView(mStatebar).init();
        mWhiteuserRecyclerview = findViewById(R.id.whiteuser_recyclerview);
        mWhiteuserRecyclerview.setAdapter(mWhiteUserAdapter);/************/

        mWhiteuserMenu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                finish();
            }
        });

        Observable.just("BackgroudTask").map(new Function<String,List<WhiteUser>>()
        {
            public List<WhiteUser> apply(String str) throws Exception
            {
                PackageManager pm = getPackageManager();
                List<WhiteUser> whiteUsers = new ArrayList<>();
                Set<String> whiteNames = getWhiteUser(WhiteUserActivity.this);
                List<ApplicationInfo> applicationList = pm.getInstalledApplications(0);
                for(ApplicationInfo applicationInfo : applicationList)/***************/
                {
                    if((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && !applicationInfo.packageName.equals(getPackageName()))
                    {
                        try
                        {
                            WhiteUser whiteUser = new WhiteUser();
                            whiteUser.setLogo(applicationInfo.loadIcon(pm));
                            whiteUser.setPackageName(applicationInfo.packageName);
                            whiteUser.setName(String.valueOf(applicationInfo.loadLabel(pm)));
                            whiteUsers.add(whiteUser);/*************************************/
                            for(String whiteName : whiteNames)/*****************************/
                            {
                                if(whiteName.equals(applicationInfo.packageName))
                                {
                                    whiteUser.setSelect(true);
                                    break;
                                }
                            }
                        }
                        catch (Exception e){}
                    }
                }
                return whiteUsers;
            }
        }).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<List<WhiteUser>>()
        {
            public void accept(List<WhiteUser> whiteUsers) throws Exception
            {
                mWhiteUserAdapter.addData(whiteUsers);
                mWhiteUserAdapter.notifyDataSetChanged();
            }
        });

        mWhiteUserAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener()
        {
            public void onItemChildClick(BaseQuickAdapter adapter,View view,int position)
            {
                 WhiteUser whiteUser = (WhiteUser)adapter.getItem(position);
                 whiteUser.setSelect(!whiteUser.isSelect());
                 adapter.notifyDataSetChanged();
            }
        });
    }

    protected void onPause()
    {
        super.onPause();
        int totalNum = 0;
        for(WhiteUser whiteUser : mWhiteUserAdapter.getData())
        {
            if(whiteUser.isSelect())
                totalNum++;
        }
        if(totalNum != getWhiteUser(this).size())
        {
            int usableApps = mWhiteUserAdapter.getData().size() - totalNum;
            if(CleanActivity.getCleanApps(this) > usableApps)
                CleanActivity.setCleanApps(this,usableApps);
            if(CpuActivity.getCoolerCpuApps(this) > usableApps)
                CpuActivity.setCoolerCpuApps(this,usableApps);
            if(BatteryActivity.getBatteryApps(this) > usableApps)
                BatteryActivity.setBatteryApps(this,usableApps);
            if(BoostActivity.getBoostApps(this) > usableApps)
                BoostActivity.setBoostApps(this,usableApps);
            setChangeStatus(this,true);/************************/
        }
        else
        {
            for(WhiteUser whiteUser : mWhiteUserAdapter.getData())
            {
                if(whiteUser.isSelect())
                {
                    if(!getWhiteUser(this).contains(whiteUser.getPackageName()))
                    {
                        int usableApps = mWhiteUserAdapter.getData().size() - totalNum;
                        if(CleanActivity.getCleanApps(this) > usableApps)
                            CleanActivity.setCleanApps(this,usableApps);
                        if(CpuActivity.getCoolerCpuApps(this) > usableApps)
                            CpuActivity.setCoolerCpuApps(this,usableApps);
                        if(BatteryActivity.getBatteryApps(this) > usableApps)
                            BatteryActivity.setBatteryApps(this,usableApps);
                        if(BoostActivity.getBoostApps(this) > usableApps)
                            BoostActivity.setBoostApps(this,usableApps);
                        setChangeStatus(this,true);/************************/
                        break;
                    }
                }
            }
        }
        putWhiteUser(this,mWhiteUserAdapter.getData());
    }

    public static Set<String> getWhiteUser(Context context)
    {
        String str = context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getString("whiteusers","");
        Set<String> values = new HashSet<>();/*************/
        List<String> strList = Arrays.asList(str.split(","));
        for(String tempStr : strList)
        {
            values.add(tempStr);
        }
        return values;
    }

    public static void putWhiteUser(Context context,List<WhiteUser> whiteUserList)
    {
        StringBuffer stringBuffer = new StringBuffer();
        for (WhiteUser item : whiteUserList)
        {
            if(item.isSelect())
            {
                if(stringBuffer.length() == 0)
                    stringBuffer.append(item.getPackageName());
                else
                    stringBuffer.append("," + item.getPackageName());
            }
        }
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putString("whiteusers",stringBuffer.toString()).apply();
    }

    public static void setChangeStatus(Context context,boolean isChanged)
    {
        context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).edit().putBoolean("ischanged",isChanged).commit();
    }

    public static boolean getChangeStatus(Context context)
    {
        return context.getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE).getBoolean("ischanged",false);
    }

}