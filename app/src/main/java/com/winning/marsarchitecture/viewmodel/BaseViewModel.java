package com.winning.marsarchitecture.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.winning.marsarchitecture.datacenter.DataRepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.subscribers.DisposableSubscriber;

import static com.winning.marsarchitecture.util.Constants.PATH_URL;

/**
 * Created by yuzhijun on 2018/4/12.
 */

public class BaseViewModel<T> extends AndroidViewModel implements IRequest<T>{
    private static final Pattern pattern = Pattern.compile("\\{(.*?)\\}");
    protected Application mApplication;
    //生命周期观察的数据
    private MutableLiveData<T> liveObservableData = new MutableLiveData<>();
    public final CompositeDisposable mDisposable = new CompositeDisposable();

    private static final MutableLiveData ABSENT = new MutableLiveData();
    {
        //noinspection unchecked
        ABSENT.setValue(null);
    }

    public BaseViewModel(@NonNull Application application) {
        super(application);
        mApplication = application;
    }

    /**
     * LiveData支持了lifecycle生命周期检测
     * @return
     */
    public LiveData<T> getELiveObservableData(String url, String... params) {
            IRequest request = (IRequest) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{IRequest.class}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Object object = null;
                    try {
                        String url = redirectParseMethodUrl(args);
                        if (null != url){
                            object = getLiveObservableData(url);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    return object;
                }
            });

            request.getLiveObservableData(url,params);
        return liveObservableData;
    }


    public LiveData<T> getLiveObservableData(String url,String...params) {
        DataRepository.getDynamicData(url,getTClass())
                .subscribeWith(new DisposableSubscriber<T>() {
                    @Override
                    public void onNext(T value) {
                        if(null != value){
                            liveObservableData.setValue(value);
                        }
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return liveObservableData;
    }

    private String redirectParseMethodUrl(Object[] args){
        if (null != args && args.length > 0){
            String url = (String) args[0];
            boolean isPath = false;
            if (null != url){
                isPath = url.contains(PATH_URL);
                if (isPath){
                   return parseMethodUrlPath(args);
                }else{
                   return parseMethodUrlKey(args);
                }
            }
        }
        return "";
    }

    private String parseMethodUrlKey(Object[] args){
        String baseUrl = "";
        if (null != args && args.length > 0){
            String url = (String) args[0];
            int index = url.indexOf("{");
            if (index > 0){
                baseUrl = url.substring(0,index-1);
                Matcher matcher = pattern.matcher(url);
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("?");
                for (int i = 0;matcher.find();i++){
                    String key = matcher.group();
                    String realKey = key.substring(key.indexOf("{")+1,key.lastIndexOf("}"));
                    Object[] value = (Object[]) args[1];
                    if (value != null){
                        stringBuffer.append(realKey+"="+value[i]+ "&");
                    }
                }

                baseUrl += stringBuffer.toString();
            }
        }

        return baseUrl;
    }

    private String parseMethodUrlPath(Object[] args) {
        String url = "";
        if (null != args && args.length > 0){
            url = (String) args[0];
            url = url.substring(0,url.lastIndexOf("/") - 1);
            Matcher matcher = pattern.matcher(url);
            for (int i = 0;matcher.find();i++){
                Object[] value = (Object[]) args[1];
                String key = matcher.group();
                if (null != value){
                    url = url.replace(key,(String)value[i]);
                }
            }
        }

        return url;
    }

    public Class<T> getTClass(){
        Class<T> tClass = (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDisposable.clear();
    }
}
