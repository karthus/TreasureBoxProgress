package com.jkarthus.treasure.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * 自定义宝箱进度UI
 * 
 * @author shihuajian
 * @since 2017/6/19 14:46
 */
public class TreasureBoxProgressView extends View {

    private Context mContext;

    // 默认Padding值
    private final static int defaultPadding = 20;
    // view宽度
    private int width;

    // view高度
    private int height;


    //  圆环起始角度
    private final static float mStartAngle = 135f;

    // 圆环结束角度
    private final static float mEndAngle = 270f;

    //外层圆环画笔
    private Paint mMiddleArcPaint;
    
    // 底部描述文本画笔
    private Paint mProgressDescribePaint;


    //进度圆环画笔
    private Paint mArcProgressPaint;

    //半径
    private int radius;

    //外层矩形
    private RectF mMiddleRect;


    //进度矩形
    private RectF mMiddleProgressRect;

    // 最小数字
    private int mMinNum = 0;

    // 最大数字
    private int mMaxNum = 40;

    // 当前进度
    private float mCurrentAngle = 0f;

    //总进度
    private float mTotalAngle = 290f;
    
    // 底部进度等级
    private String sesameLevel;

    // 中心宝箱图片
    private Bitmap semicircleCenterBitmap;
    
    /** 进度条上的光点 */
    private Bitmap semicircleLightBitmap;
    
    /** 半圆满了的图片 */
    private Bitmap semicircleFullBitmap;

    //当前点的实际位置
    private float[] pos;

    //当前点的tangent值
    private float[] tan;

    //矩阵
    private Matrix matrix;

    //小圆点画笔
    private Paint mBitmapPaint;
    private int treasureProgressSize;
    private int treasureProgressLineSize;
    private int treasureProgressBackgroundLineColor;
    private int treasureProgressFrontLineColor;
    private int maxWidth;
    private int maxHeight;
    private int treasureProgressCenterBitmapResId;   // 中心的图
    private int treasureProgressLightBitmapResId;   // 进度条的光点
    private int treasureProgressDescribeSize; // 底部进度描述字体大小
    private int treasureProgressDescribeColor; // 底部进度描述字体颜色
    private boolean isFull = false; // 宝箱是否满了

    public TreasureBoxProgressView(Context context) {
        this(context, null);
    }


