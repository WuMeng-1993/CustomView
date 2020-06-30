package com.wumeng.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * @author WuMeng
 * @date 2020/6/20
 * desc:
 */
public class CustomImageView extends View {

    /**
     * 文本
     */
    private String mTitle;

    /**
     * 文本的颜色
     */
    private int mTextColor;

    /**
     * 文本的大小
     */
    private int mTextSize;

    /**
     * 图片
     */
    private Bitmap mImage;

    /**
     * 图片的放大缩小
     */
    private int imageScale;

    /**
     * View的矩形坐标
     */
    private Rect rect;

    /**
     * 文本的矩形坐标
     */
    private Rect mTextBound;

    /**
     * 画笔
     */
    private Paint mPaint;

    private int mWidth = 0, mHeight = 0;

    public CustomImageView(Context context) {
        this(context, null);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    /**
     * 获取自定义属性
     */
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CustomImageView);
        mTitle = a.getString(R.styleable.CustomImageView_titleText1);
        mTextColor = a.getColor(R.styleable.CustomImageView_titleTextColor1, Color.BLACK);
        mTextSize = a.getDimensionPixelSize(R.styleable.CustomImageView_titleTextSize1, 16);
        mImage = BitmapFactory.decodeResource(getResources(), a.getResourceId(R.styleable.CustomImageView_image1, 0));
        imageScale = a.getInt(R.styleable.CustomImageView_imageScaleType1, 0);
        a.recycle();

        rect = new Rect();
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mTextBound = new Rect();
        mPaint.getTextBounds(mTitle, 0, mTitle.length(), mTextBound);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        // 设置宽度
        if (specMode == MeasureSpec.EXACTLY) {
            mWidth = specSize;
        } else {
            // 图片的宽度
            int desireByImg = getPaddingLeft() + mImage.getWidth() + getPaddingRight();
            // 字体的宽
            int desireByTitle = getPaddingLeft() + mTextBound.width() + getPaddingRight();

            if (specMode == MeasureSpec.AT_MOST) {
                int desire = Math.max(desireByImg, desireByTitle);
                mWidth = Math.min(desire, specSize);
            }
        }

        // 设置高度
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            mHeight = specSize;
        } else {
            int desire = getPaddingTop() + getPaddingBottom() + mImage.getHeight() + mTextBound.height();
            if (specMode == MeasureSpec.AT_MOST) {
                mHeight = Math.min(desire, specSize);
            }
        }

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制边框
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.CYAN);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);

        rect.left = getPaddingLeft();
        rect.top = getPaddingTop();
        rect.right = mWidth - getPaddingRight();
        rect.bottom = mHeight - getPaddingBottom();

        mPaint.setColor(mTextColor);
        mPaint.setStyle(Paint.Style.FILL);

        if (mTextBound.width() > mWidth) {
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mTitle,paint,mWidth - getPaddingLeft() - getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg,getPaddingLeft(),mHeight - getPaddingBottom(),mPaint);
        } else {
            canvas.drawText(mTitle,mWidth / 2 - mTextBound.width() / 2,mHeight - getPaddingBottom(),mPaint);
        }

        rect.bottom = rect.bottom - mTextBound.height();

        if (imageScale == 0) {
            canvas.drawBitmap(mImage,null,rect,mPaint);
        } else {
            rect.left = mWidth / 2 - mImage.getWidth() / 2;
            rect.top = (mHeight - mTextBound.height()) / 2 - mImage.getHeight() / 2;
            rect.right = mWidth / 2 + mImage.getWidth() / 2;
            rect.bottom = (mHeight - mTextBound.height()) / 2 + mImage.getHeight() / 2;
        }

        canvas.drawBitmap(mImage,null,rect,mPaint);
    }
}
