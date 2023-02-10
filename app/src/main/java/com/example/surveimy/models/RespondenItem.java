package com.example.surveimy.models;

import android.os.Parcel;
import android.os.Parcelable;


public class RespondenItem implements Parcelable {
    private int id;
    private int mahasiswaId;
    private String nim;
    private String username;
    private int surveyId;

    public RespondenItem() {
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(int mahasiswaId) {
        this.mahasiswaId = mahasiswaId;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(mahasiswaId);
        parcel.writeString(nim);
        parcel.writeString(username);
        parcel.writeInt(surveyId);
    }

    protected RespondenItem(Parcel in) {
        id = in.readInt();
        mahasiswaId = in.readInt();
        nim = in.readString();
        username = in.readString();
        surveyId = in.readInt();
    }

    public static final Creator<RespondenItem> CREATOR = new Creator<RespondenItem>() {
        @Override
        public RespondenItem createFromParcel(Parcel in) {
            return new RespondenItem(in);
        }

        @Override
        public RespondenItem[] newArray(int size) {
            return new RespondenItem[size];
        }
    };
}
