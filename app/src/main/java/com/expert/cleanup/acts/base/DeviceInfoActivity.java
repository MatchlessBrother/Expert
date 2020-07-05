package com.expert.cleanup.acts.base;

import java.util.List;
import android.os.Bundle;
import android.view.View;

import com.expert.cleanup.BaseApp;
import com.expert.cleanup.R;
import java.util.ArrayList;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.LayoutInflater;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import com.expert.cleanup.frags.InfoFrag;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class DeviceInfoActivity extends AppCompatActivity
{
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private List<Fragment> mFragments;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private static final String[] mFragmentTitles = new String[]{"info"};
    private static final int[] mFragmentIcon = new int[]{R.mipmap.ic_back};

    @SuppressLint("ResourceType")
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviceinfo);
        mFragments =  new ArrayList<Fragment>();/******/
        mFragments.add(new InfoFrag());/***************/
        ((BaseApp)getApplication()).startGgInterstitial();
        mTabLayout = findViewById(R.id.deviceinfo_tablayout);
        mViewPager = findViewById(R.id.deviceinfo_viewpager);
        mFragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager())
        {
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
    }
}