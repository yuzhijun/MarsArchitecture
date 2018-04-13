package com.winning.marsarchitecture.datacenter.network;

import com.winning.marsarchitecture.model.GirlsData;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

/**
 * Created by yuzhijun on 2018/4/2.
 */

public interface ApiService<T> {
    @GET
    Observable<ResponseBody> getDynamicData(@Url String url);

    @GET ("api/data/福利/{size}/{index}")
    Observable<GirlsData> getFuliData(@Path("size") String size, @Path("index") String index);
}
