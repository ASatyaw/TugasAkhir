package com.example.surveimy.datatabase;

public class AppDB {

    public AppDB(){}

    public static final class NotificationTable{
        public static final String TABLE_NAME = "tbl_notification";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE= "title";
        public static final String COLUMN_SUB_TITLE= "sub_title";
        public static final String COLUMN_CREATED= "_created";
        public static final String COLUMN_SURVEY = "_surveyId";
        public static final String COLUMN_STATUS= "status";

        //CREATE TABLE BOOKING
        public static final String SQL_CREATE_TABLE ="CREATE TABLE IF NOT EXISTS "+ TABLE_NAME +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT," +
                COLUMN_SUB_TITLE + " TEXT," +
                COLUMN_CREATED + " TEXT," +
                COLUMN_STATUS + " INTEGER  DEFAULT 0," +
                COLUMN_SURVEY+ " INTEGER )";
    }
}
