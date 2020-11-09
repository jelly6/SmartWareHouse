package com.example.smartwarehouse;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.snowing.Snow;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.lang.Thread.sleep;

public class SnowCanvas extends View {

    Handler handler;
    private Paint paint = new Paint();
    List<Snow> snows;
    int snowCount = 1;
    private final Snow snow;

    public SnowCanvas(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(Color.BLACK);
        handler = new Handler();
        snow = new Snow();


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.WHITE);
        paint.setTextSize(50);
        canvas.drawCircle(snow.getX(),snow.getY(),30,paint);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                snow.move();
                invalidate();
            }
        },1);




    }

    public void init(){
        float width = 1080;//getWidth();
        float height = 1788;//getHeight();
        snows = new ArrayList<>();
        for (int i = 0; i < snowCount; i++) {
            int x =new Random().nextInt((int) width);
            int y =new Random().nextInt((int) height);
            Snow snow = new Snow(x,y);
            snows.add(snow);
        }
    }
}
