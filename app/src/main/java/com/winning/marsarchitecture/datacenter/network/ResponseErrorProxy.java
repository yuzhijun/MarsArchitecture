package com.winning.marsarchitecture.datacenter.network;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import org.apache.http.conn.ConnectTimeoutException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

import static com.winning.marsarchitecture.util.Constants.HttpCode.HTTP_NETWORK_ERROR;
import static com.winning.marsarchitecture.util.Constants.HttpCode.HTTP_SERVER_ERROR;
import static com.winning.marsarchitecture.util.Constants.HttpCode.HTTP_UNAUTHORIZED;
import static com.winning.marsarchitecture.util.Constants.HttpCode.HTTP_UNKNOWN_ERROR;

public class ResponseErrorProxy implements InvocationHandler {
    public static final String TAG = ResponseErrorProxy.class.getSimpleName();

    private Object mProxyObject;
    private String url;

    public ResponseErrorProxy(Object proxyObject,String url) {
        mProxyObject = proxyObject;
        this.url = url;
    }

    @Override
    public Object invoke(Object proxy, final Method method, final Object[] args) {
           return Observable.just("")
                   .flatMap(new Function<String, ObservableSource<?>>() {
                       @Override
                       public ObservableSource<?> apply(String s) throws Exception {
                           return (ObservableSource<?>) method.invoke(mProxyObject, args);
                       }
                   })
                    .retryWhen(new Function<Observable<Throwable>, ObservableSource<?>>() {
                        @Override
                        public ObservableSource<?> apply(Observable<Throwable> throwableObservable) throws Exception {
                            return throwableObservable.flatMap(new Function<Throwable, ObservableSource<?>>() {
                                @Override
                                public Observable<?> apply(Throwable throwable) {
                                    ResponseError error = null;
                                    if (throwable instanceof ConnectTimeoutException
                                            || throwable instanceof SocketTimeoutException
                                            || throwable instanceof UnknownHostException
                                            || throwable instanceof ConnectException) {
                                        error = new ResponseError(HTTP_NETWORK_ERROR, "当前网络环境较差，请稍后重试!");
                                    } else if (throwable instanceof retrofit2.HttpException) {
                                        retrofit2.HttpException exception = (retrofit2.HttpException) throwable;
                                        try {
                                            error = new Gson().fromJson(exception.response().errorBody().string(), ResponseError.class);
                                        } catch (Exception e) {
                                            if (e instanceof JsonParseException) {
                                                error = new ResponseError(HTTP_SERVER_ERROR, "抱歉！服务器出错了!");
                                            } else {
                                                error = new ResponseError(HTTP_UNKNOWN_ERROR, "抱歉！系统出现未知错误!");
                                            }
                                        }
                                    } else if (throwable instanceof JsonParseException) {
                                        error = new ResponseError(HTTP_SERVER_ERROR, "抱歉！服务器出错了!");
                                    } else {
                                        error = new ResponseError(HTTP_UNKNOWN_ERROR, "抱歉！系统出现未知错误!");
                                    }

                                    if (error.getStatus() == HTTP_UNAUTHORIZED) {
                                        return refreshTokenWhenTokenInvalid();
                                    } else {
                                        return Observable.error(error);
                                    }
                                }
                            });
                        }
                    });
    }

    private Observable<?> refreshTokenWhenTokenInvalid() {
        synchronized (ResponseErrorProxy.class) {
            return Observable.error(new ResponseError(HTTP_SERVER_ERROR, "抱歉！服务器出错了!"));
        }
    }
}
