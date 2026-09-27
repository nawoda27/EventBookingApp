package com.example.eventbookingapp;

public class Booking {

    private String id;
    private String userId;
    private String userName;
    private String eventId;
    private String eventName;
    private String bookingDate;
    private String status;

    // Required empty constructor for Firestore
    public Booking() {
    }

    // Constructor
    public Booking(
            String userId,
            String userName,
            String eventId,
            String eventName,
            String bookingDate,
            String status) {

        this.userId = userId;
        this.userName = userName;
        this.eventId = eventId;
        this.eventName = eventName;
        this.bookingDate = bookingDate;
        this.status = status;
    }

    // ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // User ID
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // User Name
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Event ID
    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    // Event Name
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    // Booking Date
    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    // Status
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}