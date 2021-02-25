package com.example.smartwarehouse.model;

import java.util.HashMap;

public class Item {
    String item_model;
    String item_datecode;
    String position;
    boolean checked;

    public Item() {
    }

    public Item(String item_model,String item_datecode, String position, boolean checked) {
        this.item_model = item_model;
        this.item_datecode = item_datecode;
        this.position = position;
        this.checked = checked;
    }

    public String getItem_model() {
        return item_model;
    }

    public void setItem_model(String item_model) {
        this.item_model = item_model;
    }

    public String getItem_datecode() {
        return item_datecode;
    }

    public void setItem_datecode(String item_datecode) {
        this.item_datecode = item_datecode;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public HashMap<String,Object> pushMap(){
        HashMap<String, Object> map = new HashMap();
        map.put("item_model",item_model);
        map.put("item_datecode",item_datecode);
        map.put("checked",checked);
        map.put("position",position);
        return map;

    }



}
