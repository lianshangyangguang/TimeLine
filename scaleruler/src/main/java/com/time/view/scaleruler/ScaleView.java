package com.time.view.scaleruler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;


public class ScaleView extends TextureView implements TextureView.SurfaceTextureListener, ScaleScroller.ScrollingListener{

    ScaleScroller mScroller;

    private Rect mTextRect = new Rect();
    private RectF mBorderRectF = new RectF();

    private Paint mBorderPaint = new Paint();
    private Paint mCurrentMarkPaint = new Paint();
    private Paint mScaleMarkPaint = new Paint();

    private int mTextHeight = 20; //数字字体大小

    private int mCenterNum; //中心点数字
    private int offset = 0;
    private int dis = 0;  //刻度间距   由mWidth 和 allBlockNum计算得到
    private int allBlockNum = 30;  //刻度分割块的数量 默认30
    private float mWidth;

    private int maxNum = 240; //最大数字
    private int minNum = 0; //最小数字
    private int scaleNum = 1; //每一个刻度间相差数

    private int hourNum = 10;

    private NumberListener numberListener;

    private Context context;

    public ScaleView(Context context) {
        super(context);
        this.context =context;
        dis = dip2px(12);
        init();
//        this(context, null,0);
    }

    public ScaleView(Context context, AttributeSet attrs) {
//        this(context, attrs,0);
        super(context,attrs);
        this.context =context;
        dis = dip2px(12);
        init();
    }

