package com.expert.cleanup.acts.base;

import android.os.Bundle;
import android.view.View;
import com.expert.cleanup.R;
import android.content.Context;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.gyf.immersionbar.ImmersionBar;

import io.reactivex.disposables.Disposable;
import androidx.appcompat.app.AppCompatActivity;

public class NoteActivity extends AppCompatActivity
{
    private View statebar;
    private TextView mNoteNum;
    private ImageView mNoteMenu;
    private TextView mNoteTitle;
    private ImageView mNoteBack;
    private ImageView mNoteNext;
    private TextView mNoteBackTv;
    private TextView mNoteNextTv;
    private TextView mNoteContent;
    private LinearLayout mNoteTitleAll;

    private boolean isLoadedAds;
    private int mTriedNumsForShow;
    private Disposable mDisposable;
    private Disposable mDisposableBtn;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        statebar = findViewById(R.id.statebar);
        mNoteNum = findViewById(R.id.note_num);
        mNoteMenu = findViewById(R.id.note_menu);
        mNoteBack = findViewById(R.id.note_back);
        mNoteNext = findViewById(R.id.note_next);
        mNoteTitle = findViewById(R.id.note_title);
        mNoteBackTv = findViewById(R.id.note_back_tv);
        mNoteNextTv = findViewById(R.id.note_next_tv);
        mNoteContent = findViewById(R.id.note_content);
        mNoteTitleAll = findViewById(R.id.note_title_all);
        updateUi();/*************************************/
        ImmersionBar.with(this).statusBarView(statebar).init();
        mNoteBack.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(null != mDisposable && !mDisposable.isDisposed())
                    mDisposable.dispose();
                setCurrentShowPageNum(getCurrentShowPageNum()-1);
                updateUi();
            }
        });

        mNoteNext.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(null != mDisposable && !mDisposable.isDisposed())
                    mDisposable.dispose();
                setCurrentShowPageNum(getCurrentShowPageNum()+1);
                updateUi();
            }
        });

        mNoteMenu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                if(getCurrentShowPageNum() == 11)
                    setCurrentShowPageNum(1);
                else
                    setCurrentShowPageNum(getCurrentShowPageNum()+1);
                finish();
            }
        });
    }

    public void updateUi()
    {
        switch(getCurrentShowPageNum())
        {
            case 1:
            {
                mNoteNum.setText("1");
                mNoteContent.setText(
                "Be cautious of tests, lucky draw, and horoscope online.\n" +
                "Most result of those tests are nonsense and lack of scientific basis. It just causes confidential and privacy leak.");
                break;
            }
            case 2:
            {
                mNoteNum.setText("2");
                mNoteContent.setText(
                "Download apps from Google play or Apple App store. \n" +
                "Download apps from unkown or informal source may cause damage to information privacy and property security");
                break;
            }
            case 3:
            {
                mNoteNum.setText("3");
                mNoteContent.setText(
                "Use anti-virus, anti-spyware, and clean software on your phone. \n" +
                "Regularlly use those capability to protect and boost phones.");
                break;
            }
            case 4:
            {
                mNoteNum.setText("4");
                mNoteContent.setText(
                "Turn off location sharing. \n" +
                "Minimizing the location access can also help increase the battery life on your phone. Try to turn off location so you’re not sharing your location unknowingly.\n");
                break;
            }
            case 5:
            {
                mNoteNum.setText("5");
                mNoteContent.setText(
                "Safe Charging. \n" +
                "Use original chargers provided by phone manufacturers to avoid hazard of electrical shock.");
                break;
            }
            case 6:
            {
                mNoteNum.setText("6");
                mNoteContent.setText("Check your privacy & security settings. Don’t share your locations ,contacts to unrecognized and unreliable 3-rd parties.");
                break;
            }
            case 7:
            {
                mNoteNum.setText("7");
                mNoteContent.setText("Turn off Bluetooth when not using. It drains your battery a lot and give other access to your device.");
                break;
            }
            case 8:
            {
                mNoteNum.setText("8");
                mNoteContent.setText("Avoid use of phone and wired headset while charging. There have been some reports about accidents happening during charging.");
                break;
            }
            case 9:
            {
                mNoteNum.setText("9");
                mNoteContent.setText(
                "Try not to store sensitive information on your phone. \n" +
                "Don’t use 1 password for all your accounts. \n" +
                "Beware of private information when dealing with your old mobile phone.");
                break;
            }
            case 10:
            {
                mNoteNum.setText("10");
                mNoteContent.setText("Avoid calls at places with low signal reception and try to keep cell phone away from you while you sleeping.");
                break;
            }
            case 11:
            {
                mNoteNum.setText("11");
                mNoteContent.setText("Remove covers and cool spot. Removing phone covers wil help keep the battery at room temperature or lower. Of course, not needed if the avg. temperature in your room is already below 30 degrees Celsius.");
                break;
            }
        }
        mDisposable = null;
        isLoadedAds = false;
        mTriedNumsForShow = 0;
        mNoteBack.setVisibility(View.GONE);
        mNoteNext.setVisibility(View.GONE);
        if(getCurrentShowPageNum() == 1)
        {
            mNoteBackTv.setVisibility(View.GONE);
            mNoteNextTv.setVisibility(View.VISIBLE);
        }
        else if(getCurrentShowPageNum() == 11)
        {
            mNoteNextTv.setVisibility(View.GONE);
            mNoteBackTv.setVisibility(View.VISIBLE);
        }
        else
        {
            mNoteNextTv.setVisibility(View.VISIBLE);
            mNoteBackTv.setVisibility(View.VISIBLE);
        }
        /*mDisposableBtn = Observable.interval(0l,1000l, TimeUnit.MILLISECONDS).take(2).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>()
        {
            public void accept(Long aLong) throws Exception
            {
                if(aLong < 1)
                {
                    mNoteBackTv.setText((5 - aLong) + "");
                    mNoteNextTv.setText((5 - aLong) + "");
                }
                else
                {
                    mNoteBackTv.setVisibility(View.GONE);
                    mNoteNextTv.setVisibility(View.GONE);
                    if(getCurrentShowPageNum() == 1)
                    {
                        mNoteBack.setVisibility(View.GONE);
                        mNoteNext.setVisibility(View.VISIBLE);
                    }
                    else if(getCurrentShowPageNum() == 11)
                    {
                        mNoteNext.setVisibility(View.GONE);
                        mNoteBack.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        mNoteNext.setVisibility(View.VISIBLE);
                        mNoteBack.setVisibility(View.VISIBLE);
                    }
                    if(null != mDisposableBtn && !mDisposableBtn.isDisposed())
                        mDisposableBtn.dispose();
                }
            }
        });*/
        mNoteBackTv.setVisibility(View.GONE);
        mNoteNextTv.setVisibility(View.GONE);
        if(getCurrentShowPageNum() == 1)
        {
            mNoteBack.setVisibility(View.GONE);
            mNoteNext.setVisibility(View.VISIBLE);
        }
        else if(getCurrentShowPageNum() == 11)
        {
            mNoteNext.setVisibility(View.GONE);
            mNoteBack.setVisibility(View.VISIBLE);
        }
        else
        {
            mNoteNext.setVisibility(View.VISIBLE);
            mNoteBack.setVisibility(View.VISIBLE);
        }
        if(null != mDisposableBtn && !mDisposableBtn.isDisposed())
            mDisposableBtn.dispose();
    }

    public void onBackPressed()
    {
        super.onBackPressed();
        if(getCurrentShowPageNum() == 11)
            setCurrentShowPageNum(1);
        else
            setCurrentShowPageNum(getCurrentShowPageNum()+1);
    }

    public int getCurrentShowPageNum()
    {
        return getSharedPreferences(getPackageName(),Context.MODE_PRIVATE).getInt("currentShowPage",1);
    }

    public void setCurrentShowPageNum(int pageNum)
    {
       getSharedPreferences(getPackageName(),Context.MODE_PRIVATE).edit().putInt("currentShowPage",pageNum).commit();
    }
}