package com.expert.cleanup.models;

import java.io.Serializable;
import android.graphics.drawable.Drawable;

public class Boost implements Serializable
{
    private String name;
    private Drawable logo;
    private String packageName;

    public Boost()
    {

    }

    public Boost(String name, Drawable logo, String packageName)
    {
        this.name = name;
        this.logo = logo;
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getLogo() {
        return logo;
    }

    public void setLogo(Drawable logo) {
        this.logo = logo;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}