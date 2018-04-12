package com.winning.marsarchitecture.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class GsonHelper {
    private static Gson mGson;
    private static GsonHelper mInstance;
    private static final SimpleDateFormat JSON_STRING_DATE = new SimpleDateFormat("yyy-MM-dd", Locale.CHINA);
    private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("GMT-8:00");
    private static final long SECOND_IN_MILLISECONDS = 1000L;

    private GsonHelper(){

    }

    public static GsonHelper getIntance(){
        if (null == mInstance){
            synchronized (GsonHelper.class){
                if (null == mInstance){
                    mGson = GsonHelper.gsonBuilder();
                    mInstance = new GsonHelper();
                }
            }
        }
        return mInstance;
    }

    public static Gson gsonBuilder() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Integer.class, new JsonDeserializer() {
            @Override
            public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                try {
                    return json.getAsInt();
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        builder.registerTypeAdapter(Date.class, new JsonDeserializer() {
            @Override
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                try {
                    long outer = json.getAsLong();
                    Calendar calendar = Calendar.getInstance(TIME_ZONE);
                    calendar.setTimeInMillis(outer * SECOND_IN_MILLISECONDS);
                    return calendar.getTime();
                } catch (NumberFormatException e) {
                    try {
                        return JSON_STRING_DATE.parse(json.getAsString());
                    } catch (ParseException e1) {
                        throw new JsonParseException(e1);
                    }
                }
            }
        });
        return builder.create();
    }

    public <T> T str2JsonBean(String json, Class<T> clazz) {
        T bean = null;
        if (null != mGson) {
            try {
                bean = mGson.fromJson(json, clazz);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }
}
