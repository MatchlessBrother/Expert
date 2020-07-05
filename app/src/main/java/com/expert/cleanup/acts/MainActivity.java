package com.expert.cleanup.acts;

import java.util.List;
import android.net.Uri;
import android.view.View;
import android.os.Bundle;
import com.expert.cleanup.R;
import java.util.ArrayList;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import androidx.core.view.GravityCompat;
import android.content.pm.PackageManager;
import com.expert.cleanup.frags.HomeFrag;
import androidx.viewpager.widget.ViewPager;
import com.expert.cleanup.sers.NotifyService;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import com.expert.cleanup.acts.base.AboutActivity;
import com.expert.cleanup.nets.NetStateReceiver;
import com.expert.cleanup.acts.base.WhiteUserActivity;
import com.expert.cleanup.acts.base.DeviceInfoActivity;
import com.expert.cleanup.nets.util.ShortcutIconUtils;

public class MainActivity extends AppCompatActivity
{
    private TextView mItemRate;
    private TextView mItemName;
    private TextView mItemAbout;
    private TextView mItemUpdate;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private TextView mItemVersion;
    private TextView mItemWhiteuser;
    private TextView mItemDeviceInfo;
    private List<Fragment> mFragments;
    private DrawerLayout mDrawerLayout;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private static final String[] mFragmentTitles = new String[]{"home"};
    private static final int[] mFragmentIcon = new int[]{R.mipmap.ic_back};

    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotifyService.start(getApplication());
        mFragments =  new ArrayList<Fragment>();
        mFragments.add(new HomeFrag());/*******/
        mItemName = findViewById(R.id.item_name);
        mItemRate = findViewById(R.id.item_rate);
        mItemAbout = findViewById(R.id.item_about);
        mItemUpdate = findViewById(R.id.item_update);
        mItemVersion = findViewById(R.id.item_version);
        mTabLayout = findViewById(R.id.main_tablayout);
        mViewPager = findViewById(R.id.main_viewpager);
        mItemWhiteuser = findViewById(R.id.item_whiteuser);
        mItemDeviceInfo = findViewById(R.id.item_deviceinfo);
        mDrawerLayout = findViewById(R.id.main_drawerlayout);
        try
        {
            mItemName.setText(getString(R.string.app_name));
            mItemVersion.setText("v " + getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()){
            public Fragment getItem(int position)
            {
                return mFragments.get(position);
            }

            public int getCount()
            {
                return mFragments.size();
            }
        };
        mViewPager.setAdapter(mFragmentPagerAdapter);/***************************************/
        mTabLayout.setupWithViewPager(mViewPager);/******************************************/
        mTabLayout.removeAllTabs();/*********************************************************/
        View homeView = LayoutInflater.from(this).inflate(R.layout.item_main,null,false);/***/
        ((TextView)homeView.findViewById(R.id.item_main_tv)).setText(mFragmentTitles[0]);
        ((ImageView)homeView.findViewById(R.id.item_main_img)).setImageDrawable(getResources().getDrawable(mFragmentIcon[0]));
        ((TextView)homeView.findViewById(R.id.item_main_tv)).setTextColor(getResources().getColorStateList(R.drawable.selector_item_main));
        mTabLayout.addTab(mTabLayout.newTab().setCustomView(homeView));/******************************************************************/
        /*********************************************************************************************************************************/
        for(int index = 0;index < mTabLayout.getTabCount();index++)
        {
            final int resultIndex = index;
            ((View)mTabLayout.getTabAt(index).getCustomView().getParent()).setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View view)
                {
                    mViewPager.setCurrentItem(resultIndex);
                    mTabLayout.getTabAt(resultIndex).select();
                }
            });
        }

        mItemAbout.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) mDrawerLayout.closeDrawers();
            }
        });

        mItemDeviceInfo.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                startActivity(new Intent(MainActivity.this, DeviceInfoActivity.class));
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) mDrawerLayout.closeDrawers();
            }
        });

        mItemWhiteuser.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this, WhiteUserActivity.class));
                if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) mDrawerLayout.closeDrawers();
            }
        });

        /*try
        {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("n",getPackageName());*//***//*
            jsonObject.put("b",PhoneHelper.getSerialNumber( ));
            jsonObject.put("a",PhoneHelper.getAndroidId(this));com.expert.cleanup.acts.MainActivity
            jsonObject.put("v",getPackageManager().getPackageInfo(getPackageName(),0).versionName);
            RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), AscEncryptHelper.encrypt(jsonObject.toString(),secretKey));
            Call<ResponseBody> call = NetClient.getInstance(getApplicationContext()).getNetAllUrl().uploadInstallationData_Myself(body);*//*******************************//*
            call.enqueue(new Callback<ResponseBody>()
            {
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                {

                }

                public void onFailure(Call<ResponseBody> call, Throwable t)
                {

                }
            });
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }*/

        mItemRate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                goWrite();
            }
        });

        mItemUpdate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                goWrite();
            }
        });
        NetStateReceiver.getInstance().init(this).getConfigInfosImmediately(this);
    }

    protected void onStart()
    {
        super.onStart();
        ShortcutIconUtils.createShortcutIcon(getApplicationContext(),R.mipmap.ic_launcher2,getString(R.string.app_name));
    }

    public void toggleDrawerLayout()
    {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawers();
        else
            mDrawerLayout.openDrawer(GravityCompat.START);
    }

    public void goWrite()
    {
        try
        {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.android.vending");/****/
            intent.setData(Uri.parse("market://details?id=" + getPackageName()));
            if(intent.resolveActivity(getPackageManager()) != null)
            {
                startActivity(intent);
            }
            else
            {
                Intent intent2 = new Intent(Intent.ACTION_VIEW);
                intent2.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                if (intent2.resolveActivity(getPackageManager()) != null)
                {
                    startActivity(intent2);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}