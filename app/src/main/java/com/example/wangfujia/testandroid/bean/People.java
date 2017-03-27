package com.example.wangfujia.testandroid.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wangfujia on 17/3/24.
 */

public class People implements Parcelable {
    protected People(Parcel in) {
    }

    public static final Creator<People> CREATOR = new Creator<People>() {
        @Override
        public People createFromParcel(Parcel in) {
            return new People(in);
        }

        @Override
        public People[] newArray(int size) {
            return new People[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
