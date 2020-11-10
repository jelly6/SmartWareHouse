package com.example.smartwarehouse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.smartwarehouse.viewDesign.MazeSolver;
import com.example.smartwarehouse.viewDesign.Pos;

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
    private Map<int[], Integer> hashMap;
    private HashMap<Pos, Pos> mapCenter;
    private MazeSolver mazeSolver;
    private List<Pos> allPoints;
    private List<Pos> seeds;
    private Paint transparentPaint;

    public DrawView(Context context) { super(context);
    }

    public DrawView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //display pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        length = displayMetrics.heightPixels-16;
        width = displayMetrics.widthPixels-16;
        Log.d(TAG, "DrawView: Height:\t"+ length);
        Log.d(TAG, "DrawView: width:\t"+ width);

        //
        Log.d(TAG, "DrawView: parse\t"+stuff[0].charAt(0));
        target_cabinet = new HashMap<>();
        for (int i = 0; i < stuff.length; i++) {
            String words=stuff[i];
            Log.d(TAG, "DrawView: parse\t " +words+"\tindex:\t"+capital.indexOf(String.valueOf(words.charAt(0))));
            target_cabinet.put(capital.indexOf(String.valueOf(words.charAt(0))), (int) words.charAt(1));
        }

        //drawCabinets
        drawCabinets();
//        int[][] mat =
//                {
//                        { 1, 1, 1, 1, 1, 0, 0, 1, 1, 1 },
//                        { 0, 1, 1, 1, 1, 1, 0, 1, 0, 1 },
//                        { 0, 0, 1, 0, 1, 1, 1, 0, 0, 1 },
//                        { 1, 0, 1, 1, 1, 0, 1, 1, 0, 1 },
//                        { 0, 0, 0, 1, 0, 0, 0, 1, 0, 1 },
//                        { 1, 0, 1, 1, 1, 0, 0, 1, 1, 0 },
//                        { 0, 0, 0, 0, 1, 0, 0, 1, 0, 1 },
//                        { 0, 1, 1, 1, 1, 1, 1, 1, 0, 0 },
//                        { 1, 1, 1, 1, 1, 0, 0, 1, 1, 1 },
//                        { 0, 0, 1, 0, 0, 1, 1, 0, 0, 1 },
//                };
        int[][] mat =
                {
                        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,1,1},
                        { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,0,1},
                        { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,0,1},
                        { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,0,1},
                        { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,0,1},
                        { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,0,1},
                        { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,0,1},
                        { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,0,1},
                        { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,0,1},
                        { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,0,1},
                        { 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0,0,1},
                        { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,1,1},
                };
        //material position
        seeds = new ArrayList<>();
        //seeds.add(new Pos(0,0));
        seeds.add(new Pos(3,7));
        seeds.add(new Pos(8,4));

        //setUp the paths
        List<Pos> paths = new ArrayList<>();
        paths.add(new Pos(0,0));
        paths.add(new Pos(3,8));
        paths.add(new Pos(6,3));
        paths.add(new Pos(11,0));
        paths.add(new Pos(18,11));

        //getAllPoints
        allPoints = new ArrayList<>();
        for (int i = 0; i < paths.size()-1; i++) {
            Pos pos1 = paths.get(i);
            Pos pos2 = paths.get(i+1);
            mazeSolver = new MazeSolver(mat,pos1,pos2);
            allPoints.addAll(mazeSolver.getRoute());

        }

        for (Pos pos : mazeSolver.getRoute()) {
            Log.d(TAG, "DrawView: "+pos.getX()+",\t"+pos.getY());
        }
        drawMap(mat);

    }
    private void drawMap(int[][] map){
        int map_length = map.length;
        int map_width = map[0].length;
        Log.d(TAG, "drawMap: l\t"+length);
        Log.d(TAG, "drawMap: w\t"+width);
        int ratio_x = 8;
        int ratio_y = 10;
        int shift_x =50;
        int shift_y =30;
        float cabinet_width = (float) (width /ratio_x*0.4);
        float cabinet_length = (float) (length /ratio_y*0.4);
        float cabinet_gap = 5f;
        Log.d(TAG, "drawMap: cabinet_width\t"+cabinet_width);

        hashMap = new HashMap<>();
        mapCenter = new HashMap<>();
        for (int i = 0; i < map_width; i++) {
            for (int j = 0; j < map_length; j++) {
                int left = (int) (shift_x +i*cabinet_width);
                int top = (int) (shift_y+j*cabinet_length);
                int right = (int) (left+cabinet_width-cabinet_gap);
                int bottom = (int) (top+cabinet_length-cabinet_gap);
                int[] num = new int[]{left,top,right,bottom};
                hashMap.put(num,map[j][i]);
                Pos po = new Pos(j, i);
                Pos center = new Pos((left+right)/2, (top+bottom)/2);
//                Pos center = new Pos((left+right)/2, (top+bottom)/2);
                mapCenter.put(po,center);
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
            Log.d(TAG, "DrawView1: "+left+"\t"+top+"\t"+right+"\t"+bottom);
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


    @Override
    protected void onDraw(Canvas canvas) {
        transparentPaint = new Paint();
        transparentPaint.setARGB(0x77,0xff,0x00,0x00);

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

        if(hashMap!=null){
            for (int[] drawer : hashMap.keySet()) {
                if(hashMap.get(drawer)==0){
                    paint.setColor(Color.BLUE);
                }
                else{
                    paint.setARGB(10,100,100,100);
                }
                Log.d(TAG, "onDraw: "+"["+drawer[0]+","+drawer[1]+","+drawer[2]+","+drawer[3]+"]\t"+hashMap.get(drawer));
                canvas.drawRect(drawer[0],drawer[1],drawer[2],drawer[3],paint);
            }
        }
        if(mazeSolver!=null){
            Log.d(TAG, "onDraw maze\t: "+mapCenter.keySet().toString());
            for (Pos pos : mapCenter.keySet()) {
                //Log.d(TAG, "onDraw: mapCenter\t"+pos.getX());
                //Log.d(TAG, "onDraw: mapCenter\t"+pos.getY());
            }
            for (Pos pos : allPoints) {
                Log.d(TAG, "onDraw maze\t: "+pos.getX());
                Log.d(TAG, "onDraw maze\t: "+pos.getY());

                Pos center = mapCenter.get(pos);
                Log.d(TAG, "onDraw: "+center.getY());
                //transparentPaint.setColor(Color.CYAN);
                canvas.drawCircle(center.getX(),center.getY(),15,transparentPaint);
            }

            for (Pos seed : seeds) {
                Pos ce = mapCenter.get(seed);
                //Log.d(TAG, "onDraw: "+center.getY());
                paint.setColor(Color.RED);
                canvas.drawCircle(ce.getX(),ce.getY(),10,paint);
            }
        }

        new AlertDialog.Builder(getContext())
                .setTitle("完成!!")
                .setMessage("最佳化路徑演算完成")
                .setPositiveButton("OK",null)
                .show();


    }
    public void setTargets(){

    }
}
