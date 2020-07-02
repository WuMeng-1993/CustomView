package com.wumeng.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @Date: 2020/7/1
 * @Author: Wu Meng
 * @Desc:
 */
public class CustomProgressBar extends View {

    /**
     * 第一种颜色
     */
    private int mFirstColor;

    /**
     * 第二种颜色
     */
    private int mSecondColor;

    /**
     * 圆环的宽度
     */
    private int mCircleWidth;

    /**
     * 速度
     */
    private int mSpeed;

    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 当前进度
     */
    private int mProgress;

    /**
     * 是否开启下一个
     */
    private boolean isNext = false;

    public CustomProgressBar(Context context) {
        this(context, null);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomProgressBar);
        mFirstColor = a.getColor(R.styleable.CustomProgressBar_firstColor, Color.WHITE);
        mSecondColor = a.getColor(R.styleable.CustomProgressBar_secondColor, Color.WHITE);
        mCircleWidth = a.getDimensionPixelOffset(R.styleable.CustomProgressBar_circleWidth, 20);
        mSpeed = a.getInteger(R.styleable.CustomProgressBar_speed, 20);
        a.recycle();

        mPaint = new Paint();

        new Thread() {
            @Override
            public void run() {
                super.run();
                while (true) {
                    mProgress++;
                    if (mProgress == 360) {
                        mProgress = 0;
                        if (!isNext) {
                            isNext = true;
                        } else {
                            isNext = false;
                        }
                    }
                    postInvalidate();
                    try {
                        Thread.sleep(mSpeed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 圆心的X坐标
        int center = getWidth() / 2;
        // 半径(这里要注意，半径的取值，因为画边框的时候是向两边拓展)
        int radius = center - mCircleWidth / 2;
        // 线宽
        mPaint.setStrokeWidth(mCircleWidth);
        // 抗锯齿
        mPaint.setAntiAlias(true);
        // 不填充
        mPaint.setStyle(Paint.Style.STROKE);

        RectF oval = new RectF(center - radius, center - radius, center + radius, center + radius);
        if (!isNext) {
            mPaint.setColor(mFirstColor);
            canvas.drawCircle(center, center, radius, mPaint);
            mPaint.setColor(mSecondColor);
            canvas.drawArc(oval, -90, mProgress, false, mPaint);
        } else {
            mPaint.setColor(mSecondColor);
            canvas.drawCircle(center,center,radius,mPaint);
            mPaint.setColor(mFirstColor);
            canvas.drawArc(oval,-90,mProgress,false,mPaint);
        }
    }

}
