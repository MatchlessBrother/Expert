package com.expert.cleanup.frags;

import java.util.Timer;
import java.util.Locale;
import android.view.View;
import com.expert.cleanup.R;
import java.util.TimerTask;
import android.content.Intent;
import java.text.DecimalFormat;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.os.BatteryManager;
import android.content.IntentFilter;
import android.widget.RelativeLayout;
import com.expert.cleanup.baer.BannerHelper;
import android.view.animation.ScaleAnimation;
import com.expert.cleanup.frags.base.BaseFragment;
import com.expert.cleanup.nativetos.base.UnitTool;
import android.graphics.BitmapFactory;
import android.animation.ObjectAnimator;
import android.view.animation.Animation;
import com.gyf.immersionbar.ImmersionBar;
import com.expert.cleanup.acts.CpuActivity;
import com.expert.cleanup.nativetos.MemoryTool;
import com.expert.cleanup.acts.base.NoteActivity;
import com.expert.cleanup.acts.MainActivity;
import com.expert.cleanup.acts.CleanActivity;
import com.expert.cleanup.acts.BoostActivity;
import com.expert.cleanup.nativetos.HardwareTool;
import android.view.animation.AnimationSet;
import com.expert.cleanup.nativetos.base.WhiteListTool;
import com.expert.cleanup.acts.CpuingActivity;
import com.expert.cleanup.acts.BatteryActivity;
import android.view.animation.AlphaAnimation;
import com.expert.cleanup.acts.CleaningActivity;
import com.expert.cleanup.acts.BoostingActivity;
import com.expert.cleanup.acts.BatteringActivity;
import com.expert.cleanup.sers.NotifyService;
import android.view.animation.RotateAnimation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.animation.OvershootInterpolator;
import com.expert.cleanup.view.NewCircleProgressView;
import android.view.animation.AccelerateInterpolator;
import androidx.constraintlayout.widget.ConstraintLayout;
import static com.expert.cleanup.acts.BoostActivity.getNextBoostTime;
import static com.expert.cleanup.acts.CleanActivity.getNextCleanTime;
import static com.expert.cleanup.acts.CpuActivity.getNextCoolDownTime;
import static com.expert.cleanup.acts.BatteryActivity.getNextHibernateTime;

public class HomeFrag extends BaseFragment
{
    private Timer mTimer;
    private View mStateBar;
    private String mRamValue;
    private String mRomValue;
    private String mCpuValueQ;
    private String mCpuValueH;
    private String mRamDetail;
    private String mRomDetail;
    private ImageView mHomeMenu;
    private ImageView mHomeHelp;
    private ImageView mHomePlan;
    private String mBatteryValue;
    private ImageView mFragHomeNote;
    private LinearLayout mHomeHelpAll;
    private RelativeLayout mHomeItemCpu;
    private RelativeLayout mHomeItemClean;
    private RelativeLayout mHomeItemBoost;
    private RelativeLayout mHomeItemBattery;
    private TextView mFragHomeItemCpuNum;
    private TextView mFragHomeItemCleanNum;
    private TextView mFragHomeItemBoostNum;
    private TextView mFragHomeItemBatteryNum;
    private TextView mHomeTopItemRomValue;
    private TextView mHomeTopItemRomTitle;
    private TextView mHomeTopItemRomDetail;
    private TextView mHomeTopItemRamValue;
    private TextView mHomeTopItemRamTitle;
    private TextView mHomeTopItemRamDetail;
    private TextView mHomeTopItemCpuValue;
    private TextView mHomeTopItemCpuTitle;
    private TextView mHomeTopItemCpuDetail;

    private ConstraintLayout mHomeTopItemRom;
    private ConstraintLayout mHomeTopItemRam;
    private ConstraintLayout mHomeTopItemCpu;

    private TextView mCircleValue;
    private TextView mCircleDetail;
    private ImageView mCpuItemImg;
    private TextView mCpuItemName;
    private TextView mCpuItemValue;
    /*****************************/
    private ImageView mBoostItemImg;
    private TextView mBoostItemName;
    private TextView mBoostItemValue;
    /********************************/
    private ImageView mBatteryItemImg;
    private TextView mBatteryItemName;
    private TextView mBatteryItemValue;
    private ScaleAnimation mRedPointSa;
    private NewCircleProgressView mNewCircleProgressView;

