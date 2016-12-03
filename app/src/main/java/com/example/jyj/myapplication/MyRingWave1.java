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
import java.util.List;

/**
 * Created by jyj on 2016/12/3.
 */
public class MyRingWave1 extends View {
    private List<Wave> list;
    private int[] colors=new int[]{Color.BLUE,Color.YELLOW,Color.RED,Color.GREEN};
    private boolean isRunning=false;
    private static  final int DISTANCE=12;


    public MyRingWave1(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData();
    }

    private void initData() {
        list=new ArrayList<>();
    }


    private class Wave{
        float wx;
        float wy;
        float r;
        Paint p;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float x=event.getX();
                float y=event.getY();
                addPoint(x,y);

        }

        return true;
    }

    private void addPoint(float x, float y) {
        if (list.size()==0){
            add2list(x,y);
            isRunning=true;
            handler.sendEmptyMessage(0);
        }else {
           Wave wave= list.get(list.size()-1);
            if (Math.abs(wave.wx-x)>DISTANCE||Math.abs(wave.wy-y)>DISTANCE){
                add2list(x,y);
            }
        }


    }
    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            flushData();
            invalidate();//刷新界面
            if (isRunning){
                handler.sendEmptyMessageDelayed(0,50);
            }
        }
    };


    private void flushData() {
        for (int i=0;i<list.size();i++){
            Wave wave=list.get(i);
           int alpha= wave.p.getAlpha();

            if (alpha==0){
                list.remove(i);
                i--;
                continue;
            }

            alpha-=5;
            if (alpha<5){
                alpha=0;
            }

            wave.p.setAlpha(alpha);
            wave.r=wave.r+3;
            wave.p.setStrokeWidth(wave.r/3);
        }
        if (list.size()==0){
            isRunning=false;
        }
    }
    private void add2list(float x, float y) {
        //半径和透明度是在变化，在这里没有定义。
        Wave wave=new Wave();
        wave.wx=x;
        wave.wy=y;
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(colors[(int) (Math.random()*4)]);
        paint.setStyle(Paint.Style.FILL);
        wave.p=paint;
        list.add(wave);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int i=0;i<list.size();i++){
            Wave wave=list.get(i);
            canvas.drawCircle(wave.wx,wave.wy,wave.r,wave.p);
        }
    }
}