    public ScaleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context =context;
        dis = dip2px(12);
        init();
    }

    private void init() {
        mScroller = new ScaleScroller(getContext(), this);
        setSurfaceTextureListener(this);
        initPaints();
    }

    private void initPaints() {
        mBorderPaint.setColor(0xffffdfbe);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(2);

        //刻度线
        mScaleMarkPaint.setColor(0xff979797);
        mScaleMarkPaint.setStyle(Paint.Style.FILL);
        mScaleMarkPaint.setStrokeWidth(3);
        mScaleMarkPaint.setTextSize(mTextHeight);

        //当前
        mCurrentMarkPaint.setColor(Color.RED);
        mCurrentMarkPaint.setStyle(Paint.Style.FILL);
        mCurrentMarkPaint.setStrokeWidth(3);

    }

    //刷新视图
    private void refreshCanvas() {
        if (mBorderRectF.isEmpty()) {
            return;
        }
        Canvas canvas = lockCanvas();
        if(canvas != null) {
            canvas.drawColor(Color.WHITE);
            drawBorder(canvas);
            drawScaleMark(canvas);
            drawMarkPoint(canvas);
        }
        unlockCanvasAndPost(canvas);
    }

    //画出所有刻度:从中间向两边画
    private void drawScaleMark(Canvas canvas) {
        int count = 0;
        final int centerX = (int)mBorderRectF.centerX();
        if(mCenterNum > maxNum)
            mCenterNum = maxNum;
        if (mCenterNum < minNum)
            mCenterNum = minNum;
        if(numberListener != null)
            numberListener.onChanged(getTime());

        while(true){
            int left = centerX+distance - dis * count;
            int leftNum = mCenterNum - count * scaleNum;
            int right = centerX+distance + dis * count;
            int rightNum = mCenterNum + count * scaleNum;

            String leftText = String.valueOf(Math.round(leftNum/hourNum))+":00";
            String rightText = String.valueOf(Math.round(rightNum/hourNum))+":00";

            int showNum = hourNum;
            if (hourNum == 1){
                showNum = 3;
            }
            //间隔5刻度画文字信息
            if (leftNum < minNum){
//                break;
            }else {
                if(leftNum % (showNum*scaleNum) == 0) {
//                canvas.drawLine(left, canvas.getHeight() / 2, left, canvas.getHeight() - 1, mScaleMarkPaint);
                    canvas.drawLine(left,0, left, canvas.getHeight() / 2 ,mScaleMarkPaint);
                    mScaleMarkPaint.getTextBounds(leftText, 0, leftText.length(), mTextRect);
                    canvas.drawText(leftText, left - mTextRect.centerX(), canvas.getHeight() * 2 / 3, mScaleMarkPaint);
                }
                else
//                canvas.drawLine(left, canvas.getHeight() * 2 / 3, left, canvas.getHeight() - 1, mScaleMarkPaint);
                    canvas.drawLine(left,0, left,  canvas.getHeight() * 1/ 3, mScaleMarkPaint);
            }

            if (rightNum > maxNum){
//                break;
            }else {
                if(rightNum % (showNum*scaleNum) == 0) {
//                canvas.drawLine(right, canvas.getHeight() / 2, right, canvas.getHeight() - 1, mScaleMarkPaint);
                    canvas.drawLine(right, 0, right, canvas.getHeight() / 2, mScaleMarkPaint);
                    mScaleMarkPaint.getTextBounds(rightText, 0, rightText.length(), mTextRect);
                    canvas.drawText(rightText, right - mTextRect.centerX(), canvas.getHeight() * 2 / 3, mScaleMarkPaint);
                }
                else
//                canvas.drawLine(right, canvas.getHeight() * 2 / 3, right, canvas.getHeight() - 1, mScaleMarkPaint);
                    canvas.drawLine(right, 0, right, canvas.getHeight() * 1 / 3, mScaleMarkPaint);
            }

            count++;
            if(left < -Math.abs(distance)*2)
                break;
        }
    }

    private void drawBorder(Canvas canvas) {
        canvas.drawLine(mBorderRectF.left, mBorderRectF.bottom - 1, mBorderRectF.right, mBorderRectF.bottom - 1, mScaleMarkPaint);
    }


    private void drawMarkPoint(Canvas canvas) {
        int centerX = (int)mBorderRectF.centerX();
//        canvas.drawLine(centerX, canvas.getHeight() / 4, centerX, canvas.getHeight() - 1, mCurrentMarkPaint);
        canvas.drawLine(centerX, 0, centerX, canvas.getHeight() - 1, mCurrentMarkPaint);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mBorderRectF.set(mBorderPaint.getStrokeWidth(), mBorderPaint.getStrokeWidth(),
                width - mBorderPaint.getStrokeWidth(), height - mBorderPaint.getStrokeWidth());
        mWidth = mBorderRectF.width();
        refreshCanvas();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    int distance = 0;

    @Override
    public void onScroll(int distance) {

        this.distance += distance;
//        mCenterNum -=(int)( this.distance/dis);
//        this.distance = this.distance%dis;
//
//                if (mCenterNum <= 0 && this.distance >0){
//            return;
//        }
//        if (mCenterNum >= 24 && this.distance <0){
//            return;
//        }


//        Log.d("zxy", "mCenterNum: "+mCenterNum +"this.distance:"+this.distance);

//        || mCenterNum == 24
//        offset += distance;
////        layout(10,10,500,500);
//        if (offset > dis) {
//            offset = 0;
//            mCenterNum -= scaleNum;
//        }
//        if (offset < -dis) {
//            offset = 0;
//            mCenterNum += scaleNum;
//        }
        refreshCanvas();
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onFinished() {
            //还原中心点在刻度位置上
            mCenterNum -=Math.round((float) distance/dis);
            distance = 0;
            refreshCanvas();
    }

    @Override
    public void onJustify(float mScale) {
//        if (mScale < 0.5f){
//            mScale = 0.5f;
//        }else if (mScale >2){
//            mScale = 2;
//        }
        int index = 0;
        index =  getIndex(hourNum);
        if (index == -1){
            //
        }
        if (mScale >1){
            if (index + 1 <= multiple.length-1){
                hourNum = multiple[index +1];
            }
        }else if (mScale <1){
            if (index - 1 >=0){
                hourNum = multiple[index -1];
            }
        }
        maxNum = hourNum * 24;

//        Log.d("zxy", "mScale: "+mScale);
//        float hour = 0;
//        int shourNum = hourNum;
//        hour = hourNum*(mScale*1000);
//
//        if ((int)(hour/1000) != 0){
//            hourNum = (int)(hour/1000);
//            if (hourNum == shourNum){
//                if (mScale >1){
//                    hourNum += 1;
//                }else if (mScale <1){
//                    hourNum -= 1;
//                }
//            }
//            maxNum = hourNum * 24;
//        }

        Log.d("zxy", "maxNum: "+maxNum+"hourNum: "+hourNum);
        refreshCanvas();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mScroller.onTouchEvent(event);
    }

    interface NumberListener{
        public void onChanged(float time);
    }


    public void setNumberListener(NumberListener listener){
        this.numberListener = listener;
    }

    public void setTextSize(int textSize){
        this.mTextHeight = textSize;
    }

    public void setMaxNumber(int maxNum){
        this.maxNum = maxNum;
    }

    public void setMinNumber(int minNum){
        this.minNum = minNum;
    }

    public void setScaleNumber(int scaleNum){
        this.scaleNum = scaleNum;
    }

    public void setAllBlockNum(int allBlockNum){
        this.allBlockNum = allBlockNum;
    }

    public void setCenterNum(int centerNum){
        this.mCenterNum = centerNum;
    }

    @Override
    public float getTime() {
        return  (float) mCenterNum/hourNum;
    }

    //获取当前时间（毫秒）
    public long getCurrentTime(){
        return  (long) ((double) mCenterNum/hourNum)*60*24*1000*60;
    }

    @Override
    public void setTime(float time) {

        mCenterNum = (int)time * hourNum;
        Log.e("zxy", "setTime: "+time * hourNum);
        refreshCanvas();
    }

    private int[] multiple  = new int[]{1,2,3,4,5,6,10,15,20,30,60};

    private int getIndex(int num){
        for (int i = 0 ;i<multiple.length;i++){
            if (multiple[i] == num){
                return i;
            }
        }
        return -1;
    }

    private int dip2px(float dipValue) {
        final float scale =context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}