    protected void initWidgets(View rootView)
    {
        mStateBar = rootView.findViewById(R.id.statebar);
        mHomePlan = rootView.findViewById(R.id.home_plan);
        mHomeHelp = rootView.findViewById(R.id.frag_home_help);
        mHomeMenu = rootView.findViewById(R.id.frag_home_menu);
        mFragHomeNote = rootView.findViewById(R.id.frag_home_note);
        mHomeHelpAll = rootView.findViewById(R.id.frag_home_help_all);
        mHomeItemCpu = rootView.findViewById(R.id.frag_home_item_cpu);
        mHomeTopItemRam =  rootView.findViewById(R.id.home_top_item_ram);
        mHomeTopItemCpu =  rootView.findViewById(R.id.home_top_item_cpu);
        mHomeTopItemRom =  rootView.findViewById(R.id.home_top_item_rom);
        mHomeItemBoost = rootView.findViewById(R.id.frag_home_item_boost);
        mHomeItemClean = rootView.findViewById(R.id.frag_home_item_clean);
        mHomeItemBattery = rootView.findViewById(R.id.frag_home_item_battery);
        mFragHomeItemCpuNum = rootView.findViewById(R.id.frag_home_item_cpu_num);
        mHomeTopItemRomTitle = rootView.findViewById(R.id.home_top_item_rom_title);
        mHomeTopItemRomValue = rootView.findViewById(R.id.home_top_item_rom_value);
        mHomeTopItemRamTitle = rootView.findViewById(R.id.home_top_item_ram_title);
        mHomeTopItemRamValue = rootView.findViewById(R.id.home_top_item_ram_value);
        mHomeTopItemCpuTitle = rootView.findViewById(R.id.home_top_item_cpu_title);
        mHomeTopItemCpuValue = rootView.findViewById(R.id.home_top_item_cpu_value);
        mHomeTopItemRomDetail = rootView.findViewById(R.id.home_top_item_rom_detail);
        mHomeTopItemCpuDetail = rootView.findViewById(R.id.home_top_item_cpu_detail);
        mHomeTopItemRamDetail = rootView.findViewById(R.id.home_top_item_ram_detail);
        mFragHomeItemCleanNum = rootView.findViewById(R.id.frag_home_item_clean_num);
        mFragHomeItemBoostNum = rootView.findViewById(R.id.frag_home_item_boost_num);
        mFragHomeItemBatteryNum = rootView.findViewById(R.id.frag_home_item_battery_num);
        /*******************************************************************************/
        mCircleValue = rootView.findViewById(R.id.frag_home_circle_value);
        mCircleDetail = rootView.findViewById(R.id.frag_home_circle_detail);
        mNewCircleProgressView = rootView.findViewById(R.id.frag_home_circle);
        mCpuItemImg = rootView.findViewById(R.id.cpu_icon);
        mCpuItemName = rootView.findViewById(R.id.frag_home_item_cpu_name);
        mCpuItemValue = rootView.findViewById(R.id.frag_home_item_cpu_value);
        mBoostItemImg = rootView.findViewById(R.id.boost_icon);
        mBoostItemName = rootView.findViewById(R.id.frag_home_item_boost_name);
        mBoostItemValue = rootView.findViewById(R.id.frag_home_item_boost_value);
        mBatteryItemImg = rootView.findViewById(R.id.battery_icon);
        mBatteryItemName = rootView.findViewById(R.id.frag_home_item_battery_name);
        mBatteryItemValue = rootView.findViewById(R.id.frag_home_item_battery_value);
        BannerHelper.getInstance().initBanner(rootView,R.id.bannerview);
        BannerHelper.getInstance().showBanner();
        mRedPointSa = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        mRedPointSa.setInterpolator(new LinearInterpolator());
        mRedPointSa.setRepeatCount(Animation.INFINITE);
        mRedPointSa.setRepeatMode(Animation.REVERSE);
        mRedPointSa.setDuration(500l);
    }

