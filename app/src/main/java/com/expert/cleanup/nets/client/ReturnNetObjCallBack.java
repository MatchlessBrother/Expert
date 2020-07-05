package com.expert.cleanup.nets.client;

import android.content.Context;
import android.util.Log;

import com.expert.cleanup.nets.util.NetUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

import static com.expert.cleanup.nets.NetStateReceiver.isPrintNetLog;

/******************************查询网络数据回调的处理类****************************/
public abstract class ReturnNetObjCallBack<T extends ReturnData> implements Observer<T>
{
    private Context mContext;
    private Disposable mDisposable;

    public ReturnNetObjCallBack(Context context)
    {
        mContext = context;
        mDisposable = null;
    }

    public void onSubscribe(Disposable disposable)
    {
        onStart();
        mDisposable = disposable;
    }

    public void onNext(T baseReturnData)
    {
        if(NetFlags.RequestSuccess.equals(baseReturnData.getCode()))
        {
            onSuccess(baseReturnData);
        }
        else if(NetFlags.RequestFailForNull.equals(baseReturnData.getCode()))
        {
            onFailure(baseReturnData.getMsg());
        }
        else if(NetFlags.RequestFailForTooLong.equals(baseReturnData.getCode()))
        {
            onFailure(baseReturnData.getMsg());
        }
        else if(NetFlags.RequestFailForBlackList.equals(baseReturnData.getCode()))
        {
            onFailure(baseReturnData.getMsg());
        }
        else if(NetFlags.RequestFailForDataWritingFailure.equals(baseReturnData.getCode()))
        {
            onFailure(baseReturnData.getMsg());
        }
    }

    public void onError(Throwable e)
    {
        if(!NetUtils.WhetherConnectNet(mContext))
        {
            onError("The network is not available. Please try again later.");
        }
        else if(e instanceof SocketTimeoutException)
        {
            onError("SocketTimeoutException. Please try again later.");
        }
        else if(e instanceof ConnectException)
        {
            onError("ConnectException. Please try again later.");
        }
        else if(e instanceof HttpException)
        {
            onError("HttpException. Please try again later.");
        }
        else if(e instanceof UnknownHostException)
        {
            onError("UnknownHostException. Please try again later.");
        }
        else
        {
            onError(e.getMessage() + ". Please try again later.");
        }
        onFinish();
    }

    public void onComplete()
    {
        onFinish();

    }

    /*****************************************数据请求成功*****************************************/
    public abstract void onSuccess(T data);

    /*****************************************数据请求失败*****************************************/
    public void onFailure(String msg)
    {
        if(isPrintNetLog)
        {
            Log.i("AdsNotes","onFailure:" + msg);
        }
    }

    /***********************请求数据失败,指在请求网络接口时，出现无法联网**************************/
    /***********************缺少权限,内存泄露等原因导致无法连接到请求数据源************************/
    public void onError(String msg)
    {
        if(isPrintNetLog)
        {
            Log.i("AdsNotes","onError:" + msg);
        }
    }

    /*****************************************开始请求数据*****************************************/
    public void onStart()
    {

    }

    /*********************当请求数据结束时，无论请求结果是成功，失败或是抛出异常都*****************/
    /*********************会执行此方法给用户做处理通常隐藏“正在加载”的等待控件*******************/
    public void onFinish()
    {

    }
}