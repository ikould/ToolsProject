package com.ikould.phonetest.utils;

import android.text.TextUtils;

import java.lang.reflect.Method;

/**
 * Description
 * Created by chenqiao on 2016/6/15.
 */
public class YunOs {
    public static boolean isYunOS() {
        String version = null;
        String vmName = null;
        try {
            Method m = Class.forName("android.os.SystemProperties").getMethod(
                    "get", String.class);
            version = (String) m.invoke(null, "ro.yunos.version");
            vmName = (String) m.invoke(null, "java.vm.name");
        } catch (Exception e) {
            // nothing todo
        }
        return !TextUtils.isEmpty(version) || vmName != null && vmName.toLowerCase().contains("lemur");
    }

    public static String getYunOSVersion() {
        String version = null;
        String vmName = null;
        try {
            Method m = Class.forName("android.os.SystemProperties").getMethod(
                    "get", String.class);
            version = (String) m.invoke(null, "ro.yunos.version");
            vmName = (String) m.invoke(null, "java.vm.name");
        } catch (Exception e) {
            // nothing todo
        }
        if ((vmName != null && vmName.toLowerCase().contains("lemur"))
                || (version != null && version.trim().length() > 0)) {
            return version;
        } else {
            return "";
        }
    }
}
