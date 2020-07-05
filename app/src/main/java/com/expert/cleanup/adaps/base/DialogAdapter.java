package com.expert.cleanup.adaps.base;

import com.expert.cleanup.R;
import android.graphics.drawable.Drawable;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.BaseQuickAdapter;

public class DialogAdapter extends BaseQuickAdapter<Drawable,BaseViewHolder>
{
    public DialogAdapter()
    {
        super(R.layout.item_dialog);
    }

    protected void convert(BaseViewHolder helper,Drawable drawable)
    {
        helper.setImageDrawable(R.id.dialog_img,drawable);
    }
}