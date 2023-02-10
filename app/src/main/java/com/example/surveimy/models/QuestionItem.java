package com.example.surveimy.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class QuestionItem implements Parcelable {
    private int id;
    private int numberQuestion;
    private int surveyId;
    private int section;
    private String question;
    private String createdAt;
    private String updatedAt;

    private List<OptionItem> optionItemList;

    public QuestionItem() {
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumberQuestion() {
        return numberQuestion;
    }

    public void setNumberQuestion(int number) {
        this.numberQuestion = number;
    }

    public int getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(int surveyId) {
        this.surveyId = surveyId;
    }

    public int getSection() {
        return section;
    }

    public void setSection(int section) {
        this.section = section;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
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

    public List<OptionItem> getOptionItemList() {
        return optionItemList;
    }

    public void setOptionItemList(List<OptionItem> optionItemList) {
        this.optionItemList = optionItemList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(numberQuestion);
        parcel.writeInt(surveyId);
        parcel.writeInt(section);
        parcel.writeString(question);
        parcel.writeString(createdAt);
        parcel.writeString(updatedAt);
    }

    protected QuestionItem(Parcel in) {
        id = in.readInt();
        numberQuestion = in.readInt();
        surveyId = in.readInt();
        section = in.readInt();
        question = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();

    }

    public static final Creator<QuestionItem> CREATOR = new Creator<QuestionItem>() {
        @Override
        public QuestionItem createFromParcel(Parcel in) {
            return new QuestionItem(in);
        }

        @Override
        public QuestionItem[] newArray(int size) {
            return new QuestionItem[size];
        }
    };

    @Override
    public String toString() {
        return this.question;
    }
}
