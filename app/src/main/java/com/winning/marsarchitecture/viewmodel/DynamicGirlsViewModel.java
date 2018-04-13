package com.winning.marsarchitecture.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.winning.marsarchitecture.datacenter.GirlsResposity;
import com.winning.marsarchitecture.datacenter.db.User;
import com.winning.marsarchitecture.model.GirlsData;

import io.reactivex.subscribers.DisposableSubscriber;

public class DynamicGirlsViewModel extends BaseViewModel<GirlsData> {
    //生命周期观察的数据
    private MutableLiveData<GirlsData> mGirlsDataMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> mUserMutableLiveData = new MutableLiveData<>();

    public DynamicGirlsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<GirlsData> getGirlsData(String size,String index) {
        GirlsResposity.getFuliData(size, index)
                .subscribeWith(new DisposableSubscriber<GirlsData>() {
                    @Override
                    public void onNext(GirlsData o) {
                        if (null != o) {
                            mGirlsDataMutableLiveData.setValue(o);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return mGirlsDataMutableLiveData;
    }

    public LiveData<User> getUserData(){
        GirlsResposity.getUserData(mApplication)
                .subscribeWith(new DisposableSubscriber<User>() {
                    @Override
                    public void onNext(User user) {
                        if (null != user){
                            mUserMutableLiveData.setValue(user);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

        return mUserMutableLiveData;
    }
}
