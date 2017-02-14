package com.ikould.phonetest;

import android.app.Application;
import android.content.Intent;

import com.ikould.phonetest.service.TestService;
import com.ikould.phonetest.utils.ScreenOffManager;

/**
 * describe
 * Created by liudong on 2016/12/26.
 */

public class CoreApplication extends Application {

    private static CoreApplication instance;
    public ScreenOffManager screenOffManager;

    public static CoreApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        screenOffManager = new ScreenOffManager();
    }
}
