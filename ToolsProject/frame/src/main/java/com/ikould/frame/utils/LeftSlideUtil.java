package com.ikould.frame.utils;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * 给View添加左滑
 * Created by liudong on 2017/1/5.
 */

public class LeftSlideUtil {
    private View view;
    private float width;

    private boolean isUseful;
    private float pointX;
    private float pointY;
    private final int offsetSize = 50;
    private final float speed = 1.5f;
    private final int slideSpeed = 1000;
    private boolean isOver;

    private float currentX;
    private float startX;
    private float endX;

    private ValueAnimator valueAnimator;

    public LeftSlideUtil(View view, float width) {
        this.view = view;
        this.width = width;
        initListener();
    }

    private void initListener() {
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pointX = event.getRawX();
                        pointY = event.getRawY();
                        Log.d("LeftSlideUtil", "initListener: ACTION_DOWN");
                        isOver = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (!isUseful && (Math.abs(event.getRawX() - pointX) > offsetSize || Math.abs(event.getRawY() - pointY) > offsetSize)) {
                            isUseful = true;
                        }
                        if (isUseful) {
                            float difference = (event.getRawX() - pointX - offsetSize) * speed;
                            if (difference > 0) {
                                if (difference > width)
                                    difference = width;
                                view.setTranslationX(difference);
                            }
                            Log.d("LeftSlideUtil", "initListener: difference = " + difference);
                            if (difference > width / 2 || isSpeedOver(event)) {
                                isOver = true;
                            } else {
                                isOver = false;
                            }
                            currentX = difference;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        startX = currentX;
                        if (isOver) {
                            endX = width;
                        } else {
                            endX = 0;
                        }
                        doSmoothAnim();
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 滑动速度是否达标
     *
     * @return
     */
    private boolean isSpeedOver(MotionEvent event) {
        //创建对象
        VelocityTracker velocityTracker = VelocityTracker.obtain();
        //添加事件
        velocityTracker.addMovement(event);
        //设置计算时间
        velocityTracker.computeCurrentVelocity(1000);
        //获得轴向速度
        int xVelocity = (int) velocityTracker.getXVelocity();
        Log.d("Fragment4", "doAnim: xVelocity = " + xVelocity);
        //重置并回收
        velocityTracker.clear();
        velocityTracker.recycle();
        return Math.abs(xVelocity) > slideSpeed;
    }

    /**
     * 滑动
     */
    private void doSmoothAnim() {
        if (valueAnimator == null) {
            valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setInterpolator(new AccelerateInterpolator());//加速运动
            valueAnimator.setDuration(600);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) valueAnimator.getAnimatedValue();
                    float result = (endX - startX) * value + startX;
                    if (view != null) {
                        view.setTranslationX(result);
                    }
                    if (result == width && onSlideListener != null) {
                        onSlideListener.onSlideFinish();
                    }
                }
            });
        }
        valueAnimator.cancel();
        valueAnimator.start();
    }

    //接口
    private OnSlideListener onSlideListener;

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        this.onSlideListener = onSlideListener;
    }

    public interface OnSlideListener {
        void onSlideFinish();
    }

    /**
     * 销毁，释放
     */
    public void destroySlideUtil() {
        view = null;
        onSlideListener = null;
    }
}
