package cn.ycl.com.fivestar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by yechenglong on 2017/4/1.
 */

public class CircleWaveView extends View{
    private Paint mPaint;
    private Paint mTextPaint;
    private Paint mSrcPaint;
    private int mSRCColor;
    private Canvas mCanvas;
    private int percent =50;
    private Bitmap mBitmap;
    private int mWidth;
    private int mHeight;
    private int x,y;
    private int circleColor;
    private int waveColor;
    private int textColor;
    private Path mPath;
    private boolean isLeft;
    private PorterDuffXfermode mMode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    private PorterDuffXfermode mode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
    private static final float STRETCH_FACTOR_A = 20;
    private static final int OFFSET_Y = 0;
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 7;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 5;
    private float[] mYPositions;
    private float[] mOneYPositions;
    private float[] mTwoPositions;
    private int mOneXSpeed;
    private int mTwoXSpeed;
    private int mOneOffset;
    private int mTotalWidth, mTotalHeight;
    private int mTwoOffset;
    private float mCycleFactorW;
    private int yHeight=400;
    private Paint paint;
    public CircleWaveView(Context context) {
        this(context,null);
    }

    public CircleWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //mPaint.setColor(circleColor);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        //mTextPaint.setColor(textColor);
        mPaint.setColor(Color.parseColor("#8800ff66"));
        mSrcPaint = new Paint();
        mSrcPaint.setAntiAlias(true);
        //mSrcPaint.setColor(mSRCColor);
        mSrcPaint.setColor(Color.parseColor("#88dddddd"));
        mBitmap = Bitmap.createBitmap(500,500, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
        mPath = new Path();
        mOneXSpeed = dpToPx(context,TRANSLATE_X_SPEED_ONE);
//        mTwoXSpeed = dpToPx(context,TRANSLATE_X_SPEED_TWO);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        }


        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        }
        mYPositions = new float[mWidth];
        mOneYPositions = new float[mWidth];
        mTwoPositions = new float[mWidth];
        mCycleFactorW = (float)(4*Math.PI/mWidth);
        for (int i=0;i<mWidth;i++){
            mYPositions[i] = (float) (10 * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
        y = mHeight;
        Log.i("sssssssssss",""+y);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        y = (int) ((1-percent/100f)*mHeight);
        mBitmap.eraseColor(Color.parseColor("#00000000"));
        mCanvas.drawCircle(mWidth/2,mHeight/2,mWidth/2,mSrcPaint);

        mPaint.setXfermode(mMode);
        resetPositonY();
        for (int i = 0; i < mWidth; i++) {
            //mCanvas.drawLine(i, y - mOneYPositions[i], i, mHeight, mPaint);
            mCanvas.drawLine(i, y - mOneYPositions[i], i, mHeight, mPaint);
        }
        mOneOffset += mOneXSpeed;
        // 如果已经移动到结尾处，则重头记录
        if (mOneOffset >= mWidth) {
            mOneOffset = 0;
        }
//        if (mTwoOffset > mWidth) {
//            mTwoOffset = 0;
//        }
//        mCanvas.drawPath(mPath,mPaint);
        mPaint.setXfermode(null);
        canvas.drawBitmap(mBitmap,0,0,null);

//        String str = percent+"";
//
//        mTextPaint.setTextSize(80);
//        float txtLength = mTextPaint.measureText(str);
//
//        canvas.drawText(str, mWidth / 2 - txtLength / 2, mHeight / 2+15, mTextPaint);
//
//        mTextPaint.setTextSize(40);
        postInvalidateDelayed(30);
    }
    public void resetPositonY() {
        int yOneInterval = mYPositions.length - mOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        Log.i("ssssssss",""+mOneOffset);
        System.arraycopy(mYPositions, mOneOffset, mOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mOneYPositions, yOneInterval, mOneOffset);
//        int yTwoInterval = mYPositions.length - mTwoOffset;
//        System.arraycopy(mYPositions, mTwoOffset, mTwoPositions, 0,
//                yTwoInterval);
//        System.arraycopy(mYPositions, 0, mTwoPositions, yTwoInterval, mTwoOffset);
    }
    public int dpToPx(Context context, int dpSize){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpSize*scale+0.5f);
    }
}
