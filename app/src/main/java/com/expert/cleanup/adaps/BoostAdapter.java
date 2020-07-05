package com.expert.cleanup.adaps;

import com.expert.cleanup.R;
import com.expert.cleanup.models.Boost;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;

public class BoostAdapter extends BaseQuickAdapter<Boost,BaseViewHolder>
{
    public BoostAdapter()
    {
        super(R.layout.item_boost);
    }

    protected void convert(BaseViewHolder helper, Boost item)
    {
        String[] strNames = item.getName().split(" ");
        helper.setImageDrawable(R.id.boost_img,item.getLogo());
        helper.setText(R.id.boost_name,strNames.length > 1 ? strNames[0] + strNames[1] : strNames[0]);
    }
}