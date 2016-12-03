package com.example.jyj.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by jyj on 2016/12/3.
 */
public class MyRing extends View {
    /**
     * 圆心的x坐标
     */
    private float cx;

    /**
     * 圆心的y坐标
     */
    private float cy;

    /**
     * 圆的半径
     */
    private float radius;

    /**
     * 默认画笔
     */
    private Paint paint;

    /**
     * 判断当前动画是否在执行
     */
    protected boolean isRunning=false;


    /**
     *
     * @param context
     * @param attrs
     */
    public MyRing(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        radius=0;
        paint=new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(radius/4);
        paint.setColor(Color.GREEN);
        paint.setAlpha(255);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(cx,cy,radius,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            cx=event.getX();
            cy=event.getY();
            initView();//每次按屏幕不同的地方，都是重新开始，所有又初始化一下
            startAnim();
        }
        return true;

    }

    /**
     * 开始动画
     */
    private void startAnim() {
        isRunning=true;
        handler.sendEmptyMessageDelayed(0,50);

    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            设置透明度
            int alpha=paint.getAlpha();
            if (alpha==0){
                isRunning=false;
            }
            alpha=Math.max(0,alpha-10);
            paint.setAlpha(alpha);

//          设置半径

            radius+=5;
            paint.setStrokeWidth(radius/3);
//            通知重绘界面
            invalidate();
            if (isRunning){
                handler.sendEmptyMessageDelayed(0,50);
            }

        }
    };
}
