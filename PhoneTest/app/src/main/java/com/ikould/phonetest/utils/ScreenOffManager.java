package com.ikould.phonetest.utils;

import android.Manifest;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description  休眠管理器
 * 根据各种判定条件来决定采用何种关屏方式：
 * 1.固件日期在20160501之前的，采用黑布遮盖方式
 * 2.固件在20160501之后的，但获取不到判定属性的，使用GPIO+黑布关屏方式
 * 3.能获取到判定属性的，使用真正的休眠方式
 * <p>
 * 在主AndroidManifest.xml中添加如下：
 * <receiver
 * android:name="com.ebanswers.sdk.util.sleep.SleepAdminReceiver"
 * android:description="@string/app_name"
 * android:label="@string/app_name"
 * android:permission="android.permission.BIND_DEVICE_ADMIN">
 * <meta-data
 * android:name="android.app.device_admin"
 * android:resource="@xml/lock_screen" />
 * <p>
 * <intent-filter>
 * <action android:name="android.app.action.DEVICE_ADMIN_ENABLED" />
 * </intent-filter>
 * </receiver>
 * Created by chenqiao on 2016/8/3.
 */
public class ScreenOffManager {
    private static int type = 0;//0-黑布;1-Gpio;2-休眠
    private WindowManager wm;
    private View shadowView;
    private static DevicePolicyManager policyManager;
    private static ScreenChange callBack;

    /**
     * 初始化判断
     *
     * @param context 必须是Activity
     */
    public static void init(Context context) {
        if (canLock()) {
            type = 2;
            if (!isActive(context)) {
                activeManager(context);
            }
        } else {
            if (isAfter0501()) {
                type = 1;
            } else {
                type = 0;
            }
        }
        WakeUtils.getInstance(context).lockCpuRunning();
    }

    public static void releaseWake(Context context) {
        WakeUtils.getInstance(context).releaseCpuRunning();
    }

    /**
     * 初始化判断
     *
     * @param context 必须是Activity
     */
    public static void init(Context context, int t) {
        switch (t) {
            case 0:
                type = 0;
                break;
            case 1:
                type = 1;
                break;
            case 2:
                type = 2;
                if (!isActive(context)) {
                    activeManager(context);
                }
                break;

        }
        WakeUtils.getInstance(context).lockCpuRunning();
    }

    /**
     * 锁屏
     *
     * @param context 尽量传Activity，不然可能会报错
     */
    public void sleep(final Context context) {
        switch (type) {
            case 1:
                // Gpio.writeGpio("io3", 0);
            case 0:
                if (wm == null) {
                    wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                }
                if (shadowView != null && shadowView.getParent() != null) {
                    wm.removeView(shadowView);
                }
                shadowView = new View(context);
                shadowView.setBackgroundColor(Color.BLACK);
                WindowManager.LayoutParams params = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.type = WindowManager.LayoutParams.TYPE_PHONE;
                params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                if (shadowView.getParent() == null) {
                    wm.addView(shadowView, params);
                    shadowView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            wake();
                        }
                    });
                }
                break;
            case 2:
                if (isActive(context)) {
                    policyManager.lockNow();
                } else {
                    activeManager(context);
                }
                break;
        }
        if (callBack != null) {
            callBack.onSleep();
        }
    }

    public void wake() {
        // LogUtils.d("will wake");
        switch (type) {
            case 1:
                // Gpio.writeGpio("io3", 1);
            case 0:
                if (shadowView != null && shadowView.getParent() != null) {
                    wm.removeView(shadowView);
                }
                break;
            case 2:
                break;
        }
        if (callBack != null) {
            callBack.onWake();
        }
    }

    private static boolean isAfter0501() {
        String str = Build.DISPLAY;
        String pattern = "[0-9]{8}\\.[0-9]*";

        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        if (m.find()) {
            String temp = m.group();
            String[] strs = temp.split("\\.");
            if (strs.length > 0) {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
                    Date nowVersion = format.parse(strs[0]);
                    Date v = format.parse("20160501");
                    return nowVersion.after(v);
                } catch (ParseException e) {
                    // LogUtils.e(e.toString());
                }
            }
        }
        return false;
    }

    public static boolean canLock() {
        String model = SystemPropertiesUtil.getString("ebanswers.board.model", "");
        String manage = SystemPropertiesUtil.getString("ebanswers.function.powerManage", "");
        boolean isYunOs = YunOs.isYunOS();
        return (model.equals("EC12F-2") && manage.equals("sleep")) || isYunOs;
    }

    private static boolean isActive(Context context) {
        if (policyManager == null) {
            policyManager = (DevicePolicyManager) context.getApplicationContext().getSystemService(Context.DEVICE_POLICY_SERVICE);
        }
        ComponentName componentName = new ComponentName(context,
                SleepAdminReceiver.class);
        return policyManager.isAdminActive(componentName);
    }

    private static void activeManager(Context context) {
        // 使用隐式意图调用系统方法来激活指定的设备管理器
        ComponentName componentName = new ComponentName(context, SleepAdminReceiver.class);
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "锁屏");
        context.startActivity(intent);
    }

    @RequiresPermission(Manifest.permission.WRITE_SETTINGS)
    public static void setSystemOffTime(Context context, int time) {
        try {
            Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,
                    time);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    public void setScreenChangeListener(ScreenChange callBack) {
        ScreenOffManager.callBack = callBack;
    }

    public interface ScreenChange {
        void onWake();

        void onSleep();
    }

}