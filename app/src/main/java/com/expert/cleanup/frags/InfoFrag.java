package com.expert.cleanup.frags;

import java.util.List;
import java.util.Locale;
import android.view.View;
import java.util.TimeZone;
import com.expert.cleanup.R;
import java.util.ArrayList;
import android.content.Intent;
import android.util.TypedValue;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.Typeface;
import android.os.BatteryManager;
import android.provider.Settings;
import android.view.WindowManager;
import java.text.SimpleDateFormat;
import android.widget.LinearLayout;
import android.util.DisplayMetrics;
import android.content.IntentFilter;

import com.expert.cleanup.frags.base.BaseFragment;
import com.expert.cleanup.nativetos.base.UnitTool;
import android.animation.ObjectAnimator;
import com.expert.cleanup.nativetos.MemoryTool;
import com.gyf.immersionbar.ImmersionBar;
import android.content.BroadcastReceiver;
import com.expert.cleanup.nativetos.HardwareTool;
import com.expert.cleanup.acts.CleanActivity;
import com.expert.cleanup.acts.BoostActivity;
import com.expert.cleanup.acts.BoostingActivity;
import com.expert.cleanup.acts.CleaningActivity;
import android.view.animation.OvershootInterpolator;
import com.expert.cleanup.view.NewCircleProgressView;
import android.view.animation.AccelerateDecelerateInterpolator;
import static com.expert.cleanup.acts.BoostActivity.getNextBoostTime;
import static com.expert.cleanup.acts.CleanActivity.getNextCleanTime;

public class InfoFrag extends BaseFragment
{
    private LinearLayout mProductContainerView;
    private LinearLayout mBatteryContainerView;
    private LinearLayout mScreenContainerView;
    private NewCircleProgressView mRamCircle;
    private NewCircleProgressView mRomCircle;
    private LinearLayout mCpuContainerView;
    private LinearLayout mOSContainerView;
    private LinearLayout mBasicInfo;
    private LinearLayout mStatus;

    private ImageView mInfoMenu;
    private TextView mBoostBtn;
    private TextView mCleanBtn;
    private View stateBar;

    protected void initWidgets(View rootView)
    {
        mProductContainerView = rootView.findViewById(R.id.product_container);
        mBatteryContainerView = rootView.findViewById(R.id.battery_container);
        mScreenContainerView = rootView.findViewById(R.id.screen_container);
        mCpuContainerView = rootView.findViewById(R.id.cpu_container);
        mOSContainerView = rootView.findViewById(R.id.os_container);
        mRamCircle = rootView.findViewById(R.id.info_circle_ram);
        mRomCircle = rootView.findViewById(R.id.info_circle_rom);
        mBasicInfo = rootView.findViewById(R.id.info_basic);
        mStatus = rootView.findViewById(R.id.info_status);

        mBoostBtn = rootView.findViewById(R.id.info_circle_ram_btn);
        mCleanBtn = rootView.findViewById(R.id.info_circle_rom_btn);
        mInfoMenu = rootView.findViewById(R.id.frag_info_menu);
        stateBar = rootView.findViewById(R.id.statebar);
    }

    protected int setLayoutResID()
    {
        return R.layout.frag_info;

    }

    protected void initDatas()
    {

    }

