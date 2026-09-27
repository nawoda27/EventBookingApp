package com.example.eventbookingapp;

public class Notification {

    private String id;
    private String userId;
    private String date;
    private String message;
    private boolean read;


    public Notification() {
    }


    public Notification(String id, String userId, String date,
                        String message, boolean read) {

        this.id = id;
        this.userId = userId;
        this.date = date;
        this.message = message;
        this.read = read;
    }


    public String getId() {
        return id;
    }


    public String getUserId() {
        return userId;
    }


    public String getDate() {
        return date;
    }


    public String getMessage() {
        return message;
    }


    public boolean isRead() {
        return read;
    }

}
