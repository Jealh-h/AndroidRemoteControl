package com.example.javaremotecontroller.model;

import com.example.javaremotecontroller.R;

public class DeviceCategoryModel {
    private int imageSrc;
    private String categoryName;
    private int id;

    public DeviceCategoryModel(int imageSrc, String categoryName, int id) {
        this.imageSrc = imageSrc;
        this.categoryName = categoryName;
        this.id = id;
    }

    public int getImageSrc() {
        return this.imageSrc;
    }

    public int getId() {
        return this.id;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

}
