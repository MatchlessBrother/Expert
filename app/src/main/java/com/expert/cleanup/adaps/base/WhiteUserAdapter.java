package com.expert.cleanup.adaps.base;

import com.expert.cleanup.R;
import com.expert.cleanup.models.base.WhiteUser;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;

public class WhiteUserAdapter extends BaseQuickAdapter<WhiteUser,BaseViewHolder>
{
    public WhiteUserAdapter()
    {
        super(R.layout.item_whiteuser);
    }

    protected void convert(BaseViewHolder helper,WhiteUser item)
    {
        helper.addOnClickListener(R.id.whiteuser_select);
        helper.setText(R.id.whiteuser_name,item.getName());
        helper.setImageDrawable(R.id.whiteuser_img,item.getLogo());
        if(!item.isSelect())
        {
            helper.setImageResource(R.id.whiteuser_select, R.mipmap.ic_unseleted);
        }
        else
        {
            helper.setImageResource(R.id.whiteuser_select, R.mipmap.ic_seleted);
        }
    }
}