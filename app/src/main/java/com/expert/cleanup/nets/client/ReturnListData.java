package com.expert.cleanup.nets.client;

public class ReturnListData<T>
{
    private T[] data;
    private String msg;
    private String code;

    public ReturnListData()
    {

    }

    public T[] getData() {
        return data;
    }

    public void setData(T[] data) {
        this.data = data;
    }

    public String getMsg()
    {
        return this.msg;

    }

    public void setMsg(String msg)
    {
        this.msg = msg;

    }

    public String getCode()
    {
        return this.code;

    }

    public void setCode(String code)
    {
        this.code = code;

    }
}