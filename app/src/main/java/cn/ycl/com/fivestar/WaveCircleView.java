package cn.ycl.com.fivestar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yechenglong on 2017/4/1.
 */

public class WaveCircleView extends View {
//    private String circleColor = "#88dddddd";//圆环颜色
//    private String  waveColor = "#4a90e2";//水波纹颜色
    private int circleColor;//圆环颜色
    private int waveColor;//水波纹颜色
    private int textColor;//字体颜色
    private int textSize;//字体大小
    private final static int DEFAULT_SIZE = 20;//默认字体大小
    private final static int FIRST_LINE_SPEED = 4;//第一条线移动速度
    private final static int SECEND_LINE_SPEED = 5;//第二条线移动速度
    private static final float STRETCH_FACTOR_A = 15;//
    private static final int OFFSET_Y = 0;
    private int firstSpeedPx;//记录第一条线当前点的速度dp值
    private int secendSpeedPx;
    private int mOffset;
    private float[] mOffsets;//记录所有点的(x,y)
    private float[] mFirstOffsets;//第一条线的(x,y)
    private float[] mSecendOffsets;
    private int percent=50;
    private Paint mPaint;//画水波纹线
    private Paint mCirclePaint;//画圆的画笔
    private Paint mTextPaint;//
    private Bitmap mBitMap;
    private Canvas mCanvas;
    private int mWidth,mHeight;//view的宽高

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    private float cycle;//曲线周期
    private Context mContext;
    private float distance;//曲线起伏程度大小
    private int mOneOffset;
    private int mTwoOffset;
    public OnClickListener listener;

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    private PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    public WaveCircleView(Context context) {
        this(context,null);
    }

    public WaveCircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public WaveCircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        //获取自定义属性的值
        TypedArray  a = context.obtainStyledAttributes(attrs,R.styleable.waweCircle);
        circleColor = a.getColor(R.styleable.waweCircle_circleColor,Color.parseColor("#88dddddd"));
        waveColor = a.getColor(R.styleable.waweCircle_waveColor,Color.parseColor("#4a90e2"));
        textColor = a.getColor(R.styleable.waweCircle_textColor,Color.parseColor("#ffffff"));
        textSize = a.getDimensionPixelSize(R.styleable.waweCircle_textSize,dp2px(DEFAULT_SIZE));
        a.recycle();
        //初始化画笔
        firstSpeedPx = dp2px(FIRST_LINE_SPEED);
        secendSpeedPx = dp2px(SECEND_LINE_SPEED);
        mBitMap = Bitmap.createBitmap(500,500, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitMap);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(waveColor);
        //mPaint.setColor(Color.parseColor(waveColor));
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        //mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(circleColor);
        //mCirclePaint.setColor(Color.parseColor(circleColor));
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
    }

    //定义view的大小 为wrap_content设置默认大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        if (widthMode==MeasureSpec.AT_MOST){
            widthSize = dp2px(200);
        }
        if (heightMode == MeasureSpec.AT_MOST){
            heightSize = dp2px(200);
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    /**
     * 第一次进入应用时会调入在onMeasure之后，可以准确获得View的高宽，且之后若不改变View的大小，不会再次调用，可以使用来初始化某些与高宽相关的属性
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth =w;
        mHeight = h;
        mOffsets = new float[mWidth];
        mFirstOffsets = new float[mWidth];
        mSecendOffsets = new float[mWidth];
        cycle = (float) (2*Math.PI/mWidth);
        distance = STRETCH_FACTOR_A;
        setOffset();
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mBitMap.eraseColor(Color.parseColor("#00000000"));
        mCanvas.drawCircle(mWidth/2,mHeight/2,mWidth/2,mCirclePaint);
        mPaint.setXfermode(xfermode);
        resetPositions();
        int y = (int) ((1-percent/100f)*mHeight);
        for (int i=0;i<mWidth;i++){
            mCanvas.drawLine(i,y-mFirstOffsets[i]*distance,i,mHeight,mPaint);
            mCanvas.drawLine(i,y-mSecendOffsets[i]*distance,i,mHeight,mPaint);
        }
        mPaint.setXfermode(null);
        mOneOffset+=firstSpeedPx;
        mTwoOffset+=secendSpeedPx;
        if (mOneOffset>=mWidth){
            mOneOffset = 0;
        }
        if (mTwoOffset>=mWidth){
            mTwoOffset = 0;
        }
        canvas.drawBitmap(mBitMap,0,0,null);
        mTextPaint.setTextSize(textSize);
        canvas.drawText(percent+"%",mWidth/2-textSize/2,mHeight/2+textSize/2,mTextPaint);
    }

    private void resetPositions() {
        int firstOffset = mWidth-mOneOffset;
        System.arraycopy(mOffsets,mOneOffset,mFirstOffsets,0,firstOffset);
        System.arraycopy(mOffsets,0,mFirstOffsets,firstOffset,mOneOffset);
        int secendOffset = mWidth-mTwoOffset;
        System.arraycopy(mOffsets,mTwoOffset,mSecendOffsets,0,secendOffset);
        System.arraycopy(mOffsets,0,mSecendOffsets,secendOffset,mTwoOffset);
    }

    /**
     * 获取所有点的（x，y）坐标
     */
    private void setOffset(){
        for (int i = 0;i<mWidth;i++){
            mOffsets[i] = (float) (Math.sin(i*cycle)+OFFSET_Y);
        }
    }
    private int dp2px(int dp){
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int)(scale*dp+0.5f);
    }
    private int px2dp(int px){
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int)(px/scale+0.5f);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            listener.click();
        }
        return super.onTouchEvent(event);
    }

    public interface OnClickListener{
        void click();
    }
}
