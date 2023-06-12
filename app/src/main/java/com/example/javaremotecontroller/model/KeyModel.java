package com.example.javaremotecontroller.model;

public class KeyModel {
    public int viewId;
    public String key;
    public String name;

    public KeyModel(int viewId, String key) {
        this.viewId = viewId;
        this.key = key;
    }

    public KeyModel(int viewId, String key, String name){
        this.viewId = viewId;
        this.key = key;
        this.name = name;
    }
}
