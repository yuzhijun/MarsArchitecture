package com.winning.marsarchitecture.viewmodel;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.winning.marsarchitecture.datacenter.GirlsResposity;
import com.winning.marsarchitecture.model.GirlsData;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DynamicGirlsViewModel extends BaseViewModel<GirlsData> {
    //生命周期观察的数据
    private MutableLiveData<GirlsData> mGirlsDataMutableLiveData = new MutableLiveData<>();

    public DynamicGirlsViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<GirlsData> getGirlsData(String size,String index) {
        GirlsResposity.getFuliData(size, index)
                .subscribe(new Observer<GirlsData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(GirlsData o) {
                        if (null != o) {
                            mGirlsDataMutableLiveData.setValue(o);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return mGirlsDataMutableLiveData;
    }
}
