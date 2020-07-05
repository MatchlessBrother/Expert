package com.expert.cleanup.adaps;

import com.expert.cleanup.R;
import com.expert.cleanup.models.Clean;
import com.expert.cleanup.nativetos.base.UnityTool;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;

public class CleanAdapter extends BaseQuickAdapter<Clean,BaseViewHolder>
{
    public CleanAdapter()
    {
        super(R.layout.item_clean);
    }

    protected void convert(BaseViewHolder helper,Clean item)
    {
        String[] strNames = item.getName().split(" ");
        helper.addOnClickListener(R.id.clean_item_all);
        helper.setImageDrawable(R.id.clean_img,item.getLogo());
        helper.setText(R.id.clean_garbage,UnityTool.sizeFormat(item.getGarbageSize()));
        helper.setText(R.id.clean_name,strNames.length > 1 ? strNames[0] + strNames[1] : strNames[0]);
        if (item.isSelect())
        {
            helper.setImageResource(R.id.clean_select,R.mipmap.ic_seleted);
        }
        else
        {
            helper.setImageResource(R.id.clean_select,R.mipmap.ic_unseleted);
        }
    }
}