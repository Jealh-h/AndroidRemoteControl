package com.example.javaremotecontroller.model;

import android.os.Parcel;
import android.os.Parcelable;

import net.irext.webapi.model.Brand;

public class BrandModel extends Brand implements Parcelable {

    private Brand brand;
    private int id;
    private int categoryId;
    private String categoryName;

    public BrandModel(Brand brand) {
        this.brand = brand;
        this.id = brand.getId();
        this.categoryId = brand.getCategoryId();
        this.categoryName = brand.getCategoryName();
    }

    protected BrandModel(Parcel in) {
        this.id = in.readInt();
        this.categoryName = in.readString();
        this.categoryId = in.readInt();
    }

    public static final Creator<BrandModel> CREATOR = new Creator<BrandModel>() {
        @Override
        public BrandModel createFromParcel(Parcel in) {
            return new BrandModel(in);
        }

        @Override
        public BrandModel[] newArray(int size) {
            return new BrandModel[size];
        }
    };

    public int getId() {
        return this.id;
    }

    public int getCategoryId() {return this.categoryId;}

    public String getCategoryName() {
        return this.categoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getCategoryName());
        dest.writeInt(getCategoryId());
    }
}

