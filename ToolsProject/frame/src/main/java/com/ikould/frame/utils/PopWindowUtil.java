package com.ikould.frame.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import java.util.HashMap;
import java.util.Map;

/**
 * describe
 * Created by liudong on 2016/12/19.
 */
public class PopWindowUtil {
    //标志位
    public static final String ALARM = "ALARM";

    private Map<String, View> viewMap;
    private WindowManager mWindowManager = null;
    private WindowManager.LayoutParams params;

    private static PopWindowUtil instance;

    public static PopWindowUtil getIntsance() {
        if (instance == null) {
            instance = new PopWindowUtil();
        }
        return instance;
    }

    private PopWindowUtil() {
        initParams();
    }


    /**
     * 显示弹出框
     *
     * @param context
     */
    public void showPopupWindow(Context context, View view, String tag) {
        if (viewMap == null) {
            viewMap = new HashMap<>();
        }
        if (!viewMap.containsKey(tag)) {
            viewMap.put(tag, view);
        }
        // 获取WindowManager
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.addView(viewMap.get(tag), params);
    }

    /**
     * 初始化LayoutParams
     */
    private void initParams() {
        params = new WindowManager.LayoutParams();
        // 类型
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        // 设置flag
        params.flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;//窗口占满整个屏幕，忽略周围的装饰边框（例如状态栏）。此窗口需考虑到装饰边框的内容。
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.CENTER;
    }

    /**
     * 隐藏弹出框
     */
    public void hidePopupWindow(String tag) {
        if (viewMap.get(tag) != null) {
            mWindowManager.removeView(viewMap.get(tag));
            viewMap.remove(tag);
        }
    }

    public static void hideStatusBar(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.hideNaviBar");
        intent.putExtra("hide", true);
        context.sendBroadcast(intent);
    }

    public static void showStatusBar(Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.hideNaviBar");
        intent.putExtra("hide", false);
        context.sendBroadcast(intent);
    }
}
