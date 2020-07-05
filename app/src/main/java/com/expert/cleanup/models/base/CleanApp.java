package com.expert.cleanup.models.base;

import java.io.Serializable;

public class CleanApp implements Serializable
{
    private String name;
    private double garbageSize;

    public CleanApp()
    {

    }

    public CleanApp(String name, double garbageSize)
    {
        this.name = name;
        this.garbageSize = garbageSize;
    }

    public String getName()
    {
        return name;
    }

    public double getGarbageSize()
    {
        return garbageSize;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setGarbageSize(double garbageSize)
    {
        this.garbageSize = garbageSize;
    }
}