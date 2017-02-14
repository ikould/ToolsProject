package com.frame.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 屏幕尺寸相关工具类
 */
public class ScreenUtils {

    private static boolean isInit;
    private static int screenWidth;
    private static int screenHeight;

    public static void initScreenParams(Context context) {
        if (!isInit) {
            isInit = true;
            WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();// 创建了一张白纸
            windowManager.getDefaultDisplay().getMetrics(dm);// 给白纸设置宽高
            screenWidth = dm.widthPixels;
            screenHeight = dm.heightPixels;
        }
    }

    /**
     * 获取屏幕的宽度px
     *
     * @param context 上下文
     * @return 屏幕宽px
     */
    public static int getScreenWidth(Context context) {
        initScreenParams(context);
        return screenWidth;
    }

    /**
     * 获取屏幕的高度px
     *
     * @param context 上下文
     * @return 屏幕高px
     */
    public static int getScreenHeight(Context context) {
        initScreenParams(context);
        return screenHeight;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    public static int px2dip(Resources resources, float inParam) {
        float f = resources.getDisplayMetrics().density;
        return (int) (inParam / f + 0.5F);
    }

    public static int dip2px(Resources resources, float inParam) {
        float f = resources.getDisplayMetrics().density;
        return (int) (inParam * f + 0.5F);
    }

    public static int px2sp(Resources resources, float inParam) {
        float f = resources.getDisplayMetrics().scaledDensity;
        return (int) (inParam / f + 0.5F);
    }

    public static int sp2px(Resources resources, float inParam) {
        float f = resources.getDisplayMetrics().scaledDensity;
        return (int) (inParam * f + 0.5F);
    }
}
