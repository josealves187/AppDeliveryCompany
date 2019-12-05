package br.com.jose.alves.freedeliverycliente.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;



public class Time implements Parcelable {

    private String day;
    private String operation;


    public Time(String day, String operation) {
        this.day = day;
        this.operation = operation;
    }

    protected Time(Parcel in) {
        day = in.readString();
        operation = in.readString();
    }

    public static final Creator<Time> CREATOR = new Creator<Time>() {
        @Override
        public Time createFromParcel(Parcel in) {
            return new Time(in);
        }

        @Override
        public Time[] newArray(int size) {
            return new Time[size];
        }
    };

    public String getDay() {
        return day;
    }

    public String getOperation() {
        return operation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(day);
        dest.writeString(operation);
    }
}