    protected void initLogic()
    {
        initProductContent();
        initScreenContent();
        initCpuContent();
        initOSContent();
        initBasicInfo();
        initStatus();

        mInfoMenu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                getActivity().finish();
            }
        });

        mBoostBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(getNextBoostTime(getActivity()) > System.currentTimeMillis())
                {
                    Intent intent = new Intent(getActivity(), BoostingActivity.class);
                    intent.putExtra("justboost",false);
                    intent.putExtra("appNumbers",0);
                    intent.putExtra("isboost",true);
                    startActivity(intent);
                }
                else
                {
                    startActivity(new Intent(getActivity().getApplicationContext(), BoostActivity.class));
                }
            }
        });

        mCleanBtn.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(getNextCleanTime(getActivity()) > System.currentTimeMillis())
                {
                    Intent intent = new Intent(getActivity(), CleaningActivity.class);
                    intent.putExtra("justcleanedup",false);
                    intent.putExtra("iscleaned",true);
                    intent.putExtra("garbageSize","");
                    intent.putExtra("appNumbers",0);
                    startActivity(intent);
                }

                else
                {
                    startActivity(new Intent(getActivity(), CleanActivity.class));
                }
            }
        });
    }

    private void initContent(LinearLayout containerView, List<String> nameAndValues)
    {
        containerView.removeAllViews();
        if(nameAndValues.size() % 2 == 0)
        {
            Context context = containerView.getContext();
            LinearLayout.LayoutParams params;
            LinearLayout lineContainer;
            TextView valueView;
            TextView nameView;
            for (int i = 0; i < nameAndValues.size(); i = i + 2)
            {
                lineContainer = new LinearLayout(context);
                params = new LinearLayout.LayoutParams(0,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,40,getResources().getDisplayMetrics()));
                params.weight = 1;
                nameView = new TextView(context);
                nameView.setText(nameAndValues.get(i));
                nameView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                nameView.setTextSize(17);
                lineContainer.addView(nameView, params);

                params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,30,getResources().getDisplayMetrics()));
                valueView = new TextView(context);
                valueView.setText(nameAndValues.get(i + 1));
                valueView.setTextSize(13);
                lineContainer.addView(valueView, params);
                containerView.addView(lineContainer);
            }
        }
    }

    public void onResume()
    {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        try
        {
            getContext().getApplicationContext().registerReceiver(mBroadcastReceiver, filter);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onPause()
    {
        super.onPause();
        try
        {
            getContext().getApplicationContext().unregisterReceiver(mBroadcastReceiver);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initBasicInfo()
    {
        DisplayMetrics metrics = new DisplayMetrics();
        long[] ramSizes = MemoryTool.getRAMTotalAvailable(getContext());
        long[] romSizes = MemoryTool.getROMTotalAvailable(getContext());
        String[] ramTotalTexts = UnitTool.fileSize(ramSizes[0]);/******/
        String mRamDetail = String.format(Locale.getDefault(), "%s%s",ramTotalTexts[0], ramTotalTexts[1]);
        String[] romTotalTexts = UnitTool.fileSize(romSizes[0]);/******/
        String mRomDetail = String.format(Locale.getDefault(), "%s%s",romTotalTexts[0], romTotalTexts[1]);
        ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);
        List<String> nameAndValues = new ArrayList<>();
        nameAndValues.add(getString(R.string.androidversion));
        nameAndValues.add(android.os.Build.VERSION.RELEASE);
        nameAndValues.add(getString(R.string.brand));
        nameAndValues.add(android.os.Build.BRAND);
        nameAndValues.add(getString(R.string.model));
        nameAndValues.add(android.os.Build.MODEL);
        nameAndValues.add(getString(R.string.cpu));
        nameAndValues.add(HardwareTool.getCpuName());
        nameAndValues.add(getString(R.string.ram));
        nameAndValues.add(mRamDetail);
        nameAndValues.add(getString(R.string.storagesm));
        nameAndValues.add(mRomDetail);
        nameAndValues.add(getString(R.string.resolution));
        nameAndValues.add(metrics.widthPixels + "×" + metrics.heightPixels);
        initContent(mBasicInfo,nameAndValues);
    }

    private void initStatus()
    {
        long[] ramSizes = MemoryTool.getRAMTotalAvailable(getContext());
        long[] romSizes = MemoryTool.getROMTotalAvailable(getContext());
        float mRamValue = (ramSizes[0] - ramSizes[1]) / (float)ramSizes[0] * 100;
        float mRomValue = (romSizes[0] - romSizes[1]) / (float)romSizes[0] * 100;
        ObjectAnimator progressAnimator1 = ObjectAnimator.ofFloat(mRamCircle,"progress",0,mRamValue);
        progressAnimator1.setInterpolator(new OvershootInterpolator());
        progressAnimator1.setDuration(2000);
        progressAnimator1.start();
        ObjectAnimator progressAnimator2 = ObjectAnimator.ofFloat(mRomCircle,"progress",0,mRomValue);
        progressAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());
        progressAnimator2.setDuration(2000);
        progressAnimator2.start();
    }

    private void initOSContent()
    {
        List<String> nameAndValues = new ArrayList<>();
        nameAndValues.add(getString(R.string.text_system_release));
        nameAndValues.add(android.os.Build.VERSION.RELEASE);
        nameAndValues.add(getString(R.string.text_system_language));
        nameAndValues.add(Locale.getDefault().getLanguage());
        nameAndValues.add(getString(R.string.text_system_encoding));
        nameAndValues.add(System.getProperty("file.encoding"));
        nameAndValues.add(getString(R.string.text_system_time_zone));
        nameAndValues.add(TimeZone.getDefault().getDisplayName());
        initContent(mOSContainerView, nameAndValues);
    }

    private void initCpuContent()
    {
        List<String> nameAndValues = new ArrayList<>();
        nameAndValues.add(getString(R.string.text_cpu_model));
        nameAndValues.add(HardwareTool.getCpuName());
        nameAndValues.add(getString(R.string.text_cpu_core_number));
        nameAndValues.add(HardwareTool.getNumCores() + "");
        nameAndValues.add(getString(R.string.text_maximum_frequency));
        nameAndValues.add(HardwareTool.getMaxCpuFreq());
        nameAndValues.add(getString(R.string.text_minimum_frequency));
        nameAndValues.add(HardwareTool.getMinCpuFreq());
        nameAndValues.add(getString(R.string.text_current_frequency));
        nameAndValues.add(HardwareTool.getCurCpuFreq());
        nameAndValues.add(getString(R.string.text_cpu_architecture));
        nameAndValues.add(HardwareTool.getCpuAbi());
        initContent(mCpuContainerView, nameAndValues);
    }

    private void initScreenContent()
    {
        int brightness = 0;
        try
        {
            brightness = Settings.System.getInt(getContext().getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
        }
        catch (Settings.SettingNotFoundException e)
        {
            e.printStackTrace();
        }
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int brightnessLevel = brightness * 100 / 255;
        int refreshRate = (int) windowManager.getDefaultDisplay().getRefreshRate();
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);

        List<String> nameAndValues = new ArrayList<>();
        nameAndValues.add(getString(R.string.text_screen_brightness));
        nameAndValues.add(brightnessLevel + "%");
        nameAndValues.add(getString(R.string.text_resolution));
        nameAndValues.add(metrics.widthPixels + "×" + metrics.heightPixels);
        nameAndValues.add(getString(R.string.text_density));
        nameAndValues.add(metrics.densityDpi + "");
        nameAndValues.add(getString(R.string.text_font_scaling));
        nameAndValues.add(metrics.scaledDensity + "");
        nameAndValues.add(getString(R.string.text_refresh_rate));
        nameAndValues.add(refreshRate + "Hz");
        initContent(mScreenContainerView, nameAndValues);
    }

    private void initProductContent()
    {
        List<String> nameAndValues = new ArrayList<>();
        nameAndValues.add(getString(R.string.text_brand));
        nameAndValues.add(android.os.Build.BRAND);
        nameAndValues.add(getString(R.string.text_model));
        nameAndValues.add(android.os.Build.MODEL);
        nameAndValues.add(getString(R.string.text_host));
        nameAndValues.add(android.os.Build.HOST);
        nameAndValues.add(getString(R.string.text_serial));
        nameAndValues.add(android.os.Build.SERIAL);
        nameAndValues.add(getString(R.string.text_id));
        nameAndValues.add(android.os.Build.ID);
        nameAndValues.add(getString(R.string.text_time));
        nameAndValues.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(android.os.Build.TIME));
        initContent(mProductContainerView, nameAndValues);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (Intent.ACTION_BATTERY_CHANGED.equals(action))
            {
                List<String> nameAndValues = new ArrayList<>();
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
                int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
                int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);

                String technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
                nameAndValues.add(getString(R.string.text_battery_level));
                nameAndValues.add(level + "%");
                nameAndValues.add(getString(R.string.text_battery_temperature));
                nameAndValues.add(temperature / 10 + "℃" + "/" + (int) (temperature / 10 * 1.8 + 32) + "℉");
                nameAndValues.add(getString(R.string.text_battery_voltage));
                nameAndValues.add(voltage + "mV");
                nameAndValues.add(getString(R.string.text_battery_technology));
                nameAndValues.add(technology);
                nameAndValues.add(getString(R.string.text_battery_status));
                nameAndValues.add(getBatteryStatus(status));
                initContent(mBatteryContainerView, nameAndValues);
            }
        }

        private String getBatteryStatus(int status)
        {
            switch (status)
            {
                case BatteryManager.BATTERY_STATUS_FULL:
                    return getString(R.string.text_battery_full);
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    return getString(R.string.text_battery_charging);
                default:
                    return getString(R.string.text_battery_discharging);
            }
        }
    };

    public void initImmersionBar()
    {
        ImmersionBar.with(this).statusBarView(stateBar).init();
    }
}
