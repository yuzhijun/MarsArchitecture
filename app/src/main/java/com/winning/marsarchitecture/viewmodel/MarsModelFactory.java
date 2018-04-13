package com.winning.marsarchitecture.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import java.util.Map;

/**
 * Created by yuzhijun on 2018/4/13.
 */

public class MarsModelFactory implements ViewModelProvider.Factory {
    private Map<Class<? extends ViewModel>, ViewModel> creators;

    public MarsModelFactory(Map<Class<? extends ViewModel>, ViewModel> creators) {
        this.creators = creators;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        ViewModel creator = creators.get(modelClass);
        if (creator == null) {
            for (Map.Entry<Class<? extends ViewModel>, ViewModel> entry : creators.entrySet()) {
                if (modelClass.isAssignableFrom(entry.getKey())) {
                    creator = entry.getValue();
                    break;
                }
            }
        }
        if (creator == null) {
            throw new IllegalArgumentException("unknown model class " + modelClass);
        }
        try {
            return (T) creator;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
