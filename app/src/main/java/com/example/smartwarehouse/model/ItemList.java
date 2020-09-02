package com.example.smartwarehouse.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ItemList {
    Map<String,String> items;

    public ItemList() {
        items=new HashMap<>();
    }

    public Map<String, String> getItems() {
        return items;
    }
    public Set<String> getKeys(){
        return items.keySet();
    }

    public void setItems(Map<String, String> items) {
        this.items = items;
    }
}
