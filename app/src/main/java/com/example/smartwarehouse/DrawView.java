package com.example.smartwarehouse;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.protobuf.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DrawView extends View {
    private static final String TAG = DrawView.class.getSimpleName();
    Paint paint=new Paint();
    private List<int[]> position;
    private List<int[]> pixels;

    private int length;
    private int width;
    private Map<Integer,Integer> target_cabinet;
    List<String> capital = Arrays.asList(new String[]{"A","B","C","D","E","F"});
    String[] stuff = {"A101","B203","F504","D901"};

    public DrawView(Context context) {
        super(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        length = displayMetrics.heightPixels-16;
        width = displayMetrics.widthPixels-16;
        Log.d(TAG, "DrawView: Height:\t"+ length);
        Log.d(TAG, "DrawView: width:\t"+ width);


        Log.d(TAG, "DrawView: parse\t"+stuff[0].charAt(0));
        target_cabinet = new HashMap<>();
        for (int i = 0; i < stuff.length; i++) {
            String words=stuff[i];
            Log.d(TAG, "DrawView: parse\t " +words+"\tindex:\t"+capital.indexOf(String.valueOf(words.charAt(0))));
            target_cabinet.put(capital.indexOf(String.valueOf(words.charAt(0))), (int) words.charAt(1));
        }




        //drawCabinets
        drawCabinets();
        //drawGrid
        //drawGrid();

    }

    private void drawGrid() {
        int pixel_x =100;
        int pixel_y=100;
        int pixel_size = width/pixel_x;
        int gap =1;
        pixels = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                int left = i*pixel_size;
                int top = j*pixel_size;
                int right = left+pixel_size-gap;
                int bottom = top+pixel_size-gap;
                int[] num = new int[]{left,top,right,bottom};
                pixels.add(num);
                Log.d(TAG, "DrawView2: "+left+"\t"+top+"\t"+right+"\t"+bottom);
            }

        }
    }

    private void drawCabinets() {
        int ratio_x = 7;
        int ratio_y = 3;
        int shift_x =100;
        int shift_y =30;
        float cabinet_width = (float) (width /ratio_x*0.5);
        float cabinet_freq = (float) (width /ratio_x);
        float cabinet_length = (float) (length /ratio_y);
        position = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            int left = (int) (shift_x +i*cabinet_freq);
            int top = shift_y;
            int right = (int) (left+cabinet_width);
            int bottom = (int) (top+cabinet_length);
            int[] num = new int[]{left,top,right,bottom};
            position.add(num);
            Log.d(TAG, "DrawView: "+left+"\t"+top+"\t"+right+"\t"+bottom);
        }
    }

    private List<int[]> drawDrawer(int[] cabinet){
        int cabinet_width= cabinet[2]-cabinet[0];
        int cabinet_length= cabinet[3]-cabinet[1];
        int row = 1;
        int col = 10;
        int gap =2;
        int drawer_width = cabinet_width/row;
        int drawer_length = cabinet_length/col;
        List<int[]> drawers = new ArrayList<>();
        int start_x =cabinet[0];
        int start_y = cabinet[1];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int left = start_x+drawer_width*i;
                int top = start_y+drawer_length*j;
                int right = left+drawer_width-gap;
                int bottom = top+drawer_length-gap;
                int[] num = new int[]{left,top,right,bottom};
                drawers.add(num);
            }
        }
        return drawers;

    }

//    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//    }
//
//    public DrawView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }


    @Override
    protected void onDraw(Canvas canvas) {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        canvas.drawRect(30,30,80,80,paint);
        paint.setStrokeWidth(0);
        paint.setColor(Color.CYAN);
        canvas.drawRect(33,60,77,77,paint);
//        paint.setColor(getResources().getColor(R.color.checkList_color_yellow));
//        paint.setColor(Color.GRAY);
//        for (int[] pixel : pixels) {
//            canvas.drawRect(pixel[0],pixel[1],pixel[2],pixel[3],paint);
//        }
        paint.setColor(Color.YELLOW);
        canvas.drawRect(33, 33, 77, 60, paint );

        //cabinet
//        paint.setColor(Color.BLACK);
//        paint.setStyle(Paint.Style.STROKE);
//        paint.setStrokeWidth(3);
//        for (int[] pos : position) {
//            canvas.drawRect(pos[0],pos[1],pos[2],pos[3],paint);
//        }


        paint.setColor(Color.GRAY);
        for (int[] pos : position) {
            for (int[] drawer : drawDrawer(pos)) {
                canvas.drawRect(drawer[0],drawer[1],drawer[2],drawer[3],paint);
            }
        }

        paint.setColor(Color.RED);
        for (int i = 0; i < position.size(); i++) {
            if(target_cabinet.keySet().contains(i)){
                int[] pos = position.get(i);
                List<int[]> drawers = drawDrawer(pos);
                int[] drawer = drawers.get(i);
                canvas.drawRect(drawer[0],drawer[1],drawer[2],drawer[3],paint);
            }

        }

    }
}
