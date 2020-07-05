package com.expert.cleanup.acts.base;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import com.expert.cleanup.R;
import android.content.Intent;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.gyf.immersionbar.ImmersionBar;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity
{
    private View mStatebar;
    private TextView mAboutRate;
    private TextView mAboutHelp;
    private ImageView mAboutMenu;
    private TextView mAboutShare;
    private TextView mAboutUpdate;
    private TextView mAboutVersion;
    private TextView mAboutPrivacy;
    private TextView mAboutFeedback;

    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        mStatebar = (View) findViewById(R.id.statebar);
        mAboutUpdate = findViewById(R.id.about_update);
        mAboutRate = (TextView)findViewById(R.id.about_rate);
        mAboutHelp = (TextView) findViewById(R.id.about_help);
        mAboutMenu = (ImageView) findViewById(R.id.about_menu);
        mAboutShare = (TextView) findViewById(R.id.about_share);
        ImmersionBar.with(this).statusBarView(mStatebar).init();
        mAboutPrivacy = (TextView) findViewById(R.id.about_privacy);
        mAboutVersion = (TextView) findViewById(R.id.about_version);
        mAboutFeedback = (TextView) findViewById(R.id.about_feedback);
        try
        {
            mAboutVersion.setText("V " +  getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        mAboutMenu.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                finish();
            }
        });

        mAboutShare.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                Intent intent = new Intent();
                intent.setType("text/plain");
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getPackageName());
                startActivity(intent);/********************************************************************************/
            }
        });

        mAboutFeedback.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String[] email = {"nbklass@gmail.com"};
                Uri uri = Uri.parse("mailto:nbklass@gmail.com");
                Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                intent.putExtra(Intent.EXTRA_TEXT,"");// 正文
                intent.putExtra(Intent.EXTRA_CC,email);// 抄送人
                intent.putExtra(Intent.EXTRA_SUBJECT,"Feedback Info");// 主题
                startActivity(Intent.createChooser(intent,"Please select the mail application!"));
            }
        });

        mAboutHelp.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                String[] email = {"nbklass@gmail.com"};
                Uri uri = Uri.parse("mailto:nbklass@gmail.com");
                Intent intent = new Intent(Intent.ACTION_SENDTO,uri);
                intent.putExtra(Intent.EXTRA_TEXT,"");// 正文
                intent.putExtra(Intent.EXTRA_CC,email);// 抄送人
                intent.putExtra(Intent.EXTRA_SUBJECT,"Help us translate");// 主题
                startActivity(Intent.createChooser(intent,"Please select the mail application!"));
            }
        });

        mAboutPrivacy.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                startActivity(new Intent(AboutActivity.this,PrivacyActivity.class));
            }
        });

        mAboutUpdate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                goWrite();
            }
        });

        mAboutRate.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                goWrite();
            }
        });
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