    public TreasureBoxProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public TreasureBoxProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(attrs);
    }


    /**
     * 初始化
     */
    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TreasureBoxProgressView);
        treasureProgressSize = typedArray.getDimensionPixelSize(R.styleable.TreasureBoxProgressView_treasureProgressSize, dp2px(100));
        treasureProgressLineSize = typedArray.getDimensionPixelSize(R.styleable.TreasureBoxProgressView_treasureProgressLineSize, dp2px(3));
        treasureProgressBackgroundLineColor = typedArray.getColor(R.styleable.TreasureBoxProgressView_treasureProgressBackgroundLineColor, getResources().getColor(R.color.colorSemicircleDefaultBackgroundLine));
        treasureProgressFrontLineColor = typedArray.getColor(R.styleable.TreasureBoxProgressView_treasureProgressFrontLineColor, getResources().getColor(android.R.color.holo_orange_dark));
        treasureProgressCenterBitmapResId = typedArray.getResourceId(R.styleable.TreasureBoxProgressView_treasureProgressCenterBitmap, R.mipmap.ic_treasure_box_default);
        treasureProgressLightBitmapResId = typedArray.getResourceId(R.styleable.TreasureBoxProgressView_treasureProgressLightBitmap, R.mipmap.ic_treasure_box_light);
        sesameLevel = typedArray.getString(R.styleable.TreasureBoxProgressView_treasureProgressDescribeText);
        treasureProgressDescribeSize = typedArray.getDimensionPixelSize(R.styleable.TreasureBoxProgressView_treasureProgressDescribeSize, sp2px(12));
        treasureProgressDescribeColor = typedArray.getColor(R.styleable.TreasureBoxProgressView_treasureProgressDescribeColor, getResources().getColor(android.R.color.black));
        
        if (TextUtils.isEmpty(sesameLevel)) {
            sesameLevel = "";
        }

        //外层圆环画笔
        mMiddleArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMiddleArcPaint.setStrokeWidth(treasureProgressLineSize);
        mMiddleArcPaint.setColor(treasureProgressBackgroundLineColor);
        mMiddleArcPaint.setStyle(Paint.Style.STROKE);
        mMiddleArcPaint.setStrokeCap(Paint.Cap.ROUND);

        // 底部描述画笔
        mProgressDescribePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressDescribePaint.setColor(treasureProgressDescribeColor);
        mProgressDescribePaint.setTextAlign(Paint.Align.CENTER);

        //外层进度画笔
        mArcProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcProgressPaint.setStrokeWidth(treasureProgressLineSize);
        mArcProgressPaint.setColor(treasureProgressFrontLineColor);
        mArcProgressPaint.setStyle(Paint.Style.STROKE);
        mArcProgressPaint.setStrokeCap(Paint.Cap.ROUND);

        mBitmapPaint = new Paint();
        mBitmapPaint.setStyle(Paint.Style.FILL);
        mBitmapPaint.setAntiAlias(true);

        //初始化小圆点图片
        semicircleCenterBitmap = BitmapFactory.decodeResource(getResources(), treasureProgressCenterBitmapResId);
        semicircleLightBitmap = BitmapFactory.decodeResource(getResources(), treasureProgressLightBitmapResId);
        semicircleFullBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_treasure_box_full);
        pos = new float[2];
        tan = new float[2];
        matrix = new Matrix();
    }

    public String getSesameLevel() {
        return sesameLevel;
    }

    public void setSesameLevel(String sesameLevel) {
        this.sesameLevel = sesameLevel;
    }

    public int getTreasureProgressCenterBitmapResId() {
        return treasureProgressCenterBitmapResId;
    }

    public void setTreasureProgressCenterBitmapResId(int treasureProgressCenterBitmapResId) {
        this.treasureProgressCenterBitmapResId = treasureProgressCenterBitmapResId;
        semicircleCenterBitmap = BitmapFactory.decodeResource(getResources(), treasureProgressCenterBitmapResId);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();
        int computedWidth = resolveMeasured(widthMeasureSpec, minimumWidth);
        int computedHeight = resolveMeasured(heightMeasureSpec, minimumHeight);

        setMeasuredDimension(computedWidth, computedHeight);
    }

    private int resolveMeasured(int measureSpec, int desired) {
        int result = 0;
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (MeasureSpec.getMode(measureSpec)) {
            case MeasureSpec.UNSPECIFIED:
                result = desired;
                break;
            case MeasureSpec.AT_MOST:
                result = Math.min(specSize, desired);
                break;
            case MeasureSpec.EXACTLY:
            default:
                result = specSize;
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        maxWidth = w;
        maxHeight = h;
        width = treasureProgressSize;
        radius = width / 2;


        mMiddleRect = new RectF((maxWidth / 2) - radius, (maxHeight / 2) - radius, (maxWidth / 2)
                + radius, (maxHeight / 2) + radius);

        mMiddleProgressRect = new RectF((maxWidth / 2) - radius, (maxHeight / 2) - radius, (maxWidth / 2)
                + radius, (maxHeight / 2) + radius);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawCenterBitmap(canvas);
        drawBottomText(canvas);
        if (isFull) {
            drawRingProgressBitmap(canvas);
        } else {
            drawMiddleArc(canvas);
            drawRingProgress(canvas);
        }
    }


    /**
     * 绘制外层圆环进度和小圆点
     */
    private void drawRingProgress(Canvas canvas) {
        Path path = new Path();
        path.addArc(mMiddleProgressRect, mStartAngle, mCurrentAngle);
        PathMeasure pathMeasure = new PathMeasure(path, false);
        pathMeasure.getPosTan(pathMeasure.getLength() * 1, pos, tan);
//        matrix.postTranslate(pos[0] - bitmap.getWidth() / 2, pos[1] - bitmap.getHeight() / 2);
        canvas.drawPath(path, mArcProgressPaint);
        
        matrix.reset();
        matrix.postTranslate(pos[0] - semicircleLightBitmap.getWidth() / 2, pos[1] - semicircleLightBitmap.getHeight() / 2);   // 将图片绘制中心调整到与当前点重合
        canvas.drawBitmap(semicircleLightBitmap, matrix, new Paint());
    }

    private void drawCenterBitmap(Canvas canvas) {
        // 将画布坐标系移动到画布中央
//        canvas.translate(maxWidth / 2 - bitmap.getWidth() / 2, maxHeight / 2 - bitmap.getHeight() / 2);
        int width = semicircleCenterBitmap.getWidth();
        int height = semicircleCenterBitmap.getHeight();
        // 设置想要的大小
        int newWidth = treasureProgressSize;
        int newHeight = treasureProgressSize;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        matrix.reset();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        semicircleCenterBitmap = semicircleCenterBitmap.createBitmap(semicircleCenterBitmap, 0, 0, width, height, matrix, true);
        // 绘制图片
        canvas.drawBitmap(semicircleCenterBitmap, maxWidth / 2 - semicircleCenterBitmap.getWidth() / 2,
                ((maxHeight / 2) - radius), 
                new Paint());
    }

    /**
     * 绘制底部文字
     * @param canvas
     */
    private void drawBottomText(Canvas canvas) {
        mProgressDescribePaint.setTextSize(treasureProgressDescribeSize);
        canvas.drawText(sesameLevel, (maxWidth / 2), radius + (maxHeight / 2) + 20, mProgressDescribePaint);
    }

    /**
     * 绘制外层圆环
     */
    private void drawMiddleArc(Canvas canvas) {
        canvas.drawArc(mMiddleRect, mStartAngle, mEndAngle, false, mMiddleArcPaint);
    }

    /**
     * 
     * @param canvas
     */
    private void drawRingProgressBitmap(Canvas canvas) {
        int width = semicircleFullBitmap.getWidth();
        int height = semicircleFullBitmap.getHeight();
        // 设置想要的大小
        int newWidth = treasureProgressSize + 24;
        int newHeight = treasureProgressSize + 24;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width ;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        matrix.reset();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        semicircleFullBitmap = semicircleFullBitmap.createBitmap(semicircleFullBitmap, 0, 0, width, height, matrix, true);
//        // 绘制图片
        canvas.drawBitmap(semicircleFullBitmap, (maxWidth / 2) - (semicircleFullBitmap.getWidth() / 2 + 6),
                ((maxHeight / 2) - (semicircleFullBitmap.getHeight() / 2 + 10)),
                new Paint());
    }

    public void setSesameValues(int values, int total) {
        if (values >= 0) {
            mMaxNum = values;
            if (values >= total) {
                isFull = true;
            } else {
                isFull = false;
            }
            // 设置底部进度数据
            if (values <= 0) {
                treasureProgressBackgroundLineColor = getResources().getColor(R.color.colorSemicircleDefaultBackgroundLine);
            } else if (values > 0 && values < total) {
                treasureProgressBackgroundLineColor = getResources().getColor(R.color.colorBlack);
            } else if (values >= total) {

            }
            mMiddleArcPaint.setColor(treasureProgressBackgroundLineColor);
            mTotalAngle = ((float) values / (float) total) * mEndAngle;
            startAnim();
        }
    }


    public void startAnim() {
        ValueAnimator mAngleAnim = ValueAnimator.ofFloat(mCurrentAngle, mTotalAngle);
        mAngleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
        mAngleAnim.setDuration(3000);
        mAngleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurrentAngle = (float) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        mAngleAnim.start();

        // mMinNum = 350;
        ValueAnimator mNumAnim = ValueAnimator.ofInt(mMinNum, mMaxNum);
        mNumAnim.setDuration(3000);
        mNumAnim.setInterpolator(new LinearInterpolator());
        mNumAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                mMinNum = (int) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        mNumAnim.start();
    }


    /**
     * dp2px
     */
    public int dp2px(int values) {

        float density = getResources().getDisplayMetrics().density;
        return (int) (values * density + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }


}
