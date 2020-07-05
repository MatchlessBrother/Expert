package com.expert.cleanup.adaps;

import com.expert.cleanup.R;
import com.expert.cleanup.models.Cpu;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;

public class CpuAdapter extends BaseQuickAdapter<Cpu, BaseViewHolder>
{
    public CpuAdapter()
    {
        super(R.layout.item_cpu);
    }

    protected void convert(BaseViewHolder helper,Cpu item)
    {
        String[] strNames = item.getName().split(" ");
        helper.setImageDrawable(R.id.cpu_img,item.getLogo());
        helper.setText(R.id.cpu_name,strNames.length > 1 ? strNames[0] + strNames[1] : strNames[0]);
    }
}