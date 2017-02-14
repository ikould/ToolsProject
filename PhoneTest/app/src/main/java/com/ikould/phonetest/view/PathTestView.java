package com.ikould.phonetest.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * describe
 * Created by liudong on 2017/1/5.
 */

public class PathTestView extends View {

    private Path mPath;

    private Point[] mPoints;
    private Paint mPaint;
    private int pointInd;

    public PathTestView(Context context) {
        super(context);
        init();
    }

    public PathTestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PathTestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#333333"));
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3);

        mPoints = new Point[]{new Point(0, 0), new Point(200, 0), new Point(200, 200), new Point(300, 300), new Point(0, 300)};
        mPath = new Path();

        initAnim();
    }

    private ValueAnimator valueAnimator;
    private int currentIndex = -1;

    private void initAnim() {
        valueAnimator = ValueAnimator.ofFloat(5, 0);
        valueAnimator.setDuration(5000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(animation -> {
            float value = (float) valueAnimator.getAnimatedValue();
            int index = mPoints.length - (int) value;
            Log.d("PathTestView", "initAnim: value = " + value);
            if (currentIndex != index) {
                Log.d("PathTestView", "initAnim: currentIndex = " + currentIndex);
                if (index == mPoints.length) {
                    mPath.lineTo(mPoints[0].x, mPoints[0].y);
                } else {
                    mPath.lineTo(mPoints[index].x, mPoints[index].y);
                }
                currentIndex = index;
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
    }

    public void startAnim() {
        valueAnimator.start();
    }
}
