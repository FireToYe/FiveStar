package cn.ycl.com.fivestar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yechenglong on 2017/3/29.
 */

public class FiveStarView extends View{
    private Drawable emptyDraw;
    private Bitmap fillDraw;
    private int starCount;
    private int starDistance;
    private int starSize;
    private Paint paint;

    public float getStarMark() {
        return starMark;
    }

    private float starMark = 0.0f;
    private boolean intMarkFlag;
    private OnstarChangeListenner listenner;

    public void setListenner(OnstarChangeListenner listenner) {
        this.listenner = listenner;
    }

    public void setStarMark(float starMark) {
        if (intMarkFlag){
            this.starMark = (int) Math.ceil(starMark);
        }else{
            this.starMark = Math.round(starMark*10)*1.0f/10;
        }
        if (this.listenner!=null){
            listenner.onchangerListener();
        }
    }
    public interface OnstarChangeListenner{
        void onchangerListener();
    }
    public void setIntMarkFlag(boolean intMarkFlag) {
        this.intMarkFlag = intMarkFlag;
    }

    public FiveStarView(Context context) {
        this(context,null);
    }

    public FiveStarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FiveStarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.fiveStar);
        emptyDraw = a.getDrawable(R.styleable.fiveStar_emptyDraw);
        starSize = (int) a.getDimension(R.styleable.fiveStar_starSize,40);
        fillDraw = drawToBitMap(a.getDrawable(R.styleable.fiveStar_fillDraw));
        starCount = a.getInt(R.styleable.fiveStar_starCount,5);
        starDistance = (int) a.getDimension(R.styleable.fiveStar_starDistance,0);
        a.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(fillDraw,BitmapShader.TileMode.CLAMP,BitmapShader.TileMode.CLAMP));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(starSize*starCount+starDistance*(starCount-1),starSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (emptyDraw==null||fillDraw==null)
            return;
        if (starCount>0){
            for (int i=0;i<starCount;i++){
                emptyDraw.setBounds(i*(starSize+starDistance),0,starSize+i*(starSize+starDistance),starSize);
                emptyDraw.draw(canvas);
            }
        }
        if (starMark>1){
            canvas.drawRect(0,0,starSize,starSize,paint);
            if (starMark==(int)starMark){
                for (int i = 1;i<starMark;i++){
                    canvas.translate(starSize+starDistance,0);
                    canvas.drawRect(0,0,starSize,starSize,paint);
                }
            }else{
                for (int i=1;i<starMark-1;i++){
                    canvas.translate(starSize+starDistance,0);
                    canvas.drawRect(0,0,starSize,starSize,paint);
                }
                canvas.translate(starSize+starDistance,0);
                canvas.drawRect(0,0,starSize*(Math.round((starMark-(int)(starMark))*10)*1.0f/10),starSize,paint);
            }
        }else{
            canvas.drawRect(0,0,starSize*(Math.round(starMark-(int)(starMark))*10*1.0f/10),starSize,paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        if (x<0){
            x=0;
        }
        if (x>getMeasuredWidth())x=getMeasuredWidth();
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setStarMark(x*1.0f/(getMeasuredWidth()/starCount));
                break;
            case MotionEvent.ACTION_MOVE:
                setStarMark(x*1.0f/(getMeasuredWidth()/starCount));
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        invalidate();
        return true;
    }

    private Bitmap drawToBitMap(Drawable drawable){
        if (drawable==null)
            return null;
        Bitmap bitmap = Bitmap.createBitmap(starSize,starSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,starSize,starSize);
        drawable.draw(canvas);
        return bitmap;
    }
}
