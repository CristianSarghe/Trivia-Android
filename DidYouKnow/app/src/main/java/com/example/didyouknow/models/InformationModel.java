package com.example.didyouknow.models;

import android.os.Parcel;
import android.os.Parcelable;

public class InformationModel implements Parcelable {
    public long id;
    public long categoryId;
    public String title;
    public String text;

    public InformationModel() {

    }

    public InformationModel(long id, long categoryId, String title, String text) {
        this.id = id;
        this.categoryId = categoryId;
        this.title = title;
        this.text = text;
    }

    public InformationModel(Parcel parcel) {
        this.id = parcel.readLong();
        this.categoryId = parcel.readLong();
        this.title = parcel.readString();
        this.text = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int flags) {
        destination.writeLong(this.id);
        destination.writeLong(this.categoryId);
        destination.writeString(this.title);
        destination.writeString(this.text);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public InformationModel createFromParcel(Parcel parcel) {
            return new InformationModel(parcel);
        }

        public InformationModel[] newArray(int size) {
            return new InformationModel[size];
        }
    };

}
