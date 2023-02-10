package com.example.surveimy.models;

import android.os.Parcel;
import android.os.Parcelable;

public class SurveyItem implements Parcelable {
    public int id;
    public int surveyId;
    public int reward;
    public int mahasiswaId;
    public int status;
    public int responden;
    public String title;
    public String descreption;
    private String createdAt;
    private String updatedAt;
    private String expiredAt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public int getMahasiswaId() {
        return mahasiswaId;
    }

    public void setMahasiswaId(int mahasiswa) {
        this.mahasiswaId = mahasiswa;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescreption() {
        return descreption;
    }

    public void setDescreption(String descreption) {
        this.descreption = descreption;
    }

    public int getReward() {
        return reward;
    }

    public void setReward(int reward) {
        this.reward = reward;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(String expiredAt) {
        this.expiredAt = expiredAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getResponden() {
        return responden;
    }

    public void setResponden(int responden) {
        this.responden = responden;
    }

    public SurveyItem(){}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(surveyId);
        parcel.writeInt(mahasiswaId);
        parcel.writeInt(reward);
        parcel.writeString(title);
        parcel.writeString(descreption);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
        parcel.writeString(expiredAt);
        parcel.writeInt(status);
        parcel.writeInt(responden);
    }
    protected SurveyItem(Parcel in) {
        id = in.readInt();
        surveyId = in.readInt();
        mahasiswaId = in.readInt();
        reward = in.readInt();
        title = in.readString();
        descreption = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        expiredAt = in.readString();
        status = in.readInt();
        responden = in.readInt();
    }
    public static final Creator<SurveyItem> CREATOR = new Creator<SurveyItem>() {
        @Override
        public SurveyItem createFromParcel(Parcel in) {
            return new SurveyItem(in);
        }

        @Override
        public SurveyItem[] newArray(int size) {
            return new SurveyItem[size];
        }
    };
}
