package com.example.surveimy.models;

public class NotificationItem {
    private int id,kuesionerId,status;
    private String title, subTitle, createdAt;

    public NotificationItem(){}

    public NotificationItem(int id, int kuesionerId, String title, String subTitle, String createdAt) {
        this.kuesionerId = kuesionerId;
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.createdAt = createdAt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getKuesionerId() {
        return kuesionerId;
    }

    public void setKuesionerId(int kuesionerId) {
        this.kuesionerId = kuesionerId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