    protected int setLayoutResID()
    {
        return R.layout.frag_home;

    }

    protected void initDatas()
    {

    }

    public void onDestroyView()
    {
        BannerHelper.getInstance().destroyBanner();
        super.onDestroyView();
    }

    protected void initDataUi()
    {
        Intent intent = null;
        try
        {
            if(null != getContext() && null != getContext().getApplicationContext())
                intent = getContext().getApplicationContext().registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        long[] ramSizes = MemoryTool.getRAMTotalAvailable(getContext());
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        long[] romSizes = MemoryTool.getROMTotalAvailable(getContext());
        mBatteryValue = level + "%";

        mRamValue = (int)((ramSizes[0] - ramSizes[1]) / (float) ramSizes[0] * 100) + "";
        mRomValue = (int)((romSizes[0] - romSizes[1]) / (float) romSizes[0] * 100) + "";
        mCpuValueQ = new DecimalFormat("#.0").format(HardwareTool.getCpuTemperatureFinder(getContext()));
        mCpuValueH = "℃" + "/" +  (new DecimalFormat("#.0").format(32+ HardwareTool.getCpuTemperatureFinder(getContext()) * 1.8))+ "℉";

        String[] ramTotalTexts = UnitTool.fileSize(ramSizes[0]);
        String[] ramUsedTexts = UnitTool.fileSize(ramSizes[0] - ramSizes[1]);
        mRamDetail = String.format(Locale.getDefault(), "%s%s/%s%s", ramUsedTexts[0], ramUsedTexts[1], ramTotalTexts[0], ramTotalTexts[1]);

        String[] romTotalTexts = UnitTool.fileSize(romSizes[0]);
        String[] romUsedTexts = UnitTool.fileSize(romSizes[0] - romSizes[1]);
        mRomDetail = String.format(Locale.getDefault(), "%s%s/%s%s", romUsedTexts[0], romUsedTexts[1], romTotalTexts[0], romTotalTexts[1]);

        mHomeTopItemRamTitle.setText("%RAM");
        mHomeTopItemRamValue.setText(mRamValue);
        mHomeTopItemRamDetail.setText(mRamDetail);

        mHomeTopItemRomTitle.setText("%STORAGE");
        mHomeTopItemRomValue.setText(mRomValue);
        mHomeTopItemRomDetail.setText(mRomDetail);

        mHomeTopItemCpuValue.setText(mCpuValueQ);
        mHomeTopItemCpuTitle.setText(mCpuValueH);
        mHomeTopItemCpuDetail.setText("CPU TEMP");

        mCpuItemValue.setText(mCpuValueQ+"℃");
        mBoostItemValue.setText(mRamValue+"%");
        mBatteryItemValue.setText(mBatteryValue);
    }

    protected void initLogic()
    {
        RotateAnimation rotateAnimationRight = new RotateAnimation(0,45,
        Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimationRight.setInterpolator(new LinearInterpolator());
        rotateAnimationRight.setRepeatMode(Animation.REVERSE);
        rotateAnimationRight.setRepeatCount(1);
        rotateAnimationRight.setDuration(300l);

        RotateAnimation rotateAnimationLeft = new RotateAnimation(0,-45,
        Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        rotateAnimationLeft.setInterpolator(new LinearInterpolator());
        rotateAnimationLeft.setRepeatMode(Animation.REVERSE);
        rotateAnimationLeft.setRepeatCount(1);
        rotateAnimationLeft.setDuration(300l);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f,0.2f);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setDuration(500l);

        rotateAnimationRight.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationStart(Animation animation)
            {

            }

            public void onAnimationEnd(Animation animation)
            {
                animation.cancel();
                mFragHomeNote.startAnimation(rotateAnimationLeft);
            }

            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        rotateAnimationLeft.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationStart(Animation animation)
            {

            }

            public void onAnimationEnd(Animation animation)
            {
                animation.cancel();
                mFragHomeNote.startAnimation(alphaAnimation);
            }

            public void onAnimationRepeat(Animation animation)
            {

            }
        });

        alphaAnimation.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationStart(Animation animation)
            {

            }

            public void onAnimationEnd(Animation animation)
            {
                animation.cancel();
                mFragHomeNote.startAnimation(rotateAnimationRight);
            }

            public void onAnimationRepeat(Animation animation)
            {

            }
        }); mFragHomeNote.startAnimation(rotateAnimationRight);

        TranslateAnimation planTranslateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0f,Animation.RELATIVE_TO_SELF,0.6f,Animation.RELATIVE_TO_SELF,0.1f);
        planTranslateAnimation.setInterpolator(new AccelerateInterpolator());
        planTranslateAnimation.setRepeatCount(AnimationSet.INFINITE);
        planTranslateAnimation.setRepeatMode(AnimationSet.REVERSE);
        planTranslateAnimation.setDuration(3000l);
        AlphaAnimation planAlphaAnimation = new AlphaAnimation(1.0f,0.3f);
        planAlphaAnimation.setInterpolator(new AccelerateInterpolator());
        planAlphaAnimation.setRepeatCount(AnimationSet.INFINITE);
        planAlphaAnimation.setRepeatMode(AnimationSet.REVERSE);
        planAlphaAnimation.setDuration(1500l);
        AnimationSet planAnimationSet = new AnimationSet(true);
        planAnimationSet.addAnimation(planTranslateAnimation);
        planAnimationSet.addAnimation(planAlphaAnimation);
        mHomePlan.startAnimation(planAnimationSet);

        mHomeMenu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                ((MainActivity)getActivity()).toggleDrawerLayout();
            }
        });

        RotateAnimation homeHelpAnimation = new RotateAnimation(0,360,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
        homeHelpAnimation.setInterpolator(new LinearInterpolator());
        homeHelpAnimation.setRepeatCount(Animation.INFINITE);
        homeHelpAnimation.setRepeatMode(Animation.RESTART);
        homeHelpAnimation.setFillAfter(true);
        homeHelpAnimation.setDuration(2000l);
        mHomeHelp.startAnimation(homeHelpAnimation);

        mHomeHelpAll.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                //startActivity(new Intent(getActivity(),AboutActivity.class));
                Intent intent = new Intent(getActivity(),NoteActivity.class);
                startActivity(intent);
            }
        });

        mHomeItemCpu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(getNextCoolDownTime(getActivity()) > System.currentTimeMillis())
                {
                    Intent intent = new Intent(getActivity(), CpuingActivity.class);
                    intent.putExtra("temperature",new DecimalFormat("#.0").format(
                    HardwareTool.getCpuTemperatureFinder(getActivity())) + "℃" +
                    "/" +  (new DecimalFormat("#.0").format(32+ HardwareTool.
                    getCpuTemperatureFinder(getActivity()) * 1.8))+ "℉");
                    intent.putExtra("justcpucooler",false);
                    intent.putExtra("iscooler",true);
                    intent.putExtra("appNumbers",0);
                    startActivity(intent);
                }
                else
                {
                    startActivity(new Intent(getActivity(),CpuActivity.class));
                }
            }
        });

        mHomeItemClean.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(getNextCleanTime(getActivity()) > System.currentTimeMillis())
                {
                    Intent intent = new Intent(getActivity(),CleaningActivity.class);
                    intent.putExtra("justcleanedup",false);
                    intent.putExtra("iscleaned",true);
                    intent.putExtra("garbageSize","");
                    intent.putExtra("appNumbers",0);
                    startActivity(intent);
                }

                else
                {
                    startActivity(new Intent(getActivity(),CleanActivity.class));
                }
            }
        });

        mHomeItemBattery.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                if(getNextHibernateTime(getActivity()) > System.currentTimeMillis())
                {
                    Intent intent = new Intent(getActivity(),BatteringActivity.class);
                    intent.putExtra("battery",getActivity().registerReceiver(null,new
                    IntentFilter(Intent.ACTION_BATTERY_CHANGED)).getIntExtra(BatteryManager.EXTRA_LEVEL, 0) + "");
                    intent.putExtra("justpowersaving",false);
                    intent.putExtra("iselectricitied",true);
                    intent.putExtra("appNumbers",0);
                    startActivity(intent);
                }
                else
                {
                    startActivity(new Intent(getActivity(),BatteryActivity.class));
                }
            }
        });

        mHomeItemBoost.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(getNextBoostTime(getActivity()) > System.currentTimeMillis())
                {
                    Intent intent = new Intent(getActivity(),BoostingActivity.class);
                    intent.putExtra("justboost",false);
                    intent.putExtra("appNumbers",0);
                    intent.putExtra("isboost",true);
                    startActivity(intent);
                }
                else
                {
                    startActivity(new Intent(getActivity().getApplicationContext(),BoostActivity.class));
                }
            }
        });

        mFragHomeNote.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(),NoteActivity.class);
                startActivity(intent);
            }
        });

        mHomeTopItemRam.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(getNextBoostTime(getActivity()) > System.currentTimeMillis())
                {
                    Intent intent = new Intent(getActivity(),BoostingActivity.class);
                    intent.putExtra("justboost",false);
                    intent.putExtra("appNumbers",0);
                    intent.putExtra("isboost",true);
                    startActivity(intent);
                }
                else
                {
                    startActivity(new Intent(getActivity(),BoostActivity.class));
                }
            }
        });

        mHomeTopItemRom.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(getNextCleanTime(getActivity()) > System.currentTimeMillis())
                {
                    Intent intent = new Intent(getActivity(),CleaningActivity.class);
                    intent.putExtra("justcleanedup",true);
                    intent.putExtra("garbageSize","");
                    intent.putExtra("appNumbers",0);
                    startActivity(intent);
                }

                else
                {
                    startActivity(new Intent(getActivity(),CleanActivity.class));
                }
            }
        });

        mHomeTopItemCpu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(getNextCoolDownTime(getActivity()) > System.currentTimeMillis())
                {
                    Intent intent = new Intent(getActivity(), CpuingActivity.class);
                    intent.putExtra("justcpucooler",true);
                    intent.putExtra("temperature","");
                    intent.putExtra("appNumbers",0);
                    startActivity(intent);
                }
                else
                {
                    startActivity(new Intent(getActivity(),CpuActivity.class));
                }
            }
        });
    }

    public void onStart()
    {
        super.onStart();
        long[] romSizes = MemoryTool.getROMTotalAvailable(getContext());
        String[] romTotalTexts = UnitTool.fileSize(romSizes[0]);
        String[] romUsedTexts = UnitTool.fileSize(romSizes[0] - romSizes[1]);
        mRomValue = (int)((romSizes[0] - romSizes[1]) / (float) romSizes[0] * 100) + "";
        mRomDetail = String.format(Locale.getDefault(), "%s%s/%s%s", romUsedTexts[0], romUsedTexts[1], romTotalTexts[0], romTotalTexts[1]);
        ObjectAnimator progressAnimator1 = ObjectAnimator.ofFloat(mNewCircleProgressView,"progress",0,Float.valueOf(mRomValue));
        progressAnimator1.setInterpolator(new OvershootInterpolator());
        progressAnimator1.setDuration(2000);
        progressAnimator1.start();
        mCircleDetail.setText(mRomDetail);
        mCircleValue.setText(mRomValue+"%");
    }

    public void onResume()
    {
        super.onResume();
        if(null != mTimer)
          mTimer.cancel();
        mTimer = new Timer();
        mTimer.schedule(new TimerTask()
        {
            public void run()
            {
                ((MainActivity)getActivity()).runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        initDataUi();
                    }
                });
            }
        },0l,6000l);
        /******************************************************************/
        if(null != mFragHomeItemCpuNum.getAnimation())mFragHomeItemCpuNum.clearAnimation();
        if(getNextCoolDownTime(getActivity()) > System.currentTimeMillis())
        {
            mFragHomeItemCpuNum.setVisibility(View.GONE);
            mCpuItemName.setTextColor(getResources().getColor(R.color.font_lightgray));
            mCpuItemValue.setTextColor(getResources().getColor(R.color.font_lightgray));
            mCpuItemImg.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_cpu_1));

        }
        else
        {
            mFragHomeItemCpuNum.setVisibility(View.VISIBLE);
            mCpuItemName.setTextColor(getResources().getColor(R.color.darkblue));
            mCpuItemValue.setTextColor(getResources().getColor(R.color.darkblue));
            mCpuItemImg.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_cpu_2));
            if(CpuActivity.getLatestApp(getActivity()).size() > 0)
            {
                mFragHomeItemCpuNum.startAnimation(mRedPointSa);
                mFragHomeItemCpuNum.setText(/*CpuActivity.getLatestApp(getActivity()).size() + */"");
            }
            else
            {
                if(CpuActivity.getCoolerCpuApps(getActivity()) != 0)
                {
                    mFragHomeItemCpuNum.startAnimation(mRedPointSa);
                    mFragHomeItemCpuNum.setText(/*CpuActivity.getCoolerCpuApps(getActivity()) +*/ "");
                }
                else
                {
                    int cpuApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(getActivity())) % 7 + 6);
                    if(cpuApps > WhiteListTool.getWhiteListSize(getActivity()))
                        cpuApps = WhiteListTool.getWhiteListSize(getActivity());
                    CpuActivity.setCoolerCpuApps(getActivity(),cpuApps);
                    if(0 != cpuApps)
                    {
                        mFragHomeItemCpuNum.startAnimation(mRedPointSa);
                        mFragHomeItemCpuNum.setText(/*cpuApps +*/ "");
                    }
                    else
                        mFragHomeItemCpuNum.setVisibility(View.GONE);
                }
            }
        }
        /******************************************************************/
        if(null != mFragHomeItemCleanNum.getAnimation())mFragHomeItemCleanNum.clearAnimation();
        if(getNextCleanTime(getActivity()) > System.currentTimeMillis())
        {
            mFragHomeItemCleanNum.setVisibility(View.GONE);
        }
        else
        {
            mFragHomeItemCleanNum.setVisibility(View.VISIBLE);
            if(CleanActivity.getLatestApp(getActivity()).size() > 0)
            {
                mFragHomeItemCleanNum.startAnimation(mRedPointSa);
                mFragHomeItemCleanNum.setText(/*CleanActivity.getLatestApp(getActivity()).size() +*/ "");
            }
            else
            {
                if(CleanActivity.getCleanApps(getActivity()) != 0)
                {
                    mFragHomeItemCleanNum.startAnimation(mRedPointSa);
                    mFragHomeItemCleanNum.setText(/*CleanActivity.getCleanApps(getActivity()) +*/ "");
                }
                else
                {
                    int cleanApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(getActivity())) % 7 + 6);
                    if(cleanApps > WhiteListTool.getWhiteListSize(getActivity()))
                        cleanApps = WhiteListTool.getWhiteListSize(getActivity());
                    CleanActivity.setCleanApps(getActivity(),cleanApps);
                    if(0 != cleanApps)
                    {
                        mFragHomeItemCleanNum.startAnimation(mRedPointSa);
                        mFragHomeItemCleanNum.setText(/*cleanApps +*/ "");
                    }
                    else
                        mFragHomeItemCleanNum.setVisibility(View.GONE);
                }
            }
        }
        /******************************************************************/
        if(null != mFragHomeItemBatteryNum.getAnimation())mFragHomeItemBatteryNum.clearAnimation();
        if(getNextHibernateTime(getActivity()) > System.currentTimeMillis())
        {
            mFragHomeItemBatteryNum.setVisibility(View.GONE);
            mBatteryItemName.setTextColor(getResources().getColor(R.color.font_lightgray));
            mBatteryItemValue.setTextColor(getResources().getColor(R.color.font_lightgray));
            mBatteryItemImg.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_battery_1));
        }
        else
        {
            mFragHomeItemBatteryNum.setVisibility(View.VISIBLE);
            mBatteryItemName.setTextColor(getResources().getColor(R.color.darkblue));
            mBatteryItemValue.setTextColor(getResources().getColor(R.color.darkblue));
            mBatteryItemImg.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_battery_2));
            if(BatteryActivity.getLatestApp(getActivity()).size() > 0)
            {
                mFragHomeItemBatteryNum.startAnimation(mRedPointSa);
                mFragHomeItemBatteryNum.setText(/*BatteryActivity.getLatestApp(getActivity()).size() +*/ "");
            }
            else
            {
                if(BatteryActivity.getBatteryApps(getActivity()) != 0)
                {
                    mFragHomeItemBatteryNum.startAnimation(mRedPointSa);
                    mFragHomeItemBatteryNum.setText(/*BatteryActivity.getBatteryApps(getActivity()) +*/ "");
                }
                else
                {
                    int batteryApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(getActivity())) % 7 + 6);
                    if(batteryApps > WhiteListTool.getWhiteListSize(getActivity()))
                        batteryApps = WhiteListTool.getWhiteListSize(getActivity());
                    BatteryActivity.setBatteryApps(getActivity(),batteryApps);
                    if(0 != batteryApps)
                    {
                        mFragHomeItemBatteryNum.startAnimation(mRedPointSa);
                        mFragHomeItemBatteryNum.setText(/*batteryApps +*/ "");
                    }
                    else
                        mFragHomeItemBatteryNum.setVisibility(View.GONE);

                }
            }
        }
        /******************************************************************/
        if(null != mFragHomeItemBoostNum.getAnimation())mFragHomeItemBoostNum.clearAnimation();
        if(getNextBoostTime(getActivity()) > System.currentTimeMillis())
        {
            mFragHomeItemBoostNum.setVisibility(View.GONE);
            mBoostItemName.setTextColor(getResources().getColor(R.color.font_lightgray));
            mBoostItemValue.setTextColor(getResources().getColor(R.color.font_lightgray));
            mBoostItemImg.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_boost_1));
        }
        else
        {
            mFragHomeItemBoostNum.setVisibility(View.VISIBLE);
            mBoostItemName.setTextColor(getResources().getColor(R.color.darkblue));
            mBoostItemValue.setTextColor(getResources().getColor(R.color.darkblue));
            mBoostItemImg.setImageBitmap(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_boost_2));
            if(BoostActivity.getLatestApp(getActivity()).size() > 0)
            {
                mFragHomeItemBoostNum.startAnimation(mRedPointSa);
                mFragHomeItemBoostNum.setText(/*BoostActivity.getLatestApp(getActivity()).size() +*/ "");
            }
            else
            {
                if(BoostActivity.getBoostApps(getActivity()) != 0)
                {
                    mFragHomeItemBoostNum.startAnimation(mRedPointSa);
                    mFragHomeItemBoostNum.setText(/*BoostActivity.getBoostApps(getActivity()) +*/ "");
                }
                else
                {
                    int boostApps = (int)((Math.random() * WhiteListTool.getWhiteListSize(getActivity())) % 7 + 6);
                    if(boostApps > WhiteListTool.getWhiteListSize(getActivity()))
                        boostApps = WhiteListTool.getWhiteListSize(getActivity());
                    BoostActivity.setBoostApps(getActivity(),boostApps);
                    if(0 != boostApps)
                    {
                        mFragHomeItemBoostNum.startAnimation(mRedPointSa);
                        mFragHomeItemBoostNum.setText(/*boostApps +*/ "");
                    }
                    else
                        mFragHomeItemBoostNum.setVisibility(View.GONE);
                }
            }
        }
        NotifyService.startUpdateNotify(getActivity().getApplicationContext());
    }

    public void onPause()
    {
        super.onPause();
        if(null != mFragHomeItemBatteryNum.getAnimation())mFragHomeItemBatteryNum.clearAnimation();
        if(null != mFragHomeItemBoostNum.getAnimation())mFragHomeItemBoostNum.clearAnimation();
        if(null != mFragHomeItemCleanNum.getAnimation())mFragHomeItemCleanNum.clearAnimation();
        if(null != mFragHomeItemCpuNum.getAnimation())mFragHomeItemCpuNum.clearAnimation();
        if(null != mTimer)
        {
            mTimer.cancel();
        }
    }

    public void initImmersionBar()
    {
        ImmersionBar.with(this).statusBarView(mStateBar).init();

    }
}