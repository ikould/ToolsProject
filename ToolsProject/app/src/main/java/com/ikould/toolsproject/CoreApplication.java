package com.ikould.toolsproject;

import android.app.Application;
import android.util.Log;

/**
 * describe
 * Created by 40104 on 2016/12/17.
 */

public class CoreApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("CoreApplication", "onCreate: ");
    }
}
