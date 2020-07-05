package com.expert.cleanup.adaps;

import com.expert.cleanup.R;
import com.expert.cleanup.models.Battery;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;

public class BatteryAdapter extends BaseQuickAdapter<Battery,BaseViewHolder>
{
    public BatteryAdapter()
    {
        super(R.layout.item_battery);
    }

    protected void convert(BaseViewHolder helper,Battery item)
    {
        String[] strNames = item.getName().split(" ");
        helper.addOnClickListener(R.id.battery_item_all);
        helper.setImageDrawable(R.id.battery_img,item.getLogo());
        helper.setText(R.id.battery_name,strNames.length > 1 ? strNames[0] + strNames[1] : strNames[0]);
        if (item.isSelect())
        {
            helper.setImageResource(R.id.battery_select,R.mipmap.ic_seleted);
        }
        else
        {
            helper.setImageResource(R.id.battery_select,R.mipmap.ic_unseleted);
        }
    }
}