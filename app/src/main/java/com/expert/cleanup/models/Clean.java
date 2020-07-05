package com.expert.cleanup.models;

import java.io.Serializable;
import android.graphics.drawable.Drawable;

public class Clean implements Serializable
{
    private String name;
    private Drawable logo;
    private boolean isSelect;
    private String packageName;
    private double garbageSize;

    public Clean()
    {

    }

    public Clean(String name, Drawable logo, boolean isSelect, String packageName, double garbageSize)
    {
        this.name = name;
        this.logo = logo;
        this.isSelect = isSelect;
        this.packageName = packageName;
        this.garbageSize = garbageSize;
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

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public double getGarbageSize() {
        return garbageSize;
    }

    public void setGarbageSize(double garbageSize) {
        this.garbageSize = garbageSize;
    }
}