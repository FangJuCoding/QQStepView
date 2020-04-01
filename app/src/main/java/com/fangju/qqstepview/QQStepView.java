package com.fangju.qqstepview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 仿QQ计步器的自定义View
 * Created by FangJu on 2020/4/1.
 */
public class QQStepView extends View {
    // 外圆颜色
    private int mOuterCircleColor;
    // 内圆颜色
    private int mInnerCircleColor;
    // 步数字体颜色
    private int mStepTextColor;
    // 步数字体大小
    private int mStepTextSize;
    // 圆的宽度
    private int mCircleBorderWidth;

    private Paint mOuterCirclePaint;
    private Paint mInnerCirclePaint;
    private Paint mTextPaint;

    // 最大步数
    private int mMaxStepNumber = 0;
    // 当前步数
    private int mCurrentStepNumber = 0;

    public QQStepView(Context context) {
        this(context, null);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QQStepView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 获取自定义属性的值
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.QQStepView);
        mOuterCircleColor = a.getColor(R.styleable.QQStepView_outerCircleColor, Color.BLUE);
        mInnerCircleColor = a.getColor(R.styleable.QQStepView_innerCircleColor, Color.RED);
        mStepTextColor = a.getColor(R.styleable.QQStepView_stepTextColor, Color.RED);
        mStepTextSize = a.getDimensionPixelSize(R.styleable.QQStepView_stepTextSize, 18);
        mCircleBorderWidth = a.getDimensionPixelSize(R.styleable.QQStepView_circleBorderWidth, 3);
        a.recycle();

        // 初始化画笔
        mOuterCirclePaint = new Paint();
        mOuterCirclePaint.setAntiAlias(true);
        mOuterCirclePaint.setColor(mOuterCircleColor);
        mOuterCirclePaint.setStrokeWidth(mCircleBorderWidth);
        mOuterCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mOuterCirclePaint.setStyle(Paint.Style.STROKE);

        mInnerCirclePaint = new Paint();
        mInnerCirclePaint.setAntiAlias(true);
        mInnerCirclePaint.setColor(mInnerCircleColor);
        mInnerCirclePaint.setStrokeWidth(mCircleBorderWidth);
        mInnerCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mInnerCirclePaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setColor(mStepTextColor);
        mTextPaint.setTextSize(mStepTextSize);
        mTextPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.AT_MOST) {
            throw new RuntimeException("必须指定宽度");
        }

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int minNum = Math.min(width, height);
        setMeasuredDimension(minNum, minNum);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        // 画外圆
        RectF rectF = new RectF(mCircleBorderWidth / 2, mCircleBorderWidth / 2, 2 * center - mCircleBorderWidth / 2, 2 * center - mCircleBorderWidth / 2);
        canvas.drawArc(rectF, 135, 270, false, mOuterCirclePaint);

        // 画内圆
        if (mMaxStepNumber == 0 || mCurrentStepNumber > mMaxStepNumber)
            return;
        int sweepAngle = (int) ((mCurrentStepNumber * 1.0 / mMaxStepNumber) * 270);
        canvas.drawArc(rectF, 135, sweepAngle, false, mInnerCirclePaint);

        // 画文字
        String stepStr = String.valueOf(mCurrentStepNumber);
        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(stepStr, 0, stepStr.length(), textBounds);
        int dx = getWidth() / 2 - textBounds.width() / 2;

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        int dy = (int) ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
        int baseLine = getHeight() / 2 - dy;
        canvas.drawText(stepStr, dx, baseLine, mTextPaint);
    }

    /**
     * 设置最大步数
     */
    public void setStepMax(int stepMax) {
        this.mMaxStepNumber = stepMax;
    }

    /**
     * 设置当前的步数
     */
    public void setCurrentStep(int stepCurrent) {
        if (stepCurrent > mMaxStepNumber)
            return;
        this.mCurrentStepNumber = stepCurrent;
        invalidate();
    }
}
