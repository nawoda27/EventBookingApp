package com.example.eventbookingapp;

public class Event {

    private String id;
    private String eventName;
    private String date;
    private String location;
    private String description;

    // Required empty constructor for Firestore
    public Event() {
    }

    // Constructor
    public Event(
            String eventName,
            String date,
            String location,
            String description) {

        this.eventName = eventName;
        this.date = date;
        this.location = location;
        this.description = description;
    }

    // Event ID
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Event Name
    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    // Date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // Location
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // Description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}