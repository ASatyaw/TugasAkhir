package com.example.surveimy.models;


public class HistoryItem {
    private int id,koin,typeHistory;
    private String title,createdAt;

    public HistoryItem(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKoin() {
        return koin;
    }

    public void setKoin(int koin) {
        this.koin = koin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getTypeHistory() {
        return typeHistory;
    }

    public void setTypeHistory(int typeHistory) {
        this.typeHistory = typeHistory;
    }
}
