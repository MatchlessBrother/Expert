package com.expert.cleanup.models.base;

import java.io.Serializable;
import android.graphics.drawable.Drawable;

public class WhiteUser implements Serializable
{
    private String name;
    private Drawable logo;
    private boolean isSelect;
    private String packageName;

    public WhiteUser()
    {

    }

    public WhiteUser(String name, Drawable logo, boolean isSelect, String packageName)
    {
        this.name = name;
        this.logo = logo;
        this.isSelect = isSelect;
        this.packageName = packageName;
    }

    public String getName(

    ) {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Drawable getLogo()
    {
        return logo;
    }

    public void setLogo(Drawable logo)
    {
        this.logo = logo;
    }

    public boolean isSelect()
    {
        return isSelect;
    }

    public void setSelect(boolean select)
    {
        isSelect = select;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }
}