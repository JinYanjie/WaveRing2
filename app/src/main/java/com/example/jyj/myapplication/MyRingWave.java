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

import java.util.ArrayList;

/**
 * Created by jyj on 2016/12/3.
 */
public class MyRingWave extends View {
    /**
     * 两个相邻波浪中心最小距离
     */
    private static final int DIS_SOLP=8;
    /**
     * 颜色数组
     */
    private int [] colors=new int[]{Color.BLUE,Color.RED,Color.YELLOW,Color.GREEN};

    protected boolean isRunning=false;
    private ArrayList<Wave> mList;


    public MyRingWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        mList=new ArrayList<>();
    }

    private class Wave{
        //圆心
        int cx;
        int cy;

        //画笔
        Paint p;
        //半径r
        int r;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i=0;i<mList.size();i++){
            Wave wave=mList.get(i);
            canvas.drawCircle(wave.cx,wave.cy,wave.r,wave.p);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_MOVE:
                int x= (int) event.getX();
                int y= (int) event.getY();
                addPoint(x,y);

        }

        return true;
}

    private void addPoint(int x, int y) {
        if (mList.size()==0){
            addPoint2List(x,y);
//            第一次启动动画
            isRunning=true;
            handler.sendEmptyMessage(0);
        }else {
            Wave wave=mList.get(mList.size()-1);
            if (Math.abs(wave.cx-x)>DIS_SOLP||(Math.abs(wave.cy-y)>DIS_SOLP)){
                addPoint2List(x, y);

            }
        }
    }
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            刷新数据
            flushData();
//            刷新页面
            invalidate();
 //            循环动画
            if (isRunning){
                handler.sendEmptyMessageDelayed(0,50);
            }
//
        }
    };

    /**
     * 刷新数据
     *
     */
    private void flushData() {
        for (int i=0;i<mList.size();i++){
            Wave wave=mList.get(i);
//            改变透明度
            int alpha=wave.p.getAlpha();
            if (alpha==0){
                mList.remove(i);
//                删除i以后，i的值应该减1.否则会漏掉一个对象
                i--;
                continue;
            }
            alpha-=5;
            if (alpha<5){
                alpha=0;
            }
            wave.p.setAlpha(alpha);
            //扩大半径
            wave.r=wave.r+3;
//            设置半径厚度
            wave.p.setStrokeWidth(wave.r/3);
        }
        if (mList.size()==0){
            isRunning=false;
        }

    }

    private void addPoint2List(int x, int y) {
        Wave wave=new Wave();
        wave.cx=x;
        wave.cy=y;
        Paint paint=new Paint();
        paint.setColor(colors[(int) (Math.random()*4)]);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        wave.p=paint;
        mList.add(wave);
    }
}
