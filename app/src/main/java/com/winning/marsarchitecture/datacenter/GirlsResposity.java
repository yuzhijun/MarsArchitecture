package com.winning.marsarchitecture.datacenter;

import com.winning.marsarchitecture.datacenter.network.ApiServiceModule;
import com.winning.marsarchitecture.model.GirlsData;
import com.winning.marsarchitecture.util.SwitchSchedulers;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

/**
 * Created by yuzhijun on 2018/4/12.
 */

public class GirlsResposity {

    public static Observable getFuliData(String size, String index) {
         return  ApiServiceModule.getInstance().getNetworkService()
                 .getFuliData(size,index)
                 .compose(SwitchSchedulers.applySchedulers())
                 .map(new Function<GirlsData,GirlsData>() {
                     @Override
                     public GirlsData apply(GirlsData o) throws Exception {
                         return o;
                     }
                 });
    }
}
