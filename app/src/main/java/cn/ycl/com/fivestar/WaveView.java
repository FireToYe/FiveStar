package cn.ycl.com.fivestar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yechenglong on 2017/3/31.
 */

public class WaveView extends View{
    private int oneColor =0x880000aa;
    private int twoColor;
    private Paint paint;
    private PaintFlagsDrawFilter filter;
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
    public WaveView(Context context) {
        this(context,null);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.waveView);
//        oneColor = a.getColor(R.styleable.waveView_oneColor,0x880000aa);
//        twoColor = a.getColor(R.styleable.waveView_twoColor,0x880000aa);
//        a.recycle();
        mOneXSpeed = dpToPx(context,TRANSLATE_X_SPEED_ONE);
        mTwoXSpeed = dpToPx(context,TRANSLATE_X_SPEED_TWO);
        paint = new Paint();
        paint.setColor(oneColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        filter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.setDrawFilter(filter);
        resetPositonY();
        for (int i = 0; i < mTotalWidth; i++) {
            canvas.drawLine(i, mTotalHeight - mOneYPositions[i] - yHeight, i, mTotalHeight, paint);
            canvas.drawLine(i, mTotalHeight - mTwoPositions[i] - yHeight, i, mTotalHeight, paint);
        }
            mOneOffset += mOneXSpeed;
            mTwoOffset += mTwoXSpeed;
            // 如果已经移动到结尾处，则重头记录
            if (mOneOffset >= mTotalWidth) {
                mOneOffset = 0;
            }
            if (mTwoOffset > mTotalWidth) {
                mTwoOffset = 0;
            }
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight =h;
        mYPositions = new float[mTotalWidth];
        mOneYPositions = new float[mTotalWidth];
        mTwoPositions = new float[mTotalWidth];
        mCycleFactorW = (float)(2*Math.PI/mTotalWidth);
        for (int i=0;i<mTotalWidth;i++){
            mYPositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
    }

    public void resetPositonY() {
        int yOneInterval = mYPositions.length - mOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mOneOffset, mOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mOneYPositions, yOneInterval, mOneOffset);
        int yTwoInterval = mYPositions.length - mTwoOffset;
        System.arraycopy(mYPositions, mTwoOffset, mTwoPositions, 0,
                yTwoInterval);
        System.arraycopy(mYPositions, 0, mTwoPositions, yTwoInterval, mTwoOffset);
    }
        public int dpToPx(Context context, int dpSize){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpSize*scale+0.5f);
    }
}
