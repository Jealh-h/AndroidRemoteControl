package com.example.javaremotecontroller.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.javaremotecontroller.R;

public class DeviceCategoryModel implements Parcelable {
    private int imageSrc;
    private String categoryName;
    private int id;

    public DeviceCategoryModel(int imageSrc, String categoryName, int id) {
        this.imageSrc = imageSrc;
        this.categoryName = categoryName;
        this.id = id;
    }

    protected DeviceCategoryModel(Parcel in) {
        imageSrc = in.readInt();
        categoryName = in.readString();
        id = in.readInt();
    }

    public static final Creator<DeviceCategoryModel> CREATOR = new Creator<DeviceCategoryModel>() {
        @Override
        public DeviceCategoryModel createFromParcel(Parcel in) {
            return new DeviceCategoryModel(in);
        }

        @Override
        public DeviceCategoryModel[] newArray(int size) {
            return new DeviceCategoryModel[size];
        }
    };

    public int getImageSrc() {
        return this.imageSrc;
    }

    public int getId() {
        return this.id;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getImageSrc());
        dest.writeString(getCategoryName());
        dest.writeInt(getId());
    }
}
