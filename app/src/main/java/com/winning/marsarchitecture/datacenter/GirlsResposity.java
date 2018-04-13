package com.winning.marsarchitecture.datacenter;

import android.app.Application;

import com.winning.marsarchitecture.datacenter.db.AppDatabase;
import com.winning.marsarchitecture.datacenter.db.User;
import com.winning.marsarchitecture.datacenter.network.ApiServiceModule;
import com.winning.marsarchitecture.util.AppExecutors;
import com.winning.marsarchitecture.util.SwitchSchedulers;

import io.reactivex.Flowable;

/**
 * Created by yuzhijun on 2018/4/12.
 */

public class GirlsResposity {
    private static AppExecutors mAppExecutors = new AppExecutors();
    public static Flowable getFuliData(String size, String index) {
         return  ApiServiceModule.getInstance().getNetworkService()
                 .getFuliData(size,index)
                 .compose(SwitchSchedulers.applySchedulers())
                 .map(girlsData -> girlsData);
    }

    public static Flowable<User> getUserData(Application application){
        return AppDatabase.getInstance(application,mAppExecutors)
                .userDao().getUser();
    }
}
