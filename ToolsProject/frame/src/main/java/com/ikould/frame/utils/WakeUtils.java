package com.ikould.frame.utils;

import android.content.Context;
import android.os.PowerManager;
import android.provider.Settings;

/**
 * 休眠 工具
 * <p>
 * 1.可设置屏幕休眠和唤醒
 * 2.可设置是否进入深度休眠
 * <p>
 * Created by liudong on 2016/8/29.
 */
public class WakeUtils {
    private PowerManager.WakeLock mScreenWakeLock;
    private PowerManager.WakeLock mCpuWakeLock;
    public static WakeUtils instance;
    private PowerManager powerManager;
    private Context mContext;

    public static WakeUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (WakeUtils.class) {
                instance = new WakeUtils(context.getApplicationContext());
            }
        }
        return instance;
    }

    private WakeUtils(Context context) {
        mContext = context;
        powerManager = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
        mScreenWakeLock = powerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, this.getClass().getCanonicalName());
        mCpuWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, this.getClass().getCanonicalName());
    }

    public void wakeLock() {
        synchronized (WakeUtils.class) {
            if (!mScreenWakeLock.isHeld()) {
                mScreenWakeLock.acquire();
            }
        }
    }

    public void wakeRelease() {
        synchronized (WakeUtils.class) {
            if (mScreenWakeLock.isHeld()) {
                mScreenWakeLock.release();
            }
        }
    }

    /**
     * 保持Cpu运行
     */
    public void lockCpuRunning() {
        synchronized (WakeUtils.class) {
            if (!mCpuWakeLock.isHeld()) {
                mCpuWakeLock.acquire();
            }
        }
    }

    public void releaseCpuRunning() {
        synchronized (WakeUtils.class) {
            if (mCpuWakeLock.isHeld()) {
                mCpuWakeLock.release();
            }
        }
    }


    /**
     * 设置无操作几分钟后进入屏保
     *
     * @param paramInt 时间，以毫秒计
     */
    public void setScreenOffTime(int paramInt) {
        try {
            Settings.System.putInt(mContext.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,
                    paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 获取当前屏保设置的时间
     * 没有设置即为-1
     *
     * @return
     */
    public long getScreenOffTime() {
        return Settings.System.getLong(mContext.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, -1);
    }

    public void realseInstance(){
        instance = null;
    }

}
