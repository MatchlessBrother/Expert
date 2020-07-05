package com.expert.cleanup.acts.base;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import com.expert.cleanup.R;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.gyf.immersionbar.ImmersionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PrivacyActivity extends AppCompatActivity
{
    private View mStatebar;
    private ImageView mPrivacyMenu;
    private TextView mPrivacyContent;

    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);
        mStatebar = (View) findViewById(R.id.statebar);
        ImmersionBar.with(this).statusBarView(mStatebar).init();
        mPrivacyMenu = (ImageView) findViewById(R.id.privacy_menu);
        mPrivacyContent = (TextView) findViewById(R.id.privacy_content);
        mPrivacyContent.setText(Html.fromHtml(getString(R.string.privacy_policy)));
        mPrivacyMenu.setOnClickListener(new View.OnClickListener()/***************/
        {
            public void onClick(View view)
            {
                finish();
            }
        });
    }
}