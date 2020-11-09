package com.example.snowing;

import java.util.Random;

public class Snow{

    int x;
    int y;
    Random r =new Random();

    public Snow(int x, int y) {
        this.x=x;
        this.y=y;
    }
    public Snow(){
        x = r.nextInt(1000);
        y=0;
    }


//    @Override
//    public void run() {
//        for (int i = 0; i < 1788; i++) {
//            x = x +r.nextInt(3)-1;
//            y++;
//        }
//        try {
//            sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        //System.out.println(new String(new char[x] ).replace("\0"," ")+"s");
//    }

    public void move(){
        x = x +r.nextInt(5)-1;
        y++;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}