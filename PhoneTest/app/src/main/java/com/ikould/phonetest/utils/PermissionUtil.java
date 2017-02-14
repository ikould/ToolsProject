package com.ikould.phonetest.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * 权限管理工具类
 * Created by liudong on 2017/1/6.
 */

public class PermissionUtil {

    /**
     * 是否有某一权限
     *
     * @param context
     * @param permission
     * @return
     */
    public static boolean checkIsHavePermision(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission) ==
                PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 请求权限
     *
     * @param activity
     * @param permissions
     * @param requestCode
     */
    public static void requestPersion(Activity activity, String[] permissions, int requestCode) {
        ActivityCompat.requestPermissions(activity, permissions, requestCode);
    }


}
