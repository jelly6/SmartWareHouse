package com.example.smartwarehouse.model;

import java.util.Date;

public class Material {
    String date_code;
    String model;
    String item_name;
    String factory;
    String led_bin;
    Date date_in;

    public Material(String date_code, String model, String item_name,String led_bin, String factory , Date date_in) {
        this.date_code = date_code;
        this.model = model;
        this.item_name = item_name;
        this.factory = factory;
        this.led_bin = led_bin;
        this.date_in = date_in;
    }

    public Material(String date_code) {
        this.date_code = date_code;
    }

    public String getDate_code() {
        return date_code;
    }

    public void setDate_code(String date_code) {
        this.date_code = date_code;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getLed_bin() {
        return led_bin;
    }

    public void setLed_bin(String led_bin) {
        this.led_bin = led_bin;
    }

    public Date getDate_in() {
        return date_in;
    }

    public void setDate_in(Date date_in) {
        this.date_in = date_in;
    }
}
