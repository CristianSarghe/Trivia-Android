package com.example.didyouknow.models;

import android.os.Parcel;
import android.os.Parcelable;

public class CategoryModel implements Parcelable {

    public long id;
    public String name;

    public CategoryModel() {

    }

    public CategoryModel(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public CategoryModel(Parcel parcel){
        this.id = parcel.readLong();
        this.name = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeLong(this.id);
        destination.writeString(this.name);
    }

    public static final Parcelable.Creator<CategoryModel> CREATOR = new Parcelable.Creator() {
        public CategoryModel createFromParcel(Parcel parcel) {
            return new CategoryModel(parcel);
        }

        public CategoryModel[] newArray(int size) {
            return new CategoryModel[size];
        }
    };
}
