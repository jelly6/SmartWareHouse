package com.example.smartwarehouse.viewDesign;

import androidx.annotation.Nullable;

public class Pos{
    int x;
    int y;
    public Pos(int x, int y){
        this.x =x;
        this.y=y;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pos pos = (Pos) o;
        if(x!=pos.getX()) return false;
        if(y!=pos.getY()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        return result;
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

//    private String id;

//    public Person(String id) {
//        this.id = id;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//
//        Person person = (Person) o;
//
//        if (id != null ? !id.equals(person.id) : person.id != null) return false;
//
//        return true;
//    }
//
//    @Override
//    public int hashCode() {
//        return id != null ? id.hashCode() : 0;
//    }
//}
//那么，当我们重新执行上
