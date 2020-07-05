package com.expert.cleanup.nets.okhttp.Http2CookieJars;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import okhttp3.Cookie;
import okhttp3.Cookie.Builder;

/*****原生Cookie不能序列化,这里创建可序列化Cookie的包装类*****/
public class Cookie_PersistentStoreObj implements Serializable
{
    /***********新生反序列化的Cookie*********/
    private transient Cookie mNewCookie;
    /**********原生需序列化的Cookie**********/
    private transient final Cookie mOldCookie;

    public Cookie_PersistentStoreObj(Cookie cookie)
    {
        this.mOldCookie = cookie;
    }

    /*******************************获取最新的Cookie对象******************************/
    public Cookie getCookies()
    {
        return mNewCookie != null ? mNewCookie : mOldCookie;
    }

    /********************将原生Cookie对象输出到ObjectOutputStream流中*****************/
    private void writeObject(ObjectOutputStream out) throws IOException
    {
        out.writeObject(mOldCookie.name());
        out.writeObject(mOldCookie.value());
        out.writeLong(mOldCookie.expiresAt());
        out.writeObject(mOldCookie.domain());
        out.writeObject(mOldCookie.path());
        out.writeBoolean(mOldCookie.secure());
        out.writeBoolean(mOldCookie.httpOnly());
        out.writeBoolean(mOldCookie.hostOnly());
        out.writeBoolean(mOldCookie.persistent());
    }

    /*******************从ObjectInputStream流中反序列化生成Cookie对象*****************/
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
    {
        String name = (String) in.readObject();
        String value = (String) in.readObject();
        long expiresAt = in.readLong();
        String domain = (String) in.readObject();
        String path = (String) in.readObject();
        boolean secure = in.readBoolean();
        boolean httpOnly = in.readBoolean();
        boolean hostOnly = in.readBoolean();
        boolean persistent = in.readBoolean();
        Builder builder = new Builder();
        builder = builder.name(name);
        builder = builder.path(path);
        builder = builder.value(value);
        builder = builder.expiresAt(expiresAt);
        builder = secure ? builder.secure() : builder;
        builder = httpOnly ? builder.httpOnly() : builder;
        builder = hostOnly ? builder.hostOnlyDomain(domain) : builder.domain(domain);
        mNewCookie = builder.build();
    }
}