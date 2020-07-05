package com.expert.cleanup.nets.client;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;

public interface NetAllUrl
{
    String SERVICE_URL = "https://api.mtmin.com/";

    @POST("umi")
    @Multipart
    Observable<ReturnData<ReturnResultConfigInfos>> getAdConfigInfos(@PartMap Map<String, RequestBody> params);

    @POST("rs")
    @Multipart
    Observable<ReturnData<ReturnResultConfigInfos>> updateAdConfigInfos(@PartMap Map<String, RequestBody> params);
}