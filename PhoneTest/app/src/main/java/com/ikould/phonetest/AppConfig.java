package com.ikould.phonetest;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by liudong on 2016/4/26.
 */
public class AppConfig {
    private static AppConfig fInstance;
    private SharedPreferences sharedPrefs;

    public static AppConfig getInstance() {
        if (fInstance == null) {
            fInstance = new AppConfig();
        }
        return fInstance;
    }

    private AppConfig() {
        sharedPrefs = CoreApplication.getInstance().getSharedPreferences("test_data", Context.MODE_PRIVATE);// .MODE_PRIVATE);
    }

    public void setTestIsOpen(boolean isOpen) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean("testIsOpen", isOpen);
        editor.apply();
    }

    public boolean isTestOpen() {
        return sharedPrefs.getBoolean("testIsOpen", false);
    }

    public boolean isTest2Open() {
        return sharedPrefs.getBoolean(null, false);
    }

    public void setTest2Open(boolean isOpen) {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putBoolean(null, isOpen);
        editor.apply();
    }

}
