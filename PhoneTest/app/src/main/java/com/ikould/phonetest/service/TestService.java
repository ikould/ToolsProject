package com.ikould.phonetest.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * describe
 * Created by liudong on 2017/1/3.
 */

public class TestService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("TestService", "onBind: ");
        return binder;
    }

    private Binder binder = new Binder(){

    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TestService", "onCreate: ");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("TestService", "onStart: ");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TestService", "onStartCommand: ");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("TestService", "onDestroy: ");
    }
}
