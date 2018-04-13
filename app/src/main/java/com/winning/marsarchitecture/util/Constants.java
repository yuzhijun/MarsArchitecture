package com.winning.marsarchitecture.util;

/**
 * Created by yuzhijun on 2018/4/3.
 */
public class Constants {
    public static final String PATH_URL = "/Path";
    public static final String BASE_URL =  "http://gank.io";
    public static final String GIRLS_URL = "api/data/%E7%A6%8F%E5%88%A9/20/{index}/"+PATH_URL;

    public class HttpCode {
        public static final int HTTP_UNAUTHORIZED = 401;
        public static final int HTTP_SERVER_ERROR = 500;
        public static final int HTTP_NOT_HAVE_NETWORK = 600;
        public static final int HTTP_NETWORK_ERROR = 700;
        public static final int HTTP_UNKNOWN_ERROR = 800;
    }
}
