package com.winning.marsarchitecture.datacenter;

import com.winning.marsarchitecture.datacenter.network.ApiServiceModule;
import com.winning.marsarchitecture.util.GsonHelper;
import com.winning.marsarchitecture.util.SwitchSchedulers;

import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

/**
 * Created by yuzhijun on 2018/4/12.
 */

public class DataRepository {

    public static <T> Flowable getDynamicData(String url, final Class<T> clazz) {
        return ApiServiceModule.getInstance().getNetworkService()
                .getDynamicData(url)
                .compose(SwitchSchedulers.applySchedulers())
                .map(new Function<ResponseBody, T>() {
                    @Override
                    public T apply(ResponseBody responseBody) throws Exception {
                        return GsonHelper.getIntance().str2JsonBean(responseBody.string(),clazz);
                    }
                });
    }
}
